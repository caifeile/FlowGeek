package org.thanatos.flowgeek.ui.fragment;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;
import org.thanatos.flowgeek.DeviceManager;
import org.thanatos.flowgeek.R;
import org.thanatos.flowgeek.adapter.BaseListAdapter;
import org.thanatos.flowgeek.bean.Entity;
import org.thanatos.flowgeek.bean.Page;
import org.thanatos.flowgeek.presenter.BaseListPresenter;
import org.thanatos.flowgeek.ui.view.ErrorLayout;
import java.util.Iterator;
import java.util.List;
import butterknife.Bind;
import butterknife.ButterKnife;
import nucleus.view.NucleusSupportFragment;

/**
 * Created by thanatos on 15-7-21.
 */
public abstract class BaseListFragment<T extends Entity, P extends BaseListPresenter> extends ThxSupportFragment<P>
        implements SwipeRefreshLayout.OnRefreshListener,ErrorLayout.OnActiveClickListener, BaseListAdapter.OnLoadingListener {

    @Bind(R.id.swipe_refresh) protected SwipeRefreshLayout mSwipeRefreshLayout;
    @Bind(R.id.list_view) protected RecyclerView mListView;
    @Bind(R.id.error_frame) ErrorLayout mErrorLayout;

    protected int mCurrentPage = 0;
    protected BaseListAdapter<T> mAdapter;

    public static final int STATE_NONE = 0;
    public static final int STATE_REFRESHING = 1;
    public static final int STATE_PRESSING = 2;// 正在下拉但还没有到刷新的状态

    public static final int LOAD_MODE_DEFAULT = 1;
    public static final int LOAD_MODE_UP_DRAG = 2;
    public static final int LOAD_MODE_CACHE = 3;

    public int mState = STATE_NONE;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_universal_list, container, false);
    }

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ButterKnife.bind(this, view);

        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(
                R.color.swipe_refresh_first, R.color.swipe_refresh_second,
                R.color.swipe_refresh_third, R.color.swipe_refresh_four);

        mErrorLayout.setOnActiveClickListener(this);

        mListView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        mListView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(getActivity())
                .color(getResources().getColor(R.color.divider))
                .size(getResources().getDimensionPixelSize(R.dimen.divider_height))
                .build());

        if (mAdapter!=null){
            mListView.setAdapter(mAdapter);
        }else{
            mAdapter = onSetupAdapter();
            mListView.setAdapter(mAdapter);
            mAdapter.setOnLoadingListener(this);
            mErrorLayout.setState(ErrorLayout.LOADING);
        }
    }

    /**
     * ListView的适配器
     * @return
     */
    protected abstract BaseListAdapter<T> onSetupAdapter();

    /**
     * 请求成功，读取缓存成功，加载数据
     * @param result
     */
    public void onLoadResultData(List<T> result) {
        if (result==null) return;
        Log.d("thanatos", "onLoadResultData - result's size = " + result.size());

        if (mCurrentPage == 0)
            mAdapter.clear();

        if (mAdapter.getDataSize()+result.size() == 0){
            mErrorLayout.setState(ErrorLayout.EMPTY_DATA);
            mAdapter.setState(BaseListAdapter.STATE_HIDE);
        }else if (result.size() < Page.PAGE_SIZE ){
            mAdapter.setState(BaseListAdapter.STATE_NO_MORE);
        }else{
            Iterator<T> iterator = result.iterator();
            final List<T> data = mAdapter.getDataSet();
            while (iterator.hasNext()){
                T obj = iterator.next();
                for (int i=0; i<data.size(); i++){
                    if (data.get(i).getId().equals(obj.getId())){
                        iterator.remove();
                        break;
                    }
                }
            }
            if (mCurrentPage == 0)
                mAdapter.addItems(0, result);
            else
                mAdapter.addItems(result);
            mAdapter.setState(BaseListAdapter.STATE_LOAD_MORE);
        }
    }


    /**
     * 触发下拉刷新事件
     */
    @Override
    public void onRefresh() {
        if (mState == STATE_REFRESHING)
            return;
        if (!DeviceManager.hasInternet()){
            onNetworkInvalid(LOAD_MODE_DEFAULT);
            return;
        }
        onLoadingState();
        getPresenter().requestData(LOAD_MODE_DEFAULT, 0);
    }

    /**
     * 加载完成!
     */
    public void onLoadFinishState(int mode){
        switch (mode){
            case LOAD_MODE_DEFAULT:
                mErrorLayout.setState(ErrorLayout.HIDE);
                mSwipeRefreshLayout.setRefreshing(false);
                mSwipeRefreshLayout.setEnabled(true);
                mState = STATE_NONE;
                break;
            case LOAD_MODE_UP_DRAG:
                Log.d("thanatos", "onLoadFinishState");
//                onLoadResultData was already deal with the state
//                mAdapter.setState(BaseListAdapter.STATE_LOAD_MORE);
                break;
            case LOAD_MODE_CACHE:
                mErrorLayout.setState(ErrorLayout.HIDE);
                break;
        }

    }

    /**
     * when load data broken
     */
    public void onLoadErrorState(int mode) {
        switch (mode){
            case LOAD_MODE_DEFAULT:
                mErrorLayout.setState(ErrorLayout.LOAD_FAILED);
                mSwipeRefreshLayout.setRefreshing(false);
                mSwipeRefreshLayout.setEnabled(false);
                mState = STATE_NONE;
                break;
            case LOAD_MODE_UP_DRAG:
                mAdapter.setState(BaseListAdapter.STATE_LOAD_ERROR);
                break;
        }

    }

    /**
     * 再错误页面点击重新加载
     */
    @Override
    public void onLoadActiveClick() {
        mErrorLayout.setState(ErrorLayout.LOADING);
        getPresenter().requestData(LOAD_MODE_DEFAULT, 0);
    }

    /**
     * when there has not valid network
     */
    public void onNetworkInvalid(int mode){
        switch (mode){
            case LOAD_MODE_DEFAULT:
                if(mAdapter==null || mAdapter.getDataSize()==0){
                    mErrorLayout.setState(ErrorLayout.NOT_NETWORK);
                    mSwipeRefreshLayout.setRefreshing(false);
                    mSwipeRefreshLayout.setEnabled(false);
                }else{
                    mSwipeRefreshLayout.setRefreshing(false);
                }
                break;
            case LOAD_MODE_UP_DRAG:
                mAdapter.setState(BaseListAdapter.STATE_INVALID_NETWORK);
                break;
        }
    }

    /**
     * 顶部加载状态
     */
    public void onLoadingState() {
        if (mSwipeRefreshLayout != null) {
            mSwipeRefreshLayout.setRefreshing(true);
            mState = STATE_REFRESHING;
            mSwipeRefreshLayout.setEnabled(false);
        }
    }

    /**
     * 设置是否可刷新
     * @param refreshable 是否可刷新
     */
    public void setRefreshable(boolean refreshable){
        mSwipeRefreshLayout.setEnabled(refreshable);
    }

    /**
     * 上拉加载
     */
    @Override
    public void onLoading() {
        if (mState == STATE_REFRESHING){
            mAdapter.setState(BaseListAdapter.STATE_REFRESHING);
            return;
        }
        mCurrentPage++;
        mAdapter.setState(BaseListAdapter.STATE_LOADING);
        Log.d("thanatos", "change adapter state");
        getPresenter().requestData(LOAD_MODE_UP_DRAG, mCurrentPage);
    }

    /**
     * 在消除订阅者的时候解除刷新
     */
    public void dismissRefreshing() {
        if (mState == STATE_REFRESHING){
            mSwipeRefreshLayout.setRefreshing(false);
            mSwipeRefreshLayout.setEnabled(true);
        }
    }
}
