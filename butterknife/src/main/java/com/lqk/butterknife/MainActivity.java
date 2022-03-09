package com.lqk.butterknife;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.android.arouter.launcher.ARouter;

import org.jetbrains.annotations.NotNull;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;



public class MainActivity extends AppCompatActivity {

    @BindView(R2.id.tv_show)
    TextView tvShow;

//    @BindView(R2.id.btn_change)
//    Button btnChange;
//    @MyKotlinBindView(myId = R2.id.btn_change)
//    Button btnChange;

    @OnClick(R2.id.tv_show)
    void onTextClick() {
        tvShow.setText("点击自己");
        ARouter.getInstance().build("/butter/main").navigation();
    }

    @OnClick(R2.id.btn_change)
    void onBtnClick() {
        tvShow.setText("点击了按钮");
        ARouter.getInstance().build("/butter/zoom").navigation();
    }

    @NotNull
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.butter_activity_main);
        ButterKnife.bind(this);
        button = findViewById(R.id.btn2);
    }
}
