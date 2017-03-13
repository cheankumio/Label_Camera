package com.example.c1103304.myapplication;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.core.ImagePipelineConfig;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static android.view.ViewGroup.*;

/**
 * Created by c1103304 on 2017/3/9.
 */

public class photoview extends AppCompatActivity {
    int head=0;
    List<Uri> uriList;
    float screen_x,screen_y;
    LinearLayout mainlayout;

    private int[] headStr = {R.string.ac00,R.string.si00,R.string.Fin00,R.string.hu00,
                        R.string.bs00,R.string.bd00,R.string.fix00};
    private int[][] ACStr = {{R.string.ac01,R.string.ac011,R.string.ac012},{R.string.ac02,R.string.ac021,R.string.ac022}
        ,{R.string.ac03,R.string.ac031,R.string.ac032},{R.string.ac04,R.string.ac041,R.string.ac042}
        ,{R.string.ac05,R.string.ac051,R.string.ac052},{R.string.ac06,R.string.ac061,R.string.ac062}
        ,{R.string.ac07,R.string.ac071,R.string.ac072}};
    private int[][] SIStr = {{R.string.ac01,R.string.ac011,R.string.ac012},{R.string.si02,R.string.si021,R.string.si022}
            ,{R.string.si03},{R.string.ac06,R.string.ac061,R.string.ac062}
            ,{R.string.ac07,R.string.ac071,R.string.ac072}};
    private int[][] FINStr = {{R.string.Fin01},{R.string.Fin02},{R.string.Fin03},{R.string.Fin04}};
    private int[][] HUStr = {{R.string.ac01,R.string.ac011,R.string.ac012},{R.string.ac02,R.string.ac021,R.string.ac022}
            ,{R.string.ac03,R.string.ac031,R.string.ac032},{R.string.hu04,R.string.ac041,R.string.ac022}
            ,{R.string.ac06,R.string.ac061,R.string.ac062},{R.string.ac07,R.string.ac071,R.string.ac072}};
    private int[][] BSStr = {{R.string.ac01,R.string.ac011,R.string.ac012},{R.string.bs02,R.string.bs021,R.string.bs022}
            ,{R.string.bs03,R.string.bs021,R.string.bs022},{R.string.bs04,R.string.bs021,R.string.bs022}
            ,{R.string.ac06,R.string.ac061,R.string.ac062},{R.string.ac07,R.string.ac071,R.string.ac072}};
    private int[][] BDStr = {{R.string.ac01,R.string.ac011,R.string.ac012},{R.string.bd02,R.string.ac021,R.string.ac022}
            ,{R.string.bd03,R.string.bs021,R.string.bs022},{R.string.ac05,R.string.ac051,R.string.ac052}
            ,{R.string.ac06,R.string.ac061,R.string.ac062},{R.string.ac07,R.string.ac071,R.string.ac072}};
    private int[][] otherStr = {{R.string.si031,R.string.ac041,R.string.ac022},{R.string.si032},{R.string.ac05,R.string.ac051,R.string.ac052}};
    private int[][] FixStr = {{R.string.fix01},{R.string.fix02},{R.string.fix03},{R.string.fix04},{R.string.fix05},{R.string.fix06}};
    private List<photodata> tmplist;
    private List<SimpleDraweeView> simpleDraweeViewList;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photoview);

        ImagePipelineConfig config = ImagePipelineConfig.newBuilder(this)
                .setDownsampleEnabled(true)
                .build();
        Fresco.initialize(this,config);

        Intent in = getIntent();
        head = in.getIntExtra("head",0);
        tmplist =  welcomePage.searchs(getString(headStr[head]));
        //welcomePage.searchs();
        screen_x = welcomePage.screen_x;
        screen_y = welcomePage.screen_y;
        mainlayout = (LinearLayout)findViewById(R.id.mainlayout);

        uriList = new ArrayList<>();
        simpleDraweeViewList = new ArrayList<>();

        TextView title = new TextView(this);
        title.setTextSize(26);
        title.setGravity(Gravity.CENTER);
        title.setText(getString(headStr[head]));
        mainlayout.addView(title);
        insertSpace(mainlayout,50);

        if(tmplist!=null) {
            for (int i = 0; i < tmplist.size(); i++) {
                Log.d("MYLOG", tmplist.get(i).getTotalInfo());
            }
        }else{
            Log.d("MYLOG", "tmplist is null");
        }


        switch(head){
            case 0:
                insertImage(ACStr);
                break;
            case 1:
                insertImage(SIStr);
                break;
            case 2:
                insertImage(FINStr);
                break;
            case 3:
                insertImage(HUStr);
                break;
            case 4:
                insertImage(BSStr);
                break;
            case 5:
                insertImage(BDStr);
                break;
            case 6:
                insertImage(FixStr);
                break;
        }

        for(int i=0;i<simpleDraweeViewList.size();i++){
            simpleDraweeViewList.get(i).setImageURI(uriList.get(i));
        }

        Button Upload = new Button(this);
        Upload.setText("確認上傳");
        Upload.setBackgroundColor(Color.BLACK);
        Upload.setTextColor(Color.WHITE);
        Upload.setTextSize(22);
        Upload.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(photoview.this, " Click ! ", Toast.LENGTH_SHORT).show();
            }
        });
        mainlayout.addView(Upload);
    }

    public void insertImage(int[][] classfication){
        for(int i=0;i<classfication.length;i++){
            for(int j=0;j<classfication[i].length;j++){

                textstyle(i,j,classfication);
                insertSpace(mainlayout,50);

                if(head==1 && i==2) {
                    for (int n = 0; n < otherStr.length; n++) {
                        for (int m = 0; m < otherStr[n].length; m++) {
                            textstyle(n, m, otherStr);
                            insertSpace(mainlayout, 50);

                            LinearLayout imagelayout = new LinearLayout(this);
                            imagelayout.setOrientation(LinearLayout.VERTICAL);
                            imagelayout.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
                            for (int k = 0; k < tmplist.size(); k++) {
                                if (tmplist.get(k).getLast_name().equals(getString(otherStr[n][m]))) {

                                    Uri uri = Uri.parse("file://" + welcomePage.imgdir + "/" + tmplist.get(k).getFilename());

                                    SimpleDraweeView mImg = new SimpleDraweeView(this);
                                    mImg.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
                                    mImg.setAspectRatio(1.33f);

                                    simpleDraweeViewList.add(mImg);
                                    uriList.add(uri);
                                    imagelayout.addView(mImg);
                                    insertSpace(imagelayout, 50);
                                    //Log.d("MYLOG", getString(classfication[i][j])+"photo: "+ welcomePage.imgdir+"/"+tmplist.get(k).getFilename());
                                }
                            }
                            mainlayout.addView(imagelayout);
                        }
                    }
                }
                if(j!=0 || head==2 || head==6){
                        LinearLayout imagelayout = new LinearLayout(this);
                        imagelayout.setOrientation(LinearLayout.VERTICAL);
                        imagelayout.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT));

                        for(int k=0;k<tmplist.size();k++){
                            if(tmplist.get(k).getLast_name().equals(getString(classfication[i][j]))
                                || (head ==2 && tmplist.get(k).getSub_name().equals(getString(classfication[i][j])))
                                || (head ==6 && tmplist.get(k).getSub_name().equals(getString(classfication[i][j])))
                                    ){
                                //Log.d("MYLOG",tmplist.get(k).getLast_name().toString()+" / "+getString(classfication[i][j]));
                                Uri uri = Uri.parse("file://"+welcomePage.imgdir+"/"+tmplist.get(k).getFilename());

                                SimpleDraweeView mImg = new SimpleDraweeView(this);
                                mImg.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT));
                                mImg.setAspectRatio(1.33f);

                                simpleDraweeViewList.add(mImg);
                                uriList.add(uri);
                                imagelayout.addView(mImg);
                                insertSpace(imagelayout,50);
                                Log.d("MYLOG", getString(classfication[i][j])+"photo: "+ welcomePage.imgdir+"/"+tmplist.get(k).getFilename());
                            }
                        }
                        mainlayout.addView(imagelayout);
                    }
                }
            }

    }


    /*
    * 插入中標題與小標題
    * */
    public void textstyle(int i,int j,int[][] classf){
        TextView subtitle = new TextView(this);
        subtitle.setTextSize(18);
        subtitle.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT));
        subtitle.setGravity(Gravity.CENTER);
        subtitle.setTextColor(Color.BLACK);
        if(j==0){
            subtitle.setBackgroundColor(Color.DKGRAY);
            subtitle.setTextSize(22);
            subtitle.setTextColor(Color.WHITE);
        }
        subtitle.setText(getString(classf[i][j]));
        mainlayout.addView(subtitle);
    }

    /*
    * 插入空白間距
    * */
    public void insertSpace(LinearLayout layout,int size){
        Space space = new Space(this);
        space.setMinimumHeight(size);
        layout.addView(space);
    }

/*
    public Bitmap getBitmapFrom(String file){
        try
        {
            BitmapFactory.Options options = new BitmapFactory.Options();
            //options.inJustDecodeBounds = true;
            options.inPreferredConfig = Bitmap.Config.RGB_565;
            //options.inPurgeable = true;
            //options.inInputShareable = true;
            options.inSampleSize = calculateInSampleSize(options, 200, 200);   // width，hight设为原来的十分一

            //String sd = Environment.getExternalStorageDirectory().toString();
            //Bitmap bitmap = BitmapFactory.decodeFile(sd + "/" + file);
            InputStream is = (InputStream) new URL(file).getContent();
            //Bitmap bitmap = ;
            return BitmapFactory.decodeStream(is,null,options);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }

    public int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }
*/
}
