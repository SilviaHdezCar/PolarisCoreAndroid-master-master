package com.example.wposs_user.polariscoreandroid.Fragmentos;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wposs_user.polariscoreandroid.Actividades.MainActivity;
import com.example.wposs_user.polariscoreandroid.Comun.Global;
import com.example.wposs_user.polariscoreandroid.Comun.Messages;
import com.example.wposs_user.polariscoreandroid.Comun.Utils;
import com.example.wposs_user.polariscoreandroid.R;
import com.example.wposs_user.polariscoreandroid.TCP.TCP;

import static com.example.wposs_user.polariscoreandroid.Actividades.MainActivity.objeto;


public class ActualizarClave_perfil extends Fragment {

    private View v;
    private TextView btn_validar;
    private TextView btn_cancelar;
    private EditText perfil_clave_actual;
    private EditText perfil_clave_nueva;
    private EditText perfil_clave_confirmar;
    private LinearLayout layout_datos_cambiar_clave;
    private Button btn_aceptar_cambio_clave;
    private String nueva;
    private String confirmacion;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_actualizar_clave_perfil, container, false);

        btn_validar = (TextView) v.findViewById(R.id.lbl_validarClave);
        btn_cancelar = (TextView) v.findViewById(R.id.lbl_salir_validarClave);
        perfil_clave_actual = (EditText) v.findViewById(R.id.perfil_clave_actual);
        perfil_clave_nueva = (EditText) v.findViewById(R.id.perfil_clave_nueva);
        perfil_clave_confirmar = (EditText) v.findViewById(R.id.perfil_clave_confirmar);
        layout_datos_cambiar_clave = (LinearLayout) v.findViewById(R.id.layout_datos_cambiar_clave);
        btn_aceptar_cambio_clave = (Button) v.findViewById(R.id.btn_aceptar_cambio_clave);

        layout_datos_cambiar_clave.setVisibility(View.INVISIBLE);


        nueva = "";
        confirmacion = "";

        btn_validar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String actual = perfil_clave_actual.getText().toString();
                if (actual.isEmpty()) {
                    Toast.makeText(objeto, "Por favor ingrese la clave actual", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    Global.WEB_SERVICE = "/PolarisCore/Users/verification";
                    Global.validar_actual = actual;
                    new TaskVerificarClave().execute();
                }
                //validar que el campo de la clave no esté vacio
                //consumir servicio cambiar clave
                //mostrar layout layout_datos_cambiar_clave
            }
        });

        btn_aceptar_cambio_clave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cambiarClave();
            }
        });


        return v;
    }


    public void cambiarClave() {
        nueva = perfil_clave_nueva.getText().toString();
        confirmacion = perfil_clave_confirmar.getText().toString();

        String msj = validarClave(nueva, confirmacion);

        if (!msj.equalsIgnoreCase("ok")) {
            Toast.makeText(objeto, msj, Toast.LENGTH_LONG).show();
            return;
        } else {
            Global.WEB_SERVICE = "/PolarisCore/Users/updatePassword";
            Global.claveNueva = nueva;
            new TaskCambiarClave().execute();
        }

    }


    //Validaciones requeridas para cambiar la contraseña
    private String validarClave(String nueva, String confirmacion) {
        String msj = "ok";
        //validaciones
        if (nueva.isEmpty()) {
            return "Por favor ingrese contraseña nueva";
        } else if (confirmacion.isEmpty()) {
            return "Por favor ingrese  confirmarción de contraseña";
        } else if (!(nueva.length() >= 8)) {
            return "La contraseña debe contener como minimo 8 caracteres";
        } else if (!revisarMayMinNum(nueva)) {
            return "La contraseña debe contener números, letras en mayúscula y minúscula";
        } else if (!nueva.equals(confirmacion)) {
            return "La confirmación no coincide con la clave ingresada";
        }
        return msj;
    }

    //este metodo es para validar que la clave contenga numeros, letras minus y mayus
    public boolean revisarMayMinNum(String password) {
        String msj = "";

        char clave;

        byte contNumero = 0;
        byte contLetraMay = 0;
        byte contLetraMin = 0;

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
        if (contLetraMay > 0 && contNumero > 2 && contLetraMin > 0) {
            return true;
        }
        return false;
    }


    /*******************************************************************************
     Clase       : TaskLogin
     Description : Realiza la transacción de los parámetros del Login
     *******************************************************************************/


    class TaskVerificarClave extends AsyncTask<String, Void, Boolean> {
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
            progressDialog.setMessage("Verficando clave...");
            progressDialog.show();
        }


        /*******************************************************************************
         Método       : doInBackground
         Description  : Se ejecuta para realizar la transacción y verificar coenxión
         *******************************************************************************/
        @Override
        protected Boolean doInBackground(String... strings) {
            Messages.packMsgValidarClave();

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
                if (Messages.unPackMsgValidarClave(objeto)) {
                    Global.enSesion = true;
                    Global.StatusExit = true;
                    habilitar_inhabilitar();
                } else {
                    // Si no es OK, manda mensaje de error
                    try {
                        // Utils.GoToNextActivity(Activity_login.this, DialogError.class, Global.StatusExit);
                        if (Global.mensaje.equalsIgnoreCase("incomplete petition")) {
                            Global.mensaje = "Faltaron datos";
                        }if (Global.mensaje.equalsIgnoreCase("invalid email")) {
                            Global.mensaje = "El correo no es válido";
                        }if (Global.mensaje.equalsIgnoreCase("invalid  password")) {
                            Global.mensaje = "Contraseña inválida";
                        }

                        Toast.makeText(objeto, Global.mensaje, Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                limpiar();
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
                Toast.makeText(objeto, Global.mensaje, Toast.LENGTH_LONG).show();
                limpiar();
            }

        }


    }

    //este metodo se utiliza para desacrtivar Edit-text y botones
    private void habilitar_inhabilitar() {
        perfil_clave_actual.setEnabled(false);
        btn_cancelar.setOnClickListener(null);
        btn_validar.setOnClickListener(null);
        layout_datos_cambiar_clave.setVisibility(View.VISIBLE);
    }

    private void limpiar() {
        this.perfil_clave_actual.setText("");
        this.perfil_clave_confirmar.setText("");
        this.perfil_clave_nueva.setText("");
    }

    /*******************************************************************************
     Clase       : TaskCambiarClave
     Description : Realiza la transacción de los parámetros para actualizar clave
     *******************************************************************************/


    class TaskCambiarClave extends AsyncTask<String, Void, Boolean> {
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
            progressDialog.setMessage("Actualizando contraseña...");
            progressDialog.show();
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

            progressDialog.dismiss();

            if (value) {

                if (Messages.unPackMsgCambiarClave(objeto)) {
                    Global.enSesion = true;
                    Global.StatusExit = true;

                    Toast.makeText(objeto, "Contraseña actualizada", Toast.LENGTH_SHORT).show();
                    objeto.getSupportFragmentManager().beginTransaction().replace(R.id.contenedor_main, new PerfilFragment()).commit();

                } else {
                    // Si el login no es OK, manda mensaje de error
                    try {
                        Toast.makeText(objeto, "Falló actualización de contraseña", Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    // Limpia el login

                }

                limpiar();

            } else {//falla la conexión
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
                limpiar();

            }

        }


    }
}
