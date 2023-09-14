package com.example.databases;

import java.text.ParseException;
import java.util.concurrent.TimeUnit;


public class MyRunnable implements Runnable {
    DB_Manager dbManager = DB_Manager.getInstance(null);


    private volatile boolean quit = false;

    @Override
    public void run() {
        while (!quit) {
            try {
                // Try deleting expired coupons and their purchase history every 24 hours
                deleteExpiredCoupons();
                TimeUnit.HOURS.sleep(24);
            } catch (InterruptedException | myException | ParseException e) {
                // If the thread was interrupted, stop the job
                break;
            }
        }
    }

    private void deleteExpiredCoupons() throws myException, ParseException {
        System.out.println("Checking and deleting expired coupons...");
        dbManager.deleteExpiredCouponsAndPurchaseHistory();
        System.out.println("Expired coupons check complete.");
    }

    public void stop() {
        quit = true; // Setting quit to true will stop the loop in the run() method
        System.out.println("Stopping my code (MyThread)");
    }
}
