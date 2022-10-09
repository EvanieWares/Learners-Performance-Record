package com.evanie.lprmaker.students;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.evanie.lprmaker.DBHelper;
import com.evanie.lprmaker.R;

import java.util.ArrayList;
import java.util.List;

public class RemoveStudentFragment extends Fragment {

    Button remove;
    DBHelper helper;
    LinearLayout layout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_remove_student, container, false);

        helper = new DBHelper(getActivity());

        CheckBox studentIDs;
        TextView studentNames;
        remove = inflate.findViewById(R.id.btnRemoveStudent);
        layout = inflate.findViewById(R.id.studentListLinearLayout);
        ArrayList<String> allStudents = new ArrayList<>();
        ArrayList<String> allIDs = new ArrayList<>();
        ArrayList<String> checkedStudents = new ArrayList<>();
        ArrayList<String> checkedIDs = new ArrayList<>();

        Cursor cursor = helper.getData();
        if (cursor.moveToFirst()){
            do {
                allStudents.add(cursor.getString(1));
                allIDs.add(String.valueOf(cursor.getInt(0)));
            }while (cursor.moveToNext());
        }

        for (int i = 0; i < allIDs.size(); i++){
            String id = allIDs.get(i);
            String student = allStudents.get(i);
            String checkboxText = id + "  " + student;
            studentIDs = new CheckBox(getActivity());
            studentIDs.setId(i);
            studentIDs.setText(checkboxText);
            studentIDs.setTag(i);

            studentNames = new TextView(getActivity());
            studentNames.setText(student);

            studentIDs.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked){
                    checkedIDs.add(id);
                    Log.d("TAG", "Added "+id+" to Checked IDs");
                } else {
                    checkedIDs.remove(id);
                    Log.d("TAG", "Removed "+id+" from Checked IDs");
                }
            });
            layout.addView(studentIDs);
        }

        remove.setOnClickListener(view -> {
            if (allIDs.size() > 0) {
                checkedStudents.clear();
                for (int i = 0; i < checkedIDs.size(); i++) {
                    String name = helper.getStudentName(checkedIDs.get(i));
                    checkedStudents.add(name);
                }
                alert(checkedStudents, checkedIDs);
            }
        });

        return inflate;
    }

    private void removeStudents(List<String> studentList, ArrayList<String> idList){
        if (!idList.isEmpty()){
            for (int i = 0; i < idList.size(); i++){
                Log.e("TAG", studentList.get(i));
                helper.removeStudent(idList.get(i));
            }
            Toast.makeText(getActivity(), "You have successfully removed " + studentList, Toast.LENGTH_SHORT).show();
            Cursor cursor = helper.getData();
            while (cursor.moveToNext()){
                String id = cursor.getString(0);
                helper.refresh(id);
            }
            RemoveStudentFragment removeStudentFragment = new RemoveStudentFragment();
            requireActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.studentDetailsContainer, removeStudentFragment)
                    .addToBackStack(null).commit();
        }else {
            Toast.makeText(getActivity(), "Please select a subject", Toast.LENGTH_SHORT).show();
        }
    }

    public void alert(List<String> studentList, ArrayList<String> idList){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(requireContext());
        alertDialogBuilder.setMessage("Are you sure you want to remove " + studentList + "?");
        alertDialogBuilder.setPositiveButton("Yes", (dialogInterface, i) -> removeStudents(studentList, idList));
        alertDialogBuilder.setNegativeButton("No", (dialogInterface, i) -> Toast.makeText(getContext(), "Cancelled", Toast.LENGTH_SHORT).show());

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}