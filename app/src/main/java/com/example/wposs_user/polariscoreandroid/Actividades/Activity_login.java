package com.example.wposs_user.polariscoreandroid.Actividades;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.wposs_user.polariscoreandroid.Comun.Global;
import com.example.wposs_user.polariscoreandroid.Comun.Messages;
import com.example.wposs_user.polariscoreandroid.Comun.Utils;
import com.example.wposs_user.polariscoreandroid.R;
import com.example.wposs_user.polariscoreandroid.TCP.TCP;

public class Activity_login extends AppCompatActivity {

    private EditText txtCorreo;
    private EditText txtPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        txtCorreo = (EditText) findViewById(R.id.txtCorreo);
        txtPass = (EditText) findViewById(R.id.txtPass);
        StringBuilder str=new StringBuilder();

   /*     for(int i=0; i<Global.inputData.length;i++){
          //  str.append("{\"message\":\"success\",\"status\":\"ok\",\"data\":{\"terminales\":[{\"term_serial\":\"123456831\",\"term_brand\":\"SPECTRA\",\"term_buy_date\":\"01/25/2019 08:00\",\"term_date_finish\":\"2019-07-26T13:00:00.000Z\",\"term_date_register\":\"May 2, 2019 12:08 PM\",\"term_imei\":\" \",\"term_localication\":\"SHERNANDEZ4\",\"term_mk\":\" \",\"term_model\":\"T1000A5\",\"term_num_terminal\":\"000042\",\"term_register_by\":\"EPRUEBAS23\",\"term_security_seal\":\" \",\"term_start_date_warranty\":\"2019-01-26T13:00:00.000Z\",\"term_status\":\"PREDIAGNÓSTICO\",\"term_status_temporal\":\"0\",\"term_technology\":\" DIAL,LAN,GPRS\",\"term_warranty_time\":\"6\"}]}}");
            str.append("{\"message\":\"success\",\"status\":\"ok\",\"data\":{\"terminales\":[{\"term_serial\":\"123456833\",\"term_brand\":\"SPECTRA\",\"term_buy_date\":\"01/25/2019 08:00\",\"term_date_finish\":\"2019-07-26T13:00:00.000Z\",\"term_date_register\":\"May 2, 2019 12:08 PM\",\"term_imei\":\" \",\"term_localication\":\"SHERNANDEZ4\",\"term_mk\":\" \",\"term_model\":\"T1000\",\"term_num_terminal\":\"000044\",\"term_register_by\":\"EPRUEBAS23\",\"term_security_seal\":\" \",\"term_start_date_warranty\":\"2019-01-26T13:00:00.000Z\",\"term_status\":\"PREDIAGNÓSTICO\",\"term_status_temporal\":\"0\",\"term_technology\":\" WIFI\",\"term_warranty_time\":\"6\"},{\"term_serial\":\"123456825\",\"term_brand\":\"SPECTRA\",\"term_buy_date\":\"01/25/2019 08:00\",\"term_date_finish\":\"2019-07-26T13:00:00.000Z\",\"term_date_register\":\"May 2, 2019 12:08 PM\",\"term_imei\":\" \",\"term_localication\":\"SHERNANDEZ4\",\"term_mk\":\" \",\"term_model\":\"T1000A5\",\"term_num_terminal\":\"000036\",\"term_register_by\":\"EPRUEBAS23\",\"term_security_seal\":\" \",\"term_start_date_warranty\":\"2019-01-26T13:00:00.000Z\",\"term_status\":\"PREDIAGNÓSTICO\",\"term_status_temporal\":\"0\",\"term_technology\":\" DIAL,LAN,GPRS\",\"term_warranty_time\":\"6\"},{\"term_serial\":\"123456823\",\"term_brand\":\"SPECTRA\",\"term_buy_date\":\"01/25/2019 08:00\",\"term_date_finish\":\"2019-07-26T13:00:00.000Z\",\"term_date_register\":\"May 2, 2019 12:08 PM\",\"term_imei\":\" \",\"term_localication\":\"SHERNANDEZ4\",\"term_mk\":\" \",\"term_model\":\"T700\",\"term_num_terminal\":\"000034\",\"term_register_by\":\"EPRUEBAS23\",\"term_security_seal\":\" \",\"term_start_date_warranty\":\"2019-01-26T13:00:00.000Z\",\"term_status\":\"PREDIAGNÓSTICO\",\"term_status_temporal\":\"0\",\"term_technology\":\" LAN\",\"term_warranty_time\":\"6\"},{\"term_serial\":\"123456834\",\"term_brand\":\"SPECTRA\",\"term_buy_date\":\"01/25/2019 08:00\",\"term_date_finish\":\"2019-07-26T13:00:00.000Z\",\"term_date_register\":\"May 2, 2019 12:08 PM\",\"term_imei\":\" \",\"term_localication\":\"SHERNANDEZ4\",\"term_mk\":\" \",\"term_model\":\"T1000A5\",\"term_num_terminal\":\"000045\",\"term_register_by\":\"EPRUEBAS23\",\"term_security_seal\":\" \",\"term_start_date_warranty\":\"2019-01-26T13:00:00.000Z\",\"term_status\":\"PREDIAGNÓSTICO\",\"term_status_temporal\":\"0\",\"term_technology\":\" DIAL\",\"term_warranty_time\":\"6\"},{\"term_serial\":\"123456822\",\"term_brand\":\"SPECTRA\",\"term_buy_date\":\"01/25/2019 08:00\",\"term_date_finish\":\"2019-07-26T13:00:00.000Z\",\"term_date_register\":\"May 2, 2019 12:08 PM\",\"term_imei\":\" \",\"term_localication\":\"SHERNANDEZ4\",\"term_mk\":\" \",\"term_model\":\"T1000A5\",\"term_num_terminal\":\"000033\",\"term_register_by\":\"EPRUEBAS23\",\"term_security_seal\":\" \",\"term_start_date_warranty\":\"2019-01-26T13:00:00.000Z\",\"term_status\":\"PREDIAGNÓSTICO\",\"term_status_temporal\":\"0\",\"term_technology\":\" DIAL\",\"term_warranty_time\":\"6\"},{\"term_serial\":\"123456827\",\"term_brand\":\"SPECTRA\",\"term_buy_date\":\"01/25/2019 08:00\",\"term_date_finish\":\"2019-07-26T13:00:00.000Z\",\"term_date_register\":\"May 2, 2019 12:08 PM\",\"term_imei\":\" \",\"term_localication\":\"SHERNANDEZ4\",\"term_mk\":\" \",\"term_model\":\"T1000\",\"term_num_terminal\":\"000038\",\"term_register_by\":\"EPRUEBAS23\",\"term_security_seal\":\" \",\"term_start_date_warranty\":\"2019-01-26T13:00:00.000Z\",\"term_status\":\"PREDIAGNÓSTICO\",\"term_status_temporal\":\"0\",\"term_technology\":\" WIFI\",\"term_warranty_time\":\"6\"},{\"term_serial\":\"123456838\",\"term_brand\":\"SPECTRA\",\"term_buy_date\":\"01/25/2019 08:00\",\"term_date_finish\":\"2019-07-26T13:00:00.000Z\",\"term_date_register\":\"May 2, 2019 12:08 PM\",\"term_imei\":\" \",\"term_localication\":\"SHERNANDEZ4\",\"term_mk\":\" \",\"term_model\":\"T700\",\"term_num_terminal\":\"000049\",\"term_register_by\":\"EPRUEBAS23\",\"term_security_seal\":\" \",\"term_start_date_warranty\":\"2019-01-26T13:00:00.000Z\",\"term_status\":\"PREDIAGNÓSTICO\",\"term_status_temporal\":\"0\",\"term_technology\":\" LAN,GPRS,WIFI\",\"term_warranty_time\":\"6\"},{\"term_serial\":\"123456828\",\"term_brand\":\"SPECTRA\",\"term_buy_date\":\"01/25/2019 08:00\",\"term_date_finish\":\"2019-07-26T13:00:00.000Z\",\"term_date_register\":\"May 2, 2019 12:08 PM\",\"term_imei\":\" \",\"term_localication\":\"SHERNANDEZ4\",\"term_mk\":\" \",\"term_model\":\"T1000A5\",\"term_num_terminal\":\"000039\",\"term_register_by\":\"EPRUEBAS23\",\"term_security_seal\":\" \",\"term_start_date_warranty\":\"2019-01-26T13:00:00.000Z\",\"term_status\":\"PREDIAGNÓSTICO\",\"term_status_temporal\":\"0\",\"term_technology\":\" DIAL\",\"term_warranty_time\":\"6\"},{\"term_serial\":\"123456837\",\"term_brand\":\"SPECTRA\",\"term_buy_date\":\"01/25/2019 08:00\",\"term_date_finish\":\"2019-07-26T13:00:00.000Z\",\"term_date_register\":\"May 2, 2019 12:08 PM\",\"term_imei\":\" \",\"term_localication\":\"SHERNANDEZ4\",\"term_mk\":\" \",\"term_model\":\"T1000A5\",\"term_num_terminal\":\"000048\",\"term_register_by\":\"EPRUEBAS23\",\"term_security_seal\":\" \",\"term_start_date_warranty\":\"2019-01-26T13:00:00.000Z\",\"term_status\":\"PREDIAGNÓSTICO\",\"term_status_temporal\":\"0\",\"term_technology\":\" DIAL,LAN,GPRS\",\"term_warranty_time\":\"6\"},{\"term_serial\":\"123456829\",\"term_brand\":\"SPECTRA\",\"term_buy_date\":\"01/25/2019 08:00\",\"term_date_finish\":\"2019-07-26T13:00:00.000Z\",\"term_date_register\":\"May 2, 2019 12:08 PM\",\"term_imei\":\" \",\"term_localication\":\"SHERNANDEZ4\",\"term_mk\":\" \",\"term_model\":\"T700\",\"term_num_terminal\":\"000040\",\"term_register_by\":\"EPRUEBAS23\",\"term_security_seal\":\" \",\"term_start_date_warranty\":\"2019-01-26T13:00:00.000Z\",\"term_status\":\"PREDIAGNÓSTICO\",\"term_status_temporal\":\"0\",\"term_technology\":\" LAN\",\"term_warranty_time\":\"6\"},{\"term_serial\":\"123456832\",\"term_brand\":\"SPECTRA\",\"term_buy_date\":\"01/25/2019 08:00\",\"term_date_finish\":\"2019-07-26T13:00:00.000Z\",\"term_date_register\":\"May 2, 2019 12:08 PM\",\"term_imei\":\" \",\"term_localication\":\"SHERNANDEZ4\",\"term_mk\":\" \",\"term_model\":\"T700\",\"term_num_terminal\":\"000043\",\"term_register_by\":\"EPRUEBAS23\",\"term_security_seal\":\" \",\"term_start_date_warranty\":\"2019-01-26T13:00:00.000Z\",\"term_status\":\"PREDIAGNÓSTICO\",\"term_status_temporal\":\"0\",\"term_technology\":\" LAN,GPRS,WIFI\",\"term_warranty_time\":\"6\"},{\"term_serial\":\"123456835\",\"term_brand\":\"SPECTRA\",\"term_buy_date\":\"01/25/2019 08:00\",\"term_date_finish\":\"2019-07-26T13:00:00.000Z\",\"term_date_register\":\"May 2, 2019 12:08 PM\",\"term_imei\":\" \",\"term_localication\":\"SHERNANDEZ4\",\"term_mk\":\" \",\"term_model\":\"T700\",\"term_num_terminal\":\"000046\",\"term_register_by\":\"EPRUEBAS23\",\"term_security_seal\":\" \",\"term_start_date_warranty\":\"2019-01-26T13:00:00.000Z\",\"term_status\":\"PREDIAGNÓSTICO\",\"term_status_temporal\":\"0\",\"term_technology\":\" LAN\",\"term_warranty_time\":\"6\"},{\"term_serial\":\"123456830\",\"term_brand\":\"SPECTRA\",\"term_buy_date\":\"01/25/2019 08:00\",\"term_date_finish\":\"2019-07-26T13:00:00.000Z\",\"term_date_register\":\"May 2, 2019 12:08 PM\",\"term_imei\":\" \",\"term_localication\":\"SHERNANDEZ4\",\"term_mk\":\" \",\"term_model\":\"T1000\",\"term_num_terminal\":\"000041\",\"term_register_by\":\"EPRUEBAS23\",\"term_security_seal\":\" \",\"term_start_date_warranty\":\"2019-01-26T13:00:00.000Z\",\"term_status\":\"PREDIAGNÓSTICO\",\"term_status_temporal\":\"0\",\"term_technology\":\" GPRS\",\"term_warranty_time\":\"6\"},{\"term_serial\":\"123456831\",\"term_brand\":\"SPECTRA\",\"term_buy_date\":\"01/25/2019 08:00\",\"term_date_finish\":\"2019-07-26T13:00:00.000Z\",\"term_date_register\":\"May 2, 2019 12:08 PM\",\"term_imei\":\" \",\"term_localication\":\"SHERNANDEZ4\",\"term_mk\":\" \",\"term_model\":\"T1000A5\",\"term_num_terminal\":\"000042\",\"term_register_by\":\"EPRUEBAS23\",\"term_security_seal\":\" \",\"term_start_date_warranty\":\"2019-01-26T13:00:00.000Z\",\"term_status\":\"PREDIAGNÓSTICO\",\"term_status_temporal\":\"0\",\"term_technology\":\" DIAL,LAN,GPRS\",\"term_warranty_time\":\"6\"}]}}");
        }

        System.out.println("-------------CADENA ÑLARGA");
        System.out.println(str.length());*/

    }

    public void iniciarSesion(View view) {
        String correo = this.txtCorreo.getText().toString();
        String pass = this.txtPass.getText().toString();

        if (correo.isEmpty() && pass.isEmpty()) {
            Toast.makeText(this, "Ingrese correo y contraseña", Toast.LENGTH_SHORT).show();
            return;
        }
        if (correo.isEmpty()) {
            Toast.makeText(this, "Debe ingresar el correo", Toast.LENGTH_SHORT).show();
            return;
        }
        if (pass.isEmpty()) {
            Toast.makeText(this, "Ingrese la contraseña", Toast.LENGTH_SHORT).show();
            return;
        } else {
            Global.WEB_SERVICE = "/PolarisCore/Users/login";
            Global.primaryIP = Global.INITIAL_IP;
            Global.primaryPort = Global.INITIAL_PORT;

            Global.correo = correo;
            Global.password = pass;

            new TaskLogin().execute();//hacer la peticion


        }

    }


    /*******************************************************************************
     Clase       : TaskLogin
     Description : Realiza la transacción de los parámetros del Login
     *******************************************************************************/


    class TaskLogin extends AsyncTask<String, Void, Boolean> {
        ProgressDialog progressDialog;
        int trans = 0;



        /*******************************************************************************
         Método       : onPreExecute
         Description  : Se ejecuta antes de realizar el proceso, muestra una ventana con uin msj de espera
         *******************************************************************************/


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(Activity_login.this, R.style.MyAlertDialogStyle);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setCancelable(false);
            progressDialog.setMessage("Validando credenciales...");
            progressDialog.show();
        }


        /*******************************************************************************
         Método       : doInBackground
         Description  : Se ejecuta para realizar la transacción y verificar coenxión
         *******************************************************************************/
        @Override
        protected Boolean doInBackground(String... strings) {
            Messages.packMsgLogin();

            trans = TCP.transaction(Global.outputLen);

            // Verifica la transacción
            if (trans == Global.TRANSACTION_OK)
                return true;
            else
                return false;
        }

        /*******************************************************************************
         Método       : onPostExecute
         Description  : Se ejecuta después de realizar el doInBackground
         *******************************************************************************/
        @Override
        protected void onPostExecute(Boolean value) {

            progressDialog.dismiss();

            if (value) {
                System.out.println("*********************************************************************SI SE PUDO CONECTAR****************************");
                if (Messages.unPackMsgLogin(Activity_login.this)) {
                    Global.enSesion = true;
                    Global.StatusExit = true;

                    if (Integer.parseInt(Global.LOGIN) == 0) {
                        Utils.GoToNextActivity(Activity_login.this, Activity_UpdatePassword.class, Global.StatusExit);
                    } else {
                        Utils.GoToNextActivity(Activity_login.this, MainActivity.class, Global.StatusExit);
                    }

                } else {
                    // Si el login no es OK, manda mensaje de error
                    try {
                        // Utils.GoToNextActivity(Activity_login.this, DialogError.class, Global.StatusExit);
                        if (Global.mensaje.equalsIgnoreCase("Contrasena Invalida")) {
                            Global.mensaje ="Contraseña inválida";
                        }
                    Toast.makeText(Activity_login.this, Global.mensaje, Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    // Limpia el login

                }limpiarLogin();
            } else {
                switch (Utils.validateErrorsConexion(false, trans, Activity_login.this)) {

                    case 0:                                                                         // En caso de que continue = true y error data
                        break;

                    case 1:                                                                         // En caso de que continue = false y error data
                        break;

                    default:                                                                        // Errores de conexion
                        Global.MsgError = Global.MSG_ERR_CONEXION;
                        Global.mensaje = Global.MsgError;
                        Global.StatusExit = false;
                        // Muestra la ventana de error
                        Toast.makeText(Activity_login.this, Global.mensaje, Toast.LENGTH_LONG).show();
                        break;
                } limpiarLogin();

            }

        }


    }

    private void limpiarLogin() {
        this.txtCorreo.setText("");
        this.txtPass.setText("");
    }

    public void menu(View v) {

        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        finish();
    }

    public void probar_(View v) {

        Intent i = new Intent(this, Activity_UpdatePassword.class);
        startActivity(i);
        finish();
    }


}
