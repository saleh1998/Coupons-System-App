package com.example.databases;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

public class ManageCustomersActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    int selectedRow=-1;
    int selectedCustomerID=-1;
    String selectedCustomerFName="";

    private DrawerLayout drawerLayout;
    Button btnAdd,btnUpdate,btnDelete;
    ImageButton btnSearch;
    EditText etSearchCustomer;
    ListView lvCustomers;
    Toolbar toolbar;
    NavigationView navigationView;
    CustomerLvAdapter lvAdapter;
    AdminFacade adminFacade;
    int bgLineColor;
    LinearLayout bgLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_customers);

        btnAdd=findViewById(R.id.manageCustomers_btnAdd);
        btnUpdate=findViewById(R.id.manageCustomers_btnUpadate);
        btnDelete=findViewById(R.id.manageCustomers_btnDelete);
        btnSearch=findViewById(R.id.manageCustomers_btnSearch);
        etSearchCustomer=findViewById(R.id.manageCustomers_etCustomerCode);
        lvCustomers=findViewById(R.id.manageCustomers_lv);
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

        adminFacade = new AdminFacade(this);
        lvAdapter=new CustomerLvAdapter(this,R.layout.customer_line, adminFacade.getAllCustomers());
        lvCustomers.setAdapter(lvAdapter);
        lvCustomers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(selectedRow != -1)
                {
                    bgLayout.setBackgroundColor(bgLineColor);
                }
                selectedRow = position;
                lvCustomers.setSelection(position);

                bgLayout = (LinearLayout) view.findViewById(R.id.customerLine_ll);
                bgLineColor = view.getSolidColor();
                bgLayout.setBackgroundColor(Color.rgb(150,150,150));


                TextView etName1 = (TextView)view.findViewById(R.id.customerLine_tvFName);
                selectedCustomerFName = etName1.getText().toString();
                TextView etId1 = (TextView)view.findViewById(R.id.customerLine_tvCustomerCode);
                selectedCustomerID = Integer.parseInt(etId1.getText().toString());
            }
        });
    }

    ActivityResultLauncher<Intent> launcher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    Intent intent = result.getData();
                    if(intent != null){
                        int requestCode = intent.getIntExtra("requestCode",0);
                        if(requestCode==2){ //// returning from AddCustomerActivity
                            Customer c = (Customer) intent.getSerializableExtra("customer");
                            if(result.getResultCode()==RESULT_OK){
                                /*try {
                                    adminFacade.addCustomer(c);
                                    lvAdapter.refreshCustomerAdded(c);
                                } catch (myException e) {
                                    throw new RuntimeException(e);
                                }*/
                                lvAdapter.refreshCustomerAdded(c);
                            }
                        }
                        if(requestCode==4){//// returning from UpdateCustomerActivity
                            //Customer c = (Customer) intent.getSerializableExtra("customer");
                            if(result.getResultCode()==RESULT_OK){
                                /*try {
                                    adminFacade.updateCustomer(c);
                                    lvAdapter.refreshAllCustomers(adminFacade.getAllCustomers());
                                } catch (myException e) {
                                    throw new RuntimeException(e);
                                }*/
                                lvAdapter.refreshAllCustomers(adminFacade.getAllCustomers());
                            }
                        }
                    }
                }
            }
    );
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.nav_home){
            /*getSupportFragmentManager().beginTransaction().
                    replace(R.id.fragment_container, new AdminFragment()).commit();*/

            finish();
        }
        if (item.getItemId() == R.id.nav_logout) {
            Toast.makeText(this, "Logout", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(ManageCustomersActivity.this,MainActivity.class);
            intent.putExtra("logout",1);
            setResult(RESULT_OK, intent);
            finish();
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    class ButtonsClick implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            if(view.getId() == btnAdd.getId()){
                Intent intent = new Intent(ManageCustomersActivity.this,AddCustomerActivity.class);
                intent.putExtra("requestCode",1);
                launcher.launch(intent);
                selectedCustomerFName="";
                selectedRow=-1;
                bgLayout.setBackgroundColor(bgLineColor);

            }
            if(view.getId() == btnUpdate.getId()){
                if(selectedRow!=-1){
                Intent intent = new Intent(ManageCustomersActivity.this, UpdateCustomerActivity.class);
                Customer c = adminFacade.getOneCustomer(selectedCustomerID);
                if(c!=null){
                    intent.putExtra("customer",c);
                }
                intent.putExtra("requestCode",3);
                launcher.launch(intent);
                selectedCustomerFName="";
                selectedRow=-1;

                bgLayout.setBackgroundColor(bgLineColor);


            } else{
                    Toast.makeText(ManageCustomersActivity.this, "Please select company", Toast.LENGTH_SHORT).show();
                }
            }
            if(view.getId() == btnDelete.getId()){
                if(selectedRow!=-1){
                Customer c = adminFacade.getOneCustomer(selectedCustomerID);
                try {
                    adminFacade.deleteCustomer(selectedCustomerID);
                    lvAdapter.refreshAllCustomers(adminFacade.getAllCustomers());
                } catch (myException e) {
                    throw new RuntimeException(e);
                }
                selectedCustomerFName="";
                selectedRow=-1;

                bgLayout.setBackgroundColor(bgLineColor);


            } else{
                    Toast.makeText(ManageCustomersActivity.this, "Please select company", Toast.LENGTH_SHORT).show();
                }

            }
            if(view.getId() == btnSearch.getId()){

                String searchedId = etSearchCustomer.getText().toString();

                if (!searchedId.equals("")) {

                    Customer target = adminFacade.getOneCustomer(Integer.parseInt(searchedId));
                    if(target == null)
                        Toast.makeText(ManageCustomersActivity.this, "ID not found", Toast.LENGTH_SHORT).show();
                    else {
                    ArrayList<Customer> specificCustomer = new ArrayList<>();
                    specificCustomer.add(target);
                    lvAdapter.refreshAllCustomers(specificCustomer);
                }}
                else{
                    ArrayList<Customer> allCustomers= adminFacade.getAllCustomers();
                    lvAdapter.refreshAllCustomers(allCustomers);

                }
            }
        }
    }
}