package com.shen.refreshtest.api;


import com.shen.refreshtest.HttpResult;
import com.shen.refreshtest.model.HomeData;

import retrofit2.http.GET;
import rx.Observable;

public interface HomeNewService {
	//请求的url地址
	@GET("mobile/home/load")
	Observable<HttpResult<HomeData>> loadMobileHomeInfo();

}
