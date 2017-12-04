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
