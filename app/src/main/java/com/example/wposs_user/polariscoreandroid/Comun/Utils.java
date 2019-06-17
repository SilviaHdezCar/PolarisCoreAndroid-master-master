package com.example.wposs_user.polariscoreandroid.Comun;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.os.BatteryManager;
import android.os.Environment;
import android.os.Handler;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.View;
import android.widget.DatePicker;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.TimeZone;

/**
 * @author
 */
public class Utils {

    /**
     * Creates a new instance of Utils
     */
    public Utils() {

    }



    /**
     * Este metodo obtiene la fecha y hora actual
     * en el formato YYYYMMDDhhmmss
     *
     * @Retorna una cadena con el formato de fecha y hora
     */
    public static String getDateTime() {
        String day, month, year, hour, minute, second;
        TimeZone tz = TimeZone.getTimeZone("GMT-5");
        Calendar actualDateTime = Calendar.getInstance(tz);

        year = String.valueOf(actualDateTime.get(actualDateTime.YEAR));
        month = String.valueOf(actualDateTime.get(actualDateTime.MONTH) + 1);
        day = String.valueOf(actualDateTime.get(actualDateTime.DAY_OF_MONTH));
        hour = String.valueOf(actualDateTime.get(actualDateTime.HOUR_OF_DAY));
        minute = String.valueOf(actualDateTime.get(actualDateTime.MINUTE));
        second = String.valueOf(actualDateTime.get(actualDateTime.SECOND));

        if ((actualDateTime.get(actualDateTime.MONTH) + 1) < 10)
            month = "0" + month;
        if (actualDateTime.get(actualDateTime.DAY_OF_MONTH) < 10)
            day = "0" + day;
        if (actualDateTime.get(actualDateTime.HOUR_OF_DAY) < 10)
            hour = "0" + hour;
        if (actualDateTime.get(actualDateTime.MINUTE) < 10)
            minute = "0" + minute;
        if (actualDateTime.get(actualDateTime.SECOND) < 10)
            second = "0" + second;

        //year = year.substring(2,4);

        String dateTime = year + month + day + hour + minute + second;
        return dateTime;
    }


    public static String getDateTime2() {
        String day, month, year, hour, minute, second;
        TimeZone tz = TimeZone.getTimeZone("GMT-5");
        Calendar actualDateTime = Calendar.getInstance(tz);

        year = String.valueOf(actualDateTime.get(actualDateTime.YEAR));
        month = String.valueOf(actualDateTime.get(actualDateTime.MONTH) + 1);
        day = String.valueOf(actualDateTime.get(actualDateTime.DAY_OF_MONTH));
        hour = String.valueOf(actualDateTime.get(actualDateTime.HOUR_OF_DAY));
        minute = String.valueOf(actualDateTime.get(actualDateTime.MINUTE));
        second = String.valueOf(actualDateTime.get(actualDateTime.SECOND));

        if ((actualDateTime.get(actualDateTime.MONTH) + 1) < 10)
            month = "0" + month;
        if (actualDateTime.get(actualDateTime.DAY_OF_MONTH) < 10)
            day = "0" + day;
        if (actualDateTime.get(actualDateTime.HOUR_OF_DAY) < 10)
            hour = "0" + hour;
        if (actualDateTime.get(actualDateTime.MINUTE) < 10)
            minute = "0" + minute;
        if (actualDateTime.get(actualDateTime.SECOND) < 10)
            second = "0" + second;

        year = year.substring(2, 4);

        String dateTime = year + month + day + hour + minute;
        return dateTime;
    }


