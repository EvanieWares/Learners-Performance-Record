package com.evanie.lprmaker;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.widget.SearchView;

import com.evanie.lprmaker.databinding.ActivityViewStudentDataBinding;

public class ProgressRecord extends DrawerBaseActivity {

    ActivityViewStudentDataBinding activityViewStudentDataBinding;
    SharedPreferences sp;
    String rankBy;
    DBHelper db;
    Cursor cursor;

    TableLayout table;
    TableRow title, row;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityViewStudentDataBinding = ActivityViewStudentDataBinding.inflate(getLayoutInflater());
        setContentView(activityViewStudentDataBinding.getRoot());
        allocateActivityTitle("All Students Data");

        rankBy = "";
        sp = PreferenceManager.getDefaultSharedPreferences(this);
        rankBy = sp.getString("ranking", "");

        table = findViewById(R.id.tableMain);
        title = new TableRow(this);
        title.setPadding(5,15,5,15);
        title.setBackgroundColor(Color.parseColor("#3490dc"));

        db = new DBHelper(this);
        cursor = db.getRankedData(rankBy);
        for (int a = 0; a < cursor.getColumnCount()-1; a++){
            TextView textView = new TextView(this);
            String text = " " + cursor.getColumnName(a) + " ";
            textView.setText(text);
            textView.setTextSize(14);
            textView.setTextColor(Color.BLACK);
            title.addView(textView);
        }
        table.addView(title);

        while (cursor.moveToNext()){
            row = new TableRow(this);
            row.setPadding(5,0,5,0);
            for (int a = 0; a < cursor.getColumnCount()-1; a++){
                TextView textView = new TextView(this);
                String text = " " + cursor.getString(a) + " ";
                textView.setText(text);
                textView.setTextSize(12);
                if (!cursor.getColumnName(a).equals("NAME")) {
                    textView.setGravity(Gravity.CENTER);
                }
                row.addView(textView);
            }
            table.addView(row);
        }

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
                table.removeAllViews();

                title = new TableRow(ProgressRecord.this);
                title.setPadding(5,15,5,15);
                title.setBackgroundColor(Color.parseColor("#3490dc"));

                db = new DBHelper(ProgressRecord.this);
                cursor = db.getRankedData(rankBy);
                for (int a = 0; a < cursor.getColumnCount()-1; a++){
                    TextView textView = new TextView(ProgressRecord.this);
                    String text = " " + cursor.getColumnName(a) + " ";
                    textView.setText(text);
                    textView.setTextSize(14);
                    textView.setTextColor(Color.BLACK);
                    title.addView(textView);
                }
                table.addView(title);

                if (cursor.moveToFirst()){
                    do {
                        if (cursor.getString(1).contains(newText)){
                            row = new TableRow(ProgressRecord.this);
                            row.setPadding(5,0,5,0);
                            for (int a = 0; a < cursor.getColumnCount()-1; a++){
                                TextView textView = new TextView(ProgressRecord.this);
                                String text = " " + cursor.getString(a) + " ";
                                textView.setText(text);
                                textView.setTextSize(12);
                                if (!cursor.getColumnName(a).equals("NAME")) {
                                    textView.setGravity(Gravity.CENTER);
                                }
                                row.addView(textView);
                            }
                            table.addView(row);
                        }
                    }while (cursor.moveToNext());
                }
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }
}