package com.example.databases;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;

import java.io.Serializable;
import java.text.ParseException;
import java.util.ArrayList;

public class BuyCouponActivity extends AppCompatActivity {
    int selectedRow = -1;
    ListView lvCoupons;
    ImageButton imBack;
    Button btnBuy;
    ArrayList<Coupon> customerCoupons;
    CustomerFacade customerFacade;
    CompanyCouponsLvAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_coupon);
        lvCoupons = findViewById(R.id.buyCpn_lvCoupons);
        imBack = findViewById(R.id.buyCpn_btnBack);
        btnBuy = findViewById(R.id.buyCpn_btnbuy);

        Intent intent = getIntent();
        if(intent != null){
            int customerid = intent.getIntExtra("customerid",0);
            customerFacade = new CustomerFacade(this);
            customerFacade.setCustomerID(customerid);
            }

        ///// initializing the list view
        try {
            CouponsDBDAO couponsDBDAO= new CouponsDBDAO(this); //not sure
            customerCoupons = couponsDBDAO.getAllCoupons();
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        adapter = new CompanyCouponsLvAdapter(this, R.layout.coupon_line,customerCoupons);
        lvCoupons.setAdapter(adapter);


        lvCoupons.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                selectedRow = i;
                lvCoupons.setSelection(i);
            }
        });

        ButtonsClick buttonsClick = new ButtonsClick();
        btnBuy.setOnClickListener(buttonsClick);
        imBack.setOnClickListener(buttonsClick);

    }

    class ButtonsClick implements View.OnClickListener{

        @Override
        public void onClick(View view) {
            if(view.getId() == imBack.getId()){
                finish();
            } else if (view.getId() == btnBuy.getId()) {
                Intent intent = getIntent();
                Coupon coupon = (Coupon) lvCoupons.getSelectedItem(); /// ef7se etha getSelectedItem() returns the selected coupon
                intent.putExtra("coupon",(Serializable) coupon);
                intent.putExtra("requestCode", 1);
                setResult(RESULT_OK, intent);
                finish();
                
            }
        }
    }
}