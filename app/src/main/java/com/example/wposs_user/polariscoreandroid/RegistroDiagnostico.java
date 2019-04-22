package com.example.wposs_user.polariscoreandroid;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wposs_user.polariscoreandroid.Actividades.MainActivity;
import com.example.wposs_user.polariscoreandroid.Comun.Global;
import com.example.wposs_user.polariscoreandroid.Comun.Messages;
import com.example.wposs_user.polariscoreandroid.Comun.Utils;
import com.example.wposs_user.polariscoreandroid.TCP.TCP;
import com.example.wposs_user.polariscoreandroid.java.Repuesto;


public class RegistroDiagnostico extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_diagnostico);
    }

    //******************consumir servicio listar Repuestos

    public void listarRepuestos() {
        Global.WEB_SERVICE = "/PolarisCore/Terminals/spares ";
        new TaskListarRepuestos().execute();
        this.llenarSpinerRepuestos();

    }


    /*************************************************************************************
     * CLASE QUE CONSUME EL SERVICIO PARA LISTAR LOS REPUESTOS ASOCIADOS A UNA TERMINAL
     *
     ***************************************************** **/

//******************consumir servicio listar Repuestos
    class TaskListarRepuestos extends AsyncTask<String, Void, Boolean> {
        ProgressDialog progressDialog;
        int trans = 0;


        /*******************************************************************************
         Método       : onPreExecute
         Description  : Se ejecuta antes de realizar el proceso, muestra una ventana con uin msj de espera
         *******************************************************************************/
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(RegistroDiagnostico.this, R.style.MyAlertDialogStyle);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setCancelable(false);
            progressDialog.setMessage("Buscando repuestos asociados a la terminal...");
            progressDialog.show();
        }


        /*******************************************************************************
         Método       : doInBackground
         Description  : Se ejecuta para realizar la transacción y verificar coenxión
         *******************************************************************************/
        @Override
        protected Boolean doInBackground(String... strings) {
            Messages.packMsgListarObservaciones();

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
                System.out.println("*********************************************************************SI SE PUDIERON LISTAR LOS REPUESTOS****************************");
                if (Messages.unPackMsgListaRepuestos(RegistroDiagnostico.this)) {
                    Global.enSesion = true;
                    Global.StatusExit = true;

                    //muestra el panel con la lista de observaciones, es decir que llena el recycler view de observaciones

//OJOOOOOO LLENAR EL RECYCLER VIEW DE  OBSERVACIONES?
                } else {
                    // Si el login no es OK, manda mensaje de error
                    try {
                        // Utils.GoToNextActivity(Activity_login.this, DialogError.class, Global.StatusExit);
                        Toast.makeText(RegistroDiagnostico.this, Global.mensaje, Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                // Toast.makeText(Activity_login.this, Global.mensaje, Toast.LENGTH_LONG).show();
                // Si es falso, cierra el socket y vuelve a crearlo, si es verdadero el socket continua abierto
                TCP.disconnect();

            } else {
                switch (Utils.validateErrorsConexion(false, trans, RegistroDiagnostico.this)) {

                    case 0:                                                                         // En caso de que continue = true y error data
                        break;

                    case 1:                                                                         // En caso de que continue = false y error data
                        break;

                    default:                                                                        // Errores de conexion
                        Global.MsgError = Global.MSG_ERR_CONEXION;
                        Global.mensaje = Global.MsgError;
                        Global.StatusExit = false;
                        // Muestra la ventana de error
                        Toast.makeText(RegistroDiagnostico.this, Global.mensaje, Toast.LENGTH_LONG).show();
                        break;
                }

                Toast.makeText(RegistroDiagnostico.this, Global.mensaje, Toast.LENGTH_LONG).show();
            }
            System.out.println("******************TERMINÓ DE CONSUMIR EL SERVICIO DE LISTAR REPUESTOS");
        }


    }

    public void llenarSpinerRepuestos(){

        final AutoCompleteTextView et_repuesto = (AutoCompleteTextView) findViewById(R.id.et_repuesto);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);

        for(int i=0;i<Global.REPUESTOS.size();i++){

            adapter.add(Global.REPUESTOS.get(i).getSpar_code()+""+Global.REPUESTOS.get(i).getSpar_name());

        }
        et_repuesto.setAdapter(adapter);
        et_repuesto.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                InputMethodManager in = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                in.hideSoftInputFromWindow(view.getApplicationWindowToken(), 0);
            }
        });
        et_repuesto.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if(i == EditorInfo.IME_ACTION_DONE){
                    InputMethodManager in = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    in.hideSoftInputFromWindow(textView.getApplicationWindowToken(), 0);
                    return true;
                }
                return false;
            }
        });


    }

    public void registrarDiagnostico() {
        Global.WEB_SERVICE = "/PolarisCore/Terminals/saveDiagnosis ";
        new TaskListarRepuestos().execute();
        this.llenarSpinerRepuestos();

    }


    /*************************************************************************************
     * CLASE QUE CONSUME EL SERVICIO PARA LISTAR LAS OBSERVACIONES
     *
     ***************************************************** **/

