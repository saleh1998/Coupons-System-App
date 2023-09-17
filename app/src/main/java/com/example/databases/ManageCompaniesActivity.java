package com.example.databases;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

//comment attempt
public class ManageCompaniesActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{


    int selectedRow=-1;
    int selectedCompanyID=-1;
    String selectedCompanyName="";
    private DrawerLayout drawerLayout;
    Button btnAdd,btnUpdate,btnDelete;
    ImageButton btnSearch;
    EditText etSearchCompany;
    ListView lvCompanies;
    Toolbar toolbar;
    NavigationView navigationView;
    CompanyLvAdapter lvAdapter;
    AdminFacade adminFacade;
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

        setSupportActionBar(toolbar);
        drawerLayout=findViewById(R.id.manageCompanies_drawer_layout);
        navigationView=findViewById(R.id.nav_view);

        navigationView.setNavigationItemSelectedListener(ManageCompaniesActivity.this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_nav, R.string.close_nav);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        if(savedInstanceState == null){
            getSupportFragmentManager().beginTransaction().
                    replace(R.id.manageCompanies_fragContainer, new CompanyManagementFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_home);
        }

        /*Intent intent = getIntent();
        adminFacade = (AdminFacade) intent.getSerializableExtra("AdminFacade");*/
        adminFacade = new AdminFacade(this);

        lvAdapter=new CompanyLvAdapter(this,R.layout.company_line, adminFacade.getAllCompanies());
        lvCompanies.setAdapter(lvAdapter);
        lvCompanies.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedRow=position;
                TextView etName1 = (TextView)view.findViewById(R.id.companyLine_tvComName);
                selectedCompanyName = etName1.getText().toString();
                TextView etId1 = (TextView)view.findViewById(R.id.companyLine_tvComCode);
                selectedCompanyID = Integer.parseInt(etId1.getText().toString());
            }
        });
    }

    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.nav_home){
             finish();
        }
        if (item.getItemId() == R.id.nav_logout) {
            Toast.makeText(this, "Logout", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(ManageCompaniesActivity.this,MainActivity.class);
            intent.putExtra("logout",1);
            startActivity(intent);
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    ActivityResultLauncher<Intent> launcher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    Intent intent = result.getData();
                    if(intent != null){
                        int requestCode = intent.getIntExtra("requestCode",0);
                        if(requestCode==2){ //// returning from AddCompanyActivity
                            Company c = (Company) intent.getSerializableExtra("company");
                            if(result.getResultCode()==RESULT_OK){
                                /*try {
                                    //adminFacade.addCompany(c);
                                    lvAdapter.refreshCompanyAdded(c);
                                } catch (myException e) {
                                    throw new RuntimeException(e);
                                }*/
                                lvAdapter.refreshCompanyAdded(c);
                            }
                        }
                        if(requestCode==4){//// returning from UpdateCompanyActivity
                            //Company c = (Company) intent.getSerializableExtra("company");
                            if(result.getResultCode()==RESULT_OK){
                                /*try {
                                    adminFacade.updateCompany(c);
                                    lvAdapter.refreshAllCompanies(adminFacade.getAllCompanies());
                                } catch (myException e) {
                                    throw new RuntimeException(e);
                                }*/
                                lvAdapter.refreshAllCompanies(adminFacade.getAllCompanies());
                            }
                        }
                        /// for navigationbar click in child activity
                        int logout=intent.getIntExtra("logout",0);
                        if(logout==1){
                            finish();
                        }
                        ///
                    }
                }
            });


    class ButtonsClick implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            if(view.getId() == btnAdd.getId()){
                Intent intent = new Intent(ManageCompaniesActivity.this,AddCompanyActivity.class);
                intent.putExtra("requestCode",1);
                launcher.launch(intent);
                selectedCompanyName="";
                selectedRow=-1;
            }
            if(view.getId() == btnUpdate.getId()){
                if(selectedRow !=-1){
                Intent intent = new Intent(ManageCompaniesActivity.this,UpdateCompanyActivity.class);
                Company c = adminFacade.getOneCompany(selectedCompanyID);
                if(c!=null){
                    intent.putExtra("company",c);
                }
                intent.putExtra("requestCode",3);
                launcher.launch(intent);
                selectedCompanyName="";
                selectedRow=-1;}
                else{
                    Toast.makeText(ManageCompaniesActivity.this, "Please select company", Toast.LENGTH_SHORT).show();
                }
            }

            if(view.getId() == btnDelete.getId()){
                if(selectedRow !=-1){
                Company c = adminFacade.getOneCompany(selectedCompanyID);
                try {
                    adminFacade.deleteCompany(c);
                    lvAdapter.refreshAllCompanies(adminFacade.getAllCompanies());
                } catch (myException e) {
                    throw new RuntimeException(e);
                }
                selectedCompanyName="";
                selectedRow=-1;
            }
                else{
                    Toast.makeText(ManageCompaniesActivity.this, "Please select company", Toast.LENGTH_SHORT).show();
                }
            }
            if(view.getId() == btnSearch.getId()){
                String searchedId = etSearchCompany.getText().toString();
                if (!searchedId.equals(null)){
                Company target = adminFacade.getOneCompany(Integer.parseInt(searchedId));
                ArrayList<Company> specificCompany =new ArrayList<>();
                specificCompany.add(target);
                lvAdapter.refreshAllCompanies(specificCompany);
            }
                else{
                    lvAdapter.refreshAllCompanies(adminFacade.getAllCompanies());

                }
            }
        }
    }
}