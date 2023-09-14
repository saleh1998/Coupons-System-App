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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toolbar;

import java.util.ArrayList;
import java.util.Date;

public class AddCouponActivity extends AppCompatActivity {

    Spinner categoriesSpin;
    EditText etTitle, etDescription, etStartDate, etEndDate, etAmount, etPrice, etImg;
    ImageButton btnBack;
    Button btnSave;
    Toolbar toolbar;
    String [] categoriesList;
    int row;
    DB_Manager db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_coupon);

        Intent intent = getIntent();
        /* we have to get the comp from the intent */
        categoriesSpin = findViewById(R.id.addCoupon_spCategory);
        etTitle = findViewById(R.id.addCoupon_etTitle);
        etDescription = findViewById(R.id.addCoupon_etDescription);
        etStartDate = findViewById(R.id.addCoupon_etStartDate);
        etEndDate = findViewById(R.id.addCoupon_etEndDate);
        etAmount = findViewById(R.id.addCoupon_etAmount);
        btnBack = findViewById(R.id.addCoupon_btnBack);
        etImg = findViewById(R.id.addCoupon_etImgSrc);
        btnSave = findViewById(R.id.addCoupon_btnSave);
        toolbar= findViewById(R.id.addCoupon_toolbar);

        db = DB_Manager.getInstance(this);


        /* we have to get allCategories from db manager then put thim in categoriesList
        *******************************************************************************
        ArrayAdapter aa = new ArrayAdapter(this, android.R.layout.simple_spinner_item, categoriesList);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(aa);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
            SupportClass supportClass = SupportClass.getInstance(AddCouponActivity.this);
            if(v.getId() == btnBack.getId()){
                finish();
            }
            if(v.getId() == btnSave.getId()){
               /* ArrayList<Category> categories = db.getAllCategories();
                int categoryID = db.getCategoryID(categories.get(row));
                String title = etTitle.getText().toString();
                String describtion = etDescription.getText().toString();
                Date startDate = supportClass.getDate(etStartDate.getText().toString());
                Date endDate = supportClass.getDate(etEndDate.getText().toString());
                int anount = Integer.parseInt(etAmount.getText().toString());
                double price = Double.parseDouble(etPrice.getText().toString());
                String imgSrc = etImg.getText().toString();
                // we hav to creat new coupon with this fileds + companyID from the obj that we have got it from the intent*/
            }

        }
    }
}