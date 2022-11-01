package com.evanie.lprmaker.assessments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Gravity;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.evanie.lprmaker.DBHelper;
import com.evanie.lprmaker.DrawerBaseActivity;
import com.evanie.lprmaker.MainActivity;
import com.evanie.lprmaker.R;
import com.evanie.lprmaker.Utils;
import com.evanie.lprmaker.databinding.ActivityAssessmentsHomeBinding;

public class Assessments extends DrawerBaseActivity {

    ActivityAssessmentsHomeBinding activityAssessmentsHomeBinding;
    DBHelper db;
    Cursor cursor;
    TextView arts, chichewa, english, maths, science, social;
    TableLayout table;
    TableRow title;
    TableRow row;
    Button addNew, updateExisting;
    private static SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityAssessmentsHomeBinding = ActivityAssessmentsHomeBinding.inflate(getLayoutInflater());
        setContentView(activityAssessmentsHomeBinding.getRoot());
        allocateActivityTitle("Continuous Assessment");

        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        db = new DBHelper(this);
        arts = findViewById(R.id.artsAssessment);
        chichewa = findViewById(R.id.chichewaAssessment);
        english = findViewById(R.id.englishAssessment);
        maths = findViewById(R.id.mathsAssessment);
        science = findViewById(R.id.scienceAssessment);
        social = findViewById(R.id.socialAssessment);
        addNew = findViewById(R.id.addNewAssessment);
        updateExisting = findViewById(R.id.updateExistingAssessment);

        arts.setBackgroundColor(Color.parseColor("#3490dc"));
        changeSelectedAssessment(Utils.ARTS_TABLE);
        viewAssessment(Utils.ARTS_TABLE);
        //Toast.makeText(this, getResources().getString(R.string.chichewa), Toast.LENGTH_SHORT).show();

        addNew.setOnClickListener(v -> {
            db.addAssessment(getSelectedAssessment());
            updateAssessmentDialog();
        });

        updateExisting.setOnClickListener(v -> {
            updateAssessmentDialog();
        });

        arts.setOnClickListener(v -> {
            changeSelectedAssessment(Utils.ARTS_TABLE);
            Log.i("TAG", "Viewing "+Utils.ARTS_TABLE);
            arts.setBackgroundColor(Color.parseColor("#3490dc"));
            chichewa.setBackgroundColor(Color.parseColor("#ffffff"));
            english.setBackgroundColor(Color.parseColor("#ffffff"));
            maths.setBackgroundColor(Color.parseColor("#ffffff"));
            science.setBackgroundColor(Color.parseColor("#ffffff"));
            social.setBackgroundColor(Color.parseColor("#ffffff"));
            table.removeAllViews();
            viewAssessment(Utils.ARTS_TABLE);
        });

        chichewa.setOnClickListener(v -> {
            changeSelectedAssessment(Utils.CHICHEWA_TABLE);
            Log.i("TAG", "Viewing "+Utils.CHICHEWA_TABLE);
            chichewa.setBackgroundColor(Color.parseColor("#3490dc"));
            arts.setBackgroundColor(Color.parseColor("#ffffff"));
            english.setBackgroundColor(Color.parseColor("#ffffff"));
            maths.setBackgroundColor(Color.parseColor("#ffffff"));
            science.setBackgroundColor(Color.parseColor("#ffffff"));
            social.setBackgroundColor(Color.parseColor("#ffffff"));
            table.removeAllViews();
            viewAssessment(Utils.CHICHEWA_TABLE);
        });

        english.setOnClickListener(v -> {
            changeSelectedAssessment(Utils.ENGLISH_TABLE);
            Log.i("TAG", "Viewing "+Utils.ENGLISH_TABLE);
            english.setBackgroundColor(Color.parseColor("#3490dc"));
            chichewa.setBackgroundColor(Color.parseColor("#ffffff"));
            arts.setBackgroundColor(Color.parseColor("#ffffff"));
            maths.setBackgroundColor(Color.parseColor("#ffffff"));
            science.setBackgroundColor(Color.parseColor("#ffffff"));
            social.setBackgroundColor(Color.parseColor("#ffffff"));
            table.removeAllViews();
            viewAssessment(Utils.ENGLISH_TABLE);
        });

