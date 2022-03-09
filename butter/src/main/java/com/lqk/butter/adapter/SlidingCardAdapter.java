package com.lqk.butter.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lqk.butter.R;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * @author LQK
 * @time 2019/2/21 11:14
 * @remark
 */
public class SlidingCardAdapter extends RecyclerView.Adapter<SlidingCardAdapter.SlidingCardViewHolder> {

    private List<String> datas;

    public SlidingCardAdapter() {
        datas = new ArrayList<>();
        datas.add("火影忍者");
        datas.add("迪迦奥特曼");
        datas.add("樱桃小丸子");
        datas.add("大耳朵图图");
        datas.add("熊出没");
        datas.add("海贼王");
        datas.add("镇魂街");
        datas.add("火影忍者");
        datas.add("迪迦奥特曼");
        datas.add("樱桃小丸子");
        datas.add("大耳朵图图");
        datas.add("熊出没");
        datas.add("海贼王");
        datas.add("镇魂街");
    }

    public SlidingCardAdapter(List<String> datas) {
        this.datas = datas;
    }

    @NonNull
    @Override
    public SlidingCardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View root = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sliding_card, parent, false);
        return new SlidingCardViewHolder(root);
    }

    @Override
    public void onBindViewHolder(@NonNull SlidingCardViewHolder holder, int position) {
        ((TextView) holder.itemView.findViewById(R.id.tv_name)).setText(datas.get(position));
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    class SlidingCardViewHolder extends RecyclerView.ViewHolder {

        public TextView text;

        public SlidingCardViewHolder(@NonNull View itemView) {
            super(itemView);
            this.text = (TextView) itemView.findViewById(R.id.tv_name);
            itemView.setClickable(true);
        }
    }
}
