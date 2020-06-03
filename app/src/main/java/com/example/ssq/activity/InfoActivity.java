package com.example.ssq.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.widget.TextView;

import com.blankj.utilcode.util.AppUtils;
import com.example.ssq.R;
import com.example.ssq.base.BaseActivity;

import butterknife.BindView;

public class InfoActivity extends BaseActivity {


    @BindView(R.id.tv_version)
    TextView tv_version;

    @Override
    public int getContentViewResId() {
        return R.layout.activity_info;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        tv_version.setText("当前版本："+AppUtils.getAppVersionName());
    }

    //退出调用
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            Intent intent = new Intent();
            intent.setClass(getApplicationContext(), MainSsqActivity.class);
            startActivity(intent);
            finish();
        }
        return false;
    }
}