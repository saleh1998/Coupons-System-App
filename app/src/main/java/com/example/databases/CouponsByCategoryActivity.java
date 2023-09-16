package com.example.databases;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.ParseException;
import java.util.ArrayList;

public class CouponsByCategoryActivity extends AppCompatActivity {

    Spinner categoriesSpin;
    ListView couponsLv;
    CompanyCouponsLvAdapter lvAdapter;
    ImageButton btnBack;
    Button btnSearch;
    ArrayList<Category> categoriesList;
    int row;

    DB_Manager db;
    CompanyFacade companyFacade;
    ArrayList<String> couponsbycat = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coupons_by_category);

        couponsLv = findViewById(R.id.cpnByCategory_lv);
        categoriesSpin = findViewById(R.id.cpnByCategory_sp);
        btnBack = findViewById(R.id.cpnByCategory_btnBack);
        btnSearch = findViewById(R.id.cpnByCategory_btnSearch);
        db = DB_Manager.getInstance(this);
        Intent intent = getIntent();
        if (intent != null) {
            int companyid = intent.getIntExtra("companyid", 0);
            companyFacade = new CompanyFacade(companyid, this);
        } else {
            try {
                throw new myException("Error:Intent is null");
            } catch (myException e) {
                throw new RuntimeException(e);
            }
        }

        try {
            categoriesList = db.getAllCategories();
            for (Category c : categoriesList)
            {
                couponsbycat.add(c.toString());
            }
        }
        catch (ParseException e) {
            throw new RuntimeException(e);
        }
        ButtonsClick buttonsClick = new ButtonsClick();
        btnSearch.setOnClickListener(buttonsClick);
        btnBack.setOnClickListener(buttonsClick);


        /*   we have to get allCategories from db manager then put thim in categoriesList
         ********************************************************************************/
        ArrayAdapter aa = new ArrayAdapter(this, android.R.layout.simple_spinner_item, Category.values());
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        if (couponsbycat != null)
        {
            categoriesSpin.setAdapter(aa);
        }
            categoriesSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    row = position;
                    ((TextView) parent.getChildAt(0)).setTextSize(30);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

        }


    class ButtonsClick implements View.OnClickListener
    {
        @Override
        public void onClick(View v) {
            if(v.getId() == btnBack.getId()){
                finish();
            }
            if(v.getId() == btnSearch.getId()){
                Category category = categoriesList.get(row);
                ArrayList<Coupon> coupons = null;
                try {
                    coupons = companyFacade.getCompanyCoupons(category);
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
                lvAdapter  = new CompanyCouponsLvAdapter(CouponsByCategoryActivity.this,R.layout.coupon_line,coupons);
                if(coupons!=null) {
                couponsLv.setAdapter(lvAdapter);
            }
            }

        }
    }
}