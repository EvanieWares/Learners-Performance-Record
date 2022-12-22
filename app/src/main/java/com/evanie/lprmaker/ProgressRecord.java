package com.evanie.lprmaker;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.evanie.lprmaker.databinding.ActivityProgressRecordBinding;
import com.evanie.lprmaker.progress.Details;
import com.evanie.lprmaker.progress.RecordAdapter;

import java.util.ArrayList;

public class ProgressRecord extends DrawerBaseActivity {

    ActivityProgressRecordBinding activityProgressRecordBinding;
    SharedPreferences sp;
    String rankBy;
    RecyclerView recyclerView;
    RecordAdapter myAdapter;
    DBHelper dbHelper;
    ArrayList<Details> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityProgressRecordBinding = ActivityProgressRecordBinding.inflate(getLayoutInflater());
        setContentView(activityProgressRecordBinding.getRoot());
        allocateActivityTitle("Progress record");

        rankBy = "";
        sp = PreferenceManager.getDefaultSharedPreferences(this);
        rankBy = sp.getString("ranking", "");

        recyclerView = findViewById(R.id.rvRecord);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        list = new ArrayList<>();

        dbHelper = new DBHelper(this);
        Cursor cursor = dbHelper.getRankedData();
        if (cursor.moveToFirst()){
            do {
                Details student = new Details(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6), cursor.getString(7), cursor.getString(8), cursor.getString(9));
                list.add(student);
            }while (cursor.moveToNext());
        }
        Log.e("TAG", "The number of students is "+list.size());
        myAdapter = new RecordAdapter(this, list);
        recyclerView.setAdapter(myAdapter);

    }


    @Override
    public void onBackPressed() {
        startActivity(new Intent(ProgressRecord.this, MainActivity.class));
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search,menu);
        MenuItem menuItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setQueryHint("Type student name");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                newText = newText.toUpperCase();
                list.clear();
                Cursor cursor = dbHelper.getRankedData();
                if (cursor.moveToFirst()){
                    do {
                        if (cursor.getString(1).contains(newText)){
                            Details student = new Details(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6), cursor.getString(7), cursor.getString(8), cursor.getString(9));
                            list.add(student);
                        }
                    }while (cursor.moveToNext());
                    myAdapter = new RecordAdapter(getApplicationContext(), list);
                    recyclerView.setAdapter(myAdapter);
                }
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }
}