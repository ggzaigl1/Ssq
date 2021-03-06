package com.example.ssq.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.content.res.AppCompatResources;
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
import com.example.ssq.R;
import com.example.ssq.base.BaseFragment;

public class CardFragment extends BaseFragment {

    private String fragData;
    private TextView title;
    private TextView ctime;
    private LinearLayout listNum;
    private LinearLayout Ll_bgm;
    public TextView num1;

    private final static String Data = "fragData";

    public CardFragment() {
    }


    @Override
    public int getLayoutResId() {
        return R.layout.fragment_card;
    }


    @Override
    public void init(Bundle savedInstanceState, View view) {
        if (getArguments() != null) {
            fragData = getArguments().getString("fragData");
        }
        listNum = view.findViewById(R.id.list_num);
        Ll_bgm = view.findViewById(R.id.Ll_bgm);
        title = view.findViewById(R.id.title);
        ctime = view.findViewById(R.id.ctime);
        if (TextUtils.isEmpty(fragData)) {
            Ll_bgm.removeAllViews();
            Ll_bgm.setVisibility(View.GONE);
        } else {
            OnNextThread();
        }
    }

    public void OnNextThread() {
        Log.e("传递的参数是：", fragData.substring(2, fragData.length() - 2));
        new Thread() {
            @Override
            public void run() {
                try {
                    JSONObject js = JSON.parseObject(JSON.parseArray(fragData).get(0).toString());
                    Log.i("输出内容", "" + js.getString("created"));
                    title.setText(js.getString("mark"));
                    ctime.setText(js.getString("created"));
                    String balllist = js.getString("redball") + "," + js.getString("blueball");
                    String[] blist = balllist.split(",");
                    switch (js.getInteger("type")) {
                        case 1:
                            Log.i("输出字符串：", balllist);
                            for (int i = 0; i < 7; i++) {
                                num1 = new TextView(getActivity().getApplicationContext());
                                num1.setText(blist[i]);
                                num1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
                                num1.setGravity(Gravity.CENTER);
                                if (i > 5) {
                                    num1.setTextColor(Color.WHITE);
                                    num1.setBackground(AppCompatResources.getDrawable(getActivity().getApplicationContext(), R.drawable.bg_blue));
                                } else {
                                    num1.setTextColor(Color.WHITE);
                                    num1.setBackground(AppCompatResources.getDrawable(getActivity().getApplicationContext(), R.drawable.bg_red));
                                }
                                LinearLayout.LayoutParams btn_params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                                        ViewGroup.LayoutParams.WRAP_CONTENT);
                                btn_params.setMargins(4, 4, 4, 4);
                                btn_params.gravity = Gravity.CENTER_HORIZONTAL;
                                num1.setLayoutParams(btn_params);
                                listNum.addView(num1);
                            }
                            break;
                        case 2:
                            //大乐透
                            for (int i = 0; i < 7; i++) {
                                num1 = new TextView(getActivity().getApplicationContext());
                                num1.setText(blist[i]);
                                num1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
                                num1.setGravity(Gravity.CENTER);
                                if (i > 4) {
                                    num1.setTextColor(Color.WHITE);
                                    num1.setBackground(AppCompatResources.getDrawable(getActivity().getApplicationContext(), R.drawable.bg_blue));
                                } else {
                                    num1.setTextColor(Color.WHITE);
                                    num1.setBackground(AppCompatResources.getDrawable(getActivity().getApplicationContext(), R.drawable.bg_red));
                                }
                                LinearLayout.LayoutParams btn_params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                                        ViewGroup.LayoutParams.WRAP_CONTENT);
                                btn_params.setMargins(4, 4, 4, 4);
                                btn_params.gravity = Gravity.CENTER_HORIZONTAL;
                                num1.setLayoutParams(btn_params);
                                listNum.addView(num1);
                            }
                            break;
                        case 3:
                            num1 = new TextView(getActivity().getApplicationContext());
                            num1.setText(js.getString("charstr"));
                            num1.setTextColor(Color.BLACK);
                            num1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
                            num1.setGravity(Gravity.CENTER);
                            listNum.addView(num1);
                            break;
                    }
                } catch (JSONException e) {
                    Log.i("输出错误", e.toString());
                }
            }
        }.start();
    }


    public static CardFragment newInstance(String param1) {
        CardFragment fragment = new CardFragment();
        Bundle args = new Bundle();
        args.putString(Data, param1);
        fragment.setArguments(args);
        return fragment;
    }
}
