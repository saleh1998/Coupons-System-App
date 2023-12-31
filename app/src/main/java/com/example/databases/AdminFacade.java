package com.example.databases;

import android.content.Context;

import java.io.Serializable;
import java.text.ParseException;
import java.util.ArrayList;

public class AdminFacade extends  ClientFacade implements Serializable {

    public AdminFacade(Context context) {
        super(context);
        this.context = context;
    }

    public  boolean CompanyNameExists(Company c){
        ArrayList<Company> allCompanies = companiesDAO.getAllCompanies();
        for (Company itr:allCompanies) {
            if (c.getName().equals(itr.getName())){
                return true;
            }
        }
        return false;
    }
    public  boolean CompanyEmailExists(Company c){
        ArrayList<Company> allCompanies = companiesDAO.getAllCompanies();
        for (Company itr:allCompanies) {
            if (c.getEmail().equals(itr.getEmail())){
                return true;
            }
        }
        return false;
    }
    public void addCompany(Company company) throws myException {
        ArrayList<Company> allCompanies = companiesDAO.getAllCompanies();
        boolean nameExists = allCompanies.stream().anyMatch(c -> c.getName().equals(company.getName()));
        boolean emailExists = companiesDAO.isCompanyExists(company.getEmail(), company.getPassword());
        if (!nameExists && !emailExists) {
            companiesDAO.addCompany(company);
        } else {
            throw new myException("Company with the same name or email already exists.");
        }
    }

    public void updateCompany(Company company) throws myException {
        Company existingCompany = companiesDAO.getOneCompany(company.getId());

        if (existingCompany != null) {
            //existingCompany.setId(company.getId());
            existingCompany.setName(company.getName());
            existingCompany.setEmail(company.getEmail());
            existingCompany.setPassword(company.getPassword());
            companiesDAO.updateCompany(existingCompany);
        } else {
            throw new myException("Company not found for the given ID.");
        }
    }

    public void deleteCompany(Company company) throws myException {
        int companyIdToDelete = company.getId();
        Company companyExists = companiesDAO.getOneCompany(company.getId());
        if (companyExists != null) {
            try {
//                ArrayList<Coupon> companyCoupons = company.getCoupons();
//                if(companyCoupons!=null) { /// without this app will crash!
//                    for (Coupon coupon : companyCoupons)
//                        couponsDAO.deleteCoupon(coupon);
//                }
                companiesDAO.deleteCompany(companyIdToDelete);
            } catch (myException e) {
                throw new myException("Error deleting the company.");
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        } else {
            throw new myException("Company not found for the given ID.");
            }
        }


    public ArrayList<Company> getAllCompanies() {
        return companiesDAO.getAllCompanies();
    }

    public Company getOneCompany(int companyID) {
        return companiesDAO.getOneCompany(companyID);
    }

    /*public  boolean CustomerNameExists(Customer c){
        ArrayList<Customer> allCustomers = customersDAO.getAllCustomers();
        for (Customer itr:allCustomers) {
            if (c.getName().equals(itr.getName())){
                return true;
            }
        }
        return false;
    }*/
    public  boolean CustomerEmailExists(Customer c){
        ArrayList<Customer> allCustomers = customersDAO.getAllCustomers();
        for (Customer itr:allCustomers) {
            if (c.getEmail().equals(itr.getEmail())){
                return true;
            }
        }
        return false;
    }
    public void addCustomer(Customer customer) throws myException {
        boolean customerExists = customersDAO.isCustomerEmailExists(customer.getEmail());
        if (!customerExists) {
            customersDAO.addCustomer(customer);
        } else {
            throw new myException("Customer with the same email or password already exists.");
        }
    }

    public void updateCustomer(Customer customer) throws myException {
        Customer customerExists = customersDAO.getOneCustomer(customer.getId());
        if (customerExists != null) {
            customer.setId(customerExists.getId());
            customersDAO.updateCustomer(customer);
        } else {
            throw new myException("Customer not found for the given ID.");
        }
    }

    public void deleteCustomer(int customerID) throws myException {
        Customer customer = customersDAO.getOneCustomer(customerID);

        if (customer != null) {
            try {
//                ArrayList<Coupon> customerCoupons = customer.getCoupons();
//                if(customerCoupons!=null){
//                    customerCoupons.clear();
//                }
                customersDAO.deleteCustomer(customerID);
            } catch (myException e) {
                throw new myException("Error deleting the customer.");
            }
        } else {
            throw new myException("Customer not found for the given ID.");
        }
    }

    public ArrayList<Customer> getAllCustomers() {
        return customersDAO.getAllCustomers();
    }
    public Customer getOneCustomer(int customerID) {
        return customersDAO.getOneCustomer(customerID);
    }
    @Override
    public boolean login(String email, String password) {
        String adminEmail = "admin@admin.com";
        String adminPassword = "admin";
        if(email.equals(adminEmail) && password.equals(adminPassword))
            return true;
        return  false;
    }
}