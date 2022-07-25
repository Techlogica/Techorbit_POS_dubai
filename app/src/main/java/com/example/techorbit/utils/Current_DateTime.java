package com.example.techorbit.utils;

import android.content.Context;
import android.util.Log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Neeraj.R on 21/6/2017.
 */

public class Current_DateTime {

    Context context;

    public Current_DateTime(Context context) {
        this.context = context;
    }

    public String current_datetimefor_receiptPrint(){
        String ret_data = null;

        try{
            DateFormat dfDate = new SimpleDateFormat("dd/MM/yyyy");
            String date=dfDate.format(Calendar.getInstance().getTime());
            DateFormat dfTime = new SimpleDateFormat("HH:mm");
            String time = dfTime.format(Calendar.getInstance().getTime());

            ret_data = date+","+time;

        }catch (Exception e){
            Log.e("@current_dateti()", e.getMessage());
        }
        return ret_data;
    }
    public  String getCurrentDateAndTime(){
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
        String formattedDate = simpleDateFormat.format(c);
        return formattedDate;
    }

    public int date_diff(String dateStart, String dateStop){
        int diff_date = 0;

//        Date d1 = null;
//        Date d2 = null;
//            SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
//
//            d1 = format.parse(start);
//            d2 = format.parse(end);
//
//            diff_date = (int) ((d2.getTime() - d1.getTime()) / (1000 * 60 * 60 * 24));
//            long dif = d1.getTime() - d2.getTime();
//            float daysBetween = (dif / (24*60*60*1000));
//
//            Log.d("daysBetween", daysBetween+"");


//            String dateStart = "2020-01-01";
//            String dateStop = "2020-01-06";

            SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");

            Date d1 = null;
            Date d2 = null;

            try {
                d1 = format.parse(dateStart);
                d2 = format.parse(dateStop);

                //in milliseconds
                long diff = d2.getTime() - d1.getTime();

                long diffSeconds = diff / 1000 % 60;
                long diffMinutes = diff / (60 * 1000) % 60;
                long diffHours = diff / (60 * 60 * 1000) % 24;
                long diffDays = diff / (24 * 60 * 60 * 1000);

                System.out.print(diffDays + " days, ");
                System.out.print(diffHours + " hours, ");
                System.out.print(diffMinutes + " minutes, ");
                System.out.print(diffSeconds + " seconds.");


                Log.d("days", diffDays+"");
                diff_date = (int)diffDays;

        }catch (Exception e){
            Log.e("@date_diff()", e.getMessage());
        }
        return diff_date;
    }

    public String[] datetime_split(String report_date){
        String[]date_time = new String[2];
        try{

            Log.d("report_date", report_date);
            date_time[0] = report_date.substring(0, report_date.indexOf(" "));


            String _24HourTime = report_date.substring(report_date.indexOf(" ")+1);
            SimpleDateFormat _24HourSDF = new SimpleDateFormat("HH:mm");
            SimpleDateFormat _12HourSDF = new SimpleDateFormat("hh:mm aa");
            Date _24HourDt = _24HourSDF.parse(_24HourTime);
            date_time[1] = _12HourSDF.format(_24HourDt);

        }catch (Exception e){
            Log.e("datetime_split()", e.getMessage());
        }
        return date_time;
    }

    public String get_current_milliseconds(){
        long timeMilli =0;
        try {
            timeMilli = System.currentTimeMillis()/1000;
        }catch (Exception e){
            Log.e("Exception", e.getMessage());
        }
        return timeMilli+"";
    }

}
