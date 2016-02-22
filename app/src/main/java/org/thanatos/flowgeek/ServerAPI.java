package org.thanatos.flowgeek;

import android.util.Log;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;

import org.simpleframework.xml.convert.AnnotationStrategy;
import org.simpleframework.xml.core.Persister;
import org.thanatos.flowgeek.bean.RespBlogDetail;
import org.thanatos.flowgeek.bean.RespCmmList;
import org.thanatos.flowgeek.bean.RespNewsDetail;
import org.thanatos.flowgeek.bean.NewsList;
import org.thanatos.flowgeek.bean.RespPostDetail;
import org.thanatos.flowgeek.bean.RespSoftwareDetail;
import org.thanatos.flowgeek.bean.RespTweetList;
import org.thanatos.flowgeek.bean.RespUserInfo;
import org.thanatos.flowgeek.bean.User;

import java.util.Locale;

import retrofit.Retrofit;
import retrofit.RxJavaCallAdapterFactory;
import retrofit.SimpleXmlConverterFactory;
import retrofit.http.Field;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Query;
import rx.Observable;

/**
 * Created by thanatos on 15/12/22.
 */
public class ServerAPI {

    private static OSChinaAPI osChinaAPI;

    public static OSChinaAPI getOSChinaAPI() {
        if (osChinaAPI == null) {
            OkHttpClient httpClient = new OkHttpClient();
            httpClient.interceptors().add(chain -> {
                Request original = chain.request();
                Request request = original.newBuilder()
                        .header("Accept-Language", Locale.getDefault().toString())
                        .header("Host", "www.oschina.net")
                        .header("Connection", "Keep-Alive")
                        .header("User-Agent", getUserAgent())
                        .build();

                Log.d("thanatos", "访问网络地址: " + request.urlString());

                return chain.proceed(request);
            });

            osChinaAPI = new Retrofit.Builder()
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .addConverterFactory(SimpleXmlConverterFactory.create(new Persister(new AnnotationStrategy())))
                    .baseUrl("http://www.oschina.net/")
                    .client(httpClient)
                    .build()
                    .create(OSChinaAPI.class);
        }
        return osChinaAPI;
    }

    public static String getUserAgent() {
        return new StringBuilder("OSChina.NET")
                .append('/' + AppManager.getPackageInfo().versionName + '_' + AppManager.getPackageInfo().versionCode)// app版本信息
                .append("/Android")// 手机系统平台
                .append("/" + android.os.Build.VERSION.RELEASE)// 手机系统版本
                .append("/" + android.os.Build.MODEL) // 手机型号
                .append("/" + "WhenYouSawIt,Well!Bingo!!") // 客户端唯一标识
                .toString();
    }


    public interface OSChinaAPI {
        // ------------ 用户api -------------

        /**
         * 登陆
         *
         * @param username  username
         * @param password  password, whether it encoded by md5 or not
         * @param keepLogin keep login, does it mean the session remember user login?
         * @return
         */
        @POST("/action/api/login_validate")
        Observable<Object> login(
                @Field("username") String username,
                @Field("pwd") String password,
                @Field("keep_login") int keepLogin
        );

        /**
         * 得到用户的信息,这个api回连同用户的动态也一起得到
         * @param uid user id
         * @param hid his id
         * @param hname his name
         * @param pageIndex page index
         * @param pageSize page size
         * @return
         */
        @GET("/action/api/user_information")
        Observable<RespUserInfo> getUserInfo(
                @Query("uid") long uid,
                @Query("hisuid") long hid,
                @Query("hisname") String hname,
                @Query("pageIndex") int pageIndex,
                @Query("pageSize") int pageSize
        );

        // ------------- 新闻api --------------

        /**
         * 获得资讯数据
         *
         * @param catalog  [1-all, 2-synthesis, 3-software upgrade]
         * @param pageNum  pageNum
         * @param pageSize pageSize
         * @return
         */
        @GET("action/api/news_list")
        Observable<NewsList> getNewsList(
                @Query("catalog") int catalog,
                @Query("pageIndex") int pageNum,
                @Query("pageSize") int pageSize,
                @Query("show") String show
        );

        /**
         * 新闻详情
         *
         * @param id news's id
         * @return
         */
        @GET("/action/api/news_detail")
        Observable<RespNewsDetail> getNewsDetail(@Query("id") long id);

        // -------------- 博客api ---------------

        /**
         * 博客列表
         *
         * @param pageNum  page index
         * @param pageSize page size
         * @return
         */
        @GET("/action/api/blog_list")
        Observable<Object> getBlogList(
                @Query("pageIndex") int pageNum,
                @Query("pageSize") int pageSize
        );

        /**
         * 博客详情
         * @param id 博客id
         * @return
         */
        @GET("/action/api/blog_detail")
        Observable<RespBlogDetail> getBlogDetail(
                @Query("id") long id
        );


        // -------------- 帖子api ---------------

        /**
         *  帖子详情
         * @param id 帖子id
         * @return
         */
        @GET("/action/api/post_detail")
        Observable<RespPostDetail> getPostDetail(
                @Query("id") long id
        );


        // -------------- 动弹api ---------------
        @GET("/action/api/tweet_list")
        Observable<RespTweetList> getTweetList(
                @Query("uid") int uid,
                @Query("pageIndex") int pageIndex,
                @Query("pageSize") int pageSize
        );


        // -------------- 评论api ---------------
        @GET("/action/api/comment_list")
        Observable<RespCmmList> getCmmList(
                @Query("catalog") int catalog,
                @Query("id") long id,
                @Query("pageIndex") int pageIndex,
                @Query("pageSize") int pageSize
        );

        @GET("/action/api/blogcomment_list")
        Observable<RespCmmList> getBlogCmmList(
                @Query("id") long id,
                @Query("pageIndex") int pageIndex,
                @Query("pageSize") int pageSize
        );

        // -------------- 收藏api ---------------


        // -------------- 软件api ---------------
        /**
         * 软件详情
         * @param identify 软件的标识
         * @return
         */
        @GET("/action/api/software_detail")
        Observable<RespSoftwareDetail> getSoftwareDetail(
                @Query("ident") String identify
        );


        // -------------- 私信api ---------------
        // -------------- 搜索api ---------------
        // -------------- 通知api ---------------

        /**
         * 博客列表
         *
         * @param type     the new, the recommend
         * @param pageNum  page index
         * @param pageSize page size
         * @return
         */
        @GET("/action/api/blog_list")
        Observable<Object> getBlogList(
                @Query("type") int type,
                @Query("pageIndex") int pageNum,
                @Query("pageSize") int pageSize
        );


    }


}
