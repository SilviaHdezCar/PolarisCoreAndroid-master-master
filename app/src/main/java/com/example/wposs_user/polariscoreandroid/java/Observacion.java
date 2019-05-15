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

            obj.put(" teob_description", teob_description);
            obj.put("teob_serial_terminal", teob_serial_terminal);
            obj.put("teob_photo", "");
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
        String[] fecha_ob = getTeob_fecha().split(" ");
        int mesfec = Utils.obtenerNumMes(fecha_ob[0]);
        String dia = fecha_ob[1].substring(0, fecha_ob[1].length() - 1);

        int diafec = Integer.parseInt(dia);
        int aniofec = Integer.parseInt(fecha_ob[2]);

        int total = mesfec + diafec + aniofec;

        String[] fecha_ob2 = o.getTeob_fecha().split(" ");

        String dia2 = fecha_ob2[1].substring(0, fecha_ob2[1].length() - 1);
        int mesfec2 = Utils.obtenerNumMes(fecha_ob2[0]);
        int diafec2 = Integer.parseInt(dia2);
        int aniofec2 = Integer.parseInt(fecha_ob2[2]);

        if (aniofec > aniofec2) {
            return 1;
        } else if(aniofec < aniofec2){
            return -1;
        } else if (aniofec == aniofec2) {
            if (mesfec == mesfec2) {
                if (diafec == diafec2) {
                    return 0;
                } else if (diafec > diafec2) {
                    return 1;
                } else {
                    return -1;
                }
            } else if (mesfec > mesfec2) {
                return 1;
            } else if (mesfec < mesfec2) {
                return -1;
            }

        }


        return 0;
    }
}
