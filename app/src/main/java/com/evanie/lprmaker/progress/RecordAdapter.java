package com.evanie.lprmaker.progress;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.evanie.lprmaker.R;

import java.util.ArrayList;

public class RecordAdapter extends RecyclerView.Adapter<MyViewHolder> {

    Context context;
    ArrayList<Details> list;

    public RecordAdapter(Context context, ArrayList<Details> list){
        this.context = context;
        this.list = list;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.row_items, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Details student = list.get(position);
        holder.id.setText(student.getId());
        holder.name.setText(student.getName());
        holder.sex.setText(student.getSex());
        holder.arts.setText(student.getArts());
        holder.chichewa.setText(student.getChichewa());
        holder.english.setText(student.getEnglish());
        holder.maths.setText(student.getMaths());
        holder.science.setText(student.getScience());
        holder.social.setText(student.getSocial());
        holder.total.setText(student.getTotal());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
