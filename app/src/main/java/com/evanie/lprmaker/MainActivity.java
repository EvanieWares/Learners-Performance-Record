package com.evanie.lprmaker;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Gravity;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.evanie.lprmaker.databinding.ActivityMainBinding;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends DrawerBaseActivity {

    ActivityMainBinding activityMainBinding;
    String rankBy, show, mode, latestVersionName;
    PieChart overall, subject;
    TextView learnersAdded, allBoys, allGirls;
    TextView numberOne, numberTwo, numberThree;
    DBHelper DB;
    ArrayList<String> studentList;
    LinearLayout overallLayout = null;

    int latestVersionCode;
    int currentVersionCode = BuildConfig.VERSION_CODE;
    StorageReference storage = FirebaseStorage.getInstance().getReference();
    StorageReference latestAppPath;
    //FirebaseAuth mAuth;
    FirebaseAnalytics analytics;
    FirebaseRemoteConfig remoteConfig;
    int allLearners, malesCount, femalesCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //FirebaseInAppMessaging.getInstance().triggerEvent("main_activity_ready");
        analytics = FirebaseAnalytics.getInstance(this);
        activityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(activityMainBinding.getRoot());
        allocateActivityTitle("Home");

        retrieveGeneratedToken();
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        preferences.edit().putInt(keyCurrentVersionCode, currentVersionCode).apply();
        /*if (preferences.getLong(keyCurrentVersionCode, 0) < currentVersionCode) {

        }*/

        rankBy = preferences.getString("ranking", "");
        show = preferences.getString("pieChart", "");

        HashMap<String, Object> defaults = new HashMap<>();
        defaults.put("new_version_code", String.valueOf(currentVersionCode));

        //mAuth = FirebaseAuth.getInstance();
        remoteConfig = FirebaseRemoteConfig.getInstance();
        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                .setMinimumFetchIntervalInSeconds(10)
                .build();
        remoteConfig.setConfigSettingsAsync(configSettings);
        remoteConfig.setDefaultsAsync(defaults);
        remoteConfig.fetchAndActivate().addOnCompleteListener(this, task -> {
            if (task.isSuccessful()){
                latestVersionCode = (int) remoteConfig.getLong("new_version_code");
                latestVersionName = remoteConfig.getString("new_version_name");
                if (latestVersionCode > currentVersionCode){
                    preferences.edit().putInt(keyLatestVersionCode, latestVersionCode).apply();
                    preferences.edit().putString(keyLatestVersionName, latestVersionName).apply();
                    updateAvailable();
                }else{
                    preferences.edit().putInt(keyLatestVersionCode, currentVersionCode).apply();
                }
            }
        });

        malesCount = 0;
        femalesCount = 0;

        DB = new DBHelper(this);
        studentList = new ArrayList<>();

        overallLayout = findViewById(R.id.llOverall);
        learnersAdded = findViewById(R.id.learnersAdded);
        allBoys = findViewById(R.id.allBoys);
        allGirls = findViewById(R.id.allGirls);
        numberOne = findViewById(R.id.tvOne);
        numberTwo = findViewById(R.id.tvTwo);
        numberThree = findViewById(R.id.tvThree);

        //setting up textView
        TextView textView = new TextView(this);
        textView.setGravity(Gravity.CENTER);
        textView.setPadding(5,5,5,5);
        textView.setTextColor(getResources().getColor(R.color.black));
        textView.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.blue));
        textView.setBackground(ContextCompat.getDrawable(this, R.drawable.border2));

        TextView textView1 = new TextView(this);
        textView1.setGravity(Gravity.CENTER);
        textView1.setPadding(5,5,5,5);
        textView1.setTextColor(getResources().getColor(R.color.black));
        textView1.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.blue));
        textView1.setBackground(ContextCompat.getDrawable(this, R.drawable.border2));

        // load the summary
        summary();

        //show overall performance text
        textView.setText(R.string.overall_performance);
        //overallLayout.addView(textView);

        //overall performance pie chart
        HorizontalScrollView overallScrollView = new HorizontalScrollView(this);
        overall = new PieChart(this);
        overall.setMinimumWidth(600);
        overall.setMinimumHeight(600);
        populatePieChart(overall, "GRADE", "Overall Performance");
        //overallScrollView.addView(overall);
        //overallLayout.addView(overallScrollView);

        //show performance per subject text
        textView1.setText(R.string.performance_per_subject);
        overallLayout.addView(textView1);

        //performance per subject
        for (int i = 3; i < DB.getData().getColumnCount()-1; i++){
            subject = new PieChart(this);
            subject.setMinimumWidth(500);
            subject.setMinimumHeight(500);
            populatePieChart(subject, DB.getData().getColumnName(i), DB.getData().getColumnName(i));
            overallLayout.addView(subject);
        }
    }

    // setup pie charts for presenting student performance
    void populatePieChart(PieChart pieChart, String column, String text) {
        pieChart.setDrawHoleEnabled(true);
        pieChart.setUsePercentValues(!show.equals("values"));
        pieChart.setEntryLabelTextSize(12f);
        pieChart.setEntryLabelColor(Color.BLACK);
        pieChart.setCenterText(text);
        pieChart.setCenterTextSize(24);
        pieChart.getDescription().setEnabled(false);

        fillPieChartData(pieChart, column);
    }

    // fill in pie charts
    void fillPieChartData(PieChart pieChart, String column) {
        ArrayList<PieEntry> entries = new ArrayList<>();
        if (DB.allSubjects().size() > 0){
            if (column.equals("GRADE")){
                entries.add(new PieEntry(DB.gradingData(4, column), "Grade 4"));
                entries.add(new PieEntry(DB.gradingData(3, column), "Grade 3"));
                entries.add(new PieEntry(DB.gradingData(2, column), "Grade 2"));
                entries.add(new PieEntry(DB.gradingData(1, column), "Grade 1"));
            }
            else {
                entries.add(new PieEntry(DB.gradingData(4, column), "Excellent"));
                entries.add(new PieEntry(DB.gradingData(3, column), "Very Good"));
                entries.add(new PieEntry(DB.gradingData(2, column), "Good"));
                entries.add(new PieEntry(DB.gradingData(1, column), "Need Support"));
            }

            ArrayList<Integer> colors = new ArrayList<>();
            for (int color : ColorTemplate.MATERIAL_COLORS) {
                colors.add(color);
            }
            for (int color : ColorTemplate.VORDIPLOM_COLORS) {
                colors.add(color);
            }

            PieDataSet pieDataSet;
            if (!column.equals("NULL")){
                pieDataSet = new PieDataSet(entries, "REMARKS");
            }else {
                pieDataSet = new PieDataSet(entries, "SEX");
            }
            pieDataSet.setColors(colors);

            PieData data = new PieData(pieDataSet);
            data.setDrawValues(true);
            data.setValueFormatter(new PercentFormatter(pieChart));
            data.setValueTextSize(12f);
            data.setValueTextColor(Color.BLACK);

            pieChart.setData(data);
            pieChart.invalidate();
            pieChart.animateXY(2000, 2000);
        } else {
            Toast.makeText(this, "", Toast.LENGTH_SHORT).show();
        }
    }

    // get the summary of the student's performance
    private void summary(){
        Cursor cursor = DB.getRankedData();
        if (cursor.moveToFirst()){
            do {
                // get the total number of boys and girls
                if (cursor.getString(2).equals("M")){
                    malesCount++;
                }else {
                    femalesCount++;
                }

                // get all student names
                studentList.add(cursor.getString(1));
            }while (cursor.moveToNext());

            // initialize student count
            allBoys.setText(String.valueOf(malesCount));
            allGirls.setText(String.valueOf(femalesCount));
            allLearners = malesCount+femalesCount;
            learnersAdded.setText(String.valueOf(allLearners));

            // get the best students
            for (int i = 0; i < studentList.size(); i++){
                if (i == 0){
                    numberOne.setText(studentList.get(i));
                }else if (i == 1){
                    numberTwo.setText(studentList.get(i));
                }else if (i == 2){
                    numberThree.setText(studentList.get(i));
                }
            }
        }
    }
    @Override
    protected void onStart() {
        super.onStart();
        //FirebaseUser user = mAuth.getCurrentUser();

        //Check if user is signed in (non-null) and update UI accordingly.
        if (!Utils.isLoggedIn()){
            try {
                mode = preferences.getString(keyAppMode, "");
                if (!mode.equals(valueOffline)){
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                }
            }catch(Exception e) {
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
            }
        }
    }

    private boolean doubleBackToExitPressedOnce = false;
    @Override
    public void onBackPressed() {
        Toast toast = Toast.makeText(this, "Please click back again to exit", Toast.LENGTH_SHORT);
        if (doubleBackToExitPressedOnce){
            toast.cancel();
            finishAffinity();
        }
        this.doubleBackToExitPressedOnce = true;
        toast.show();
        new Handler(Looper.getMainLooper()).postDelayed(() -> doubleBackToExitPressedOnce = false, 2000);
    }

    // check if an update is available
    private void updateAvailable() {
        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setTitle("New Update Available")
                .setMessage(R.string.update_alert)
                .setPositiveButton("Download", (dialog, which) -> Utils.createDownloadLink(MainActivity.this, analytics, storage, latestVersionName))
                .setNegativeButton("Remind me later", (dialog, which) -> dialog.dismiss()).show();
        alertDialog.setCancelable(false);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // retrieve the registration token for firebase messaging
    private void retrieveGeneratedToken(){
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Log.w("TAG", "Fetching FCM registration token failed", task.getException());
                        return;
                    }

                    // Get new FCM registration token
                    String token = task.getResult();

                    // Log and toast
                    Log.d("TAG", "The token is: "+token);
                });
    }

    private void whatsNew(){

    }
}