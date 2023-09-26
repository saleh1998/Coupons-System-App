package com.example.databases;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class UpdateCouponActivity extends AppCompatActivity {

    Spinner categoriesSpin;
    ImageButton addImage;
    EditText etTitle, etDescription, etStartDate, etEndDate, etAmount, etPrice, etImg;
    ImageButton btnBack;
    Button btnUpdate;
    int companyid, couponid;
    private Calendar calendar;
    String[] categoriesList;
    int row;
    private SimpleDateFormat dateFormat;
    Date startDate, endDate;
    Coupon coupon;
    DB_Manager db;
    Spinner spinner;
    Uri selectedImage;
    boolean flag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_coupon);
        flag =false;
        Intent intent = getIntent();
        companyid = intent.getIntExtra("companyid", 0);
        Coupon c = (Coupon) intent.getSerializableExtra("coupon");
        coupon = c;

        couponid = c.getId();
        etPrice = findViewById(R.id.updateCoupon_etPrice);
        etTitle = findViewById(R.id.updateCoupon_etTitle);
        etDescription = findViewById(R.id.updateCoupon_etDescription);
        etStartDate = findViewById(R.id.updateCoupon_etStartDate);
        etEndDate = findViewById(R.id.updateCoupon_etEndDate);
        etAmount = findViewById(R.id.updateCoupon_etAmount);

        btnBack = findViewById(R.id.updateCoupon_btnBack);
        addImage = findViewById(R.id.updateCoupon_addIm);
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


        String imagePathFromDB = c.getImage();
        Bitmap imageBitmap = loadImageFromInternalStorage(imagePathFromDB);
        if (imageBitmap != null) {
            addImage.setImageBitmap(imageBitmap);
        }

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
        addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                launcher.launch(intent);


            }
        });
    }





    private Bitmap loadImageFromInternalStorage(String path) {
        try {
            File file = new File(path);
            return BitmapFactory.decodeStream(new FileInputStream(file));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }






    ActivityResultLauncher<Intent> launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            Intent intent = result.getData();
            if (intent != null) {
                if (result.getResultCode() == RESULT_OK ){
                    selectedImage = Uri.parse(String.valueOf(intent.getData()));
                    addImage.setImageURI(selectedImage);
                     flag = true;


                }
            }
        }
    });

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



    private String saveImageToInternalStorage(Bitmap bitmap, Context context) {
        try {
            // Use the app's private directory.
            File directory = context.getDir("imageDir", Context.MODE_PRIVATE);
            // Name the file.
            File myImageFile = new File(directory, "selectedImage.jpg");

            FileOutputStream fos = new FileOutputStream(myImageFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fos);
            fos.close();

            return myImageFile.getAbsolutePath();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
    class ButtonsClick implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            SupportClass supportClass = SupportClass.getInstance(UpdateCouponActivity.this);
            if (v.getId() == btnBack.getId()) {
                finish();
            }
            if (v.getId() == btnUpdate.getId()) {
                if (startDate.after(endDate)) {
                    Toast.makeText(UpdateCouponActivity.this, "end date can not be before start date", Toast.LENGTH_SHORT).show();

                }
                ArrayList<Category> categories = null;
                try {
                    categories = db.getAllCategories();
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
                Category cate = Category.values()[row];
                String title = etTitle.getText().toString();
                String describtion = etDescription.getText().toString();
                if(startDate == null)
                {
                    startDate = coupon.getStartDate();


                }
                if(endDate == null)
                {

                    endDate = coupon.getEndDate();
                }
                int anount = Integer.parseInt(etAmount.getText().toString());
                double price = Double.parseDouble(etPrice.getText().toString());
                String imgSrc = coupon.getImage();
                if(flag)
                {

                    Bitmap selectedImageBitmap = null;
                    try {
                        selectedImageBitmap = MediaStore.Images.Media.getBitmap(UpdateCouponActivity.this.getContentResolver(), selectedImage);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    imgSrc = saveImageToInternalStorage(selectedImageBitmap, UpdateCouponActivity.this);

                }

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