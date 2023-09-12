package com.example.databases;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;

import android.database.sqlite.SQLiteDatabase;
import android.database.Cursor;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class DB_Manager extends SQLiteOpenHelper  {

    private final static String DB_NAME = "DB_1";
    private final static int DB_VER = 1;
    private Context context;
    ArrayList<Company> companies;
    ArrayList<Customer> customers;
    ArrayList<Coupon> coupons;

    private final static String TBL_COMPANIES = "companies";
    final static String COMPANY_ID = "id";
    private final static String COMPANY_NAME = "name";
    private final static String COMPANY_EMAIL = "email";
    private final static String COMPANY_PASSWORD = "password";



    private final static String CREATE_TABLE_COMPANIES =
            "CREATE TABLE IF NOT EXISTS " + TBL_COMPANIES +
                    " (" + COMPANY_ID + " text primary key, " +
                    COMPANY_NAME + " text, "+
                    COMPANY_EMAIL + " text, " +
                    COMPANY_PASSWORD + " text)";


    //...Singleton.........................................

    private static DB_Manager instance = null;

    private DB_Manager(Context context) {
        super(context, DB_NAME, null, DB_VER);
        try {
            this.context = context;
        } catch (Exception e) {
            throw e;
        }
    }


    public static DB_Manager getInstance(Context context) {
        if (instance == null) instance = new DB_Manager(context);
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_COMPANIES);
        db.execSQL(CREATE_TABLE_CUSTOMERS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TBL_COMPANIES);
        db.execSQL("DROP TABLE IF EXISTS " + TBL_CUSTOMERS);

        onCreate(db);
    }
