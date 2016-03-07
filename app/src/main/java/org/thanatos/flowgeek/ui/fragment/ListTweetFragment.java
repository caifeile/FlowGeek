package org.thanatos.flowgeek.ui.fragment;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.trello.rxlifecycle.ActivityEvent;
import com.trello.rxlifecycle.FragmentEvent;

import org.thanatos.base.adapter.BaseListAdapter;
import org.thanatos.base.manager.DeviceManager;
import org.thanatos.base.ui.fragment.BaseListFragment;
import org.thanatos.flowgeek.AppManager;
import org.thanatos.flowgeek.R;
import org.thanatos.flowgeek.UIManager;
import org.thanatos.flowgeek.adapter.TweetAdapter;
import org.thanatos.flowgeek.bean.Tweet;
import org.thanatos.flowgeek.event.Events;
import org.thanatos.flowgeek.event.RxBus;
import org.thanatos.flowgeek.presenter.ListTweetPresenter;
import org.thanatos.flowgeek.ui.activity.DetailActivity;
import org.thanatos.flowgeek.ui.activity.TweetDetailActivity;
import org.thanatos.flowgeek.utils.DialogFactory;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import nucleus.factory.RequiresPresenter;
import rx.Subscription;


/**
 * 最新动弹\最热动弹\我的动弹
 */
@RequiresPresenter(ListTweetPresenter.class)
public class ListTweetFragment extends BaseListFragment<Tweet, ListTweetPresenter>
        implements BaseListAdapter.OnItemClickListener, BaseListAdapter.OnItemLongClickListener {

    public static final int TWEET_TYPE_NEW = 0;
    public static final int TWEET_TYPE_HOT = 1;
    public static final int TWEET_TYPE_SELF = 2;

    public static final int STATE_USER_NOT_LOGIN = 4;

    public int mCatalog = TWEET_TYPE_NEW;

    @Bind(R.id.layout_login) RelativeLayout mLoginLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCatalog = getArguments().getInt(BUNDLE_TYPE, TWEET_TYPE_NEW);
        initSubscribers();
    }

    private void initSubscribers() {

        if (mCatalog == TWEET_TYPE_SELF){
            RxBus.with(this)
                    .setEndEvent(FragmentEvent.DESTROY)
                    .setEvent(Events.EventEnum.DELIVER_LOGIN)
                    .onNext((event) -> {
                        mLoginLayout.setVisibility(View.GONE);
                        onLoadingState(BaseListFragment.LOAD_MODE_DEFAULT);
                        getPresenter().requestData(BaseListFragment.LOAD_MODE_DEFAULT, 0);
                    }).create();

            RxBus.with(this)
                    .setEndEvent(FragmentEvent.DESTROY)
                    .setEvent(Events.EventEnum.DELIVER_LOGOUT)
                    .onNext((event) -> {
                        Log.d("thanatosx", "get logout message");
                        mAdapter.clear();
                        mErrorLayout.setVisibility(View.GONE);
                        mSwipeRefreshLayout.setEnabled(false);
                        mLoginLayout.setVisibility(View.VISIBLE);
                        mState = STATE_USER_NOT_LOGIN;
                    }).create();
        }

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        mListView.setPadding(0, setDividerSize(), 0, 0);
        mAdapter.setOnItemClickListener(this);
        mAdapter.setOnItemLongClickListener(this);

        if (!AppManager.isLogined() && mCatalog == TWEET_TYPE_SELF){
                onUserNotLogin();
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_list_tweet, container, false);
    }

    @Override
    protected BaseListAdapter<Tweet> onSetupAdapter() {
        return new TweetAdapter(mContext, BaseListAdapter.ONLY_FOOTER);
    }

    @Override
    public void onItemClick(int position, long id, View view) {
        Intent intent = new Intent(getContext(), TweetDetailActivity.class);
        intent.putExtra(TweetDetailActivity.BUNDLE_KEY_TWEET, mAdapter.getItem(position));
        startActivity(intent);
    }

    @Override
    public void onLongClick(int position, long id, View view) {
        new AlertDialog.Builder(mContext)
                .setAdapter(new BaseAdapter() {
                    @Override
                    public int getCount() {
                        if (AppManager.isLogined() &&
                                AppManager.LOCAL_LOGINED_USER.getUid() == mAdapter.getItem(position).getAuthorId()){
                            return 2;
                        }else return 1;
                    }
                    @Override
                    public Object getItem(int position) { return null; }
                    @Override
                    public long getItemId(int position) { return 0; }
                    @Override
                    public View getView(int position, View convertView, ViewGroup parent) {
                        TextView view = (TextView) LayoutInflater.from(mContext)
                                .inflate(R.layout.list_item_dialog_option, parent, false);
                        switch (position){
                            case 0: // 0 --> 复制
                                view.setText(getResources().getString(R.string.copy));
                                break;

                            case 1: // 1 --> 删除
                                view.setText(getResources().getString(R.string.delete));
                                break;
                        }
                        return view;
                    }
                }, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        switch (which){
                            case 0: // 复制
                                DeviceManager.getClipboardManager(mContext)
                                        .setPrimaryClip(ClipData.newPlainText(
                                                        null, mAdapter.getItem(position).getBody())
                                        );
                                Toast.makeText(mContext, "复制成功!（づ￣ 3￣)づ", Toast.LENGTH_SHORT).show();
                                break;
                            case 1: // 删除
                                new AlertDialog.Builder(mContext, DialogFactory.getFactory().getTheme(mContext))
                                        .setTitle("(#‵′)凸")
                                        .setMessage(getResources().getString(R.string.are_you_sure_delete_the_comment))
                                        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                            }
                                        })
                                        .setPositiveButton(R.string.sure, new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                                onDeleting();
                                                getPresenter().deleteTweet(mAdapter.getItem(position).getId());
                                            }
                                        }).create().show();
                        }
                    }
                })
                .create().show();
    }

    /**
     * 正在删除动弹
     */
    public void onDeleting() {

    }

    /**
     * 在用户没有登录的时候进入我的动弹界面
     */
    public void onUserNotLogin() {
        if (getView()==null) return;
        mAdapter.clear();
        mLoginLayout.setVisibility(View.VISIBLE);
        mErrorLayout.setVisibility(View.GONE);
        mSwipeRefreshLayout.setEnabled(false);
        if (mState == STATE_REFRESHING){
            mSwipeRefreshLayout.setRefreshing(false);
        }

        mState = STATE_USER_NOT_LOGIN;
    }

    @OnClick(R.id.btn_submit) void toLogin(){
        UIManager.jump2login(mContext);
    }

    /**
     * 删除成功
     * @param tid
     */
    public void onDeleteSuccessful(long tid) {
        mAdapter.removeObjectForId(tid);
    }
}
