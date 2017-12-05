package com.shen.refreshtest.app;

import android.animation.ArgbEvaluator;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.shen.refresh.RecyclerRefreshLayout;
import com.shen.refresh.RefreshConentAdapter;
import com.shen.refresh.RefreshLoadLayout;
import com.shen.refresh.util.LogUtils;
import com.shen.refreshtest.Constants;
import com.shen.refreshtest.R;
import com.shen.refreshtest.app.adapter.HotSaleAdapter;
import com.shen.refreshtest.app.adapter.HotSaleTitleAdapter;
import com.shen.refreshtest.app.adapter.ImgAdAdapter;
import com.shen.refreshtest.app.adapter.NoticeAdapter;
import com.shen.refreshtest.app.adapter.RecmdAdapter;
import com.shen.refreshtest.app.adapter.SlideAdapter;
import com.shen.refreshtest.core.base.BaseFragment;
import com.shen.refreshtest.engine.HomePresenter;
import com.shen.refreshtest.engine.HomeView;
import com.shen.refreshtest.model.HomeData;
import com.shen.refreshtest.model.ImgData;
import com.shen.refreshtest.model.NoticeData;
import com.shen.refreshtest.model.Product;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * Created by jerry shen on 2017/7/30.
 */

public class NormalFragment extends BaseFragment implements HomeView {

    @Bind(R.id.home_search_title_layout)
    LinearLayout searchTitleLayout;


    @Bind(R.id.home_refresh_load_layout)
    RefreshLoadLayout homeRefreshLoadLayout;

    /**
     * 首页的presenter
     */
    private HomePresenter homePresenter;
    /**
     * 首页请求主体框架adapter
     */
    private RefreshConentAdapter refreshConentAdapter;

    /**
     * 轮播图
     */
    private SlideAdapter slideAdapter;
    private List<ImgData> slideItems;

    /**
     * 图片广告
     */
    private ImgAdAdapter imgAdAdapter;
    private List<ImgData> imgAdDatas;

    /**
     * 快报
     */
    private NoticeAdapter noticeAdapter;
    private List<NoticeData> noticeDatas;

    /**
     * 热销商品
     */
    private HotSaleAdapter hotSaleAdapter;
    private List<Product> hotSaleProducts = new ArrayList<>();

    /**
     * 猜你喜欢标题
     */
    private HotSaleTitleAdapter hotSaleTitleAdapter;

    /**
     * 大数据推荐
     */
    private RecmdAdapter recmdAdapter;
    private List<Product> recmdProducts = new ArrayList<>();



    private View mHomeView;

    //上拉透明效果变量
    private int mSumY = 0;
    private float mDistance = 200;  //控制透明度在距离150px的变化
    private int startValue = 0x50FF4081;
    private int endValue = 0xffFF4081;
    private int bgColor;
    private ArgbEvaluator mEvaluator;


