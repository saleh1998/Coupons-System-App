package com.example.databases;

import android.content.Context;
import android.os.Parcelable;
import android.widget.Toast;

import java.io.Serializable;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;

public class CompanyFacade extends ClientFacade {
    private int companyID;

    public int getCompanyID() {
        return companyID;
    }

    public void setCompanyID(int companyID) {
        this.companyID = companyID;
    }
    public CompanyFacade(int companyID, Context context) {
        super(context);
        this.context = context;
        this.companyID = companyID;

    }
       // V
        public void addCoupon(Coupon coupon) throws myException, ParseException {
            ArrayList<Coupon> companyCoupons = companiesDAO.getCompanyCouponsbyID(companyID);
           if(companyCoupons!=null)
            for (Coupon existingCoupon : companyCoupons) {
                if (existingCoupon.getTitle().equals(coupon.getTitle())) {
                    //throw new myException("Coupon with the same title already exists for same company.");
/*
                    Toast.makeText(context, "Coupon with the same title already exists for same company please try again", Toast.LENGTH_SHORT).show();
*/
                    throw new myException("Coupon with the same title already exists for same company.");

                }
            }

            couponsDAO.addCoupon(coupon);
    }

    @Override
    // v
    public boolean login(String email, String password) {
            if(companiesDAO.isCompanyExists(email, password)){
                int id = companiesDAO.getCompanyId(email,password);
                setCompanyID(id);
                return true;
            }
            return false;
    }

    // v
    public void updateCoupon(Coupon coupon) throws ParseException, myException {
            Coupon existingCoupon = couponsDAO.getOneCoupon(coupon.getId());
            if (existingCoupon != null) {
                couponsDAO.updateCoupon(coupon);
            } else {
                throw new myException("Coupon not found for the given ID.");
            }
        }

        // v
        public void deleteCoupon(Coupon coupon) throws ParseException, myException {
            Coupon existingCoupon = couponsDAO.getOneCoupon(coupon.getId());
            if (existingCoupon != null) {
                couponsDAO.deleteCoupon(coupon);
                Company co = companiesDAO.getOneCompany(existingCoupon.getCompanyID());
               // co.getCoupons().remove(existingCoupon);
                companiesDAO.updateCompany(co);
            } else {
                throw new myException("Coupon not found for the given ID.");
            }
        }


        // v
        public ArrayList<Coupon> getCompanyCoupons() throws ParseException {
          // return companiesDAO.getOneCompany(companyID).getCoupons();
        return companiesDAO.getCompanyCouponsbyID(companyID);
    }


        // v
        public ArrayList<Coupon> getCompanyCoupons(Category category) throws ParseException {
            ArrayList<Coupon> companyCouponsByCategory = new ArrayList<>();
            ArrayList<Coupon> companyCoupons = getCompanyCoupons();
            for (Coupon coupon : companyCoupons) {
                if (coupon.getCategory() == category) {
                    companyCouponsByCategory.add(coupon);
                }
            }
            return companyCouponsByCategory;
        }


        // v
        public ArrayList<Coupon> getCompanyCoupons(double maxPrice) throws ParseException {
            ArrayList<Coupon> companyCoupons = getCompanyCoupons();
            ArrayList<Coupon> companyCouponsByMaxPrice = new ArrayList<>();
            for (Coupon coupon : companyCoupons) {
                if (coupon.getPrice() <= maxPrice)
                    companyCouponsByMaxPrice.add(coupon);
                }
            return companyCouponsByMaxPrice;
        }

        // V

        public Company getCompanyDetails() {
            return companiesDAO.getOneCompany(companyID);
        }
        public Company getCompanyDetails(int id) {
            return companiesDAO.getOneCompany(id);

        }


    }


