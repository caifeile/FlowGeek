package org.thanatos.flowgeek;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import org.thanatos.flowgeek.bean.Article;
import org.thanatos.flowgeek.bean.News;
import org.thanatos.flowgeek.bean.User;
import org.thanatos.flowgeek.ui.activity.CmmActivity;
import org.thanatos.flowgeek.ui.activity.DetailActivity;
import org.thanatos.flowgeek.ui.activity.LoginActivity;
import org.thanatos.flowgeek.ui.activity.MainActivity;
import org.thanatos.flowgeek.ui.activity.TweetPublishActivity;
import org.thanatos.flowgeek.ui.activity.UserHomeActivity;
import org.thanatos.flowgeek.ui.fragment.ListTweetFragment;
import org.thanatos.flowgeek.utils.URLUtils;
import org.thanatos.base.utils.Utilities;

/**
 * Created by aoyolo on 15/12/28.
 */
public class UIManager {

    // 链接样式文件，代码块高亮的处理
    public final static String WEB_STYLE = "<script type=\"text/javascript\" src=\"file:///android_asset/shCore.js\"></script>"
            + "<script type=\"text/javascript\" src=\"file:///android_asset/brush.js\"></script>"
            + "<script type=\"text/javascript\" src=\"file:///android_asset/client.js\"></script>"
            + "<script type=\"text/javascript\" src=\"file:///android_asset/detail_page.js\"></script>"
            + "<script type=\"text/javascript\">SyntaxHighlighter.all();</script>"
            + "<script type=\"text/javascript\">function showImagePreview(var url){window.location.url= url;}</script>"
            + "<link rel=\"stylesheet\" type=\"text/css\" href=\"file:///android_asset/shThemeDefault.css\">"
            + "<link rel=\"stylesheet\" type=\"text/css\" href=\"file:///android_asset/shCore.css\">"
            + "<link rel=\"stylesheet\" type=\"text/css\" href=\"file:///android_asset/common.css\">";

    public static final String WEB_LOAD_IMAGES = "<script type=\"text/javascript\"> var allImgUrls = getAllImgSrc(document.body.innerHTML);</script>";


    private static final java.lang.String SHOW_IMAGE_DATA = "ima-api:action=showImage&data=";

    /**
     * 解析新闻类型,跳转到相应的Activity
     *
     * @param news
     */
    public static void redirectNewsActivity(Context context, News news) {
        String url = news.getUrl();

        Article article = new Article();
        article.setId(news.getId());
        article.setAuthor(news.getAuthor());
        article.setAuthorId((long) news.getAuthorId());
        article.setTitle(news.getTitle());
        article.setPubDate(news.getPubDate());
        article.setCmmCount(news.getCommentCount());

        // 如果是活动详情
        String eventUrl = news.getNewType().getEventUrl();
        if (!Utilities.isEmpty(eventUrl)) {
            article.setId(Utilities.toLong(news.getNewType().getAttachment(), 0L));
            showEventDetail(context, article);
            return;
        }
        // 如果不需要使用网页打开
        if (Utilities.isEmpty(url)) {
            int newsType = news.getNewType().getType();
            String attchId = news.getNewType().getAttachment();
            switch (newsType) {
                // 新闻详情
                case News.NEWS_TYPE_NEWS:
                    showNewsDetail(context, article);
                    break;
                // 软件详情
                case News.NEWS_TYPE_SOFTWARE:
                    article.setKey(attchId);
                    showSoftwareDetail(context, article);
                    break;
                // 技术问答里面的帖子详情
                case News.NEWS_TYPE_POST:
                    article.setId(Utilities.toLong(attchId, 1L));
                    showPostDetail(context, article);
                    break;
                // 博客详情
                case News.NEWS_TYPE_BLOG:
                    article.setId(Utilities.toLong(attchId, 1L));
                    showBlogDetail(context, article);
                    break;
                default:
                    break;
            }
        } else {
            redirectURLActivity(context, url, article);
        }
    }

