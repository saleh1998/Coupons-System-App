package com.example.databases;

public abstract class ClientFacade {
    CompaniesDAO companiesDAO= new CompaniesDBDAO();
    CustomersDAO customersDAO= new CustomersDBDAO();
    CouponsDAO couponsDAO= new CouponsDBDAO();
    public boolean login(String email,String password){
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
