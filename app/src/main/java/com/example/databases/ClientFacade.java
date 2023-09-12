package com.example.databases;

import android.content.Context;

public abstract class ClientFacade {

    Context context;
    CompaniesDAO companiesDAO= new CompaniesDBDAO(context);
    CustomersDAO customersDAO= new CustomersDBDAO(context);
    CouponsDAO couponsDAO= new CouponsDBDAO(context);

    public ClientFacade(Context context) {
        this.context = context;
    }

    public abstract boolean login(String email, String password);
}
