package org.thanatos.flowgeek.presenter;

import android.os.Bundle;

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

import nucleus.presenter.delivery.Delivery;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.schedulers.Schedulers;

/**
 * Created by thanatos on 15/12/28.
 */
public class DetailPresenter extends ThxPresenter<DetailActivity> {

    @SuppressWarnings("all")
    @Override
    protected void onCreate(Bundle savedState) {
        super.onCreate(savedState);

        // load remote data
        add(Observable.create(subscriber -> {
                    subscriber.onNext(null);
                    subscriber.onCompleted();
                }).compose(this.deliverFirst())
                        .observeOn(Schedulers.io())
                        .flatMap(new Func1<Delivery<DetailActivity, Object>, Observable<Article>>() {
                                     @Override
                                     public Observable<Article> call(Delivery<DetailActivity, Object> delivery) {
                                         switch (getView().getCatalog()) {
                                             case DetailActivity.DISPLAY_NEWS:
                                                 return Observable.zip(
                                                         ServerAPI.getOSChinaAPI().getNewsDetail(getView().getNews().getId()),
                                                         ServerAPI.getOSChinaAPI().getUserInfo(-1, getView().getNews().getAuthorId(), "", 0, 0),
                                                         new Func2<RespNewsDetail, RespUserInfo, Article>() {
                                                             @Override
                                                             public Article call(RespNewsDetail respNewsDetail, RespUserInfo respUserInfo) {
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
                                                             }
                                                         });
                                             case DetailActivity.DISPLAY_BLOG:
                                                 return Observable.zip(
                                                         ServerAPI.getOSChinaAPI().getBlogDetail(getView().getNews().getId()),
                                                         ServerAPI.getOSChinaAPI().getUserInfo(-1, getView().getNews().getAuthorId(), "", 0, 0),
                                                         new Func2<RespBlogDetail, RespUserInfo, Article>() {
                                                             @Override
                                                             public Article call(RespBlogDetail respBlogDetail, RespUserInfo respUserInfo) {
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
                                                             }
                                                         });
                                             case DetailActivity.DISPLAY_SOFTWARE:
                                                 return ServerAPI.getOSChinaAPI().getSoftwareDetail(getView().getNews().getKey())
                                                         .map((detail) -> {
                                                             Article<Software> article = new Article<Software>();
                                                             article.setTitle(detail.getSoftware().getTitle());
                                                             article.setCmmCount(detail.getSoftware().getTweetCount());
                                                             article.setFavorite(detail.getSoftware().getFavorite());
                                                             article.setBody(detail.getSoftware().getBody());
                                                             article.setArticle(detail.getSoftware());
                                                             return article;
                                                         });
                                             case DetailActivity.DISPLAY_POST:
                                                 return ServerAPI.getOSChinaAPI().getPostDetail(getView().getNews().getId())
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
                                             default:
                                                 return Observable.just(null);
                                         }
                                     }
                                 }
                        )
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                (detail) -> {
                                    getView().onLoadFinished(detail);
                                },
                                (error) -> {
                                    error.printStackTrace();
                                    getView().onLoadFailure();
                                })
        );

    }
}
