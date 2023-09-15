package com.example.databases;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import java.util.ArrayList;

public class CouponsByPriceActivity extends AppCompatActivity {

    EditText etPrice;
    ListView couponsLv;
    CompanyCouponsLvAdapter lvAdapter;
    ImageButton btnBack;
    Button btnSearch;
    DB_Manager db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coupons_by_price);
        couponsLv = findViewById(R.id.cpnByPrice_lv);
        etPrice = findViewById(R.id.cpnByPrice_etPrice);
        btnBack = findViewById(R.id.cpnByPrice_btnBack);
        btnSearch = findViewById(R.id.cpnByPrice_btnSearch);
        db = DB_Manager.getInstance(this);

        ButtonsClick buttonsClick = new ButtonsClick();
        btnSearch.setOnClickListener(buttonsClick);
        btnBack.setOnClickListener(buttonsClick);


    }
    class ButtonsClick implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            if(v.getId() == btnBack.getId()){
                finish();
            }
            if(v.getId() == btnSearch.getId()){
               Double price = Double.parseDouble(etPrice.getText().toString());
                /*ArrayList<Coupon> coupons = db.getCompanyCoupons(price);
                lvAdapter  = new CompanyCouponsLvAdapter(CouponsByPriceActivity.this,R.layout.coupon_line,coupons);*/
            }

        }
    }


}