package com.lqk.butter.ui.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.jakewharton.rxbinding3.view.RxView;
import com.lqk.annotations.MyKotlinBindView;
import com.lqk.butter.R;
import com.lqk.butter.base.BaseActivity;
import com.lqk.butter.compiler.DoBinder;
import com.lqk.butter.widget.SweetPanelListView;
import com.lqk.butter.zxing.CaptureActivity;
import com.tbruyelle.rxpermissions2.RxPermissions;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 自定义滑动指示器
 * SweetPanelListView
 */
@Route(path = "/butter/main")
public class MainActivity extends BaseActivity implements SweetPanelListView.OnScrollPositionChangedListener {

    @MyKotlinBindView(myId = R.id.tv_butter_show)
    TextView textView;

    @OnClick(R.id.tv_butter_show)
    void onTextClicked() {
        startActivity(new Intent(this, com.lqk.butterknife.MainActivity.class));
    }

    @MyKotlinBindView(myId = R.id.tv_butter_rx)
    TextView textView1;

    @Override
    public int layoutId() {
        return R.layout.activity_main;
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @SuppressLint("CheckResult")
    @Override
    public void initView() {
        ButterKnife.bind(this);
        DoBinder.INSTANCE.bind(this);
        SweetPanelListView mListView = findViewById(R.id.lv_scroll);
        ScrollAdapter scrollAdapter = new ScrollAdapter();
        mListView.setAdapter(scrollAdapter);
        mListView.setCacheColorHint(Color.TRANSPARENT);
        mListView.setListener(this);

        RxPermissions rxPermissions = new RxPermissions(this);
        RxView.clicks(findViewById(R.id.tv_butter_rx))
                .compose(rxPermissions.ensure(Manifest.permission.CAMERA))
                .subscribe(it -> {
                    if (it) {
                        startActivity(new Intent(MainActivity.this, CaptureActivity.class));
                        Log.d("RxPermission", "" + it.toString());
                    } else {
                        Log.d("RxPermission", "" + it.toString());
                    }
                });
        runOnUiThread(() -> {

        });
    }

    public void abc(@Nullable String a) {
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onPositionChanged(SweetPanelListView sweetPanelListView, int position, View panelView) {
        ((TextView) panelView.findViewById(R.id.tv_scrollbar)).setText("Position:" + position);

        textView.setText(position + "Position");
        textView1.setText("Position" + position);
    }

    private class ScrollAdapter extends BaseAdapter {

        private int mNum = 100;

        @Override
        public int getCount() {
            return mNum;
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @SuppressLint("SetTextI18n")
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(MainActivity.this).inflate(R.layout.item_list, parent, false);
            }
            // item
            ((TextView) convertView.findViewById(R.id.tv_position)).setText("-- " + position + " --");
            convertView.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), ZoomActivity.class)));
            return convertView;
        }
    }

    private long firstTime = 0;

    @Override
    public void onBackPressed() {
//        long secondTime = System.currentTimeMillis();
//        if (secondTime - firstTime > 2000) {
//            Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
//            firstTime = secondTime;
//        } else {
//            BaseApplication.Companion.getInstance().checkIcon(Variable.INSTANCE.getOldIcon(), Variable.INSTANCE.getNewIcon());
//            BaseApplication.Companion.getInstance().clearActivity();
////            finish();
//        }
        super.onBackPressed();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        return super.onKeyUp(keyCode, event);
    }
}