    /*******************************************************************************
     Function        : getDate
     Description     : Recibe la fecha en formato Dia/mes/año (20/01/18)
     Return          : Fecha
     ******************************************************************************/
    public static String getDate() {
        String day, month, year, hour, minute, second;
        TimeZone tz = TimeZone.getTimeZone("GMT-5");
        Calendar actualDateTime = Calendar.getInstance(tz);

        year = String.valueOf(actualDateTime.get(actualDateTime.YEAR));
        month = String.valueOf(actualDateTime.get(actualDateTime.MONTH) + 1);
        day = String.valueOf(actualDateTime.get(actualDateTime.DAY_OF_MONTH));
        hour = String.valueOf(actualDateTime.get(actualDateTime.HOUR_OF_DAY));
        minute = String.valueOf(actualDateTime.get(actualDateTime.MINUTE));
        second = String.valueOf(actualDateTime.get(actualDateTime.SECOND));

        if ((actualDateTime.get(actualDateTime.MONTH) + 1) < 10)
            month = "0" + month;
        if (actualDateTime.get(actualDateTime.DAY_OF_MONTH) < 10)
            day = "0" + day;
        if (actualDateTime.get(actualDateTime.HOUR_OF_DAY) < 10)
            hour = "0" + hour;
        if (actualDateTime.get(actualDateTime.MINUTE) < 10)
            minute = "0" + minute;
        if (actualDateTime.get(actualDateTime.SECOND) < 10)
            second = "0" + second;

        year = year.substring(2, 4);

        //String dateTime = year + month + day + hour + minute + second;
        String dateTime = day + "-" + month + "-" + year;
        return dateTime;
    }

    /*******************************************************************************
     Function        : getTime
     Description     : Recibe la hora en formato HH:MM (Hora:Minuto)
     Return          : Hora
     ******************************************************************************/
    public static String getTime() {

        String time;
        String day, month, year, hour, minute, second, ampm;
        TimeZone tz = TimeZone.getTimeZone("GMT-5");
        Calendar actualDateTime = Calendar.getInstance(tz);

        hour = String.valueOf(actualDateTime.get(actualDateTime.HOUR_OF_DAY));
        minute = String.valueOf(actualDateTime.get(actualDateTime.MINUTE));


        if (actualDateTime.get(actualDateTime.HOUR_OF_DAY) < 10)
            hour = "0" + hour;
        if (actualDateTime.get(actualDateTime.MINUTE) < 10)
            minute = "0" + minute;

        time = hour + ":" + minute;

        return time;
    }

    /*******************************************************************************
     Function        : obtenerMesLetras
     Description     : Recibe el mes en entero y lo convierte a cadena
     Input           : mes = mes en entero a convertir
     Return          : Cadena con fecha
     ******************************************************************************/
    public static String obtenerMesLetras(int mes) {

        switch (mes) {
            case 1:
                return "Ene";
            case 2:
                return "Feb";
            case 3:
                return "Mar";
            case 4:
                return "Abr";
            case 5:
                return "May";
            case 6:
                return "Jun";
            case 7:
                return "Jul";
            case 8:
                return "Ago";
            case 9:
                return "Sep";
            case 10:
                return "Oct";
            case 11:
                return "Nov";
            case 12:
                return "Dic";
            default:
                return "";
        }

    }





    // Metodo para convertir una hora en formato 12 a entero



    /**
     * Metodo que le da formato a la fecha cuando es= May 13, 2019 1:35 PM  ---> DD/MM/AAAA HORA
     *
     * @param fecha
     * @return
     */
    public static String darFormatoFechaObservaciones(String fecha) {
        String[] date = fecha.split(" ");

        String mes = date[0];
        String dia = date[1].substring(0, date[1].length() - 1);
        String anio = date[2];
        String hora = date[3];
        String AM_PM = date[4];
        String[] hour = hora.split(":");
        int hor = Integer.parseInt(hour[0]);
        String min = hour[1].substring(0, 2);

        if (AM_PM.equalsIgnoreCase("PM")) {
            hor += 12;
        }
        hora = hor + ":" + min;
        int mesFec = obtenerNumMes(mes);
        String month="";
        if (mesFec < 10){
            month = "0" + mesFec;
        } if (Integer.parseInt( dia)<10){
            dia="0"+dia;
        }

        return dia + "-" + month + "-" + anio + " " + hora;
    }


    /**
     * Metodo que le da formato a la fecha cuando es= Tue Jan 27 2009 15:27:00  ---> DD/MM/AAAA HORA
     *
     * @param fecha
     * @return
     */
    public static String darFormatoFecha2(String fecha) {
        String[] date = fecha.split(" ");
        int mes = obtenerNumMes(date[1]);
        String dia = date[2];
        String anio = date[3];
        String hora[] = date[4].split(":");
        String month=mes+"";
        if (mes < 10)
            month = "0" + mes;

        return dia + "-" + month + "-" + anio + " " + hora[0] + ":" + hora[1];
    }

