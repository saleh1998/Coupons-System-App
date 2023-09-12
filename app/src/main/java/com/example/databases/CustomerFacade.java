package com.example.databases;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;

public class CustomerFacade extends ClientFacade {
    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

    int customerID ;
    public CustomerFacade()  {
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

    public void purchaseCoupon(Coupon coupon) {
        if (coupon.getAmount() != 0){
            coupon.getEndDate();
            LocalDate currentDate = LocalDate.now();
            LocalDate endDate = coupon.getEndDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            LocalDate startDate = coupon.getStartDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            if(currentDate.isAfter(startDate)&& currentDate.isBefore(endDate)){
                // adding the coupon to the customer coupon list
                try {
                    couponsDAO.addCouponPurchase(customerID,coupon.getId());
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                } catch (myException e) { // sho my exception b3mal
                    throw new RuntimeException(e);
                }

                // the quantity in stock of the coupon must be reduced by 1


            }
            else {
                // we can not use the coupon because of the date
            }

        }
        else{
            // there is no more coupons
        }

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
