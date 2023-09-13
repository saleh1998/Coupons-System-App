package com.example.databases;

import java.util.ArrayList;

public class CustomerFacade extends ClientFacade {
    private int customerID;

    public CustomerFacade(int customerID) {
        this.customerID = customerID;
    }

    public void purchaseCoupon(Coupon coupon) {

    }
    public ArrayList<Coupon> getCustomerCoupons() {

    }
    public ArrayList<Coupon> getCustomerCoupons(Category category) {

    }
    public ArrayList<Coupon> getCustomerCoupons(double maxPrice) {

    }
    public void getCompanyDetails(Company company) {

    }


}