    public static String darFormatoSimple(String fecha) {
        String[] date = fecha.split(" ");
        int mes = obtenerNumMes(date[1]);
        String dia = date[2];
        String anio = date[3];
        String month=mes+"";
        if (mes < 10)
            month = "0" + mes;

        return dia + "/" + month + "/" + anio ;
    }

    public static Date convertirDate(String fecha){
        Date date =null;
        SimpleDateFormat format= new SimpleDateFormat("dd/MM/yyyy");

        try{
             date =format.parse(darFormatoSimple(fecha));
            System.out.println(date);
            System.out.println(format.format(date));
        }catch (Exception e){
            e.printStackTrace();
        }
        return date;
    }


    /**
     * Este metodo obtiene la suma de la fecha     *
     * @Params recibe la fecha en el formato
     * @Retorna una un entero con la sumatopria de la fecha
     */
    public static int getSumaFecha(String fecha) {
        String fechaFormateada = darFormatoFechaObservaciones(fecha);
        String date[] = fechaFormateada.split(" ");

        int day = Integer.parseInt(date[1]);
        System.out.println("dia: " + day);

        int month = obtenerNumMes(date[0]);
        System.out.println("Mes: " + month);

        int year = Integer.parseInt(date[2]);
        System.out.println("Año: " + year);

        return day + month + year;

    }
    /**
     * Metodo que le da formato a la fecha cuando es= Thu Jun 13 11:47:49 GMT-05:00 2019---> DD/MM/AAAA HORA
     *
     * @param fecha
     * @return
     */
    public static String darFormatoNewDate(String fecha) {
        String[] date = fecha.split(" ");
        System.out.println("fecha "+fecha);
        for(int i=0;i<date.length;i++){
            System.out.println("Fecha pos["+i+"]:"+date[i]);
        }
        int mes = obtenerNumMes(date[1]);
        String dia = date[2];
        String anio = date[3];
        String hora[] = date[4].split(":");

        String month=mes+"";
        if (mes < 10)
            month = "0" + mes;

        return dia + "-" + month + "-" + anio + " " + hora[0] + ":" + hora[1];
    }
    /**
     * Este metodo obtiene la fecha y hora actual
     * en el formato DD/MM/YYYY
     *
     * @Retorna una cadena con el formato de fecha
     */
    //String fecha=Utils.getDateDDMMYYYY(getFEcha;
    public static String getDateFechaDDMMYYYY(String fecha) {
        String fechaFormateada = darFormatoFechaObservaciones(fecha);
        String date[] = fechaFormateada.split(" ");

        String day = date[1];
        System.out.println("dia: " + day);

        int month = obtenerNumMes(date[0]);
        System.out.println("Mes: " + month);

        String year = date[2];
        System.out.println("Año: " + year);

        String mes;

        if (month < 10)
            mes = "0" + month;
        if (Integer.parseInt(day) < 10)
            day = "0" + day;


        //year = year.substring(2,4);

        String dateTime = day + "/" + month + "/" + year;
        return dateTime;
    }

    /**
     * Este metodo se utiliza para obtener el mes en español
     *
     * @param mes
     * @return
     */
    public static String obtenerMes(String mes) {
        String mesFec = "";
        switch (mes) {
            case "January":
                mesFec = "Enero";
                break;
            case "February":
                mesFec = "Febrero";
                break;
            case "March":
                mesFec = "Marzo";
                break;
            case "April":
                mesFec = "Abril";
                break;
            case "May":
                mesFec = "Mayo";
                break;
            case "June":
                mesFec = "Junio";
                break;
            case "July":
                mesFec = "Julio";
                break;
            case "August":
                mesFec = "Agosto";
                break;
            case "September":
                mesFec = "Septiembre";
                break;
            case "October":
                mesFec = "Octubre";
                break;
            case "November":
                mesFec = "Noviembre";
                break;
            case "December":
                mesFec = "Diciembre";
                break;
            case "Jan":
                mesFec = "Enero";
                break;
            case "Feb":
                mesFec = "Febrero";
                break;
            case "Mar":
                mesFec = "Marzo";
                break;
            case "Apr":
                mesFec = "Abril";
                break;
            case "Jun":
                mesFec = "Junio";
                break;
            case "Jul":
                mesFec = "Julio";
                break;
            case "Aug":
                mesFec = "Agosto";
                break;
            case "Sep":
                mesFec = "Septiembre";
                break;
            case "Oct":
                mesFec = "Octubre";
                break;
            case "Nov":
                mesFec = "Noviembre";
                break;
            case "Dec":
                mesFec = "Diciembre";
                break;

        }
        return mesFec;
    }

