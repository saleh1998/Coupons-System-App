package com.example.databases;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toolbar;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class UpdateCouponActivity extends AppCompatActivity {

    Spinner categoriesSpin;
    EditText etTitle, etDescription, etStartDate, etEndDate, etAmount, etPrice, etImg;
    ImageButton btnBack;
    Button btnUpdate;
    int companyid, couponid;
    private Calendar calendar;
    String[] categoriesList;
    int row;
    private SimpleDateFormat dateFormat;
    Date startDate, endDate;

    DB_Manager db;
    Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_coupon);

        Intent intent = getIntent();
        companyid = intent.getIntExtra("companyid", 0);
        Coupon c = (Coupon) intent.getSerializableExtra("coupon");
        /* we have to get the coupon from the intent */
/*
        categoriesSpin = findViewById(R.id.updateCoupon_spCategory);
*/


        couponid = c.getId();
        etPrice = findViewById(R.id.updateCoupon_etPrice);
        etTitle = findViewById(R.id.updateCoupon_etTitle);
        etDescription = findViewById(R.id.updateCoupon_etDescription);
        etStartDate = findViewById(R.id.updateCoupon_etStartDate);
        etEndDate = findViewById(R.id.updateCoupon_etEndDate);
        etAmount = findViewById(R.id.updateCoupon_etAmount);
        btnBack = findViewById(R.id.updateCoupon_btnBack);
        etImg = findViewById(R.id.updateCoupon_etImgSrc);
        btnUpdate = findViewById(R.id.updateCoupon_btnUpdate);
        spinner = findViewById(R.id.updateCoupon_spCategory);
        dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        calendar = Calendar.getInstance();
        ButtonsClick buttonsClick = new ButtonsClick();
        btnUpdate.setOnClickListener(buttonsClick);
        btnBack.setOnClickListener(buttonsClick);
        db = DB_Manager.getInstance(this);
        ArrayAdapter aa = new ArrayAdapter(this, android.R.layout.simple_spinner_item, Category.values());
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(aa);


        etTitle.setText(c.getTitle());
        etDescription.setText(c.getDescription());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Date start = c.getStartDate();
        Date end = c.getEndDate();
        etStartDate.setText(simpleDateFormat.format(start));
        etEndDate.setText(simpleDateFormat.format(end));
        etAmount.setText(c.getAmount()+"");
        etPrice.setText(c.getPrice()+"");
        etImg.setText(c.getImage());
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
                        if (editText.equals(etStartDate))
                            startDate = selectedDate;
                        if (editText.equals(etEndDate))
                            endDate = selectedDate;
                    }
                },
                year, month, day
        );
        datePickerDialog.show();
    }

    class ButtonsClick implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            SupportClass supportClass = SupportClass.getInstance(UpdateCouponActivity.this);
            if (v.getId() == btnBack.getId()) {
                finish();
            }
            if (v.getId() == btnUpdate.getId()) {
                ArrayList<Category> categories = null;
                try {
                    categories = db.getAllCategories();
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
                Category cate = Category.values()[row];
                String title = etTitle.getText().toString();
                String describtion = etDescription.getText().toString();
               /* Date startDate = supportClass.getDate(etStartDate.getText().toString());
                Date endDate = supportClass.getDate(etEndDate.getText().toString());*/
                int anount = Integer.parseInt(etAmount.getText().toString());
                double price = Double.parseDouble(etPrice.getText().toString());
                String imgSrc = etImg.getText().toString();

                Coupon c = new Coupon(couponid, companyid, cate, title, describtion, startDate, endDate, anount, price, imgSrc);
                Intent intent = new Intent();
                intent.putExtra("coupon", c);
                intent.putExtra("codeForCompanyActivity", 2);
                setResult(RESULT_OK, intent);
                finish();

            }

        }
    }

}