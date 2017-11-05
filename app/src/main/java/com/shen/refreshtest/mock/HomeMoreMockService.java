package com.shen.refreshtest.mock;

import com.google.gson.Gson;
import com.shen.netclient.net.MockService;
import com.shen.netclient.util.LogUtils;
import com.shen.refreshtest.HttpResult;
import com.shen.refreshtest.model.Product;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by shenjianli on 2016/7/8.
 * 测试使用的本地MockService用来返回请求的json字符串
 */
public class HomeMoreMockService extends MockService {
    @Override
    public String getJsonData() {

        List<Product> testData = getHomeMoreData();

        HttpResult<List<Product>> result = new HttpResult<>();
        result.setReqCode(HttpResult.REQ_SUCC);
        result.setMsg("没有错误");
        result.setData(testData);
        String resultStr =  new Gson().toJson(result);
        LogUtils.i("获得的json字符串为：" + result);
        //返回json字符串
        return resultStr;
    }


    /**
     * 测试使用数据
     * @return
     */
    private List<Product> getHomeMoreData(){



        List<Product> products = new ArrayList<>();

        Product product = new Product();
        product.setName("新加坡馆Us CLINICALS优克利深海鱼油软胶囊欧米伽3深海1");
        product.setUrl("http://m.mall.icbc.com.cn/mobile/mobileProduct/product_0000579919.jhtml?fromType=index");
        product.setPrice("￥435.00");
        product.setImgUrl("http://image8.mall.icbc.com.cn/image/10012963/1459415955255_2_1.jpg");
        products.add(product);


        product = new Product();
        product.setName("金牛牌特级冰糖古晋官燕 6X75G，一套三盒2");
        product.setUrl("http://m.mall.icbc.com.cn/mobile/mobileProduct/product_0000663700.jhtml?fromType=index");
        product.setPrice("￥435.00");
        product.setImgUrl("http://image5.mall.icbc.com.cn/image/10013696/1492570565174_2_1.jpg");
        products.add(product);

        product = new Product();
        product.setName("新加坡馆Us CLINICALS优克利深海鱼油软胶囊欧米伽3深海3");
        product.setUrl("http://m.mall.icbc.com.cn/mobile/mobileProduct/product_0000481011.jhtml?fromType=index");
        product.setPrice("￥435.00");
        product.setImgUrl("http://image8.mall.icbc.com.cn/image/10012963/1459415955255_2_1.jpg");
        products.add(product);

        product = new Product();
        product.setName("新加坡馆Us CLINICALS优克利深海鱼油软胶囊欧米伽3深海4");
        product.setUrl("http://m.mall.icbc.com.cn/mobile/mobileProduct/product_0000579919.jhtml?fromType=index");
        product.setPrice("￥45.00");
        product.setImgUrl("http://image8.mall.icbc.com.cn/image/10012963/1459415955255_2_1.jpg");
        products.add(product);

        product = new Product();
        product.setName("新加坡馆Us CLINICALS优克利深海鱼油软胶囊欧米伽3深海5");
        product.setUrl("http://m.mall.icbc.com.cn/mobile/mobileProduct/product_0000579919.jhtml?fromType=index");
        product.setPrice("￥5.00");
        product.setImgUrl("http://image8.mall.icbc.com.cn/image/10012963/1459415955255_2_1.jpg");
        products.add(product);

        product = new Product();
        product.setName("新加坡馆Us-----测试用商品");
        product.setUrl("http://m.mall.icbc.com.cn/mobile/mobileProduct/product_0000579919.jhtml?fromType=index");
        product.setPrice("￥25.00");
        product.setImgUrl("http://image8.mall.icbc.com.cn/image/10012963/1459415955255_2_1.jpg");
        products.add(product);


        return products;
    }
}
