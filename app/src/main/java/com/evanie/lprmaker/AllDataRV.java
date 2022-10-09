package com.evanie.lprmaker;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AllDataRV extends AppCompatActivity {

    private RecyclerView recyclerView;
    DBHelper db;
    Cursor cursor;
    SharedPreferences sp;
    String rankBy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_data_rv);

        rankBy = "";
        sp = PreferenceManager.getDefaultSharedPreferences(this);
        rankBy = sp.getString("ranking", "");

        recyclerView = findViewById(R.id.rvAllData);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);

        db = new DBHelper(this);
        cursor = db.getRankedData(rankBy);
        ArrayList<MyModel> models = new ArrayList<>();{
            for (int i = 0; i < cursor.getColumnCount()-1; i++){
                models.add(new MyModel(cursor.getColumnName(i)));
            }
        }

        MyRecyclerViewAdapter adapter = new MyRecyclerViewAdapter(models);
        recyclerView.setAdapter(adapter);
    }
}