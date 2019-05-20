package com.example.wposs_user.polariscoreandroid.Fragmentos;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.wposs_user.polariscoreandroid.Actividades.Activity_UpdatePassword;
import com.example.wposs_user.polariscoreandroid.Actividades.Activity_login;
import com.example.wposs_user.polariscoreandroid.Actividades.MainActivity;
import com.example.wposs_user.polariscoreandroid.Comun.Global;
import com.example.wposs_user.polariscoreandroid.Comun.Messages;
import com.example.wposs_user.polariscoreandroid.Comun.Utils;
import com.example.wposs_user.polariscoreandroid.R;
import com.example.wposs_user.polariscoreandroid.TCP.TCP;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static android.content.Context.INPUT_METHOD_SERVICE;
import static com.example.wposs_user.polariscoreandroid.Actividades.MainActivity.objeto;
import static com.example.wposs_user.polariscoreandroid.java.SharedPreferencesClass.eliminarValuesLogueoHuella;
import static com.example.wposs_user.polariscoreandroid.java.SharedPreferencesClass.saveValueStrPreferenceLogueoHuella;


public class ActualizarClave_perfil extends Fragment {

    private View v;
    private TextView btn_validar;
    private EditText perfil_clave_actual;
    private EditText perfil_clave_nueva;
    private EditText perfil_clave_confirmar;
    private LinearLayout layout_datos_cambiar_clave;
    private LinearLayout layout_clave_actual;
    private Button btn_aceptar_cambio_clave;
    private String actual;
    private String nueva;
    private String confirmacion;
    private RequestQueue queue;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_actualizar_clave_perfil, container, false);

        objeto.setTitle("ACTUALIZAR CONTRASEÑA");
        queue = Volley.newRequestQueue(objeto);
        btn_validar = (TextView) v.findViewById(R.id.lbl_validarClave);
        perfil_clave_actual = (EditText) v.findViewById(R.id.perfil_clave_actual);
        perfil_clave_nueva = (EditText) v.findViewById(R.id.perfil_clave_nueva);
        perfil_clave_confirmar = (EditText) v.findViewById(R.id.perfil_clave_confirmar);
        layout_datos_cambiar_clave = (LinearLayout) v.findViewById(R.id.layout_datos_cambiar_clave);
        layout_clave_actual = (LinearLayout) v.findViewById(R.id.layout_clave_actual);
        btn_aceptar_cambio_clave = (Button) v.findViewById(R.id.btn_aceptar_cambio_clave);

        layout_datos_cambiar_clave.setVisibility(View.INVISIBLE);


        actual = "";
        nueva = "";
        confirmacion = "";


        btn_validar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actual = perfil_clave_actual.getText().toString();

                if (actual.isEmpty()) {
                    Toast.makeText(objeto, "Por favor ingrese la clave actual", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    consumirServicoVerificarClave();
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
            limpiar();
            return;
        } else {
            consumirServicioCambiarClave();
        }
        InputMethodManager in = (InputMethodManager) objeto.getSystemService(INPUT_METHOD_SERVICE);
        in.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);

    }


    //Validaciones requeridas para cambiar la contraseña
    private String validarClave(String nueva, String confirmacion) {
        String msj = "ok";
        //validaciones
        if (nueva.isEmpty()) {
            return "Por favor ingrese contraseña nueva";
        } else if (confirmacion.isEmpty()) {
            return "Por favor ingrese  confirmarción de contraseña";
        } else if (nueva.equals(actual)) {
            return "La contraseña nueva no puede ser igual a la antigua";
        } else if (!(nueva.length() >= 8)) {
            return "La contraseña no cumple con las condiciones";
        } else if (!revisarMayMinNum(nueva)) {
            return "La contraseña no cumple con las condiciones";
        } else if (!nueva.equals(confirmacion)) {
            return "Las contraseñas no coinciden";
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
        if (contLetraMay > 0 && contNumero > 3 && contLetraMin > 0) {
            return true;
        }
        return false;
    }


    public void consumirServicoVerificarClave() {
        String url = "http://100.25.214.91:3000/PolarisCore/Users/verification";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user_email", Global.EMAIL);
            jsonObject.put("user_password", this.actual);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsArrayRequest = new JsonObjectRequest(
                Request.Method.POST,
                url,
                jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.get("status").toString().equalsIgnoreCase("fail")) {
                                try {
                                    Global.mensaje = response.get("message").toString();
                                    if (response.get("message").toString().equalsIgnoreCase("incomplete petition")) {
                                        Global.mensaje = "Faltaron datos";
                                    }
                                    if (response.get("message").toString().equalsIgnoreCase("invalid email")) {
                                        Global.mensaje = "El correo no es válido";
                                    }
                                    if (response.get("message").toString().equalsIgnoreCase("invalid  password")) {
                                        Global.mensaje = "Contraseña inválida";
                                    }
                                    if (response.get("message").toString().equalsIgnoreCase("token no valido")) {
                                        Toast.makeText(objeto, "Su sesión ha expirado, debe iniciar sesión nuevamente", Toast.LENGTH_SHORT).show();
                                        objeto.consumirSercivioCerrarSesion();
                                        return;
                                    }
                                    limpiar();
                                    Toast.makeText(objeto, "ERROR: " + Global.mensaje, Toast.LENGTH_SHORT).show();
                                    return;

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                return;

                            } else {
                                habilitar_inhabilitar();
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Log.d("RESPUESTA", response.toString());
                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("ERROR", "Error Respuesta en JSON: " + error.getMessage());
                        Toast.makeText(objeto, "ERROR\n " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authenticator", Global.TOKEN);
                return params;
            }
        };

        queue.add(jsArrayRequest);
    }


    //este metodo se utiliza para desacrtivar Edit-text y botones
    private void habilitar_inhabilitar() {
        perfil_clave_actual.setEnabled(false);
        btn_validar.setOnClickListener(null);
        layout_datos_cambiar_clave.setVisibility(View.VISIBLE);
        layout_clave_actual.setVisibility(View.INVISIBLE);
    }

    private void limpiar() {
        this.perfil_clave_actual.setText("");
        this.perfil_clave_confirmar.setText("");
        this.perfil_clave_nueva.setText("");
    }


    public void consumirServicioCambiarClave() {
        String url = "http://100.25.214.91:3000/PolarisCore/Users/updatePassword";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user_identification", Global.ID);
            jsonObject.put("user_password", this.nueva);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsArrayRequest = new JsonObjectRequest(
                Request.Method.PUT,
                url,
                jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (!response.get("message").toString().equalsIgnoreCase("success")) {
                                try {
                                    Global.mensaje = response.get("message").toString();
                                    if (response.get("message").toString().equalsIgnoreCase("token no valido")) {
                                        Toast.makeText(objeto, "Su sesión ha expirado, debe iniciar sesión nuevamente", Toast.LENGTH_SHORT).show();
                                        objeto.consumirSercivioCerrarSesion();
                                        return;
                                    }
                                    Toast.makeText(objeto, Global.mensaje, Toast.LENGTH_SHORT).show();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                actualizarLogueoHuella();
                                Toast.makeText(objeto, "Contraseña actualizada", Toast.LENGTH_SHORT).show();
                                objeto.getSupportFragmentManager().beginTransaction().replace(R.id.contenedor_main, new PerfilFragment()).addToBackStack(null).commit();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Log.d("RESPUESTA", response.toString());
                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("ERROR", "Error Respuesta en JSON: " + error.getMessage());
                        Toast.makeText(objeto, "ERROR\n " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authenticator", Global.TOKEN);
                return params;
            }
        };

        queue.add(jsArrayRequest);
    }

    public void actualizarLogueoHuella() { //guarda la clave y el usuario
        eliminarValuesLogueoHuella(objeto);
        saveValueStrPreferenceLogueoHuella(objeto, "email_user", Global.EMAIL);
        saveValueStrPreferenceLogueoHuella(objeto, "pass", this.nueva);

    }
}
