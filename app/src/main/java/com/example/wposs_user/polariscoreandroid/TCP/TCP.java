package com.example.wposs_user.polariscoreandroid.TCP;


import android.util.Log;


import com.example.wposs_user.polariscoreandroid.Comun.Global;
import com.example.wposs_user.polariscoreandroid.Comun.Messages;
import com.example.wposs_user.polariscoreandroid.Comun.Utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.sql.SQLOutput;
import java.util.StringTokenizer;

import static com.example.wposs_user.polariscoreandroid.Comun.Utils.uninterpret_ASCII;


/**
 * Created by Yeison Sanchez on 31/01/2019.
 */

public class TCP {
    /***************************
     Function        : transaction
     Description     : Realiza la transacción
     Input           : len = Longitud
     Return          : TRUE = Retorna transacción ok, FALSE = Catch Exception
     **************************/
    public static int transaction(int len){

        String received = "";
        PrintStream output;
        InputStream input;
        //byte[]  data = null;

        //Se conecta al servidor
        if ( tcpOpenSocket() == false){
            return Global.ERR_OPEN_SOCKET;
        }

        System.out.println("--------------------------------------------------------------------");
        System.out.println("Enviando ...");
        System.out.println(" URL: " + Global.primaryIP + ":" + Global.primaryPort);
        Utils.dumpMemory(Global.outputData, len);
        System.out.println("--------------------------------------------------------------------");

        try {
            output = new PrintStream(Global.tcpSocket.getOutputStream());//lo quee etstav enviando

            //envia peticion
            output.write(Global.outputData, 0, len);
        } catch (IOException e) {
            // Cierra el socket si hay falla en el envio
            disconnect();
            //Log.e("E/TCP Client 2 :", "" + ex.getMessage());
            System.out.println("Error Enviando - getOutputStream"+ e.getMessage() );
            return Global.ERR_WRITE_SOCKET;
        }

        try {
            Global.tcpSocket.setSoTimeout(Global.SOCKET_TIMEOUT);
        } catch (SocketException e) {
            System.out.println("Error setSoTimeout"+ e.getMessage() );
            return Global.ERR_TIMEOUT_SOCKET;
        }

        try {
            /*input = Global.tcpSocket.getInputStream();
            //recibe respuesta
            Global.inputLen = input.read(Global.inputData);*/

            int ciclo =1;
            int total_length = 0;
            int indice = 0;

            input = Global.tcpSocket.getInputStream();

            //
            System.out.println("INPUTLEN"  +Global.inputLen);
            while ((Global.inputLen = input.read(Global.inputDataTemp)) != -1 ) {//este no funciona cuando se se logue mal y luegio ingresa bien

                //Utils.dumpMemory(Global.inputDataTemp,Global.inputLen);

                System.arraycopy(Global.inputDataTemp, 0, Global.inputData, indice, Global.inputLen);
                indice += Global.inputLen;

                Global.inputLen = indice;

                byte[]  data =  Utils.replaceSpecialChars(Global.inputData, Global.inputLen);
                System.arraycopy(data, 0, Global.inputData, 0, data.length);

                Global.inputLen = data.length;


                System.out.println("CICLO ********************" + ciclo);
                System.out.println("Global.inputLen "+Global.inputLen);

                if(ciclo==1) {

                    if(!valida_http()){
                        System.out.println("valida_http(): "+false);
                        disconnect();
                        return -1;
                    }

                    total_length = Utils.calculateTotalLength();
                }

                if( total_length == indice) {
                    System.out.println("total_length == indice ");
                    break;
                }
                ciclo++;
            }



        } catch (IOException e) {
            System.out.println("Error getInputStream"+ e.getMessage() );
            return Global.ERR_READ_SOCKET;
        }
        System.out.println("INPUTLEN2"  +Global.inputLen);
        if (Global.inputLen > 0) {
            //Global.inputData=Utils.replaceSpecialChars(Global.inputData, Global.inputData.length);
            /*
            byte[]  data=  Utils.replaceSpecialChars(Global.inputData, Global.inputLen);
            System.arraycopy(data, 0, Global.inputData, 0, data.length);

            Global.inputLen = data.length;
            */
           System.out.println("---------------------------ENTRÓ A RECCIBIR-----------------------------------------");
            System.out.println("Recibido: ");
           Utils.dumpMemory(Global.inputData, Global.inputLen);
            System.out.println("---------------------------fin dumpMemory  TCP-----------------------------------------");


            //System.out.println("*********************************************************************************outpu");
            // Reemplaza los caracteres especiales
            //Utils.replaceSpecialChars(Global.inputData, Global.inputData.length);            // Reemplaza los caracteres especiales

            /*
            if(!valida_http()){
                return -1;
            }
            */

        }


       /*if (Global.pingActivo){
           Global.timerPing.cancel();
           Global.timerPing.start();
       }*/

       // Si la conexiobn es por demanda entonces cierro el socket
       disconnect();

       return Global.TRANSACTION_OK;

    }


