package com.shen.refreshtest;

import android.app.Application;

import com.facebook.stetho.Stetho;
import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.shen.netclient.NetClient;
import com.shen.netclient.engine.NetClientLib;
import com.squareup.leakcanary.LeakCanary;

/**
 * Created by jerry shen on 2017/10/30.
 */

public class RefreshApp extends Application{

    public static RefreshApp refreshApp;

    public static RefreshApp getAppInstance() {
        return refreshApp;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        refreshApp = this;
        initNetClient();
        initStetho();
        initByGradleFile();
        initMemLeak();
    }


    private void initNetClient() {
        NetClientLib.getLibInstance().setMobileContext(this);
    }

    private void initStetho() {
        Stetho.initializeWithDefaults(this);
        NetClient.addNetworkInterceptor(new StethoInterceptor());
    }

    /*
    根据主项目中的gradle配置文件开初始化不同的开发模式
   */
    private void initByGradleFile() {

        if(Constants.TEST_MODE.equals(BuildConfig.MODE)){
            NetClientLib.getLibInstance().setLogEnable(true);
            NetClientLib.getLibInstance().setUrlConfigManager(R.xml.url);
        }
        else if(Constants.DEV_MODE.equals(BuildConfig.MODE))
        {
            NetClientLib.getLibInstance().setLogEnable(true);
            NetClientLib.getLibInstance().setServerBaseUrl(BuildConfig.SERVER_URL);
        }
        else if(Constants.RELEASE_MODE.equals(BuildConfig.MODE)){
            NetClientLib.getLibInstance().setLogEnable(false);
            NetClientLib.getLibInstance().setServerBaseUrl(BuildConfig.SERVER_URL);
        }
    }


    private void initMemLeak() {
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        LeakCanary.install(this);
    }

}
