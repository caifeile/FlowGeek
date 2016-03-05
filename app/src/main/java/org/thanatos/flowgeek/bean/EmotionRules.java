package org.thanatos.flowgeek.bean;


import org.thanatos.flowgeek.R;

/**
 * Created by thanatos on 15-7-26.
 */
public enum EmotionRules {
    EMOTION0(0, R.mipmap.smiley_0, "[微笑]", "[0]"),
    EMOTION1(0, R.mipmap.smiley_1, "[撇嘴]", "[1]"),
    EMOTION2(0, R.mipmap.smiley_2, "[色]", "[2]"),
    EMOTION3(0, R.mipmap.smiley_3, "[发呆]", "[3]"),
    EMOTION4(0, R.mipmap.smiley_4, "[得意]", "[4]"),
    EMOTION5(0, R.mipmap.smiley_5, "[流泪]", "[5]"),
    EMOTION6(0, R.mipmap.smiley_6, "[害羞]", "[6]"),
    EMOTION7(0, R.mipmap.smiley_7, "[闭嘴]", "[7]"),
    EMOTION8(0, R.mipmap.smiley_8, "[睡]", "[8]"),
    EMOTION9(0, R.mipmap.smiley_9, "[大哭]", "[9]"),
    EMOTION10(0, R.mipmap.smiley_10, "[尴尬]", "[10]"),
    EMOTION11(0, R.mipmap.smiley_11, "[发怒]", "[11]"),
    EMOTION12(0, R.mipmap.smiley_12, "[调皮]", "[12]"),
    EMOTION13(0, R.mipmap.smiley_13, "[呲牙]", "[13]"),
    EMOTION14(0, R.mipmap.smiley_14, "[惊讶]", "[14]"),
    EMOTION15(0, R.mipmap.smiley_15, "[难过]", "[15]"),
    EMOTION16(0, R.mipmap.smiley_16, "[酷]", "[16]"),
    EMOTION17(0, R.mipmap.smiley_17, "[冷汗]", "[17]"),
    EMOTION18(0, R.mipmap.smiley_18, "[抓狂]", "[18]"),
    EMOTION19(0, R.mipmap.smiley_19, "[吐]", "[19]"),
    EMOTION20(0, R.mipmap.smiley_20, "[偷笑]", "[20]"),
    EMOTION21(0, R.mipmap.smiley_21, "[可爱]", "[21]"),
    EMOTION22(0, R.mipmap.smiley_22, "[白眼]", "[22]"),
    EMOTION23(0, R.mipmap.smiley_23, "[傲慢]", "[23]"),
    EMOTION24(0, R.mipmap.smiley_24, "[饥饿]", "[24]"),
    EMOTION25(0, R.mipmap.smiley_25, "[困]", "[25]"),
    EMOTION26(0, R.mipmap.smiley_26, "[惊恐]", "[26]"),
    EMOTION27(0, R.mipmap.smiley_27, "[流汗]", "[27]"),
    EMOTION28(0, R.mipmap.smiley_28, "[憨笑]", "[28]"),
    EMOTION29(0, R.mipmap.smiley_29, "[大兵]", "[29]"),
    EMOTION30(0, R.mipmap.smiley_30, "[奋斗]", "[30]"),
    EMOTION31(0, R.mipmap.smiley_31, "[咒骂]", "[31]"),
    EMOTION32(0, R.mipmap.smiley_32, "[疑问]", "[32]"),
    EMOTION33(0, R.mipmap.smiley_33, "[嘘]", "[33]"),
    EMOTION34(0, R.mipmap.smiley_34, "[晕]", "[34]"),
    EMOTION35(0, R.mipmap.smiley_35, "[折磨]", "[35]"),
    EMOTION36(0, R.mipmap.smiley_36, "[衰]", "[36]"),
    EMOTION37(0, R.mipmap.smiley_37, "[骷髅]", "[37]"),
    EMOTION38(0, R.mipmap.smiley_38, "[敲打]", "[38]"),
    EMOTION39(0, R.mipmap.smiley_39, "[再见]", "[39]"),
    EMOTION40(0, R.mipmap.smiley_40, "[擦汗]", "[40]"),
    EMOTION41(0, R.mipmap.smiley_41, "[抠鼻]", "[41]"),
    EMOTION42(0, R.mipmap.smiley_42, "[鼓掌]", "[42]"),
    EMOTION43(0, R.mipmap.smiley_43, "[糗大了]", "[43]"),
    EMOTION44(0, R.mipmap.smiley_44, "[坏笑]", "[44]"),
    EMOTION45(0, R.mipmap.smiley_45, "[左哼哼]", "[45]"),
    EMOTION46(0, R.mipmap.smiley_46, "[右哼哼]", "[46]"),
    EMOTION47(0, R.mipmap.smiley_47, "[哈欠]", "[47]"),
    EMOTION48(0, R.mipmap.smiley_48, "[鄙视]", "[48]"),
    EMOTION49(0, R.mipmap.smiley_49, "[委屈]", "[49]"),
    EMOTION50(0, R.mipmap.smiley_50, "[快哭了]", "[50]"),
    EMOTION51(0, R.mipmap.smiley_51, "[阴险]", "[51]"),
    EMOTION52(0, R.mipmap.smiley_52, "[亲亲]", "[52]"),
    EMOTION53(0, R.mipmap.smiley_53, "[吓]", "[53]"),
    EMOTION54(0, R.mipmap.smiley_54, "[可怜]", "[54]");

    private int type;
    private int mResId;
    private String name;
    private String remote;

    EmotionRules(int type, int mResId, String name, String remote){
        this.type = type;
        this.mResId = mResId;
        this.name = name;
        this.remote = remote;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getMResId() {
        return mResId;
    }

    public void setMResId(int mResId) {
        this.mResId = mResId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRemote() {
        return remote;
    }

    public void setRemote(String remote) {
        this.remote = remote;
    }

    public static EmotionRules containOf(String s) {
        EmotionRules[] rules = EmotionRules.values();
        for (EmotionRules item : rules){
            if (item.getName().equals(s) || item.getRemote().equals(s)) return item;
        }
        return null;
    }
}
