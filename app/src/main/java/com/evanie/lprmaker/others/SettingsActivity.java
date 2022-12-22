package com.evanie.lprmaker.others;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

import com.evanie.lprmaker.AboutAppActivity;
import com.evanie.lprmaker.BuildConfig;
import com.evanie.lprmaker.DBHelper;
import com.evanie.lprmaker.MainActivity;
import com.evanie.lprmaker.R;
import com.evanie.lprmaker.Utils;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class SettingsActivity extends AppCompatActivity {

    MaterialCardView clearData, clearMarks, updateApp, aboutApp;
    TextView settingsAppVersionName, settingsLatestAppVersionName;
    StorageReference storage = FirebaseStorage.getInstance().getReference();
    //StorageReference latestAppPath;
    FirebaseAnalytics analytics;
    int latestVersionCode;
    String latestVersionName;
    DBHelper DB = new DBHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        analytics = FirebaseAnalytics.getInstance(this);
        setContentView(R.layout.activity_settings);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        latestVersionCode = preferences.getInt(Utils.keyLatestVersionCode, 0);
        latestVersionName = preferences.getString(Utils.keyLatestVersionName, "");

        clearData = findViewById(R.id.clearData);
        clearMarks = findViewById(R.id.clearMarks);
        updateApp = findViewById(R.id.cvUpdateApp);
        aboutApp = findViewById(R.id.cvAboutApp);
        settingsLatestAppVersionName = findViewById(R.id.settingsLatestAppVersionName);
        settingsAppVersionName = findViewById(R.id.settingsAppVersionName);
        String currentVersionName = settingsAppVersionName.getText().toString()+ Utils.getAppVersionName(SettingsActivity.this);
        settingsAppVersionName.setText(currentVersionName);
        String latestAppVersionName = settingsLatestAppVersionName.getText().toString()+latestVersionName;
        settingsLatestAppVersionName.setText(latestAppVersionName);

        if (latestVersionCode > BuildConfig.VERSION_CODE){
            updateApp.setVisibility(View.VISIBLE);
        }else {
            updateApp.setVisibility(View.INVISIBLE);
        }

        if (savedInstanceState == null){
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, new SettingsFragment())
                    .commit();
        }

        Toast.makeText(this, "Ranking by grades is temporarily disabled", Toast.LENGTH_SHORT).show();

        clearData.setOnClickListener(v -> clearDataAlert());
        clearMarks.setOnClickListener(v -> clearMarksAlert());
        aboutApp.setOnClickListener(v -> startActivity(new Intent(SettingsActivity.this, AboutAppActivity.class)));
        updateApp.setOnClickListener(v -> Utils.createDownloadLink(SettingsActivity.this, analytics, storage, latestVersionName));

    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(SettingsActivity.this, MainActivity.class));
        finish();
    }

    public static class SettingsFragment extends PreferenceFragmentCompat {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.preferences, rootKey);
        }
    }

    public void clearDataAlert(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Clear data").setMessage("Are you sure you want to clear all the data?");
        alertDialogBuilder.setPositiveButton("Yes", (dialogInterface, i) -> {
            DB = new DBHelper(getApplicationContext());
            DB.clearData();
            Toast.makeText(this, "Data cleared!", Toast.LENGTH_SHORT).show();
        });
        alertDialogBuilder.setNegativeButton("No", (dialogInterface, i) -> {

        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public void clearMarksAlert(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Clear marks").setMessage("Are you sure you want to clear all students' marks?");
        alertDialogBuilder.setPositiveButton("Yes", (dialogInterface, i) -> {
            DB = new DBHelper(getApplicationContext());
            DB.clearMarks();
            Toast.makeText(this, "Marks cleared!", Toast.LENGTH_SHORT).show();
        });
        alertDialogBuilder.setNegativeButton("No", (dialogInterface, i) -> {

        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

}