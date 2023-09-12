package com.example.databases;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;


import com.google.android.material.navigation.NavigationView;

//comment attempt
public class ManageCompaniesActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private DrawerLayout drawerLayout;
    Button btnAdd,btnUpdate,btnDelete;
    ImageButton btnSearch;
    EditText etSearchCompany;
    ListView lvCompanies;
    Toolbar toolbar;
    NavigationView navigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_companies);

        btnAdd=findViewById(R.id.manageCompanies_btnAdd);
        btnUpdate=findViewById(R.id.manageCompanies_btnUpadate);
        btnDelete=findViewById(R.id.manageCompanies_btnDelete);
        btnSearch=findViewById(R.id.manageCompanies_btnSearch);
        etSearchCompany=findViewById(R.id.manageCompanies_etCompanyCode);
        lvCompanies=findViewById(R.id.manageCompanies_lv);
        toolbar = findViewById(R.id.manageCompanies_toolbar);

        ButtonsClick buttonsClick = new ButtonsClick();
        btnAdd.setOnClickListener(buttonsClick);
        btnUpdate.setOnClickListener(buttonsClick);
        btnDelete.setOnClickListener(buttonsClick);
        btnSearch.setOnClickListener(buttonsClick);

        toolbar = findViewById(R.id.manageCompanies_toolbar);
        setSupportActionBar(toolbar);
        drawerLayout=findViewById(R.id.manageCompanies_drawer_layout);
        navigationView=findViewById(R.id.nav_view);

        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_nav, R.string.close_nav);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        if(savedInstanceState == null){
            getSupportFragmentManager().beginTransaction().
                    replace(R.id.manageCompanies_fragContainer, new CompanyManagementFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_home);
        }
    }

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
            if(view.getId() == btnAdd.getId()){
                /*Intent intent = new Intent(this,ManageCompaniesActivity.class);
                startActivity(intent);*/
            }
            if(view.getId() == btnUpdate.getId()){
                /*Intent intent = new Intent(this,ManageCustomersActivity.class);
                startActivity(intent);*/
            }
            if(view.getId() == btnDelete.getId()){
                //finish();
            }
        }
    }
}