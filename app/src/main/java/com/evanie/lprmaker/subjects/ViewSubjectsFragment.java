package com.evanie.lprmaker.subjects;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.evanie.lprmaker.DBHelper;
import com.evanie.lprmaker.R;

public class ViewSubjectsFragment extends Fragment {

    DBHelper helper;
    LinearLayout layout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_view_subjects, container, false);

        layout = inflate.findViewById(R.id.llSubjects);
        helper = new DBHelper(getActivity());
        Cursor cursor = helper.getData();

        for (int i = 3; i < cursor.getColumnCount()-1; i++){
            TextView textView = new TextView(getActivity());
            textView.setText(cursor.getColumnName(i));
            textView.setTextSize(18);
            textView.setPadding(5,5,5,5);
            layout.addView(textView);
        }
        return inflate;
    }
}