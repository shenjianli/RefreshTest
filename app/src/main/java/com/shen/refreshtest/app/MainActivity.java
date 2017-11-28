package com.shen.refreshtest.app;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.shen.netclient.util.LogUtils;
import com.shen.refreshtest.R;
import com.shen.refreshtest.engine.HomePresenter;
import com.shen.refreshtest.engine.HomeView;
import com.shen.refreshtest.model.HomeData;
import com.shen.refreshtest.model.Product;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity{

    @Bind(R.id.log)
    TextView log;
    @Bind(R.id.main_normal)
    Button mainNormal;
    @Bind(R.id.main_custom)
    Button mainCustom;
    @Bind(R.id.main_content)
    FrameLayout mainContent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        showDefaultFragment();
        initData();
    }

    private void initData() {

    }


    /**
     * 通过设置全屏，设置状态栏透明
     *
     * @param activity
     */
    private void fullScreen(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                //5.x开始需要把颜色设置透明，否则导航栏会呈现系统默认的浅灰色
                Window window = activity.getWindow();
                View decorView = window.getDecorView();
                //两个 flag 要结合使用，表示让应用的主体内容占用系统状态栏的空间
                int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
                decorView.setSystemUiVisibility(option);
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(Color.TRANSPARENT);
                //导航栏颜色也可以正常设置
//                window.setNavigationBarColor(Color.TRANSPARENT);
            } else {
                Window window = activity.getWindow();
                WindowManager.LayoutParams attributes = window.getAttributes();
                int flagTranslucentStatus = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
                int flagTranslucentNavigation = WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION;
                attributes.flags |= flagTranslucentStatus;
//                attributes.flags |= flagTranslucentNavigation;
                window.setAttributes(attributes);
            }
        }
    }


    private HomeFragment1 homeFragment;
    private final String HOME_INDEX_STR = "1";

    private void showDefaultFragment() {

        homeFragment = (HomeFragment1) getSupportFragmentManager()
                .findFragmentByTag(HOME_INDEX_STR);
        if (null == homeFragment) {
            homeFragment = HomeFragment1.newInstance(HOME_INDEX_STR);
        }
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_content, homeFragment,
                        String.valueOf(HOME_INDEX_STR))
                .commit();
        log.setText(homeFragment.getClass().getSimpleName());
    }





    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.main_normal, R.id.main_custom,R.id.main_test})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.main_normal:
                Intent intent = new Intent(this,NormalActivity.class);
                startActivity(intent);
                break;
            case R.id.main_custom:
                Intent customActivity = new Intent(this,CustomActivity.class);
                startActivity(customActivity);
                break;
            case R.id.main_test:
                Intent testIntent = new Intent(this,TestActivity.class);
                startActivity(testIntent);
                break;
            default:
                break;
        }
    }
}
