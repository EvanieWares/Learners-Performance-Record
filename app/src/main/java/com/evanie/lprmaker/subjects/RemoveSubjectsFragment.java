package com.evanie.lprmaker.subjects;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.evanie.lprmaker.DBHelper;
import com.evanie.lprmaker.R;

import java.util.ArrayList;

public class RemoveSubjectsFragment extends Fragment {

    LinearLayout layout;
    Button remove;
    DBHelper helper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_remove_subjects, container, false);

        CheckBox subject;
        layout = inflate.findViewById(R.id.llRemove);
        remove = inflate.findViewById(R.id.btnRemoveSubject);
        ArrayList<String> list = new ArrayList<>();
        ArrayList<String> checkedSubjects = new ArrayList<>();
        helper = new DBHelper(getActivity());

        for (int i = 3; i < helper.getData().getColumnCount()-3; i++){
            list.add(helper.getData().getColumnName(i));
        }

        for (int i = 0; i < list.size(); i++){
            subject = new CheckBox(getActivity());
            subject.setId(i);
            subject.setText(list.get(i));
            subject.setTag(i);

            String s = list.get(i);
            subject.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked){
                    checkedSubjects.add(s);
                } else {
                    checkedSubjects.remove(s);
                }
            });
            layout.addView(subject);
        }

        remove.setOnClickListener(v -> {
            if (!checkedSubjects.isEmpty()){
                for (int i = 0; i < checkedSubjects.size(); i++){
                    Log.e("TAG", checkedSubjects.get(i));
                    helper.removeSubjects(checkedSubjects.get(i));
                    helper.addSubjects("TOTAL");
                    helper.addSubjects("GRADE");
                    helper.addSubjects("TOTAL_GRADE");
                }
                Toast.makeText(getActivity(), "You have successfully removed " + checkedSubjects, Toast.LENGTH_SHORT).show();
                Cursor cursor = helper.getData();
                while (cursor.moveToNext()){
                    String id = cursor.getString(0);
                    helper.refresh(id);
                }
                RemoveSubjectsFragment removeSubjectsFragment = new RemoveSubjectsFragment();
                requireActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.subjectsContainer, removeSubjectsFragment)
                        .addToBackStack(null).commit();
            }else {
                Toast.makeText(getActivity(), "Please select a subject", Toast.LENGTH_SHORT).show();
            }
        });
        return inflate;
    }
}