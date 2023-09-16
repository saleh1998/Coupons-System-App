package com.example.databases;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.Serializable;
import java.text.ParseException;
import java.util.ArrayList;

public class BuyCouponActivity extends AppCompatActivity {
  Button search;
    int selectedRow = -1;
    ListView lvCoupons;
    ImageButton imBack;
    Button btnBuy;
    Spinner spCategory;
    ArrayList<Coupon> allCoupons;
    CustomerFacade customerFacade;
    CompanyCouponsLvAdapter adapter;
    EditText etMaxPrice;

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
            int customerid = intent.getIntExtra("customerid",0);
            customerFacade = new CustomerFacade(this);
            customerFacade.setCustomerID(customerid);
            }

        ///// initializing the list view
        try {
            CouponsDBDAO couponsDBDAO= new CouponsDBDAO(this); //not sure
            allCoupons = couponsDBDAO.getAllCoupons();
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        adapter = new CompanyCouponsLvAdapter(this, R.layout.coupon_line, allCoupons);
        lvCoupons.setAdapter(adapter);


        lvCoupons.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                selectedRow = i;
                lvCoupons.setSelection(i);
            }
        });
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
        btnBuy.setOnClickListener(buttonsClick);
        imBack.setOnClickListener(buttonsClick);
        search.setOnClickListener(buttonsClick);
        spCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != -1) {
                    Category category = Category.valueOf(items.get(position));
                    try {
                        ArrayList<Coupon> couponsByCat = customerFacade.getCustomerCoupons(category);
                        adapter = new CompanyCouponsLvAdapter(BuyCouponActivity.this, R.layout.coupon_line, couponsByCat);
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

    }

    class ButtonsClick implements View.OnClickListener{

        @Override
        public void onClick(View view) {
            if(view.getId() == imBack.getId()){
                finish();
            } else if (view.getId() == btnBuy.getId()) {
                Intent intent = getIntent();
                Coupon c = null;
                    c = allCoupons.get(selectedRow);
                if (c != null) {
                    intent.putExtra("coupon", (Serializable) c);
                    intent.putExtra("requestCode", 1);
                    setResult(RESULT_OK, intent);
                    finish();

                }
                else {
                    Toast.makeText(BuyCouponActivity.this, "null coupon", Toast.LENGTH_SHORT).show();
                }
            }
            if (view.getId() == search.getId()){
                double maxPrice = Double.parseDouble(etMaxPrice.getText().toString());
                try {
                    ArrayList<Coupon> couponsByPrice = customerFacade.getCustomerCoupons(maxPrice);
                    adapter = new CompanyCouponsLvAdapter(BuyCouponActivity.this, R.layout.coupon_line,couponsByPrice);
                    lvCoupons.setAdapter(adapter);
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}