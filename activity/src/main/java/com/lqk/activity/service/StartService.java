package com.lqk.activity.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import androidx.annotation.Nullable;

/**
 * @date 2018/9/13
 * @time 16:34
 * @remarks
 */
public class StartService extends Service {

    @Override
    public void onCreate() {
        super.onCreate();
    }

    // 启动 服务
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    // 启动 服务
//    @Override
//    public void onStart(Intent intent, int startId) {
//        super.onStart(intent, startId);
//    }



    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
