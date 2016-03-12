package org.thanatos.flowgeek;

import android.content.SharedPreferences;
import android.util.Log;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import com.squareup.picasso.Picasso;

import org.simpleframework.xml.convert.AnnotationStrategy;
import org.simpleframework.xml.core.Persister;
import org.thanatos.base.model.SharePreferenceManager;
import org.thanatos.base.model.SharePreferenceManager.LocalUser;
import org.thanatos.flowgeek.bean.RespBlogDetail;
import org.thanatos.flowgeek.bean.RespCmmList;
import org.thanatos.flowgeek.bean.RespNewsDetail;
import org.thanatos.flowgeek.bean.NewsList;
import org.thanatos.flowgeek.bean.RespPostDetail;
import org.thanatos.flowgeek.bean.RespResult;
import org.thanatos.flowgeek.bean.RespSoftwareDetail;
import org.thanatos.flowgeek.bean.RespTweetList;
import org.thanatos.flowgeek.bean.RespUser;
import org.thanatos.flowgeek.bean.RespUserInfo;
import org.thanatos.flowgeek.bean.User;

import java.io.File;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import retrofit.Call;
import retrofit.Retrofit;
import retrofit.RxJavaCallAdapterFactory;
import retrofit.SimpleXmlConverterFactory;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.Part;
import retrofit.http.Query;
import rx.Observable;

/**
 * Created by thanatos on 15/12/22.
 */
public class ServerAPI {

    private static OSChinaAPI osChinaAPI;
    private static String cookies;

    public static OSChinaAPI getOSChinaAPI() {
        if (osChinaAPI == null) {
            OkHttpClient httpClient = new OkHttpClient();

            httpClient.interceptors().add(chain -> {
                if (chain.request().url().getPath().equals("/action/api/login_validate")){
                    Response response = chain.proceed(chain.request());
                    String cookies = response.header("Set-Cookie");
                    Log.d("thanatos", "Cookie is " + cookies);
                    SharedPreferences.Editor editor = SharePreferenceManager
                            .getLocalUser(AppManager.context).edit();
                    editor.putString(LocalUser.KEY_COOKIES, cookies);
                    editor.apply();
                    clearCookies();
                    return response;
                }else{
                    return chain.proceed(chain.request());
                }
            });

            httpClient.setConnectTimeout(4, TimeUnit.MINUTES);

            httpClient.interceptors().add(chain -> {
                Request original = chain.request();
                Request request = original.newBuilder()
                        .header("Accept-Language", Locale.getDefault().toString())
                        .header("Host", "www.oschina.net")
                        .header("Connection", "Keep-Alive")
                        .header("Cookie", cookies == null ? getCookies() : cookies)
                        .header("User-Agent", getUserAgent())
                        .build();
                Log.d("thanatos", "The Cookie is " + request.header("Cookie"));
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

    private static String getCookies(){
        if (cookies == null) {
            SharedPreferences preferences = SharePreferenceManager.getLocalUser(AppManager.context);
            return cookies = preferences.getString(LocalUser.KEY_COOKIES, "");
        }
        return cookies;
    }

    public static void clearCookies(){
        cookies = null;
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
        Observable<RespUser> login(
                @Query("username") String username,
                @Query("pwd") String password,
                @Query("keep_login") int keepLogin
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
                @Query("uid") Long uid,
                @Query("hisuid") long hid,
                @Query("hisname") String hname,
                @Query("pageIndex") int pageIndex,
                @Query("pageSize") int pageSize
        );

        /**
         * 得到自己的信息
         * @param uid
         * @return
         */
        @GET("/action/api/my_information")
        Observable<RespUser> getSelfInfo(
                @Query("uid") long uid
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
                @Query("uid") long uid,
                @Query("pageIndex") int pageIndex,
                @Query("pageSize") int pageSize
        );

        @Multipart
        @POST("/action/api/tweet_pub")
        Call<RespResult> publicTweet(
                @Part("uid") RequestBody uid,
                @Part("msg") RequestBody message,
                @Part("img\"; filename=\"image.png\" ") RequestBody image,
                @Part("amr") RequestBody voice);

        @FormUrlEncoded
        @POST("/action/api/tweet_delete")
        Call<RespResult> deleteTweet(
                @Field("uid") long uid,
                @Field("tweetid") long tid
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

        @FormUrlEncoded
        @POST("/action/api/comment_pub")
        Observable<RespResult> publicNormalComment(
                @Field("catalog") int catalog,
                @Field("id") long aid,
                @Field("uid") long uid,
                @Field("content") String content,
                @Field("isPostToMyZone") int sure
        );

        @FormUrlEncoded
        @POST("/action/api/comment_reply")
        Observable<RespResult> replyNormalComment(
                @Field("catalog") int catalog,
                @Field("id") long aid,
                @Field("uid") long uid,
                @Field("content") String content,
                @Field("replyid") int rid,
                @Field("authorid") int auid
        );

        @FormUrlEncoded
        @POST("/action/api/blogcomment_pub")
        Observable<RespResult> publicBlogComment(
                @Field("blog") long aid,
                @Field("uid") long uid,
                @Field("content") String content
        );

        @FormUrlEncoded
        @POST("/action/api/blogcomment_pub")
        Observable<RespResult> replyBlogComment(
                @Field("blog") long aid,
                @Field("uid") long uid,
                @Field("content") String content,
                @Field("reply_id") int rid,
                @Field("objuid") int auid
        );

        @FormUrlEncoded
        @POST("/action/api/comment_delete")
        Observable<RespResult> deleteNormalComment(
                @Field("id") long aid,
                @Field("catalog") int catalog,
                @Field("replyid") long rid,
                @Field("authorid") long auid
        );

        @FormUrlEncoded
        @POST("/action/api/blogcomment_delete")
        Observable<RespResult> deleteBlogComment(
                @Field("uid") long uid,
                @Field("blogid") long bid,
                @Field("replyid") long rid,
                @Field("authorid") long auid,
                @Field("owneruid") long oid
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
