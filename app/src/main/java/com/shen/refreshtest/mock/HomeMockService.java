package com.shen.refreshtest.mock;

import android.os.SystemClock;

import com.google.gson.Gson;
import com.shen.netclient.net.MockService;
import com.shen.netclient.util.LogUtils;
import com.shen.refreshtest.HttpResult;
import com.shen.refreshtest.model.HomeData;
import com.shen.refreshtest.model.ImgData;
import com.shen.refreshtest.model.NoticeData;
import com.shen.refreshtest.model.Product;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by shenjianli on 2016/7/8.
 * 测试使用的本地MockService用来返回请求的json字符串
 */
public class HomeMockService extends MockService {
    @Override
    public String getJsonData() {

        HomeData testData = getHomeData();

        HttpResult<HomeData> result = new HttpResult<>();
        result.setReqCode(HttpResult.REQ_SUCC);
        result.setMsg("没有错误");
        result.setData(testData);
        String resultStr =  new Gson().toJson(result);
        LogUtils.i("获得的json字符串为：" + result);
        SystemClock.sleep(2000);
        //返回json字符串
        return resultStr;
    }


    /**
     * 测试使用数据
     * @return
     */
    private HomeData getHomeData(){
        HomeData homeData = new HomeData();
        homeData.setDefaultKey("Google Android");

        List<ImgData> slides = new ArrayList<>();
        List<ImgData> imgBigAdDatas = new ArrayList<>();


        ImgData imgData = new ImgData();
        imgData.setImgUrl("http://c.hiphotos.baidu.com/image/pic/item/d31b0ef41bd5ad6ed7d993388bcb39dbb6fd3c3b.jpg");
        imgData.setHrefUrl("http://m.mall.icbc.com.cn/mobile/mobileStroe/showSpecial.jhtml?themeId=0000000000143693&fromType=index");

        slides.add(imgData);
        imgBigAdDatas.add(imgData);


        imgData = new ImgData();
        imgData.setImgUrl("http://b.hiphotos.baidu.com/image/pic/item/5bafa40f4bfbfbed1af73b9572f0f736aec31fa9.jpg");
        imgData.setHrefUrl("http://m.mall.icbc.com.cn/mobile/mobileProduct/product_0001351329.jhtml?fromType=index");
        slides.add(imgData);
        imgBigAdDatas.add(imgData);

        imgData = new ImgData();
        imgData.setHrefUrl("http://d.hiphotos.baidu.com/image/pic/item/b17eca8065380cd722113b06ab44ad34588281ca.jpg");
        imgData.setImgUrl("http://mpic.tiankong.com/aa6/099/aa6099c5de10c4dd6a11797a3ba7bb4c/640.jpg");
        imgBigAdDatas.add(imgData);
        slides.add(imgData);


        imgData = new ImgData();
        imgData.setImgUrl("http://mpic.tiankong.com/6d2/db0/6d2db0e71e4c3f12575cc522624c66b5/640.jpg");
        imgData.setHrefUrl("http://m.mall.icbc.com.cn/mobile/mobileStroe/showSpecial.jhtml?themeId=0000000000143291&fromType=index");
        imgBigAdDatas.add(imgData);
        slides.add(imgData);

        imgData = new ImgData();
        imgData.setImgUrl("http://c.hiphotos.baidu.com/image/pic/item/1e30e924b899a901bee64fde17950a7b0308f503.jpg");
        imgData.setHrefUrl("http://channels1.mall.icbc.com.cn/channels/mobile/operation/icbcsubtswh/index.html?fromType=index");
        imgBigAdDatas.add(imgData);
        slides.add(imgData);



        //快报
        List<NoticeData> noticeDatas = new ArrayList<>();
        NoticeData noticeData = new NoticeData();


        noticeData.setMsgUrl("http://m.mall.icbc.com.cn/mobile/mobileStroe/showSpecial.jhtml?themeId=0000000000143291&fromType=index");
        noticeData.setMsgDesc("大促销下单拆福袋，亿万新年红包随便拿");
        noticeDatas.add(noticeData);

        noticeData = new NoticeData();
        noticeData.setMsgUrl("http://m.mall.icbc.com.cn/mobile/mobileStroe/showSpecial.jhtml?themeId=0000000000143291&fromType=index");
        noticeData.setMsgDesc("家电五折团，抢十亿无门槛现金红包");
        noticeDatas.add(noticeData);

        noticeData = new NoticeData();
        noticeData.setMsgUrl("http://channels1.mall.icbc.com.cn/channels/mobile/operation/icbcsubtswh/index.html?fromType=index");
        noticeData.setMsgDesc("星球大战剃须刀首发送200元代金券");
        noticeDatas.add(noticeData);


        List<Product> hotSales = new ArrayList<>();

        Product product = new Product();
        product.setName("新加坡馆Us CLINICALS优克利深海鱼油软胶囊欧米伽3深海1");
        product.setUrl("http://m.mall.icbc.com.cn/mobile/mobileProduct/product_0000579919.jhtml?fromType=index");
        product.setPrice("￥435.00");
        product.setImgUrl("http://photocdn.sohu.com/20150727/mp24531450_1437996717965_13.jpeg");
        hotSales.add(product);


        product = new Product();
        product.setName("金牛牌特级冰糖古晋官燕 6X75G，一套三盒2");
        product.setUrl("http://m.mall.icbc.com.cn/mobile/mobileProduct/product_0000663700.jhtml?fromType=index");
        product.setPrice("￥435.00");
        product.setImgUrl("http://www.sznews.com/ent/images/attachement/jpg/site3/20141128/001e4f9d635215e25c9d4a.jpg");
        hotSales.add(product);

        product = new Product();
        product.setName("新加坡馆Us CLINICALS优克利深海鱼油软胶囊欧米伽3深海3");
        product.setUrl("http://m.mall.icbc.com.cn/mobile/mobileProduct/product_0000481011.jhtml?fromType=index");
        product.setPrice("￥435.00");
        product.setImgUrl("http://img.taopic.com/uploads/userup/81387/e723c95ed40ba11bbb4f.jpg");
        hotSales.add(product);

        product = new Product();
        product.setName("新加坡馆Us CLINICALS优克利深海鱼油软胶囊欧米伽3深海4");
        product.setUrl("http://m.mall.icbc.com.cn/mobile/mobileProduct/product_0000579919.jhtml?fromType=index");
        product.setPrice("￥45.00");
        product.setImgUrl("http://dl.bizhi.sogou.com/images/2013/02/21/331906.jpg");
        hotSales.add(product);

        product = new Product();
        product.setName("新加坡馆Us CLINICALS优克利深海鱼油软胶囊欧米伽3深海5");
        product.setUrl("http://m.mall.icbc.com.cn/mobile/mobileProduct/product_0000579919.jhtml?fromType=index");
        product.setPrice("￥5.00");
        product.setImgUrl("http://news.cnhubei.com/xw/yl/201402/W020140216440305697457.jpg");
        hotSales.add(product);

        product = new Product();
        product.setName("新加坡馆Us-----测试用商品");
        product.setUrl("http://m.mall.icbc.com.cn/mobile/mobileProduct/product_0000579919.jhtml?fromType=index");
        product.setPrice("￥25.00");
        product.setImgUrl("http://file.iqilu.com/upimg/userup/0901/141K524HN.jpg");
        hotSales.add(product);

        homeData.setSlides(slides);
        homeData.setBigProm(imgData);
        homeData.setBigAds(imgBigAdDatas);
        homeData.seteMsgs(noticeDatas);
        homeData.setRecmdProds(hotSales);

        return homeData;
    }
}
