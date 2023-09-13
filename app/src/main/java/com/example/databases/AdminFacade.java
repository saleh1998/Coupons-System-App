package com.example.databases;

import java.text.ParseException;
import java.util.ArrayList;

public class AdminFacade extends  ClientFacade{
    public AdminFacade() {
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
            existingCompany.setId(company.getId());
            existingCompany.setName(company.getName());
            companiesDAO.updateCompany(existingCompany);
        } else {
            throw new myException("Company not found for the given ID.");
        }
    }
    public void deleteCompany(Company company) throws myException {
            int companyIdToDelete = company.getId();
             Company companyExists = companiesDAO.getOneCompany(company.getId());

            if (companyExists!= null) {
                try {
                    ArrayList<Coupon > companyCoupons = company.getCoupons();
                    for (Coupon coupon : companyCoupons)
                        couponsDAO.deleteCoupon(coupon);
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
                ArrayList<Coupon> customerCoupons = customer.getCoupons();
                customerCoupons.clear();
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
}
