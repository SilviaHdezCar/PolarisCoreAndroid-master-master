package com.example.wposs_user.polariscoreandroid.Actividades;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
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

import static com.example.wposs_user.polariscoreandroid.Actividades.MainActivity.objeto;
import static com.example.wposs_user.polariscoreandroid.java.SharedPreferencesClass.eliminarValues;
import static com.example.wposs_user.polariscoreandroid.java.SharedPreferencesClass.eliminarValuesLogueoHuella;
import static com.example.wposs_user.polariscoreandroid.java.SharedPreferencesClass.saveValueStrPreferenceLogueoHuella;

public class Activity_UpdatePassword extends AppCompatActivity {


    private EditText txt_nueva;
    private EditText txt_confirmacion;
    private String nueva;
    private String confirmacion;

    Button cambioClave;
    String msj = "";
    private RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_password);

        queue = Volley.newRequestQueue(Activity_UpdatePassword.this);
        txt_nueva = (EditText) findViewById(R.id.txtClave1);
        txt_confirmacion = (EditText) findViewById(R.id.txtClave2);

        cambioClave = (Button) findViewById(R.id.cambio_clave);
        cambioClave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validarClave()) {
                    System.out.println("DIO CLIC EN CAMBIAR CLAVE");
                    consumirServicioCambiarClave();
                    System.out.println("FIN CAMBIAR CLAVE");
                }


            }
        });


    }


    public void onBackPressed() {
        super.onBackPressed();
        consumirSercivioCerrarSesion();

    }

    /*************************************************************************************
     *Metodo utilizado para cambiar la clave
     *
     ***************************************************** **/
    public boolean validarClave() {

        nueva = txt_nueva.getText().toString();
        confirmacion = txt_confirmacion.getText().toString();

        String msj = "ok";
        //validaciones
        if (nueva.isEmpty()) {
            Toast.makeText(this, "Por favor ingrese contraseña nueva", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (confirmacion.isEmpty()) {
            Toast.makeText(this, "Por favor ingrese  confirmarción de contraseña", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!(nueva.length() >= 8) || !revisarMayMinNum(nueva)) {
            Toast.makeText(this, "La contraseña no cumple con las condiciones requeridas", Toast.LENGTH_SHORT).show();
            txt_nueva.setText("");
            txt_confirmacion.setText("");
            return false;
        }
        if (!nueva.equals(confirmacion)) {
            Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
            txt_nueva.setText("");
            txt_confirmacion.setText("");
            return false;
        }

        return true;
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


    /*************************************************************************************
     *METODO QUE CONSUME EL SERVICIO PARA ACTUALIZAR CONTRASEÑA POR PRIMERA VEZ
     ***************************************************** **/
    public void consumirServicioCambiarClave() {
        String url = "http://100.25.214.91:3000/PolarisCore/Users/updatePassword";
        JSONObject jsonObject = new JSONObject();
        try {
            System.out.println("user_password " + txt_confirmacion.getText().toString());
            jsonObject.put("user_identification", Global.ID);
            jsonObject.put("user_password", txt_confirmacion.getText().toString());
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
                                    Global.mensaje = response.get("message").toString();
                                    if (Global.mensaje.equalsIgnoreCase("token no valido")) {
                                        AlertDialog alertDialog = new AlertDialog.Builder(objeto).create();
                                        alertDialog.setTitle("Información");
                                        alertDialog.setMessage("Su sesión ha expirado, debe iniciar sesión nuevamente ");
                                        alertDialog.setCancelable(true);
                                        alertDialog.show();
                                        objeto.consumirSercivioCerrarSesion();
                                        return;
                                    }
                                    Toast.makeText(objeto, "ERROR: " + Global.mensaje, Toast.LENGTH_SHORT).show();
                                    return;
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                actualizarLogueoHuella();
                                Toast.makeText(Activity_UpdatePassword.this, "Contraseña actualizada con éxito", Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(Activity_UpdatePassword.this, MainActivity.class);
                                startActivity(i);
                                finish();

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Log.d("RESPUESTA CERRAR SESIÓN", response.toString());
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


    /*************************************************************************************
     *METODO QUE CONSUME EL SERVICIO PARA CERRAR SESIÓN
     *Vuelve al login
     ***************************************************** **/
    public void consumirSercivioCerrarSesion() {
        String url = "http://100.25.214.91:3000/PolarisCore/Users/close";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user", Global.ID);
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

                                    Toast.makeText(Activity_UpdatePassword.this, "ERROR: " + Global.mensaje, Toast.LENGTH_SHORT).show();
                                    return;

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            eliminarValues(Activity_UpdatePassword.this);
                            Intent i = new Intent(Activity_UpdatePassword.this, Activity_login.class);
                            startActivity(i);
                            finish();
                            return;

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
                params.put("Authenticator", Global.TOKEN);//QUITAR
                return params;
            }
        };

        queue.add(jsArrayRequest);
    }

    public void actualizarLogueoHuella() { //guarda la clave y el usuario
        eliminarValuesLogueoHuella(getApplicationContext());
        saveValueStrPreferenceLogueoHuella(getApplicationContext(), "email_user", Global.EMAIL);
        saveValueStrPreferenceLogueoHuella(getApplicationContext(), "pass", txt_confirmacion.getText().toString());

    }
}


