package org.thanatos.flowgeek.ui.activity;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ZoomButtonsController;

import com.squareup.picasso.Picasso;
import com.trello.rxlifecycle.ActivityEvent;

import org.thanatos.base.domain.Entity;
import org.thanatos.base.model.SharePreferenceManager;
import org.thanatos.base.model.SharePreferenceManager.ApplicationSetting;
import org.thanatos.base.model.SharePreferenceManager.ApplicationSetting.ApplicationTheme;
import org.thanatos.base.ui.activity.BaseHoldBackActivity;
import org.thanatos.base.utils.UIHelper;
import org.thanatos.flowgeek.R;
import org.thanatos.flowgeek.UIManager;
import org.thanatos.flowgeek.bean.Article;
import org.thanatos.flowgeek.bean.Blog;
import org.thanatos.flowgeek.bean.Software;
import org.thanatos.flowgeek.event.Events;
import org.thanatos.flowgeek.event.RxBus;
import org.thanatos.flowgeek.listener.OnScrollerGoDownListener;
import org.thanatos.flowgeek.presenter.DetailPresenter;
import org.thanatos.base.utils.Utilities;
import org.thanatos.flowgeek.utils.DialogFactory;

import java.text.ParseException;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import nucleus.factory.RequiresPresenter;

/**
 * Created by thanatos on 15/12/28.
 */
@SuppressWarnings("all")
@RequiresPresenter(DetailPresenter.class)
public class DetailActivity extends BaseHoldBackActivity<DetailPresenter> {

    public static final int DISPLAY_NEWS = 0;
    public static final int DISPLAY_BLOG = 1;
    public static final int DISPLAY_SOFTWARE = 2;
    public static final int DISPLAY_POST = 3;
    public static final int DISPLAY_TWEET = 4;
    public static final int DISPLAY_EVENT = 5;
    public static final int DISPLAY_TEAM_ISSUE_DETAIL = 6;
    public static final int DISPLAY_TEAM_DISCUSS_DETAIL = 7;
    public static final int DISPLAY_TEAM_TWEET_DETAIL = 8;
    public static final int DISPLAY_TEAM_DIARY = 9;
    public static final int DISPLAY_COMMENT = 10;

    public static final String BUNDLE_KEY_DISPLAY_TYPE = "BUNDLE_KEY_DISPLAY_TYPE";
    public static final String BUNDLE_KEY_NEWS_OBJECT = "BUNDLE_KEY_NEWS_OBJECT";

    private Article<? extends Entity> article;
    private int mCatalog;
    private ProgressDialog dialog;
    private RecyclerView.Adapter<TextViewHold> mAdapter;

    @Bind(R.id.list_view) RecyclerView mRecycler;
    @Bind(R.id.layout_bottom_panel) LinearLayout mBottomLayout;
    @Bind(R.id.tv_person_name) TextView tvName;
    @Bind(R.id.img_portrait) ImageView ivPortrait;
    @Bind(R.id.tv_type) TextView tvType;
    @Bind(R.id.tv_extend) TextView tvExtend;
    @Bind(R.id.tv_publish_time) TextView tvCreateOn;
    @Bind(R.id.tv_title) TextView tvTitle;
    @Bind(R.id.btn_bookmark) TextView btnBookMark;
    @Bind(R.id.btn_comment) TextView btnComment;

    protected class TextViewHold extends RecyclerView.ViewHolder{
        @Bind(R.id.tv_content) WebView mWebView;
        public TextViewHold(View view) {
            super(view);

            ButterKnife.bind(this, view);

            // init WebView
            WebSettings settings = mWebView.getSettings();
            settings.setJavaScriptEnabled(true);
            settings.setSupportZoom(true);
            settings.setBuiltInZoomControls(true);
            int sysVersion = Build.VERSION.SDK_INT;
            if (sysVersion >= 11) {
                settings.setDisplayZoomControls(false);
            } else {
                ZoomButtonsController zbc = new ZoomButtonsController(mWebView);
                zbc.getZoomControls().setVisibility(View.GONE);
            }
//            mWebView.setWebViewClient(UIHelper.getWebViewClient());
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mCatalog = getIntent().getIntExtra(BUNDLE_KEY_DISPLAY_TYPE, DISPLAY_NEWS);
        article = (Article) getIntent().getSerializableExtra(BUNDLE_KEY_NEWS_OBJECT);
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);

