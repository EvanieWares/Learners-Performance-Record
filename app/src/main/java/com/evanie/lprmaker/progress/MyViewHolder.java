package com.evanie.lprmaker.progress;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.evanie.lprmaker.R;

public class MyViewHolder extends RecyclerView.ViewHolder {

    TextView id, name, sex, arts, chichewa, english, maths, science, social, total;

    public MyViewHolder(@NonNull View itemView) {
        super(itemView);

        id = itemView.findViewById(R.id.rvID);
        name = itemView.findViewById(R.id.rvUserName);
        sex = itemView.findViewById(R.id.rvSex);
        arts = itemView.findViewById(R.id.rvArts);
        chichewa = itemView.findViewById(R.id.rvChichewa);
        english = itemView.findViewById(R.id.rvEnglish);
        maths = itemView.findViewById(R.id.rvMaths);
        science = itemView.findViewById(R.id.rvScience);
        social = itemView.findViewById(R.id.rvSocial);
        total = itemView.findViewById(R.id.rvTotal);
    }
}
