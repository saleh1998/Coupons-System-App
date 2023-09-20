package com.example.databases;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CouponsDBDAO implements CouponsDAO, Serializable {
    Context context;

    DB_Manager mydb = DB_Manager.getInstance(context);

    public CouponsDBDAO(Context context) {
        this.context = context;
    }

    public void addCoupon(Coupon coupon)
    {
       mydb.addCoupon(coupon);

    }

    @Override
    public void updateCoupon(Coupon coupon) throws myException, ParseException {
        mydb.updateCoupon(coupon);
    }

    @Override
    public void deleteCoupon(Coupon coupon) throws ParseException, myException {
        mydb.deleteCoupon(coupon);
//        mydb.deleteCustomerVsCouponCouponForAllCustomersByCouponId(coupon.getId());
    }

    @Override
    public ArrayList<Coupon> getAllCoupons() throws ParseException {
        return mydb.getAllCoupons();
    }

    @Override
    public Coupon getOneCoupon(int CouponID) throws ParseException {
        return mydb.getOneCoupon(CouponID);
    }

    @Override
    public void addCouponPurchase(int customerID, int couponID) throws ParseException, myException {
        mydb.addCouponPurchase(customerID,couponID);
    }

    @Override
    public void deleteCouponPurchase(int customerID, int couponID) throws ParseException, myException {
        mydb.deleteCouponPurchase(customerID,couponID);
    }

    public void deleteExpiredCouponsAndPurchaseHistory() throws myException, ParseException {
        // 1. Get a list of all expired coupons.
        List<Coupon> expiredCoupons = getExpiredCoupons();

        // 2. Delete each expired coupon.
        for (Coupon coupon : expiredCoupons) {
            int compId = coupon.getCompanyID();
            Company company = mydb.getOneCompany(compId);
            company.getCoupons().remove(coupon);
            ArrayList<Integer> customersIds =  mydb.getAllCustomersForSpecificCoupon(compId);
            for(int customerId : customersIds){
                Customer customer = mydb.getOneCustomer(customerId);
                customer.getCoupons().remove(coupon);
            }
            mydb.deleteCustomerVsCouponCouponForAllCustomersByCouponId(coupon.getId());
            mydb.deleteCoupon(coupon);
        }
    }

    private List<Coupon> getExpiredCoupons() throws ParseException {
        List<Coupon> expiredCoupons = new ArrayList<>();

        Date today = new Date(); // Assuming that Coupon has a Date type for expiration date
        for (Coupon c : mydb.getAllCoupons()) {
            if (c.getEndDate().before(today)) {
                expiredCoupons.add(c);
            }
        }
        return expiredCoupons;
    }


}
