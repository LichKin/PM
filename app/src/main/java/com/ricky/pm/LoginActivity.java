package com.ricky.pm;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ricky.pm.dao.UserDao;
import com.ricky.pm.model.User;

import java.util.List;

public class LoginActivity extends Activity {

    private static final String TAG = "Login";

    private EditText edtName,edtPwd;
    private Button btnLogin;
    private String name,pwd;
    private UserDao userDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initUser();
        initView();
        setEvent();
    }

    private void initView(){
        edtName = (EditText) findViewById(R.id.edt_uname);
        edtPwd = (EditText) findViewById(R.id.edt_pwd);
        btnLogin = (Button) findViewById(R.id.btn_login);
    }

    private void setEvent(){
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name = edtName.getText().toString().trim();
                pwd = edtPwd.getText().toString().trim();
                if (!validateInput(name, pwd)) {
                    showToast("Name or password is empty!");
                }
                User user = userDao.get(name);
                if (user != null) {
                    Log.i("UserInfo", user.getId() + "," + user.getName() + "," + user.getPassword());
                    if (user.getPassword().equals(pwd)) {
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.putExtra("user", user);
                        startActivity(intent);
                        LoginActivity.this.finish();
                    } else {
                        showToast("Error password!");
                    }
                } else {
                    showToast("No such user!");
                }

            }
        });
    }

    /**
     * Initialise user info
     * origin count：ricky
     * origin password：123123
     */
    private void initUser(){
        if(userDao==null){
            userDao = new UserDao(this);
        }
        Log.d(TAG,userDao.getAll().toString());
        if(userDao.getAll()==null||userDao.getAll().size()<=0){
            userDao.add(new User("ricky","123123"));
        }else{
            List<User> users = userDao.getAll();
            for(User user:users){
                Log.e(TAG,"name:"+user.getName()+";pwd:"+user.getPassword());
            }
        }
    }

    /**
     * Verify the inputs
     * @param name
     * @param pwd
     * @return
     */
    private boolean validateInput(String name,String pwd){
        if(name==null||pwd==null||name.equals("")||pwd.equals("")){
            return false;
        }
        return true;
    }

    private void showToast(String msg){
        Toast.makeText(LoginActivity.this,msg,Toast.LENGTH_SHORT).show();
    }
}
