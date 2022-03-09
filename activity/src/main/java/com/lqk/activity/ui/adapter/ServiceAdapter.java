package com.lqk.activity.ui.adapter;

import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.view.ViewGroup;

import com.lqk.activity.ui.activity.NotificationActivity;

import java.util.List;

/**
 * @author lqk
 * @date 2018/8/13
 * @time 14:30
 * @remarks
 */
public class ServiceAdapter extends RecyclerView.Adapter<ServiceAdapter.ViewHolder> implements View.OnClickListener {

    private Context context;
    private List<String> data;
    private OnItemClickListener onItemClickListener;

    public ServiceAdapter(Context context, List<String> data) {
        this.context = context;
        this.data = data;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        this.context.startActivity(new Intent(this.context, NotificationActivity.class));

        switch (v.getId()) {
            case 1:
                break;
            case 2:
                break;
        }
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }


    interface OnItemClickListener {
        public void OnClick();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
}
