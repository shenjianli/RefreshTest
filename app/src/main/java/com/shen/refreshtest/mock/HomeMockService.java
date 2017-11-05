package com.shen.refreshtest.mock;

import com.google.gson.Gson;
import com.shen.netclient.net.MockService;
import com.shen.netclient.util.LogUtils;
import com.shen.refreshtest.HttpResult;
import com.shen.refreshtest.model.HomeData;


/**
 * Created by shenjianli on 2016/7/8.
 * 测试使用的本地MockService用来返回请求的json字符串
 */
public class HomeMockService extends MockService {
    @Override
    public String getJsonData() {

        HomeData testData = new HomeData();
        HttpResult<HomeData> result = new HttpResult<>();

        result.setReqCode(HttpResult.REQ_SUCC);
        result.setMsg("没有错误");
        result.setData(testData);
        String resultStr =  new Gson().toJson(result);
        LogUtils.i("获得的json字符串为：" + result);
        //返回json字符串
        return resultStr;
    }
}
