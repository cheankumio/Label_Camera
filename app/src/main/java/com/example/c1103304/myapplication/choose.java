package com.example.c1103304.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by c1103304 on 2017/3/9.
 */

public class choose extends AppCompatActivity implements View.OnClickListener {
    LinearLayout mainlayout;
    float screen_x,screen_y;
    int[] headname = {R.string.ac00,R.string.si00,R.string.Fin00,R.string.hu00,R.string.bs00,R.string.bd00,R.string.fix00};
    List<Button> mButton;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photoview);
        mButton = new ArrayList<>();
        mainlayout = (LinearLayout)findViewById(R.id.mainlayout);
        screen_x = welcomePage.screen_x;
        screen_y = welcomePage.screen_y;

        for(int i = 0 ; i < headname.length ; i++){
            Button button = new Button(this);
            button.setText(headname[i]);
            button.setId(headname[i]/3);
            button.setOnClickListener(this);
            mButton.add(button);
            mainlayout.addView(button);
        }
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case (R.string.ac00)/3:
                nextPage(0);
                break;
            case (R.string.si00)/3:
                nextPage(1);
                break;
            case (R.string.Fin00)/3:
                nextPage(2);
                break;
            case (R.string.hu00)/3:
                nextPage(3);
                break;
            case (R.string.bs00)/3:
                nextPage(4);
                break;
            case (R.string.bd00)/3:
                nextPage(5);
                break;
            case (R.string.fix00)/3:
                nextPage(6);
                break;
        }
    }

    public void nextPage(int head){
        Intent in = new Intent();
        in.setClass(this,photoview.class);
        in.putExtra("head",head);
        startActivity(in);
    }

}
