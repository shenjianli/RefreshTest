package com.shen.refreshtest.engine;


import com.shen.refreshtest.core.base.MvpView;
import com.shen.refreshtest.model.HomeData;
import com.shen.refreshtest.model.Product;

import java.util.List;

public interface HomeView extends MvpView {

    /**
     * 更新大数据推荐
     * @param recommends 大数据数据集合
     */
    public void updateRmdInfo(List<Product> recommends);

    /**
     * 更新首页数据集合
     * @param homeData 首页数据对象
     */
    public void updateHomeInfo(HomeData homeData);

    /**
     * 当首页数据请求报错时，进行回调，方便进行重试
     * @param type
     */
    public void updateHomeError(int type);
}
