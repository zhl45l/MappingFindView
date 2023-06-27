package com.gc.mappingfindviewLibary;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gc.mappingfindview.MappingFindView;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyHolder> {
    List<String> datas;
    Context context;

    public MyAdapter(List<String> datas, Context context) {
        this.datas = datas;
        this.context = context;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_test1, parent, false);
        return new MyHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        String ss = datas.get(position);
        // 这里直接使用
        holder.tv_test1.setText("tv =  " + ss);
        holder.bt_bt1.setText("tv =  " + ss);
        holder.ll_panle.setBackgroundColor(Color.DKGRAY);
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        TextView tv_test1;
        LinearLayout ll_panle;
        Button bt_bt1;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            MappingFindView.mappingAdapter(this, itemView);
            // 之后可直接使用
        }
    }
}
