package com.example.myapplication.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.myapplication.CardFragment;
import com.example.myapplication.R;
import com.example.myapplication.base.BaseActivity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author Administrator
 */
public class MainActivity extends BaseActivity implements View.OnClickListener {

    @Override
    public int getContentViewResId() {
        return R.layout.activity_main_ssq;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        verifyStoragePermissions(this);
        Log.i("获得文件内容：", readFromFile(checkFile()));
    }

    @OnClick({R.id.btn_add, R.id.btn_info})
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_add:
                Intent intent = new Intent();
                intent.setClass(getApplicationContext(), CardAddActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.btn_info:
                Intent intent1 = new Intent();
                intent1.setClass(getApplicationContext(), InfoActivity.class);
                startActivity(intent1);
                finish();
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
        File file = new File(url);
        StringBuilder content = new StringBuilder();
        try {
            InputStream instream = new FileInputStream(file);
            InputStreamReader inputreader = new InputStreamReader(instream);
            BufferedReader buffreader = new BufferedReader(inputreader);
            String line;
            //分行读取
            while ((line = buffreader.readLine()) != null) {
                content.append(line).append("\n");
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                CardFragment listCard = new CardFragment();
                Bundle bundle2 = new Bundle();
                //String json_str="{'blueball':'3','created':'2017-07-23 19:49:14','mark':'双色球','redball':'','type':1}";
                //String json_str=line.substring(1,line.length()-1);
                bundle2.putString("fragData", line);
                listCard.setArguments(bundle2);
                transaction.add(R.id.list_content, listCard);
                transaction.commit();
            }
            instream.close();
        } catch (IOException e) {
            Log.d("TestFile", e.getMessage());
        }
        return content.toString();
    }

    //退出调用
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            dialog_loginOut();
        }
        return false;
    }

    //退出提示
    protected void dialog_loginOut() {
        new AlertDialog.Builder(this)
                .setTitle("喂！")
                .setMessage("就这样离开吗？")
                .setPositiveButton("确定", (dialoginterface, i) -> {
                    Intent intent = new Intent(Intent.ACTION_MAIN);
                    intent.addCategory(Intent.CATEGORY_HOME);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    android.os.Process.killProcess(android.os.Process.myPid());
                })
                .setNegativeButton(getString(R.string.cancel), (dialog, which) -> dialog.dismiss()).show();
    }

    private static final int REQUEST_EXTERNAL_STORAGE = 1;

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
