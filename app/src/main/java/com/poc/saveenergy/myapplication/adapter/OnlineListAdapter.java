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

import de.hdodenhof.circleimageview.CircleImageView;

public class OnlineListAdapter extends RecyclerView.Adapter<OnlineListAdapter.MyViewHolder> {

    private List<OnlineListModel> userList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView txt_name;
        private CircleImageView imgIsOnline;
        private CircleImageView imgUser;

        public MyViewHolder(View view) {
            super(view);
            txt_name = (TextView) view.findViewById(R.id.txt_username);
            imgIsOnline = (CircleImageView) view.findViewById(R.id.img_online);
            imgUser = (CircleImageView) view.findViewById(R.id.img_usr);

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
        if(onlineListModel.isOnline()){
            holder.imgIsOnline.setImageResource(R.drawable.circle_green);
        }
        else{
            holder.imgIsOnline.setImageResource(R.drawable.circle_red);
        }
        if(onlineListModel.getImageResource() != 0){
            holder.imgUser.setImageResource(onlineListModel.getImageResource());
        }
        else{
            holder.imgUser.setImageResource(R.drawable.img_default_user);
        }

    }

    @Override
    public int getItemCount() {
        return userList.size();
    }
    public void animateTo(List<OnlineListModel> models) {
        applyAndAnimateRemovals(models);
        applyAndAnimateAdditions(models);
        applyAndAnimateMovedItems(models);
    }

    private void applyAndAnimateRemovals(List<OnlineListModel> newModels) {
        for (int i = userList.size() - 1; i >= 0; i--) {
            final OnlineListModel model = userList.get(i);
            if (!newModels.contains(model)) {
                removeItem(i);
            }
        }
    }

    private void applyAndAnimateAdditions(List<OnlineListModel> newModels) {
        for (int i = 0, count = newModels.size(); i < count; i++) {
            final OnlineListModel model = newModels.get(i);
            if (!userList.contains(model)) {
                addItem(i, model);
            }
        }
    }

    private void applyAndAnimateMovedItems(List<OnlineListModel> newModels) {
        for (int toPosition = newModels.size() - 1; toPosition >= 0; toPosition--) {
            final OnlineListModel model = newModels.get(toPosition);
            final int fromPosition = userList.indexOf(model);
            if (fromPosition >= 0 && fromPosition != toPosition) {
                moveItem(fromPosition, toPosition);
            }
        }
    }

    public OnlineListModel removeItem(int position) {
        final OnlineListModel model = userList.remove(position);
        notifyItemRemoved(position);
        return model;
    }

    public void addItem(int position, OnlineListModel model) {
        userList.add(position, model);
        notifyItemInserted(position);
    }

    public void moveItem(int fromPosition, int toPosition) {
        final OnlineListModel model = userList.remove(fromPosition);
        userList.add(toPosition, model);
        notifyItemMoved(fromPosition, toPosition);
    }
}