package com.example.wposs_user.polariscoreandroid.Actividades;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.wposs_user.polariscoreandroid.Comun.Global;
import com.example.wposs_user.polariscoreandroid.Comun.Messages;
import com.example.wposs_user.polariscoreandroid.Comun.Utils;
import com.example.wposs_user.polariscoreandroid.R;
import com.example.wposs_user.polariscoreandroid.TCP.TCP;

public class Activity_UpdatePassword extends AppCompatActivity {


    EditText clave1;
    EditText clave2;
    String pas1;
    String pas2;
    Button cambioClave;
    String msj="";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_password);
        clave1=(EditText)findViewById(R.id.txtClave1);
        clave2= (EditText)findViewById(R.id.txtClave2);
       cambioClave= (Button)findViewById(R.id.cambio_clave);



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

        System.out.print(pas1);


        //validaciones
        if (pas1.isEmpty()||pas2.isEmpty()) {

            Toast mensaje3 = Toast.makeText(this, "Debe ingresar la contraseña", Toast.LENGTH_LONG);
            mensaje3.show();

        }
        else if(pas1.equals(pas2)){

            boolean rev = revisarMayMinNum(pas1);

            if(rev){
                Global.WEB_SERVICE = "/PolarisCore/Users/updatePassword";
                Global.primaryIP = Global.INITIAL_IP;
                Global.primaryPort = Global.INITIAL_PORT;
                Global.claveNueva= pas1;
                Messages.packMsgUpdatePass();
                new TaskUpdatePassword().execute();//hacer la peticion
                Toast mensaje3 = Toast.makeText(this, msj= "La contraseña se ha actualizado exitosamente", Toast.LENGTH_LONG);
                mensaje3.show();


            }

            else if(!rev) {

                Toast mensaje3 = Toast.makeText(this, msj = "La contraseña no cumple las condiciones minimas ", Toast.LENGTH_LONG);
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
        return contLetraMay > 0 && contNumero > 2 && contLetraMin > 0 && totalCaracteres > 7;


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
            progressDialog = new ProgressDialog(Activity_UpdatePassword.this, R.style.MyAlertDialogStyle);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setCancelable(false);
            progressDialog.setMessage("conectando...");
            //progressDialog.show();
        }


        /*******************************************************************************
         Método       : doInBackground
         Description  : Se ejecuta para realizar la transacción y verificar coenxión
         *******************************************************************************/
        @Override
        protected Boolean doInBackground(String... strings) {
            Messages.packMsgUpdatePass();
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

                    }
    }



    public void limpiarClave () {

        clave1.setText("");
        clave2.setText("");

    }
}