        maths.setOnClickListener(v -> {
            changeSelectedAssessment(Utils.MATHS_TABLE);
            Log.i("TAG", "Viewing "+Utils.MATHS_TABLE);
            maths.setBackgroundColor(Color.parseColor("#3490dc"));
            chichewa.setBackgroundColor(Color.parseColor("#ffffff"));
            english.setBackgroundColor(Color.parseColor("#ffffff"));
            arts.setBackgroundColor(Color.parseColor("#ffffff"));
            science.setBackgroundColor(Color.parseColor("#ffffff"));
            social.setBackgroundColor(Color.parseColor("#ffffff"));
            table.removeAllViews();
            viewAssessment(Utils.MATHS_TABLE);
        });

        science.setOnClickListener(v -> {
            changeSelectedAssessment(Utils.SCIENCE_TABLE);
            Log.i("TAG", "Viewing "+Utils.SCIENCE_TABLE);
            science.setBackgroundColor(Color.parseColor("#3490dc"));
            chichewa.setBackgroundColor(Color.parseColor("#ffffff"));
            english.setBackgroundColor(Color.parseColor("#ffffff"));
            maths.setBackgroundColor(Color.parseColor("#ffffff"));
            arts.setBackgroundColor(Color.parseColor("#ffffff"));
            social.setBackgroundColor(Color.parseColor("#ffffff"));
            table.removeAllViews();
            viewAssessment(Utils.SCIENCE_TABLE);
        });

        social.setOnClickListener(v -> {
            changeSelectedAssessment(Utils.SOCIAL_TABLE);
            Log.i("TAG", "Viewing "+Utils.SOCIAL_TABLE);
            social.setBackgroundColor(Color.parseColor("#3490dc"));
            chichewa.setBackgroundColor(Color.parseColor("#ffffff"));
            english.setBackgroundColor(Color.parseColor("#ffffff"));
            maths.setBackgroundColor(Color.parseColor("#ffffff"));
            science.setBackgroundColor(Color.parseColor("#ffffff"));
            arts.setBackgroundColor(Color.parseColor("#ffffff"));
            table.removeAllViews();
            viewAssessment(Utils.SOCIAL_TABLE);
        });
    }

    private void viewAssessment(String assessment) {
        table = findViewById(R.id.assessmentTable);
        title = new TableRow(this);
        title.setPadding(5,15,5,15);
        title.setBackgroundColor(Color.parseColor("#3490dc"));

        cursor = db.getAssessmentData(assessment);

        for (int a = 0; a < cursor.getColumnCount(); a++){
            TextView textView = new TextView(getApplicationContext());
            String text = " " + cursor.getColumnName(a) + " ";
            textView.setText(text);
            textView.setTextSize(14);
            textView.setTextColor(Color.BLACK);
            title.addView(textView);
        }
        table.addView(title);

        while (cursor.moveToNext()){
            row = new TableRow(getApplicationContext());
            row.setPadding(5,0,5,0);
            for (int a = 0; a < cursor.getColumnCount(); a++){
                TextView textView = new TextView(getApplicationContext());
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

    private void updateAssessmentDialog(){
        UpdateAssessmentDialog updateAssessmentDialog = new UpdateAssessmentDialog();
        updateAssessmentDialog.show(getSupportFragmentManager(), "update assessment");
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(Assessments.this, MainActivity.class));
        finish();
    }

    private void changeSelectedAssessment(String assessment){
        preferences.edit().putString("selectedAssessment", assessment).apply();
    }

    public static String getSelectedAssessment(){
        return preferences.getString("selectedAssessment", "");
    }
}