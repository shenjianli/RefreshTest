package com.shen.refreshtest.engine;

import com.shen.netclient.NetClient;
import com.shen.netclient.util.LogUtils;
import com.shen.refreshtest.HttpResultFunc;
import com.shen.refreshtest.api.HomeNewService;
import com.shen.refreshtest.core.base.BasePresenter;
import com.shen.refreshtest.model.HomeData;
import com.shen.refreshtest.model.ImgData;
import com.shen.refreshtest.model.NoticeData;
import com.shen.refreshtest.model.Product;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


/**
 * Created by edianzu on 2016/12/30.
 */
public class HomePresenter extends BasePresenter<HomeView> {


    private Subscriber<HomeData> homeDataSubscriber;
    private Subscriber<List<Product>> homeMoreSubscriber;

    public HomePresenter(){
    }

    /**
     * 加载首页数据
     */
    public void loadHomeInfoData(){
        LogUtils.i("开始请求-----新版---首页请求");


        homeDataSubscriber = new Subscriber<HomeData>() {
            @Override
            public void onCompleted() {
                LogUtils.i("表求完成");
            }

            @Override
            public void onError(Throwable e) {
                LogUtils.i(e.getStackTrace().toString());
                if(null != mMvpView ){
                    //HomeData homeData = getHomeData();
                    //LogUtils.i("----测试----");
                    //mMvpView.updateHomeInfo(homeData);
                    mMvpView.showError(0, e.getMessage());
                    mMvpView.hideLoading(0);
                }
            }

            @Override
            public void onNext(HomeData data) {
                LogUtils.i("获取数据成功");
                if(null != mMvpView ){
                    LogUtils.i("成功收到新版首页数据：" + data.toString());
                    mMvpView.hideLoading(0);
                    mMvpView.updateHomeInfo(data);
                }

            }
        };

        HomeNewService homeService = NetClient.retrofit().create(HomeNewService.class);
        homeService.loadMobileHomeInfo()
                .map(new HttpResultFunc<HomeData>())
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(homeDataSubscriber);
    }

    /**
     * 测试使用数据
     * @return
     */
    private HomeData getHomeData(){
        HomeData homeData = new HomeData();

        List<ImgData> slides = new ArrayList<>();
        List<ImgData> imgBigAdDatas = new ArrayList<>();


        ImgData imgData = new ImgData();
        imgData.setImgUrl("http://image6.mall.icbc.com.cn/image/operationDefault/1508286955413.jpg");
        imgData.setHrefUrl("http://m.mall.icbc.com.cn/mobile/mobileStroe/showSpecial.jhtml?themeId=0000000000143693&fromType=index");

        slides.add(imgData);
        imgBigAdDatas.add(imgData);


        imgData = new ImgData();
        imgData.setImgUrl("http://image5.mall.icbc.com.cn/image/operationDefault/1508290943308.jpg");
        imgData.setHrefUrl("http://m.mall.icbc.com.cn/mobile/mobileProduct/product_0001351329.jhtml?fromType=index");
        slides.add(imgData);
        imgBigAdDatas.add(imgData);

        imgData = new ImgData();
        imgData.setHrefUrl("http://jipiao.mall.icbc.com.cn/HTML5/index.aspx?fromType=index");
        imgData.setImgUrl("http://image6.mall.icbc.com.cn/image/operationDefault/1508210841709.jpg");
        imgBigAdDatas.add(imgData);
        slides.add(imgData);


        imgData = new ImgData();
        imgData.setImgUrl("http://image5.mall.icbc.com.cn/image/operationDefault/1508117446936.jpg");
        imgData.setHrefUrl("http://m.mall.icbc.com.cn/mobile/mobileStroe/showSpecial.jhtml?themeId=0000000000143291&fromType=index");
        imgBigAdDatas.add(imgData);
        slides.add(imgData);

        imgData = new ImgData();
        imgData.setImgUrl("http://image6.mall.icbc.com.cn/image/operationDefault/1508305321669.jpg");
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
        product.setImgUrl("http://image8.mall.icbc.com.cn/image/10012963/1459415955255_2_1.jpg");
        hotSales.add(product);


        product = new Product();
        product.setName("金牛牌特级冰糖古晋官燕 6X75G，一套三盒2");
        product.setUrl("http://m.mall.icbc.com.cn/mobile/mobileProduct/product_0000663700.jhtml?fromType=index");
        product.setPrice("￥435.00");
        product.setImgUrl("http://image5.mall.icbc.com.cn/image/10013696/1492570565174_2_1.jpg");
        hotSales.add(product);

        product = new Product();
        product.setName("新加坡馆Us CLINICALS优克利深海鱼油软胶囊欧米伽3深海3");
        product.setUrl("http://m.mall.icbc.com.cn/mobile/mobileProduct/product_0000481011.jhtml?fromType=index");
        product.setPrice("￥435.00");
        product.setImgUrl("http://image8.mall.icbc.com.cn/image/10012963/1459415955255_2_1.jpg");
        hotSales.add(product);

        product = new Product();
        product.setName("新加坡馆Us CLINICALS优克利深海鱼油软胶囊欧米伽3深海4");
        product.setUrl("http://m.mall.icbc.com.cn/mobile/mobileProduct/product_0000579919.jhtml?fromType=index");
        product.setPrice("￥45.00");
        product.setImgUrl("http://image8.mall.icbc.com.cn/image/10012963/1459415955255_2_1.jpg");
        hotSales.add(product);

        product = new Product();
        product.setName("新加坡馆Us CLINICALS优克利深海鱼油软胶囊欧米伽3深海5");
        product.setUrl("http://m.mall.icbc.com.cn/mobile/mobileProduct/product_0000579919.jhtml?fromType=index");
        product.setPrice("￥5.00");
        product.setImgUrl("http://image8.mall.icbc.com.cn/image/10012963/1459415955255_2_1.jpg");
        hotSales.add(product);

        product = new Product();
        product.setName("新加坡馆Us-----测试用商品");
        product.setUrl("http://m.mall.icbc.com.cn/mobile/mobileProduct/product_0000579919.jhtml?fromType=index");
        product.setPrice("￥25.00");
        product.setImgUrl("http://image8.mall.icbc.com.cn/image/10012963/1459415955255_2_1.jpg");
        hotSales.add(product);

        homeData.setSlides(slides);
        homeData.setBigProm(imgData);
        homeData.setBigAds(imgBigAdDatas);
        homeData.seteMsgs(noticeDatas);
        homeData.setRecmdProds(hotSales);

        return homeData;
    }


    /**
     * 请求首页大数据推荐
     */
    public void loadRcmProductInfo(){
        LogUtils.i("开始------首页大数据推荐———————请求");
        homeMoreSubscriber = new Subscriber<List<Product>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(List<Product> products) {
                LogUtils.i("大数据----获取数据成功");
                if(null != mMvpView && null != products && products.size() >0){
                    mMvpView.hideLoading(0);
                    mMvpView.updateRmdInfo(products);
                }
            }
        };
        HomeNewService homeService = NetClient.retrofit().create(HomeNewService.class);
        homeService.loadMobileHomeMoreInfo()
                .map(new HttpResultFunc<List<Product>>())
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(homeMoreSubscriber);


    }
}
