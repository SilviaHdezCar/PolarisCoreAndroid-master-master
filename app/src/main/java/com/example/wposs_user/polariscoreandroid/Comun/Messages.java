package com.example.wposs_user.polariscoreandroid.Comun;

import android.content.Context;
import android.util.Log;

import com.example.wposs_user.polariscoreandroid.java.Repuesto;
import com.example.wposs_user.polariscoreandroid.java.Terminal;
import com.example.wposs_user.polariscoreandroid.java.Observacion;
import com.example.wposs_user.polariscoreandroid.java.Tipificacion;
import com.example.wposs_user.polariscoreandroid.java.Validacion;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Messages {


    /*****************************************************************************************
     * EMPAQUETADO DE LISTAR Tipificaciones, LO QUE SE ENVIA
     *SOLO TIENE ENCABEZADO
     * **************************************************************************************/
    public static void packMsgListarTipificaciones() {
        //packHttpDataListarObservaciones(); no lleva body
        packHttpHeaderLogueado();

        Global.outputData = (Global.httpHeaderBuffer).getBytes();

        Global.outputLen = Global.outputData.length;
        //Utils.dumpMemory(Global.outputData, Global.outputLen);
        Log.i("outputData*******", "" + uninterpret_ASCII(Global.inputData, 0, Global.inputData.length));

    }

    public static void packMsgUpdatePass() {

        packHttpUpdatePass();
        packHttpHeaderPut();


        Global.outputData = (Global.httpHeaderBuffer).getBytes();

        Global.outputData = (Global.httpHeaderBuffer + Global.httpDataBuffer).getBytes();

        Global.outputLen = Global.outputData.length;
        //Utils.dumpMemory(Global.outputData, Global.outputLen);
        Log.i("outputData*******", "" + uninterpret_ASCII(Global.inputData, 0, Global.inputData.length));

    }

    /*********************************************
     * ARMA EL CUERPO DE LA TRAMA DE ENVIO PARA ACTUALIZAR LA CLAVE
     * ***********************************************************/
    public static void packHttpUpdatePass() {
        //comienza a armar la trama
        Global.httpDataBuffer = "{\"user_identification\":\"<cc>\",\"user_password\":\"<clave_nueva>\"}";//se arma la trama

        Global.httpDataBuffer = Global.httpDataBuffer.replace("<cc>", Global.ID);
        Global.httpDataBuffer = Global.httpDataBuffer.replace("<clave_nueva>", Global.claveNueva);
        //fn


    }


    /************************************************************************************************
     * DESEMPAQUETADO DE LA RESPUESTA DEL SERVIDOR --> LISTAR LAS tipificaciones
     *****************************************************************************************************/
    public static boolean unPackMsgListarTipificaciones(Context c) {

        String tramaCompleta = "";
        Tipificacion v = null;

        int indice = 0;

        Global.inputData = Global.httpDataBuffer.getBytes();


        tramaCompleta = uninterpret_ASCII(Global.inputData, indice, Global.inputData.length);//se convierte arreglo de bytes a string


        int tramaNecesitada = tramaCompleta.indexOf("}");

        String trama = tramaCompleta.substring(0, tramaNecesitada + 1);//ESTA ES LA TRAMA QUE ENVIA EL SERVIDOR, ES LA QUE SE VA A DESEMPAQUETAR


        String[] lineastrama = trama.split(",");
        Gson gson = new GsonBuilder().create();
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(tramaCompleta);

            if (jsonObject.get("message").toString() != null) {
                System.out.println("--------------ENTRÓ AL MSJ DE ERROR");
                Global.mensaje = lineastrama[0].substring(12, tramaNecesitada - 1);
                Log.i("mensaje de error", "" + jsonObject.get("message").toString());
                return false;
            }

            System.out.println("*********Obtiene el arreglo de tipificaciones");
            JSONArray jsonArray = jsonObject.getJSONArray("tipificaciones");

            Global.TIPIFICACIONES = new ArrayList<Tipificacion>();
            System.out.println("Va a recorrer el JsonArray de tipificciones");
            if (jsonArray.length() == 0) {
                Global.mensaje = "No tiene tipificaciones";
                return true;
            }
            for (int i = 0; i < jsonArray.length(); i++) {
                String val = jsonArray.getString(i);

                v = gson.fromJson(val, Tipificacion.class);
                System.out.println("***********Va a agg tipificacion a la List<Tipificcon>**********(" + i + "): " + v.toString());
                Global.TIPIFICACIONES.add(v);

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return true;


    }


    /*****************************************************************************************
     * EMPAQUETADO DE LISTAR VALIDACIONES, LO QUE SE ENVIA
     *SOLO TIENE ENCABEZADO
     * **************************************************************************************/
    public static void packMsgListarValidaciones() {
        // no lleva body
        packHttpHeaderLogueadoValidaciones();

        Global.outputData = (Global.httpHeaderBuffer).getBytes();

        Global.outputLen = Global.outputData.length;
        //Utils.dumpMemory(Global.outputData, Global.outputLen);
        Log.i("outputData*******", "" + uninterpret_ASCII(Global.inputData, 0, Global.inputData.length));

    }

    /************************************************************************************************
     * DESEMPAQUETADO DE LA RESPUESTA DEL SERVIDOR --> LISTAR LAS VALIDACIONES*************************/
    public static boolean unPackMsgListarValidaciones(Context c) {

        String tramaCompleta = "";
        Validacion v = null;

        int indice = 0;

        Global.inputData = Global.httpDataBuffer.getBytes();


        tramaCompleta = uninterpret_ASCII(Global.inputData, indice, Global.inputData.length);//se convierte arreglo de bytes a string


        Gson gson = new GsonBuilder().create();
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(tramaCompleta);

            Global.STATUS_SERVICE = jsonObject.get("status").toString();


            if (Global.STATUS_SERVICE.equalsIgnoreCase("fail")) {
                Global.mensaje = jsonObject.get("message").toString();
                return false;
            }


            jsonObject=new JSONObject(jsonObject.get("data").toString());

            JSONArray jsonArray = jsonObject.getJSONArray("validaciones");

            Global.VALIDACIONES = new ArrayList<Validacion>();
            System.out.println("**************************TAMAÑO DEL ARREGLO DE VALIDACIONES: "+jsonArray.length());
            if (jsonArray.length() == 0) {
                Global.mensaje = "No tiene validaciones";
                return true;
            }
            for (int i = 0; i < jsonArray.length(); i++) {
                String val = jsonArray.getString(i);

                v = gson.fromJson(val, Validacion.class);
                System.out.println("***********Va a agg VALIDACION a la List<vALIDACIONES>**********(" + i + "): " + v.toString());
                Global.VALIDACIONES.add(v);

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return true;


    }

    /*****************************************************************************************
     * EMPAQUETADO DE LISTAR REPUESTOS, LO QUE SE ENVIA
     *
     * **************************************************************************************/

    public static void packMsgListarRepuestos() {
        packHttpDataListarRepuestos();
        packHttpHeaderLogueado();

        Global.outputData = (Global.httpHeaderBuffer + "\r\n\r\n" + Global.httpDataBuffer).getBytes();

        Global.outputLen = Global.outputData.length;
        //Utils.dumpMemory(Global.outputData, Global.outputLen);
        Log.i("outputData*******", "" + uninterpret_ASCII(Global.inputData, 0, Global.inputData.length));

    }


    /*********************************************
     * ARMA EL CUERPO DE LA TRAMA DE ENVIO PARA LISTAR REPUESTOS
     * ***********************************************************/
    public static void packHttpDataListarRepuestos() {
        //comienza a armar la trama
        Global.httpDataBuffer = "{\"model\": \"<SERIAL>\"}";//se arma la trama

        Global.httpDataBuffer = Global.httpDataBuffer.replace("<SERIAL>", "9220");


        //fn


    }


    /*****************************************************************************************
     * EMPAQUETADO DE Los DIAGNOSTICOS, LO QUE SE ENVIA
     *
     * **************************************************************************************/
    public static void packMsgRegistrarDiag() {
        packHttpDataListarObservaciones();
        packHttpHeaderLogueado();

        Global.outputData = (Global.httpHeaderBuffer + "\r\n\r\n" + Global.httpDataBuffer).getBytes();

        Global.outputLen = Global.outputData.length;
        //Utils.dumpMemory(Global.outputData, Global.outputLen);
        Log.i("outputData*******", "" + uninterpret_ASCII(Global.inputData, 0, Global.inputData.length));

    }


    /*****************************************************************************************
     * EMPAQUETADO DE LISTAR OBSERVACIONES, LO QUE SE ENVIA
     *
     * **************************************************************************************/
    public static void packMsgListarObservaciones() {
        packHttpDataListarObservaciones();
        packHttpHeaderLogueado();

        Global.outputData = (Global.httpHeaderBuffer + "\r\n\r\n" + Global.httpDataBuffer).getBytes();

        Global.outputLen = Global.outputData.length;
        //Utils.dumpMemory(Global.outputData, Global.outputLen);
        Log.i("outputData*******", "" + uninterpret_ASCII(Global.inputData, 0, Global.inputData.length));

    }

    /*********************************************
     * ARMA EL CUERPO DE LA TRAMA DE ENVIO PARA LISTAR OBSERVACIONES
     * ***********************************************************/
    public static void packHttpDataListarObservaciones() {
        //comienza a armar la trama
        Global.httpDataBuffer = "{\"serial\": \"<SERIAL>\"}";//se arma la trama

        Global.httpDataBuffer = Global.httpDataBuffer.replace("<SERIAL>", Global.serial_ter);
        //fn


    }

    /*********************************************
     * ARMA EL CUERPO DE LA TRAMA DE ENVIO PARA LISTAR DIAGNOSTICOS
     * ***********************************************************/
    public static void packHttpDataDiagnostico() {
        //comienza a armar la trama
        Global.httpDataBuffer = "{\"serial\": \"<SERIAL>\"}";//se arma la trama

        Global.httpDataBuffer = Global.httpDataBuffer.replace("<SERIAL>", "9220");
        //fn


    }


    /************************************************************************************************
     * DESEMPAQUETADO DE LA RESPUESTA DEL SERVIDOR --> LISTAR LOS REPUESTOS
     *****************************************************************************************************/
    public static boolean unPackMsgListaRepuestos(Context c) {

        String tramaCompleta = "";
        Repuesto r = null;


        int indice = 0;

        Global.inputData = Global.httpDataBuffer.getBytes();


        tramaCompleta = uninterpret_ASCII(Global.inputData, indice, Global.inputData.length);//se convierte arreglo de bytes a string


        int tramaNecesitada = tramaCompleta.indexOf("}");

        String trama = tramaCompleta.substring(0, tramaNecesitada + 1);//ESTA ES LA TRAMA QUE ENVIA EL SERVIDOR, ES LA QUE SE VA A DESEMPAQUETAR


        String[] lineastrama = trama.split(",");
        Gson gson = new GsonBuilder().create();
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(tramaCompleta);
/*
            if(jsonObject.get("message").toString()!=null){
                System.out.println("--------------ENTRÓ AL MSJ DE ERROR");
                Global.mensaje=lineastrama[0].substring(12, tramaNecesitada-1);
                Log.i("mensaje de error", ""+jsonObject.get("message").toString());
                return false;
            }
*/
            System.out.println("*********Obtiene el arreglo de repuestos");
            JSONArray jsonArray = jsonObject.getJSONArray("repuestos");

            Global.REPUESTOS = new ArrayList<Repuesto>();
            System.out.println("Va a recorrer el JsonArray de repuestos");
            if (jsonArray.length() == 0) {
                Global.mensaje = "No se encontraron repuestos asociados a la terminal";
                return true;
            }
            for (int i = 0; i < jsonArray.length(); i++) {
                String obs = jsonArray.getString(i);

                r = gson.fromJson(obs, Repuesto.class);
                System.out.println("***********Va a agg terminal a la List<Repuestos>*************Repuesto(" + i + "): " + r.toString());
                Global.REPUESTOS.add(r);

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        System.out.print("numero de repuestos" + "  " + Global.REPUESTOS.size());

        return true;


    }


    /************************************************************************************************
     * DESEMPAQUETADO DE LA RESPUESTA DEL SERVIDOR --> LISTAR LAS OBSERVACIONES
     *****************************************************************************************************/
    public static boolean unPackMsgListarObservaciones(Context c) {

        String tramaCompleta = "";
        Observacion o = null;

        int indice = 0;

        Global.inputData = Global.httpDataBuffer.getBytes();

        tramaCompleta = uninterpret_ASCII(Global.inputData, indice, Global.inputData.length);//se convierte arreglo de bytes a string

        Gson gson = new GsonBuilder().create();
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(tramaCompleta);

            Global.STATUS_SERVICE = jsonObject.get("status").toString();


            if (Global.STATUS_SERVICE.equalsIgnoreCase("fail")) {
                Global.mensaje = jsonObject.get("message").toString();
                return false;
            }


            jsonObject=new JSONObject(jsonObject.get("data").toString());

            JSONArray jsonArray = jsonObject.getJSONArray("observaciones");

            Global.OBSERVACIONES = new ArrayList<Observacion>();
            System.out.println("*******************Tamaño del arreglo de observaciones: "+jsonArray.length());
            if (jsonArray.length() == 0) {
                Global.mensaje = "No tiene observaciones";
                return true;
            }

            for (int i = 0; i < jsonArray.length(); i++) {
                String obs = jsonArray.getString(i);

                o = gson.fromJson(obs, Observacion.class);
                Global.OBSERVACIONES.add(o);

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return true;


    }


    //************************EMPAQUETADO DE LISTAR TERMINALES*****LO QUE SE ENVIA**********************************
    public static void packMsgListaAsociadas() {
        packHttpDataListaAsociadas();
        packHttpHeaderLogueado();

        Global.outputData = (Global.httpHeaderBuffer + "\r\n\r\n" + Global.httpDataBuffer).getBytes();

        Global.outputLen = Global.outputData.length;
        //Utils.dumpMemory(Global.outputData, Global.outputLen);
        Log.i("outputData*******", "" + uninterpret_ASCII(Global.inputData, 0, Global.inputData.length));

    }


    //ARMA EL CUERPO DE LA TRAMA DE ENVIO
    public static void packHttpDataListaAsociadas() {
        //comienza a armar la trama
        Global.httpDataBuffer = "{\"code\": \"<CODE>\"}";//se arma la trama

        Global.httpDataBuffer = Global.httpDataBuffer.replace("<CODE>", Global.CODE);
        //fn


    }

    //ARMA LA CABECERA DE METODOS QUE VAN POR GET CUANDO ESTÁN LOGUEADOS___---------------------------------------------------------------------------
    public static void packHttpHeaderLogueadoValidaciones() {
//cabecera
        int tam;
        Global.httpHeaderBuffer = "";
        Global.httpHeaderBuffer = "POST " + Global.WEB_SERVICE + " HTTP/1.1";
        Global.httpHeaderBuffer = Global.httpHeaderBuffer + "\r\n";
        Global.httpHeaderBuffer = Global.httpHeaderBuffer + "Authenticator: " + Global.TOKEN;
        Global.httpHeaderBuffer = Global.httpHeaderBuffer + "\r\n";
        Global.httpHeaderBuffer = Global.httpHeaderBuffer + Global.HTTP_HEADER2;
        Global.httpHeaderBuffer = Global.httpHeaderBuffer + Global.INITIAL_IP;
        Global.httpHeaderBuffer = Global.httpHeaderBuffer + ":";
        Global.httpHeaderBuffer = Global.httpHeaderBuffer + Global.INITIAL_PORT;
       Global.httpHeaderBuffer = Global.httpHeaderBuffer + "\r\n";
       Global.httpHeaderBuffer = Global.httpHeaderBuffer + Global.HTTP_HEADER3;
       Global.httpHeaderBuffer = Global.httpHeaderBuffer + Global.httpDataBuffer.length();

    }

    //ARMA LA CABECERA DE METODOS QUE VAN POR POST CUANDO ESTÁN LOGUEADOS
    public static void packHttpHeaderLogueado() {
//cabecera
        int tam;
        Global.httpHeaderBuffer = "";
        Global.httpHeaderBuffer = "POST " + Global.WEB_SERVICE + " HTTP/1.1";
        Global.httpHeaderBuffer = Global.httpHeaderBuffer + "\r\n";
        Global.httpHeaderBuffer = Global.httpHeaderBuffer + "Authenticator: " + Global.TOKEN;
        Global.httpHeaderBuffer = Global.httpHeaderBuffer + "\r\n";
        Global.httpHeaderBuffer = Global.httpHeaderBuffer + Global.HTTP_HEADER1;
        Global.httpHeaderBuffer = Global.httpHeaderBuffer + "\r\n";
        Global.httpHeaderBuffer = Global.httpHeaderBuffer + Global.HTTP_HEADER2;
        Global.httpHeaderBuffer = Global.httpHeaderBuffer + Global.INITIAL_IP;
        Global.httpHeaderBuffer = Global.httpHeaderBuffer + ":";
        Global.httpHeaderBuffer = Global.httpHeaderBuffer + Global.INITIAL_PORT;
        Global.httpHeaderBuffer = Global.httpHeaderBuffer + "\r\n";
        Global.httpHeaderBuffer = Global.httpHeaderBuffer + Global.HTTP_HEADER3;
        Global.httpHeaderBuffer = Global.httpHeaderBuffer + Global.httpDataBuffer.length();

    }


    public static void packHttpHeaderPut() {
//cabecera
        Global.httpHeaderBuffer = " ";
        Global.httpHeaderBuffer = "PUT " + Global.WEB_SERVICE + " HTTP/1.1";
        Global.httpHeaderBuffer = Global.httpHeaderBuffer + "\r\n";
        Global.httpHeaderBuffer = Global.httpHeaderBuffer + Global.HTTP_HEADER1;
        Global.httpHeaderBuffer = Global.httpHeaderBuffer + "\r\n";
        Global.httpHeaderBuffer = Global.httpHeaderBuffer + "Authenticator: " + Global.TOKEN;
        Global.httpHeaderBuffer = Global.httpHeaderBuffer + "\r\n";
        Global.httpHeaderBuffer = Global.httpHeaderBuffer + Global.HTTP_HEADER2;
        Global.httpHeaderBuffer = Global.httpHeaderBuffer + Global.INITIAL_IP;
        Global.httpHeaderBuffer = Global.httpHeaderBuffer + ":";
        Global.httpHeaderBuffer = Global.httpHeaderBuffer + Global.INITIAL_PORT;
        Global.httpHeaderBuffer = Global.httpHeaderBuffer + "\r\n";
        Global.httpHeaderBuffer = Global.httpHeaderBuffer + Global.HTTP_HEADER6;
        Global.httpHeaderBuffer = Global.httpHeaderBuffer + Global.httpDataBuffer.length();
        Global.httpHeaderBuffer = Global.httpHeaderBuffer + "\r\n";
        Global.httpHeaderBuffer = Global.httpHeaderBuffer + "\r\n";

    }




    //***************************DESEMPAQUETADO DE LISTAR TERMINALES*******LO QUE RECIBO
    public static boolean unPackMsgListarAsociadas(Context c) {

        String tramaCompleta = "";
        Terminal t = null;

        int indice = 0;

        Global.inputData = Global.httpDataBuffer.getBytes();


        tramaCompleta = uninterpret_ASCII(Global.inputData, indice, Global.inputData.length);//se convierte arreglo de bytes a string


        Gson gson = new GsonBuilder().create();
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(tramaCompleta);

            Global.STATUS_SERVICE = jsonObject.get("status").toString();


            if (Global.STATUS_SERVICE.equalsIgnoreCase("fail")) {
                Global.mensaje = jsonObject.get("message").toString();
                return false;
            }


            jsonObject=new JSONObject(jsonObject.get("data").toString());


            JSONArray jsonArray = jsonObject.getJSONArray("terminales");

            Global.TERMINALES_ASOCIADAS = new ArrayList<Terminal>();

            if (jsonArray.length() == 0) {
                Global.mensaje = "No tiene terminales asociadas";
                return true;
            }
            for (int i = 0; i < jsonArray.length(); i++) {
                String ter = null;

                ter = jsonArray.getString(i);

                t = gson.fromJson(ter, Terminal.class);
                Global.TERMINALES_ASOCIADAS.add(t);
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
        return true;
    }
//************************EMPAQUETADO DEL LOGIN*****LO QUE SE ENVIA**********************************

    public static void packMsgLogin() {
        packHttpData();
        packHttpHeader();

        Global.outputData = (Global.httpHeaderBuffer + "\r\n\r\n" + Global.httpDataBuffer).getBytes();

        Global.outputLen = Global.outputData.length;
        //Utils.dumpMemory(Global.outputData, Global.outputLen);

    }


    //ARMA LA TRAMA DE ENVIO DEL LOGIN
    public static void packHttpData() {
        //comienza a armar la trama
        Global.httpDataBuffer = "{\"user_email\": \"<CORREO>\",\"user_password\": \"<PASSWORD>\",\"gethash\": \"true\"}";//se arma la trama

        Global.httpDataBuffer = Global.httpDataBuffer.replace("<CORREO>", Global.correo);
        Global.httpDataBuffer = Global.httpDataBuffer.replace("<PASSWORD>", Global.password);
        //fn


    }

    //ARMA LA CABECERA
    public static void packHttpHeader() {
//cabecera
        int tam;
        Global.httpHeaderBuffer = "";
        Global.httpHeaderBuffer = "POST " + Global.WEB_SERVICE + " HTTP/1.1";
        Global.httpHeaderBuffer = Global.httpHeaderBuffer + "\r\n";
        Global.httpHeaderBuffer = Global.httpHeaderBuffer + Global.HTTP_HEADER1;
        Global.httpHeaderBuffer = Global.httpHeaderBuffer + "\r\n";
        Global.httpHeaderBuffer = Global.httpHeaderBuffer + Global.HTTP_HEADER2;
        Global.httpHeaderBuffer = Global.httpHeaderBuffer + Global.INITIAL_IP;
        Global.httpHeaderBuffer = Global.httpHeaderBuffer + ":";
        Global.httpHeaderBuffer = Global.httpHeaderBuffer + Global.INITIAL_PORT;
        Global.httpHeaderBuffer = Global.httpHeaderBuffer + "\r\n";
        Global.httpHeaderBuffer = Global.httpHeaderBuffer + Global.HTTP_HEADER3;
        Global.httpHeaderBuffer = Global.httpHeaderBuffer + Global.httpDataBuffer.length();

    }


    //***************************DESEMPAQUETADO DEL LOGIN*******LO QUE RECIBO
    public static boolean unPackMsgLogin(Context c) {


        String tramaCompleta = "";


        int indice = 0;

        Global.inputData = Global.httpDataBuffer.getBytes();
        //Global.inputData = Utils.replaceSpecialChars(Global.inputData, Global.inputData.length);

        //System.out.println("***********************************input data reempplqazado*******************"+uninterpret_ASCII(Global.inputData, indice, Global.inputData.length));


        tramaCompleta = uninterpret_ASCII(Global.inputData, indice, Global.inputData.length);//se convierte arreglo de bytes a string


        int tramaNecesitada = tramaCompleta.indexOf("}");

        String trama = tramaCompleta.substring(0, tramaNecesitada + 1);//ESTA ES LA TRAMA QUE ENVIA EL SERVIDOR, ES LA QUE SE VA A DESEMPAQUETAR
        Log.i("TRAMA OBTENIDA:    ", "" + tramaCompleta);

        String[] lineastrama = trama.split(",");

        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(tramaCompleta);

            Global.MESSAGE = jsonObject.get("message").toString();
            Log.i("MESSAGE:    ", "" + Global.MESSAGE);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (!Global.MESSAGE.equalsIgnoreCase("success")) {
            try {
                Global.mensaje = jsonObject.get("description").toString();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return false;
        } else {
            Global.mensaje = "";
            Global.TOKEN = lineastrama[0].substring(10, lineastrama[0].length() - 1);
            Global.MESSAGE = lineastrama[1].substring(11, lineastrama[1].length() - 1);
            Global.ROL = lineastrama[2].substring(9, lineastrama[2].length() - 1);
            Global.LOGIN = lineastrama[3].substring(9, lineastrama[3].length() - 1);
            Global.ID = lineastrama[4].substring(6, lineastrama[4].length() - 1);//CEDULA
            Global.STATUS = lineastrama[5].substring(10, lineastrama[5].length() - 1);
            Global.POSITION = lineastrama[6].substring(12, lineastrama[6].length() - 1);
            Global.CODE = lineastrama[7].substring(8, lineastrama[7].length() - 1);
            Global.NOMBRE = lineastrama[8].substring(8, lineastrama[8].length() - 1);//se coloca -2 porque toma la llave de cerrar, si vienen más elementos se deja -1
            //faltan más datos Celular-correo-ubicacion


            Log.i("------------STATUS: ", "" + Global.STATUS);
            Log.i("------------POSITION: ", "" + Global.POSITION);
            Log.i("------------TOKEN: ", "" + Global.TOKEN);
            Log.i("--------CODE: ", "" + Global.CODE);
            Log.i("-------NAME: ", "" + Global.NOMBRE);

            if (!Global.POSITION.equalsIgnoreCase("TECNICO")) {
                Global.mensaje = "El usuario no tiene permisos";
                return false;
            } else if (Global.STATUS.equalsIgnoreCase("INACTIVO")) {
                Global.mensaje = "El usuario está inactivo";
                return false;
            }
            return true;
        }
    }


    public static void packTramaCambioClave() {
        String tramaCompleta = "";

        //Head


    }

    public static void packHttpDataActPass() {


    }


 /*   public static void messageGuardar(int mensaje, String informacion, int tiempo) {
        Global.mensaje="";
        switch(mensaje) {

            case 1:
                GuardarMensaje("No se establecio");
                GuardarMensaje("conexion GPRS.");
                GuardarMensaje("<Intente mas tarde>");
                break;

            case 2:
                GuardarMensaje("No se establecio");
                GuardarMensaje("comunicacion con el");
                GuardarMensaje("servidor. ");
                GuardarMensaje("<Intente mas tarde>");
                break;

            default: break;
        }

        //if(tiempo!=null)
        //    Global.TimeMensaje=(tiempo);

    }*/

    /**
     * Este metodo convierte un array de Bytes a un objeto tipo String
     *
     * @retorna la cadena tipo String
     */
    public static String uninterpret_ASCII(byte[] rawData, int offset, int length) {
        char[] ret = new char[length];
        for (int i = 0; i < length; i++) {
            ret[i] = (char) rawData[offset + i];
        }
        return new String(ret);
    }


    private static void GuardarMensaje(String mensaje) {


        Global.mensaje += mensaje + "\n";


    }


}
