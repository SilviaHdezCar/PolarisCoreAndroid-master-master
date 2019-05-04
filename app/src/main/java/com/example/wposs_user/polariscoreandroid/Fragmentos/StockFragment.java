package com.example.wposs_user.polariscoreandroid.Fragmentos;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.wposs_user.polariscoreandroid.Comun.Global;
import com.example.wposs_user.polariscoreandroid.Comun.Messages;
import com.example.wposs_user.polariscoreandroid.Comun.Utils;
import com.example.wposs_user.polariscoreandroid.R;
import com.example.wposs_user.polariscoreandroid.TCP.TCP;


public class StockFragment extends Fragment {


private View v;
private ImageView foto_perfil;

    public StockFragment() {
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         v = inflater.inflate(R.layout.fragment_stock, container, false);
         foto_perfil=(ImageView)v.findViewById(R.id.img_photo_perfil);
          foto_perfil=Global.foto_perfil;
          return v;

    }

    public void buscarFoto() {
        Global.WEB_SERVICE = "/PolarisCore/upload/view/:1093.jpg ";
        new TaskFoto().execute();


    }


    /*************************************************************************************
     * CLASE QUE CONSUME EL SERVICIO PARA Obtener la Foto   *********************/

    class TaskFoto extends AsyncTask<String, Void, Boolean> {
        ProgressDialog progressDialog;
        int trans = 0;


        /*******************************************************************************
         Método       : onPreExecute
         Description  : Se ejecuta antes de realizar el proceso, muestra una ventana con un msj de espera
         *******************************************************************************/
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(v.getContext(), R.style.MyAlertDialogStyle);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setCancelable(false);
            progressDialog.setMessage("Cargando Foto...");
            progressDialog.show();
        }




        /*******************************************************************************
         Método       : doInBackground
         Description  : Se ejecuta para realizar la transacción y verificar coenxión
         *******************************************************************************/
        @Override
        protected Boolean doInBackground(String... strings) {

            Global.inputData= new byte[Global.MAX_LEN_INPUTDATA];
            Global.httpDataBuffer="";
            Messages.packMsgPhoto();
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
            Messages.unPackMsgPhoto(v.getContext());
            if (value) {
                         System.out.println("*********************************************************************SI SE PUDO CARGAR LA FOTO****************************");

                  try {

                        Toast.makeText(v.getContext(), Global.mensaje, Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                //    TCP.disconnect();

             else {
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
            System.out.println("******************SE CARGO LA FOTO");
        }


    }










}
