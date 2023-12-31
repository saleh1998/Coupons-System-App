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

public class UpdateCustomerActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    Button btnSave, btnCancel;
    EditText etFName, etLName, etEmail, etPassword;
    Toolbar toolbar;
    NavigationView navigationView;
    Customer customer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_customer);

        btnSave = findViewById(R.id.updateCustomer_btnSave);
        btnCancel = findViewById(R.id.updateCustomer_btnCancel);
        etFName = findViewById(R.id.updateCustomer_etFName);
        etLName = findViewById(R.id.updateCustomer_etLName);
        etEmail = findViewById(R.id.updateCustomer_etEmail);
        etPassword = findViewById(R.id.updateCustomer_etPassword);



        Intent intent = getIntent();
        int requestCode = intent.getIntExtra("requestCode", 0);
        customer = (Customer) intent.getSerializableExtra("customer");
        etFName.setText(customer.getFirstName());
        etLName.setText(customer.getLastName());
        etEmail.setText(customer.getEmail());
        etPassword.setText(customer.getPassword());


        ButtonsClick buttonsClick = new ButtonsClick();
        btnSave.setOnClickListener(buttonsClick);
        btnCancel.setOnClickListener(buttonsClick);

        toolbar = findViewById(R.id.updateCustomer_toolbar);
        setSupportActionBar(toolbar);
        drawerLayout = findViewById(R.id.updateCustomer_drawer_layout);
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

            Intent intent = new Intent(UpdateCustomerActivity.this, AdminActivity.class);
            startActivity(intent);
            Toast.makeText(this, "Moving to Home", Toast.LENGTH_SHORT).show();

        }
        if (item.getItemId() == R.id.nav_logout) {
            Intent intent = new Intent(UpdateCustomerActivity.this, MainActivity.class);
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
                ///Update Customer
                String f_name = etFName.getText().toString();
                String l_name = etLName.getText().toString();
                String email = etEmail.getText().toString();
                String password = etPassword.getText().toString();

                if (!f_name.isEmpty() && !l_name.isEmpty() && !email.isEmpty() && !password.isEmpty()) {
                        String originalEmail = customer.getEmail();
                        customer.setFirstName(f_name);
                        customer.setLastName(l_name);
                        customer.setEmail(email);
                        customer.setPassword(password);
                        ///// added by rashad with advice from bayan
                        AdminFacade adminFacade = new AdminFacade(UpdateCustomerActivity.this);
                        if (email.equals(originalEmail) || !adminFacade.CustomerEmailExists(customer)) {
                            try {
                                adminFacade.updateCustomer(customer);
                                Intent intent = getIntent();
                                intent.putExtra("customer", customer);
                                intent.putExtra("requestCode", 4);
                                setResult(RESULT_OK, intent);
                                finish();
                            } catch (myException e) {
                                throw new RuntimeException(e);
                            }
                        } else {
                            Toast.makeText(UpdateCustomerActivity.this, "Email is already taken", Toast.LENGTH_SHORT).show();
                        }

                    /////

                    /*Intent intent = getIntent();
                    intent.putExtra("customer", customer);
                    intent.putExtra("requestCode", 4);
                    setResult(RESULT_OK, intent);
                    finish();*/
                }else{
                    Toast.makeText(UpdateCustomerActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                }
            }

            if (view.getId() == btnCancel.getId()) {
                finish();
            }
        }
    }
}
