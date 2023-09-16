package com.example.databases;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class CustomerViewProfileActivity extends AppCompatActivity {
    TextView tvCustID, tvCustName, tvCustEmail, tvCustPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_view_profile);

        tvCustID = findViewById(R.id.fragCustViewProfile_tvCustID);
        tvCustName = findViewById(R.id.fragCustViewProfile_tvCustName);
        tvCustEmail = findViewById(R.id.fragCustViewProfile_tvCustEmail);
        tvCustPass = findViewById(R.id.fragCustViewProfile_tvCustPassword);
    }
}