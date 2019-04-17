package com.example.wposs_user.polariscoreandroid;

import android.content.Context;


import java.util.Calendar;
import java.util.Date;
import java.util.StringTokenizer;

/**
 * Created by Julian on 7/06/2018.
 */

public class Tools {
    static Context ctx;

    public static void setCurrentContext(Context context)
    {
        ctx=context;
    }
    public static Context getCurrentContext()
    {
        return ctx;
    }

    public static void toast(String mensaje)
    {
        //Toast.makeText(Tools.getCurrentContext(),mensaje,Toast.LENGTH_LONG).show();

    }

    public static String timeStr(){
        Date date = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        //cal.setTimeZone(TimeZone.getTimeZone("GMT-5"));

        int hr = cal.get(Calendar.HOUR);
        int mi = cal.get(Calendar.MINUTE);
        int se = cal.get(Calendar.SECOND);
        String hrC;
        String miC;
        String seC;

        ///Hora
        if (cal.get(Calendar.AM_PM) == Calendar.AM ){
            if (hr == 0)
                hr=12;  ///Noon

            if (hr < 10){
                hrC = "0" + String.valueOf(hr);
            }
            else{
                hrC = String.valueOf(hr);
            }
        }
        else{
            hrC = String.valueOf(hr + 12); ///Se agregó para pointpay un + 1
        }

        ///Minuto
        if (mi < 10){
            miC = "0" + String.valueOf(mi);
        }
        else{
            miC = String.valueOf(mi);
        }

        ///Segundo
        if (se < 10){
            seC = "0" + String.valueOf(se);
        }
        else{
            seC = String.valueOf(se);
        }

        return hrC + miC + seC;
    }

    public static String dateStr(){
        Calendar cal = Calendar.getInstance();
        int mm = cal.get(Calendar.MONTH) + 1;
        int dd = cal.get(Calendar.DAY_OF_MONTH);
        String mmC;
        String ddC;

        ///Mes
        if (mm < 10){
            mmC = "0" + String.valueOf(mm);
        }
        else{
            mmC = String.valueOf(mm);
        }

        ///Dia
        if (dd < 10){
            ddC = "0" + String.valueOf(dd);
        }
        else{
            ddC = String.valueOf(dd);
        }
        return mmC + ddC;
    }

    public static String convert2YYYYMMDDStr(String date){
        String mm;
        String dd ;
        String yyyy;

        StringTokenizer tokens=new StringTokenizer(date,"/");
        dd=tokens.nextElement().toString();
        mm=tokens.nextElement().toString();
        yyyy=tokens.nextElement().toString();
        return yyyy+mm+dd;

    }
    public static String dateYYYYMMDDStr(){
        Calendar cal = Calendar.getInstance();
        int mm = cal.get(Calendar.MONTH) + 1;
        int dd = cal.get(Calendar.DAY_OF_MONTH);
        int yyyy = cal.get(Calendar.YEAR);
        String mmC;
        String ddC;
        String yyyyC;

        ///Mes
        if (mm < 10){
            mmC = "0" + String.valueOf(mm);
        }
        else{
            mmC = String.valueOf(mm);
        }

        ///Dia
        if (dd < 10){
            ddC = "0" + String.valueOf(dd);
        }
        else{
            ddC = String.valueOf(dd);
        }

        //Año
        if (yyyy < 200)
            yyyy += 1900;

        yyyyC = String.valueOf(yyyy);

        return yyyyC + mmC + ddC;
    }

    public static String dateYYYYMMDDStr2(String date){
        int mm;
        int dd ;
        int yyyy;

        StringTokenizer tokens=new StringTokenizer(date,"/");
        dd=Integer.parseInt(tokens.nextElement().toString());
        mm=Integer.parseInt(tokens.nextElement().toString());
        yyyy=Integer.parseInt(tokens.nextElement().toString());


        String mmC;
        String ddC;
        String yyyyC;

        ///Mes
        if (mm < 10){
            mmC = "0" + String.valueOf(mm);
        }
        else{
            mmC = String.valueOf(mm);
        }

        ///Dia
        if (dd < 10){
            ddC = "0" + String.valueOf(dd);
        }
        else{
            ddC = String.valueOf(dd);
        }

        //Año
        if (yyyy < 200)
            yyyy += 1900;

        yyyyC = String.valueOf(yyyy);

        return yyyyC+"/"+ mmC+"/" + ddC;
    }
}