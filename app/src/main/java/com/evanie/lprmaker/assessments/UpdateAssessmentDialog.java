package com.evanie.lprmaker.assessments;

import android.app.Dialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.evanie.lprmaker.DBHelper;
import com.evanie.lprmaker.R;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class UpdateAssessmentDialog extends AppCompatDialogFragment {

    Button update;
    TextInputEditText etID;
    TextInputEditText etScore;
    DBHelper helper;
    LinearLayout layout = null;
    RadioGroup subjectsGroup;
    RadioButton subjectButton;
    List<String> subjectsList;
    List<RadioButton> radioButtonList;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.update_assessments_dialog, null);
        builder.setView(view)
                .setTitle("Update Assessment")
                .setNegativeButton("Finish", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        builder.setCancelable(false);

        update = view.findViewById(R.id.btnUpdateAssessmentMarksUpdate);
        etID = view.findViewById(R.id.etUpdateAssessmentMarksID);
        etScore = view.findViewById(R.id.etUpdateAssessmentMarksScore);
        helper = new DBHelper(getContext());
        layout = view.findViewById(R.id.llUpdateAssessmentMarks);
        subjectsGroup = new RadioGroup(getContext());
        subjectsList = new ArrayList<>();
        radioButtonList = new ArrayList<>();

        Cursor cursor = helper.getAssessmentData(Assessments.getSelectedAssessment());
        for (int i = 3; i < cursor.getColumnCount(); i++){
            subjectsList.add(cursor.getColumnName(i));
        }

        for (int i = 0; i < subjectsList.size(); i++){
            subjectButton = new RadioButton(getContext());
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
                Toast.makeText(getContext(), "Select subject", Toast.LENGTH_SHORT).show();
                subjectsGroup.requestFocus();
            }else {
                String subject = subjectsList.get(subjectsGroup.getCheckedRadioButtonId());
                int stuScore = Integer.parseInt(score);
                boolean success = helper.updateStudentAssessmentMarks(Assessments.getSelectedAssessment(), id, stuScore, subject);
                if (success){
                    etScore.setText("");
                    helper.refresh(id);
                    String name = helper.getStudentName(id);
                    Toast.makeText(getContext(), name + "'s " + subject + " score is " + stuScore, Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(getContext(), "Unable to update. Please try again", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return builder.create();
    }
}