    //Obtener dia mes-->Ejemplo: Mayo-->05
    public static int obtenerNumMes(String mes) {
        int numMes = 0;
        switch (mes) {
            case "Enero":
                numMes = 1;
                break;
            case "Febrero":
                numMes = 2;
                break;
            case "Marzo":
                numMes = 3;
                break;
            case "Abril":
                numMes = 4;
                break;
            case "Mayo":
                numMes = 5;
                break;
            case "Junio":
                numMes = 6;
                break;
            case "Julio":
                numMes = 7;
                break;
            case "Agosto":
                numMes = 8;
                break;
            case "Septiembre":
                numMes = 9;
                break;
            case "Octubre":
                numMes = 10;
                break;
            case "Noviembre":
                numMes = 11;
                break;
            case "Diciembre":
                numMes = 12;
                break;
            case "January":
                numMes = 1;
                break;
            case "February":
                numMes = 2;
                break;
            case "March":
                numMes = 3;
                break;
            case "April":
                numMes = 4;
                break;
            case "May":
                numMes = 5;
                break;
            case "June":
                numMes = 6;
                break;
            case "July":
                numMes = 7;
                break;
            case "August":
                numMes = 8;
                break;
            case "September":
                numMes = 9;
                break;
            case "October":
                numMes = 10;
                break;
            case "November":
                numMes = 11;
                break;
            case "December":
                numMes = 12;
                break;
            case "Jan":
                numMes = 1;
                break;
            case "Feb":
                numMes = 2;
                break;
            case "Mar":
                numMes = 3;
                break;
            case "Apr":
                numMes = 4;
                break;
            case "Jun":
                numMes = 6;
                break;
            case "Jul":
                numMes = 7;
                break;
            case "Aug":
                numMes = 8;
                break;
            case "Sep":
                numMes = 9;
                break;
            case "Oct":
                numMes = 10;
                break;
            case "Nov":
                numMes = 11;
                break;
            case "Dec":
                numMes = 12;
                break;

        }
        return numMes;
    }

    //Obtener dia mes-->Ejemplo: Mayo-->05
    public static String obtenerNumMes2(String mes) {
        String numMes = "";
        switch (mes) {
            case "Enero":
                numMes = "01";
                break;
            case "Febrero":
                numMes = "02";
                break;
            case "Marzo":
                numMes = "03";
                break;
            case "Abril":
                numMes = "04";
                break;
            case "Mayo":
                numMes = "05";
                break;
            case "Junio":
                numMes = "06";
                break;
            case "Julio":
                numMes = "07";
                break;
            case "Agosto":
                numMes = "08";
                break;
            case "Septiembre":
                numMes = "09";
                break;
            case "Octubre":
                numMes = "10";
                break;
            case "Noviembre":
                numMes = "11";
                break;
            case "Diciembre":
                numMes = "12";
                break;

        }
        return numMes;
    }

    public static String formatoDia(int dia){
        String rta=String.valueOf(dia);

        if(dia<10){

            if(dia==1){
                rta="01";

            }
            if(dia==2){
                rta="02";

            }
            if(dia==3){
                rta="03";

            }
            if(dia==4){
                rta="04";

            }
            if(dia==5){
                rta="05";

            }
            if(dia==6){
                rta="06";

            }
            if(dia==7){
                rta="07";

            }
            if(dia==8){
                rta="08";

            }
            if(dia==9){
                rta="09";

            }
        }
     return rta;

    }




}
