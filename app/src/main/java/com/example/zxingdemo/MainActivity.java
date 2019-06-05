package com.example.zxingdemo;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import cn.bingoogolapple.qrcode.core.QRCodeView;
import cn.bingoogolapple.qrcode.zbar.ZBarView;

public class MainActivity extends AppCompatActivity implements QRCodeView.Delegate {
    private  static  final  int REOUEST_CODE = 666;
    private ZBarView zBarView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //获取控件id
        zBarView = findViewById(R.id.zbarview);
        zBarView.setDelegate(this);

    }


    @Override
    protected void onStart() {
        super.onStart();
        //ActivityCompat检查和请求权限的类
        //Manifest.permission获取清单文件中所有支持的权限
        //PackageManager使用两个权限常量，一个PERMISSION_GRANTED：允许；一个PERMISSION_DENIED:不予许
        int havePermission = ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        if (havePermission == PackageManager.PERMISSION_GRANTED) {//允许使用相机
            zBarView.startCamera(); // 打开后置摄像头开始预览，但是并未开始识别
        //mZXingView.startCamera(Camera.CameraInfo.CAMERA_FACING_FRONT); // 打开前置摄像头开始预览，但是并未开始识别
            zBarView.startSpotAndShowRect(); // 显示扫描框，并开始识别
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA},
                    REOUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REOUEST_CODE) {
            if (permissions[0].equals(Manifest.permission.CAMERA)
                    &&grantResults[0] == PackageManager.PERMISSION_GRANTED){
                zBarView.startCamera(); // 打开后置摄像头开始预览，但是并未开始识别
        //mZXingView.startCamera(Camera.CameraInfo.CAMERA_FACING_FRONT); // 打开前置摄像头开始预览，但是并未开始识别
                zBarView.startSpotAndShowRect(); // 显示扫描框，并开始识别
            }
        }
    }

    @Override
    protected void onStop() {
        zBarView.stopCamera(); // 关闭摄像头预览，并且隐藏扫描框
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        zBarView.onDestroy(); // 销毁二维码扫描控件
        super.onDestroy();
    }

    @Override
    public void onScanQRCodeSuccess(String result) {
        Toast.makeText(getBaseContext(), result, Toast.LENGTH_LONG).show();
        // mZXingView.startSpot(); // 开始识别
    }

    @Override
    public void onCameraAmbientBrightnessChanged(boolean isDark) {
        // 这里是通过修改提示文案来展示环境是否过暗的状态，接入方也可以根据 isDark 的值来实现其他交互效果
        if (isDark) {
            //button.setVisibility(View.VISIBLE);
            zBarView.getScanBoxView().setTipText("环境过暗，请打开闪光灯");
        } else {
            //button.setVisibility(View.GONE);
            zBarView.getScanBoxView().setTipText("");
        }
    }

    @Override
    public void onScanQRCodeOpenCameraError() {
        Log.e("dt", "打开相机出错");
    }
}



