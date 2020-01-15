package com.example.myapplication.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;

import androidx.annotation.Nullable;

import com.example.myapplication.R;
import com.example.myapplication.base.BaseActivity;

public class InfoActivity extends BaseActivity {


    @Override
    public int getContentViewResId() {
        return R.layout.activity_info;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {

    }

    //退出调用
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            Intent intent = new Intent();
            intent.setClass(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }
        return false;
    }
}