package com.example.c1103304.myapplication;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by c1103304 on 2017/3/9.
 */

public class photodata extends RealmObject{

    @PrimaryKey
    private String filename;
    private String serial;
    private String head_name;
    private String sub_name;
    private String last_name;
    private String totalInfo;
    private boolean isupload=false;

    public String getTotalInfo() {
        return totalInfo;
    }

    public void setTotalInfo(String totalInfo) {
        this.totalInfo = totalInfo;
    }




    public String getSerial() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }

    public String getHead_name() {
        return head_name;
    }

    public void setHead_name(String head_name) {
        this.head_name = head_name;
    }

    public String getSub_name() {
        return sub_name;
    }

    public void setSub_name(String sub_name) {
        this.sub_name = sub_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public boolean isupload() {
        return isupload;
    }

    public void setIsupload(boolean isupload) {
        this.isupload = isupload;
    }




}
