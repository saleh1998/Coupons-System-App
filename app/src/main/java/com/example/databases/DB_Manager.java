package com.example.databases;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;

import android.database.sqlite.SQLiteDatabase;
import android.database.Cursor;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DB_Manager extends SQLiteOpenHelper implements Serializable {

    private final static String DB_NAME = "DB_1";
    private final static int DB_VER = 2;
    private Context context;
    ArrayList<Company> companies;
    ArrayList<Customer> customers;
    ArrayList<Coupon> coupons;
    ArrayList<Category> categories;


    private final static String TBL_COMPANIES = "companies";
    private final static String COMPANY_ID = "id";
    private final static String COMPANY_NAME = "name";
    private final static String COMPANY_EMAIL = "email";
    private final static String COMPANY_PASSWORD = "password";



    private final static String CREATE_TABLE_COMPANIES =
            "CREATE TABLE IF NOT EXISTS " + TBL_COMPANIES +
                    " (" + COMPANY_ID + " integer primary key autoincrement, " +
                    COMPANY_NAME + " text, " +
                    COMPANY_EMAIL + " text, " +
                    COMPANY_PASSWORD + " text)";

    private final static String TBL_CUSTOMERS = "customers";
    private final static String CUSTOMER_ID = "id";
    private final static String CUSTOMER_FNAME = "firstName";
    private final static String CUSTOMER_LNAME = "lastName";
    private final static String CUSTOMER_EMAIL = "email";
    private final static String CUSTOMER_PASSWORD = "password";


    private final static String CREATE_TABLE_CUSTOMERS =
            "CREATE TABLE IF NOT EXISTS " + TBL_CUSTOMERS +
                    " (" + CUSTOMER_ID + " integer primary key autoincrement, " +
                    CUSTOMER_FNAME + " text, " +
                    CUSTOMER_LNAME + " text, " +
                    CUSTOMER_EMAIL + " text, " +
                    CUSTOMER_PASSWORD + " text)";


    private final static String TBL_COUPONS = "coupons";
    final static String COUPON_ID = "id";
    private static final String  COUPON_COMPANY_ID = "companyID";
    private static final String  COUPONS_CATEGORY = "category";
    private final static String COUPONS_TITLE = "title";
    private final static String COUPONS_DESC = "description";
    private final static String COUPONS_START_DATE = "startDate";
    private final static String COUPONS_END_DATE = "endDate";
    private final static String COUPONS_AMOUNT = "amount";
    private final static String COUPONS_PRICE = "price";
    private final static String COUPONS_IMAGE = "image";


    private final static String CREATE_TABLE_COUPONS =
            "CREATE TABLE IF NOT EXISTS " + TBL_COUPONS +
                    " (" + COUPON_ID + " integer primary key autoincrement," +
                    COUPON_COMPANY_ID + " integer, " +
                    COUPONS_CATEGORY + " integer, " +
                    COUPONS_TITLE + " text, " +
                    COUPONS_DESC + " text, " +
                    COUPONS_START_DATE + " date, " +
                    COUPONS_END_DATE + " date, " +
                    COUPONS_AMOUNT + " integer, " +
                    COUPONS_PRICE + " double, " +
                    COUPONS_IMAGE + " text)";

    //...Singleton.........................................

    private static DB_Manager instance = null;

    private DB_Manager(Context context) {
        super(context, DB_NAME, null, DB_VER);
        try {
            this.context = context;
            companies = new ArrayList<>();
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
        db.execSQL(CREATE_TABLE_COUPONS);
        db.execSQL("DROP TABLE IF EXISTS " + TBL_CATEGORIES);
        db.execSQL(CREATE_TABLE_CATEGORIES);
        db.execSQL(CREATE_TABLE_CUSTOMERS_VS_COUPONS);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TBL_COMPANIES);
        db.execSQL("DROP TABLE IF EXISTS " + TBL_CUSTOMERS);
        db.execSQL("DROP TABLE IF EXISTS " + TBL_COUPONS);

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
    public int getCompanyId(String email, String password) {
        companies = getAllCompanies();
        for (Company c : companies) {
            if (c.getEmail().equals(email))
                if (c.getPassword().equals(password))
                    return c.getId();
        }
        return -1;
    }

    public void addCompany(Company company)
    {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        //values.put(COMPANY_ID, company.getId());
        values.put(COMPANY_NAME, company.getName());
        values.put(COMPANY_EMAIL, company.getEmail());
        values.put(COMPANY_PASSWORD, company.getPassword());

        long newRowId = db.insert(TBL_COMPANIES, null, values);
        companies.add(company);
        db.close();
        if(newRowId != -1){
            company.setId((int)newRowId);
            companies.add(company);
        }
    }

    public void updateCompany(Company company) throws myException {
        companies = getAllCompanies();
        boolean flag = false;
        for (Company c : companies) {
            if (c.getId() == (company.getId())) {
                c.setEmail(company.getEmail());
                c.setPassword(company.getPassword());
                c.setCoupons(company.getCoupons());
                flag = true;
            }
        }
        if (flag) {
            ContentValues cv = new ContentValues();
            cv.put(COMPANY_NAME, company.getName());
            cv.put(COMPANY_EMAIL, company.getEmail());
            cv.put(COMPANY_PASSWORD, company.getPassword());

            SQLiteDatabase db = getWritableDatabase();
            db.update(TBL_COMPANIES, cv, COMPANY_ID + "=" + company.getId(), null);
        } else
            throw new myException("Company not exists!");

    }

    public void deleteCompany(int companyID) throws myException, ParseException {
        Company tobeDeleted = null;
        boolean flag = false;
        for (Company c : companies) {
            if (c.getId() == (companyID)) {
                tobeDeleted = c;
                flag = true;
                break;
            }
        }
        if (flag) {
            for(Coupon c : tobeDeleted.getCoupons())
            {
                deleteCoupon(c);
            }
            companies.remove(tobeDeleted); // deleting it from the arraylist because all object in the list are references
            SQLiteDatabase db = getWritableDatabase();
            db.delete(TBL_COMPANIES, COMPANY_ID + "= ?", new String[]{(Integer.toString(companyID))});

        } else throw new myException("Delete failed No company found with id =" + companyID + "!");
    }

    public ArrayList<Company> getAllCompanies() {
        ArrayList<Company> companies1 = new ArrayList<>();
        String[] fields = {COMPANY_ID, COMPANY_NAME, COMPANY_EMAIL, COMPANY_PASSWORD};
        String id, name, email, password;
        try {
            Cursor cr = getCursor(TBL_COMPANIES, fields, null);
            if (cr.moveToFirst())
                do {
                    id = cr.getString(0);
                    name = cr.getString(1);
                    email = cr.getString(2);
                    password = cr.getString(3);
                    companies1.add(new Company(Integer.parseInt(id),name, email, password,null));
                } while (cr.moveToNext());
            return companies1;
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
        values.put(CUSTOMER_PASSWORD, customer.getPassword());

        db.insert(TBL_CUSTOMERS, null, values);
        db.close();
        customers.add(customer);
    }


    public void updateCustomer(Customer customer) throws myException {
        customers = getAllCustomers();
        boolean flag = false;
        for (Customer c : customers) {
            if (c.getId() == (customer.getId())) {
                c.setLastName(customer.getFirstName());
                c.setFirstName(customer.getLastName());
                c.setEmail(customer.getEmail());
                c.setPassword(customer.getPassword());
                flag = true;
            }
        }
        if (flag) {
            ContentValues cv = new ContentValues();
            cv.put(CUSTOMER_FNAME, customer.getFirstName());
            cv.put(CUSTOMER_LNAME, customer.getLastName());
            cv.put(CUSTOMER_EMAIL, customer.getEmail());
            cv.put(CUSTOMER_PASSWORD, customer.getPassword());

            SQLiteDatabase db = getWritableDatabase();
            db.update(TBL_CUSTOMERS, cv, CUSTOMER_ID + "=" + customer.getId(), null);
        } else
            throw new myException("User not exists!");

    }


    public void deleteCustomer(int customerID) throws myException {
        Customer tobeDeleted = null;
        boolean flag = false;
        for (Customer c : customers) {
            if (c.getId() == (customerID)) {
                tobeDeleted = c;
                flag = true;
                break;
            }
        }
        if (flag) {
            customers.remove(tobeDeleted); // deleting it from the arraylist because all object in the list are references
            SQLiteDatabase db = getWritableDatabase();
            db.delete(TBL_CUSTOMERS, CUSTOMER_ID + "= ?", new String[]{(Integer.toString(customerID))});
            deleteCustomerVsCouponCouponForAllCouponsByCustomerId(customerID);
        } else
            throw new myException("Delete failed No customer found with id =" + customerID + "!");
    }


    public ArrayList<Customer> getAllCustomers() {
        ArrayList<Customer> customers = new ArrayList<>();
        String[] fields = {CUSTOMER_ID, CUSTOMER_FNAME, CUSTOMER_LNAME, CUSTOMER_EMAIL, CUSTOMER_PASSWORD};
        String id, fname, lname, email, password;
        try {
            Cursor cr = getCursor(TBL_CUSTOMERS, fields, null);
            if (cr.moveToFirst())
                do {
                    id = cr.getString(0);
                    fname = cr.getString(1);
                    lname = cr.getString(2);
                    email = cr.getString(3);
                    password = cr.getString(4);
                    customers.add(new Customer(fname, lname, email, password));
                } while (cr.moveToNext());
            return customers;
        } catch (Exception e) {
            throw e;
        }
    }


    public Customer getOneCustomer(int CustomerID) {
        customers = getAllCustomers();
        for (Customer c : customers) {
            if (c.getId() == CustomerID)
                return c;
        }
        return null;
    }
    //+_________________________________________Coupons_____________________________________________________




    public void addCoupon(Coupon coupon) {
        Date startDate = coupon.getStartDate();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String start = dateFormat.format(startDate);

        Date endDate = coupon.getEndDate();
        String end = dateFormat.format(endDate);

        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        //values.put(COUPON_ID, coupon.getId());
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
/*
        coupons.add(coupon);
*/

    }


    public void updateCoupon(Coupon coupon) throws myException, ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        coupons = getAllCoupons();
        boolean flag = false;
        for (Coupon c : coupons) {
            if (c.getId() == (coupon.getId())) {
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
        if (flag) {
            ContentValues values = new ContentValues();
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

    public void deleteCoupon(Coupon coupon) throws myException, ParseException {

        Coupon tobeDeleted = null;
        boolean flag = false;
        for (Coupon c : coupons) {
            if (c.getId() == (coupon.getId())) {
                tobeDeleted = c;
                flag = true;
                break;
            }
        }
        if (flag) {
            coupons.remove(tobeDeleted); // deleting it from the arraylist because all object in the list are references
            // Moaad func to delete also from customer vs coupons
            for(Customer customer: customers) {
                if (customer.getCoupons().contains(coupon)) {
                    customer.getCoupons().remove(coupon);
                }
            }
                for(Company company: companies){
                    if(company.getCoupons().contains(coupon)) {
                        company.getCoupons().remove(coupon);
                    }
            }
            deleteCustomerVsCouponCouponForAllCustomersByCouponId(coupon.getId());
            SQLiteDatabase db = getWritableDatabase();
            db.delete(TBL_COUPONS, COUPON_ID + "= ?", new String[]{(Integer.toString(coupon.getId()))});
            coupons = getAllCoupons();
        } else
            throw new myException("Delete failed No coupon found with id =" + coupon.getId() + "!");
    }

    public ArrayList<Coupon> getAllCoupons() throws ParseException {
        ArrayList<Coupon> coupons = new ArrayList<>();
        String[] fields = {COUPON_ID, COUPON_COMPANY_ID, COUPONS_CATEGORY, COUPONS_TITLE, COUPONS_DESC, COUPONS_START_DATE, COUPONS_END_DATE, COUPONS_AMOUNT, COUPONS_PRICE, COUPONS_IMAGE};
        String id, compID, category, title, desc, start, end, amount, price, image;
        try {
            Cursor cr = getCursor(TBL_COUPONS, fields, null);
            if (cr.moveToFirst())
                do {
                    id = cr.getString(0);
                    compID = cr.getString(1);
                    category = cr.getString(2);
                    title = cr.getString(3);
                    desc = cr.getString(4);
                    start = cr.getString(5);
                    end = cr.getString(6);
                    amount = cr.getString(7);
                    price = cr.getString(8);
                    image = cr.getString(9);
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    coupons.add(new Coupon( Integer.parseInt(id),Integer.parseInt(compID), Category.valueOf(category), title, desc, dateFormat.parse(start), dateFormat.parse(end), Integer.parseInt(amount), Double.parseDouble(price), image));
                } while (cr.moveToNext());
            return coupons;
        } catch (Exception e) {
            throw e;
        }


    }

    public Coupon getOneCoupon(int CouponID) throws ParseException {
        coupons = getAllCoupons();
        for (Coupon c : coupons) {
            if (c.getId() == CouponID)
                return c;
        }
        return null;
    }

    public void addCouponPurchase(int customerID, int couponID) throws ParseException, myException {
        Coupon coupon = getOneCoupon(couponID);
        Customer customer = getOneCustomer(customerID);

        if (coupon != null && customer != null) {
            if (coupon.getAmount() > 0) {
                coupon.setAmount(coupon.getAmount() - 1);

                customer.getCoupons().add(coupon);

                try {
                    updateCoupon(coupon);
                    updateCustomer(customer);
                    addCustomerVsCoupon(customerID,couponID);
                } catch (myException e) {
                    throw new myException(" database error during update");
                }
            } else {
                throw new myException("no coupons left ");
            }
        }
    }


    public void deleteCouponPurchase(int customerID, int couponID) throws myException, ParseException {
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

    public ArrayList<Coupon> getCompanyCouponsbyID(int companyID) throws ParseException {
        ArrayList<Coupon> coupons = getAllCoupons();
        ArrayList<Coupon> reqCoupons = new ArrayList<>();
        if(coupons!=null) {
            for (Coupon c : coupons)
            {
            if(c.getCompanyID() == companyID)
                reqCoupons.add(c);


            }
            return reqCoupons;
        }
        return null;

    }

    //___________________________________Categories___________________________________


    private final static String TBL_CATEGORIES = "categories";
    private final static String CATEGORY_ID = "id";
    private final static String CATEGORY_NAME = "name";


    private final static String CREATE_TABLE_CATEGORIES =
            "CREATE TABLE IF NOT EXISTS " + TBL_CATEGORIES +
                    " (" + CATEGORY_ID + " integer primary key autoincrement, " +
                    CATEGORY_NAME + " text)";
    public ArrayList<Category> getAllCategories() throws ParseException {
        ArrayList<Category> categories = new ArrayList<>();
        String[] fields = {CATEGORY_ID, CATEGORY_NAME};
        String  catName;
        int id;
        try {
            Cursor cr = getCursor(TBL_CATEGORIES, fields, null);
            if (cr.moveToFirst())
                do {
                    id = cr.getInt(0);
                    catName = cr.getString(1);
                    categories.add(Category.valueOf(catName));
                } while (cr.moveToNext());
            return categories;
        } catch (Exception e) {
            throw e;
        }
    }

    public Category getCategoryByName(String categoryName) {
        if (isValidEnum(Category.class, categoryName)) {
            return Category.valueOf(categoryName);
        }
        return null;
    }

    public static <E extends Enum<E>> boolean isValidEnum(Class<E> enumClass, String testString) {
        try {
            Enum.valueOf(enumClass, testString);
            return true;
        } catch (IllegalArgumentException ex) {
            return false;
        }
    }

    public void addCategory(String category) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();


        values.put(CATEGORY_NAME, category);

        db.insert(TBL_CATEGORIES, null, values);
        db.close();
    }

//___________________________________CUSTOMERS_VS_COUPONS___________________________________


    private final static String TBL_CUSTOMERS_VS_COUPONS = "customers_vs_coupons";
    private final static String CUSTOMERS_VS_COUPON_CUSTOMER_ID = "customer_id";
    private final static String CUSTOMERS_VS_COUPON_COUPON_ID = "coupon_id";


    private final static String CREATE_TABLE_CUSTOMERS_VS_COUPONS =
            "CREATE TABLE IF NOT EXISTS " + TBL_CUSTOMERS_VS_COUPONS +
                    " (" + CUSTOMERS_VS_COUPON_CUSTOMER_ID + " integer, " +
                    CUSTOMERS_VS_COUPON_COUPON_ID + " integer, " +
                    "PRIMARY KEY (" + CUSTOMERS_VS_COUPON_CUSTOMER_ID + ", " + CUSTOMERS_VS_COUPON_COUPON_ID + "))";



    public void addCustomerVsCoupon(int customerId,int couponId) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();


        values.put(CUSTOMERS_VS_COUPON_CUSTOMER_ID, customerId);
        values.put(CUSTOMERS_VS_COUPON_COUPON_ID, couponId);

        db.insert(TBL_CUSTOMERS_VS_COUPONS, null, values);
        db.close();
    }

    public ArrayList<Integer> getAllCouponsForSpecificCustomer(int customerId) {
        SQLiteDatabase db = getReadableDatabase();
        ArrayList<Integer> couponIds = new ArrayList<>();

        String[] columns = {CUSTOMERS_VS_COUPON_COUPON_ID};
        String selection = CUSTOMERS_VS_COUPON_CUSTOMER_ID + "=?";
        String[] selectionArgs = {String.valueOf(customerId)};

        Cursor cursor = db.query(TBL_CUSTOMERS_VS_COUPONS, columns, selection, selectionArgs, null, null, null);

        if (cursor.moveToFirst()) {
            int columnIndex = cursor.getColumnIndex(CUSTOMERS_VS_COUPON_COUPON_ID);
            if (columnIndex != -1) {
                do {
                    int couponId = cursor.getInt(columnIndex);
                    couponIds.add(couponId);
                } while (cursor.moveToNext());
            }
        }

        cursor.close();
        db.close();

        return couponIds;
    }

    public ArrayList<Integer> getAllCustomersForSpecificCoupon(int couponId) {
        SQLiteDatabase db = getReadableDatabase();
        ArrayList<Integer> customerIds = new ArrayList<>();

        String[] columns = {CUSTOMERS_VS_COUPON_CUSTOMER_ID};
        String selection = CUSTOMERS_VS_COUPON_COUPON_ID + "=?";
        String[] selectionArgs = {String.valueOf(couponId)};

        Cursor cursor = db.query(TBL_CUSTOMERS_VS_COUPONS, columns, selection, selectionArgs, null, null, null);

        if (cursor.moveToFirst()) {
            int columnIndex = cursor.getColumnIndex(CUSTOMERS_VS_COUPON_CUSTOMER_ID);
            if (columnIndex != -1) {
                do {
                    int customerId = cursor.getInt(columnIndex);
                    customerIds.add(customerId);
                } while (cursor.moveToNext());
            }
        }
        cursor.close();
        db.close();

        return customerIds;
    }

    public void deleteCustomerVsCouponCouponForAllCustomersByCouponId(int couponId) {
        SQLiteDatabase db = getWritableDatabase();

        // Define 'where' part of query
        String selection = CUSTOMERS_VS_COUPON_COUPON_ID + " = ?";

        // Specify arguments in placeholder order
        String[] selectionArgs = { String.valueOf(couponId) };

        // Issue SQL statement
        db.delete(TBL_CUSTOMERS_VS_COUPONS, selection, selectionArgs);
        db.close();
    }
    public void deleteCustomerVsCouponCouponForAllCouponsByCustomerId(int customerId) {
        SQLiteDatabase db = getWritableDatabase();

        // Define 'where' part of query
        String selection = CUSTOMERS_VS_COUPON_CUSTOMER_ID + " = ?";

        // Specify arguments in placeholder order
        String[] selectionArgs = { String.valueOf(customerId) };

        // Issue SQL statement
        db.delete(TBL_CUSTOMERS_VS_COUPONS, selection, selectionArgs);
        db.close();
    }



    // Functions Mix:


    public void deleteExpiredCouponsAndPurchaseHistory() throws myException, ParseException {
        // 1. Get a list of all expired coupons.
        List<Coupon> expiredCoupons = getExpiredCoupons();

        // 2. Delete each expired coupon.
        if (expiredCoupons != null) {
            for (Coupon coupon : expiredCoupons) {
                int compId = coupon.getCompanyID();
                Company company = getOneCompany(compId);
                if (company.getCoupons() != null)
                    company.getCoupons().remove(coupon);
                ArrayList<Integer> customersIds = getAllCustomersForSpecificCoupon(compId);
                if (customersIds != null)
                    for (int customerId : customersIds) {
                        Customer customer = getOneCustomer(customerId);
                        customer.getCoupons().remove(coupon);
                    }
                deleteCustomerVsCouponCouponForAllCustomersByCouponId(coupon.getId());
                deleteCoupon(coupon);
            }
        }
    }
    private List<Coupon> getExpiredCoupons() throws ParseException {
        List<Coupon> expiredCoupons = new ArrayList<>();
        coupons = getAllCoupons();
        Date today = new Date(); // Assuming that Coupon has a Date type for expiration date
        for (Coupon c : coupons) {
            if (c.getEndDate().before(today)) {
                expiredCoupons.add(c);
            }
        }
        return expiredCoupons;
    }



//    public void deleteExpiredCouponsAndPurchaseHistory() throws myException, ParseException {
//        // 1. Get a list of all expired coupons.
//        List<Coupon> expiredCoupons = getExpiredCoupons();
//
//        // 2. Delete each expired coupon.
//        for (Coupon coupon : expiredCoupons) {
//            deleteCoupon(coupon);
//
//        }
//    }




}
