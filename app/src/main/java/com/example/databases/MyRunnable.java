package com.example.databases;

import java.text.ParseException;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;


public class MyRunnable implements Runnable {
    DB_Manager dbManager = DB_Manager.getInstance(null);


    private volatile boolean quit = false;

    @Override
    public void run() {
        while (!quit) {
            try {
                // Get the current time
                Calendar now = Calendar.getInstance();

                // Get the time for the next midnight
                Calendar nextMidnight = (Calendar) now.clone();
                nextMidnight.add(Calendar.DATE, 1);
                nextMidnight.set(Calendar.HOUR_OF_DAY, 0);
                nextMidnight.set(Calendar.MINUTE, 0);
                nextMidnight.set(Calendar.SECOND, 0);

                // Calculate the time to sleep until the next midnight
                long sleepTimeMillis = nextMidnight.getTimeInMillis() - now.getTimeInMillis();

                // Sleep until the next midnight
                TimeUnit.MILLISECONDS.sleep(sleepTimeMillis);

                // After waking up (at midnight), delete expired coupons
                deleteExpiredCoupons();

            } catch (InterruptedException e) {
                // If the thread was interrupted, stop the job
                break;
            } catch (myException | ParseException e) {
                // Handle the exceptions as per your requirements
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
