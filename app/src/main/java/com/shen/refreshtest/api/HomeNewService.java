package com.shen.refreshtest.api;


import com.shen.refreshtest.HttpResult;
import com.shen.refreshtest.model.HomeData;
import com.shen.refreshtest.model.Product;

import java.util.List;

import retrofit2.http.GET;
import rx.Observable;

/**
 * 首页数据请求服务类
 */
public interface HomeNewService {
	/**
	 * 加载首页数据信息
	 * @return 返回首页数据集合
	 */
	@GET("/mobile/home/load")
	Observable<HttpResult<HomeData>> loadMobileHomeInfo();


	/**
	 * 加载首页大数据推荐数据信息
	 * @return 返回大数据商品列表
	 */
	@GET("/mobile/home/more")
	Observable<HttpResult<List<Product>>> loadMobileHomeMoreInfo();

}
