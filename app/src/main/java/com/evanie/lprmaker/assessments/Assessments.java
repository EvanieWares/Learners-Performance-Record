package com.evanie.lprmaker.assessments;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.evanie.lprmaker.DBHelper;
import com.evanie.lprmaker.DrawerBaseActivity;
import com.evanie.lprmaker.MainActivity;
import com.evanie.lprmaker.R;
import com.evanie.lprmaker.databinding.ActivityAssessmentsHomeBinding;

import java.util.ArrayList;

public class Assessments extends DrawerBaseActivity {

    ActivityAssessmentsHomeBinding activityAssessmentsHomeBinding;
    DBHelper db;
    Cursor cursor;
    ArrayList<String> assessmentTitle;

    TableLayout table, assessmentList;
    TableRow assessments, title, row;
    TextView txtAssessmentName;
    int views = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityAssessmentsHomeBinding = ActivityAssessmentsHomeBinding.inflate(getLayoutInflater());
        setContentView(activityAssessmentsHomeBinding.getRoot());
        allocateActivityTitle("Continuous Assessment");

        txtAssessmentName = (TextView) findViewById(R.id.txtAssessmentName);
        assessmentList = findViewById(R.id.assessmentTitle);
        assessments = new TableRow(this);
        assessments.setPadding(5,15,5,15);
        assessments.setBackgroundColor(Color.parseColor("#3490dc"));

        db = new DBHelper(this);
        assessmentTitle = db.allSubjects();

        if (assessmentTitle.size() > 0) {
            String assess = assessmentTitle.get(0);
            txtAssessmentName.setText(assess);
            viewAssessment(assess);
            for (int i = 0; i < assessmentTitle.size(); i++) {
                TextView textView = new TextView(this);
                String assessment = assessmentTitle.get(i);
                String text = "   " + assessment + "   ";
                textView.setText(text);
                textView.setTextSize(14);
                textView.setTextColor(Color.BLACK);
                textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (views > 0) {
                            table.removeAllViews();
                        }
                        viewAssessment(assessment);
                    }
                });
                assessments.addView(textView);
            }
            assessmentList.addView(assessments);
        }
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(Assessments.this, MainActivity.class));
        finish();
    }

    private void viewAssessment(String assessment) {
        txtAssessmentName.setText(assessment);
        table = findViewById(R.id.assessmentDetails);
        title = new TableRow(Assessments.this);
        title.setPadding(5,15,5,15);
        title.setBackgroundColor(Color.parseColor("#3490dc"));

        cursor = db.getAssessmentData(assessment);
        for (int a = 0; a < cursor.getColumnCount(); a++){
            TextView textView = new TextView(Assessments.this);
            String text = " " + cursor.getColumnName(a) + " ";
            textView.setText(text);
            textView.setTextSize(14);
            textView.setTextColor(Color.BLACK);
            title.addView(textView);
        }
        table.addView(title);

        while (cursor.moveToNext()){
            row = new TableRow(Assessments.this);
            row.setPadding(5,0,5,0);
            for (int a = 0; a < cursor.getColumnCount(); a++){
                TextView textView = new TextView(Assessments.this);
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
        views++;
    }
}