package com.shen.refreshtest.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
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

    @OnClick({R.id.main_normal, R.id.main_custom})
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
        }
    }
}
