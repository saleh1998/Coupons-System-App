package com.example.databases;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import java.text.ParseException;
import java.util.ArrayList;

public class CouponsByPriceActivity extends AppCompatActivity {

    EditText etPrice;
    ListView couponsLv;
    CompanyCouponsLvAdapter lvAdapter;
    ImageButton btnBack;
    ImageButton btnSearch;
    CompanyFacade companyFacade;
    Double price = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coupons_by_price);
        couponsLv = findViewById(R.id.cpnByPrice_lv);
        etPrice = findViewById(R.id.cpnByPrice_etPrice);
        btnBack = findViewById(R.id.cpnByPrice_btnBack);
        btnSearch = findViewById(R.id.cpnByPrice_btnSearch);

        ButtonsClick buttonsClick = new ButtonsClick();
        btnSearch.setOnClickListener(buttonsClick);
        btnBack.setOnClickListener(buttonsClick);
        Intent intent = getIntent();
        if (intent != null) {
            int companyid = intent.getIntExtra("companyid",0);
            companyFacade = new CompanyFacade(companyid,this);
        } else {
            try {
                throw new myException("Error:Intent is null");
            } catch (myException e) {
                throw new RuntimeException(e);
            }
        }


    }
    class ButtonsClick implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            if(v.getId() == btnBack.getId()){
                finish();
            }
            if(v.getId() == btnSearch.getId()){
                if(!etPrice.getText().toString().trim().isEmpty()) {
                price = Double.parseDouble(etPrice.getText().toString()); }
                ArrayList<Coupon> coupons = null;
                try {
                    coupons = companyFacade.getCompanyCoupons(price);
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
                if(coupons!= null) {
                    lvAdapter = new CompanyCouponsLvAdapter(CouponsByPriceActivity.this, R.layout.coupon_line, coupons);
                    couponsLv.setAdapter(lvAdapter);

                }
            }

        }
    }


}