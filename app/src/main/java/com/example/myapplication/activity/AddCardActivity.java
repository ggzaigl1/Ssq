package com.example.myapplication.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.blankj.utilcode.util.BarUtils;
import com.example.myapplication.R;
import com.example.myapplication.base.BaseActivity;

import org.json.JSONArray;

import java.io.File;
import java.io.RandomAccessFile;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Random;

import butterknife.BindView;
import butterknife.OnClick;

public class AddCardActivity  extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.case_result)
    TextView caseResult;
    @BindView(R.id.case_type_num)
    CheckBox caseTypeNum;
    @BindView(R.id.case_type_word)
    CheckBox caseTypeWord;
    @BindView(R.id.case_type_dxx)
    CheckBox caseTypeDXX;
    @BindView(R.id.case_length)
    EditText caseLength;
    @BindView(R.id.case_mark)
    TextView caseMark;
    @BindView(R.id.case_group)
    RadioGroup caseGroup;
    @BindView(R.id.case_zdy)
    LinearLayout caseZDY;
    @BindView(R.id.Ll_title)
    LinearLayout Ll_title;

    public JSONArray arrayResult = new JSONArray();
    public static final String bigChar = "ABCDEFGHJKLMNPQRSTUVWXYZ";
    public static final String smallChar = "abcdefghijkmnpqrstuvwxyz";
    public static final String numberChar = "0123456789";

    @Override
    public int getContentViewResId() {
        return R.layout.activity_cardadd;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        BarUtils.addMarginTopEqualStatusBarHeight(Ll_title);
        BarUtils.setStatusBarColor(this, ContextCompat.getColor(this,R.color.red));
        caseMark.setText("双色球");
        //单项选择
        caseGroup.setOnCheckedChangeListener((radioGroup, i) -> {
            int radioButtonId = radioGroup.getCheckedRadioButtonId();
            RadioButton rb = findViewById(radioButtonId);
            if (radioButtonId == R.id.other_case) {
                caseZDY.setVisibility(View.VISIBLE);
            } else {
                caseZDY.setVisibility(View.GONE);
            }
            caseMark.setText(rb.getText());
        });
    }

    @OnClick({R.id.btn_save, R.id.btn_create, R.id.btn_back})
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            //确认保存
            case R.id.btn_save:
                if (writeToFile(arrayResult.toString())) {
                    Toast.makeText(getApplicationContext(), "新的方案结果已经保存", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent();
                    intent.setClass(getApplicationContext(), MainSsqActivity.class);
                    startActivity(intent);
                    finish();
                }
                break;
            //返回按钮
            case R.id.btn_back:
                Intent intent = new Intent();
                intent.setClass(getApplicationContext(), MainSsqActivity.class);
                startActivity(intent);
                finish();
                break;
            //确认生成
            case R.id.btn_create:
                int checkId = caseGroup.getCheckedRadioButtonId();
                //双色球
                if (checkId == R.id.ssq_case) {
                    caseResult.setText(ssqCreate());
                }
                //大乐透
                if (checkId == R.id.dlt_case) {
                    caseResult.setText(dltCreate());
                }
                //自定义
                if (checkId == R.id.other_case) {
                    boolean iNum = caseTypeNum.isChecked();
                    boolean iWord = caseTypeWord.isChecked();
                    boolean iDXX = caseTypeDXX.isChecked();
                    if (caseLength.getText().length() > 0) {
                        int iLength = Integer.parseInt(caseLength.getText().toString());
                        if ((iNum | iWord | iDXX) && iLength > 0) {
                            caseResult.setText(zdyCreate(iNum, iWord, iDXX, iLength));
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "请输入方案长度", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
    }

    //写入文件
    public boolean writeToFile(String content) {
        String url = Environment.getExternalStorageDirectory() + "/magicalchiken/" + "tmp.txt";
        // 每次写入时，都换行写
        String strContent = content + "\r\n";
        try {
            File file = new File(url);
            RandomAccessFile raf = new RandomAccessFile(file, "rwd");
            raf.seek(file.length());
            raf.write(strContent.getBytes());
            raf.close();
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    //双色球生成方案
    public String ssqCreate() {
        int[] ssqRed = new int[6];
        int[] ssqBlue = new int[1];
        for (int i = 0; i < 6; i++) {
            int randomInt = (int) (Math.random() * 33) + 1;
            while (checkNum(ssqRed, randomInt)) {
                randomInt = (int) (Math.random() * 33) + 1;
            }
            ssqRed[i] = randomInt;
        }
        int randomInt = (int) (Math.random() * 16) + 1;
        ssqBlue[0] = randomInt;
        Arrays.sort(ssqRed);

        String ssqRedStr = Arrays.toString(ssqRed);
        String ssqBlueStr = Arrays.toString(ssqBlue);

        JSONObject jsonObject = new JSONObject();
        //赋值
        jsonObject.put("type", 1);
        jsonObject.put("redball", ssqRedStr.substring(1, ssqRedStr.length() - 1));
        jsonObject.put("blueball", ssqBlueStr.substring(1, ssqBlueStr.length() - 1));
        jsonObject.put("mark", caseMark.getText().toString());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        jsonObject.put("created", dateFormat.format(new Date()));
        //把JSONObject 添加到JSONArray
        arrayResult.put(jsonObject);
        Log.i("双色球的json", "" + arrayResult.toString());
        return ssqRedStr + ssqBlueStr;
    }

    //大乐透生成方案
    public String dltCreate() {
        int[] dltRed = new int[5];
        int[] dltBlue = new int[2];
        Random random = new Random();
        for (int i = 0; i < 5; i++) {
            int randomInt = random.nextInt(35) + 1;
            while (checkNum(dltRed, randomInt)) {
                randomInt = random.nextInt(35) + 1;
            }
            dltRed[i] = randomInt;
        }
        for (int i = 0; i < 2; i++) {
            int randomInt = random.nextInt(12) + 1;
            while (checkNum(dltBlue, randomInt)) {
                randomInt = random.nextInt(12) + 1;
            }
            dltBlue[i] = randomInt;
        }
        Arrays.sort(dltRed);
        Arrays.sort(dltBlue);

        String dltRedStr = Arrays.toString(dltRed);
        String dltBlueStr = Arrays.toString(dltBlue);

        JSONObject jsonObject = new JSONObject();
        //赋值
        jsonObject.put("type", 2);
        jsonObject.put("redball", dltRedStr.substring(1, dltRedStr.length() - 1));
        jsonObject.put("blueball", dltBlueStr.substring(1, dltBlueStr.length() - 1));
        jsonObject.put("mark", caseMark.getText().toString());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        jsonObject.put("created", dateFormat.format(new Date()));
        //把JSONObject 添加到JSONArray
        arrayResult.put(jsonObject);
        Log.i("大乐透的json", "" + arrayResult.toString());

        return Arrays.toString(dltRed) + Arrays.toString(dltBlue);
    }

    //自定义生成方案
    public String zdyCreate(boolean sNum, boolean sWord, boolean sDXX, int slength) {
        String seedStr = "";
        if (sNum) {
            seedStr = seedStr + numberChar;
        }
        if (sWord) {
            seedStr = seedStr + smallChar;
        }
        if (sDXX) {
            seedStr = seedStr + bigChar;
        }
        StringBuffer sb = new StringBuffer();
        Random random = new Random();
        for (int i = 0; i < slength; i++) {
            sb.append(seedStr.charAt(random.nextInt(seedStr.length())));
        }
        String sbStr = sb.toString();
        JSONObject jsonObject = new JSONObject();
        //赋值
        jsonObject.put("type", 3);
        jsonObject.put("charstr", sbStr);
        jsonObject.put("mark", caseMark.getText().toString());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        jsonObject.put("created", dateFormat.format(new Date()));
        //把JSONObject 添加到JSONArray
        arrayResult.put(jsonObject);
        Log.i("自定义的json", "" + arrayResult.toString());
        return sbStr;
    }

    //检测数字是否重复
    public boolean checkNum(int[] arr, int num) {
        boolean result = false;
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] == num) {
                result = true;
                break;
            }
        }
        return result;
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
