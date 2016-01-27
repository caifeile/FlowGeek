package org.thanatos.flowgeek;

import android.app.Application;
import android.test.ApplicationTestCase;
import android.test.UiThreadTest;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;
import org.thanatos.flowgeek.bean.Blog;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {
    public ApplicationTest() {
        super(Application.class);
    }


    @UiThreadTest
    public void test01(){
        Serializer serializer = new Persister();
        try {
            Blog blog = serializer.read(Blog.class, "<blog>\n" +
                    "<id>608727</id>\n" +
                    "<title>\n" +
                    "<![CDATA[ OSChina 周一乱弹 —— 深圳下冰雹了 ]]>\n" +
                    "</title>\n" +
                    "<url>\n" +
                    "<![CDATA[ http://my.oschina.net/xxiaobian/blog/608727 ]]>\n" +
                    "</url>\n" +
                    "<where>\n" +
                    "<![CDATA[ OSChina 乱弹 ]]>\n" +
                    "</where>\n" +
                    "<commentCount>27</commentCount>\n" +
                    "<body>\n" +
                    "<![CDATA[\n" +
                    "<style type='text/css'>pre {white-space:pre-wrap;word-wrap:break-word;}</style><p><strong>昨天冷吗？当然冷啦，深圳都下冰雹了能不冷吗？</strong></p> <p><a href=\"http://my.oschina.net/u/2485200\" target=\"_blank\" rel=\"nofollow\">@郑丽纯</a>：此图是今天状态</p> <p><img a\n" +
                    "]]>\n" +
                    "<![CDATA[\n" +
                    "lt=\"http://static.oschina.net/uploads/space/2016/0124/092149_ZMLD_2485200.jpg\" src=\"http://static.oschina.net/uploads/space/2016/0124/092149_ZMLD_2485200.jpg\"></p> <p><strong>不过冷也有冷的好处</strong><br></p> <p>科学证明：寒冷的环境可以使人变得年轻。小明的爷爷今年70多岁了，出门冻得跟孙子一样。——欲仙欲死熊猫饼</p> <p><strong>天一冷起来，戴个隐形眼镜都麻烦</strong><br></p> <p>最近天真冷啊（分享自@没品图&nbsp;&nbsp;）</p> <p><img src=\"http://static.oschina.net/uploads/space/2016/0124/183201_HriG_2306979.jpg\"></p> <p><strong>你知道广州深圳下个雪有多不容易吗？</strong><br></p> <p>广州人堆雪人。。</p> <p><img src=\"http://static.oschina.net/uploads/space/2016/0124/184542_hjjd_2306979.jpg\"></p> <p><strong>其实王八也不容易</strong><br></p> <p><a href=\"http://my.oschina.net/DBboy\" target=\"_blank\" rel=\"nofollow\">@开源中国首席最狠男人</a>：最直观的气温变化</p> <p><img title=\"\" alt=\"http://static.oschina.net/uploads/space/2016/0124/180821_lw6K_1440880.jpg\" src=\"http://static.oschina.net/uploads/space/2016/0124/180821_lw6K_1440880.jpg\"></p> <p><strong>虽然很冷，可你们还是要起床上班</strong><br></p> <p>一大早就在票圈受到了会心一击，我不念我不念我不想念via：App蘑菇仔</p> <p><img src=\"http://static.oschina.net/uploads/space/2016/0124/184209_Nd2q_2306979.jpg\"></p> <p><strong>春节要来了，你们准备怎么过呢？</strong><br></p> <p>民警巡逻，看到一男突然大喊：站住！int类型占几个字节？男：4个。民警：你可以走了。除夕夜还在街上走，幸苦又寒酸的样子，不是小偷就是程序员。</p> <p><strong>女生是胖点好还是瘦点好？</strong><br></p> <p>老公，我这么胖，你会不会不爱我啦？ 怎么会！你瘦的时候住进我心里了，后来胖了，卡在里面就出不来了。 多美的情话，胖点好，瘦了就被挤出来了。 胖了，别人挤不进去，别控制，一直吃……感动的赞起来！</p> <p><strong>听说这几天郭敬明又跟别人闹矛盾了？</strong><br></p> <p>哈哈哈哈哈哈via我的微博有点贱</p> <p><img src=\"http://static.oschina.net/uploads/space/2016/0124/183710_RTUj_2306979.jpg\"></p> <p><strong>给假币也就算了，可逆见过给这么霸道的假币吗？</strong><br></p> <p>。假币via:Saba盐烧</p> <p><img src=\"http://static.oschina.net/uploads/space/2016/0124/183844_ipDP_2306979.jpg\"></p> <p><strong>长得可爱的男生肯定是有男朋友的，长得霸气的女生也是有女朋友的</strong><br></p> <p>果然那个妹子那么可爱一定有女朋友了</p> <p><img src=\"http://static.oschina.net/uploads/space/2016/0124/191709_ZtoL_2306979.jpg\"></p> <p><strong>过年过节为了方便亲戚朋友给红包，咱们特意开通了微信支付</strong><br></p> <p>快过年了，提前在房门上贴好方便前来拜年的亲朋好友们</p> <p><img src=\"http://static.oschina.net/uploads/space/2016/0124/191905_7R3F_2306979.jpg\"></p> <p><strong>为什么2016春晚吉祥物的腮边有两坨呢？其实是有原因的</strong><br></p> <p>猴腮雷</p> <p><img src=\"http://static.oschina.net/uploads/space/2016/0124/191823_uVso_2306979.jpg\"></p> <p><br></p> <p><span style=\"font-size: 14px;\"><strong>部分内容来自网络</strong><br></span></p> <p><span style=\"padding: 0px; margin: 0px; color: rgb(0, 0, 0); font-size: 14px;\"><strong>马上扫一扫下面的二维码</strong></span></p> <p><span style=\"padding: 0px; margin: 0px; font-size: 14px;\"><strong><a href=\"http://static.oschina.net/uploads/space/2013/0304/160442_8Fw6_179699.png\" target=\"_blank\" rel=\"nofollow\"><img src=\"http://static.oschina.net/uploads/space/2013/0304/160442_8Fw6_179699.png\" title=\"OSC微信二维码\"></a></strong></span><br><span style=\"padding: 0px; margin: 0px; font-size: 14px;\"><strong>（扫一扫，关注OSChina微信号，每天为你送上精选资讯早点，还有每天的 OSChina<strong>&nbsp;乱弹</strong>哦）</strong></span></p> <p><br></p>\n" +
                    "]]>\n" +
                    "</body>\n" +
                    "<author>\n" +
                    "<![CDATA[ 小小编辑 ]]>\n" +
                    "</author>\n" +
                    "<authorid>1428332</authorid>\n" +
                    "<documentType>1</documentType>\n" +
                    "<pubDate>2016-01-25 07:35:36</pubDate>\n" +
                    "<favorite>0</favorite>\n" +
                    "</blog>");

            System.out.println(blog);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



}