package com.example.databases;

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
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import java.io.Serializable;
import java.text.ParseException;

public class CompanyActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, Serializable {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company);


        Intent intent = getIntent();
        if (intent != null) {
            companyFacade = (CompanyFacade) intent.getSerializableExtra("companyFacade");
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
        lvCompanyCoupons.setAdapter(companyCouponsLvAdapter);
        lvCompanyCoupons.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(selectedRow != -1){
                    bgLayout.setBackgroundColor(bgLineColor);
                }
                selectedRow = position;
                lvCompanyCoupons.setSelection(position);

                bgLayout = (LinearLayout) view.findViewById(R.id.couponLine_layout);
                bgLineColor = view.getSolidColor();
                bgLayout.setBackgroundColor(Color.rgb(150,150,150));
            }
        });

    }

    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.navCompany_home){
           /******************************/
        }
        if(item.getItemId() == R.id.navCompany_profile){
           Intent intent = new Intent(CompanyActivity.this, CompanyViewProfileActivity.class);
           /*we have to send the company with the intent*/
            startActivity(intent);
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

    class ButtonsClick implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            if(v.getId() == btnAdd.getId()){

                Intent intent = new Intent(CompanyActivity.this, AddCouponActivity.class);
                /* we hav to send with the intent the company obj */
                startActivity(intent);
            }
            if(v.getId() == btnUpdate.getId()){
                Intent intent = new Intent(CompanyActivity.this, UpdateCouponActivity.class);
                /* we hav to send with the intent the coupon obj */
                startActivity(intent);
            }
            if(v.getId() == btnDelete.getId()){

                try {
                    Coupon c = companyFacade.getCompanyCoupons().get(selectedRow);
                    companyFacade.deleteCoupon(c);
                    companyCouponsLvAdapter.refreshAllCoupons(companyFacade.getCompanyCoupons());
                } catch (ParseException e) {
                    Toast.makeText(CompanyActivity.this, "cant get all coupons from DB thought CompanyFacade", Toast.LENGTH_SHORT).show();
                } catch (myException e) {
                    Toast.makeText(CompanyActivity.this, "deleting failed", Toast.LENGTH_SHORT).show();
                }
            }
            if(v.getId() == btnGetBycategory.getId()){
                Intent intent = new Intent(CompanyActivity.this, CouponsByCategoryActivity.class);
                startActivity(intent);
            }
            if(v.getId() == btnGetByPrice.getId()){
                Intent intent = new Intent(CompanyActivity.this, CouponsByPriceActivity.class);
                startActivity(intent);
            }
        }
    }
}