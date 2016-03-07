package com.ricky.pm.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.ricky.pm.model.AppInfo;
import com.ricky.pm.model.User;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by liqi on 16/3/3.
 */
public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

    private static final String TABLE_NAME = "sqlite_pm.db";

    private Map<String,Dao> daos = new HashMap<String,Dao>();

    private DatabaseHelper(Context context){
        super(context,TABLE_NAME,null,3);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource) {
        try{
            TableUtils.createTable(connectionSource, User.class);
            TableUtils.createTable(connectionSource, AppInfo.class);
        }catch (SQLException e){
            e.printStackTrace();
        }

    }


    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource, int i, int i1) {

        try {
            TableUtils.dropTable(connectionSource,User.class,true);
            TableUtils.dropTable(connectionSource,AppInfo.class,true);
            onCreate(sqLiteDatabase, connectionSource);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private static DatabaseHelper instance;

    public static synchronized DatabaseHelper getHelper(Context context){
        if(instance==null){
            synchronized (DatabaseHelper.class){
                if(instance==null){
                instance = new DatabaseHelper(context.getApplicationContext());
                }
            }
        }
        return instance;
    }

    public synchronized Dao getDao(Class clazz) throws SQLException{
        Dao dao = null;
        String className = clazz.getSimpleName();
        if(daos.containsKey(className)){
            dao = daos.get(className);
        }
        if(dao==null){
            dao = super.getDao(clazz);
            daos.put(className,dao);
        }
        return dao;
    }

    @Override
    public void close() {
        super.close();
        for(String key:daos.keySet()){

            Dao dao = daos.get(key);
            dao = null;
        }
    }
}