    public static NormalFragment newInstance(String index) {
        NormalFragment homeFragment = new NormalFragment();
        Bundle bundle = new Bundle();
        bundle.putString("index", index);
        homeFragment.setArguments(bundle);
        return homeFragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        mHomeView = inflater.inflate(R.layout.fragment_normal, container, false);
        ButterKnife.bind(this, mHomeView);
        super.onCreateView(inflater, container, savedInstanceState);
        initHomeView();
        initStatusBarView();
        return mHomeView;
    }




    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (null != homePresenter) {
            homePresenter.loadHomeInfoData();
        }
    }

    private void initHomeView() {

    }

    @Override
    public void initData() {

        homePresenter = new HomePresenter();
        homePresenter.attachView(this);

        mEvaluator = new ArgbEvaluator();

        refreshConentAdapter = new RefreshConentAdapter();

        slideItems = new ArrayList<>();
        slideAdapter = new SlideAdapter(context, slideItems);

        imgAdDatas = new ArrayList<>();
        imgAdAdapter = new ImgAdAdapter(context, imgAdDatas);


        noticeDatas = new ArrayList<>();
        noticeAdapter = new NoticeAdapter(context, noticeDatas);

        //热销商品
        hotSaleProducts = new ArrayList<>();
        hotSaleAdapter = new HotSaleAdapter(context, hotSaleProducts);

        //猜你喜欢标题
        hotSaleTitleAdapter = new HotSaleTitleAdapter(context, "");

        //大数据推荐
        recmdProducts = new ArrayList<>();
        recmdAdapter = new RecmdAdapter(context, recmdProducts);

        //把首页相应模块加入到主体框架中
        refreshConentAdapter.addAdapter(slideAdapter);
        refreshConentAdapter.addAdapter(imgAdAdapter);
        refreshConentAdapter.addAdapter(noticeAdapter);
        refreshConentAdapter.addAdapter(hotSaleTitleAdapter);
        refreshConentAdapter.addAdapter(hotSaleAdapter);
        refreshConentAdapter.addAdapter(recmdAdapter);
    }

    /**
     * 搜索关键字
     */
    private EditText searchKeyEt;


    private int statusAlpha = 0;
    private View homeStatusBarView;

    private void initStatusBarView() {
        homeStatusBarView = mHomeView.findViewById(R.id.home_default_status_bar);
        homeStatusBarView.getBackground().setAlpha(statusAlpha);
    }
    @Override
    public void initView() {

        searchKeyEt = (EditText) mHomeView.findViewById(R.id.et_search);


        homeRefreshLoadLayout.setOnRefreshActionListener(new RefreshLoadLayout.OnRefreshActionListener() {
            /**
             * 用来控制搜索标题的显示隐藏
             * @param moveY 距顶部的距离
             */
            @Override
            public void onRefreshMoveToTop(float moveY) {
                LogUtils.i("刷新滑动距离：" + moveY);
                if (moveY > 20) {
                    searchTitleLayout.setVisibility(View.INVISIBLE);
                } else {
                    searchTitleLayout.setVisibility(View.VISIBLE);
                }

            }

            /**
             * 表示没有触发刷新时，进行重置
             */
            @Override
            public void onRefreshReset() {
                LogUtils.i("重置显示显示");
                searchTitleLayout.setVisibility(View.VISIBLE);
            }

            /**
             * 返回顶部刷新布局的状态
             * @param refreshState
             */
            @Override
            public void onUpdateRefreshState(RecyclerRefreshLayout.REFRESH_STATE refreshState) {

            }

            /**
             * 返回刷新动画的开始与结束状态
             * @param animState
             */
            @Override
            public void onUpdateAnimState(RecyclerRefreshLayout.ANIM_STATE animState) {

            }

            /**
             * 表示刷新行为触发
             */
            @Override
            public void onRefresh() {
                //下拉刷新时，先清空大数据推荐
                if (null != recmdProducts && recmdProducts.size() > 0) {
                    recmdProducts.clear();
                    refreshConentAdapter.updateItemNumByType(Constants.HOME_RCM_TYPE);
                    refreshConentAdapter.notifyDataSetChanged();
                }
                if (null != homePresenter) {
                    homePresenter.loadHomeInfoData();
                }
            }
        });

        /**
         * 内容区域滚动监听，用来改变搜索标题的色值
         */
        homeRefreshLoadLayout.setOnRefreshScrollListener(new RefreshLoadLayout.OnRefreshScrollListener() {
            @Override
            public void onScrollToTop(int toTop) {
                if (toTop <= 0) {
                    bgColor = startValue;
                    statusAlpha = 0;
                    searchTitleLayout.setBackgroundResource(R.color.transparent);
                } else if (toTop >= mDistance) {
                    bgColor = endValue;
                    statusAlpha = 255;
                    searchTitleLayout.setBackgroundColor(bgColor);
                } else {
                    bgColor = (int) mEvaluator.evaluate(toTop / mDistance, startValue, endValue);
                    statusAlpha = 255;
                    searchTitleLayout.setBackgroundColor(bgColor);
                }

                homeStatusBarView.getBackground().setAlpha(statusAlpha);
            }
        });

        /**
         * 加载更多监听
         */
        homeRefreshLoadLayout.setOnLoadActionListener(new RefreshLoadLayout.OnLoadActionListener() {
           @Override
           public void onLoadMore() {
               if (null != homePresenter) {
                   homePresenter.loadRcmProductInfo();
               }
           }
       });

        /**
         * 对首页不同的模块添加不同的分割线
         */
        homeRefreshLoadLayout.addItemDecoration(new SpacesItemDecoration());
        //主布局中的布局管理器
        GridLayoutManager manager = new GridLayoutManager(getActivity(), 2);
        manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                //表示大数据，热销商品，占2份中的1份，也就是一半
                if (refreshConentAdapter.getItemViewType(position) == Constants.HOME_RCM_TYPE || refreshConentAdapter.getItemViewType(position) == Constants.HOME_HOT_SALE_TYPE) {
                    return 1;
                } else {
                    return 2;
                }
            }
        });
        homeRefreshLoadLayout.setLayoutManager(manager);
        homeRefreshLoadLayout.setAdapter(refreshConentAdapter);

    }

    @Override
    public void clearObject() {

    }

    @Override
    public void clearView() {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (null != homePresenter) {
            homePresenter.detachView();
            homePresenter = null;
        }
        ButterKnife.unbind(this);
    }

    @Override
    public void startLoading(int type) {

    }

    @Override
    public void hideLoading(int type) {

    }

    @Override
    public void showError(int type, String msg) {

    }

    /**
     * 更新大数据推荐集合
     * @param recommends 大数据数据集合
     */
    @Override
    public void updateRmdInfo(List<Product> recommends) {
        homeRefreshLoadLayout.onLoadSuccess();
        homeRefreshLoadLayout.setLoadMoreEnable(false);
        if (null != recommends && recommends.size() > 0) {
            recmdProducts.clear();
            recmdProducts.addAll(recommends);
            refreshConentAdapter.updateItemNumByType(Constants.HOME_RCM_TYPE);
            refreshConentAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 表示接收到首页数据 更新显示界面
     * @param homeData 首页数据对象
     */
    @Override
    public void updateHomeInfo(HomeData homeData) {
        homeRefreshLoadLayout.closeRefresh();
        if (null != homeData) {
            /**
             * 设置搜索关键字
             */
            String defaultKey = homeData.getDefaultKey();
            if (!TextUtils.isEmpty(defaultKey)) {
                searchKeyEt.setText(defaultKey);
            }
            /**
             * 更新首页轮播图
             */
            List<ImgData> slideDatas = homeData.getSlides();
            if (slideDatas != null && slideDatas.size() > 0) {
                slideItems.clear();
                slideItems.addAll(slideDatas);
                refreshConentAdapter.updateItemNumByType(Constants.HOME_SLIDE_TYPE);
            }

            /**
             * 更新图片广告
             */
            ImgData bigProm = homeData.getBigProm();
            if (null != bigProm) {
                imgAdDatas.clear();
                imgAdDatas.add(bigProm);
                refreshConentAdapter.updateItemNumByType(Constants.HOME_IMG_AD_TYPE);
            }

            /**
             * 更新快讯显示
             */
            List<NoticeData> noticeMsgs = homeData.geteMsgs();
            if (null != noticeMsgs) {
                noticeDatas.clear();
                noticeDatas.addAll(noticeMsgs);
                refreshConentAdapter.updateItemNumByType(Constants.HOME_NOTICE_TYPE);
            }

            /**
             * 更新热销商品显示
             */
            List<Product> recmdProds = homeData.getRecmdProds();
            if (null != recmdProds && recmdProds.size() > 0) {
                hotSaleProducts.clear();
                hotSaleProducts.addAll(recmdProds);
                refreshConentAdapter.updateItemNumByType(Constants.HOME_HOT_SALE_TYPE);
            }

            refreshConentAdapter.notifyDataSetChanged();
            /**
             * 设置加载更多可以使用
             */
            homeRefreshLoadLayout.setLoadMoreEnable(true);
        }
    }

    @Override
    public void updateHomeError(int type) {
        homeRefreshLoadLayout.closeRefresh();
    }

}
