package org.thanatos.flowgeek.presenter;

import android.os.Bundle;
import android.util.Log;

import com.trello.rxlifecycle.FragmentEvent;

import org.thanatos.base.domain.Page;
import org.thanatos.base.presenter.BaseListPresenter;
import org.thanatos.base.ui.fragment.BaseListFragment;
import org.thanatos.flowgeek.AppManager;
import org.thanatos.flowgeek.ServerAPI;
import org.thanatos.flowgeek.bean.RespCmmList;
import org.thanatos.flowgeek.bean.RespResult;
import org.thanatos.flowgeek.event.Events;
import org.thanatos.flowgeek.event.RxBus;
import org.thanatos.flowgeek.ui.activity.DetailActivity;
import org.thanatos.flowgeek.ui.fragment.BaseCmmFragment;

import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @author thanatos
 * @create 2016-01-05
 **/
public class CmmPresenter extends BaseListPresenter<BaseCmmFragment> {

    private static final int RESTART_REQUEST_REMOTE = 1;

    @Override
    protected void onCreate(Bundle savedState) {
        super.onCreate(savedState);

        this.<Integer, Integer, Integer, Object>restartable(RESTART_REQUEST_REMOTE, (pageIndex, pageSize, mode, o4) -> {
            Observable<RespCmmList> observable;
            if (getView().mCatalog == DetailActivity.DISPLAY_BLOG){
                observable = ServerAPI.getOSChinaAPI().getBlogCmmList(getView().mArticleId, pageIndex, pageSize);
            }else{
                observable = ServerAPI.getOSChinaAPI().getCmmList(getView().mRemoteCatalog, getView().mArticleId, pageIndex, pageSize);
            }
            return observable
                    .subscribeOn(Schedulers.io())
                    .compose(this.<RespCmmList>deliverFirst())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(split(
                            (view, page) -> {
                                page.sortList();
                                view.onLoadResultData(page.getComments());
                                view.onLoadFinishState(mode);
                            },
                            (view, error) -> {
                                error.printStackTrace();
                                if (error instanceof UnknownHostException || error instanceof SocketTimeoutException) {
                                    view.onNetworkInvalid(mode);
                                } else {
                                    view.onLoadErrorState(mode);
                                }
                            }));
        });

        add(afterTakeView().subscribe(this::init, Throwable::printStackTrace));

    }

    private void init(BaseListFragment view) {
        Log.d("thanatosx", "CmmPresenter init()");
        start(RESTART_REQUEST_REMOTE, 0, Page.PAGE_SIZE, BaseListFragment.LOAD_MODE_DEFAULT, null);
    }


    @Override
    public void requestData(int mode, int pageNum) {
        start(RESTART_REQUEST_REMOTE, pageNum, Page.PAGE_SIZE, mode, null);
    }

    /**
     * 发表评论
     * @param catalog 文章类型
     * @param aid 文章id
     * @param content 评论内容
     * @param rid 回复的评论id
     * @param auid 回复的评论的作者的id
     * @param type 类型,主要是为了区别普通评论和博客评论,API不一样,坑爹啊
     */
    public void publicComment(int catalog, int aid, String content, int rid, int auid, int type){
        Log.d("thanatos", "LocalUser id is " + AppManager.LOCAL_LOGINED_USER.getUid());
        Log.d("thanatos", String.format("catalog=%d, aid=%d, content=%s, rid=%s, auid=%s, type=%s",
                catalog, aid, content, rid, auid, type));
        Observable<RespResult> observable;
        switch (type){
            default:  // 普通评论
                if (rid == 0){ // --> 不是回复评论
                    observable = ServerAPI.getOSChinaAPI().publicNormalComment(
                            catalog, aid, AppManager.LOCAL_LOGINED_USER.getUid(), content, 1);
                }else{ // --> 是回复评论
                    observable = ServerAPI.getOSChinaAPI().replyNormalComment(
                            catalog, aid, AppManager.LOCAL_LOGINED_USER.getUid(), content, rid, auid);
                }
                break;
            case 1: // 博客评论
                if (rid == 0){
                    observable = ServerAPI.getOSChinaAPI().publicBlogComment(
                            aid, AppManager.LOCAL_LOGINED_USER.getUid(), content);
                }else{
                    observable = ServerAPI.getOSChinaAPI().replyBlogComment(
                            aid, AppManager.LOCAL_LOGINED_USER.getUid(), content, rid, auid);
                }
        }
        add(observable.subscribeOn(Schedulers.io())
                .compose(this.<RespResult>deliverFirst())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(split(
                        (view, data) -> {
                            if (data.getResult().getErrorCode() == 1) {
                                view.onCmmPublishSuccessful(data.getComment());
                            } else {
                                view.onCmmPublishFailed(data.getResult().getErrorMessage());
                            }
                        },
                        (view, error) -> {
                            error.printStackTrace();
                            view.onCmmPublishFailed("");
                        }
                )));
    }

    /**
     * 删除评论
     * @param aid 文章id
     * @param catalog 文章类型
     * @param rid 评论id
     * @param authorId 评论作者
     * @param mArticleOwnerId 文章作者id
     * @param type 类型, 普通 or 博客
     */
    public void deleteComment(long aid, int catalog, Long rid, int authorId, long mArticleOwnerId, int type) {
        Log.d("thanatos", "LocalUser id is " + AppManager.LOCAL_LOGINED_USER.getUid());
        Log.d("thanatos", String.format("catalog=%d, aid=%d, mArticleOwnerId=%s, rid=%s, auid=%s, type=%s",
                catalog, aid, mArticleOwnerId, rid, authorId, type));
        Observable<RespResult> observable;
        switch (type){
            default:  // 普通评论
                observable = ServerAPI.getOSChinaAPI().deleteNormalComment(aid, catalog, rid, authorId);
            case 1: // 博客评论
                observable = ServerAPI.getOSChinaAPI().deleteBlogComment(
                        AppManager.LOCAL_LOGINED_USER.getUid(), aid, rid, authorId, mArticleOwnerId);
                break;
        }
        add(observable.subscribeOn(Schedulers.io())
                .compose(this.<RespResult>deliverFirst())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(split(
                        (view, data) -> {
                            if (data.getResult().getErrorCode() == 1) {
                                view.onDeleteCmmSuccessful();
                            } else {
                                view.onDeleteCmmFailed(data.getResult().getErrorMessage());
                            }
                        },
                        (view, error) -> {
                            error.printStackTrace();
                            view.onDeleteCmmFailed("");
                        }
                )));
    }
}