//******************consumir servicio listar observaciones
    class TaskRegistrarDiagnostico extends AsyncTask<String, Void, Boolean> {
        ProgressDialog progressDialog;
        int trans = 0;


        /*******************************************************************************
         Método       : onPreExecute
         Description  : Se ejecuta antes de realizar el proceso, muestra una ventana con uin msj de espera
         *******************************************************************************/
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(RegistroDiagnostico.this, R.style.MyAlertDialogStyle);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setCancelable(false);
            progressDialog.setMessage("Enviando registro...");
            progressDialog.show();
        }


        /*******************************************************************************
         Método       : doInBackground
         Description  : Se ejecuta para realizar la transacción y verificar coenxión
         *******************************************************************************/
        @Override
        protected Boolean doInBackground(String... strings) {
            Messages.packMsgRegistrarDiag();

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
                System.out.println("*********************************************************************SI SE PUDO CONECTAR LISTAR OBSER****************************");
                if (Messages.unPackMsgListarObservaciones(RegistroDiagnostico.this)) {
                    Global.enSesion = true;
                    Global.StatusExit = true;

                    //muestra el panel con la lista de observaciones, es decir que llena el recycler view de observaciones

//OJOOOOOO LLENAR EL RECYCLER VIEW DE  OBSERVACIONES?
                } else {
                    // Si el login no es OK, manda mensaje de error
                    try {
                        // Utils.GoToNextActivity(Activity_login.this, DialogError.class, Global.StatusExit);
                        Toast.makeText(RegistroDiagnostico.this, Global.mensaje, Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                // Toast.makeText(Activity_login.this, Global.mensaje, Toast.LENGTH_LONG).show();
                // Si es falso, cierra el socket y vuelve a crearlo, si es verdadero el socket continua abierto
                TCP.disconnect();

            } else {
                switch (Utils.validateErrorsConexion(false, trans, RegistroDiagnostico.this)) {

                    case 0:                                                                         // En caso de que continue = true y error data
                        break;

                    case 1:                                                                         // En caso de que continue = false y error data
                        break;

                    default:                                                                        // Errores de conexion
                        Global.MsgError = Global.MSG_ERR_CONEXION;
                        Global.mensaje = Global.MsgError;
                        Global.StatusExit = false;
                        // Muestra la ventana de error
                        Toast.makeText(RegistroDiagnostico.this, Global.mensaje, Toast.LENGTH_LONG).show();
                        break;
                }

                Toast.makeText(RegistroDiagnostico.this, Global.mensaje, Toast.LENGTH_LONG).show();
            }
            System.out.println("******************TERMINÓ DE CONSUMIR EL SERVICIO DE LISTAR OBSERVA");
        }


    }


}