//...Singleton.........................................

    private Cursor getCursor(String tableName, String[] fields, String where) {
        String strQry = "SELECT ";
        for (int i = 0; i < fields.length; i++) {
            strQry += fields[i] + " ";
            if (i < fields.length - 1)
                strQry += ",";
        }
        strQry += " FROM " + tableName;
        if (where != null && !where.isEmpty())
            strQry += " " + where;

        try {
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cr = db.rawQuery(strQry, null);
            return cr;
        } catch (Exception e) {
            throw e;
        }
    }
    public SQLiteDatabase getWritableDB() {
        return getWritableDatabase();
    }













    //_______________________Companies _________________________

    public boolean isCompanyExists(String email, String password)
    {
        if(getCompanyId(email,password)!= -1 )
            return true;
        return false;

    }

    public int getCompanyId(String email, String password)
    {
        companies = getAllCompanies();
        for (Company c : companies) {
            if (c.getEmail().equals(email))
                if(c.getPassword().equals(password))
                    return c.getId();
        }
        return -1;
    }

    public void addCompany(Company company) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COMPANY_ID, company.getId());
        values.put(COMPANY_NAME, company.getName());
        values.put(COMPANY_EMAIL, company.getEmail());
        values.put(COMPANY_PASSWORD, company.getPassword());

        db.insert(TBL_COMPANIES, null, values);
        db.close();
    }


    public void updateCompany(Company company) throws myException {
        companies = getAllCompanies();
        boolean flag =false;
            for (Company c : companies) {
                if (c.getId()== (company.getId())) {
                    c.setName(company.getName());
                    c.setEmail(company.getEmail());
                    c.setPassword(company.getPassword());
                    flag = true;
                }
            }
            if(flag)
            {
            ContentValues cv = new ContentValues();
            cv.put(COMPANY_NAME, company.getName());
            cv.put(COMPANY_EMAIL, company.getEmail());
            cv.put(COMPANY_PASSWORD, company.getPassword());

            SQLiteDatabase db = getWritableDatabase();
            db.update(TBL_COMPANIES, cv, COMPANY_ID + "=" + company.getId(), null);
        } else
            throw new myException("User not exists!");

    }

    public void deleteCompany(int companyID) throws myException {
        Company tobeDeleted = null;
      boolean flag =false;
            for (Company c : companies) {
                if (c.getId() == (companyID) ){
                    tobeDeleted = c;
                    flag = true;
                    break;
                }
            }
            if(flag) {
            companies.remove(tobeDeleted); // deleting it from the arraylist because all object in the list are refrences
            SQLiteDatabase db = getWritableDatabase();
            db.delete(TBL_COMPANIES, COMPANY_ID + "= ?", new String[]{(Integer.toString(companyID))});

        }
            else throw new myException("Delete failed No company found with id ="+ companyID +"!");

    }

    public ArrayList<Company> getAllCompanies() {
        ArrayList<Company> companies = new ArrayList<>();
        String[] fields = {COMPANY_ID, COMPANY_NAME, COMPANY_EMAIL,COMPANY_PASSWORD};
        String id ,name,email,password;
        try {
            Cursor cr = getCursor(TBL_COMPANIES, fields, null);
            if (cr.moveToFirst())
                do {
                    id = cr.getString(0);
                    name = cr.getString(1);
                    email = cr.getString(2);
                    password = cr.getString(3);
                    companies.add(new Company(Integer.parseInt(id),name,email,password));
                } while (cr.moveToNext());
            return companies;
        } catch (Exception e) {
            throw e;
        }
    }


    public Company getOneCompany(int CompanyID) {
        companies = getAllCompanies();
        for (Company c : companies) {
            if (c.getId() == CompanyID)
                    return c;
        }
        return null;
    }






















    //___________________________________Customers___________________________________





    private final static String TBL_CUSTOMERS = "customers";
    final static String CUSTOMER_ID = "id";
    private final static String CUSTOMER_FNAME = "firstName";
    private final static String CUSTOMER_LNAME = "lastName";
    private final static String CUSTOMER_EMAIL = "email";
    private final static String CUSTOMER_PASSWORD = "password";



    private final static String CREATE_TABLE_CUSTOMERS =
            "CREATE TABLE IF NOT EXISTS " + TBL_CUSTOMERS +
                    " (" + CUSTOMER_ID + " text primary key, " +
                    CUSTOMER_FNAME + " text, "+
                    CUSTOMER_LNAME + " text, "+
                    CUSTOMER_EMAIL + " text, " +
                    CUSTOMER_PASSWORD + " text)";








    public boolean isCustomerExists(String email, String password) {
        if(getCustomerId(email, password) != -1)
            return true;
        return false;
    }

    public int getCustomerId(String email, String password) {
        customers = getAllCustomers();
        for (Customer c : customers) {
            if (c.getEmail().equals(email))
                if(c.getPassword().equals(password))
                    return c.getId();
        }
        return -1;    }


    public void addCustomer(Customer customer) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(CUSTOMER_ID, customer.getId());
        values.put(CUSTOMER_FNAME, customer.getFirstName());
        values.put(CUSTOMER_LNAME, customer.getLastName());
        values.put(CUSTOMER_EMAIL, customer.getEmail());
        values.put(COMPANY_PASSWORD, customer.getPassword());

        db.insert(TBL_CUSTOMERS, null, values);
        db.close();
    }


    public void updateCustomer(Customer customer) throws myException {
        customers = getAllCustomers();
        boolean flag =false;
        for (Customer c : customers) {
            if (c.getId()== (customer.getId())) {
                c.setLastName(customer.getFirstName());
                c.setFirstName(customer.getLastName());
                c.setEmail(customer.getEmail());
                c.setPassword(customer.getPassword());
                flag = true;
            }
        }
        if(flag)
        {ContentValues cv = new ContentValues();
            cv.put(CUSTOMER_FNAME, customer.getFirstName());
            cv.put(CUSTOMER_LNAME, customer.getLastName());
            cv.put(COMPANY_EMAIL, customer.getEmail());
            cv.put(COMPANY_PASSWORD, customer.getPassword());

            SQLiteDatabase db = getWritableDatabase();
            db.update(TBL_CUSTOMERS, cv, CUSTOMER_ID + "=" + customer.getId(), null);
        } else
            throw new myException("User not exists!");

    }


    public void deleteCustomer(int customerID) throws myException {
        Customer tobeDeleted = null;
        boolean flag =false;
        for (Customer c : customers) {
            if (c.getId() == (customerID) ){
                tobeDeleted = c;
                flag = true;
                break;
            }
        }
        if(flag) {
            customers.remove(tobeDeleted); // deleting it from the arraylist because all object in the list are refrences
            SQLiteDatabase db = getWritableDatabase();
            db.delete(TBL_CUSTOMERS, CUSTOMER_ID + "= ?", new String[]{(Integer.toString(customerID))});

        }
        else throw new myException("Delete failed No customer found with id ="+ customerID +"!");
    }


    public ArrayList<Customer> getAllCustomers() {
        ArrayList<Customer> customers = new ArrayList<>();
        String[] fields = {CUSTOMER_ID, CUSTOMER_FNAME, CUSTOMER_LNAME,CUSTOMER_EMAIL,CUSTOMER_PASSWORD};
        String id ,fname,lname,email,password;
        try {
            Cursor cr = getCursor(TBL_CUSTOMERS, fields, null);
            if (cr.moveToFirst())
                do {
                    id = cr.getString(0);
                    fname = cr.getString(1);
                    lname = cr.getString(2);
                    email = cr.getString(3);
                    password = cr.getString(4);
                    customers.add(new Customer(Integer.parseInt(id),fname,lname,email,password));
                } while (cr.moveToNext());
            return customers;
        } catch (Exception e) {
            throw e;
        }
    }



    public Customer getOneCustomer(int CustomerID)
    {
        customers = getAllCustomers();
        for (Customer c : customers)
        {
            if (c.getId() == CustomerID)
                return c;
        }
        return null;
    }
    //+_________________________________________Coupons_____________________________________________________



    private final static String TBL_COUPONS = "coupons";
    final static String COUPON_ID = "id";
    private final static String COUPON_COMPANY_ID= "companyID";
    private final static String COUPONS_CATEGORY = "category";
    private final static String COUPONS_TITLE = "title";
    private final static String COUPONS_DESC = "description";
    private final static String COUPONS_START_DATE = "startDate";
    private final static String COUPONS_END_DATE = "endDate";
    private final static String COUPONS_AMOUNT = "amount";
    private final static String COUPONS_PRICE = "price";
    private final static String COUPONS_IMAGE = "image";



    private final static String CREATE_TABLE_COUPONS =
            "CREATE TABLE IF NOT EXISTS " + COUPON_ID +
                    " (" + COUPON_COMPANY_ID + " text primary key, " +
                    COUPONS_CATEGORY + " text, "+
                    COUPONS_TITLE + " text, "+
                    COUPONS_DESC + " text, "+
                    COUPONS_START_DATE + " text, " +
                    COUPONS_END_DATE + " text, " +
                    COUPONS_AMOUNT + " text, " +
                    COUPONS_PRICE + " text, " +
                    COUPONS_IMAGE + " text)";



   public void addCoupon(Coupon coupon)
    {
Date startDate = coupon.getStartDate();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String start = dateFormat.format(startDate);

        Date endDate = coupon.getEndDate();
        String end = dateFormat.format(endDate);

        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COUPON_ID, coupon.getId());
        values.put(COUPON_COMPANY_ID, coupon.getCompanyID());
        values.put(COUPONS_CATEGORY, coupon.getCategory().name());
        values.put(COUPONS_TITLE, coupon.getTitle());
        values.put(COUPONS_DESC, coupon.getDescription());
        values.put(COUPONS_START_DATE, start);
        values.put(COUPONS_END_DATE, end);
        values.put(COUPONS_AMOUNT, coupon.getAmount());
        values.put(COUPONS_PRICE, coupon.getPrice());
        values.put(COUPONS_IMAGE, coupon.getImage());

        db.insert(TBL_COUPONS, null, values);
        db.close();


    }


    public void updateCoupon(Coupon coupon) throws myException, ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        coupons = getAllCoupons();
        boolean flag =false;
        for (Coupon c : coupons) {
            if (c.getId()== (coupon.getId())) {
                c.setCompanyID(coupon.getCompanyID());
                c.setCategory(coupon.getCategory());
                c.setTitle(coupon.getTitle());
                c.setDescription(coupon.getDescription());
                c.setStartDate(coupon.getStartDate());
                c.setEndDate(coupon.getEndDate());
                c.setAmount(coupon.getAmount());
                c.setPrice(coupon.getPrice());
                c.setImage(coupon.getImage());
                flag = true;

                break;
            }
        }
        if(flag)
        {ContentValues values = new ContentValues();
            values.put(COUPON_ID, coupon.getId());
            values.put(COUPON_COMPANY_ID, coupon.getCompanyID());
            values.put(COUPONS_CATEGORY, coupon.getCategory().name());
            values.put(COUPONS_TITLE, coupon.getTitle());
            values.put(COUPONS_DESC, coupon.getDescription());
            values.put(COUPONS_START_DATE, dateFormat.format(coupon.getStartDate()));
            values.put(COUPONS_END_DATE, dateFormat.format(coupon.getEndDate()));
            values.put(COUPONS_AMOUNT, coupon.getAmount());
            values.put(COUPONS_PRICE, coupon.getPrice());
            values.put(COUPONS_IMAGE, coupon.getImage());

            SQLiteDatabase db = getWritableDatabase();
            db.update(TBL_COUPONS, values, COUPON_ID + "=" + coupon.getId(), null);
        } else
            throw new myException("coupon does not exists in db!");

    }
    public void deleteCoupon(Coupon coupon) throws myException {

        Coupon tobeDeleted = null;
        boolean flag =false;
        for (Coupon c : coupons) {
            if (c.getId() == (coupon.getId()) ){
                tobeDeleted = c;
                flag = true;
                break;
            }
        }
        if(flag) {
            coupons.remove(tobeDeleted); // deleting it from the arraylist because all object in the list are refrences
            SQLiteDatabase db = getWritableDatabase();
            db.delete(TBL_COUPONS, COUPON_ID + "= ?", new String[]{(Integer.toString(coupon.getId()))});

        }
        else throw new myException("Delete failed No coupon found with id ="+ coupon.getId() +"!");


    }
    public  ArrayList<Coupon> getAllCoupons() throws ParseException {
        ArrayList<Coupon> coupons = new ArrayList<>();
        String[] fields = {COUPON_ID, COUPON_COMPANY_ID, COUPONS_CATEGORY,COUPONS_TITLE,COUPONS_DESC,COUPONS_START_DATE,COUPONS_END_DATE,COUPONS_AMOUNT,COUPONS_PRICE,COUPONS_IMAGE};
        String id,compID,category,title,desc,start,end,amount,price,image;
        try {
            Cursor cr = getCursor(TBL_COUPONS, fields, null);
            if (cr.moveToFirst())
                do {
                    id = cr.getString(0);
                    compID  = cr.getString(1);
                    category = cr.getString(2);
                    title = cr.getString(3);
                    desc = cr.getString(4);
                    start   = cr.getString(5);
                    end= cr.getString(6);
                    amount = cr.getString(7);
                    price = cr.getString(8);
                    image  = cr.getString(9);
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    coupons.add(new Coupon(Integer.parseInt(id),Integer.parseInt(compID),Category.valueOf(category),title,desc,dateFormat.parse(start),dateFormat.parse(end),Integer.parseInt(amount),Double.parseDouble(price),image));
                } while (cr.moveToNext());
            return coupons;
        } catch (Exception e) {
            throw e;
        }


    }
    public Coupon getOneCoupon(int CouponID) throws ParseException {
        coupons = getAllCoupons();
        for (Coupon c : coupons)
        {
            if (c.getId() == CouponID)
                return c;
        }
        return null;
    }
    public  void addCouponPurchase(int customerID, int couponID) throws ParseException, myException {
        Coupon coupon = getOneCoupon(couponID);
        Customer customer = getOneCustomer(customerID);

        if (coupon != null && customer != null) {
            if (coupon.getAmount() > 0) {
                coupon.setAmount(coupon.getAmount() - 1);

                customer.getCoupons().add(coupon);

                try {
                    updateCoupon(coupon);
                    updateCustomer(customer);
                } catch (myException e) {
                    throw new myException(" database error during update");
                }
            } else {
               throw new myException("no coupons left ");
            }
        }
    }


    public  void deleteCouponPurchase(int customerID, int couponID) throws myException, ParseException {
        Customer customer = getOneCustomer(customerID);
        Coupon coupon = getOneCoupon(couponID);

        if (customer != null && coupon != null) {
            if (customer.getCoupons().contains(coupon)) {
                customer.getCoupons().remove(coupon);

                try {
                    updateCustomer(customer);
                } catch (myException e) {
                    throw new myException(" database error during update");
                }
            } else {
                throw new myException("customer did not purchase that account");

            }
        } else {
            throw new myException("customer or company not found");

        }
    }





}
