package com.evanie.lprmaker;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import java.util.ArrayList;

public class SyncDataDialog extends AppCompatDialogFragment {
    Button sync;
    DBHelper helper;
    LinearLayout layout = null;
    CheckBox subject;
    ArrayList<String> list;
    ArrayList<String> checkedSubjects = new ArrayList<>();

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.sync_data_dialog, null);
        builder.setView(view)
                .setTitle("Select subjects to sync")
                .setNegativeButton("Finish", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        builder.setCancelable(false);

        helper = new DBHelper(getContext());
        layout = view.findViewById(R.id.llDataToSync);
        sync = view.findViewById(R.id.btnSyncData);

        list = helper.getDataToSync();
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

        sync.setOnClickListener(v -> {
            if (!checkedSubjects.isEmpty()){
                for (int i = 0; i < checkedSubjects.size(); i++){
                    if (helper.syncData(checkedSubjects.get(i))){
                        Log.e("TAG", checkedSubjects.get(i)+" updated successfully");
                        Toast.makeText(getContext(), "Data is updated", Toast.LENGTH_SHORT).show();
                    } else {
                        Log.e("TAG", checkedSubjects.get(i)+" not updated");
                        Toast.makeText(getContext(), "Sync failed", Toast.LENGTH_SHORT).show();
                    }
                }
            } else {
                Toast.makeText(getActivity(), "Please select a subject to sync", Toast.LENGTH_SHORT).show();
            }
        });

        return builder.create();
    }
}
