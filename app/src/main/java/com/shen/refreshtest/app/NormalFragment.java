package com.shen.refreshtest.app;

import android.animation.ArgbEvaluator;
import android.graphics.drawable.AnimationDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shen.refresh.BaseLoadMoreView;
import com.shen.refresh.RecyclerRefreshLayout;
import com.shen.refresh.RefreshConentAdapter;
import com.shen.refresh.RefreshLoadLayout;
import com.shen.refresh.util.LogUtils;
import com.shen.refreshtest.R;
import com.shen.refreshtest.app.adapter.HotSaleAdapter;
import com.shen.refreshtest.app.adapter.HotSaleTitleAdapter;
import com.shen.refreshtest.app.adapter.ImgAdAdapter;
import com.shen.refreshtest.app.adapter.NoticeAdapter;
import com.shen.refreshtest.app.adapter.RecmdAdapter;
import com.shen.refreshtest.app.adapter.SlideAdapter;
import com.shen.refreshtest.core.SpacesItemDecoration;
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


    public static final int HOME_SLIDE_TYPE = 1;
    public static final int HOME_IMG_AD_TYPE = 2;
    public static final int HOME_NOTICE_TYPE = 3;

    public static final int HOME_HOT_SALE_TITLE_TYPE = 4;
    public static final int HOME_HOT_SALE_TYPE = 5;
    public static final int HOME_RCM_TYPE = 6;


    @Bind(R.id.home_search_title_layout)
    LinearLayout searchTitleLayout;


    @Bind(R.id.home_refresh_load_layout)
    RefreshLoadLayout homeRefreshLoadLayout;


    private HomePresenter homePresenter;

    private RefreshConentAdapter refreshConentAdapter;

    private SlideAdapter slideAdapter;
    private List<ImgData> slideItems;

    //图片广告
    private ImgAdAdapter imgAdAdapter;
    private List<ImgData> imgAdDatas;

    //快报
    private NoticeAdapter noticeAdapter;
    private List<NoticeData> noticeDatas;

    //热销商品
    private HotSaleAdapter hotSaleAdapter;
    private List<Product> hotSaleProducts = new ArrayList<>();

    //猜你喜欢标题
    private HotSaleTitleAdapter hotSaleTitleAdapter;

    //大数据推荐
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

        refreshConentAdapter.addAdapter(slideAdapter);
        refreshConentAdapter.addAdapter(imgAdAdapter);
        refreshConentAdapter.addAdapter(noticeAdapter);
        refreshConentAdapter.addAdapter(hotSaleTitleAdapter);
        refreshConentAdapter.addAdapter(hotSaleAdapter);
        refreshConentAdapter.addAdapter(recmdAdapter);
    }

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
            @Override
            public void onRefreshMoveToTop(float moveY) {
                LogUtils.i("刷新滑动距离：" + moveY);
                if (moveY > 20) {
                    searchTitleLayout.setVisibility(View.INVISIBLE);
                } else {
                    searchTitleLayout.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onRefreshReset() {
                LogUtils.i("重置显示显示");
                searchTitleLayout.setVisibility(View.VISIBLE);
            }

            @Override
            public void onUpdateRefreshState(RecyclerRefreshLayout.REFRESH_STATE refreshState) {

            }

            @Override
            public void onUpdateAnimState(RecyclerRefreshLayout.ANIM_STATE animState) {

            }

            @Override
            public void onRefresh() {
                //下拉刷新时，先清空大数据推荐
                if (null != recmdProducts && recmdProducts.size() > 0) {
                    recmdProducts.clear();
                    refreshConentAdapter.updateItemNumByType(HOME_RCM_TYPE);
                    refreshConentAdapter.notifyDataSetChanged();
                }
                if (null != homePresenter) {
                    homePresenter.loadHomeInfoData();
                }
            }
        });
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
       homeRefreshLoadLayout.setOnLoadActionListener(new RefreshLoadLayout.OnLoadActionListener() {
           @Override
           public void onLoadMore() {
               if (null != homePresenter) {
                   homePresenter.loadRcmProductInfo();
               }
           }
       });

        homeRefreshLoadLayout.addItemDecoration(new SpacesItemDecoration(20));
        //主布局中的布局管理器
        GridLayoutManager manager = new GridLayoutManager(getActivity(), 2);
        manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                //表示大数据，热销商品，占2份中的1份，也就是一半
                if (refreshConentAdapter.getItemViewType(position) == NormalFragment.HOME_RCM_TYPE || refreshConentAdapter.getItemViewType(position) == NormalFragment.HOME_HOT_SALE_TYPE) {
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

    @Override
    public void updateRmdInfo(List<Product> recommends) {
        homeRefreshLoadLayout.onLoadSuccess();
        homeRefreshLoadLayout.setLoadMoreEnable(false);
        if (null != recommends && recommends.size() > 0) {
            recmdProducts.clear();
            recmdProducts.addAll(recommends);
            refreshConentAdapter.updateItemNumByType(HOME_RCM_TYPE);
            refreshConentAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void updateHomeInfo(HomeData homeData) {
        homeRefreshLoadLayout.closeRefresh();
        if (null != homeData) {

            String defaultKey = homeData.getDefaultKey();
            if (!TextUtils.isEmpty(defaultKey)) {
                searchKeyEt.setText(defaultKey);
            }

            List<ImgData> slideDatas = homeData.getSlides();

            if (slideDatas != null && slideDatas.size() > 0) {
                slideItems.clear();
                slideItems.addAll(slideDatas);
                refreshConentAdapter.updateItemNumByType(NormalFragment.HOME_SLIDE_TYPE);
            }

            ImgData bigProm = homeData.getBigProm();
            if (null != bigProm) {
                imgAdDatas.clear();
                imgAdDatas.add(bigProm);
                refreshConentAdapter.updateItemNumByType(NormalFragment.HOME_IMG_AD_TYPE);
            }


            List<NoticeData> noticeMsgs = homeData.geteMsgs();
            if (null != noticeMsgs) {
                noticeDatas.clear();
                noticeDatas.addAll(noticeMsgs);
                refreshConentAdapter.updateItemNumByType(NormalFragment.HOME_NOTICE_TYPE);
            }


            List<Product> recmdProds = homeData.getRecmdProds();
            if (null != recmdProds && recmdProds.size() > 0) {
                hotSaleProducts.clear();
                hotSaleProducts.addAll(recmdProds);
                refreshConentAdapter.updateItemNumByType(NormalFragment.HOME_HOT_SALE_TYPE);
            }
            refreshConentAdapter.notifyDataSetChanged();
            homeRefreshLoadLayout.setLoadMoreEnable(true);
        }
    }

    @Override
    public void updateHomeError(int type) {

    }

}
