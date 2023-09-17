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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;


import com.google.android.material.navigation.NavigationView;

import java.io.Serializable;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;


public class CustomerActivity extends AppCompatActivity implements Serializable,NavigationView.OnNavigationItemSelectedListener{

    Button btnSearch,btnBuyCoupon,btnQRCode;
    ListView lvCoupons;
    Spinner spCategory;
    EditText etMaxPrice;

    CustomerFacade customerFacade;
    CompanyCouponsLvAdapter adapter; // ef7se etha bfreqsh
    ArrayList<Coupon> customerCoupons;
    int selectedRow =-1;
    int bgLineColor;
    LinearLayout bgLayout;

    Toolbar toolbar;
    NavigationView navigationView;
    private DrawerLayout drawerLayout;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer);
        btnBuyCoupon = findViewById(R.id.customer_btnBuy);
        btnSearch = findViewById(R.id.customer_btnSearch);
        lvCoupons = findViewById(R.id.customer_lvCoupons);
        spCategory = findViewById(R.id.customer_categorySpinner);
        etMaxPrice = findViewById(R.id.customer_etMaxPrice);
        btnQRCode = findViewById(R.id.customer_btnQRCode);

        drawerLayout = findViewById(R.id.customer_drawLayout);
        toolbar = findViewById(R.id.customer_toolBar);

        setSupportActionBar(toolbar);
        drawerLayout=findViewById(R.id.customer_drawLayout);
        navigationView=findViewById(R.id.customer_nv);

        navigationView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_nav, R.string.close_nav);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        Intent intent = getIntent();
        if(intent != null){
            int customerid = intent.getIntExtra("customerid",0);
            customerFacade = new CustomerFacade(this);
            customerFacade.setCustomerID(customerid);
        }


        ///// initializing the list view
        try {
            customerCoupons = customerFacade.getCustomerCoupons();
            if(customerCoupons != null)
            {
            adapter = new CompanyCouponsLvAdapter(this, R.layout.coupon_line,customerCoupons);
            lvCoupons.setAdapter(adapter);
            }
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }



        ArrayAdapter<String> categoryAdapter;
        ArrayList<String> items = new ArrayList<>();
        items.add(Category.Food.toString());
        items.add(Category.Electricity.name());
        items.add(Category.Restaurant.name());
        items.add(Category.Vacation.name());
        categoryAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, items);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCategory.setAdapter(categoryAdapter);



        ButtonsClick buttonsClick = new ButtonsClick();
        btnBuyCoupon.setOnClickListener(buttonsClick);
        btnSearch.setOnClickListener(buttonsClick);
        btnQRCode.setOnClickListener(buttonsClick);

        spCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != -1) {
                    Category category = Category.valueOf(items.get(position));
                    try {
                        ArrayList<Coupon> couponsByCat = customerFacade.getCustomerCoupons(category);
                        adapter = new CompanyCouponsLvAdapter(CustomerActivity.this, R.layout.coupon_line, couponsByCat);
                        lvCoupons.setAdapter(adapter);
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }

                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        lvCoupons.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(selectedRow != -1){
                    bgLayout.setBackgroundColor(bgLineColor);
                }
                selectedRow = position;
                lvCoupons.setSelection(position);

                bgLayout = (LinearLayout) view.findViewById(R.id.couponLine_layout);
                bgLineColor = view.getSolidColor();
                bgLayout.setBackgroundColor(Color.rgb(150,150,150));
            }

        });



    }

    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        if(item.getItemId() == R.id.navCustomer_profile){
            Intent intent = new Intent(CustomerActivity.this, CustomerViewProfileActivity.class);
            /*we have to send the company with the intent*/
            intent.putExtra("customerid",customerFacade.getCustomerID());
            startActivity(intent);
        }
        if (item.getItemId() == R.id.navCustomer_logout) {
            Toast.makeText(this, "Logout", Toast.LENGTH_SHORT).show();
            finish();
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    ActivityResultLauncher<Intent> launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            Intent intent = result.getData();
            if(intent != null) {
                int requestCode = intent.getIntExtra("requestCode", 0);
                if (requestCode == 1){ /// buy new coupon
                    if (result.getResultCode() == RESULT_OK) {
                        Coupon c = (Coupon) intent.getSerializableExtra("coupon");
                        if (c != null) {
                            try {
                                customerFacade.purchaseCoupon(c);
                                adapter.refreshCouponAdded(c);
                            } catch (myException ex) {
                                Toast.makeText(CustomerActivity.this,  ex.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                            catch (ParseException ex) {
                                Toast.makeText(CustomerActivity.this,  ex.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }


                }

            }

        }
    });


    class ButtonsClick implements View.OnClickListener{

        @Override
        public void onClick(View view) {
            if(view.getId() == btnBuyCoupon.getId()){
                //////////// starting activity to buy new coupon
                Intent intent = new Intent(CustomerActivity.this, BuyCouponActivity.class);
                intent.putExtra("customerid", customerFacade.getCustomerID());
                launcher.launch(intent);

            }
             if (view.getId() == btnSearch.getId()){
               double maxPrice = Double.parseDouble(etMaxPrice.getText().toString());
                try {
                    ArrayList<Coupon> couponsByPrice = customerFacade.getCustomerCoupons(maxPrice);
                    adapter = new CompanyCouponsLvAdapter(CustomerActivity.this, R.layout.coupon_line,couponsByPrice);
                    lvCoupons.setAdapter(adapter);
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
             }
                if(view.getId() == btnQRCode.getId()){
                    if(selectedRow != -1){
                        String qrCode;
                        qrCode = customerFacade.getCustomerID()+"";
                        try {
                            Coupon c = customerFacade.getCustomerCoupons().get(selectedRow);
                            qrCode += c.getId() +"";
                            Intent intent = new Intent(CustomerActivity.this, CouponQRCodeActivity.class);
                            intent.putExtra("qrCode",qrCode );
                            intent.putExtra("coupon",c);
                            intent.putExtra("customerId",customerFacade.getCustomerID()+"");
                            startActivity(intent);
                        } catch (ParseException e) {
                            throw new RuntimeException(e);
                        }
                        selectedRow = -1;
                    }
                    else {
                        Toast.makeText(CustomerActivity.this, "Please choose a coupon", Toast.LENGTH_SHORT).show();
                    }

                }


                /*{
                   try {
                       couponsByCategory = customerFacade.getCustomerCoupons(Category.valueOf(selectedCategory));
                       ArrayList<Coupon> priceAndCategory= new ArrayList<>();
                       Set<Coupon> couponsCategory = new HashSet<>(couponsByCategory);
                       couponsByPrice = customerFacade.getCustomerCoupons(maxPrice);
                       for(Coupon coupon : couponsByPrice)
                           if(couponsCategory.contains(coupon))
                               priceAndCategory.add(coupon);
                       adapter.refreshAllCoupons(priceAndCategory);

                   } catch (ParseException e) {
                       throw new RuntimeException(e);
                   }

               }*/


            }
        }
    }

