package com.evanie.lprmaker.others;

import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.evanie.lprmaker.R;

public class FaqActivity extends AppCompatActivity {

    TextView txtHelp;
    String help;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faq);

        help = "<h1>How to use the app</h1><br>" +
                "<p>Go to the navigation drawer by swiping on the screen from the left edge of the phone to the right or simply click the three horizontal lines on the top the left corner.</p><br><b>" +
                "\n" +
                "<p><i>In the navigation drawer:</i></p><br>" +
                "\n" +
                "<p>Go to <i><u>Subjects</u></i> to view/add/remove subjects. When adding, enter a subject name without spaces and click \"OK\". You can add as many subjects as you wish. Then click \"SUBMIT\". When removing, tick against the subjects you want to remove. You can add/remove the subject whenever you feel like doing so.</p><br>" +
                "\n" +
                "<p>Go to <i><u>Student Details</u></i> to view/add/remove students in the database. When adding students, enter student name, sex and ID (examination number or any unique number you can use to identify each of the students).</p><br>" +
                "\n" +
                "<p>Go to <i><u>Update Student Data</u></i> to enter student marks. Tick against the learning area, enter student ID and score.</p><br>" +
                "\n" +
                "<p>Go to <i><u>All Students</u></i> to view full the full progress record table, showing all the students' data. There is a search icon used to search for specific students.</p><br>" +
                "\n" +
                "<p>Go to <i><u>Export</u></i> to extract the data to a CSV file for printing. The app will ask for storage permission to accomplish this. The file will be exported as \"Performance Record.csv\" and will be exported to the \"documents\" folder in the root storage of your device.</p><br>" +
                "\n" +
                "<p>Contact the <u>developer</u> if you need any other help.</p><br><br>" +
                "\n" +
                "<i>Continuous Assessment section is still under development.</i>";
        txtHelp = findViewById(R.id.txtHelp);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            txtHelp.setText(Html.fromHtml(help, Html.FROM_HTML_MODE_COMPACT));
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}