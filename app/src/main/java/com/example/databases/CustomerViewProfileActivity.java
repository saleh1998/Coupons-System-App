package com.example.databases;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public class CustomerViewProfileActivity extends AppCompatActivity {
    TextView tvCustID, tvCustName, tvCustEmail, tvCustPass;
    ImageButton imBack;

    int customerId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_view_profile);

        tvCustID = findViewById(R.id.fragCustViewProfile_tvCustID);
        tvCustName = findViewById(R.id.fragCustViewProfile_tvCustName);
        tvCustEmail = findViewById(R.id.fragCustViewProfile_tvCustEmail);
        tvCustPass = findViewById(R.id.fragCustViewProfile_tvCustPassword);
        imBack = findViewById(R.id.fragCustViewProfile_btnBack);

        customerId = getIntent().getIntExtra("customerid",0);
        CustomerFacade customerFacade = new CustomerFacade(this);
        customerFacade.setCustomerID(customerId);
        Customer customer = customerFacade.getCustomerDetails();

        tvCustID.setText("ID: "+customer.getId());
        tvCustName.setText("Name: " + customer.getFirstName()+" " + customer.getLastName());
        tvCustEmail.setText("Email: " + customer.getEmail());
        tvCustPass.setText("password: " + customer.getPassword());

        imBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}