package com.example.databases;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public class CompanyViewProfileActivity extends AppCompatActivity {
    TextView tvComID, tvComName, tvComEmail, tvComPass;
    ImageButton imBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_view_profile);

        tvComID = findViewById(R.id.fragCompViewProfile_tvComID);
        tvComName = findViewById(R.id.fragCompViewProfile_tvComName);
        tvComEmail = findViewById(R.id.fragCompViewProfile_tvComEmail);
        tvComPass = findViewById(R.id.fragCompViewProfile_tvComPassword);
        imBack = findViewById(R.id.fragCompViewProfile_btnBack);

        int companyId = getIntent().getIntExtra("companyid",0);
        CompanyFacade companyFacade = new CompanyFacade(companyId,this);
        Company company = companyFacade.getCompanyDetails();

        tvComID.setText("ID: "+company.getId());
        tvComName.setText("Name: " + company.getName());
        tvComEmail.setText("Email: " + company.getEmail());
        tvComPass.setText("password: " + company.getPassword());

        imBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}