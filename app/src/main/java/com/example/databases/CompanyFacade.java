package com.example.databases;

import java.text.ParseException;
import java.util.ArrayList;

public class CompanyFacade extends ClientFacade {
    private int companyID;

    public void setCompanyID(int companyID) {
        this.companyID = companyID;
    }

    public CompanyFacade(int companyID) {
        this.companyID = companyID;
        }
        public void addCoupon(Coupon coupon) throws myException, ParseException {
            ArrayList<Coupon> companyCoupons = couponsDAO.getAllCoupons();
            for (Coupon existingCoupon : companyCoupons) {
                if (existingCoupon.getTitle().equals(coupon.getTitle())) {
                    throw new myException("Coupon with the same title already exists for the company.");
                }
            }
            couponsDAO.addCoupon(coupon);
    }

    @Override
    public boolean login(String email, String password) {
            if(companiesDAO.isCompanyExists(email, password)){
                int id = companiesDAO.getCompanyId(email,password);
                setCompanyID(id);
                return true;
            }
            return false;

    }

    public void updateCoupon(Coupon coupon) throws ParseException, myException {
            Coupon existingCoupon = couponsDAO.getOneCoupon(coupon.getId());
            if (existingCoupon != null) {
                couponsDAO.updateCoupon(coupon);
            } else {
                throw new myException("Coupon not found for the given ID.");
            }
        }
        public void deleteCoupon(Coupon coupon) throws ParseException, myException {
            Coupon existingCoupon = couponsDAO.getOneCoupon(coupon.getId());
            if (existingCoupon != null) {
                couponsDAO.deleteCoupon(coupon);
            } else {
                throw new myException("Coupon not found for the given ID.");
            }
        }
        public ArrayList<Coupon> getCompanyCoupons() throws ParseException {
            return couponsDAO.getAllCoupons();
        }
        public ArrayList<Coupon> getCompanyCoupons(Category category) throws ParseException {
            ArrayList<Coupon> companyCouponsByCategory = new ArrayList<>();
            ArrayList<Coupon> companyCoupons = couponsDAO.getAllCoupons();
            for (Coupon coupon : companyCoupons) {
                if (coupon.getCategory() == category) {
                    companyCouponsByCategory.add(coupon);
                }
            }
            return companyCouponsByCategory;
        }
        public ArrayList<Coupon> getCompanyCoupons(double maxPrice) throws ParseException {
            ArrayList<Coupon> companyCoupons = couponsDAO.getAllCoupons();
            ArrayList<Coupon> companyCouponsByMaxPrice = new ArrayList<>();
            for (Coupon coupon : companyCoupons) {
                if (coupon.getPrice() <= maxPrice) {
                    companyCouponsByMaxPrice.add(coupon);
                }}
            return companyCouponsByMaxPrice;
        }
        public Company getCompanyDetails(Company company) {
            return companiesDAO.getOneCompany(company.getId());
        }


    }


