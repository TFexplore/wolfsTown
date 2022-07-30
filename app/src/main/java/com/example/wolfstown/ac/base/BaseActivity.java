package com.example.wolfstown.ac.base;

import android.app.Application;
import android.graphics.Rect;
import android.net.ConnectivityManager;
import android.net.Network;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.wolfstown.easyhttp.model.HttpServer;
import com.example.wolfstown.easyhttp.model.RequestHandler;
import com.hjq.base.action.KeyboardAction;
import com.hjq.http.EasyConfig;
import com.luck.picture.lib.utils.ToastUtils;
import com.tencent.mmkv.MMKV;

import okhttp3.OkHttpClient;

public class BaseActivity extends AppCompatActivity implements KeyboardAction {



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        initSdk(this.getApplication());


    }
    void initSdk(Application application){

        MMKV.initialize(application);

        // 网络请求框架初始化
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .build();

        EasyConfig.with(okHttpClient)
                .setLogEnabled(true)
                .setServer(new HttpServer())
                .setHandler(new RequestHandler(application))
                .setRetryCount(3)
                .into();

        Log.d("http", "initSdk: http");
        // 注册网络状态变化监听
        ConnectivityManager connectivityManager = ContextCompat.getSystemService(application, ConnectivityManager.class);
        if (connectivityManager != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            connectivityManager.registerDefaultNetworkCallback(new ConnectivityManager.NetworkCallback() {
                @Override
                public void onLost(@NonNull Network network) {
                    ToastUtils.showToast(application,"网络不可用");
                }
            });
        }
    }




}
