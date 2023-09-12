package com.example.databases;

import android.content.Context;

public class LoginManager {

    private static LoginManager instance = null;

    private LoginManager() {

    }


    //...Singleton.........................................
    public static LoginManager getInstance(Context context) {
        if (instance == null) instance = new LoginManager();
        return instance;
    }

    ClientFacade login(String email, String password, ClientType clientType){
        if(clientType == ClientType.Administrator){
            AdminFacade admin = new AdminFacade();
           if(admin.login(email,password))
               return admin;

        }
        if(clientType == ClientType.Company){
            CompanyFacade company = new CompanyFacade(0);
            if(company.login(email,password))
                return company;

        }
        if(clientType == ClientType.Customer){
            CustomerFacade customer = new CustomerFacade();
            if(customer.login(email,password))
                return customer;

        }
        return null;

    }
}
