package com.example.databases;

import android.content.Context;
import android.net.ParseException;

import java.io.Serializable;


import java.util.ArrayList;

public class CompaniesDBDAO implements CompaniesDAO, Serializable {

    Context context;

    DB_Manager mydb = DB_Manager.getInstance(context);

    public CompaniesDBDAO(Context context) {
        this.context = context;
    }

    @Override
    public boolean isCompanyExists(String email, String password) {
        return mydb.isCompanyExists(email,password);
    }

    public int getCompanyId(String email, String password) {
        return mydb.getCompanyId(email,password);
    }

    @Override
    public void addCompany(Company company) throws myException {
    mydb.addCompany(company);

    }

    @Override
    public void updateCompany(Company company) throws myException {
    mydb.updateCompany(company);
    }

    @Override
    public void deleteCompany(int companyID) throws myException, ParseException, java.text.ParseException {
    mydb.deleteCompany(companyID);
    }

    @Override
    public ArrayList<Company> getAllCompanies() {
        return mydb.getAllCompanies();
    }

    @Override
    public Company getOneCompany(int CompanyID) {
        return mydb.getOneCompany(CompanyID);
    }

    public ArrayList<Coupon> getCompanyCouponsbyID(int companyID) throws ParseException, java.text.ParseException {
        return mydb.getCompanyCouponsbyID(companyID);

    }

}
