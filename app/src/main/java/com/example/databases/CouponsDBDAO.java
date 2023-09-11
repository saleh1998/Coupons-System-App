package com.example.databases;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class CouponsDBDAO implements CouponsDAO {
    Context context;

    DB_Manager mydb = DB_Manager.getInstance(context);



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


}
