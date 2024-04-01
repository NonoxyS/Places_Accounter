package com.example.placesaccounter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class CheckOutDateChecker {
    public static boolean checkOutDateIsClose(String date) {
        Date currentLocalDate = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.US);

        long diffInMillies, diffInDays;

        try {
            Date comparasionDate = dateFormat.parse(date);

            diffInMillies = comparasionDate.getTime() - currentLocalDate.getTime();
            diffInDays = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);

        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        return diffInDays < 2;
    }
}
