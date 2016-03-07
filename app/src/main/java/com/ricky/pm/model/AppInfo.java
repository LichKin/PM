package com.ricky.pm.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

/**
 * Created by liqi on 16/3/4.
 */
@DatabaseTable(tableName = "db_appinfo")
public class AppInfo implements Serializable{

    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField(columnName = "appname")
    private String appname;

    @DatabaseField(canBeNull = true,foreign = true,columnName = "user_id")
    private User user;

    public String getApppassword() {
        return apppassword;
    }

    public void setApppassword(String apppassword) {
        this.apppassword = apppassword;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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


    @DatabaseField(columnName = "appcount")
    private String appcount;

    @DatabaseField(columnName = "apppassword")
    private String apppassword;

    public AppInfo(String name, String count, String password,User user) {
        this.appname = name;
        this.appcount = count;
        this.apppassword = password;
        this.user = user;

    }

    public AppInfo() {

    }

    @Override
    public String toString() {
        return "Appinfo:[id= " + id + ",appname= " + appname + ",appcount= "
                + appcount + ",apppassword= " + apppassword + ",user= "+user+"]";
    }
}
