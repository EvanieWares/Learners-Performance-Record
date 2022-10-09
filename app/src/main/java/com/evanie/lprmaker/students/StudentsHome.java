package com.evanie.lprmaker.students;

import android.content.Intent;
import android.os.Bundle;

import com.evanie.lprmaker.DrawerBaseActivity;
import com.evanie.lprmaker.MainActivity;
import com.evanie.lprmaker.R;
import com.evanie.lprmaker.databinding.ActivityStudentDetailsHomeBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class StudentsHome extends DrawerBaseActivity {
    ActivityStudentDetailsHomeBinding activityStudentDetailsHomeBinding;
    BottomNavigationView bottomNavigationView;
    AddStudentsFragment addStudentsFragment = new AddStudentsFragment();
    RemoveStudentFragment removeStudentFragment = new RemoveStudentFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityStudentDetailsHomeBinding = ActivityStudentDetailsHomeBinding.inflate(getLayoutInflater());
        setContentView(activityStudentDetailsHomeBinding.getRoot());
        allocateActivityTitle("Students");

        bottomNavigationView = findViewById(R.id.studentDetailsNavigationView);
        getSupportFragmentManager().beginTransaction().replace(R.id.studentDetailsContainer, addStudentsFragment).commit();

        bottomNavigationView.setOnItemSelectedListener(item -> {
            final int nav_add = R.id.nav_add_student;
            final int nav_remove = R.id.nav_remove_student;

            switch (item.getItemId()) {
                case nav_add:
                    getSupportFragmentManager().beginTransaction().replace(R.id.studentDetailsContainer, addStudentsFragment).commit();
                    return true;
                case nav_remove:
                    getSupportFragmentManager().beginTransaction().replace(R.id.studentDetailsContainer, removeStudentFragment).commit();
                    return true;
            }
            return true;
        });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(StudentsHome.this, MainActivity.class));
        finish();
    }
}