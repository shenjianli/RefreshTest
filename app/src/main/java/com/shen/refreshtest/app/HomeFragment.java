package com.shen.refreshtest.app;

import android.animation.ArgbEvaluator;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shen.refresh.HomeAdapter;
import com.shen.refresh.RecyclerLoadMoreView;
import com.shen.refresh.RecyclerRefreshLayout;
import com.shen.refresh.util.LogUtils;
import com.shen.refreshtest.R;
import com.shen.refreshtest.app.adapter.ImgAdAdapter;
import com.shen.refreshtest.app.adapter.SlideAdapter;
import com.shen.refreshtest.core.base.BaseFragment;
import com.shen.refreshtest.engine.HomePresenter;
import com.shen.refreshtest.engine.HomeView;
import com.shen.refreshtest.model.HomeData;
import com.shen.refreshtest.model.ImgData;
import com.shen.refreshtest.model.Product;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * Created by jerry shen on 2017/7/30.
 */

public class HomeFragment extends BaseFragment implements HomeView {


    public static final int HOME_SLIDE_TYPE = 1;
    public static final int HOME_IMG_AD_TYPE = 2;



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
//    @Bind(R.id.top_container)
//    LinearLayout topContainer;
    @Bind(R.id.home_search_title_layout)
    LinearLayout searchTitleLayout;

    private HomePresenter homePresenter;

    private HomeAdapter homeAdapter;

    private SlideAdapter slideAdapter;
    private List<ImgData> slideItems;

    //图片广告
    private ImgAdAdapter imgAdAdapter;
    private List<ImgData> imgAdDatas;


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
        initHomeView();
        return mHomeView;
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

        homeAdapter = new HomeAdapter();

        slideItems = new ArrayList<>();
        slideAdapter = new SlideAdapter(context, slideItems);

        imgAdDatas = new ArrayList<>();
        imgAdAdapter = new ImgAdAdapter(context,imgAdDatas);

        homeAdapter.addAdapter(slideAdapter);
        homeAdapter.addAdapter(imgAdAdapter);
    }

    @Override
    public void initView() {
        //searchTitleLayout = (LinearLayout) mHomeView.findViewById(R.id.home_search_title_layout);
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
        });

        homeLoadMoreLayout.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {

            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                mSumY += dy;
                //LogUtils.i("距离顶部距离：" + mSumY);
                if (mSumY <= 5) {
                    //searchTitleLayout.setBackgroundResource(R.drawable.titlebackimage);
                } else if (mSumY > 5 && mSumY < 220) {
                    //leLayout.setBackgroundResource(R.color.title_bg);
                    searchTitleLayout.getBackground().setAlpha(mSumY);
                } else {
//                    searchTitleLayout.setBackgroundResource(R.color.title_bg);
                    searchTitleLayout.getBackground().setAlpha(225);
                }
            }
        });
        homeRefreshLayout.setOnRefreshListener(new RecyclerRefreshLayout.OnRefreshListener() {
            @Override
            public void OnRefresh() {
                if(null != homePresenter){
                    homePresenter.loadHomeInfoData();
                }
            }
        });

        //主布局中的布局管理器
        GridLayoutManager manager = new GridLayoutManager(getActivity(), 2);
        manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return 2;
            }
        });
        homeLoadMoreLayout.setLayoutManager(manager);
        homeLoadMoreLayout.setAdapter(homeAdapter);


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

    }

    @Override
    public void updateHomeInfo(HomeData homeData) {
        homeRefreshLayout.closeRefresh();
        if (null != homeData) {

            String defaultKey = homeData.getDefaultKey();
            if (!TextUtils.isEmpty(defaultKey)) {
//                searchTv.setText(defaultKey);
            }

            List<ImgData> slideDatas = homeData.getSlides();

            if (slideDatas != null && slideDatas.size() > 0) {
                slideItems.clear();
                slideItems.addAll(slideDatas);
                homeAdapter.updateItemNumByType(HomeFragment.HOME_SLIDE_TYPE);
            }

            ImgData bigProm = homeData.getBigProm();
            if(null != bigProm){
                imgAdDatas.clear();
                imgAdDatas.add(bigProm);
                homeAdapter.updateItemNumByType(HomeFragment.HOME_IMG_AD_TYPE);
            }

            homeAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void updateHomeError(int type) {

    }

}
