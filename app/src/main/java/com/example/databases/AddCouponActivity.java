package com.example.databases;

import androidx.appcompat.app.AppCompatActivity;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

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
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;

public class AddCouponActivity extends AppCompatActivity {

    Spinner categoriesSpin;
    EditText etTitle, etDescription, etStartDate, etEndDate, etAmount, etPrice, etImg;
    ImageButton btnBack;
    Button btnSave;
    Toolbar toolbar;
    String [] categoriesList;
    Date startDate,endDate;
    int row;
    CompanyFacade companyFacade;
    private Calendar calendar;
    private SimpleDateFormat dateFormat;
    DB_Manager db ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_coupon);
        db = DB_Manager.getInstance(this);
        Intent intent = getIntent();
        /* we have to get the comp from the intent */
        int companyid = intent.getIntExtra("companyid",0);
        companyFacade = new CompanyFacade(companyid,this);
        etTitle = findViewById(R.id.addCoupon_etTitle);
        etDescription = findViewById(R.id.addCoupon_etDescription);
        etStartDate = findViewById(R.id.addCoupon_etStartDate);
        etEndDate = findViewById(R.id.addCoupon_etEndDate);
        etAmount = findViewById(R.id.addCoupon_etAmount);
        btnBack = findViewById(R.id.addCoupon_btnBack);
        etImg = findViewById(R.id.addCoupon_etImgSrc);
        btnSave = findViewById(R.id.addCoupon_btnSave);
        toolbar= findViewById(R.id.addCoupon_toolbar);
        etPrice = findViewById(R.id.addCoupon_etPrice); // this was forgot when writing  !!!!!!!!!!!!
        calendar = Calendar.getInstance();
        dateFormat = new SimpleDateFormat("dd-MM-yyyy");

        categoriesSpin = findViewById(R.id.addCoupon_spCategory);
        ArrayAdapter<Category> categoryAdapter;
        categoryAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, Category.values());
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categoriesSpin.setAdapter(categoryAdapter);


        /* we have to get allCategories from db manager then put them in categoriesList
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


        ButtonsClick buttonsClick =new ButtonsClick();
        btnSave.setOnClickListener(buttonsClick);
        btnBack.setOnClickListener(buttonsClick);

    }
    public void onStartDateClick(View view) {
        showDatePickerDialog(etStartDate);
    }

    public void onEndDateClick(View view) {
        showDatePickerDialog(etEndDate);
    }

    private void showDatePickerDialog(final EditText editText) {
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
                        calendar.set(selectedYear, selectedMonth, selectedDay);
                        Date selectedDate = calendar.getTime();
                        String formattedDate = dateFormat.format(selectedDate);
                        editText.setText(formattedDate);
                        if(editText.equals(etStartDate))
                            startDate = selectedDate;
                        if(editText.equals(etEndDate))
                            endDate = selectedDate;
                    }
                },
                year, month, day
        );
        datePickerDialog.show();
    }
    class ButtonsClick implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            SupportClass supportClass = SupportClass.getInstance(AddCouponActivity.this);
            if(v.getId() == btnBack.getId()){
                finish();
            }
            if(v.getId() == btnSave.getId()){
// try {
// ArrayList<Category> categories = db.getAllCategories();
// } catch (ParseException e) {
// throw new RuntimeException(e);
// }

                Category selectedCategory = (Category) categoriesSpin.getSelectedItem();
                String title = etTitle.getText().toString();
                String description = etDescription.getText().toString();
                int amount = Integer.parseInt(etAmount.getText().toString());
                double price = Double.parseDouble(etPrice.getText().toString());
                String imgSrc = etImg.getText().toString();
                int companyId= companyFacade.getCompanyID();
                Coupon newCoupon = new Coupon(companyId, selectedCategory, title, description, startDate, endDate, amount, price, imgSrc);

/*
                Intent intent1 = new Intent();
                intent1.putExtra("product", p);
                intent1.putExtra("requestCode", 1);*/

                    Intent intent = new Intent();
                    intent.putExtra("coupon",newCoupon);
                    intent.putExtra("codeForCompanyActivity",1);
                    setResult(RESULT_OK, intent);
                    Toast.makeText(AddCouponActivity.this, "Coupon added successfully!", Toast.LENGTH_SHORT).show();
                    finish();
            }

        }

    }
}