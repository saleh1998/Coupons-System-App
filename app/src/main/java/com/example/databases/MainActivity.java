package com.example.databases;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.io.Serializable;
import java.text.ParseException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    // Create an instance of DB_Manager
    DB_Manager dbManager = DB_Manager.getInstance(this);

    ClientFacade clientFacade;

    // Access methods from the CompaniesDAO interface
    //boolean companyExists = dbManager.isCompanyExists("companyEmail", "companyPassword");
    // You can also call other methods from CompaniesDAO if needed

    EditText etUserName, etPassword;
    RadioGroup rg;
    RadioButton rdAdmin, rdCustomer, rdCompany;
    Button btnLogIn;
    ArrayList<Category> categories;

    int selectedRadioBtnRow = -1;

    // Variables related to the background service
    private Thread myThread;
    private MyRunnable myRunnable;
    LoginManager loginManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try {
             categories = dbManager.getAllCategories();
             //dbManager.generate();
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }


       /* // INSERTING CATEGORIES
        for(Category c : Category.values())
        {
            dbManager.addCategory(c.name());
        }
*/


        loginManager = LoginManager.getInstance(MainActivity.this);
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
                    String userName = etUserName.getText().toString();
                    String pass = etPassword.getText().toString();
                    if(ValidInput.isValidEmail(userName)){
                    if(selected.getId() == rdAdmin.getId()){
                        AdminFacade adminFacade = (AdminFacade) loginManager.login(userName,pass,ClientType.Administrator);
                        if(adminFacade != null) {
                            Intent intent = new Intent(MainActivity.this, AdminActivity.class);
                            /*clientFacade = new AdminFacade(MainActivity.this);
                            AdminFacade adfc = new AdminFacade(MainActivity.this);
                            intent.putExtra("AdminFacade",adfc);
                            */
                            startActivity(intent);
                        }
                        else {
                            Toast.makeText(MainActivity.this,"User Name or Password incorrect", Toast.LENGTH_LONG).show();
                        }


                    }
                    if(selected.getId() == rdCustomer.getId()){
                        CustomerFacade customerFacade = (CustomerFacade) loginManager.login(userName,pass,ClientType.Customer);
                        if(customerFacade != null) {
                                Intent intent = new Intent(MainActivity.this, CustomerActivity.class);
                            intent.putExtra("customerid", customerFacade.getCustomerID());
                                startActivity(intent);
                        }
                        else {
                            Toast.makeText(MainActivity.this,"User Name or Password incorrect", Toast.LENGTH_LONG).show();

                        }

                    }
                    if(selected.getId() == rdCompany.getId()){
                        CompanyFacade companyFacade = (CompanyFacade)loginManager.login(userName,pass,ClientType.Company);
                        if(companyFacade != null) {
                            Intent intent = new Intent(MainActivity.this, CompanyActivity.class);
                            intent.putExtra("companyid", companyFacade.getCompanyID());
                            startActivity(intent);
                        }
                        else {
                            Toast.makeText(MainActivity.this,"User Name or Password incorrect", Toast.LENGTH_LONG).show();
                        }}

                    }
                    else {
                        Toast.makeText(MainActivity.this,"this is not valid email", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

// Start the background service to check for expired coupons
        myRunnable = new MyRunnable();
        myThread = new Thread(myRunnable);
        myThread.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Properly stop the runnable when the activity is destroyed.
        if (myRunnable != null) {
            myRunnable.stop();
            try {
                myThread.join(); // Wait for the thread to finish its execution.
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}