package com.ricky.pm;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.ricky.pm.adapter.MyAdapter;
import com.ricky.pm.configs.Config;
import com.ricky.pm.dao.AppInfoDao;
import com.ricky.pm.dao.UserDao;
import com.ricky.pm.model.AppInfo;
import com.ricky.pm.model.AppListInfo;
import com.ricky.pm.model.CommonInfo;
import com.ricky.pm.model.SimpleAppInfo;
import com.ricky.pm.model.User;
import com.ricky.pm.utils.SharedPreferenceUtil;

import java.io.IOException;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends Activity {

    private ListView listView;
    private MyAdapter myAdapter;
    private AppInfoDao appInfoDao;
    private UserDao userDao;
    private List<AppInfo> appInfos;
    private TextView txtAdd, txtUser;
    private LinearLayout layoutView;
    private LayoutInflater mInflater;
    private AlertDialog alertDialog;
    private ImageView imgSyc;

    private Gson mGson = new Gson();
    private OkHttpClient mOkHttpClient;
    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");
    private String localTime, remoteTime;

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
        imgSyc = (ImageView) findViewById(R.id.img_syc);
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
                Intent in = new Intent(MainActivity.this, UserActivity.class);
                in.putExtra("user", mUser);
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

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog confrim = new AlertDialog.Builder(MainActivity.this).setTitle("Delete this item?")
                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                AppInfo info = myAdapter.getItem(position);
                                appInfoDao.delete(info);
                                refreshList();
                            }
                        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).create();
                confrim.show();
                return true;
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
                                    edtCount.getText().toString(), edtPassword.getText().toString(), mUser);
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

        imgSyc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SyncPullTask().execute();
            }
        });
    }


    /**
     * Synchronized post data to server
     */
    private void syncPost() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String result = okPost(Config.CLOUD_SYNC_PATH,
                            mGson.toJson(new AppListInfo(mUser.getName(), SimpleAppInfo.toSimples(appInfos))));
                    if (result != null) {
                        CommonInfo info = mGson.fromJson(result, CommonInfo.class);
                        if (info.getCode() == 200) {
                            Log.i("Main Syc", "Success!");
                        } else {
                            processError("Main Syc Failed! caused by " + info.getMsg());
                        }
                    } else {
                        processError("Main Syc Failed! Caused by no response from server");
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    class SyncPullTask extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {
            try {
                String result = okGet(Config.CLOUD_PULL_PATH + "/" + mUser.getName());
                return result;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return "";
        }

        @Override
        protected void onPostExecute(String s) {
            if (s != "") {
                CommonInfo info = mGson.fromJson(s, CommonInfo.class);
                if (info.getCode() == 200 && info.getMsg() != null) {
                    Log.i("Info", info.getMsg());
                    AppListInfo listInfo = mGson.fromJson(info.getMsg(), AppListInfo.class);

                    remoteTime = listInfo.getInfos().get(0).getUpdatetime();
                    localTime = SharedPreferenceUtil.getUpdateTime();
                    //如果本地没有同步过数据,或者服务端数据比较新，将远端数据同步到本地
                    if (localTime == "" || Long.valueOf(remoteTime) > Long.valueOf(localTime)) {
                        Log.i("Sync", "Pull data from remote");
                        appInfoDao.addAll(SimpleAppInfo.toAppInfos(listInfo.getInfos()));
                    } else if (Long.valueOf(remoteTime) < Long.valueOf(localTime)) {
                        Log.i("Sync", "Push data to remote");
                        syncPost();
                    } else {
                        Log.i("Sync", "Not need to sync");
                    }

                } else if (info.getCode() == CommonInfo.CODE_NOT_SYNC) {
                    //第一次使用同步
                    syncPost();
                } else {
                    processError("请求失败，" + info.getMsg());
                }

            } else {
                processError("请求失败，返回信息为空");
            }
        }
    }


    private String okGet(String url) throws IOException {
        if (mOkHttpClient == null) {
            mOkHttpClient = new OkHttpClient();
        }
        Request mRequest = new Request.Builder().url(url).get().build();
        Response mResponse = mOkHttpClient.newCall(mRequest).execute();
        return mResponse.body().string();

    }

    private String okPost(String url, String body) throws IOException {
        if (mOkHttpClient == null) {
            mOkHttpClient = new OkHttpClient();
        }
        RequestBody mBody = RequestBody.create(JSON, body);
        Request mRequest = new Request.Builder().url(url).post(mBody).build();
        Response mResponse = mOkHttpClient.newCall(mRequest).execute();
        return mResponse.body().string();
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
    private void refreshUser() {
        txtUser.setText(userDao.getUserById(mUser.getId()).getName());
    }


    private void processError(String msg) {
        Log.e("MainActivty", msg);
    }


}
