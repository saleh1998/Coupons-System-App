package com.example.databases;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class CompanyViewProfileActivity extends AppCompatActivity {
    TextView tvComID, tvComName, tvComEmail, tvComPass,passwordhint;
    ImageView backbtn;
  
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_view_profile);
        passwordhint = findViewById(R.id.showPasswordHint);
        tvComID = findViewById(R.id.fragCompViewProfile_tvComID);
        tvComName = findViewById(R.id.fragCompViewProfile_tvComName);
        tvComEmail = findViewById(R.id.fragCompViewProfile_tvComEmail);
        tvComPass = findViewById(R.id.fragCompViewProfile_tvComPassword);

        backbtn = findViewById(R.id.companyprofi_backbtn);
        Company c = (Company) getIntent().getSerializableExtra("Company");
        tvComID.setText(" "+c.getId()+"");
        tvComName.setText(" "+c.getName());
        tvComEmail.setText(" "+c.getEmail());
        tvComPass.setText(" "+c.getPassword());
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tvComPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                togglePasswordVisibility();            }
        });


    }
    private void togglePasswordVisibility() {
        int inputType = tvComPass.getInputType();

        if (inputType == (InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD)) {
            // Password is currently hidden, show it
            tvComPass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            passwordhint.setText("Press to Hide Password");
        } else {
            // Password is currently visible, hide it
            tvComPass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            passwordhint.setText("Press to Show Password");
        }

    }

    }

