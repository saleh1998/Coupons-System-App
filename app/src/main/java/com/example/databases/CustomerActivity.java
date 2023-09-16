package com.example.databases;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class CustomerActivity extends AppCompatActivity {

    Button btnSearch,btnBuyCoupon;
    ListView lvCoupons;
    Spinner spCategory;
    EditText etMaxPrice;

    CustomerFacade customerFacade;
    CompanyCouponsLvAdapter adapter; // ef7se etha bfreqsh
    ArrayList<Coupon> customerCoupons;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer);
        btnBuyCoupon = findViewById(R.id.customer_btnBuy);
        btnSearch = findViewById(R.id.customer_btnSearch);
        lvCoupons = findViewById(R.id.customer_lvCoupons);
        spCategory = findViewById(R.id.customer_categorySpinner);
        etMaxPrice = findViewById(R.id.customer_etMaxPrice);

        Intent intent = getIntent();
        if(intent != null){
            int customerid = intent.getIntExtra("customerid",0);
            customerFacade = new CustomerFacade(this);
            customerFacade.setCustomerID(customerid);
        }


        ///// initializing the list view
        try {
            customerCoupons = customerFacade.getCustomerCoupons();
            if(customerCoupons != null){
            adapter = new CompanyCouponsLvAdapter(this, R.layout.coupon_line,customerCoupons);
            lvCoupons.setAdapter(adapter);}
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }



        ArrayAdapter<String> categoryAdapter;
        ArrayList<String> items = new ArrayList<>();
        items.add("choose category");
        items.add(Category.Food.toString());
        items.add(Category.Electricity.name());
        items.add(Category.Restaurant.name());
        items.add(Category.Vacation.name());
        categoryAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, items);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
       spCategory.setAdapter(categoryAdapter);

        ButtonsClick buttonsClick = new ButtonsClick();
        btnBuyCoupon.setOnClickListener(buttonsClick);
        btnSearch.setOnClickListener(buttonsClick);






    }

    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        ////////// esale tayma hon

        if(item.getItemId() == R.id.navCustomer_profile){
            Intent intent = new Intent(CustomerActivity.this, CompanyViewProfileActivity.class);
            /*we have to send the company with the intent*/
            startActivity(intent);
        }

        return true;
    }

    ActivityResultLauncher<Intent> launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            Intent intent = result.getData();
            if(intent != null) {
                int requestCode = intent.getIntExtra("requestCode", 0);
                if (requestCode == 1){ /// buy new coupon
                    if (result.getResultCode() == RESULT_OK) {
                        Coupon c = (Coupon) intent.getSerializableExtra("coupon");
                        if (c != null) {
                            try {
                                customerFacade.purchaseCoupon(c);
                                adapter.refreshCouponAdded(c);
                            } catch (myException ex) {
                                Toast.makeText(CustomerActivity.this,  ex.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                            catch (ParseException ex) {
                                Toast.makeText(CustomerActivity.this,  ex.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }


                }

            }

        }
    });

    class ButtonsClick implements View.OnClickListener{

        @Override
        public void onClick(View view) {
            if(view.getId() == btnBuyCoupon.getId()){
                //////////// starting activity to buy new coupon
                Intent intent = new Intent(CustomerActivity.this, BuyCouponActivity.class);
                intent.putExtra("customerid", customerFacade.getCustomerID());
                launcher.launch(intent);

            }
            else if (view.getId() == btnSearch.getId()){
               String selectedCategory = (String) spCategory.getSelectedItem();
               double maxPrice = Double.parseDouble(etMaxPrice.getText().toString());
               ArrayList<Coupon> couponsByPrice;
               ArrayList<Coupon> couponsByCategory;
               if(selectedCategory.equals("choose category")){
                  //// filtering just by price
                   try {
                       couponsByPrice = customerFacade.getCustomerCoupons(maxPrice);
                       adapter.refreshAllCoupons(couponsByPrice);

                   } catch (ParseException e) {
                       throw new RuntimeException(e);
                   }
               }
               else {
                   try {
                       couponsByCategory = customerFacade.getCustomerCoupons(Category.valueOf(selectedCategory));
                       ArrayList<Coupon> priceAndCategory= new ArrayList<>();
                       Set<Coupon> couponsCategory = new HashSet<>(couponsByCategory);
                       couponsByPrice = customerFacade.getCustomerCoupons(maxPrice);
                       for(Coupon coupon : couponsByPrice)
                           if(couponsCategory.contains(coupon))
                               priceAndCategory.add(coupon);
                       adapter.refreshAllCoupons(priceAndCategory);

                   } catch (ParseException e) {
                       throw new RuntimeException(e);
                   }

               }


            }
        }
    }
}