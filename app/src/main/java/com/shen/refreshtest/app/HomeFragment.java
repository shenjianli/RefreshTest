package com.shen.refreshtest.app;

import android.animation.ArgbEvaluator;
import android.graphics.drawable.AnimationDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.shen.refresh.LoadMoreDefaultView;
import com.shen.refresh.RefreshConentAdapter;
import com.shen.refresh.RecyclerLoadMoreView;
import com.shen.refresh.RecyclerRefreshLayout;
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

public class HomeFragment extends BaseFragment implements HomeView {


    public static final int HOME_SLIDE_TYPE = 1;
    public static final int HOME_IMG_AD_TYPE = 2;
    public static final int HOME_NOTICE_TYPE = 3;

    public static final int HOME_HOT_SALE_TITLE_TYPE = 4;
    public static final int HOME_HOT_SALE_TYPE = 5;
    public static final int HOME_RCM_TYPE = 6;



    @Bind(R.id.home_refresh_layout)
    RecyclerRefreshLayout homeRefreshLayout;

    @Bind(R.id.home_load_more_layout)
    RecyclerLoadMoreView homeLoadMoreLayout;
    @Bind(R.id.ib_sweep)
    TextView ibSweep;
    @Bind(R.id.et_search)
    EditText etSearch;
    @Bind(R.id.tv_news)
    TextView tvNews;

    @Bind(R.id.home_search_title_layout)
    LinearLayout searchTitleLayout;

    private HomePresenter homePresenter;

    private RefreshConentAdapter refreshConentAdapter;

    private SlideAdapter slideAdapter;
    private List<ImgData> slideItems;

    //图片广告
    private ImgAdAdapter imgAdAdapter;
    private List<ImgData> imgAdDatas;

    //快报
    private NoticeAdapter noticeAdapter;
    private List<NoticeData>   noticeDatas;

    //热销商品
    private HotSaleAdapter hotSaleAdapter;
    private List<Product> hotSaleProducts = new ArrayList<>();

    //猜你喜欢标题
    private HotSaleTitleAdapter hotSaleTitleAdapter;

    //大数据推荐
    private RecmdAdapter recmdAdapter;
    private List<Product> recmdProducts = new ArrayList<>();


    public static HomeFragment newInstance(String index) {
        HomeFragment homeFragment = new HomeFragment();
        Bundle bundle = new Bundle();
        bundle.putString("index", index);
        homeFragment.setArguments(bundle);
        return homeFragment;
    }


    private View mHomeView;

