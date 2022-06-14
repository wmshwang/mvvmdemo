package com.desaysv.mvvmdemo;

import android.app.Application;
import android.content.Intent;
import android.os.Build;

import androidx.multidex.MultiDex;

import dagger.hilt.android.HiltAndroidApp;

/**
 * 自定义Application
 */
@HiltAndroidApp
public class BaseApplication extends Application {
    private static BaseApplication application;
    private boolean isSplash = false;//是否已经打开启动页

    public static synchronized void setInstance(BaseApplication instance) {
        BaseApplication.application = instance;
    }

    /**
     * 获取Application
     * @return BaseApplication
     */
    public static synchronized BaseApplication getInstance() {
        if (application == null) {
            application = new BaseApplication();
        }
        return application;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        setInstance(this);

        MultiDex.install(this);// 初始化MultiDex
        startService();//判断服务是否已经起来
    }

    //判断服务是否已经起来
    private void startService() {
        if (MyService.getInstance() == null) {
            Intent service = new Intent(this, MyService.class);
            if (Build.VERSION.SDK_INT >= 26) {
                startForegroundService(service);
            } else {
                startService(service);
            }
        }
    }

    public boolean isSplash() {
        return isSplash;
    }

    public void setSplash(boolean splash) {
        isSplash = splash;
    }
}
