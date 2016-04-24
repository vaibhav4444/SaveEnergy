package com.poc.saveenergy.myapplication.adapter;

/**
 * Created by Vaib on 24-04-2016.
 */
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.poc.saveenergy.myapplication.R;
import com.poc.saveenergy.myapplication.model.OnlineListModel;

import java.util.List;

public class OnlineListAdapter extends RecyclerView.Adapter<OnlineListAdapter.MyViewHolder> {

    private List<OnlineListModel> userList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView txt_name;


        public MyViewHolder(View view) {
            super(view);
            txt_name = (TextView) view.findViewById(R.id.txt_username);

        }
    }


    public OnlineListAdapter(List<OnlineListModel> userList) {
        this.userList = userList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.online_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        OnlineListModel onlineListModel = userList.get(position);
        holder.txt_name.setText(onlineListModel.getName());

    }

    @Override
    public int getItemCount() {
        return userList.size();
    }
}