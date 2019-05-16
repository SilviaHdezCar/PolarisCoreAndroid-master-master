package com.example.wposs_user.polariscoreandroid.Comun;

import android.widget.ImageView;

import com.example.wposs_user.polariscoreandroid.java.Observacion;
import com.example.wposs_user.polariscoreandroid.java.Repuesto;
import com.example.wposs_user.polariscoreandroid.java.Terminal;
import com.example.wposs_user.polariscoreandroid.java.Tipificacion;
import com.example.wposs_user.polariscoreandroid.java.Validacion;

import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Global {



    public static String correo;
    public static String password;
    public static String serial= "";
    public static String modelo= "";
    public static String codigo_rep = "";
    public static String validar_actual= "";//es la que se envia al serviso al validar la clave

    public static Observacion obs;




    public static String serial_ter= "";



    public static String diagnosticoTerminal= "";//es asignado al dar clic en el btn asociadas o autorizadas, es utilizado en tipificaciones, para consumir el servicio de diag asociada o reparacion


    public static String opcion_consulta= "";




    public static String foto1_etapa_ter= "";
    public static String foto2_etapa_ter= "";

    public static String primaryIP;
    public static int primaryPort;

    public static  String mensaje ="";

    public static Socket tcpSocket = null;

    //variables don se guarda lo que recibe de la trama al iniciar sesion correctamente
    public static String TOKEN;
    public static  String MESSAGE;
    public static String ROL;
    public static  String LOGIN;
    public static String ID;
    public static String STATUS;
    public static String POSITION;
    public static String CODE;
    public static String NOMBRE = "";
    public static String EMAIL = "";
    public static String LOCATION = "";
    public static String PHONE = "";
    public static String PHOTO = "";
    public static ImageView foto_perfil;





    public static String STATUS_SERVICE;


    //variables donde se guarda lo que recibe de la trama al intentar iniciar sesion
    public static String DESCRIPCION_ERROR;




    public static final byte PIPE ='|';
    public static  String [] tokens =null;
    public static  int tamañoTrama;

    public static  String    WEB_SERVICE          = "";

    public static final String    INITIAL_IP          = "100.25.214.91";
    public static final int    INITIAL_PORT           = 3000;

    public static final String    HTTP_HEADER1       = "Content-Type: application/json";
    public static   final String    HTTP_HEADER2        ="Host: ";
    public static final String    HTTP_HEADER3        = "Content-Length: ";
    public static final String    HTTP_HEADER6        = "content-length: ";

    public static final int       SOCKET_TIMEOUT          = 20000;

    public static final int       TRANSACTION_OK          = 1;
     public static final int       ERR_OPEN_SOCKET         = -1;
    public static final int       ERR_WRITE_SOCKET        = -2;
    public static final int       ERR_TIMEOUT_SOCKET      = -3;
    public static final int       ERR_READ_SOCKET         = -4;
    public static final int       ERR_DATA_RECEIVED       = -5;


    public static final int       MAX_LEN_OUTPUTDATA	  = 10 * 1024;
//  public static final int       MAX_LEN_OUTPUTDATA	  = 2048;
    public static final int       MAX_LEN_INPUTDATA		  = 128 * 1024;
//  public static final int       MAX_LEN_INPUTDATA		  = 2 * 4096;




    public static String    httpDataBufferAux       = "";
    public static String    httpDataBuffer       = "";
    public static String    httpHeaderBuffer     = "";


    public static int            inputLen;
    public static int outputLen;

    public static byte[]         outputData               = new byte[MAX_LEN_OUTPUTDATA];
    public static byte[]         inputData                = new byte[MAX_LEN_INPUTDATA];
    public static byte[]         inputDataTemp            = new byte[MAX_LEN_INPUTDATA];
    //public static byte[]         inputData                = null;


    public static boolean enSesion=false;

    public static boolean         StatusExit              = false;

    public static String MsgError;
    public static final String MSG_ERR_CONEXION        = "Error de Conexión: No se estableció comunicación con el servidor, revise la configuración de Datos Móviles o WIFI";




    public static List<Terminal> TODAS_TERMINALES;


    public static List<Terminal> TERMINALES_ASOCIADAS ;
    public static List<Observacion> OBSERVACIONES;
    public static List<Validacion> VALIDACIONES;
    public static List<Tipificacion> TIPIFICACIONES;
    public static ArrayList<Repuesto> REPUESTOS = new ArrayList<>();
    public static ArrayList<Repuesto> REPUESTOS_DIAGONOSTICO= new ArrayList<>();


    public static ArrayList<Observacion>observaciones_con_fotos;


    public static ArrayList<Repuesto>REPUESTOS_DEFECTUOSOS_AUTORIZADAS;
    public static ArrayList<Repuesto>REPUESTOS_DEFECTUOSOS_SOLICITAR;


    public static ArrayList<Validacion> VALIDACIONES_DIAGNOSTICO;
    public static ArrayList<Tipificacion> TIPIFICACIONES_DIAGNOSTICO;
    public static ArrayList<Tipificacion> lista_tipificaciones_tabla=new ArrayList<Tipificacion>();//utilizado para llenar la tabla de tipificaciones asociadas
    public static ArrayList<Tipificacion> listTipificaciones ;//utilizado
    public static String reparable="";
    public static String fallaDetectada="";

    public static List<Terminal> TERMINALES_AUTORIZADAS ;


    public static int lenS1;
    public static String claveNueva;

    ///Variables Terminales Autorizadas
    public static String repuestosAutorizadas = "";
    public static String validacionesAutorizadas = "";
    public static String tipificacionesAutorizadas = "";
    public static Terminal terminalVisualizar;
    public static Object validaciones_terminal_autorizadas;

   public static Map<String, String>validaciones_listar_autorizadas;
   public static Map<String, String>tipificaciones_listar_autorizadas ;
   public static Map<String, String>repuestos_listar_autorizadas ;
}
