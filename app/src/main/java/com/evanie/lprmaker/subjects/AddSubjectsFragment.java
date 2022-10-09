package com.evanie.lprmaker.subjects;

import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.evanie.lprmaker.DBHelper;
import com.evanie.lprmaker.R;

import java.util.ArrayList;

public class AddSubjectsFragment extends Fragment {

    Button submit, ok;
    EditText subjectName;
    ArrayList<String> subjectList;
    ListView listView;
    DBHelper helper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_add_subjects, container, false);

        helper = new DBHelper(getActivity());
        subjectList = new ArrayList<>();
        submit = inflate.findViewById(R.id.btnSubmitSubjects);
        ok = inflate.findViewById(R.id.btnOkAddSubjects);
        subjectName = inflate.findViewById(R.id.etAddSubject);
        listView = inflate.findViewById(R.id.lvView);

        ok.setBackgroundColor(Color.parseColor("#3490dc"));
        ok.setOnClickListener(v -> {
            String text = subjectName.getText().toString().toUpperCase().replaceAll("\\s","");
            if (!text.equals("")) {
                subjectList.add(text);
                listSubjects();
                subjectName.setText("");
            }else {
                Toast.makeText(getActivity(), "Enter subject name", Toast.LENGTH_SHORT).show();
            }
        });

        submit.setBackgroundColor(Color.parseColor("#3490dc"));
        submit.setOnClickListener(v -> {
            if (!subjectList.isEmpty()){
                for (int i = 0; i < subjectList.size(); i++){
                    helper.addSubjects(subjectList.get(i));
                }

                helper.addSubjects("TOTAL");
                helper.addSubjects("GRADE");
                helper.addSubjects("TOTAL_GRADE");
                Toast.makeText(getActivity(), "You have added " + subjectList, Toast.LENGTH_SHORT).show();
                Cursor cursor = helper.getData();
                while (cursor.moveToNext()){
                    String id = cursor.getString(0);
                    helper.refresh(id);
                }
                AddSubjectsFragment addSubjectsFragment = new AddSubjectsFragment();
                requireActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.subjectsContainer, addSubjectsFragment)
                        .addToBackStack(null).commit();
            }else {
                Toast.makeText(getActivity(), "Failed to add", Toast.LENGTH_SHORT).show();
            }
        });
        return inflate;
    }

    private void listSubjects(){
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, subjectList);
        listView.setAdapter(arrayAdapter);
    }
}