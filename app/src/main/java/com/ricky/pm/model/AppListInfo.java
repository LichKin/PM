package com.ricky.pm.model;

import java.util.List;

/**
 * Created by liqi on 16/9/28.
 */
public class AppListInfo {


    private String username;
    private List<SimpleAppInfo> infos;


    public AppListInfo(String name,List<SimpleAppInfo> infos){
        this.infos = infos;
        username = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<SimpleAppInfo> getInfos() {
        return infos;
    }

    public void setInfos(List<SimpleAppInfo> infos) {
        this.infos = infos;
    }
}