    //上拉透明效果变量
    private int mSumY = 0;
    private float mDistance = 200;  //控制透明度在距离150px的变化
    private int startValue = 0x50436EEE;
    private int endValue = 0xff436EEE;
    private int bgColor;
    private ArgbEvaluator mEvaluator;





    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        mHomeView = inflater.inflate(R.layout.fragment_home, container,false);
        ButterKnife.bind(this, mHomeView);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getActivity().getWindow().setStatusBarColor(0xAA000000);
        }
        super.onCreateView(inflater,container,savedInstanceState);
        initRefreshLayout();
        initLoadMoreLayout();
        initHomeView();
        return mHomeView;
    }

    private void initLoadMoreLayout() {
        BaseLoadMoreView loadmoreView  = new LoadMoreCustomView();
        homeLoadMoreLayout.addLoadMoveView(loadmoreView);
    }

    private TextView keyTextTv;
    private TextView refreshStateTv;
    private RelativeLayout pullRefreshLoadLayout;
    private ImageView refreshPeople;
    private ImageView refreshGoods;
    private ImageView refreshingLoadingImg;
    private AnimationDrawable refreshDrawable;

    private void initRefreshLayout() {
        View refreshHeaderLayout = View.inflate(getContext(), R.layout.refresh_head_layout, null);

        keyTextTv = (TextView) refreshHeaderLayout.findViewById(R.id.refresh_key_text);
        refreshStateTv = (TextView) refreshHeaderLayout.findViewById(R.id.refresh_state_tv);
        pullRefreshLoadLayout = (RelativeLayout) refreshHeaderLayout.findViewById(R.id.pull_refresh_load_layout);
        refreshPeople = (ImageView) refreshHeaderLayout.findViewById(R.id.pull_refresh_people);
        refreshGoods = (ImageView) refreshHeaderLayout.findViewById(R.id.pull_refresh_goods);
        refreshingLoadingImg = (ImageView) refreshHeaderLayout.findViewById(R.id.pull_refreshing_load_img);
        refreshDrawable = (AnimationDrawable) refreshingLoadingImg.getBackground();
        //homeRefreshLayout.addRefreshLayout(refreshHeaderLayout);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(null != homePresenter){
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
        imgAdAdapter = new ImgAdAdapter(context,imgAdDatas);


        noticeDatas = new ArrayList<>();
        noticeAdapter = new NoticeAdapter(context,noticeDatas);

        //热销商品
        hotSaleProducts = new ArrayList<>();
        hotSaleAdapter = new HotSaleAdapter(context,hotSaleProducts);

        //猜你喜欢标题
        hotSaleTitleAdapter = new HotSaleTitleAdapter(context,"");

        //大数据推荐
        recmdProducts = new ArrayList<>();
        recmdAdapter = new RecmdAdapter(context,recmdProducts);

        refreshConentAdapter.addAdapter(slideAdapter);
        refreshConentAdapter.addAdapter(imgAdAdapter);
        refreshConentAdapter.addAdapter(noticeAdapter);
        refreshConentAdapter.addAdapter(hotSaleTitleAdapter);
        refreshConentAdapter.addAdapter(hotSaleAdapter);
        refreshConentAdapter.addAdapter(recmdAdapter);
    }

    private EditText searchKeyEt;
    @Override
    public void initView() {
        //searchTitleLayout = (LinearLayout) mHomeView.findViewById(R.id.home_search_title_layout);
        searchKeyEt = (EditText) mHomeView.findViewById(R.id.et_search);
        homeRefreshLayout.setOnRefreshTouchListener(new RecyclerRefreshLayout.OnRefreshTouchListener() {
            @Override
            public void onRefreshMove(float moveY) {
                LogUtils.i("刷新滑动距离：" + moveY);
                if(moveY > 20){
                    searchTitleLayout.setVisibility(View.INVISIBLE);
                }
                else {
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
                if(refreshState == RecyclerRefreshLayout.REFRESH_STATE.PULL_TO_REFRESH){
                    refreshStateTv.setText("下拉刷新。。。");
                }
                else if(refreshState == RecyclerRefreshLayout.REFRESH_STATE.RELEASE_TO_REFRESH){
                    refreshStateTv.setText("松开刷新。。。");
                }
                else if(refreshState == RecyclerRefreshLayout.REFRESH_STATE.REFRESHING){
                    refreshStateTv.setText("正在刷新。。。");
                }
            }

            @Override
            public void onUpdateAnimState(RecyclerRefreshLayout.ANIM_STATE animState) {
                if(animState == RecyclerRefreshLayout.ANIM_STATE.ANIM_START){
                    refreshDrawable.start();
                    refreshingLoadingImg.setVisibility(View.VISIBLE);
                    pullRefreshLoadLayout.setVisibility(View.GONE);
                }
                else if(animState == RecyclerRefreshLayout.ANIM_STATE.ANIM_STOP){
                    refreshDrawable.stop();
                    refreshingLoadingImg.setVisibility(View.GONE);
                    pullRefreshLoadLayout.setVisibility(View.VISIBLE);
                }
            }
        });

        homeLoadMoreLayout.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {

            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                mSumY += dy;
                //LogUtils.i("距离顶部距离：" + mSumY);
//                if (mSumY <= 5) {
//                    //searchTitleLayout.setBackgroundResource(R.drawable.home_title_bg);
//                } else if (mSumY > 5 && mSumY < 220) {
//                    //leLayout.setBackgroundResource(R.color.title_bg);
//                    searchTitleLayout.getBackground().setAlpha(mSumY);
//                } else {
////                    searchTitleLayout.setBackgroundResource(R.color.title_bg);
//                    searchTitleLayout.getBackground().setAlpha(225);
//                }


                if (mSumY <= 0) {
                    bgColor = startValue;
                } else if (mSumY >= mDistance) {
                    bgColor = endValue;
                } else {
                    bgColor = (int) mEvaluator.evaluate(mSumY / mDistance, startValue, endValue);
                }
                searchTitleLayout.setBackgroundColor(bgColor);
            }
        });
        homeLoadMoreLayout.setOnLoadMoreListener(new RecyclerLoadMoreView.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                if(null != homePresenter){
                    homePresenter.loadRcmProductInfo();
                }
            }
        });
        homeRefreshLayout.setOnRefreshListener(new RecyclerRefreshLayout.OnRefreshListener() {
            @Override
            public void OnRefresh() {
                //下拉刷新时，先清空大数据推荐
                if(null != recmdProducts && recmdProducts.size() > 0){
                    recmdProducts.clear();
                    refreshConentAdapter.updateItemNumByType(HOME_RCM_TYPE);
                    refreshConentAdapter.notifyDataSetChanged();
                }
                if(null != homePresenter){
                    homePresenter.loadHomeInfoData();
                }
            }
        });

        homeLoadMoreLayout.addItemDecoration(new SpacesItemDecoration(20));
        //主布局中的布局管理器
        GridLayoutManager manager = new GridLayoutManager(getActivity(), 2);
        manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                //表示大数据，热销商品，占2份中的1份，也就是一半
                if( refreshConentAdapter.getItemViewType(position) == HomeFragment.HOME_RCM_TYPE || refreshConentAdapter.getItemViewType(position)== HomeFragment.HOME_HOT_SALE_TYPE){
                    return 1;
                }
                else {
                    return 2;
                }
            }
        });
        homeLoadMoreLayout.setLayoutManager(manager);
        homeLoadMoreLayout.setAdapter(refreshConentAdapter);


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
        homeLoadMoreLayout.onLoadSuccess();
        homeLoadMoreLayout.setLoadMoreEnable(false);
        if(null != recommends && recommends.size() > 0){
            recmdProducts.clear();
            recmdProducts.addAll(recommends);
            refreshConentAdapter.updateItemNumByType(HOME_RCM_TYPE);
            refreshConentAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void updateHomeInfo(HomeData homeData) {
        homeRefreshLayout.closeRefresh();
        if (null != homeData) {

            String defaultKey = homeData.getDefaultKey();
            if (!TextUtils.isEmpty(defaultKey)) {
                searchKeyEt.setText(defaultKey);
            }

            List<ImgData> slideDatas = homeData.getSlides();

            if (slideDatas != null && slideDatas.size() > 0) {
                slideItems.clear();
                slideItems.addAll(slideDatas);
                refreshConentAdapter.updateItemNumByType(HomeFragment.HOME_SLIDE_TYPE);
            }

            ImgData bigProm = homeData.getBigProm();
            if(null != bigProm){
                imgAdDatas.clear();
                imgAdDatas.add(bigProm);
                refreshConentAdapter.updateItemNumByType(HomeFragment.HOME_IMG_AD_TYPE);
            }


            List<NoticeData> noticeMsgs = homeData.geteMsgs();
            if(null != noticeMsgs){
                noticeDatas.clear();
                noticeDatas.addAll(noticeMsgs);
                refreshConentAdapter.updateItemNumByType(HomeFragment.HOME_NOTICE_TYPE);
            }


            List<Product> recmdProds = homeData.getRecmdProds();
            if(null != recmdProds && recmdProds.size() > 0){
                hotSaleProducts.clear();
                hotSaleProducts.addAll(recmdProds);
                refreshConentAdapter.updateItemNumByType(HomeFragment.HOME_HOT_SALE_TYPE);
            }
            refreshConentAdapter.notifyDataSetChanged();
            homeLoadMoreLayout.setLoadMoreEnable(true);
        }
    }

    @Override
    public void updateHomeError(int type) {

    }

}
