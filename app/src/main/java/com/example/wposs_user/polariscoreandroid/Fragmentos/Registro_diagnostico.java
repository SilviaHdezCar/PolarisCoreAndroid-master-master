package com.example.wposs_user.polariscoreandroid.Fragmentos;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wposs_user.polariscoreandroid.Adaptadores.AdapterRepuesto;
import com.example.wposs_user.polariscoreandroid.Comun.Global;
import com.example.wposs_user.polariscoreandroid.Comun.Messages;
import com.example.wposs_user.polariscoreandroid.Comun.Utils;
import com.example.wposs_user.polariscoreandroid.R;
import com.example.wposs_user.polariscoreandroid.TCP.TCP;
import com.example.wposs_user.polariscoreandroid.Comun.Tools;
import com.example.wposs_user.polariscoreandroid.java.Observacion;
import com.example.wposs_user.polariscoreandroid.java.Repuesto;
import com.example.wposs_user.polariscoreandroid.java.Tipificacion;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.INPUT_METHOD_SERVICE;
import static com.example.wposs_user.polariscoreandroid.Actividades.MainActivity.objeto;


public class Registro_diagnostico extends Fragment {

    private View v;
    private EditText cantidad_req;
    private AutoCompleteTextView aut_repuesto;
    private RecyclerView rv;
    private Button agregar;
    private EditText observ;
    private Button registroDiag;



    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_registro_diagnostico, container, false);

        aut_repuesto = (AutoCompleteTextView) v.findViewById(R.id.auto_repuesto);
        cantidad_req = v.findViewById(R.id.txt_cantReq);
        rv = (RecyclerView) v.findViewById(R.id.rv_repuestos_diag);
        agregar= (Button) v.findViewById(R.id.bton_agregarRepuesto);
        observ = (EditText) v.findViewById(R.id.txt_observaciones);
        registroDiag=(Button)v.findViewById(R.id.btn_registroDioagnostico);
         this.listarRepuestos();
         Global.serial_ter="111111";

         registroDiag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registrarDiagnostico();

            }
        });

       agregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                agregarRepuesto();
            }
        });


        return v;
    }


    public void listarRepuestos() {
        Global.WEB_SERVICE = "/PolarisCore/Terminals/spares ";
        new TaskListarRepuestos().execute();
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
            progressDialog = new ProgressDialog(v.getContext(), R.style.MyAlertDialogStyle);
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
            Messages.packMsgListarRepuestos();

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
                if (Messages.unPackMsgListaRepuestos(v.getContext())) {
                    Global.enSesion = true;
                    Global.StatusExit = true;
                    llenarAutocomplete();
                    if(Global.REPUESTOS.size()==0){

                       observ.setText("No hay repuestos disponibles para el modelo de terminal seleccionado");
                       agregar.setEnabled(false);
                        registroDiag.setEnabled(false);
                    }
                          } else {

                    try {

                        Toast.makeText(v.getContext(), Global.mensaje, Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                  //    TCP.disconnect();

            } else {
                switch (Utils.validateErrorsConexion(false, trans, v.getContext())) {

                    case 0:                                                                         // En caso de que continue = true y error data
                        break;

                    case 1:                                                                         // En caso de que continue = false y error data
                        break;

                    default:                                                                        // Errores de conexion
                        Global.MsgError = Global.MSG_ERR_CONEXION;
                        Global.mensaje = Global.MsgError;
                        Global.StatusExit = false;
                        // Muestra la ventana de error
                        Toast.makeText(v.getContext(), Global.mensaje, Toast.LENGTH_LONG).show();
                        break;
                }

                Toast.makeText(v.getContext(), Global.mensaje, Toast.LENGTH_LONG).show();
            }
            System.out.println("******************TERMINÓ DE CONSUMIR EL SERVICIO DE LISTAR REPUESTOS");
        }


    }





    public void registrarDiagnostico(View v) {
        Global.WEB_SERVICE = "/PolarisCore/Terminals/saveDiagnosis ";
        new TaskListarRepuestos().execute();


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
            progressDialog = new ProgressDialog(v.getContext(), R.style.MyAlertDialogStyle);
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
                if (Messages.unPackMsgListarObservaciones(v.getContext())) {
                    Global.enSesion = true;
                    Global.StatusExit = true;


                    //muestra el panel con la lista de observaciones, es decir que llena el recycler view de observaciones

//OJOOOOOO LLENAR EL RECYCLER VIEW DE  OBSERVACIONES?
                } else {
                    // Si el login no es OK, manda mensaje de error
                    try {
                        // Utils.GoToNextActivity(Activity_login.this, DialogError.class, Global.StatusExit);
                        Toast.makeText(v.getContext(), Global.mensaje, Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                // Toast.makeText(Activity_login.this, Global.mensaje, Toast.LENGTH_LONG).show();
                // Si es falso, cierra el socket y vuelve a crearlo, si es verdadero el socket continua abierto
                //TCP.disconnect();

            } else {
                switch (Utils.validateErrorsConexion(false, trans, v.getContext())) {

                    case 0:                                                                         // En caso de que continue = true y error data
                        break;

                    case 1:                                                                         // En caso de que continue = false y error data
                        break;

                    default:                                                                        // Errores de conexion
                        Global.MsgError = Global.MSG_ERR_CONEXION;
                        Global.mensaje = Global.MsgError;
                        Global.StatusExit = false;
                        // Muestra la ventana de error
                        Toast.makeText(v.getContext(), Global.mensaje, Toast.LENGTH_LONG).show();
                        break;
                }

                Toast.makeText(v.getContext(), Global.mensaje, Toast.LENGTH_LONG).show();
            }
            System.out.println("******************TERMINÓ DE CONSUMIR EL SERVICIO DE LISTAR OBSERVA");
            //TCP.disconnect();
        }


    }

    public String[] convertirRepuestos(){

        String[] rep  = new String[Global.REPUESTOS.size()];


        for(int i =0;i<Global.REPUESTOS.size();i++){

            rep[i]= Global.REPUESTOS.get(i).getSpar_code()+"   "+Global.REPUESTOS.get(i).getSpar_name();

        }
        return rep;

    }


    public void llenarAutocomplete(){
     /*   aut = (AutoCompleteTextView)v.findViewById(R.id.auto_repuesto);
        String [] rep = this.convertirRepuestos();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(v.getContext(),R.layout.spinner_sytle,rep);
        aut.setAdapter(adapter);
*/
        final String [] rep = this.convertirRepuestos();
        aut_repuesto = (AutoCompleteTextView)v.findViewById(R.id.auto_repuesto);
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(v.getContext(), R.layout.spinner_sytle, rep);

        aut_repuesto.setAdapter(adapter);
        aut_repuesto.setThreshold(1);
        aut_repuesto.setOnItemClickListener(new AdapterView.OnItemClickListener() {
       @Override
       public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
         String code=adapter.getItem(i);
         String[] repuest= code.split(" ");
           Global.codigo_rep=repuest[0];

                System.out.println(" Codigo del repuesto seleccionado;"+Global.codigo_rep);
                InputMethodManager in = (InputMethodManager) v.getContext().getSystemService(INPUT_METHOD_SERVICE);
                in.hideSoftInputFromWindow(view.getApplicationWindowToken(), 0);
            }
        });
        aut_repuesto.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {

                if(i == EditorInfo.IME_ACTION_DONE){

                    String code=adapter.getItem(i);
                    String[] repuest= code.split(" ");
                    Global.codigo_rep=repuest[0];
                      InputMethodManager in = (InputMethodManager) v.getContext().getSystemService(INPUT_METHOD_SERVICE);
                    in.hideSoftInputFromWindow(textView.getApplicationWindowToken(), 0);
                    return true;
                }
                return false;
            }
        });

   }


     public void agregarRepuesto() {

        String cant= cantidad_req.getText().toString();


        if(Global.REPUESTOS.size()==0){

            observ.setText("No hay repuestos para el modelo de terminal seleccionado, seleccione otre intentelo de nuevo");
            agregar.setEnabled(false);
            registroDiag.setEnabled(false);
            return;
        }

        if ( Global.codigo_rep.isEmpty()||cant.isEmpty()) {
            Toast.makeText(objeto, "Faltan datos para agregar el repuesto", Toast.LENGTH_SHORT).show();
            return;
        }

            int cant_solicitada= Integer.parseInt(cant);

        if(cant_solicitada<=0){

            Toast.makeText(objeto, "Debe solicitar como minimo 1 repuesto", Toast.LENGTH_SHORT).show();

        }

                for (int i =0;i< Global.REPUESTOS.size();i++) {

                if(Global.REPUESTOS.get(i).getSpar_code().equals(Global.codigo_rep)){
                        if(Global.REPUESTOS.get(i).getSpar_quantity()<cant_solicitada){
                        Toast.makeText(objeto, "El repuesto seleccionado no tiene disponible la cantidad solicitada", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    Repuesto r = new Repuesto(Global.REPUESTOS.get(i).getSpar_code(),Global.REPUESTOS.get(i).getSpar_name(),cant_solicitada);
                    Global.REPUESTOS_DIAGONOSTICO.add(r);
                     Toast.makeText(objeto, "El repuesto fue agregado exitosamente", Toast.LENGTH_SHORT).show();
                    this.llenarRv();
                    cantidad_req.setText("");
                    aut_repuesto.setText("");
                    return;

                }

            }

    }


    private void llenarRv() {
        rv.setHasFixedSize(true);

        LinearLayoutManager llm = new LinearLayoutManager(Tools.getCurrentContext());
        rv.setLayoutManager(llm);

        final AdapterRepuesto adapter = new AdapterRepuesto(Global.REPUESTOS_DIAGONOSTICO, new AdapterRepuesto.interfaceClick() {//seria termi asoc

            public void onClick(List<Repuesto> terminal, int position) {

                Global.REPUESTOS_DIAGONOSTICO.remove(position);
                llenarRv();
            }
        }, R.layout.panel_agregarep);

        rv.setAdapter(adapter);

    }




    /*************************************************************************************
     * CLASE QUE CONSUME EL SERVICIO PARA REGISTRAR EL DIAGNOSTICO SI LA TERMINAL ES REPARABLE
     *
     ***************************************************** **/

    class TaskRegistrarDiagnosticos extends AsyncTask<String, Void, Boolean> {
        ProgressDialog progressDialog;
        int trans = 0;


        /*******************************************************************************
         Método       : onPreExecute
         Description  : Se ejecuta antes de realizar el proceso, muestra una ventana con uin msj de espera
         *******************************************************************************/
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(v.getContext(), R.style.MyAlertDialogStyle);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setCancelable(false);
            progressDialog.setMessage("Enviando los datos del diagnostico");
            progressDialog.show();
        }


        /*******************************************************************************
         Método       : doInBackground
         Description  : Se ejecuta para realizar la transacción y verificar coenxión
         *******************************************************************************/
        @Override
        protected Boolean doInBackground(String... strings) {
            Messages.packMsgRegistrarDiagnosticos();

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
                System.out.println("*********************************************************************SI SE PUDO REGISTRAR EL DIAGNOSTICO****************************");
                if (Messages.unPackMsgDiagnostico(v.getContext())) {
                    Toast.makeText(v.getContext(), "El diagnostico se registro correctamente", Toast.LENGTH_LONG).show();
                } else {

                    try {

                        Toast.makeText(v.getContext(), Global.mensaje, Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            //    TCP.disconnect();

            } else {
                switch (Utils.validateErrorsConexion(false, trans, v.getContext())) {

                    case 0:                                                                         // En caso de que continue = true y error data
                        break;

                    case 1:                                                                         // En caso de que continue = false y error data
                        break;

                    default:                                                                        // Errores de conexion
                        Global.MsgError = Global.MSG_ERR_CONEXION;
                        Global.mensaje = Global.MsgError;
                        Global.StatusExit = false;
                        // Muestra la ventana de error
                        Toast.makeText(v.getContext(), Global.mensaje, Toast.LENGTH_LONG).show();
                        break;
                }

                Toast.makeText(v.getContext(), Global.mensaje, Toast.LENGTH_LONG).show();
            }
            System.out.println("******************TERMINÓ DE CONSUMIR EL SERVICIO DE LISTAR REPUESTOS");
        }


    }




    public void registrarDiagnostico(){


        Global.WEB_SERVICE = "/PolarisCore/Terminals/saveDiagnosis";



    String descripicionObserv= observ.getText().toString();
    Observacion obs= new Observacion("as12542", descripicionObserv,"","","",Global.serial_ter);

    if(descripicionObserv.isEmpty()){
        Toast.makeText(objeto, "Debe agregar al menos una observacion del estado de la terminal", Toast.LENGTH_SHORT).show();
        return;

    }
    if(rv.getAdapter()==null){

        Toast.makeText(objeto, "Debe agregar al menos un repuesto", Toast.LENGTH_SHORT).show();
      //  rv.removeAllViewsInLayout(); Para limpiar el reciler view
        return;

    }

      Global.observacion= obs.toString();

    new TaskRegistrarDiagnosticos().execute();

    }















}
