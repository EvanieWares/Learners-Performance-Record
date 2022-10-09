package com.evanie.lprmaker.students;

import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.evanie.lprmaker.DBHelper;
import com.evanie.lprmaker.R;

public class StudentDataFragment extends Fragment {

    TableLayout table;
    TableRow row;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_student_details, container, false);

        table = inflate.findViewById(R.id.tableMain);
        row = new TableRow(getActivity());
        row.setPadding(10,15,10,15);
        row.setBackground(ContextCompat.getDrawable(requireActivity(),R.drawable.border2));

        DBHelper db = new DBHelper(getActivity());
        Cursor cursor = db.getData();
        for (int i = 0; i < 3; i++){
            TextView textView = new TextView(getActivity());
            String text = " " + cursor.getColumnName(i) + " ";
            textView.setText(text);
            textView.setTextSize(18);
            textView.setPadding(20,0,20,0);
            textView.setTextColor(Color.BLACK);
            row.addView(textView);
        }
        table.addView(row);

        while (cursor.moveToNext()){
            row = new TableRow(getActivity());
            row.setPadding(10,0,10,0);
            row.setBackground(ContextCompat.getDrawable(requireActivity(),R.drawable.border3));
            for (int i = 0; i < 3; i++){
                TextView textView = new TextView(getActivity());
                String text = " " + cursor.getString(i) + " ";
                textView.setText(text);
                textView.setTextSize(18);
                textView.setPadding(20,0,20,0);
                if (!cursor.getColumnName(i).equals("NAME")) {
                    textView.setGravity(Gravity.CENTER);
                }
                row.addView(textView);
            }
            table.addView(row);
        }

        return inflate;
    }
}