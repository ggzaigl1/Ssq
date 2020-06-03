package com.example.ssq.activity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.blankj.utilcode.util.BarUtils;
import com.example.ssq.R;
import com.example.ssq.base.BaseActivity;
import com.example.ssq.fragment.CardFragment;
import com.example.ssq.utils.FileSizeUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import butterknife.BindView;
import butterknife.OnClick;

public class MainSsqActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.Ll_title)
    LinearLayout Ll_title;
    @BindView(R.id.rv_view)
    RecyclerView mRecyclerView;

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private File file;
    FragmentManager mFragmentManager;
    String urlStr = Environment.getExternalStorageDirectory() + "/flcp/" + "ssq.txt";

    @Override
    public int getContentViewResId() {
        return R.layout.activity_main_ssq;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        BarUtils.addMarginTopEqualStatusBarHeight(Ll_title);
        BarUtils.setStatusBarColor(this, ContextCompat.getColor(this, R.color.red));
        verifyStoragePermissions(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);

        mFragmentManager = getSupportFragmentManager();
        Log.e("获得文件内容：", readFromFile(checkFile()));
    }

    @OnClick({R.id.btn_add, R.id.btn_info})
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_add:
                Intent intent = new Intent();
                intent.setClass(getApplicationContext(), AddCardActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.btn_info:
                Intent intent1 = new Intent();
                intent1.setClass(getApplicationContext(), InfoActivity.class);
                startActivity(intent1);
                finish();
                break;
            default:
                break;
        }
    }



    //读取文件
    public String checkFile() {
        String urlStr = Environment.getExternalStorageDirectory() + "/magicalchiken/" + "tmp.txt";
        //检查存储设备
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            try {
                File file = new File(urlStr);
                if (!file.exists()) {
                    file.getParentFile().mkdirs();
                    file.createNewFile();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(getApplicationContext(), "存储设备有问题", Toast.LENGTH_SHORT).show();
        }
        return urlStr;
    }

    //读取文件
    public String readFromFile(String url) {
        file = new File(url);
        StringBuilder content = new StringBuilder();
        try {
            InputStream instream = new FileInputStream(file);
            InputStreamReader inputreader = new InputStreamReader(instream);
            BufferedReader buffreader = new BufferedReader(inputreader);
            String line;
            //分行读取
            while ((line = buffreader.readLine()) != null) {
//                content.append(line).append("\n");
                content.append(line);
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                CardFragment listCard = new CardFragment();
                Bundle bundle2 = new Bundle();
                //String json_str="{'blueball':'3','created':'2017-07-23 19:49:14','mark':'双色球','redball':'','type':1}";
                //String json_str=line.substring(1,line.length()-1);
                bundle2.putString("fragData", line);
                listCard.setArguments(bundle2);
                fragmentTransaction.add(R.id.list_content, listCard);
                fragmentTransaction.commitAllowingStateLoss();
            }
            instream.close();
        } catch (IOException e) {
            Log.d("TestFile", e.getMessage());
        }
        return content.toString();
    }


    private void ExitActivity() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    //退出调用
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            dialog_loginOut("温馨提示", "真的要退出吗", "确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialog_loginOut("系统提示", "是否清空数据", "确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            FileSizeUtil.DeleteFolder(urlStr);
                            ExitActivity();
                        }
                    }, "取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            ExitActivity();
                        }
                    });
                    ExitActivity();
                }
            }, "取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
        }
        return false;
    }

    //退出提示
    protected void dialog_loginOut(String title, String message, String PositiveButtonContext, DialogInterface.OnClickListener PositiveButton, String NegativeButtonContext, DialogInterface.OnClickListener NegativeButton) {
        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(PositiveButtonContext, PositiveButton)
                .setNegativeButton(NegativeButtonContext, NegativeButton).show();
    }

    public void verifyStoragePermissions(Activity activity) {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            String[] PERMISSIONS_STORAGE = {
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
            };
            int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

            if (permission != PackageManager.PERMISSION_GRANTED) {
                // We don't have permission so prompt the user
                ActivityCompat.requestPermissions(
                        activity,
                        PERMISSIONS_STORAGE,
                        REQUEST_EXTERNAL_STORAGE
                );
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_EXTERNAL_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "获取" + Manifest.permission.READ_EXTERNAL_STORAGE + "权限成功", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
