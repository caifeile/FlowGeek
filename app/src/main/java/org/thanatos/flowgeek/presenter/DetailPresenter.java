package org.thanatos.flowgeek.presenter;

import android.os.Bundle;
import android.util.Log;

import org.thanatos.flowgeek.ServerAPI;
import org.thanatos.flowgeek.bean.Article;
import org.thanatos.flowgeek.bean.Blog;
import org.thanatos.flowgeek.bean.News;
import org.thanatos.flowgeek.bean.Post;
import org.thanatos.flowgeek.bean.RespBlogDetail;
import org.thanatos.flowgeek.bean.RespNewsDetail;
import org.thanatos.flowgeek.bean.RespSoftwareDetail;
import org.thanatos.flowgeek.bean.RespUserInfo;
import org.thanatos.flowgeek.bean.Software;
import org.thanatos.flowgeek.ui.activity.DetailActivity;

import nucleus.presenter.RxPresenter;
import nucleus.presenter.delivery.Delivery;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.schedulers.Schedulers;

/**
 * Created by thanatos on 15/12/28.
 */
public class DetailPresenter extends RxPresenter<DetailActivity> {


    @SuppressWarnings("all")
    @Override
    protected void onCreate(Bundle savedState) {
        super.onCreate(savedState);

        // load remote data
        add(afterTakeView()
                        .observeOn(Schedulers.io())
                        .flatMap((view) -> {
                            switch (view.getCatalog()) {
                                case DetailActivity.DISPLAY_NEWS:
                                    return requestNewsDetail(view);

                                case DetailActivity.DISPLAY_BLOG:
                                    return requestBlogDetail(view);

                                case DetailActivity.DISPLAY_SOFTWARE:
                                    return requestSoftwareDetail(view);

                                case DetailActivity.DISPLAY_POST:
                                    return requestPostDetail(view);

                                default:
                                    return Observable.just(null);
                            }
                        })
                        .filter((article -> article != null))
                        .compose(this.<Article>deliverFirst())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(split(
                                        (view, detail) -> {
                                            view.onLoadFinished(detail);
                                        },
                                        (view, error) -> {
                                            error.printStackTrace();
                                            view.onLoadFailure();
                                        })
                        )
        );
    }

    /**
     * 请求问答帖子详情
     * @param view
     * @return
     */
    private Observable<Article> requestPostDetail(DetailActivity view) {
        return ServerAPI.getOSChinaAPI().getPostDetail(view.getNews().getId())
                .map((detail) -> {
                    Article<Post> article = new Article<Post>();
                    article.setId(detail.getPost().getId());
                    article.setAuthor(detail.getPost().getAuthor());
                    article.setAuthorId((long) detail.getPost().getAuthorId());
                    article.setBody(detail.getPost().getBody());
                    article.setFavorite(detail.getPost().getFavorite());
                    article.setCmmCount(detail.getPost().getAnswerCount());
                    article.setTitle(detail.getPost().getTitle());
                    article.setPortrait(detail.getPost().getPortrait());
                    article.setPubDate(detail.getPost().getPubDate());
                    article.setArticle(detail.getPost());
                    return article;
                });
    }

    /**
     * 请求软件按详情
     * @param view
     * @return
     */
    private Observable<Article> requestSoftwareDetail(DetailActivity view) {
        return ServerAPI.getOSChinaAPI().getSoftwareDetail(view.getNews().getKey())
                .map((detail) -> {
                    Article<Software> article = new Article<Software>();
                    article.setTitle(detail.getSoftware().getTitle());
                    article.setCmmCount(detail.getSoftware().getTweetCount());
                    article.setFavorite(detail.getSoftware().getFavorite());
                    article.setBody(detail.getSoftware().getBody());
                    article.setArticle(detail.getSoftware());
                    return article;
                });
    }

    /**
     * 请求博客详情
     * @param view
     * @return
     */
    private Observable<Article> requestBlogDetail(DetailActivity view) {
        return Observable.zip(
                ServerAPI.getOSChinaAPI().getBlogDetail(view.getNews().getId()),
                ServerAPI.getOSChinaAPI().getUserInfo(null, view.getNews().getAuthorId(), "", 0, 0),
                (respBlogDetail, respUserInfo) -> {
                    Article<Blog> article = new Article<Blog>();
                    article.setId(respBlogDetail.getBlog().getId());
                    article.setAuthor(respBlogDetail.getBlog().getAuthor());
                    article.setAuthorId((long) respBlogDetail.getBlog().getAuthorId());
                    article.setBody(respBlogDetail.getBlog().getBody());
                    article.setFavorite(respBlogDetail.getBlog().getFavorite());
                    article.setCmmCount(respBlogDetail.getBlog().getCmmCount());
                    article.setTitle(respBlogDetail.getBlog().getTitle());
                    article.setPortrait(respUserInfo.getUser().getPortrait());
                    article.setPubDate(respBlogDetail.getBlog().getPubDate());
                    article.setArticle(respBlogDetail.getBlog());
                    return article;
                });
    }

    private Observable<Article> requestNewsDetail(DetailActivity view) {
        return Observable.zip(
                ServerAPI.getOSChinaAPI().getNewsDetail(view.getNews().getId()),
                ServerAPI.getOSChinaAPI().getUserInfo(null, view.getNews().getAuthorId(), "", 0, 0),
                (respNewsDetail, respUserInfo) -> {
                    Article<News> article = new Article<News>();
                    article.setId(respNewsDetail.getNews().getId());
                    article.setAuthor(respNewsDetail.getNews().getAuthor());
                    article.setAuthorId((long) respNewsDetail.getNews().getAuthorId());
                    article.setBody(respNewsDetail.getNews().getBody());
                    article.setFavorite(respNewsDetail.getNews().getFavorite());
                    article.setCmmCount(respNewsDetail.getNews().getCommentCount());
                    article.setTitle(respNewsDetail.getNews().getTitle());
                    article.setPortrait(respUserInfo.getUser().getPortrait());
                    article.setPubDate(respNewsDetail.getNews().getPubDate());
                    article.setArticle(respNewsDetail.getNews());
                    return article;
                });
    }
}
