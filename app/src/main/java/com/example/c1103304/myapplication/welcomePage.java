package com.example.c1103304.myapplication;

import android.Manifest;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import io.realm.annotations.RealmModule;
import io.realm.internal.IOException;

/**
 * Created by c1103304 on 2017/3/6.
 */

public class welcomePage  extends AppCompatActivity {
    FragmentTransaction transaction;
    boolean permissionCheck = false;
    static public float screen_x,screen_y;
    RealmConfiguration config;
    static public Realm realm;
    static public String imgdir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)+"";



    // 設定Module 欄位 ( 資料格式 )
    @RealmModule(classes = {photodata.class})
    public static class Module {
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        realmSetting();
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        screen_x =metrics.widthPixels;
        screen_y =metrics.heightPixels;

            new TedPermission(this)
                    .setDeniedMessage("請授予相機權限。")
                    .setRationaleConfirmText("確定")
                    .setDeniedCloseButtonText("關閉")
                    .setGotoSettingButtonText("設定")
                    .setPermissions(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
                    .setPermissionListener(new PermissionListener() {
                        @Override
                        public void onPermissionGranted() {
                            permissionCheck = true;
                        }

                        @Override
                        public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                            permissionCheck = false;
                        }
                    })
                    .check();
    }

    public void opencamera(View v){
        if(permissionCheck){
            Intent ns = new Intent();
            ns.setClass(this,CameraActivity.class);
            startActivity(ns);
        }else{
            Toast.makeText(this, "請給予APP權限", Toast.LENGTH_SHORT).show();
        }
    }

    // 設置資料庫
    private void realmSetting(){
        // Realm 基本屬性配置
        config = new RealmConfiguration.Builder(this)
                .name("database_name.realm")
                .setModules(new Module())
                .deleteRealmIfMigrationNeeded()
                .build();
        // 實例Realm，並設置其基本屬性config
        realm = Realm.getInstance(config);
    }

    public static void insertdata(photodata p){
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(p);
        realm.commitTransaction();
    }


    public static List searchs(String headName) {
        List<photodata> mPhotodata = new ArrayList<>();
        //if(mPhotodata!=null)mPhotodata.clear();
        RealmQuery<photodata> query = realm.where(photodata.class);
        query.contains("head_name",headName);
        RealmResults<photodata> result = query.findAll();
        for (photodata p : result) {
            Log.d("MYLOG","FileName: "+ p.getFilename()+" FileInfo: "+ p.getTotalInfo() +"\n HEAD: "+p.getHead_name()+" SUB: "+p.getSub_name()+" LAST: "+p.getLast_name());
            mPhotodata.add(p);
        }
        return  mPhotodata;
    }

    /*
    public static void searchs() {
        RealmQuery<photodata> query = realm.where(photodata.class);
        RealmResults<photodata> result = query.findAll();
        for (photodata p : result) {
            Log.d("MYLOG","FileName: "+ p.getFilename()+" FileInfo: "+ p.getTotalInfo() +"\n HEAD: "+p.getHead_name()+" SUB: "+p.getSub_name()+" LAST: "+p.getLast_name());
            mPhotodata.add(p);
        }
    }*/

    public void photoView(View v) {
        Intent ns = new Intent();
        ns.setClass(this,choose.class);
        startActivity(ns);
    }



    @Override
    protected void onDestroy() {
        realm.close();

        super.onDestroy();
    }
}
