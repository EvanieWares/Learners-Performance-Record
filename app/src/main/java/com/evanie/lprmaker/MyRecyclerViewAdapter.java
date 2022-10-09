package com.evanie.lprmaker;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.myViewHolder> {

    private final ArrayList<MyModel> models;

    public MyRecyclerViewAdapter(ArrayList<MyModel> models) {
        this.models = models;
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.single_item, parent, false);
        return new myViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {
        holder.textView.setText(models.get(position).getText());
    }

    @Override
    public int getItemCount() {
        return models.size();
    }

    static class myViewHolder extends RecyclerView.ViewHolder{
        TextView textView;
        @SuppressLint("ResourceType")
        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(1000030);
        }
    }
}
