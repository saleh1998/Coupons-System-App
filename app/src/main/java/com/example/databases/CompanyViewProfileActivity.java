package com.example.databases;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class CompanyViewProfileActivity extends AppCompatActivity {
    TextView tvComID, tvComName, tvComEmail, tvComPass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_view_profile);

        tvComID = findViewById(R.id.fragCompViewProfile_tvComID);
        tvComName = findViewById(R.id.fragCompViewProfile_tvComName);
        tvComEmail = findViewById(R.id.fragCompViewProfile_tvComEmail);
        tvComPass = findViewById(R.id.fragCompViewProfile_tvComPassword);
    }
}