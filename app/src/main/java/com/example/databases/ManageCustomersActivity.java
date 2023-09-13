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
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

public class ManageCustomersActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private DrawerLayout drawerLayout;
    Button btnAdd,btnUpdate,btnDelete;
    ImageButton btnSearch;
    EditText etSearchCompany;
    ListView lvCompanies;
    Toolbar toolbar;
    NavigationView navigationView;
    CompanyLvAdapter lvAdapter;
    DB_Manager db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_customers);

        btnAdd=findViewById(R.id.manageCustomers_btnAdd);
        btnUpdate=findViewById(R.id.manageCustomers_btnUpadate);
        btnDelete=findViewById(R.id.manageCustomers_btnDelete);
        btnSearch=findViewById(R.id.manageCustomers_btnSearch);
        etSearchCompany=findViewById(R.id.manageCustomers_etCustomerCode);
        lvCompanies=findViewById(R.id.manageCustomers_lv);
        toolbar = findViewById(R.id.manageCustomers_toolbar);

        ButtonsClick buttonsClick = new ButtonsClick();
        btnAdd.setOnClickListener(buttonsClick);
        btnUpdate.setOnClickListener(buttonsClick);
        btnDelete.setOnClickListener(buttonsClick);
        btnSearch.setOnClickListener(buttonsClick);

        setSupportActionBar(toolbar);
        drawerLayout=findViewById(R.id.manageCustomers_drawer_layout);
        navigationView=findViewById(R.id.nav_view);

        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_nav, R.string.close_nav);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        if(savedInstanceState == null){
            getSupportFragmentManager().beginTransaction().
                    replace(R.id.manageCustomers_fragContainer, new CompanyManagementFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_home);
        }

        db = DB_Manager.getInstance(this);
        //lvAdapter=new CompanyLvAdapter(this,R.layout.company_line,db.getAllCustomers());
        //lvCompanies.setAdapter(lvAdapter);
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
                Intent intent = new Intent(ManageCustomersActivity.this,AddCompanyActivity.class);
                startActivity(intent);
            }
            if(view.getId() == btnUpdate.getId()){
                Intent intent = new Intent(ManageCustomersActivity.this,UpdateCompanyActivity.class);
                startActivity(intent);
            }
            if(view.getId() == btnDelete.getId()){
                //finish();
            }
        }
    }
}