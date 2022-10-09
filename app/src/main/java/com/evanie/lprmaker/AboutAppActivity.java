package com.evanie.lprmaker;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.evanie.lprmaker.others.PrivacyPolicyActivity;

public class AboutAppActivity extends AppCompatActivity {

    String about;
    TextView txtAbout;
    TextView aboutAppVersionName;
    TextView privacyPolicy;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_app);

        txtAbout = findViewById(R.id.txtAbout);
        privacyPolicy = findViewById(R.id.txtPrivacy);
        aboutAppVersionName = findViewById(R.id.aboutAppVersionName);
        String aboutApp = aboutAppVersionName.getText().toString()+ Utils.getAppVersionName(AboutAppActivity.this);
        aboutAppVersionName.setText(aboutApp);

        privacyPolicy.setOnClickListener(view ->{
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                Intent intent = new Intent(AboutAppActivity.this, PrivacyPolicyActivity.class);
                startActivity(intent);
            }else {
                String url = "https://www.termsfeed.com/live/89f0886d-8778-4f08-b7fe-b9f5efedcfca";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });
        about = "<p>The best app for tracking your learners’ performance is the one which is accurate, " +
                "saves your time and usable without any technical knowledge. <strong>Learners Performance Record</strong> app has it all.</p><br>" +
                "<p>Smartly developed for primary school teachers. " +
                "This is the most effective way to compute and summarise learners' end of term results. " +
                "The current version of the app is strictly for six subjects (MANEB format) and does not allow users to add or remove the subjects. " +
                "The subjects are: English, Chichewa, Mathematics, Primary Science, Arts & Life Skills and Social & BK/RE.</p><br>" +
                "<h2><u>Features</u></h2>" +
                "<p>    • Add learners</p>" +
                "<p>    • Enter learners’ marks</p>" +
                "<p>    • Remove learners</p>" +
                "<p>    • View auto-ranked table for all the learners</p>" +
                "<p>    • Export the data in CSV</p><br>" +
                "<h2><u>Why this app?</u></h2>" +
                "<p>    • Simple to use</p>" +
                "<p>    • Does not require any technical knowledge</p>" +
                "<p>    • Reliable</p>" +
                "<p>    • Accurate results</p>" +
                "<p>    • Time saving since ranking/sorting/filtering is done automatically</p>" +
                "<p>    • Help to keep track of learners’ data</p>" +
                "<p>    • Auto-updated learners’ overall performance</p>" +
                "<p>    • Auto-updated learners’ performance per subject</p>" +
                "<p>    • Reduced work as the app uses IDs when updating learners’ data</p><br>" +
                "<p><i>Note: This app uses the grading system. The app does not use total marks as a ranking key. " +
                "In some cases, it might be seen learners with lower scores in better grades than the learners " +
                "with quite some high scores. This app will first rank the learners according to their grades. " +
                "Since the can be a number of learners on the same grade, the app will then rank those learners " +
                "according to their total marks. Learners matching both grades and marks will be ranked by their " +
                "performance in English subject.</i></p><br><br>" +
                "<p>We are currently working on the next update which will allow users to:</p>" +
                "<ol><li>add or remove subjects from the table</li>" +
                "<li>share the tables directly from the app</li>" +
                "<li>export data in xls or xlsx</li></ol>";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            txtAbout.setText(Html.fromHtml(about, Html.FROM_HTML_MODE_COMPACT));
        }else {
            Toast.makeText(this, "This device does support some contents of this page.", Toast.LENGTH_SHORT).show();
            txtAbout.setText("This app helps you to record learners' performance after administering exams. " +
                    "The best app for tracking your learners’ performance is the one which is accurate, " +
                    "saves your time and usable without any technical knowledge. Learners Performance Record app has it all. " +
                    "Smartly developed for primary school teachers. This is the most effective way to compute and summarize " +
                    "learners' end of term results. The current version of the app is strictly for six subjects (MANEB format) " +
                    "and does not allow users to add or remove the subjects. The subjects are: English, Chichewa, Mathematics, " +
                    "Primary Science, Arts & Life Skills and Social & BK/RE. This app lets you: Add learners, Enter learners’ " +
                    "marks, Remove learners, View auto-ranked table for all the learners and Export the data in CSV. The app: is " +
                    "simple to use, does not require any technical knowledge, reliable, produce accurate results, time saving since " +
                    "ranking/sorting/filtering is done automatically, help to keep track of learners’ data, auto-updated learners’ " +
                    "overall performance, auto-updated learners’ performance per subject, reduced work as the app uses IDs " +
                    "when updating learners’ data");
        }
    }
}