package com.shen.refreshtest.app;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.shen.refreshtest.R;
import com.shen.refreshtest.core.StatusBarUtil;


/**
 * Created by ljq on 2017/11/24.
 */

public class TestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtil.setTransparent(this);
        setContentView(R.layout.activity_test);

    }
}
