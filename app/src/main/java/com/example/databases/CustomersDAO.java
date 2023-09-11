package com.example.databases;

import java.util.ArrayList;

public interface CustomersDAO {
    boolean isCustomerExists(String email,String password);
    void addCustomer(Customer customer);
    void updateCustomer(Customer customer) throws myException;
    void deleteCustomer(int customerID) throws myException;
    ArrayList<Customer> getAllCustomers();
    Customer getOneCustomer(int CustomerID);
}
