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
import java.util.Calendar;
import java.util.Random;
import java.util.TimeZone;

/**
 *
 * @author Yeison Sanchez
 */
public class Utils {

    /** Creates a new instance of Utils */
    public Utils() {
    }

    /** Este metodo realiza un retardo en milisegundos                          
     * @
     */
    public static void delay(long milisecs){
        long t0 = System.currentTimeMillis() + milisecs;

        while(System.currentTimeMillis() < t0);

    }

    /** Este metodo convierte un array de Bytes a un objeto tipo String                          
     * @retorna la cadena tipo String
     */
    public static String uninterpret_ASCII(byte[] rawData, int offset, int length){
        char[] ret = new char[length];
        for (int i = 0; i < length; i++)
        {
            ret[i] = (char)rawData[offset + i];
        }
        return new String(ret);
    }

    /*******************************************************************************
     Function:   replaceChar
     Description: reemplazar un caracter por otro en un buffer
     Input:    *outputData = buffer a modificar
     antiguo = caracter que se reemplaza
     nuevo= caracter con el que se reemplaza
     len= longitud a recorrer en el buffer
     Return:   Nothing
     *******************************************************************************/
    public static void replaceChar(byte[] outputData, byte antiguo , byte nuevo, int len){
        int i;

        for(i=0; i<len; i++)
            if(outputData[i] == antiguo)
                outputData[i] = nuevo;
    }

    /** Este metodo separa un array de bytes en tokens dependiendo del caracter recibido  
     * @Retorna un array de cadenas con los tokens separados
     */
    public static String[] tokenizer(byte[] array, int offset, int length, byte separator, int numTokens){
        String[] tokens = new String[numTokens];
        int i, len_tok, j=0;

        while(j < numTokens){
            len_tok=0;
            for(i=offset; i<length; i++){
                if(array[i] == separator){

                    tokens[j]= uninterpret_ASCII(array, offset, len_tok);

                    offset += (len_tok + 1);
                    j++;
                    break;
                }
                len_tok++;
            }
        }

        return tokens;
    }

    /** Este metodo separa un array de bytes en tokens dependiendo del tama�o (LV)  
     * @Retorna un array de cadenas con los tokens separados
     */
    public static String[] tokenizer(byte[] array, int offset, int numTokens){
        String[] tokens = new String[numTokens];
        int i;

        for(i=0; i<numTokens; i++){
            tokens[i]= uninterpret_ASCII(array, offset + 1, array[offset]);
            offset += (array[offset] + 1);
        }

        return tokens;
    }

    /** Este metodo obtiene la fecha y hora actual
     *  en el formato YYYYMMDDhhmmss  
     * @Retorna una cadena con el formato de fecha y hora
     */
    public static String getDateTime(){
        String day, month, year, hour, minute, second;
        TimeZone tz = TimeZone.getTimeZone("GMT-5");
        Calendar actualDateTime = Calendar.getInstance(tz);

        year= String.valueOf(actualDateTime.get(actualDateTime.YEAR));
        month= String.valueOf(actualDateTime.get(actualDateTime.MONTH) + 1);
        day= String.valueOf(actualDateTime.get(actualDateTime.DAY_OF_MONTH));
        hour= String.valueOf(actualDateTime.get(actualDateTime.HOUR_OF_DAY));
        minute= String.valueOf(actualDateTime.get(actualDateTime.MINUTE));
        second= String.valueOf(actualDateTime.get(actualDateTime.SECOND));

        if((actualDateTime.get(actualDateTime.MONTH) + 1) < 10)
            month= "0" + month ;
        if(actualDateTime.get(actualDateTime.DAY_OF_MONTH) < 10)
            day= "0" + day ;
        if(actualDateTime.get(actualDateTime.HOUR_OF_DAY) < 10)
            hour= "0" + hour ;
        if(actualDateTime.get(actualDateTime.MINUTE) < 10)
            minute= "0" + minute ;
        if(actualDateTime.get(actualDateTime.SECOND) < 10)
            second= "0" + second ;

        //year = year.substring(2,4);

        String dateTime = year + month + day + hour + minute + second;
        return dateTime;
    }


    public static String getDateTime2(){
        String day, month, year, hour, minute, second;
        TimeZone tz = TimeZone.getTimeZone("GMT-5");
        Calendar actualDateTime = Calendar.getInstance(tz);

        year= String.valueOf(actualDateTime.get(actualDateTime.YEAR));
        month= String.valueOf(actualDateTime.get(actualDateTime.MONTH) + 1);
        day= String.valueOf(actualDateTime.get(actualDateTime.DAY_OF_MONTH));
        hour= String.valueOf(actualDateTime.get(actualDateTime.HOUR_OF_DAY));
        minute= String.valueOf(actualDateTime.get(actualDateTime.MINUTE));
        second= String.valueOf(actualDateTime.get(actualDateTime.SECOND));

        if((actualDateTime.get(actualDateTime.MONTH) + 1) < 10)
            month= "0" + month ;
        if(actualDateTime.get(actualDateTime.DAY_OF_MONTH) < 10)
            day= "0" + day ;
        if(actualDateTime.get(actualDateTime.HOUR_OF_DAY) < 10)
            hour= "0" + hour ;
        if(actualDateTime.get(actualDateTime.MINUTE) < 10)
            minute= "0" + minute ;
        if(actualDateTime.get(actualDateTime.SECOND) < 10)
            second= "0" + second ;

        year = year.substring(2,4);

        String dateTime = year + month + day + hour + minute;
        return dateTime;
    }




