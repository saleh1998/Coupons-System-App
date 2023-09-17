package com.example.databases;

import android.content.Context;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;

public class CustomerFacade extends ClientFacade {


    int customerID ;



    public CustomerFacade(Context context) {
        super(context);
        this.context= context;
    }

    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

    public int getCustomerID() {
        return customerID;
    }

    @Override
    public boolean login(String email, String password) {
        if(customersDAO.isCustomerExists(email, password)){
            int id = customersDAO.getCustomerId(email,password);
            setCustomerID(id);
            return true;
        }
        return false;
    }

    public void purchaseCoupon(Coupon coupon) throws myException, ParseException {
        ArrayList<Coupon> customerCpn = getCustomerCoupons();
        for(Coupon c : customerCpn)
            if(c.getId() == coupon.getId())
                throw new myException(" You already have this coupon") ;
        if (coupon.getAmount() != 0) {
            coupon.getEndDate();
            LocalDate currentDate = LocalDate.now();
            LocalDate endDate = coupon.getEndDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            LocalDate startDate = coupon.getStartDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            if (currentDate.isBefore(endDate)) {
                // adding the coupon to the customer coupon list
                try {
                    couponsDAO.addCouponPurchase(customerID, coupon.getId());
                } catch (myException | ParseException e) {
                    throw new myException("Can't add new coupon");
                }
            } else {
                throw new myException("The coupon is either expired or not yet valid.");
            }
        } else {
            throw new myException("There are no more coupons available.");
        }
    }

    public ArrayList<Coupon> getCustomerCoupons() throws ParseException {
        return(customersDAO.getCustomerCoupons(customerID));
    }

    public ArrayList<Coupon> getCustomerCoupons(Category category) throws ParseException {
        ArrayList<Coupon> customerCoupons = getCustomerCoupons();
        ArrayList<Coupon> customerCouponsByCategory = new ArrayList<>();
        for(Coupon coupon : customerCoupons)
            if(coupon.getCategory().equals(category))
                customerCouponsByCategory.add(coupon);
        return customerCouponsByCategory;
    }

    public ArrayList<Coupon> getCustomerCoupons(double maxPrice) throws ParseException {
        ArrayList<Coupon> customerCoupons = getCustomerCoupons();
        ArrayList<Coupon> customerCouponsByCategory = new ArrayList<>();
        for(Coupon coupon : customerCoupons)
            if(coupon.getPrice() <= maxPrice)
                customerCouponsByCategory.add(coupon);
        return customerCouponsByCategory;
    }

    public Customer getCustomerDetails(Company company) {
        return(customersDAO.getOneCustomer(customerID));
    }


}