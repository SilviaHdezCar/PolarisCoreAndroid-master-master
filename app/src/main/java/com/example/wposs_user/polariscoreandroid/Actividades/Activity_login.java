package com.example.wposs_user.polariscoreandroid;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.wposs_user.polariscoreandroid.Actividades.DialogError;
import com.example.wposs_user.polariscoreandroid.Comun.Global;
import com.example.wposs_user.polariscoreandroid.Comun.Messages;
import com.example.wposs_user.polariscoreandroid.Comun.Utils;
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
                    Global.StatusExit=true;

                    if(Integer.parseInt(Global.LOGIN)==0){
                        Utils.GoToNextActivity(Activity_login.this, Actualizar_clave.class, Global.StatusExit);
                    }else {
                        Utils.GoToNextActivity(Activity_login.this, MainActivity.class, Global.StatusExit);
                    }

                } else {
                    // Si el login no es OK, manda mensaje de error
                    try {
                       // Utils.GoToNextActivity(Activity_login.this, DialogError.class, Global.StatusExit);
                        Toast.makeText(Activity_login.this, Global.mensaje, Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    // Limpia el login

                }

                limpiarLogin();
                Toast.makeText(Activity_login.this, Global.mensaje, Toast.LENGTH_LONG).show();
                // Si es falso, cierra el socket y vuelve a crearlo, si es verdadero el socket continua abierto
                TCP.disconnect();

            } else {
                switch (Utils.validateErrorsConexion(false, trans, Activity_login.this)) {

                    case 0:                                                                         // En caso de que continue = true y error data
                        break;

                    case 1:                                                                         // En caso de que continue = false y error data
                        break;

                    default:                                                                        // Errores de conexion
                        Global.MsgError = Global.MSG_ERR_CONEXION;
                        Global.mensaje=Global.MsgError;
                        Global.StatusExit = false;
                        // Muestra la ventana de error
                        Toast.makeText(Activity_login.this, Global.mensaje, Toast.LENGTH_LONG).show();
                        break;
                }

                Toast.makeText(Activity_login.this, Global.mensaje, Toast.LENGTH_LONG).show();
                limpiarLogin();

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

    public void actu_clave(View v) {

        Intent i = new Intent(this, Actualizar_clave.class);
        startActivity(i);
        finish();
    }


}
