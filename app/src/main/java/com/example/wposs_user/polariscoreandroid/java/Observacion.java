package com.example.wposs_user.polariscoreandroid.java;

import com.example.wposs_user.polariscoreandroid.Comun.Global;
import com.example.wposs_user.polariscoreandroid.Comun.Utils;

import org.json.JSONException;
import org.json.JSONObject;

public class Observacion implements Comparable<Observacion> {

    //colocarle los mismos atributos que estan en la BD
    private String teob_id;
    private String teob_description;
    private String teob_fecha;
    private String teob_id_user;
    private String teob_photo;
    private String teob_serial_terminal;


    public Observacion(String teob_id, String teob_description, String teob_fecha, String teob_id_user, String teob_photo, String teob_serial_terminal) {
        this.teob_id = teob_id;
        this.teob_description = teob_description;
        this.teob_fecha = teob_fecha;
        this.teob_id_user = teob_id_user;
        this.teob_photo = teob_photo;
        this.teob_serial_terminal = teob_serial_terminal;
    }

    public Observacion(String teob_description, String teob_serial_terminal, String teob_photo) {
        this.teob_description = teob_description;
        this.teob_photo = teob_photo;
        System.out.println("observacion serial: " + teob_serial_terminal);
        this.teob_serial_terminal = teob_serial_terminal;
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

        String dato1=Utils.getDateFechaDDMMYYYY(this.getTeob_fecha());
        String dato2= Utils.getDateFechaDDMMYYYY(o.getTeob_fecha());

        String[] fecha= dato1.split("/");
        String[] fecha2= dato2.split("/");

        int dia= Integer.parseInt(fecha[0]);
        int mes= Integer.parseInt(fecha[1]);
        int año= Integer.parseInt(fecha[2]);

        int dia2= Integer.parseInt(fecha2[0]);
        int mes2= Integer.parseInt(fecha2[1]);
        int año2= Integer.parseInt(fecha2[2]);


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

            if(mes-mes2==0){
                if(dia>dia2){return 1;}
                if(dia<dia2){return -1;}

            }
        }


        else if((año-año2)<0){
            return -1;
        }


        return 0;




    }

}
