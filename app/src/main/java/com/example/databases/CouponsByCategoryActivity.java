package com.example.databases;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;

public class CouponsByCategoryActivity extends AppCompatActivity {

    Spinner categoriesSpin;
    ListView couponsLv;
    CompanyCouponsLvAdapter lvAdapter;
    ImageButton btnBack;
    Button btnSearch;
    String [] categoriesList;
    int row;
    DB_Manager db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coupons_by_category);

        couponsLv = findViewById(R.id.cpnByCategory_lv);
        categoriesSpin = findViewById(R.id.cpnByCategory_sp);
        btnBack = findViewById(R.id.cpnByCategory_btnBack);
        btnSearch = findViewById(R.id.cpnByCategory_btnSearch);
        db = DB_Manager.getInstance(this);

        ButtonsClick buttonsClick = new ButtonsClick();
        btnSearch.setOnClickListener(buttonsClick);
        btnBack.setOnClickListener(buttonsClick);


        /* we have to get allCategories from db manager then put thim in categoriesList
        *******************************************************************************
        ArrayAdapter aa = new ArrayAdapter(this, android.R.layout.simple_spinner_item, categoriesList);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categoriesSpin.setAdapter(aa);
        categoriesSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                row = position;
                ((TextView) parent.getChildAt(0)).setTextSize(30);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });*/

    }

    class ButtonsClick implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            if(v.getId() == btnBack.getId()){
                finish();
            }
            if(v.getId() == btnSearch.getId()){
               /* Category category = db.getAllCategories.get(row);
                ArrayList<Coupon> coupons = db.getCompanyCoupons(category);
                lvAdapter  = new CompanyCouponsLvAdapter(CouponsByCategoryActivity.this,R.layout.coupon_line,coupons);*/
            }

        }
    }
}