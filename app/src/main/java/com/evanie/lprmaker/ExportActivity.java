package com.evanie.lprmaker;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ExportActivity extends AppCompatActivity {

    Button btnExport;
    Button btnUpload;
    Button btnRetrieve;
    String rankBy;
    String appMode;
    String cloudFileName;
    SharedPreferences preferences;
    FirebaseAuth mAuth;
    StorageReference storage;
    StorageReference cloudFilePath;
    DBHelper helper;
    ProgressDialog progressDialog;

    //Context context = this;
    final String databaseName = "learners.db";
    String databaseDirectory;
    static final String rootPath = Environment.getExternalStorageDirectory().toString();
    final static String filesPath = rootPath+"/Android/data/com.evanie.lprmaker/files/databases";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_export);

        databaseDirectory = getDatabasePath(databaseName).getParent();

        mAuth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance().getReference();
        progressDialog = new ProgressDialog(this);

        helper = new DBHelper(ExportActivity.this);

        btnExport = findViewById(R.id.btnExport);
        btnUpload = findViewById(R.id.btnUploadToCloud);
        btnRetrieve = findViewById(R.id.btnRetrieveFromCloud);
        rankBy = "";

        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        rankBy = preferences.getString("ranking", "");
        appMode = preferences.getString(Utils.keyAppMode, "");

        btnRetrieve.setOnClickListener(view ->{
            if (!appMode.equals(Utils.valueOffline)) {
                if (Utils.isNetworkAvailable(ExportActivity.this)) {
                    if (Utils.storagePermissionGranted(ExportActivity.this)){
                        download();
                    }
                } else {
                    Toast.makeText(ExportActivity.this, "Network is not available", Toast.LENGTH_SHORT).show();
                }
            }else {
                Toast.makeText(this, "Your are not logged in", Toast.LENGTH_SHORT).show();
            }
        });

        btnUpload.setOnClickListener(view ->{
            if (Utils.isNetworkAvailable(ExportActivity.this)) {
                if (Utils.isLoggedIn()) {
                    uploadFile();
                } else {
                    Toast.makeText(ExportActivity.this, "Your are not logged in", Toast.LENGTH_SHORT).show();
                }
            }else {
                Toast.makeText(this, "Network is not available", Toast.LENGTH_SHORT).show();
            }
        });

        btnExport.setOnClickListener(view -> {
            if (Utils.storagePermissionGranted(ExportActivity.this)){
                helper.exportDB(rankBy);
                Toast.makeText(ExportActivity.this, "The file was successfully exported to \"LPR/Performance Record.csv\"", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void uploadFile(){
        progressDialog.setMessage("Uploading to cloud...");
        cloudFileName = Objects.requireNonNull(mAuth.getCurrentUser()).getEmail();
        cloudFilePath = storage.child("databases").child(cloudFileName+".db");
        progressDialog.setMessage("Uploading data...");
        progressDialog.show();
        String databasePath = databaseDirectory+databaseName;
        Uri uri = Uri.fromFile(new File(databasePath));
        cloudFilePath.putFile(uri).addOnSuccessListener(taskSnapshot -> {
            progressDialog.dismiss();
            Toast.makeText(ExportActivity.this, "Upload finished", Toast.LENGTH_SHORT).show();
        }).addOnFailureListener(e -> Toast.makeText(ExportActivity.this, "Error: "+ e.getMessage(), Toast.LENGTH_SHORT).show()).addOnCanceledListener(() -> Toast.makeText(ExportActivity.this, "Upload cancelled", Toast.LENGTH_SHORT).show());
    }

    private void downloadFile(String link){
        progressDialog.setMessage("Syncing...");
        progressDialog.setProgress(0);
        progressDialog.show();
        ExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        Handler handler = new Handler(Looper.getMainLooper());
        executor.execute(new Runnable() {
            int count;
            @Override
            public void run() {
                //background work here
                try {
                    URL url = new URL(link);
                    URLConnection connection = url.openConnection();

                    int lengthOfFile = connection.getContentLength();
                    Log.v("TAG", "File size is: "+lengthOfFile);

                    File file = new File(filesPath);
                    if (!file.exists()){
                        if (file.mkdirs()){
                            Log.v("TAG", filesPath+" created");
                        }
                    }

                    //download the file
                    InputStream inputStream = connection.getInputStream();
                    OutputStream outputStream = new FileOutputStream(file+"/"+databaseName);
                    byte[] data = new byte[1024];
                    long total = 0;

                    //writing data to file
                    while ((count = inputStream.read(data)) != -1){
                        total += count;
                        publishProgress("" + (int) ((total * 100) / lengthOfFile));
                        outputStream.write(data, 0, count);
                    }
                    outputStream.flush();
                    outputStream.close();
                    inputStream.close();

                    handler.post(() -> {
                        if (helper.copyDatabase()){
                            Toast.makeText(ExportActivity.this, "Data is updated", Toast.LENGTH_SHORT)
                                    .show();
                            if (file.delete()){
                                Log.i("TAG", "Downloaded file was deleted");
                            }
                        }else {
                            Toast.makeText(ExportActivity.this, "Update failed", Toast.LENGTH_SHORT)
                                    .show();
                        }
                        progressDialog.dismiss();
                    });
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

    private void publishProgress(String... progress) {
        progressDialog.setProgress(Integer.parseInt(progress[0]));
    }

    private void download(){
        cloudFileName = Objects.requireNonNull(mAuth.getCurrentUser()).getEmail();
        cloudFilePath = storage.child("databases").child(cloudFileName+".db");
        cloudFilePath.getDownloadUrl().addOnSuccessListener(uri -> {
            String url = String.valueOf(uri);
            Log.e("TAG", "Download link created: "+url);
            downloadFile(url);
        }).addOnFailureListener(e -> Toast.makeText(ExportActivity.this, "Sync failed", Toast.LENGTH_SHORT).show());
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(ExportActivity.this, MainActivity.class));
        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        progressDialog.dismiss();
    }
}