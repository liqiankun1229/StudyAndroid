package com.lqk.activity.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import androidx.annotation.Nullable;
import android.util.Log;

/**
 * @date 2018/9/13
 * @time 16:40
 * @remarks
 */
public class BindService extends Service {

    private static final String TAG = "BindService";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "---- onBind() ----");
        return new MyBinder();
    }


    public class MyBinder extends Binder {
        BindService getService() {
            return BindService.this;
        }
    }
}