    /*******************************************************************************
     Function        : getDate
     Description     : Recibe la fecha en formato Dia/mes/año (20/01/18)
     Return          : Fecha
     ******************************************************************************/
    public static String getDate(){
        String day, month, year, hour, minute, second;
        TimeZone tz = TimeZone.getTimeZone("GMT-5");
        Calendar actualDateTime = Calendar.getInstance(tz);

        year= String.valueOf(actualDateTime.get(actualDateTime.YEAR));
        month= String.valueOf(actualDateTime.get(actualDateTime.MONTH) + 1);
        day= String.valueOf(actualDateTime.get(actualDateTime.DAY_OF_MONTH));
        hour= String.valueOf(actualDateTime.get(actualDateTime.HOUR_OF_DAY));
        minute= String.valueOf(actualDateTime.get(actualDateTime.MINUTE));
        second= String.valueOf(actualDateTime.get(actualDateTime.SECOND));

        if((actualDateTime.get(actualDateTime.MONTH) + 1) < 10)
            month= "0" + month ;
        if(actualDateTime.get(actualDateTime.DAY_OF_MONTH) < 10)
            day= "0" + day ;
        if(actualDateTime.get(actualDateTime.HOUR_OF_DAY) < 10)
            hour= "0" + hour ;
        if(actualDateTime.get(actualDateTime.MINUTE) < 10)
            minute= "0" + minute ;
        if(actualDateTime.get(actualDateTime.SECOND) < 10)
            second= "0" + second ;

        year = year.substring(2,4);

        //String dateTime = year + month + day + hour + minute + second;
        String dateTime = day + "/" + month + "/" + year;
        return dateTime;
    }

    /*******************************************************************************
     Function        : getTime
     Description     : Recibe la hora en formato HH:MM (Hora:Minuto)
     Return          : Hora
     ******************************************************************************/
    public static String getTime(){

        String time;
        String day, month, year, hour, minute, second, ampm;
        TimeZone tz = TimeZone.getTimeZone("GMT-5");
        Calendar actualDateTime = Calendar.getInstance(tz);

        hour= String.valueOf(actualDateTime.get(actualDateTime.HOUR_OF_DAY));
        minute= String.valueOf(actualDateTime.get(actualDateTime.MINUTE));


        if(actualDateTime.get(actualDateTime.HOUR_OF_DAY) < 10)
            hour= "0" + hour ;
        if(actualDateTime.get(actualDateTime.MINUTE) < 10)
            minute= "0" + minute ;

        time = hour + ":" + minute;

        return time;
    }

