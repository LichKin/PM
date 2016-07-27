package com.ricky.pm.dao;

import android.content.Context;

import com.j256.ormlite.dao.Dao;
import com.ricky.pm.db.DatabaseHelper;
import com.ricky.pm.model.AppInfo;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by liqi on 16/3/5.
 */
public class AppInfoDao {

    private Context context;
    private Dao<AppInfo,Integer> appInfoDao;
    private DatabaseHelper dbHelper;

    public AppInfoDao(Context context){

        this.context = context;
        try {
            dbHelper = DatabaseHelper.getHelper(context);
            appInfoDao = dbHelper.getDao(AppInfo.class);
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void add(AppInfo appInfo){
        try {
            appInfoDao.create(appInfo);
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public boolean update(AppInfo appInfo){
        try{
            appInfoDao.update(appInfo);
            return true;
        }catch (SQLException e){
            e.printStackTrace();
            return false;
        }

    }

    public void delete(int id){
        try{
            AppInfo appInfo = appInfoDao.queryForId(id);
            if(appInfo!=null){
                appInfoDao.delete(appInfo);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void delete(AppInfo appinfo){
        try{
            appInfoDao.delete(appinfo);
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public List<AppInfo> listByUserId(int userId){
        try{
            return appInfoDao.queryBuilder().where().eq("user_id",userId).query();
        }catch (SQLException e){
            e.printStackTrace();
            return null;
        }

    }

    public List<AppInfo> getAll(){
        try{
            List<AppInfo> appInfos = appInfoDao.queryForAll();
            return appInfos;
        }catch (SQLException e){
            e.printStackTrace();
            return null;
        }

    }
}
