package com.ricky.pm;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.ricky.pm.dao.AppInfoDao;
import com.ricky.pm.model.AppInfo;

public class DetailedActivity extends Activity {

    private EditText edtName,edtCount,edtPwd;
    private Button btnSet,btnSave;
    private TextView txtBack;
    private AppInfo appInfo;
    private AppInfoDao appInfoDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        setView();
        setEvent();
    }

    @Override
    protected void onResume() {
        super.onResume();
        enableEdit(false);
    }

    private void setView(){

        appInfoDao = new AppInfoDao(this);
        appInfo = (AppInfo) getIntent().getSerializableExtra("appinfo");

        edtName = (EditText) findViewById(R.id.edt_app_name);
        edtName.setText(appInfo.getAppname());
        edtCount = (EditText) findViewById(R.id.edt_app_count);
        edtCount.setText(appInfo.getAppcount());
        edtPwd = (EditText) findViewById(R.id.edt_app_pwd);
        edtPwd.setText(appInfo.getApppassword());

        btnSet = (Button) findViewById(R.id.btn_set);
        btnSave = (Button) findViewById(R.id.btn_save);

        txtBack = (TextView) findViewById(R.id.txt_back);
    }

    private void setEvent(){

        btnSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                enableEdit(true);
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        txtBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void enableEdit(boolean enable){
        if(enable){
            edtName.setEnabled(true);
            edtCount.setEnabled(true);
            edtPwd.setEnabled(true);
        }else{
            edtName.setEnabled(false);
            edtCount.setEnabled(false);
            edtPwd.setEnabled(false);
        }
    }
}
