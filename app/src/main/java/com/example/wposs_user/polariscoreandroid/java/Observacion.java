package com.example.wposs_user.polariscoreandroid.java;

import com.example.wposs_user.polariscoreandroid.Comun.Global;
import com.example.wposs_user.polariscoreandroid.Comun.Utils;

import org.json.JSONException;
import org.json.JSONObject;

public class Observacion implements Comparable<Observacion> {

    //colocarle los mismos atributos que estan en la BD
    private String teob_id;
    private String teob_date;
    private String teob_description;
    private String teob_fecha;
    private String teob_id_user;
    private String teob_open;
    private String teob_photo;
    private String teob_serial_terminal;

  /*  private String teob_id;
    private String teob_description;
    private String teob_fecha;
    private String teob_id_user;
    private String teob_photo;
    private String teob_serial_terminal;*/


    public Observacion(String teob_id, String teob_description, String teob_fecha, String teob_id_user, String teob_photo, String teob_serial_terminal) {
        this.teob_id = teob_id;
        this.teob_description = teob_description;
        this.teob_fecha = teob_fecha;
        this.teob_id_user = teob_id_user;
        this.teob_photo = teob_photo;
        this.teob_serial_terminal = teob_serial_terminal;
    }

    public Observacion(String teob_id, String teob_date, String teob_description, String teob_fecha, String teob_id_user, String teob_open, String teob_photo, String teob_serial_terminal) {
        this.teob_id = teob_id;
        this.teob_date = teob_date;
        this.teob_description = teob_description;
        this.teob_fecha = teob_fecha;
        this.teob_id_user = teob_id_user;
        this.teob_open = teob_open;
        this.teob_photo = teob_photo;
        this.teob_serial_terminal = teob_serial_terminal;
    }
    public Observacion(String teob_description, String teob_serial_terminal, String teob_photo) {
        this.teob_description = teob_description;
        this.teob_photo = teob_photo;
        System.out.println("observacion serial: " + teob_serial_terminal);
        this.teob_serial_terminal = teob_serial_terminal;
    }


    public String getTeob_date() {
        return teob_date;
    }

    public void setTeob_date(String teob_date) {
        this.teob_date = teob_date;
    }

    public String getTeob_open() {
        return teob_open;
    }

    public void setTeob_open(String teob_open) {
        this.teob_open = teob_open;
    }

    public String getTeob_id() {
        return teob_id;
    }

    public void setTeob_id(String teob_id) {
        this.teob_id = teob_id;
    }

    public String getTeob_description() {
        return teob_description;
    }

    public void setTeob_description(String teob_description) {
        this.teob_description = teob_description;
    }

    public String getTeob_fecha() {
        return teob_fecha;
    }

    public void setTeob_fecha(String teob_fecha) {
        this.teob_fecha = teob_fecha;
    }

    public String getTeob_id_user() {
        return teob_id_user;
    }

    public void setTeob_id_user(String teob_id_user) {
        this.teob_id_user = teob_id_user;
    }

    public String getTeob_photo() {
        return teob_photo;
    }

    public void setTeob_photo(String teob_photo) {
        this.teob_photo = teob_photo;
    }

    public String getTeob_serial_terminal() {
        return teob_serial_terminal;
    }

    public void setTeob_serial_terminal(String teob_serial_terminal) {
        this.teob_serial_terminal = teob_serial_terminal;
    }

    @Override
    public String toString() {
        return "-------------id: " + teob_id + " descripcion: " + teob_description + " fecha: " + teob_fecha + " foto: " + teob_photo + "------------";
    }

    public JSONObject getObj() {


        JSONObject obj = new JSONObject();
        try {

            obj.put("teob_description", teob_description);
            obj.put("teob_serial_terminal", teob_serial_terminal);
            obj.put("teob_photo", teob_photo);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return obj;
    }

    public JSONObject getObjRep() {


        JSONObject obj = new JSONObject();
        try {

            obj.put("teob_description", teob_description);
            obj.put("teob_serial_terminal", teob_serial_terminal);
            obj.put("teob_photo", teob_photo);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return obj;
    }

    @Override
    public int compareTo(Observacion o) {

        String[] dato1=getTeob_fecha().split(" ");
        String[] dato2=o.getTeob_fecha().split(" ");
        System.out.println("formato de fecha dado************"+teob_fecha);

        int mes= Utils.obtenerNumMes(dato1[0]);
        int mes2= Utils.obtenerNumMes(dato2[0]);
        int dia= Integer.parseInt(dato1[1].substring(0,dato1[1].length()-1));
        int dia2= Integer.parseInt(dato2[1].substring(0,dato2[1].length()-1));
        int año= Integer.parseInt(dato1[2]);
        int año2= Integer.parseInt(dato2[2]);

        String[] hora=dato1[3].split(":");
        String[] hora2=dato2[3].split(":");

        String formato=dato1[4];
        String formato2=dato2[4];

        int horas = Integer.parseInt(hora[0]);
        int horas2 = Integer.parseInt(hora2[0]);

        int minutos = Integer.parseInt(hora[1]);
        int minutos2 = Integer.parseInt(hora2[1]);

        if(formato.equals("PM")){
            horas= horas+12;

        }
        if(formato2.equals("PM")){
            horas2= horas+12;

        }


        if((año-año2)>0){
            return 1;
        }

        else  if((año-año2)==0){

            if((mes-mes2>0)){
                return 1;
            }

            if(mes-mes2<0){
                return -1;
            }

            else if(mes-mes2==0){
                if(dia>dia2){return 1;}
                if(dia<dia2){return -1;}

                else if(dia==dia2){
                    if(horas>horas2){ return 1;}
                    if(horas<horas2){ return -1;}
                    else if(horas==horas2){
                        if(minutos>minutos2){ return 1;}
                        if(minutos<minutos2){ return -1;}

                    }
                }

            }
        }


        else if((año-año2)<0){
            return -1;
        }


        return 0;
    }

}