    /**
     * 解析URL,跳转到相应的Activity上
     *
     * @param url
     */
    public static void redirectURLActivity(Context context, String url, Article article) {
        if (url == null)
            return;

        // 这是什么鬼? 城市活动?
        if (url.contains("city.oschina.net/")) {
            article.setId(Utilities.toLong(url.substring(url.lastIndexOf('/') + 1), 1));
            showEventDetail(context, article);
            return;
        }

        // 图片预览? 这样搞我也是醉了
        if (url.startsWith(SHOW_IMAGE_DATA)) {
            String realUrl = url.substring(SHOW_IMAGE_DATA.length());
            try {
                JSONObject json = new JSONObject(realUrl);
                int idx = json.optInt("index");
                String[] urls = json.getString("urls").split(",");
                showImagePreview(context, idx, urls);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return;
        }

        URLUtils urls = URLUtils.parseURL(url);
        if (urls != null) {
            showLinkRedirect(context, urls.getObjType(), urls.getObjId(), urls.getObjKey(), article);
        } else {
            openBrowser(context, url);
        }
    }

    public static void showLinkRedirect(Context context, int type, int id, String key, Article article) {
        article.setId((long) id);
        switch (type) {
            case URLUtils.URL_OBJ_TYPE_NEWS:
                showNewsDetail(context, article);
                break;
            case URLUtils.URL_OBJ_TYPE_QUESTION:
                showPostDetail(context, article);
                break;
            case URLUtils.URL_OBJ_TYPE_QUESTION_TAG:
                showPostListByTag(context, key);
                break;
            case URLUtils.URL_OBJ_TYPE_SOFTWARE:
                article.setKey(key);
                showSoftwareDetail(context, article);
                break;
            case URLUtils.URL_OBJ_TYPE_ZONE:
                showUserCenter(context, id, key);
                break;
            case URLUtils.URL_OBJ_TYPE_TWEET:
                showTweetDetail(context, article);
                break;
            case URLUtils.URL_OBJ_TYPE_BLOG:
                showBlogDetail(context, article);
                break;
            case URLUtils.URL_OBJ_TYPE_OTHER:
                openBrowser(context, key);
                break;
            case URLUtils.URL_OBJ_TYPE_TEAM:
                openSysBrowser(context, key);
                break;
            case URLUtils.URL_OBJ_TYPE_GIT:
                openSysBrowser(context, key);
                break;
        }
    }

    public static void openBrowser(Context context, String url) {
        if (Utilities.isImgUrl(url)) {
            showImagePreview(context, 0, new String[]{url});
            return;
        }

        if (url.startsWith("http://www.oschina.net/tweet-topic/")) {
            Toast.makeText(context, "simple back", Toast.LENGTH_SHORT).show();
            return;
        }
        openSysBrowser(context, url);
    }

    public static void openSysBrowser(Context context, String url) {
        Uri uri = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        if (intent.resolveActivity(context.getPackageManager()) != null)
            context.startActivity(intent);
        else
            Toast.makeText(context, "没有什么东西可以打开它的", Toast.LENGTH_SHORT).show();
    }

    public static void showTweetDetail(Context context, Article tweet) {
        Toast.makeText(context, "动弹详情", Toast.LENGTH_SHORT).show();
       /* Intent intent = new Intent(context, DetailActivity.class);
        intent.putExtra("tweetId", id);
        intent.putExtra("tweet", tweet);
        intent.putExtra(DetailActivity.BUNDLE_KEY_DISPLAY_TYPE, DetailActivity.DISPLAY_TWEET);
        context.startActivity(intent);*/
    }

    public static void showUserCenter(Context context, int id, String key) {
        Toast.makeText(context, "用户中心", Toast.LENGTH_SHORT).show();
    }

    public static void showPostListByTag(Context context, String key) {
        Toast.makeText(context, "相关Tag帖子列表", Toast.LENGTH_SHORT).show();
    }

    public static void showImagePreview(Context context, int idx, String[] urls) {
        Toast.makeText(context, "图片预览", Toast.LENGTH_SHORT).show();
    }

    public static void showBlogDetail(Context context, Article blog) {
        Intent intent = new Intent(context, DetailActivity.class);
        intent.putExtra(DetailActivity.BUNDLE_KEY_DISPLAY_TYPE, DetailActivity.DISPLAY_BLOG);
        intent.putExtra(DetailActivity.BUNDLE_KEY_NEWS_OBJECT, blog);
        context.startActivity(intent);
    }

    public static void showPostDetail(Context context, Article post) {
        Intent intent = new Intent(context, DetailActivity.class);
        intent.putExtra(DetailActivity.BUNDLE_KEY_DISPLAY_TYPE, DetailActivity.DISPLAY_POST);
        intent.putExtra(DetailActivity.BUNDLE_KEY_NEWS_OBJECT, post);
        context.startActivity(intent);
    }

    public static void showSoftwareDetail(Context context, Article software) {
        Intent intent = new Intent(context, DetailActivity.class);
        intent.putExtra(DetailActivity.BUNDLE_KEY_NEWS_OBJECT, software);
        intent.putExtra(DetailActivity.BUNDLE_KEY_DISPLAY_TYPE, DetailActivity.DISPLAY_SOFTWARE);
        context.startActivity(intent);
    }

    public static void showNewsDetail(Context context, Article news) {
        Intent intent = new Intent(context, DetailActivity.class);
        intent.putExtra(DetailActivity.BUNDLE_KEY_DISPLAY_TYPE, DetailActivity.DISPLAY_NEWS);
        intent.putExtra(DetailActivity.BUNDLE_KEY_NEWS_OBJECT, news);
        context.startActivity(intent);
    }

    public static void showEventDetail(Context context, Article event) {
        Toast.makeText(context, "活动", Toast.LENGTH_SHORT).show();
      /*  Intent intent = new Intent(context, DetailActivity.class);
        intent.putExtra(DetailActivity.BUNDLE_KEY_DISPLAY_TYPE, DetailActivity.DISPLAY_EVENT);
        intent.putExtra(DetailActivity.BUNDLE_KEY_NEWS_OBJECT, event);
        context.startActivity(intent);*/
    }

    public static void showCmmActivity(Context context) {
        Intent intent = new Intent(context, CmmActivity.class);
        context.startActivity(intent);
    }

    /**
     * 跳到登录页面
     * @param context
     */
    public static void jump2login(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
    }

    public static void showTweetPublishUI(Context context, int type) {
        Intent intent = new Intent(context, TweetPublishActivity.class);
        intent.putExtra(TweetPublishActivity.TYPE_KEY, type);
        context.startActivity(intent);
    }

    public static void toUserHome(Context context, User user) {
        Intent intent = new Intent(context, UserHomeActivity.class);
        intent.putExtra(UserHomeActivity.BUNDLE_USER_KEY, user);
        context.startActivity(intent);
    }
}
