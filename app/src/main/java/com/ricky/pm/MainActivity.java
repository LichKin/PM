package com.ricky.pm;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.ricky.pm.adapter.MyAdapter;
import com.ricky.pm.dao.AppInfoDao;
import com.ricky.pm.dao.UserDao;
import com.ricky.pm.model.AppInfo;
import com.ricky.pm.model.User;

import java.util.List;

public class MainActivity extends Activity {

    private ListView listView;
    private MyAdapter myAdapter;
    private AppInfoDao appInfoDao;
    private UserDao userDao;
    private List<AppInfo> appInfos;
    private TextView txtAdd,txtUser;
    private LinearLayout layoutView;
    private LayoutInflater mInflater;
    private AlertDialog alertDialog;

    private EditText edtName, edtCount, edtPassword;
    private User mUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setView();
        setEvent();
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshList();
        refreshUser();
    }

    /**
     * initialize view
     */
    private void setView() {

        mUser = (User) getIntent().getSerializableExtra("user");
        mInflater = LayoutInflater.from(this);
        layoutView = (LinearLayout) mInflater.inflate(R.layout.view_dialog, null);
        edtName = (EditText) layoutView.findViewById(R.id.edt_name);
        edtCount = (EditText) layoutView.findViewById(R.id.edt_count);
        edtPassword = (EditText) layoutView.findViewById(R.id.edt_password);


        appInfoDao = new AppInfoDao(this);
        userDao = new UserDao(this);
        listView = (ListView) findViewById(R.id.listview);
        txtAdd = (TextView) findViewById(R.id.txt_add);
        txtUser = (TextView) findViewById(R.id.txt_user);
        appInfos = appInfoDao.listByUserId(mUser.getId());
        if (appInfos != null) {
            myAdapter = new MyAdapter(MainActivity.this, appInfos);
        } else {
            myAdapter = new MyAdapter(MainActivity.this, null);
        }
        listView.setAdapter(myAdapter);

    }


    private void setEvent() {

        txtUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(MainActivity.this,UserActivity.class);
                in.putExtra("user",mUser);
                startActivity(in);
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                AppInfo appInfo = myAdapter.getItem(i);
                Log.i("List item at:", i + "");
                Intent in = new Intent(MainActivity.this, DetailedActivity.class);
                in.putExtra("appinfo", appInfo);
                startActivity(in);
            }
        });

        txtAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (alertDialog == null) {
                    alertDialog = new AlertDialog.Builder(MainActivity.this).
                            setView(layoutView).setTitle("Add new appinfo").setPositiveButton("Add", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            AppInfo appInfo = new AppInfo(edtName.getText().toString(),
                                    edtCount.getText().toString(), edtPassword.getText().toString(),mUser);
                            appInfoDao.add(appInfo);
                            myAdapter.addItem(appInfo);
                            myAdapter.refresh();
                        }
                    }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    }).create();
                } else {
                    edtName.setText("");
                    edtCount.setText("");
                    edtPassword.setText("");
                    edtName.setHint("Please input appname");
                    edtCount.setHint("Please input appcount");
                    edtPassword.setHint("Please input apppassword");
                }
                alertDialog.show();
            }
        });
    }

    /**
     * refresh appinfo list
     */
    private void refreshList() {
        myAdapter.setItems(appInfoDao.listByUserId(mUser.getId()));
    }

    /**
     * refresh current user name
     */
    private void refreshUser(){
        txtUser.setText(userDao.getUserById(mUser.getId()).getName());
    }


}
