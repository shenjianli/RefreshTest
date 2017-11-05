package com.shen.refreshtest.model;

import java.util.List;

/**
 * Created by jerry shen on 2017/10/30.
 */

public class HomeData {

    /**
     *  搜索栏默认关键词
     */
    private String defaultKey;
    /**
     * 顶部轮播广告
     */
    private List<ImgData> slides;
    /**
     * 大型活动图片
     */
    private ImgData bigProm;
    private List<NoticeData> eMsgs;//E购快讯

    private List<ImgData> bigAds;//大屏广告

    private List<Product> recmdProds;//猜你喜欢

    public List<Product> getRecmdProds() {
        return recmdProds;
    }

    public void setRecmdProds(List<Product> recmdProds) {
        this.recmdProds = recmdProds;
    }

    public String getDefaultKey() {
        return defaultKey;
    }

    public void setDefaultKey(String defaultKey) {
        this.defaultKey = defaultKey;
    }

    public List<ImgData> getSlides() {
        return slides;
    }

    public void setSlides(List<ImgData> slides) {
        this.slides = slides;
    }

    public List<NoticeData> geteMsgs() {
        return eMsgs;
    }

    public void seteMsgs(List<NoticeData> eMsgs) {
        this.eMsgs = eMsgs;
    }

    public List<ImgData> getBigAds() {
        return bigAds;
    }

    public void setBigAds(List<ImgData> bigAds) {
        this.bigAds = bigAds;
    }

    public ImgData getBigProm() {
        return bigProm;
    }

    public void setBigProm(ImgData bigProm) {
        this.bigProm = bigProm;
    }

    @Override
    public String toString() {
        return "RefreshApp{" +
                "defaultKey='" + defaultKey + '\'' +
                ", slides=" + slides +
                ", bigProm=" + bigProm +
                ", eMsgs=" + eMsgs +
                ", bigAds=" + bigAds +
                ", recmdProds=" + recmdProds +
                '}';
    }
}
