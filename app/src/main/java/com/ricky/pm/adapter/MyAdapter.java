package com.ricky.pm.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ricky.pm.R;
import com.ricky.pm.model.AppInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ricky on 2016/1/29.
 */
public class MyAdapter extends BaseAdapter{

    private LayoutInflater inflater;
    private List<AppInfo> items = null;
    private Activity mAcitity;

    public MyAdapter(Activity activity, List<AppInfo> itemsT){

        mAcitity = activity;
        inflater = LayoutInflater.from(activity);
        if(itemsT!=null){
            items = itemsT;
        }else{
            items = new ArrayList<>();
        }
    }

    public void setItems(List<AppInfo> itemsT){
        if(itemsT!=null){
            items.clear();
            items.addAll(itemsT);
        }
        refresh();
    }

    public void addItem(AppInfo item){
        if(item!=null){
            items.add(item);
        }
        refresh();
    }

    public void refresh(){
        mAcitity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public AppInfo getItem(int i) {
        return items.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder = null;
        if(holder==null){
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.item_list,null);
            holder.name = (TextView) view.findViewById(R.id.item_name);
            holder.count = (TextView) view.findViewById(R.id.item_count);
            holder.password = (TextView) view.findViewById(R.id.item_pwd);

            view.setTag(holder);
        }else{
            holder = (ViewHolder) view.getTag();
        }
        AppInfo info = items.get(i);
        if(info!=null){
            holder.name.setText(info.getAppname());
            holder.count.setText(info.getAppcount());
            holder.password.setText(info.getApppassword());
        }

        return view;
    }

    class ViewHolder{
        TextView name;
        TextView count;
        TextView password;
    }
}