        initView();
        initData();
        initSubscribers();
    }

    /**
     * 初始化订阅者
     */
    private void initSubscribers() {
        // 处理请求文章id
        RxBus.getInstance().toObservable()
                .compose(bindUntilEvent(ActivityEvent.DESTROY))
                .filter(events -> events.what == Events.EventEnum.GET_ARTICLE_ID)
                .subscribe(events -> {
                    Events<Long> event = new Events<Long>();
                    event.what = events.getMessage();
                    event.message = article.getId();
                    RxBus.getInstance().send(event);
                });

        // 文章所属人id
        RxBus.with(this)
                .setEvent(Events.EventEnum.GET_ARTICLE_OWNER_ID)
                .setEndEvent(ActivityEvent.DESTROY)
                .onNext((events) -> {
                    Events<Long> event = new Events<Long>();
                    event.what = events.getMessage();
                    event.message = article.getAuthorId();
                    RxBus.getInstance().send(event);
                }).create();

        // 处理请求详情类别catalog
        RxBus.getInstance().toObservable()
                .compose(bindUntilEvent(ActivityEvent.DESTROY))
                .filter(events -> events.what == Events.EventEnum.GET_ARTICLE_CATALOG)
                .subscribe(events -> {
                    Events<Integer> event = new Events<Integer>();
                    event.what = events.getMessage();
                    event.message = mCatalog;
                    RxBus.getInstance().send(event);
                });
    }

    /**
     * init data
     */
    @SuppressWarnings("all")
    private void initData() {
        switch (mCatalog){
            case DISPLAY_SOFTWARE:
                tvTitle.setText(article.getTitle());
                Software software = article.<Software>getArticle();
                if (software==null) break;
                tvTitle.setText(software.getTitle());
                tvName.setText("开发语言：" + software.getLanguage());
                tvName.setTextColor(tvCreateOn.getCurrentTextColor());
                tvCreateOn.setText("收录时间：" + software.getRecordTime());
                tvType.setText("开源协议：" + software.getLicense());
                tvType.setVisibility(View.VISIBLE);
                tvExtend.setText("System：" + software.getOs());
                tvExtend.setVisibility(View.VISIBLE);
                Picasso.with(this).load(software.getLogo()).into(ivPortrait);
                break;
            case DISPLAY_BLOG:
                tvType.setVisibility(View.VISIBLE);
                if (article.getArticle()==null) break;
                if (article.<Blog>getArticle().getDocumenttype()==Blog.DOC_TYPE_REPASTE){
                    tvType.setText(getResources().getText(R.string.repaste));
                }else{
                    tvType.setText(getResources().getText(R.string.original));
                }
            case DISPLAY_NEWS:
            case DISPLAY_EVENT:
            case DISPLAY_POST:
                tvTitle.setText(article.getTitle());
                tvName.setText(article.getAuthor());
                if (!Utilities.isEmpty(article.getPubDate())){
                    try {
                        tvCreateOn.setText(getResources().getText(R.string.publish_on) + Utilities.dateFormat(article.getPubDate()));
                    } catch (ParseException e) {
                        e.printStackTrace();
                        tvCreateOn.setText(article.getPubDate());
                    }
                }
                if (!Utilities.isEmpty(article.getPortrait())){
                    Picasso.with(this).load(article.getPortrait()).into(ivPortrait);
                }
                break;
        }

        btnComment.setText(getResources().getString(R.string.comment) + "(" + String.valueOf(article.getCmmCount()) + ")");

        if (article.getFavorite()==1){
            btnBookMark.setCompoundDrawables(null, getResources().getDrawable(R.mipmap.icon_star_mid_yes, getTheme()), null, null);
        }
        mAdapter.notifyItemChanged(0);
    }

