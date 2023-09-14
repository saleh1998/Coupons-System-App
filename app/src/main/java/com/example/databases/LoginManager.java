package com.example.databases;

import android.content.Context;

public class LoginManager {

    private static LoginManager instance = null;
    Context context;
    private LoginManager(Context context) {
        this.context = context;
    }


    //...Singleton.........................................
    public static LoginManager getInstance(Context context) {
        if (instance == null) instance = new LoginManager(context);
        return instance;
    }
    ClientFacade login(String email, String password, ClientType clientType){
        if(clientType == ClientType.Administrator){
            AdminFacade admin = new AdminFacade(context);
           if(admin.login(email,password))
               return admin;
        }
        if(clientType == ClientType.Company){
            CompanyFacade company = new CompanyFacade(0,context);
            if(company.login(email,password))
                return company;
        }
        if(clientType == ClientType.Customer){
            CustomerFacade customer = new CustomerFacade(context);
            if(customer.login(email,password))
                return customer;
        }
        return null;
    }
}
