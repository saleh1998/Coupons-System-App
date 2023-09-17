package com.example.databases;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import android.app.DatePickerDialog;
import android.net.Uri;
import android.os.Bundle;

import android.provider.MediaStore;
import android.text.TextUtils;

import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.content.Intent;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

public class AddCouponActivity extends AppCompatActivity {

    Spinner categoriesSpin;
    EditText etTitle, etDescription, etStartDate, etEndDate, etAmount, etPrice, etImg;
    ImageButton btnBack , addImage;
    Button btnSave;
    Toolbar toolbar;
    String [] categoriesList;
    Date startDate,endDate;
    int row;
    CompanyFacade companyFacade;
    private Calendar calendar;
    private SimpleDateFormat dateFormat;
    Uri selectedImage;
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
       // etImg = findViewById(R.id.addCoupon_etImgSrc);
        btnSave = findViewById(R.id.addCoupon_btnSave);
        toolbar= findViewById(R.id.addCoupon_toolbar);
        etPrice = findViewById(R.id.addCoupon_etPrice); // this was forgot when writing  !!!!!!!!!!!!
        calendar = Calendar.getInstance();
        dateFormat = new SimpleDateFormat("dd-MM-yyyy");

        addImage = findViewById(R.id.addCoupon_addIm);


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

        addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                launcher.launch(intent);

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

    ActivityResultLauncher<Intent> launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            Intent intent = result.getData();
            if (intent != null) {
                if (result.getResultCode() == RESULT_OK ){
                    selectedImage = intent.getData();
                    addImage.setImageURI(selectedImage);

                }
            }
        }
    });
    class ButtonsClick implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            SupportClass supportClass = SupportClass.getInstance(AddCouponActivity.this);
            if(v.getId() == btnBack.getId()){
                finish();
            }

            if(v.getId() == btnSave.getId()) {

                if (TextUtils.isEmpty(etTitle.getText()) ||
                        TextUtils.isEmpty(etDescription.getText()) ||
                        TextUtils.isEmpty(etStartDate.getText()) ||
                        TextUtils.isEmpty(etEndDate.getText()) ||
                        TextUtils.isEmpty(etAmount.getText()) ||
                        TextUtils.isEmpty(etPrice.getText()) ) {

                    Toast.makeText(AddCouponActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();

                } else {

                    Category selectedCategory = (Category) categoriesSpin.getSelectedItem();
                    String title = etTitle.getText().toString();
                    String description = etDescription.getText().toString();
                    int amount = Integer.parseInt(etAmount.getText().toString());
                    double price = Double.parseDouble(etPrice.getText().toString());
/*
                    String imgSrc = etImg.getText().toString();
*/
                    String imgSrc = selectedImage.toString();
                    int companyId = companyFacade.getCompanyID();
                    if (startDate.after(endDate)) {
                        Toast.makeText(AddCouponActivity.this, "end date can not be before start date", Toast.LENGTH_SHORT).show();

                    } else {
                        Coupon newCoupon = new Coupon(companyId, selectedCategory, title, description, startDate, endDate, amount, price, imgSrc);

                        Intent intent = new Intent();
                        intent.putExtra("coupon", newCoupon);
                        intent.putExtra("codeForCompanyActivity", 1);
                        setResult(RESULT_OK, intent);
                        /*Toast.makeText(AddCouponActivity.this, "Coupon added successfully!", Toast.LENGTH_SHORT).show();*/
                        finish();
                    }
                }
            }
        }

    }
}