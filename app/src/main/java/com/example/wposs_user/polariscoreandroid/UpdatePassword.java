package com.example.wposs_user.polariscoreandroid;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.wposs_user.polariscoreandroid.Comun.Global;
import com.example.wposs_user.polariscoreandroid.Comun.Messages;
import com.example.wposs_user.polariscoreandroid.Comun.Utils;
import com.example.wposs_user.polariscoreandroid.TCP.TCP;

public class UpdatePassword extends AppCompatActivity {


    EditText clave1;
    EditText clave2;
    String pas1;
    String pas2;
    EditText resp1;
    EditText resp2;
    EditText resp3;
    String rta1;
    String rta2;
    String rta3;
    Button cambioClave;
    String msj="";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_password);
        clave1=(EditText)findViewById(R.id.txtClave1);
        clave2= (EditText)findViewById(R.id.txtClave2);
        resp1= (EditText)findViewById(R.id.txt_resp1);
        resp2=(EditText) findViewById(R.id.txt_resp2);
        resp3=(EditText) findViewById(R.id.txt_resp3);

        cambioClave= (Button)findViewById(R.id.cambio_clave);
        Global.claveNueva=pas1;


    }



    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(this, Activity_login.class);
        startActivity(i);
        finish();
    }

    public void validaClave(View v){

        pas1=clave1.getText().toString();
        pas2= clave2.getText().toString();

        //validaciones
        if (pas1.equals("")||pas2.equals("")) {

            Toast mensaje3 = Toast.makeText(this, "Debe ingresar la contraseña", Toast.LENGTH_LONG);
            mensaje3.show();

        }
        else if(pas1.equals(pas2)){

            boolean rev = revisarMayMinNum(pas1);

            if(rev){

                Global.WEB_SERVICE = "/PolarisCore/Users/updatePassword";
                Global.primaryIP = Global.INITIAL_IP;
                Global.primaryPort = Global.INITIAL_PORT;



                new TaskUpdatePassword().execute();//hacer la peticion
                Toast mensaje3 = Toast.makeText(this, msj= "La contraseña se ha actualizado exitosamente", Toast.LENGTH_LONG);
                mensaje3.show();
                resp1.setText("");
            }

            else if(rev==false) {

                Toast mensaje3 = Toast.makeText(this, msj = "La contraseña no cumple las condiciones minimas, debe contener una mayúscula,una minúscula 3 numeros, y longitud de 8", Toast.LENGTH_LONG);
                mensaje3.show();
            }
        }


    }

    public boolean revisarMayMinNum( String password){
        String msj="";

        char clave;

        byte  contNumero = 0;
        byte contLetraMay = 0;
        byte contLetraMin=0;

        for (byte i = 0; i < password.length(); i++) {

            clave = password.charAt(i);

            String passValue = String.valueOf(clave);

            if (passValue.matches("[A-Z]")) {

                contLetraMay++;
            } else if (passValue.matches("[a-z]")) {

                contLetraMin++;
            } else if (passValue.matches("[0-9]")) {

                contNumero++;
            }

        }

        int totalCaracteres= contNumero+contLetraMin+contLetraMay;
        if(contLetraMay>0 && contNumero>2 && contLetraMin>0 && totalCaracteres>7){
            return true;
        }


        return false;
    }






    public void validaRta(View v){

        rta1= resp1.getText().toString();
        rta2= resp2.getText().toString();
        rta3= resp3.getText().toString();

        int respu1 = rta1.length();
        int respu2= rta2.length();
        int respu3=rta3.length();


        System.out.println(rta1);
        System.out.println(rta1);
        System.out.println(rta1);


        if(rta1.isEmpty()||rta2.isEmpty()||rta3.isEmpty()){
            Toast mensaje3 = Toast.makeText(this, msj= "Falta una o mas preguntas por responder", Toast.LENGTH_SHORT);
            mensaje3.show();
            limpiarRta();
            return;
        }
        else   if(respu1<5||respu2<5||respu3<5){
            Toast mensaje3 = Toast.makeText(this, msj= "Las respuestas deben tener una longitud de minimo 5", Toast.LENGTH_LONG);
            mensaje3.show();
            limpiarRta();
            return;
        }


        else
        if(respu1>5&& respu2>5&&respu3>5) {
            clave1.setVisibility(View.VISIBLE);
            clave2.setVisibility(View.VISIBLE);
            cambioClave.setVisibility(View.VISIBLE);
            Toast mensaje5 = Toast.makeText(this, msj = "Las preguntas se han validado satisfactoriamente", Toast.LENGTH_LONG);
            mensaje5.show();




        }


    }


    class TaskUpdatePassword extends AsyncTask<String, Void, Boolean> {
        ProgressDialog progressDialog;
        int trans = 0;


        /*******************************************************************************
         Método       : onPreExecute
         Description  : Se ejecuta antes de realizar el proceso, muestra una ventana con uin msj de espera
         *******************************************************************************/


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(UpdatePassword.this, R.style.MyAlertDialogStyle);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setCancelable(false);
            progressDialog.setMessage("conectando...");
            progressDialog.show();
        }


        /*******************************************************************************
         Método       : doInBackground
         Description  : Se ejecuta para realizar la transacción y verificar coenxión
         *******************************************************************************/
        @Override
        protected Boolean doInBackground(String... strings) {
            Messages.packUpdatePass();
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
                {

                    Global.enSesion = true;
                    Global.StatusExit = true;

                    Utils.GoToNextActivity(UpdatePassword.this, MainActivity.class, Global.StatusExit);


                    switch (Utils.validateErrorsConexion(false, trans, UpdatePassword.this)) {

                        case 0:                                                                         // En caso de que continue = true y error data
                            break;

                        case 1:                                                                         // En caso de que continue = false y error data
                            break;

                        default:                                                                        // Errores de conexion
                            Global.MsgError = Global.MSG_ERR_CONEXION;
                            Global.mensaje = Global.MsgError;
                            Global.StatusExit = false;
                            // Muestra la ventana de error
                            Toast.makeText(UpdatePassword.this, Global.mensaje, Toast.LENGTH_LONG).show();
                            break;
                    }


                }
            }



        }
    }

    public void limpiarRta () {

        resp1.setText("");
        resp2.setText("");
        resp3.setText("");

    }


    public void limpiarClave () {

        clave1.setText("");
        clave2.setText("");

    }
}
