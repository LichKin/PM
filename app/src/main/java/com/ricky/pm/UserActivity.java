package com.ricky.pm;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ricky.pm.dao.UserDao;
import com.ricky.pm.model.User;

public class UserActivity extends Activity {

    /**
     * check or modify current user info
     */
    private EditText edtName,edtPwd;
    private Button btnSet,btnSave;
    private TextView txtBack;

    private UserDao userDao;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        setView();
        setEvent();
    }

    @Override
    protected void onResume() {
        super.onResume();
        enableEdit(false);
        refreshUser();
    }

    private void setView(){

        user = (User) getIntent().getSerializableExtra("user");
        userDao = new UserDao(this);
        edtName = (EditText) findViewById(R.id.edt_user_name);
        edtPwd = (EditText) findViewById(R.id.edt_user_password);
        btnSet = (Button) findViewById(R.id.btn_set);
        btnSave = (Button) findViewById(R.id.btn_save);
        txtBack = (TextView) findViewById(R.id.txt_back);
        if(user!=null){
            edtName.setText(user.getName());
            edtPwd.setText(user.getPassword());
        }
    }

    private void setEvent(){
        txtBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enableEdit(true);
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enableEdit(false);
                user.setName(edtName.getText().toString());
                user.setPassword(edtPwd.getText().toString());
                if(userDao.update(user)){
                    Toast.makeText(UserActivity.this,"Update success!",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(UserActivity.this,"Update failed!",Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void enableEdit(boolean enable){
        if(enable){
            edtPwd.setEnabled(true);
            edtName.setEnabled(true);
        }else{
            edtName.setEnabled(false);
            edtPwd.setEnabled(false);
        }

    }

    /**
     * update user info
     */
    private void refreshUser(){
        edtName.setText(userDao.getUserById(user.getId()).getName());
    }
}
