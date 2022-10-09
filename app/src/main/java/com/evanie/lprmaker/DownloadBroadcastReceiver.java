package com.evanie.lprmaker;
/*
import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class DownloadBroadcastReceiver extends BroadcastReceiver {

    final String databaseName = "learners.db";
    @SuppressLint("SdCardPath")
    final String databaseDirectory = "/data/data/com.evanie.lprmaker/databases/";
    @SuppressLint("SdCardPath")
    final String path = "/sdcard/Android/data/com.evanie.lprmaker/files/databases/learners.db";

    DBHelper helper;

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        helper = new DBHelper(context);
        if (DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(action)){
            Log.e("TAG", "Download finished");
            File database = context.getDatabasePath(databaseName);
            if (database.exists()){
                database.delete();
            }
            if (!database.exists()){
                helper.getReadableDatabase();
                if (copyDatabase()){
                    Toast.makeText(context, "Data is updated", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(context, "Update failed", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private boolean copyDatabase() {
        try {
            File file = new File(path);
            InputStream inputStream = new FileInputStream(file);
            OutputStream outputStream = new FileOutputStream(databaseDirectory+databaseName);
            byte[] buff = new byte[1024];
            int length;
            while ((length = inputStream.read(buff)) > 0){
                outputStream.write(buff, 0, length);
            }
            outputStream.flush();
            outputStream.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
*/
