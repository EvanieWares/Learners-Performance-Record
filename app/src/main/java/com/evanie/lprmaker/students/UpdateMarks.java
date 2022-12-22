package com.evanie.lprmaker.students;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.evanie.lprmaker.DBHelper;
import com.evanie.lprmaker.DrawerBaseActivity;
import com.evanie.lprmaker.MainActivity;
import com.evanie.lprmaker.R;
import com.evanie.lprmaker.databinding.ActivityUpdateMarksBinding;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class UpdateMarks extends DrawerBaseActivity {

    ActivityUpdateMarksBinding activityUpdateMarksBinding;

    LinearLayout layout = null;
    RadioGroup subjectsGroup;
    RadioButton subjectButton;
    List<String> subjectsList;
    List<RadioButton> radioButtonList;
    Button update;
    TextInputEditText etID;
    TextInputEditText etScore;
    DBHelper helper;
    TextView studentName;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityUpdateMarksBinding = ActivityUpdateMarksBinding.inflate(getLayoutInflater());
        setContentView(activityUpdateMarksBinding.getRoot());
        allocateActivityTitle("Update Marks");

        helper = new DBHelper(this);
        layout = findViewById(R.id.llUpdateMarks);
        subjectsGroup = new RadioGroup(this);
        subjectsList = new ArrayList<>();
        radioButtonList = new ArrayList<>();
        update = findViewById(R.id.btnUpdateMarksUpdate);
        etID = findViewById(R.id.etUpdateMarksID);
        etScore = findViewById(R.id.etUpdateMarksScore);
        studentName = findViewById(R.id.tvStudentNameToUpdateMarksFor);

        Cursor cursor = helper.getData();

        for (int i = 3; i < cursor.getColumnCount()-1; i++){
            subjectsList.add(cursor.getColumnName(i));
        }

        for (int i = 0; i < subjectsList.size(); i++){
            subjectButton = new RadioButton(this);
            subjectButton.setId(i);
            subjectButton.setText(subjectsList.get(i));
            subjectButton.setTag(subjectsList.get(i));
            radioButtonList.add(subjectButton);
            subjectsGroup.addView(subjectButton);
        }
        layout.addView(subjectsGroup);

        update.setOnClickListener(v -> {
            String id = Objects.requireNonNull(etID.getText()).toString();
            String score = Objects.requireNonNull(etScore.getText()).toString();
            if (TextUtils.isEmpty(id)){
                etID.setError("Enter student ID");
                etID.requestFocus();
            }else if (TextUtils.isEmpty(score)){
                etScore.setError("Enter score");
                etScore.requestFocus();
            }else if(subjectsGroup.getCheckedRadioButtonId() == -1){
                Toast.makeText(this, "Select subject", Toast.LENGTH_SHORT).show();
                subjectsGroup.requestFocus();
            }else {
                String subject = subjectsList.get(subjectsGroup.getCheckedRadioButtonId());
                int stuScore = Integer.parseInt(score);
                boolean success = helper.updateStudentMarks(id, stuScore, subject);
                if (success){
                    etScore.setText("");
                    helper.refresh(id);
                    String name = helper.getStudentName(id);
                    Toast.makeText(UpdateMarks.this, name + "'s " + subject + " score is " + stuScore, Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(UpdateMarks.this, "Unable to update. Please try again", Toast.LENGTH_SHORT).show();
                }
            }
        });

        etID.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    etID.setText("");
                }
            }
        });

        etID.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() != 0){
                    String id = Objects.requireNonNull(etID.getText()).toString();
                    String name = helper.getStudentName(id);
                    if (!name.isEmpty()){
                        studentName.setText(name);
                    } else {
                        studentName.setText("");
                    }
                }else {
                    studentName.setText("");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(UpdateMarks.this, MainActivity.class));
        finish();
    }
}