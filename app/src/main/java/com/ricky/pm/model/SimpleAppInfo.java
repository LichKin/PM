package com.ricky.pm.model;

import android.util.Log;

import com.ricky.pm.utils.SharedPreferenceUtil;
import com.ricky.pm.utils.TimeUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liqi on 16/9/28.
 */
public class SimpleAppInfo {

    private String appname;
    private String appcount;
    private String apppassword;
    private String updatetime="";

    public String getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(String updatetime) {
        this.updatetime = updatetime;
    }

    public String getAppname() {
        return appname;
    }

    public void setAppname(String appname) {
        this.appname = appname;
    }

    public String getAppcount() {
        return appcount;
    }

    public void setAppcount(String appcount) {
        this.appcount = appcount;
    }

    public String getApppassword() {
        return apppassword;
    }

    public void setApppassword(String apppassword) {
        this.apppassword = apppassword;
    }

    public static SimpleAppInfo toSimple(AppInfo info,String updatetime){
        SimpleAppInfo mSimpleAppInfo = new SimpleAppInfo();
        mSimpleAppInfo.setAppcount(info.getAppcount());
        mSimpleAppInfo.setAppname(info.getAppname());
        mSimpleAppInfo.setApppassword(info.getApppassword());
        mSimpleAppInfo.setUpdatetime(updatetime);
        return mSimpleAppInfo;
    }

    public static List<SimpleAppInfo> toSimples(List<AppInfo> infos){
        String localTime = String.valueOf(System.currentTimeMillis());
        Log.i("local update time", TimeUtil.timestamp2Date(localTime));
        SharedPreferenceUtil.storeUpdateTime(localTime);
        List<SimpleAppInfo> simples = new ArrayList<>();
        for(AppInfo info:infos){
            simples.add(toSimple(info,localTime));
        }
        return  simples;
    }

    public static AppInfo toAppInfo(SimpleAppInfo simpleInfo){
        AppInfo info = new AppInfo();
        info.setAppname(simpleInfo.getAppname());
        info.setAppcount(simpleInfo.getAppcount());
        info.setApppassword(simpleInfo.getApppassword());
        return  info;
    }

    public static List<AppInfo> toAppInfos(List<SimpleAppInfo> simpleAppInfos){
        List<AppInfo> apps = new ArrayList<>();
        for(SimpleAppInfo simple:simpleAppInfos){
            apps.add(toAppInfo(simple));
        }
        return apps;
    }

}