    /*******************************************************************************
     Function        : obtenerMesLetras
     Description     : Recibe el mes en entero y lo convierte a cadena
     Input           : mes = mes en entero a convertir
     Return          : Cadena con fecha
     ******************************************************************************/
    public static String obtenerMesLetras(int mes){

        switch (mes){
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

    /** Obtiene un numero (int) aleatorio en un inervalo determinado
     *  int: 32 bits [ -2.147.483.648  a  2.147.483.647 ]
     * @desde = Limite inferior  
     * @hasta = Limite superior
     * @Retorna el numero aleatorio
     */
    public static int getRandomInt(int desde , int hasta){
        Random azar;
        azar = new Random(System.currentTimeMillis());
        return desde + Math.abs(azar.nextInt()) % (hasta - desde + 1);
    }


    /** Obtiene un numero (long) aleatorio en un inervalo determinado
     *  long: 64 bits [ -9.223.372.036.854.775.808  a  9.223.372.036.854.775.807 ]
     * @desde = Limite inferior  
     * @hasta = Limite superior
     * @Retorna el numero aleatorio
     */
    public static long getRandomLong(long desde , long hasta){
        Random azar;
        azar = new Random(System.currentTimeMillis());
        return desde + Math.abs(azar.nextLong()) % (hasta - desde + 1);
    }


   /**
    * Asigna al Buffer el caracter especificado en cada una de sus posiciones hasta completar size
    * @param Buffer: buffer de tipo byte que se va a modificar
    * @param caracter: caracter a agregar al buffer
    * @param size: Tama�o que se va copiar
    */
    public static void memSet(byte[] Buffer, byte caracter, int size ) {
        int i;

        for(i=0; i<size; i++){
            Buffer[i] = caracter;
        }
    }


    /**
     * Muestra la memoria de un boffer en hexa y ascii
     * @param Buffer: buffer que se va a mostrar
     * @param tam: Tama�o que se va a mostrar     
     */
    public static void dumpMemory(byte[] Buffer, int tam)
    {
        int i;

        byte[] BufferDisplay = new byte[tam];

        for(i=0; i<tam; i++){
            if( Buffer[i] >= 32 && Buffer[i] <= 126 )
                BufferDisplay[i] = Buffer[i];
            else
                BufferDisplay[i] = '.';
        }

        System.out.println("\n\n\n");

        for(i=0; i<tam; i++){

            //System.out.print( ISOUtil.unPadLeft( (ISOUtil.padleft(Integer.toHexString(Buffer[i]), 2, '0').toUpperCase() ) , 'F' ) + " " );

            System.out.print(ISOUtil.hexString(Buffer[i]) + " ");

            if( (i+1) % 16 == 0){
               System.out.print( "   "  + uninterpret_ASCII(BufferDisplay, i - 15, 16) );
               System.out.println();
            }
            else if(i+1 == tam){

               System.out.print( ISOUtil.padleft( "   ", 3*(16-tam%16), ' ' ) );
               System.out.print( "   " + uninterpret_ASCII(BufferDisplay, i - (tam%16) + 1, tam%16) );

               System.out.println();
            }

        }

        System.out.println();

        //System.out.println(ISOUtil.hexString(Buffer, 0, tam));        

    }

    /*******************************************************************************
     Function        : strLen
     Description     : Recibe un array de bytes y muestra la cantidad
     Input           : Buffer = Tamaño del byte
     Return          : Cantidad
     ******************************************************************************/
    public static int strLen(byte[] Buffer) {
        int i;

        for(i=0; i<Buffer.length; i++){
            if(Buffer[i] == 0x00)
                break;
        }

        return i;
    }


    /*******************************************************************************
     Function        : formatMiles
     Description     : Recibe una cadena y la convierte en formato miles
     Input           : cadena = Cadena a convertir
     Return          : cadena formateada
     ******************************************************************************/
    public static String formatMiles(String cadena){
        int i, k = 0;
        int j = 0;
        int tam;

        tam = cadena.length();

        byte[] cadena_orig = cadena.getBytes();
        byte[] cad_destino = new byte[50];

        i=tam/3;															// Calc�lo la cantidad de puntos de mil que se van a agregar
        if(i*3==tam) i--;
	k=0;

	for(j=tam-1; j>=0; j--){
            cad_destino[j+i]=cadena_orig[j];
            k++;
            if( (k/3)*3==k ){
                i--;

                if( (j+i) > 0 )
                    cad_destino[j+i]='.';
            }
	}

        return uninterpret_ASCII(cad_destino, 0, strLen(cad_destino) ) ;
    }

    /**
     * 弹出撤销框
     */
    private static int toByte(char c) {
        byte b = (byte)"0123456789ABCDEF".indexOf(c);
        return b;
    }
    public static byte[] hexStringToByteArray(String hex) {
        int len = hex.length() / 2;
        byte[] result = new byte[len];
        char[] achar = hex.toCharArray();

        for(int i = 0; i < len; ++i) {
            int pos = i * 2;
            result[i] = (byte)(toByte(achar[pos]) << 4 | toByte(achar[pos + 1]));
        }

        return result;
    }

    /*******************************************************************************
     Function        : extraerString
     Description     : Extrae digitos de una cadena
     Input           : s = Cadena a extraer
     Return          : cadena final
     ******************************************************************************/
    public static String extraerString(String s){
        String strfinal="";

        for(int i =0; i< s.length();i++) {
            strfinal +="3";
            strfinal += s.substring(i,i+1);

        }
        return strfinal;
    }

    /*******************************************************************************
     Function        : writeLOG
     Description     :
     Input           : data =
                       tipo =
     Return          : 0
     ******************************************************************************/
    public  static int writeLOG(String data, String tipo) {

        File extStore = Environment.getExternalStorageDirectory();
        String fileName = tipo+".txt";
        String path = extStore.getAbsolutePath() + "/" + fileName;
        try {
            File myFile = new File(path);
            myFile.createNewFile();
            FileOutputStream fOut = new FileOutputStream(myFile);
            OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
            myOutWriter.append(data);
            myOutWriter.close();
            fOut.close();

            //Toast.makeText(getApplicationContext(), fileName + " saved", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Log.e("Ficheros Reverso w", "Error al escribir fichero a memoria interna");
        }
        return 0;
    }

    /*******************************************************************************
     Function        : getImei
     Description     : Obtiene el IMEI de un dispositivo en Android
     Input           : c = Contexto
     Return          : IMEI
     ******************************************************************************/
    @SuppressLint("MissingPermission")
    public static String getImei(Context c) {
        TelephonyManager telephonyManager = (TelephonyManager) c
                .getSystemService(Context.TELEPHONY_SERVICE);

        return telephonyManager.getDeviceId();
    }

    /*******************************************************************************
     Function        : getIDSim
     Description     : Obtiene el ID de una SIMCARD en Android
     Input           : c = Contexto
     Return          : ID de la SIMCARD
     ********************************************** ********************************/
    @SuppressLint("MissingPermission")
    public static String getIDSim(Context c) {
        TelephonyManager telephonyManager = (TelephonyManager) c
                .getSystemService(Context.TELEPHONY_SERVICE);

        return telephonyManager.getSimSerialNumber();
    }


    /*******************************************************************************
     Function        : getSerialMaquina
     Description     : Obtiene el serial del dispositivo
     Input           : c = Contexto
     Return          : serial de la Maquina
     ******************************************************************************/
    public static String getSerialMaquina(Context context) {
        String serialMaquina = "";
        try {
            serialMaquina = android.os.Build.SERIAL;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return serialMaquina;
    }

    /*******************************************************************************
     Function        : getSerialJjpita
     Description     : Obtiene el serial de la perla
     Input           : c = Contexto
     Return          : serialPerla
     ******************************************************************************/
   /* public static String getSerialJjpita(Context c){

        String serialMaquina = getSerialMaquina(c);
        String serial = Global.PREFIJO + serialMaquina.substring(serialMaquina.length()-8, serialMaquina.length() );

        //return "72131008606"; // SERIAL PARA PRUEBAS EN OTROS DISPOSITIVOS
        //return "8010LM1102502195";
        return serial;
    }*/

    /*******************************************************************************
     Function        : contarCaracter
     Description     : Cuenta caracteres
     Input           : array = Array a dividir
                       offset = inicio
                       length = longitud
                       separator = separador
     Return          : numero de caracteres
     ******************************************************************************/
    public static int contarCaracter (byte[] array, int offset, int length, byte separator) {

        int i;
        int numCaracteres = 0;

        for (i = offset; i < length; i++) {
            if (array[i] == separator) {

                numCaracteres++;
            }
        }
        return numCaracteres;
    }

    // Metodo para convertir una hora en formato 12 a entero

    /*******************************************************************************
     Function        : timeStrToInt_12h
     Description     : Convierte una hora en formato 12 a entero
     Input           : horaLlegada = Hora a convertir
     Return          : hora convertida
     ******************************************************************************/
    public static int timeStrToInt_12h (String horaLlegada){
        String meridiano;
        String horas24 = "";

        meridiano = horaLlegada.substring(6,8);

        horaLlegada = horaLlegada.substring(0,5);
        horaLlegada = horaLlegada.replace(":","");

        if (meridiano.equals("PM") ){
            horas24 = String.valueOf( Integer.parseInt( horaLlegada.substring(0,2) ) + 12 );
            horaLlegada = horaLlegada.substring( 2, horaLlegada.length() );
        }

        horaLlegada = horas24 + horaLlegada;

        return Integer.parseInt(horaLlegada);
    }

    /*******************************************************************************
     Function        : timeStrToInt_24h
     Description     : Convierte una hora en formato 24 a entero
     Input           : horaLlegada = Hora a convertir
     Return          : hora convertida
     ******************************************************************************/
    public static int timeStrToInt_24h (String horaActual){

        horaActual = horaActual.substring(0,5);
        horaActual = horaActual.replace(":","");

        return Integer.parseInt(horaActual);
    }

    /*******************************************************************************
     Function        : DialogError
     Description     : Crea un dialogo de error
     Input           : c = Contexto
     Return          : Ninguno
     ******************************************************************************/
   /* public static void DialogError(Context c) {

        Global.MsgError = "Código: " + Global.tokens[1] + "\n" + "Descripción: " + Global.tokens[2];

        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(c,
                R.style.DialogColor));

        builder.setTitle("Error");
        builder.setMessage(Global.MsgError);
        builder.setCancelable(true);


        final Dialog dialog = builder.create();
        dialog.show();

        int titleDividerId = c.getResources().getIdentifier("titleDivider", "id", "android");
        View titleDivider = dialog.findViewById(titleDividerId);
        if (titleDivider != null)
            titleDivider.setBackgroundColor(c.getResources().getColor(R.color.colorPrimary));

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                dialog.dismiss();
            }
        },5000);

    }
*/
    /*******************************************************************************
     Function        : CustomAlertDialog
     Description     : Crea un dialogo customizado
     Input           : c = Contexto
                       titulo = Titulo del dialogo
                       msg = Mensaje del dialogo
                       tiempo = Tiempo del dialogo
     Return          : Ninguno
     ******************************************************************************/
   /* public static void CustomAlertDialog (Context c, String titulo, String msg, int tiempo){

        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(c,
                R.style.DialogColor));

        builder.setTitle(titulo);
        builder.setMessage(msg);
        builder.setCancelable(true);


        final Dialog dialog = builder.create();
        dialog.show();

        int titleDividerId = c.getResources().getIdentifier("titleDivider", "id", "android");
        View titleDivider = dialog.findViewById(titleDividerId);
        if (titleDivider != null)
            titleDivider.setBackgroundColor(c.getResources().getColor(colors.colorPrimary));

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                dialog.dismiss();
            }
        }, tiempo);
    }
*/
    /*******************************************************************************
    Function        : CustomAlertDialogImpresion
    Description     : Crea un dialogo customizado
    Input           : c = Contexto
                      titulo = Titulo del dialogo
                      msg = Mensaje del dialogo
                      tiempo = Tiempo del dialogo
    Return          : Ninguno
     ******************************************************************************/
    /*public static void CustomAlertDialogImpresion (Context c, String titulo, String msg, int tiempo){

        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(c,
                R.style.DialogColor));

        builder.setTitle(titulo);
        builder.setMessage(msg);
        builder.setCancelable(false);


        final Dialog dialog = builder.create();
        dialog.show();

        int titleDividerId = c.getResources().getIdentifier("titleDivider", "id", "android");
        View titleDivider = dialog.findViewById(titleDividerId);
        if (titleDivider != null)
            titleDivider.setBackgroundColor(c.getResources().getColor(R.color.colorPrimary));

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                dialog.dismiss();
            }
        }, tiempo);
    }*/

    /*******************************************************************************
     Function        : CustomToast
     Description     : Crea un toast customizado
     Input           : c = Contexto
                       titulo = Titulo del dialogo
                       msg = Mensaje del dialogo
                       duracion = Duración del dialogo
     Return          : Ninguno
     ******************************************************************************/
   /* public static void CustomToast(Context c, String titulo, String msg, int duracion){

            TextView textTitulo = (TextView) Global.layout.findViewById(R.id.textTitulo);
            textTitulo.setText(titulo);

            TextView textMensaje = (TextView) Global.layout.findViewById(R.id.textMensaje);
            textMensaje.setText(msg);

            Toast toast = new Toast(c);
            toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
            toast.setDuration(duracion);
            toast.setView(Global.layout);
            toast.show();
    }
*/
    /*******************************************************************************
     Function        : getDecimals
     Description     : Recibe un numero decimal y retorna una cadena con los digitos decimales deseados
     Input           : numero = numero a convertir
                       cantDecimales = Cantidad de decimales
     Return          : cadena con los decimales seleccionados
     ******************************************************************************/
   /* public static String getDecimals(String numero, int cantDecimales){
        String parteDecimal = "", parteDecimalRet = "";
        int numPuntos = 0, numTokens;

        numero = numero + ".";

        numPuntos = contarCaracter(numero.getBytes(),0,numero.getBytes().length,(byte) '.');

        numTokens = numPuntos;

        Global.tokens = tokenizer(numero.getBytes(), 0, numero.getBytes().length, (byte) '.', numTokens);

        if (numTokens > 1)
            parteDecimal = Global.tokens[1];

        //parteDecimal = ISOUtil.padleft(parteDecimal, cantDecimales, '0');
        if (parteDecimal.length() < cantDecimales)
            parteDecimal = ISOUtil.padRight(parteDecimal, cantDecimales, '0');

        parteDecimalRet = parteDecimal.substring(0,cantDecimales);

        return parteDecimalRet;
    }
*/
    /*******************************************************************************
     Function        : pintarLineaDialogo
     Description     : Pinta la linea de un AlertDialog de color verde
     Input           : c = Contexto
                       dialog = Dialogo a pintar
     Return          : cadena con los decimales seleccionados
     ******************************************************************************/
  /*  public static void pintarLineaDialogo(Context c, Dialog dialog) {

        int titleDividerId = c.getResources().getIdentifier("titleDivider", "id", "android");
        View titleDivider = dialog.findViewById(titleDividerId);
        if (titleDivider != null)
            titleDivider.setBackgroundColor(c.getResources().getColor(R.color.colorPrimary));
    }*/

    /*******************************************************************************
     Function        : colorizeDatePicker
     Description     : Pinta la linea de un DatePicker del color del colorPrimary
     Input           : datePicker = DatePicker a pintar
     Return          : Nothing
     ******************************************************************************/
  /*  public static void colorizeDatePicker(DatePicker datePicker) {
        Resources system = Resources.getSystem();
        int dayId = system.getIdentifier("day", "id", "android");
        int monthId = system.getIdentifier("month", "id", "android");
        int yearId = system.getIdentifier("year", "id", "android");

        NumberPicker dayPicker = (NumberPicker) datePicker.findViewById(dayId);
        NumberPicker monthPicker = (NumberPicker) datePicker.findViewById(monthId);
        NumberPicker yearPicker = (NumberPicker) datePicker.findViewById(yearId);

        setDividerColor(dayPicker);
        setDividerColor(monthPicker);
        setDividerColor(yearPicker);
    }
*/
    /*******************************************************************************
     Function        : setDividerColor
     Description     : pintador de la función colorizeDatePicker
     Input           : picker = NumberPicker a pintar
     Return          : Nothing
     ******************************************************************************/
   /* private static void setDividerColor(NumberPicker picker) {
        if (picker == null)
            return;

        final int count = picker.getChildCount();
        for (int i = 0; i < count; i++) {
            try {
                Field dividerField = picker.getClass().getDeclaredField("mSelectionDivider");
                dividerField.setAccessible(true);
                ColorDrawable colorDrawable = new ColorDrawable(picker.getResources().getColor(R.color.colorPrimary));
                dividerField.set(picker, colorDrawable);
                picker.invalidate();
            } catch (Exception e) {
                Log.w("setDividerColor", e);
            }
        }
    }
*/
    /*******************************************************************************
     Function        : format32CharsPerLine
     Description     : Reemplaza por un salto de linea al llegar a 40
     Input           : str = cadena de llegada
     Return          : Cadena a imprimir
     ******************************************************************************/
    public static String format32CharsPerLine(String str){
        String strRet = "";
        int i = 0;

        for (i=0; i < str.length() - 32; i += 32)
            strRet = strRet + str.substring(i, i + 32) + "\n";

        strRet = strRet + str.substring(i, str.length() );

        return strRet;
    }


    /*******************************************************************************
     Function        : replaceSpecialChars
     Description     : Reemplaza los caracteres especiales
     Input           :
     Return          :
     ******************************************************************************/
   /* public static void replaceSpecialChars (){

        replaceChar(Global.inputData, (byte) 0xF1, (byte) 'n', Global.inputData.length); // Cambio la ñ por n
        replaceChar(Global.inputData, (byte) 0xD1, (byte) 'N', Global.inputData.length); // Cambio la Ñ mayusculas por la N
        replaceChar(Global.inputData, (byte) 0xC1, (byte) 'A', Global.inputData.length); // Cambio la A tildada por A
        replaceChar(Global.inputData, (byte) 0xC9, (byte) 'E', Global.inputData.length); // Cambio la E tildada por E
        replaceChar(Global.inputData, (byte) 0xCD, (byte) 'I', Global.inputData.length); // Cambio la I tildada por I
        replaceChar(Global.inputData, (byte) 0xD3, (byte) '0', Global.inputData.length); // Cambio la 0 tildada por 0
        replaceChar(Global.inputData, (byte) 0xDA, (byte) 'U', Global.inputData.length); // Cambio la U tildada por U
        replaceChar(Global.inputData, (byte) 0xE1, (byte) 'a', Global.inputData.length); // Cambio la a tildada por a
        replaceChar(Global.inputData, (byte) 0xE9, (byte) 'e', Global.inputData.length); // Cambio la e tildada por e
        replaceChar(Global.inputData, (byte) 0xED, (byte) 'i', Global.inputData.length); // Cambio la i tildada por i
        replaceChar(Global.inputData, (byte) 0xF3, (byte) 'o', Global.inputData.length); // Cambio la o tildada por o
        replaceChar(Global.inputData, (byte) 0xFA, (byte) 'u', Global.inputData.length); // Cambio la u tildada por u
        replaceChar(Global.inputData, (byte) 0xBF, (byte) ' ', Global.inputData.length); // Cambio la ¿ tildada por " "
        replaceChar(Global.inputData, (byte) 0xA1, (byte) ' ', Global.inputData.length); // Cambio la ¡ tildada por " "
    }*/

    public static byte[] replaceSpecialChars(byte[] data, int cnt) {

        byte[] UTF8_n = new byte[]{ (byte) 0xC3, (byte) 0xB1};
        byte[] UTF8_N = new byte[]{ (byte) 0xC3, (byte) 0x91};
        byte[] UTF8_A = new byte[]{ (byte) 0xC3, (byte) 0x81};
        byte[] UTF8_E = new byte[]{ (byte) 0xC3, (byte) 0x89};
        byte[] UTF8_I = new byte[]{ (byte) 0xC3, (byte) 0x8D};
        byte[] UTF8_O = new byte[]{ (byte) 0xC3, (byte) 0x93};
        byte[] UTF8_U = new byte[]{ (byte) 0xC3, (byte) 0x9A};
        byte[] UTF8_a = new byte[]{ (byte) 0xC3, (byte) 0xA1};
        byte[] UTF8_e = new byte[]{ (byte) 0xC3, (byte) 0xA9};
        byte[] UTF8_i = new byte[]{ (byte) 0xC3, (byte) 0xAD};
        byte[] UTF8_o = new byte[]{ (byte) 0xC3, (byte) 0xB3};
        byte[] UTF8_u = new byte[]{ (byte) 0xC3, (byte) 0xBA};
        byte[] UTF8_int= new byte[]{ (byte) 0xC2, (byte) 0xBF};
        byte[] UTF8_adm= new byte[]{ (byte) 0xC2, (byte) 0xA1};

        cnt = replace_all_bytes_secuences(data, UTF8_n, "n".getBytes(), cnt, 2, 1);
        cnt = replace_all_bytes_secuences(data, UTF8_N, "N".getBytes(), cnt, 2, 1);
        cnt = replace_all_bytes_secuences(data, UTF8_A, "A".getBytes(), cnt, 2, 1);
        cnt = replace_all_bytes_secuences(data, UTF8_E, "E".getBytes(), cnt, 2, 1);
        cnt = replace_all_bytes_secuences(data, UTF8_I, "I".getBytes(), cnt, 2, 1);
        cnt = replace_all_bytes_secuences(data, UTF8_O, "O".getBytes(), cnt, 2, 1);
        cnt = replace_all_bytes_secuences(data, UTF8_U, "U".getBytes(), cnt, 2, 1);
        cnt = replace_all_bytes_secuences(data, UTF8_a, "a".getBytes(), cnt, 2, 1);
        cnt = replace_all_bytes_secuences(data, UTF8_e, "e".getBytes(), cnt, 2, 1);
        cnt = replace_all_bytes_secuences(data, UTF8_i, "i".getBytes(), cnt, 2, 1);
        cnt = replace_all_bytes_secuences(data, UTF8_o, "o".getBytes(), cnt, 2, 1);
        cnt = replace_all_bytes_secuences(data, UTF8_u, "u".getBytes(), cnt, 2, 1);
        cnt = replace_all_bytes_secuences(data, UTF8_int, " ".getBytes(), cnt, 2, 1);
        cnt = replace_all_bytes_secuences(data, UTF8_adm, " ".getBytes(), cnt, 2, 1);

        byte[] newData = new byte[cnt];
        System.arraycopy(data, 0, newData, 0, cnt);

        return newData;
    }
    /***
     Function:	  replace_all_bytes_secuences
     Description: reemplaza todas las ocurrencias de la secuencia de Bytes S2 en S1 por S3
     Input:		  S1 = array de Bytes base
     S2 = array de Bytes a buscar
     S3 = array de Bytes a reemplazar
     lenS1 = longitud del Array S1
     lenS2 = longitud del Array S2
     lenS3 = longitud del Array S3
     Return:	  longitud del nuevo array de Bytes S1
     ***/
    private static int replace_all_bytes_secuences(byte[] S1, byte[] S2, byte[] S3, int lenS1, int lenS2, int lenS3){
        int i=0, indice= 0;

        Global.lenS1 = lenS1;

        do{
            indice= replace_bytes_secuence(S1, S2, S3, lenS2, lenS3);

            if(indice != 0)
                i += indice;

        }while(indice != 0);

        return Global.lenS1;

    }

    /***
     Function:	  replace_bytes_secuence
     Description: reemplaza la primera ocurrencia de la secuencia de Bytes S2 en S1 por S3
     Input:		  S1 = array de Bytes base
     S2 = array de Bytes a buscar
     S3 = array de Bytes a reemplazar
     lenS1 = longitud del Array S1
     lenS2 = longitud del Array S2
     lenS3 = longitud del Array S3
     Return:	  retorna el indice de la posicion siguiente al campo reemplazado
     retorna 0 sino encontró la cadena
     ***/

    private static int replace_bytes_secuence(byte[] S1, byte[] S2, byte[] S3, int lenS2, int lenS3){
        byte[] buffer = new byte[Global.MAX_LEN_INPUTDATA];
        int i, lenBuffer;

        i = find_bytes_secuence(S1, S2, Global.lenS1, lenS2);

        if( i == -1 )
            return 0;

        lenBuffer =  (Global.lenS1) - (i + lenS2);

        System.arraycopy(S1, i + lenS2, buffer, 0, lenBuffer);

        System.arraycopy(S3, 0, S1, i, lenS3);

        System.arraycopy(buffer, 0, S1, i + lenS3, lenBuffer);

        Global.lenS1 = i + lenS3 + lenBuffer;

        return i + lenS3;
    }

    /***
     Function:	  find_bytes_secuence
     Description: encuentra la primera ocurrencia de la secuencia de Bytes S2 en S1
     Input:		  S1 = array de Bytes base
     S2 = array de Bytes a buscar
     lenS1 = longitud del Array S1
     lenS2 = longitud del Array S2
     Return:	  retorna el indice de la secuencia encontrada
     retorna -1 sino encontró la secuencia
     ***/
    public static int find_bytes_secuence(byte[] S1, byte[] S2, int lenS1, int lenS2){
        int i, j, len = 0;
        boolean primeraCoincidencia = false;

        for(i=0; i<lenS1; i++){

            len=0;
            for(j=0; j<lenS2; j++){
                if(S1[i + j] == S2[j]){
                    len++;
                    primeraCoincidencia = true;
                    if(len == lenS2)
                        return(i);
                }
                else{
                    if (primeraCoincidencia)
                        len = 0;

                    i+=len;
                    break;
                }
            }

        }

        return -1;

    }



    /*********
     Function:	  find_bytes_secuence
     Description: encuentra la primera ocurrencia de la secuencia de Bytes S2 en S1
     Input:		  S1 = array de Bytes base
     S2 = array de Bytes a buscar
     lenS1 = longitud del Array S1
     lenS2 = longitud del Array S2
     Return:	  retorna el indice de la secuencia encontrada
     retorna -1 sino encontró la secuencia
     *********/
  /*  public static int find_bytes_secuence(byte[] S1, byte[] S2, int lenS1, int lenS2){
        int i, j, len = 0;
        boolean primeraCoincidencia = false;

        for(i=0; i<lenS1; i++){

            len=0;
            for(j=0; j<lenS2; j++){
                if(S1[i + j] == S2[j]){
                    len++;
                    primeraCoincidencia = true;
                    if(len == lenS2)
                        return(i);
                }
                else{
                    if (primeraCoincidencia)
                        len = 0;

                    i+=len;
                    break;
                }
            }
        }

        return -1;

    }*/

    /***********************************************************************************************
     Function        : validate_battery
     Description     : Valida el estado de la bateria
     Input           : context = Contexto
     Return          : TRUE = Si la carga es mayor a la debida
     FALSE = Si la carga es menor a la debida
     **********************************************************************************************/
 /*   public static boolean validate_battery(Context context) {
        IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = context.registerReceiver(null, ifilter);

        int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);

        if (level <= Global.CARGA_MINIMA_BATERIA) {
            Global.levelBattery = false;
            return false;
        }

        Global.levelBattery = true;
        return true;
    }*/

    /***********************************************************************************************
     Function        : validate_paper
     Description     : Valida el papel de la impresora
     Input           : context = Contexto
     Return          : TRUE = Si hay papel
     FALSE = Si no hay papel
     **********************************************************************************************/
    /*public static boolean validate_paper(Context c) {
        int ret = 0;

        try {
            ret = Reports.validarImpresora(c);                                                      // Obtiene el estado de la impresora
        } catch (DeviceException e) {
            e.printStackTrace();
        }

        if ( ret == 1) {
            Global.TITULO_ERR_VALIDAR_IMPRESORA = "NO HAY PAPEL!!!";
            Global.MsgError = Global.MSG_ERR_PAPEL;                                             // Si la impresora no tiene papel
            Global.statusPaper = false;
            return false;

        } else {
            Global.statusPaper = true;
            return true;
        }
    }*/


    /*******************************************************************************
     Function    : validate_printer
     Description : Valida la impresora
     Input       : Nothing
     Return      : TRUE = Si la impresora esta lista
     FALSE = Si la impresora no esta lista
     *******************************************************************************/
  /*  public static boolean validate_printer(Context c) {

        if ( !Utils.validate_battery(c) ) {
            System.out.println("ENTRO AL FALSE BATERIA ******************");
            return false;
        } else {
            System.out.println("ENTRO AL TRUE BATERIA ******************");
        }

        if ( !validate_paper(c) ) {
            System.out.println("ENTRO AL FALSE PAPER ******************");
            return false;
        } else {
            System.out.println("ENTRO AL TRUE PAPER ******************");
        }

        return true;
    }*/

    /*******************************************************************************
     Function        : validateErrorsConexion
     Description     : Valida los errores de conexion
     Input           : continua = TRUE = Si continua con otra transaccion
     FALSE = Si no puede continuar
     errors   = Errores del TCP
     context  = Contexto
     Return          : 0 = Si retorna dialogo de error
     -1 = Si retorna actividad de error
     ******************************************************************************/
   public static int validateErrorsConexion(boolean continua, int errors, Context context){

        switch (errors) {
            case Global.ERR_DATA_RECEIVED:

                Global.mensaje="Error \n No se ha podido descargar el producto";
                if (continua)
                    return 0;
                else
                    return 1;

            default:
                Global.StatusExit = false;
                return  -1;
        }
    }
   /*******************************************************************************
     Método       : GoToNextActivity
     Description  : Se dirige a otra actividad
     Input        : activity = Clase donde se encuentra
                    clase = Clase a la que se dirige
                    finaliza = TRUE: Finaliza la clase
                               FALSE: No Finaliza la clase
     Return       : nothing
     *******************************************************************************/
    public static void GoToNextActivity(Activity activity, Class clase, boolean finaliza)
    {
        Intent intent = new Intent(activity, clase );
        activity.startActivity(intent);

        if (finaliza){
            activity.finish();
        }
    }
}
