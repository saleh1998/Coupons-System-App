package com.example.databases;

import java.util.ArrayList;

public interface CompaniesDAO {
     boolean isCompanyExists(String email,String password);
     void addCompany(Company company) throws myException;
    void updateCompany(Company company) throws myException;
    void deleteCompany(int companyID) throws myException;
    ArrayList<Company> getAllCompanies();
    Company getOneCompany(int CompanyID);
}
