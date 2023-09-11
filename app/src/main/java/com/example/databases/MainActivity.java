package com.example.databases;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class MainActivity extends AppCompatActivity {
    // Create an instance of DB_Manager
    DB_Manager dbManager = DB_Manager.getInstance(this);

    // Access methods from the CompaniesDAO interface
    boolean companyExists = dbManager.isCompanyExists("companyEmail", "companyPassword");
    // You can also call other methods from CompaniesDAO if needed

    EditText etUserName, etPassword;
    RadioGroup rg;
    RadioButton rdAdmin, rdCustomer, rdCompany;
    Button btnLogIn;

    int selectedRadioBtnRow = -1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etUserName = findViewById(R.id.main_etUserName);
        etPassword = findViewById(R.id.main_etPassword);

        rg = findViewById(R.id.main_rg);
        rdAdmin = findViewById(R.id.main_rbtnAdmin);
        rdCustomer = findViewById(R.id.main_rbtnUser);
        rdCompany = findViewById(R.id.main_rbtnCompany);


        btnLogIn = findViewById(R.id.main_btnSignIn);
        btnLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedRadioBtnRow = rg.getCheckedRadioButtonId();
                if(selectedRadioBtnRow != -1){
                    RadioButton selected = findViewById(selectedRadioBtnRow);
                    if(selected.getId() == rdAdmin.getId()){
                        if(etUserName.getText().toString().equals("admin@admin.com") &&
                            etPassword.getText().toString().equals("admin") ){
                            Intent intent = new Intent(MainActivity.this, AdminActivity.class);
                            startActivity(intent);
                        }
                    }
                    if(selected.getId() == rdCustomer.getId()){

                    }
                    if(selected.getId() == rdCompany.getId()){

                    }
                }
            }
        });



    }


}