    /**
     * init view
     */
    private void initView() {
        btnComment.setClickable(false);
        mRecycler.setLayoutManager(new LinearLayoutManager(this));
        mRecycler.addOnScrollListener(new OnScrollerGoDownListener(mBottomLayout));
        mRecycler.setAdapter(mAdapter = new RecyclerView.Adapter<TextViewHold>() {
            @Override
            public TextViewHold onCreateViewHolder(ViewGroup parent, int viewType) {
                return new TextViewHold(getLayoutInflater().inflate(R.layout.view_news_content, parent, false));
            }

            @Override
            public void onBindViewHolder(TextViewHold holder, int position) {
                holder.mWebView.loadDataWithBaseURL("", loadHTMLData(article.getBody() == null ? "" : article.getBody()), "text/html", "UTF-8", "");
            }

            @Override
            public int getItemCount() {
                return 1;
            }
        });

        onLoadFinished(article);
        onLoading();

    }

    /**
     * load html form remote
     * @param body
     * @return
     */
    private String loadHTMLData(String body) {
        StringBuilder builder = new StringBuilder().append(UIManager.WEB_STYLE)
                .append(UIManager.WEB_LOAD_IMAGES);

        SharedPreferences preferences = SharePreferenceManager.getApplicationSetting(this);
        int theme = preferences.getInt(ApplicationSetting.KEY_THEME, ApplicationTheme.LIGHT.getKey());
        if(theme == SharePreferenceManager.ApplicationSetting.ApplicationTheme.DARK.getKey()){
            builder.append("<body class='night'><div class='contentstyle' id='article_body'>");
        }else{
            builder.append("<body><div class='contentstyle' id='article_body'>");
        }

        return builder.append(setupContentImage(body))
                .append("</div></body>")
                .toString();
    }

    /**
     * 对HTML里的图片做些处理
     * @param body
     * @return
     */
    public static String setupContentImage(String body) {
        // 过滤掉 img标签的width,height属性
        body = body.replaceAll("(<img[^>]*?)\\s+width\\s*=\\s*\\S+", "$1");
        body = body.replaceAll("(<img[^>]*?)\\s+height\\s*=\\s*\\S+", "$1");
        // 添加点击图片放大支持
        body = body.replaceAll("(<img[^>]+src=\")(\\S+)\"", "$1$2\" onClick=\"showImagePreview('$2')\"");
        return body;
    }

    /**
     * 当加载完成, 供presenter调用
     * @param article
     */
    public void onLoadFinished(Article article) {
        if (dialog!=null){
            dialog.dismiss();
            dialog = null;
        }
        this.article = article;
        initData();
        btnComment.setClickable(true);
    }

    /**
     * 当加载失败, 供presenter调用
     */
    public void onLoadFailure() {
        if (dialog!=null) {
            dialog.dismiss();
            dialog = null;
        }
        Toast.makeText(DetailActivity.this, R.string.error, Toast.LENGTH_SHORT).show();
    }

    /**
     * 当加载中, 显示dialog
     */
    public void onLoading() {
        dialog = DialogFactory.getFactory().getLoadingDialog(this);
    }

    @OnClick(R.id.btn_comment) void onClickCmm(){
        UIManager.showCmmActivity(this);
    }

    @OnClick(R.id.btn_like) void onClickLike(){

    }


    public int getCatalog(){
        return mCatalog;
    }

    public Article getNews() {
        return article;
    }

    @Override
    protected String onSetTitle() {
        switch (mCatalog){
            case DISPLAY_NEWS:
                return getResources().getString(R.string.news_detail);
            case DISPLAY_BLOG:
                return getResources().getString(R.string.blog_detail);
            case DISPLAY_POST:
                return getResources().getString(R.string.post_detail);
            case DISPLAY_TWEET:
                return getResources().getString(R.string.tweet_detail);
            case DISPLAY_SOFTWARE:
                return getResources().getString(R.string.software_detail);
        }
        return "---";
    }

    @Override
    protected int onBindLayout() {
        return R.layout.activity_news_detail;
    }
}
