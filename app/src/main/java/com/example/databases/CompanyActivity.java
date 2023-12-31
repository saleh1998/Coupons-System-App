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
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import java.io.Serializable;
import java.text.ParseException;

public class
CompanyActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, Serializable {

    private DrawerLayout drawerLayout;
    Button btnAdd,btnUpdate,btnDelete, btnGetBycategory, btnGetByPrice;
    ListView lvCompanyCoupons;
    CompanyCouponsLvAdapter companyCouponsLvAdapter;
    int selectedRow =-1;
    int bgLineColor;
    LinearLayout bgLayout;
    Toolbar toolbar;
    NavigationView navigationView;
    CompanyFacade companyFacade;
    View vieww;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company);


        Intent intent = getIntent();
        if (intent != null) {
            int companyid = intent.getIntExtra("companyid",0);
            companyFacade = new CompanyFacade(companyid,this);

        } else {
          //  throw new myException("ERROR: Intent is null");
        }

        drawerLayout = findViewById(R.id.company_drawLayout);
        btnAdd = findViewById(R.id.company_btnAdd);
        btnUpdate = findViewById(R.id.company_btnUpdate);
        btnDelete = findViewById(R.id.company_btnDelete);
        btnGetBycategory = findViewById(R.id.company_btnCategories);
        btnGetByPrice = findViewById(R.id.company_btnByPrice);
        toolbar = findViewById(R.id.company_toolBar);

        setSupportActionBar(toolbar);
        drawerLayout=findViewById(R.id.company_drawLayout);
        navigationView=findViewById(R.id.company_nv);

        navigationView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_nav, R.string.close_nav);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        lvCompanyCoupons = findViewById(R.id.company_lvCoupons);


        try {
            companyCouponsLvAdapter = new CompanyCouponsLvAdapter(this,R.layout.coupon_line,companyFacade.getCompanyCoupons());
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        try {
            if(companyFacade.getCompanyCoupons()!=null)
                lvCompanyCoupons.setAdapter(companyCouponsLvAdapter);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        lvCompanyCoupons.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(selectedRow != -1)
                {

                    if (bgLayout != null) {
                    if(bgLayout!= null) {
                    Drawable borderDrawable = getResources().getDrawable(R.drawable.border);
                    bgLayout.setBackground(borderDrawable);
                }
                    //bgLayout.setBackgroundColor(bgLineColor);
                }}
                selectedRow = position;
                lvCompanyCoupons.setSelection(position);

                bgLayout = (LinearLayout) view.findViewById(R.id.couponLine_layout);
                bgLineColor = view.getSolidColor();

                Drawable borderDrawable = getResources().getDrawable(R.drawable.border);
                //bgLayout.setBackground(borderDrawable);
                bgLayout.setBackgroundColor(Color.rgb(150,150,150));

            }
        });
        ButtonsClick buttonsClick = new ButtonsClick();
        btnAdd.setOnClickListener(buttonsClick);
        btnDelete.setOnClickListener(buttonsClick);
        btnUpdate.setOnClickListener(buttonsClick);
        btnGetBycategory.setOnClickListener(buttonsClick);
        btnGetByPrice.setOnClickListener(buttonsClick);
    }

    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.navCompany_home){
           /********NONEED********************/
        }
        if(item.getItemId() == R.id.navCompany_profile){
           Intent intent = new Intent(CompanyActivity.this, CompanyViewProfileActivity.class);

           /*we have to send the company with the intent*/
           intent.putExtra("companyid",companyFacade.getCompanyID());
           intent.putExtra("Company",companyFacade.getCompanyDetails(companyFacade.getCompanyID()));
           launcher.launch(intent);

        }
       /* if(item.getItemId() == R.id.nav_settings){
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new SettingsFragment()).commit();
        }
        if(item.getItemId() == R.id.nav_share) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new ShareFragment()).commit();
        }
        if (item.getItemId() == R.id.nav_about) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new AboutFragment()).commit();
        }*/
        if (item.getItemId() == R.id.navCompany_logout) {
            Toast.makeText(this, "Logout", Toast.LENGTH_SHORT).show();
            finish();
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }else{
            super.onBackPressed();
        }
        super.onBackPressed();
    }
    ActivityResultLauncher<Intent> launcher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    Intent intent = result.getData();
                    if(intent != null){
                        int requestCode = intent.getIntExtra("codeForCompanyActivity",0);
                        if(requestCode==1){ //add
                           /* int companyid = intent.getIntExtra("companyid",0);
                            companyFacade = new CompanyFacade(companyid,CompanyActivity.this);*/
                            try {
                                Coupon newCoupon = (Coupon)intent.getSerializableExtra("coupon");
                                companyFacade.addCoupon(newCoupon);
                                Toast.makeText(CompanyActivity.this, "Coupon added successfully!", Toast.LENGTH_SHORT).show();

                            } catch (ParseException e) {
                                throw new RuntimeException(e);
                            } catch (myException e) {
                                Toast.makeText(CompanyActivity.this, "Coupon with the same title already exists for same company please try again", Toast.LENGTH_SHORT).show();

                            }
                            try {
                                companyCouponsLvAdapter.refreshAllCoupons(companyFacade.getCompanyCoupons());
                            } catch (ParseException e) {
                                throw new RuntimeException(e);
                            }
                        }
                        if(requestCode == 2) //update
                        {
                           try {
                               Coupon newCoupon = (Coupon)intent.getSerializableExtra("coupon");
                               if( newCoupon != null) {
                               companyFacade.updateCoupon(newCoupon);
                               companyCouponsLvAdapter.refreshAllCoupons(companyFacade.getCompanyCoupons());
                           } }catch (ParseException e) {
                               throw new RuntimeException(e);
                           } catch (myException e) {
                               throw new RuntimeException(e);
                           }
                        }
                    }
                }
            });
    class ButtonsClick implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            if(v.getId() == btnAdd.getId()) {

                Intent intent = new Intent(CompanyActivity.this, AddCouponActivity.class);
                intent.putExtra("companyid", companyFacade.getCompanyID());
                //   intent.putExtra("codeForCompanyActivity",1);
                launcher.launch(intent);
                selectedRow = -1;

                if (bgLayout != null) {
                    if(bgLayout!= null) {
                    Drawable borderDrawable = getResources().getDrawable(R.drawable.border);
                    bgLayout.setBackground(borderDrawable);
                }
                }
            }
            if(v.getId() == btnUpdate.getId()) {
                if (selectedRow != -1) {
                    Intent intent = new Intent(CompanyActivity.this, UpdateCouponActivity.class);
                    Coupon c = null;
                    try {
                        c = companyFacade.getCompanyCoupons().get(selectedRow);
                    } catch (ParseException ex) {
                        throw new RuntimeException(ex);
                    }
                    intent.putExtra("coupon", c);
                    intent.putExtra("requestCode", 2);
                    intent.putExtra("companyid", companyFacade.getCompanyID());
                    launcher.launch(intent);
                    selectedRow = -1;
                    if (bgLayout != null) {
                    if(bgLayout!= null) {
                    Drawable borderDrawable = getResources().getDrawable(R.drawable.border);
                    bgLayout.setBackground(borderDrawable);
                }
                }} else {
                    Toast.makeText(CompanyActivity.this, "please select first the coupon you want to update", Toast.LENGTH_SHORT).show();
                }
            }
            if(v.getId() == btnDelete.getId()){
                try {
                    if(selectedRow != -1) {
                        Coupon c = companyFacade.getCompanyCoupons().get(selectedRow);
                        companyFacade.deleteCoupon(c);
                       companyCouponsLvAdapter.refreshAllCoupons(companyFacade.getCompanyCoupons());
                       // lvCompanyCoupons.setAdapter(companyCouponsLvAdapter);
                        selectedRow = -1;
                    }
                } catch (ParseException e) {
                    Toast.makeText(CompanyActivity.this, "cant get all coupons from DB thought CompanyFacade", Toast.LENGTH_SHORT).show();
                } catch (myException e) {
                    Toast.makeText(CompanyActivity.this, "deleting failed", Toast.LENGTH_SHORT).show();
                }
            }
            if(v.getId() == btnGetBycategory.getId()){
                Intent intent = new Intent(CompanyActivity.this, CouponsByCategoryActivity.class);
                intent.putExtra("companyid", companyFacade.getCompanyID());
                startActivity(intent);
                selectedRow = -1;

                    if(bgLayout!= null) {
                    Drawable borderDrawable = getResources().getDrawable(R.drawable.border);
                    bgLayout.setBackground(borderDrawable);

                }
            }
            if(v.getId() == btnGetByPrice.getId()){
                Intent intent = new Intent(CompanyActivity.this, CouponsByPriceActivity.class);
                intent.putExtra("companyid", companyFacade.getCompanyID());
                startActivity(intent);
                selectedRow = -1;


                    if(bgLayout!= null) {
                    Drawable borderDrawable = getResources().getDrawable(R.drawable.border);
                    bgLayout.setBackground(borderDrawable);

                }
            }
        }
    }
}