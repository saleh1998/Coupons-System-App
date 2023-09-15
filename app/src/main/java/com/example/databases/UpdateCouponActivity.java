package com.example.databases;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toolbar;

import java.util.ArrayList;

public class UpdateCouponActivity extends AppCompatActivity {

    Spinner categoriesSpin;
    EditText etTitle, etDescription, etStartDate, etEndDate, etAmount, etPrice, etImg;
    ImageButton btnBack;
    Button btnUpdate;

    String [] categoriesList;
    int row;
    DB_Manager db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_coupon);

        Intent intent = getIntent();
        /* we have to get the coupon from the intent */
        categoriesSpin = findViewById(R.id.updateCoupon_spCategory);
        etTitle = findViewById(R.id.updateCoupon_etTitle);
        etDescription = findViewById(R.id.updateCoupon_etDescription);
        etStartDate = findViewById(R.id.updateCoupon_etStartDate);
        etEndDate = findViewById(R.id.updateCoupon_etEndDate);
        etAmount = findViewById(R.id.updateCoupon_etAmount);
        btnBack = findViewById(R.id.updateCoupon_btnBack);
        etImg = findViewById(R.id.updateCoupon_etImgSrc);
        btnUpdate = findViewById(R.id.updateCoupon_btnUpdate);


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
            SupportClass supportClass = SupportClass.getInstance(UpdateCouponActivity.this);
            if(v.getId() == btnBack.getId()){
                finish();
            }
            if(v.getId() == btnUpdate.getId()){
               /* ArrayList<Category> categories = db.getAllCategories();
                int categoryID = db.getCategoryID(categories.get(row));
                String title = etTitle.getText().toString();
                String describtion = etDescription.getText().toString();
                Date startDate = supportClass.getDate(etStartDate.getText().toString());
                Date endDate = supportClass.getDate(etEndDate.getText().toString());
                int anount = Integer.parseInt(etAmount.getText().toString());
                double price = Double.parseDouble(etPrice.getText().toString());
                String imgSrc = etImg.getText().toString();*/
                // we hav to creat new coupon with this fileds + companyID from the obj that we have got it from the intent
            }

        }
    }

}