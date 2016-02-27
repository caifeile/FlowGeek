package org.thanatos.flowgeek.ui.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ClipData;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.trello.rxlifecycle.FragmentEvent;

import org.thanatos.base.adapter.BaseListAdapter;
import org.thanatos.base.manager.DeviceManager;
import org.thanatos.base.ui.fragment.BaseListFragment;
import org.thanatos.flowgeek.AppManager;
import org.thanatos.flowgeek.R;
import org.thanatos.flowgeek.bean.Comment;
import org.thanatos.flowgeek.bean.RespCmmList;
import org.thanatos.flowgeek.event.Events;
import org.thanatos.flowgeek.event.RxBus;
import org.thanatos.flowgeek.presenter.CmmPresenter;
import org.thanatos.flowgeek.ui.activity.DetailActivity;
import org.thanatos.flowgeek.utils.DialogFactory;

/**
 * Created by thanatos on 2/25/16.
 */
public abstract class BaseCmmFragment<P extends CmmPresenter>
        extends BaseListFragment<Comment, P> implements BaseListAdapter.OnItemClickListener, BaseListAdapter.OnItemLongClickListener {

    protected Dialog dialog;
    public int mCatalog;
    public int mRemoteCatalog;
    public long mArticleOwnerId;
    public long mArticleId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initSubscribers();
    }

    private void initSubscribers() {
        // 得到目标类型
        RxBus.with(this)
                .setEndEvent(FragmentEvent.DESTROY)
                .setEvent(Events.EventEnum.DELIVER_ARTICLE_CATALOG_FROM_LIST)
                .onNext((events)->{
                    mCatalog = events.getMessage();
                    switch (mCatalog) {
                        case DetailActivity.DISPLAY_BLOG:
                            break;
                        case DetailActivity.DISPLAY_NEWS:
                            mRemoteCatalog = RespCmmList.CATALOG_NEWS;
                            break;
                        case DetailActivity.DISPLAY_POST:
                            mRemoteCatalog = RespCmmList.CATALOG_POST;
                            break;
                        case DetailActivity.DISPLAY_TWEET:
                            mRemoteCatalog = RespCmmList.CATALOG_TWEET;
                            break;
                        default:
                            Log.d("thanatos", "什么都没有");
                    }
                })
                .onError(Throwable::printStackTrace).create();
        RxBus.getInstance().send(Events.EventEnum.GET_ARTICLE_CATALOG,
                Events.EventEnum.DELIVER_ARTICLE_CATALOG_FROM_LIST);

        // 得到目标id
        RxBus.with(this)
                .setEndEvent(FragmentEvent.DESTROY)
                .setEvent(Events.EventEnum.DELIVER_ARTICLE_ID_FROM_LIST)
                .onNext((events)->{
                    mArticleId = events.getMessage();
                })
                .onError(Throwable::printStackTrace).create();
        RxBus.getInstance().send(Events.EventEnum.GET_ARTICLE_ID,
                Events.EventEnum.DELIVER_ARTICLE_ID_FROM_LIST);

        // 得到文章所属人id
        RxBus.with(this)
                .setEvent(Events.EventEnum.DELIVER_ARTICLE_OWNER_ID)
                .setEndEvent(FragmentEvent.DESTROY)
                .onNext((events ->
                        mArticleOwnerId = events.getMessage()
                )).create();
        RxBus.getInstance().send(Events.EventEnum.GET_ARTICLE_OWNER_ID,
                Events.EventEnum.DELIVER_ARTICLE_OWNER_ID);

        // 发送评论
        RxBus.with(this)
                .setEndEvent(FragmentEvent.DESTROY)
                .setEvent(Events.EventEnum.DELIVER_SEND_COMMENT)
                .onNext((events) -> {
                    Comment comment = events.getMessage();
                    String content = comment.getContent();
                    int type = mCatalog == DetailActivity.DISPLAY_BLOG ? 1 : 0;

                    onPublishing();

                    getPresenter().publicComment(mRemoteCatalog, (int)mArticleId, content,
                            comment.getId().intValue(), comment.getAuthorId(), type);
                })
                .onError(Throwable::printStackTrace).create();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mAdapter.setOnItemClickListener(this);
        mAdapter.setOnItemLongClickListener(this);
    }

    public void onPublishing(){
        dialog = DialogFactory.getFactory()
                .getLoadingDialog(mContext, getResources().getString(R.string.publishing_comment));
        dialog.show();
    }

    public void onDeleting() {
        dialog = DialogFactory.getFactory()
                .getLoadingDialog(mContext, getResources().getString(R.string.deleting_comment));
        dialog.show();
    }

    public void onCmmPublishSuccessful(Comment comment){
        dismissDialog();
        Toast.makeText(mContext, "评论成功~(≧▽≦)/~", Toast.LENGTH_SHORT).show();
        mAdapter.addItem(0, comment);
        RxBus.getInstance().send(Events.EventEnum.DELIVER_CLEAR_IMPUT, null);
    }

    public void onCmmPublishFailed(String message){
        dismissDialog();
        Toast.makeText(mContext, "评论失败 " + message + " O__O\"...", Toast.LENGTH_SHORT).show();
    }

    public void onDeleteCmmSuccessful() {
        dismissDialog();
        Toast.makeText(mContext, "删除成功~(≧▽≦)/~", Toast.LENGTH_SHORT).show();
        onRefresh();
    }

    public void onDeleteCmmFailed(String message) {
        dismissDialog();
        Toast.makeText(mContext, "删除失败 " + message + " O__O\"...", Toast.LENGTH_SHORT).show();
    }



    private void dismissDialog(){
        if (dialog !=null){
            dialog.dismiss();
            dialog = null;
        }
    }

    @Override
    public void onItemClick(int position, long id, View view) {
        Events<Comment> events = Events.just(mAdapter.getItem(position));
        events.what = Events.EventEnum.DELIVER_REPLY_SOMEONE;
        RxBus.getInstance().send(events);
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
                                                null, mAdapter.getItem(position).getContent())
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
                                                getPresenter().deleteComment(mArticleId, mRemoteCatalog,
                                                        mAdapter.getItem(position).getId(),
                                                        mAdapter.getItem(position).getAuthorId(),
                                                        mArticleOwnerId,
                                                        mCatalog == DetailActivity.DISPLAY_BLOG ? 1 : 0);
                                            }
                                        }).create().show();
                        }
                    }
                })
                .create().show();
    }

}
