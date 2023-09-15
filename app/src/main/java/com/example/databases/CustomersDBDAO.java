package com.example.databases;

import android.content.Context;

import java.io.Serializable;
import java.util.ArrayList;

public class CustomersDBDAO implements CustomersDAO, Serializable {
    Context context;

    DB_Manager mydb = DB_Manager.getInstance(context);

    public CustomersDBDAO(Context context) {
        this.context = context;
    }

    @Override
    public boolean isCustomerExists(String email, String password) {
        return mydb.isCustomerExists(email, password);
    }

    public int getCustomerId(String email, String password) {
        return mydb.getCustomerId(email, password);
    }

    @Override
    public void addCustomer(Customer customer) {
        mydb.addCustomer(customer);
    }

    @Override
    public void updateCustomer(Customer customer) throws myException {
        mydb.updateCustomer(customer);
    }

    @Override
    public void deleteCustomer(int customerID) throws myException {
        mydb.deleteCustomer(customerID);
    }

    @Override
    public ArrayList<Customer> getAllCustomers() {
        return mydb.getAllCustomers();
    }

    @Override
    public Customer getOneCustomer(int CustomerID) {
        return mydb.getOneCustomer(CustomerID);
    }


    public boolean isCustomerEmailExists(String email) {
        if(mydb.customers.contains(email)) return true;
        return false;
    }



}