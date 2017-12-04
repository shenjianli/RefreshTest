package com.shen.refreshtest.app;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.shen.refreshtest.R;

import butterknife.ButterKnife;

public class CustomActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_normal);
        ButterKnife.bind(this);
        showDefaultFragment();
    }

    private CustomFragment customFragment;

    private final String CUSTOM_INDEX_STR = "1";

    /**
     * 显示默认fragment
     */
    private void showDefaultFragment() {


        customFragment = (CustomFragment) getSupportFragmentManager()
                .findFragmentByTag(CUSTOM_INDEX_STR);
        if (null == customFragment) {
            customFragment = CustomFragment.newInstance(CUSTOM_INDEX_STR);
        }
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_content, customFragment,
                        String.valueOf(CUSTOM_INDEX_STR))
                .commit();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
}
