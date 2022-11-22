package com.evanie.lprmaker.students;

import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.evanie.lprmaker.DBHelper;
import com.evanie.lprmaker.R;

public class AddStudentsFragment extends Fragment {

    Button btnAdd;
    EditText stName, stID;
    RadioGroup genderRadioGroup;
    String sex;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_add_students, container, false);

        btnAdd = inflate.findViewById(R.id.add);
        stName = inflate.findViewById(R.id.stName);
        stID = inflate.findViewById(R.id.stID);
        genderRadioGroup = inflate.findViewById(R.id.genderRadioGroup);
        genderRadioGroup.setOnCheckedChangeListener(onCheckedChangeListener);

        btnAdd.setBackgroundColor(Color.parseColor("#3490dc"));
        btnAdd.setOnClickListener(view -> {
            String stuID = stID.getText().toString();
            String stuName = stName.getText().toString();

            if (TextUtils.isEmpty(stuID)){
                stID.setError("ID cannot be empty");
                stID.requestFocus();
            }else if (TextUtils.isEmpty(stuName)){
                stName.setError("Name cannot be empty");
                stName.requestFocus();
            }else if (genderRadioGroup.getCheckedRadioButtonId() == -1){
                Toast.makeText(getContext(), "Please select gender", Toast.LENGTH_SHORT).show();
            }else {
                DBHelper helper = new DBHelper(getContext());
                String name = helper.getStudentName(stuID);
                if (name.isEmpty()){
                    try {
                        helper.addOne(Integer.parseInt(stuID), stuName.toUpperCase(), sex);
                        helper.refresh(stuID);
                        name = helper.getStudentName(stuID);
                        Toast.makeText(getContext(), "You have successfully added " + name + "", Toast.LENGTH_SHORT).show();
                        stName.setText("");
                        stID.setText("");
                    }catch (Exception e){
                        e.printStackTrace();
                        Toast.makeText(getContext(), "Unable to add student. Please try again", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(), "The ID you have entered is already registered to "+ name, Toast.LENGTH_SHORT).show();
                }
            }
        });
        return inflate;
    }

    public RadioGroup.OnCheckedChangeListener onCheckedChangeListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {

            final int radioMale = R.id.radioMale;
            final int radioFemale = R.id.radioFemale;
            switch (checkedId){
                case radioMale:
                    sex = "M";
                    break;
                case radioFemale:
                    sex = "F";
                    break;
                default:break;
            }
        }
    };
}