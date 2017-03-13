package com.example.c1103304.myapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;

/**
 * Created by c1103304 on 2017/3/6.
 */

public class CameraActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.space);


        getFragmentManager().beginTransaction()
                .replace(R.id.space, MainActivity.newInstance())
                .commit();
    }
}
