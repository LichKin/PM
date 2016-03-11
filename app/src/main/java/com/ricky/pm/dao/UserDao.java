package com.ricky.pm.dao;

import android.content.Context;
import android.util.Log;

import com.j256.ormlite.dao.Dao;
import com.ricky.pm.db.DatabaseHelper;
import com.ricky.pm.model.User;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by liqi on 16/3/7.
 */
public class UserDao {

    private Context context;
    private Dao<User,Integer> userDao;
    private DatabaseHelper databaseHelper;

    public UserDao(Context context){
        this.context = context;
        try{
            databaseHelper = DatabaseHelper.getHelper(context);
            userDao = databaseHelper.getDao(User.class);
        }catch (SQLException e){
            e.printStackTrace();
        }

    }

    /**
     * add a new user
     * @param user
     * @return whether this operation is success
     */
    public boolean add(User user){
        try{
            userDao.create(user);
            Log.i("User","Add a new user");
            return true;
        }catch(SQLException e){
            e.printStackTrace();
            return false;
        }
    }

    /**
     * update user info
     * @param user
     * @return
     */
    public boolean update(User user){
        try{
            userDao.update(user);
            return true;
        }catch (SQLException e){
            e.printStackTrace();
            return false;
        }

    }

    /**
     * get all users stored in current device
     * @return
     */
    public List<User> getAll(){
        try{
            return userDao.queryForAll();
        }catch (SQLException e){
            e.printStackTrace();
            return  null;
        }
    }

    /**
     * get userinfo by name
     * @param name
     * @return
     */
    public User get(String name){

        try{
            List<User> users = userDao.queryBuilder().where().eq("name",name).query();
            if(users!=null&&users.size()>0){
                Log.i("User","Query success");
                return users.get(0);
            }
        }catch (SQLException e){
            e.printStackTrace();
            return  null;
        }
        return null;
    }

    /**
     * get userinfo by id
     * @param id
     * @return
     */
    public User getUserById(int id){
        try{
            return userDao.queryForId(id);
        }catch (SQLException e){
            e.printStackTrace();
            return null;
        }
    }

}
