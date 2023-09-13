package com.example.databases;

import java.text.ParseException;
import java.util.ArrayList;

public interface CouponsDAO {
    void addCoupon(Coupon coupon);
    void updateCoupon(Coupon coupon) throws myException, ParseException;
    void deleteCoupon(Coupon coupon) throws ParseException, myException;
    ArrayList<Coupon> getAllCoupons() throws ParseException;
    Coupon getOneCoupon(int CouponID) throws ParseException;
    void addCouponPurchase(int customerID,int couponID) throws ParseException, myException;
    void deleteCouponPurchase(int customerID,int couponID) throws ParseException, myException;

    void deleteExpiredCouponsAndPurchaseHistory() throws myException, ParseException;
}
