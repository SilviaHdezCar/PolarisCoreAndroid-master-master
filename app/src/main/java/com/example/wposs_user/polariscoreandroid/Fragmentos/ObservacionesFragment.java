package com.example.wposs_user.polariscoreandroid.Fragmentos;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wposs_user.polariscoreandroid.Comun.Global;
import com.example.wposs_user.polariscoreandroid.Comun.Messages;
import com.example.wposs_user.polariscoreandroid.Comun.Utils;
import com.example.wposs_user.polariscoreandroid.R;
import com.example.wposs_user.polariscoreandroid.TCP.TCP;

import static com.example.wposs_user.polariscoreandroid.Actividades.MainActivity.objeto;

public class ObservacionesFragment extends Fragment {
    private View v;
    private TextView btn_tomar_foto;
    private TextView txt_observacion;
    private ImageView imagen_observación;
    private Button finalizar;
    private String nomFoto;
    Bitmap bitmap;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_observaciones, container, false);

        imagen_observación = (ImageView) v.findViewById(R.id.imagen_observación);

        finalizar = (Button) v.findViewById(R.id.btn_finalizar_observacion);

        txt_observacion = (TextView) v.findViewById(R.id.txt_observacion_fin);
        btn_tomar_foto = (TextView) v.findViewById(R.id.lbl_cargarFoto);
        btn_tomar_foto.setPaintFlags(btn_tomar_foto.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        nomFoto = Global.ID + ".jpg";
        bitmap = null;


        btn_tomar_foto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intento1 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intento1, 0);
            }
        });

        finalizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finalizarDiagnostico();
            }
        });

        return v;

    }

    //este metodo se utiliza para obetenr la foto tomada
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        bitmap = (Bitmap) data.getExtras().get("data");
        imagen_observación.setImageBitmap(bitmap);

    }

    //Metodo utilizado para finalizar el diagnostico (Consume el servicio de finalizar diagnostico)
    public void finalizarDiagnostico() {
        String observaciones = txt_observacion.getText().toString();

        if (observaciones.isEmpty()) {
            Toast.makeText(objeto, "Por favor ingrese la observación", Toast.LENGTH_SHORT).show();
            return;
        }if(bitmap==null){
            Toast.makeText(objeto, "Por favor tome la foto", Toast.LENGTH_SHORT).show();
            return;
        }
        else {
            Global.WEB_SERVICE = "/PolarisCore/Terminals/saveDiagnosis";
           /* Global.primaryIP = Global.INITIAL_IP;
            Global.primaryPort = Global.INITIAL_PORT;

            Global.correo = correo;
            Global.password = pass;
*/
            new TaskFinalizarDiagnostico().execute();//hacer la peticion
        }
    }

    /*******************************************************************************
     Clase       : TaskLogin
     Description : Realiza la transacción de los parámetros del Login
     *******************************************************************************/


    class TaskFinalizarDiagnostico extends AsyncTask<String, Void, Boolean> {
        ProgressDialog progressDialog;
        int trans = 0;


        /*******************************************************************************
         Método       : onPreExecute
         Description  : Se ejecuta antes de realizar el proceso, muestra una ventana con uin msj de espera
         *******************************************************************************/


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(objeto, R.style.MyAlertDialogStyle);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setCancelable(false);
            progressDialog.setMessage("Finalizando diagnóstico...");
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
                if (Messages.unPackMsgFinalizarDiagnostico(objeto)) {
                    Global.enSesion = true;
                    Global.StatusExit = true;

                    AlertDialog alertDialog = new AlertDialog.Builder(objeto).create();
                    alertDialog.setTitle("INFORMACIÓN");
                    alertDialog.setMessage("El diagnóstico fue creado correctamente");
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "ACEPTAR",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    objeto.getSupportFragmentManager().beginTransaction().replace(R.id.contenedor_main, new InicialFragment()).commit();
                                }
                            });
                    alertDialog.show();


                } else {
                    // Si el login no es OK, manda mensaje de error
                    try {
                        Toast.makeText(objeto, Global.mensaje, Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    // Limpia el login

                }
            } else {
                switch (Utils.validateErrorsConexion(false, trans, objeto)) {

                    case 0:                                                                         // En caso de que continue = true y error data
                        break;

                    case 1:                                                                         // En caso de que continue = false y error data
                        break;

                    default:                                                                        // Errores de conexion
                        Global.MsgError = Global.MSG_ERR_CONEXION;
                        Global.mensaje = Global.MsgError;
                        Global.StatusExit = false;
                        // Muestra la ventana de error
                        Toast.makeText(objeto, Global.mensaje, Toast.LENGTH_LONG).show();
                        break;
                }

            }

        }


    }


}
