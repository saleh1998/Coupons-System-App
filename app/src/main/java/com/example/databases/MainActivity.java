package com.example.databases;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {
    // Create an instance of DB_Manager
    DB_Manager dbManager = DB_Manager.getInstance(this);

    // Access methods from the CompaniesDAO interface
    boolean companyExists = dbManager.isCompanyExists("companyEmail", "companyPassword");
// You can also call other methods from CompaniesDAO if needed

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

}