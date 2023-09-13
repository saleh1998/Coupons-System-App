package com.example.databases;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

public class UpdateCustomerActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private DrawerLayout drawerLayout;
    Button btnSave,btnCancel;
    EditText etName,etEmail,etPassword,etConfirmPassword;
    Toolbar toolbar;
    NavigationView navigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_customer);

        btnSave = findViewById(R.id.updateCustomer_btnSave);
        btnCancel = findViewById(R.id.updateCustomer_btnCancel);
        etName = findViewById(R.id.updateCustomer_etName);
        etEmail = findViewById(R.id.updateCustomer_etEmail);
        etPassword = findViewById(R.id.updateCustomer_etPassword);
        etConfirmPassword = findViewById(R.id.updateCustomer_etConfirmPassword);

        ButtonsClick buttonsClick = new ButtonsClick();
        btnSave.setOnClickListener(buttonsClick);
        btnCancel.setOnClickListener(buttonsClick);

        toolbar = findViewById(R.id.updateCustomer_toolbar);
        setSupportActionBar(toolbar);
        drawerLayout=findViewById(R.id.updateCustomer_drawer_layout);
        navigationView=findViewById(R.id.nav_view);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_nav, R.string.close_nav);
        navigationView.setNavigationItemSelectedListener(this);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        if(savedInstanceState == null){
            navigationView.setCheckedItem(R.id.nav_home);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.nav_home){
            getSupportFragmentManager().beginTransaction().
                    replace(R.id.fragment_container, new AdminFragment()).commit();
        }
        if (item.getItemId() == R.id.nav_logout) {
            Toast.makeText(this, "Logout", Toast.LENGTH_SHORT).show();
            finish();
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
    class ButtonsClick implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            if (view.getId() == btnSave.getId()) {
                ///Save new Company
            }
            if (view.getId() == btnCancel.getId()) {
                finish();
            }
        }
    }
}