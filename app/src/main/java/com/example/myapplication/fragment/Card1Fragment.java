package com.example.myapplication.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.example.myapplication.R;
import com.example.myapplication.base.BaseFragment;

import static android.support.v4.content.ContextCompat.getDrawable;

public class Card1Fragment extends BaseFragment {

    private String fragData;
    private TextView title;
    private TextView ctime;
    private LinearLayout listNum;
    private LinearLayout Ll_bgm;

    public Card1Fragment() {
    }


    @Override
    public int getLayoutResId() {
        return R.layout.fragment_card1;
    }


    @Override
    public void init(Bundle savedInstanceState, View view) {
        if (getArguments() != null) {
            fragData = getArguments().getString("fragData");
        }
    }



    public static Card1Fragment newInstance() {
        return new Card1Fragment();
    }
}
