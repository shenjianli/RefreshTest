package com.shen.refreshtest.app;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.shen.netclient.util.LogUtils;
import com.shen.refreshtest.R;
import com.shen.refreshtest.core.StatusBarUtil;
import com.shen.refreshtest.core.StatusUtils;
import com.shen.refreshtest.engine.HomePresenter;
import com.shen.refreshtest.engine.HomeView;
import com.shen.refreshtest.model.HomeData;
import com.shen.refreshtest.model.Product;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NormalActivity extends AppCompatActivity{



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtil.setTransparentForImageViewInFragment(this, null);
        setContentView(R.layout.activity_normal);
        ButterKnife.bind(this);
        showDefaultFragment();
        initData();
    }

    private void initData() {

    }

//    private void setStatusBarUpperAPI21(){
//        Window window = getWindow();
//        //取消设置透明状态栏,使 ContentView 内容不再覆盖状态栏
//        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//
//        //需要设置这个 flag 才能调用 setStatusBarColor 来设置状态栏颜色
//        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//        //设置状态栏颜色
//        //由于setStatusBarColor()这个API最低版本支持21, 本人的是15,所以如果要设置颜色,自行到style中通过配置文件设置
//        window.setStatusBarColor(Color.TRANSPARENT);
//        ViewGroup mContentView = (ViewGroup) findViewById(Window.ID_ANDROID_CONTENT);
//        View mChildView = mContentView.getChildAt(0);
//        if (mChildView != null) {
//            //注意不是设置 ContentView 的 FitsSystemWindows, 而是设置 ContentView 的第一个子 View . 预留出系统 View 的空间.
//            ViewCompat.setFitsSystemWindows(mChildView, true);
//        }
//    }
////
////
//    private void setStatusBarUpperAPI19() {
//        Window window = getWindow();
//        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//
//        ViewGroup mContentView = (ViewGroup) findViewById(Window.ID_ANDROID_CONTENT);
//        int statusBarHeight = getStatusBarHeight();
//        int statusColor = getResources().getColor(R.color.colorPrimary);
//
//        View mTopView = mContentView.getChildAt(0);
//        if (mTopView != null && mTopView.getLayoutParams() != null &&
//                mTopView.getLayoutParams().height == statusBarHeight) {
//            //避免重复添加 View
//            mTopView.setBackgroundColor(statusColor);
//            return;
//        }
//        //使 ChildView 预留空间
//        if (mTopView != null) {
//            ViewCompat.setFitsSystemWindows(mTopView, true);
//        }
//
//        //添加假 View
//        mTopView = new View(this);
//        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, statusBarHeight);
//        mTopView.setBackgroundColor(statusColor);
//        mContentView.addView(mTopView, 0, lp);
//    }


    // 5.0版本以上
    private void setStatusBarUpperAPI21() {
        Window window = getWindow();
        //设置透明状态栏,这样才能让 ContentView 向上
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        //需要设置这个 flag 才能调用 setStatusBarColor 来设置状态栏颜色
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

        ViewGroup mContentView = (ViewGroup) findViewById(Window.ID_ANDROID_CONTENT);
        View mChildView = mContentView.getChildAt(0);
        if (mChildView != null) {
            //注意不是设置 ContentView 的 FitsSystemWindows, 而是设置 ContentView 的第一个子 View . 使其不为系统 View 预留空间.
            ViewCompat.setFitsSystemWindows(mChildView, false);
        }
    }


    // 4.4 - 5.0版本
    private void setStatusBarUpperAPI19() {
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        ViewGroup mContentView = (ViewGroup) findViewById(Window.ID_ANDROID_CONTENT);
        View statusBarView = mContentView.getChildAt(0);
        //移除假的 View
        if (statusBarView != null && statusBarView.getLayoutParams() != null &&
                statusBarView.getLayoutParams().height == getStatusBarHeight()) {
            mContentView.removeView(statusBarView);
        }
        //不预留空间
        if (mContentView.getChildAt(0) != null) {
            ViewCompat.setFitsSystemWindows(mContentView.getChildAt(0), false);
        }
    }


    private int getStatusBarHeight(){
        int result = 0;
        int resId = getResources().getIdentifier("status_bar_height","dimen","android");
        if(resId>0){
            result = getResources().getDimensionPixelSize(resId);
        }
        return result;
    }

    private NormalFragment normalFragment;

    private final String HOME_INDEX_STR = "1";

    private void showDefaultFragment() {
        normalFragment = (NormalFragment) getSupportFragmentManager()
                .findFragmentByTag(HOME_INDEX_STR);

        if (null == normalFragment) {
            normalFragment = NormalFragment.newInstance(HOME_INDEX_STR);
        }

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_content, normalFragment,
                        String.valueOf(HOME_INDEX_STR))
                .commit();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
}
