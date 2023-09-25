package com.example.databases;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.Serializable;
import java.text.ParseException;
import java.util.ArrayList;

public class BuyCouponActivity extends AppCompatActivity implements Serializable {
    ImageButton search;
    int selectedRow = -1;
    int bgLineColor;
    LinearLayout bgLayout;
    ListView lvCoupons;
    ImageButton imBack;
    Button btnBuy;
    Spinner spCategory;
    ArrayList<Coupon> allCoupons = null;;
    CustomerFacade customerFacade;
    CompanyCouponsLvAdapter adapter;
    EditText etMaxPrice;
    CouponsDBDAO couponsDBDAO;
    ArrayList<Coupon> requested = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_coupon);
        lvCoupons = findViewById(R.id.buyCpn_lvCoupons);
        imBack = findViewById(R.id.buyCpn_btnBack);
        btnBuy = findViewById(R.id.buyCpn_btnbuy);
        search= findViewById(R.id.buyCpn_btnSearch);
        spCategory = findViewById(R.id.buyCpn_categorySpinner);
        etMaxPrice = findViewById(R.id.buyCpn_etMaxPrice);
        Intent intent = getIntent();
        if(intent != null){
            int customerId = intent.getIntExtra("customerid",0);
            customerFacade = new CustomerFacade(this);
            customerFacade.setCustomerID(customerId);
            }
        ///// initializing the list view
        try {
           couponsDBDAO= new CouponsDBDAO(this); //not sure
            allCoupons = couponsDBDAO.getAllCoupons();
            adapter = new CompanyCouponsLvAdapter(BuyCouponActivity.this, R.layout.coupon_line, allCoupons);
            if(allCoupons != null) {
                lvCoupons.setAdapter(adapter);
            }
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }



        lvCoupons.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(selectedRow != -1){
                    //bgLayout.setBackgroundColor(bgLineColor);
                    if(bgLayout!= null) {
                    Drawable borderDrawable = getResources().getDrawable(R.drawable.border);
                    bgLayout.setBackground(borderDrawable);
                } }
                selectedRow = position;
                lvCoupons.setSelection(position);

                bgLayout = (LinearLayout) view.findViewById(R.id.couponLine_layout);
                bgLineColor = view.getSolidColor();
                bgLayout.setBackgroundColor(Color.rgb(150,150,150));
            }
        });

        ArrayAdapter categoryAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, Category.values());
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCategory.setAdapter(categoryAdapter);

        ButtonsClick buttonsClick = new ButtonsClick();
        btnBuy.setOnClickListener(buttonsClick);
        imBack.setOnClickListener(buttonsClick);
        search.setOnClickListener(buttonsClick);


        spCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != -1) {
                    Category category = (Category) spCategory.getSelectedItem();
                    try {
                        requested.clear();
                        ArrayList<Coupon> couponsByCat =  couponsDBDAO.getAllCoupons();
                        for (Coupon c:couponsByCat) {
                            if(c.getCategory()==(category))
                               requested.add(c);
                        }
                        CompanyCouponsLvAdapter adapter2 = new CompanyCouponsLvAdapter(BuyCouponActivity.this, R.layout.coupon_line, requested);
                       lvCoupons.setAdapter(adapter2);
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    class ButtonsClick implements View.OnClickListener{

        @Override
        public void onClick(View view) {
            if(view.getId() == imBack.getId()){
                finish();
            } else if (view.getId() == btnBuy.getId()) {
                if(selectedRow != -1) {
                    Intent intent = getIntent();
                    Coupon c = null;
                    c = requested.get(selectedRow);
                    if (c != null) {
                        intent.putExtra("coupon", (Serializable) c);
                        intent.putExtra("requestCode", 1);
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                    selectedRow = -1;
                }
                else {
                    Toast.makeText(BuyCouponActivity.this, "Select a coupon", Toast.LENGTH_SHORT).show();
                }
            }
            if (view.getId() == search.getId()){
                if(!etMaxPrice.getText().toString().trim().isEmpty()) {
                    double maxPrice = Double.parseDouble(etMaxPrice.getText().toString());
                    try {
                        ArrayList<Coupon> couponsByPrice = customerFacade.getAllCouponsbyprice(maxPrice);
                        adapter = new CompanyCouponsLvAdapter(BuyCouponActivity.this, R.layout.coupon_line, couponsByPrice);
                        if (couponsByPrice != null)
                            lvCoupons.setAdapter(adapter);
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
    }
}