    private static  boolean valida_http(){

        String statusLine = "";
        String aux = uninterpret_ASCII(Global.inputData, 0, Global.inputData.length);
        //Log.d("VALIDA INPUT", "VALIDANDO--"+aux);

       String [] data = aux.split("\n");
       statusLine = data[0];
        //Log.i("----STATUS:--", "boolean valida_http() ");

       if(!validaErrorHttp(statusLine)){
        return false;
       }

       Global.httpDataBuffer = data[data.length-1];


        //Log.d("VALIDA INPUT", "VALIDANDO HEADER--"+data[0]);
        //Log.d("VALIDA INPUT", "VALIDANDO DATA--"+data[data.length-1]);
        return true;
    }



    private static boolean validaErrorHttp(String status){

        String ok_status = "200";
        String []aux = status.split(" ");

        Log.i("----STATUS:--", "mETODO private static boolean validaErrorHttp: "+aux[1]);

        if(aux[1].equals("400")){
            Global.mensaje="ERROR \n  al establecer la conexión con el servidor\n la estructura de los datos es incorrecta";
            return false;
        }else     if(!aux[1].equals(ok_status)){
            Global.mensaje="ERROR \n Problemas de HTTP";

            Log.i("ValidarError", ""+Global.mensaje);
            // Caja cerrada
            return false;
        }
      return true;
    }


    /***************************
     Function        : checkConex
     Description     : Revisa la conexión de la transacción
     Return          : TRUE = Retorna transacción ok, FALSE = Retorna false
     **************************/
    public static boolean checkConex (){

        if ( transaction(0) == Global.TRANSACTION_OK){
            return true;
        } else{
            return false;
        }

    }

    /***************************
     Function        : disconnect
     Description     : Desconecta el socket
     Return          : TRUE = Retorna socket cerrado, FALSE = Catch Exception
     **************************/
    public static boolean disconnect(){

        try{

            Global.tcpSocket.close();
            return true;

        } catch (IOException ex) {
            System.out.println("Error, cerrando socket ---" + ex.getMessage() );
            Log.e("Error, cerrando socket:", "" + ex.getMessage());
            return false;
        }
    }

    /***************************
     Function        : tcpOpenSocket
     Description     : Crea y conecta un nuevo socket
     Return          : TRUE = Retorna socker abeirto, FALSE = Catch Exception
     **************************/
    public static boolean tcpOpenSocket(){


        if( Global.tcpSocket == null || Global.tcpSocket.isClosed() ){

            System.out.println(" *************** Creando nuevo socket");
            Global.tcpSocket = new Socket();
        }

        try{

            System.out.println(" *************** Antes de conectar");

            if ( Global.tcpSocket.isConnected() == false ) {

                System.out.println(" *************** Conectando socket");
                Global.tcpSocket.connect(new InetSocketAddress(Global.primaryIP, Global.primaryPort), Global.SOCKET_TIMEOUT);
            }

        } catch (IOException ex) {
            System.out.println("Error, tcpOpenSocket ---" + ex.getMessage() );
            System.out.println("*************** Error, tcpOpenSocket" + "" + ex.getMessage());
            disconnect();
            return false;
        }

        return true;
    }
}