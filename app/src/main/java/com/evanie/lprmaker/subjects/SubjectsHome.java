package com.evanie.lprmaker.subjects;

import android.content.Intent;
import android.os.Bundle;

import com.evanie.lprmaker.DrawerBaseActivity;
import com.evanie.lprmaker.MainActivity;
import com.evanie.lprmaker.R;
import com.evanie.lprmaker.databinding.ActivitySubjectsHomeBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class SubjectsHome extends DrawerBaseActivity {

    ActivitySubjectsHomeBinding activitySubjectsHomeBinding;
    BottomNavigationView bottomNavigationView;
    AddSubjectsFragment addSubjectsFragment = new AddSubjectsFragment();
    RemoveSubjectsFragment removeSubjectsFragment = new RemoveSubjectsFragment();
    ViewSubjectsFragment viewSubjectsFragment = new ViewSubjectsFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activitySubjectsHomeBinding = ActivitySubjectsHomeBinding.inflate(getLayoutInflater());
        setContentView(activitySubjectsHomeBinding.getRoot());
        allocateActivityTitle("Subjects");

        bottomNavigationView = findViewById(R.id.subjectsNavigationView);
        getSupportFragmentManager().beginTransaction().replace(R.id.subjectsContainer, viewSubjectsFragment).commit();

        bottomNavigationView.setOnItemSelectedListener(item -> {
            final int nav_add_subjects = R.id.nav_add_subjects;
            final int nav_remove_subjects = R.id.nav_remove_subjects;
            final int nav_all_subjects = R.id.nav_all_subjects;

            switch (item.getItemId()) {
                case nav_add_subjects:
                    getSupportFragmentManager().beginTransaction().replace(R.id.subjectsContainer, addSubjectsFragment).commit();
                    return true;
                case nav_remove_subjects:
                    getSupportFragmentManager().beginTransaction().replace(R.id.subjectsContainer, removeSubjectsFragment).commit();
                    return true;
                case nav_all_subjects:
                    getSupportFragmentManager().beginTransaction().replace(R.id.subjectsContainer, viewSubjectsFragment).commit();
                    return true;
            }
            return true;
        });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(SubjectsHome.this, MainActivity.class));
        finish();
    }
}