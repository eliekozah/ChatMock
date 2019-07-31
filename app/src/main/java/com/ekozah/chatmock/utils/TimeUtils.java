package com.ekozah.chatmock.utils;

import android.text.format.DateUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ThreadLocalRandom;

public class TimeUtils {

    public static SimpleDateFormat DATE_NUMBERS_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    public static SimpleDateFormat TIME_SELECTED_FORMAT = new SimpleDateFormat("hh:mm a");

    /**
     * this function returns a date object from string
     * @param dateString date in string format
     * @return date object
     */
    public static Date getDateFromString(String dateString) {
        Date date = new Date();
        try {
            date = DATE_NUMBERS_FORMAT.parse(dateString);

        }catch (
                ParseException e){

        }
        return date;
    }

    /**
     * this function returns the time if the date was today was today, yesterday if the date was yesterday, and the date if the date was before yesterday
     * @param date the date to be formatted
     * @return boolean teslling if the time is yesterday
     */
    public static boolean isYesterday(Date date) {
        return DateUtils.isToday(date.getTime() + DateUtils.DAY_IN_MILLIS);
    }


    /**
     * this function returns the time if the date was today was today, yesterday if the date was yesterday, and the date if the date was before yesterday
     * @param date the date to be formatted
     * @return string the target time or date format
     */
    public static String getTimeOrDate (Date date){
        Date d = new Date();
        String s = DATE_NUMBERS_FORMAT.format(d);
        String s2 = DATE_NUMBERS_FORMAT.format(date);
        if(s.equals(s2)){
            return TIME_SELECTED_FORMAT.format(date);
        }
        if(isYesterday(date)){
            return "Yesterday";
        }
        return s2;
    }

    /**
     * this function returns a random time between now and last three days
     * @param
     * @return string the target time or date format
     */
    public static Date generateRandomTimeBetweenNowAndLastThreeDays() {
        Date now = new Date();
        long timeRangeMs = 1000 * 60 * 60 * 24 * 3; // 3 days in ms
        long random = ThreadLocalRandom.current().nextLong(timeRangeMs);
        Date randomDate = new Date(now.getTime() - random);
        return randomDate;
    }


}
