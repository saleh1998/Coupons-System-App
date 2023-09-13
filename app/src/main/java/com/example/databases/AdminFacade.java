package com.example.databases;

import android.content.Context;

import java.util.ArrayList;

public class AdminFacade extends ClientFacade{


    public AdminFacade(Context context) {
        super(context);
        this.context=context;
    }

    public void addCompany(Company company) throws myException {
        boolean companyExists = companiesDAO.isCompanyExists(company.getEmail(), company.getPassword());
        ArrayList<Company> compamnies = getAllCompanies();
        for (Company c :compamnies) {
            if(c.getName().equals(company.getName()))
            {
                throw new myException("Company with the same title already exists.");
            }
        }
        if (!companyExists) {
            companiesDAO.addCompany(company);
        } else {
            throw new myException("Company with the same email or password already exists.");
        }
    }
    public void updateCompany(Company company) throws myException {
        Company existingCompany = companiesDAO.getOneCompany(company.getId());

        if (existingCompany != null) {
            companiesDAO.updateCompany(company);
        } else {
           throw new myException("Company not found for the given ID.");
        }
    }
    public void deleteCompany(Company company) throws myException {
            int companyIdToDelete = company.getId();
             Company companyExists = companiesDAO.getOneCompany(company.getId());

            if (companyExists!= null) {
                try {
                    companiesDAO.deleteCompany(companyIdToDelete);
                } catch (myException e) {
                    throw new myException("Error deleting the company.");
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
    public void addCustomer(Customer customer) throws myException {
        boolean customerExists = customersDAO.isCustomerExists(customer.getEmail(), customer.getPassword());
        if (!customerExists) {
            customersDAO.addCustomer(customer);
        } else {
            throw new myException("Customer with the same email or password already exists.");
        }
    }
    public void updateCustomer(Customer customer) throws myException {
        Customer customerExists = customersDAO.getOneCustomer(customer.getId());
        if (customerExists != null) {
            customersDAO.updateCustomer(customer);
        } else {
            throw new myException("Customer not found for the given ID.");
        }
    }
    public void deleteCustomer(int customerID) throws myException {
        Customer customerExists = customersDAO.getOneCustomer(customerID);
        if (customerExists!=null) {
            try {
                customersDAO.deleteCustomer(customerID);
            } catch (myException e) {
                throw new myException("Error deleting the customer.");
            }
        } else {
            throw new myException("Customer not found for the given ID.");
        }
    }
    public ArrayList<Customer> gelAllCustomers() {
        return customersDAO.getAllCustomers();
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
