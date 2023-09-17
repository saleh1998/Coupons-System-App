package com.example.databases;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

public class AddCustomerActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {


    private DrawerLayout drawerLayout;
    Button btnSave, btnCancel;
    EditText etFName, etLName, etEmail, etPassword, etConfirmPassword;
    Toolbar toolbar;
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_customer);

        btnSave = findViewById(R.id.addCustomer_btnSave);
        btnCancel = findViewById(R.id.addCustomer_btnCancel);
        etFName = findViewById(R.id.addCustomer_etFName);
        etLName = findViewById(R.id.addCustomer_etLName);
        etEmail = findViewById(R.id.addCustomer_etEmail);
        etPassword = findViewById(R.id.addCustomer_etPassword);
        etConfirmPassword = findViewById(R.id.addCustomer_etConfirmPassword);

        ButtonsClick buttonsClick = new ButtonsClick();
        btnSave.setOnClickListener(buttonsClick);
        btnCancel.setOnClickListener(buttonsClick);

        toolbar = findViewById(R.id.addCustomer_toolbar);
        setSupportActionBar(toolbar);
        drawerLayout = findViewById(R.id.addCustomer_drawer_layout);
        navigationView = findViewById(R.id.nav_view);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_nav, R.string.close_nav);
        navigationView.setNavigationItemSelectedListener(this);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        if (savedInstanceState == null) {
            navigationView.setCheckedItem(R.id.nav_home);
        }
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.nav_home) {

            Intent intent = new Intent(AddCustomerActivity.this, AdminActivity.class);
            startActivity(intent);
            Toast.makeText(this, "Moving to Home", Toast.LENGTH_SHORT).show();

        }
        if (item.getItemId() == R.id.nav_logout) {
            Intent intent = new Intent(AddCustomerActivity.this, MainActivity.class);
            startActivity(intent);
            Toast.makeText(this, "Logout", Toast.LENGTH_SHORT).show();
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    class ButtonsClick implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            if (view.getId() == btnSave.getId()) {
                ///Save new Customer
                String f_name = etFName.getText().toString();
                String l_name = etLName.getText().toString();
                String email = etEmail.getText().toString();
                String password = etPassword.getText().toString();
                String confirmPassword = etConfirmPassword.getText().toString();
                if (!f_name.isEmpty() && !l_name.isEmpty() && !email.isEmpty() && !password.isEmpty()) {
                    if (password.equals(confirmPassword)) {
                        Customer customer = new Customer(f_name, l_name, email, password);

                        ///// added by rashad with advice from bayan
                        AdminFacade adminFacade = new AdminFacade(AddCustomerActivity.this);

                        if (!adminFacade.CustomerEmailExists(customer)) {
                            try {
                                adminFacade.addCustomer(customer);
                                Intent intent = getIntent();
                                intent.putExtra("customer", customer);
                                intent.putExtra("requestCode", 2);
                                setResult(RESULT_OK, intent);
                                finish();
                            } catch (myException e) {
                                throw new RuntimeException(e);
                            }
                        } else {
                            Toast.makeText(AddCustomerActivity.this, "Email is already taken", Toast.LENGTH_SHORT).show();
                        }
                        /////

                    /*Intent intent = getIntent();
                    intent.putExtra("customer", customer);
                    intent.putExtra("requestCode", 2);
                    setResult(RESULT_OK, intent);
                    finish();*/

                    } else {
                        etPassword.setText("");
                        etConfirmPassword.setText("");
                        Toast.makeText(AddCustomerActivity.this, "Please insert correct password confirmation!!", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(AddCustomerActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                }
            }
            if (view.getId() == btnCancel.getId()) {
                finish();
            }
        }
    }
}