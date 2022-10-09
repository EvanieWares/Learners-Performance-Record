package com.evanie.lprmaker.others;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.evanie.lprmaker.R;

public class HelpSupportActivity extends AppCompatActivity {

    CardView phone, faq, email, whatsapp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_support);

        phone = findViewById(R.id.cvPhone);
        faq = findViewById(R.id.cvFaq);
        email = findViewById(R.id.cvEmail);
        whatsapp = findViewById(R.id.cvWhatsapp);

        faq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    Intent intent = new Intent(HelpSupportActivity.this, FaqActivity.class);
                    startActivity(intent);
                }else {
                    Toast.makeText(HelpSupportActivity.this, "Your device does not support this page. Please contact the developer.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("message/rfc822");
                intent.putExtra(Intent.EXTRA_EMAIL  , new String[]{"chisopsyelera@gmail.com"});
                try {
                    startActivity(Intent.createChooser(intent, "Send mail using..."));
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(HelpSupportActivity.this, "There are no email clients installed.",Toast.LENGTH_SHORT).show();
                }
            }
        });

        phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:0994142773"));
                startActivity(intent);
            }
        });

        whatsapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Uri uri = Uri.parse("smsto:" + "+265994142773");
                    Intent sendIntent = new Intent(Intent.ACTION_SENDTO, uri);
                    startActivity(Intent.createChooser(sendIntent,"Complete action using..."));
                } catch (Exception e) {
                    Toast.makeText(HelpSupportActivity.this, "Whatsapp is not installed", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        });
    }
}