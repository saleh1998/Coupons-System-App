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


    public boolean login(String email, String password){
//        String adminEmail = "admin@admin.com";
//        String adminPassword = "admin";
//        if (email.equals(adminEmail)&&password.equals(adminPassword)
//                ||(customersDAO.isCustomerExists(email, password))
//                ||companiesDAO.isCompanyExists(email,password))
//        return true;
//        else return false;
        return false;
    }
}
