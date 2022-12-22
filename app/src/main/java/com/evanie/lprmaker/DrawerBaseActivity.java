package com.evanie.lprmaker;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.preference.PreferenceManager;

import com.evanie.lprmaker.others.HelpSupportActivity;
import com.evanie.lprmaker.others.SettingsActivity;
import com.evanie.lprmaker.students.StudentsHome;
import com.evanie.lprmaker.students.UpdateMarks;
import com.evanie.lprmaker.subjects.SubjectsHome;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class DrawerBaseActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout;
    FirebaseAuth mAuth;
    SharedPreferences preferences;
    final String keyAppMode = "appMode";
    final String valueOffline = "offline";
    public final String keyLatestVersionCode = "latestVersionCode";
    public final String keyCurrentVersionCode = "currentVersionCode";
    public final String keyLatestVersionName = "latestVersionName";

    @SuppressLint("InflateParams")
    @Override
    public void setContentView(View view) {
        drawerLayout = (DrawerLayout) getLayoutInflater().inflate(R.layout.activity_drawer_base, null);
        FrameLayout container = drawerLayout.findViewById(R.id.activityContainer);
        container.addView(view);
        super.setContentView(drawerLayout);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);

        Toolbar toolbar = drawerLayout.findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);

        NavigationView navigationView = drawerLayout.findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);

        try {
            String mode;
            mode = preferences.getString(keyAppMode, "");
            Menu menu = navigationView.getMenu();
            if (mode.equals(valueOffline)){
                menu.findItem(R.id.nav_logout).setVisible(false);
            }else {
                menu.findItem(R.id.nav_login).setVisible(false);
            }
        }catch(Exception e){
            Log.e("TAG", "Exception: " + e.getMessage());
        }
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        drawerLayout.closeDrawer(GravityCompat.START);
        final int nav_students = R.id.nav_students;
        final int nav_export_import = R.id.nav_export_import;
        final int nav_settings = R.id.nav_settings;
        final int nav_update_marks = R.id.nav_update_marks;
        final int nav_progress_record = R.id.nav_progress_record;
        final int nav_home = R.id.nav_home;
        final int nav_login = R.id.nav_login;
        final int nav_logout = R.id.nav_logout;
        final int nav_subjects = R.id.nav_subjects;
        final int nav_help_support = R.id.nav_help_support;

        switch (item.getItemId()){
            case nav_students:
                startActivity(new Intent(this, StudentsHome.class));
                overridePendingTransition(0,0);
                break;

            case nav_subjects:
                startActivity(new Intent(this, SubjectsHome.class));
                overridePendingTransition(0,0);
                break;

            case nav_settings:
                startActivity(new Intent(this, SettingsActivity.class));
                overridePendingTransition(0,0);
                break;

            case nav_update_marks:
                startActivity(new Intent(this, UpdateMarks.class));
                overridePendingTransition(0,0);
                break;

            case nav_progress_record:
                startActivity(new Intent(this, ProgressRecord.class));
                overridePendingTransition(0,0);
                break;

            case nav_home:
                startActivity(new Intent(this, MainActivity.class));
                overridePendingTransition(0,0);
                break;

            case nav_help_support:
                startActivity(new Intent(this, HelpSupportActivity.class));
                overridePendingTransition(0,0);
                break;

            case nav_export_import:
                startActivity(new Intent(this, ExportActivity.class));
                overridePendingTransition(0,0);
                break;

            case nav_login:
                startActivity(new Intent(this, LoginActivity.class));
                overridePendingTransition(0,0);
                break;

            case nav_logout:
                logoutAlert();
                overridePendingTransition(0,0);
                break;
        }
        return false;
    }

    protected void allocateActivityTitle(String titleString){
        if (getSupportActionBar() != null){
            getSupportActionBar().setTitle(titleString);
        }
    }

    /*public String getAppVersionName(String version){
        String versionName = null;
        int versionCode = 0;
        try {
            PackageInfo pInfo = this.getPackageManager().getPackageInfo(this.getPackageName(), 0);
            versionCode = pInfo.versionCode;
            versionName = pInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if (version.equals("code")) {
            return String.valueOf(versionCode);
        } else {
            return versionName;
        }
    }*/

    public void logoutAlert(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Are you sure you want to logout?");
        alertDialogBuilder.setPositiveButton("Yes", (dialogInterface, i) -> {
            if (Utils.isNetworkAvailable(DrawerBaseActivity.this)){
                mAuth = FirebaseAuth.getInstance();
                mAuth.signOut();
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            }else {
                Toast.makeText(getApplicationContext(), "Network is not available", Toast.LENGTH_SHORT).show();
            }
        });
        alertDialogBuilder.setNegativeButton("No", (dialogInterface, i) -> Toast.makeText(DrawerBaseActivity.this, "Cancelled", Toast.LENGTH_SHORT).show());

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    /*public boolean isNetworkAvailable(){
        try {
            ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = null;

            if (manager != null){
                networkInfo = manager.getActiveNetworkInfo();
            }
            return networkInfo != null && networkInfo.isConnected();
        }
        catch (NullPointerException e){
            return false;
        }
    }*/
}