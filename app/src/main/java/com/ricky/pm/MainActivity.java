package com.ricky.pm;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
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
    private TextView txtAdd;
    private LinearLayout layoutView;
    private LayoutInflater mInflater;
    private AlertDialog alertDialog;

    private EditText edtName, edtCount, edtPassword;
    private int uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setView();
        setEvent();
    }

    private void setView() {

        uid = getIntent().getIntExtra("userId", 0);
        mInflater = LayoutInflater.from(this);
        layoutView = (LinearLayout) mInflater.inflate(R.layout.view_dialog, null);
        edtName = (EditText) layoutView.findViewById(R.id.edt_name);
        edtCount = (EditText) layoutView.findViewById(R.id.edt_count);
        edtPassword = (EditText) layoutView.findViewById(R.id.edt_password);


        appInfoDao = new AppInfoDao(this);
        userDao = new UserDao(this);
        listView = (ListView) findViewById(R.id.listview);
        txtAdd = (TextView) findViewById(R.id.txt_add);
        appInfos = appInfoDao.listByUserId(uid);
        if (appInfos != null) {
            myAdapter = new MyAdapter(MainActivity.this, appInfos);
        } else {
            myAdapter = new MyAdapter(MainActivity.this, null);
        }
        listView.setAdapter(myAdapter);

    }

    private void setEvent() {

        txtAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (alertDialog == null) {
                    alertDialog = new AlertDialog.Builder(MainActivity.this).
                            setView(layoutView).setTitle("Add new appinfo").setPositiveButton("Add", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            User user = userDao.getUserById(uid);
                            AppInfo appInfo = new AppInfo(edtName.getText().toString(),
                                    edtCount.getText().toString(), edtPassword.getText().toString(), user);
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


}