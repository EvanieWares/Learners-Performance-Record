package com.evanie.lprmaker;

import android.Manifest;
import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.StorageReference;

public class Utils extends Activity {

    final static String databaseName = "learners.db";
    final static String keyAppMode = "appMode";
    final static String valueOffline = "offline";
    final static String valueLoggedIn = "loggedIn";
    public static final String keyLatestVersionCode = "latestVersionCode";
    public static final String keyLatestVersionName = "latestVersionName";

    // check if storage permission is granted. If not, request permission
    public static boolean storagePermissionGranted(Context context){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
                Log.v("TAG","Permission is granted");
                return true;
            }else {
                Log.v("TAG","Permission is revoked");
                ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        }else {
            Log.v("TAG","Permission is granted");
            return true;
        }
    }

    // check if user is logged in
    public static boolean isLoggedIn(){
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        return user != null;
    }

    // check if network is available
    public static boolean isNetworkAvailable(Context context){
        try {
            ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = null;

            if (manager != null){
                networkInfo = manager.getActiveNetworkInfo();
            }
            return networkInfo != null && networkInfo.isConnected();
        }
        catch (NullPointerException e){
            return false;
        }
    }

    //creating the download link of the app update
    public static void createDownloadLink(Context context, FirebaseAnalytics analytics, StorageReference storage, String latestVersionName){
        Bundle bundle = new Bundle();
        bundle.putString("download", "update_button");
        analytics.logEvent("update_app", bundle);

        StorageReference latestAppPath = storage.child("latestVersion").child(latestVersionName + ".apk");
        latestAppPath.getDownloadUrl().addOnSuccessListener(uri -> {
            String url = String.valueOf(uri);
            Log.e("TAG", "Download link created: "+url);
            downloadAppUpdate(context, latestVersionName, url);
        }).addOnFailureListener(e -> Toast.makeText(context, "Update failed", Toast.LENGTH_SHORT).show());
    }

    // downloading the latest version of the app
    private static void downloadAppUpdate(Context context, String latestVersionName, String link) {
        if (storagePermissionGranted(context)) {
            String fileName = "learners_performance_record_v" + latestVersionName + ".apk";
            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(link));
            request.setDescription("Downloading latest version...");
            request.allowScanningByMediaScanner();
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED); //Notify client once download is completed!
            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName);
            DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
            downloadManager.enqueue(request);
            Toast.makeText(context, "Download started...", Toast.LENGTH_SHORT).show();
            Log.v("TAG", "Downloading " + fileName);
        }
    }

    // get app version name
    public static String getAppVersionName(Context context){
        String versionName = null;
        try {
            PackageInfo pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            versionName = pInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionName;
    }

    /*
    **
    private boolean copyDatabase() {
        try {
            File file = new File(databaseDirectory, downloadedDatabaseName);
            Log.v("TAG", "File attached");
            InputStream inputStream = new FileInputStream(file);
            OutputStream outputStream = new FileOutputStream(absoluteDatabasePath);
            //OutputStream outputStream2 = new FileOutputStream(canonicalDatabasePath);
            byte[] buff = new byte[1024];
            int length;
            while ((length = inputStream.read(buff)) > 0){
                outputStream.write(buff, 0, length);
                //outputStream2.write(buff, 0, length);
            }
            Log.v("TAG", "Database written");
            outputStream.flush();
            outputStream.close();
            outputStream2.flush();
            outputStream2.close();
            return true;
} catch (Exception e) {
        e.printStackTrace();
        return false;
        }
        }
     */

}
