package com.example.wposs_user.polariscoreandroid.Actividades;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
import com.example.wposs_user.polariscoreandroid.Comun.Global;
import com.example.wposs_user.polariscoreandroid.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Reestablecer_password extends AppCompatActivity {

    private Button btn_buscar_usuario;
    private Button btn_enviar_correo;
    private EditText txt_codigo_usuario;

    private TextView txt_userRecuperacion;
    private TextView txt_correoRecuperacion;
    private TextView txt_estadoRecuperacion;

    private LinearLayout layout_enviarRespuesta;

    private RequestQueue queue;
    private String codigo;
    private String identificacionRespuesta;
    private String codigoRespuesta;
    private String correoRespuesta;
    private String estadoRespuesta;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reestablecer_password);
        queue = Volley.newRequestQueue(Reestablecer_password.this);
        btn_buscar_usuario = (Button) findViewById(R.id.btn_buscar_usuario);
        btn_enviar_correo = (Button) findViewById(R.id.btn_enviar_correo);
        txt_codigo_usuario = (EditText) findViewById(R.id.txt_codigo_usuario);

        txt_userRecuperacion = (TextView) findViewById(R.id.txt_userRecuperacion);
        txt_correoRecuperacion = (TextView) findViewById(R.id.txt_correoRecuperacion);
        txt_estadoRecuperacion = (TextView) findViewById(R.id.txt_estadoRecuperacion);


        layout_enviarRespuesta = (LinearLayout) findViewById(R.id.layout_enviarRespuesta);

        layout_enviarRespuesta.setVisibility(View.GONE);

        btn_buscar_usuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                codigo = txt_codigo_usuario.getText().toString().trim();
                if (codigo.isEmpty()) {
                    Toast.makeText(Reestablecer_password.this, "Por favor ingrese su código", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    consumirServicioBuscarUsuario();
                }

            }
        });
        btn_enviar_correo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                consumirServicioEnviarCorreo();
            }
        });

    }

    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(this, Activity_login.class);
        startActivity(i);
        finish();
    }

    /**
     * Metodo utilizado para consumir el servicio que consulta el usurio
     * en la cabecera es enviado el cod del usuario
     */
    public void consumirServicioBuscarUsuario() {

        String url = "http://100.25.214.91:3000/PolarisCore/Users/userByCode";
        final JSONObject jsonObject = new JSONObject();

        JsonObjectRequest jsArrayRequest = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,//cuerpo de la peticion
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            System.out.println("respuesta serv: " + response.get("message").toString());
                            if (response.get("status").toString().equalsIgnoreCase("fail")) {
                                Global.mensaje = response.get("message").toString();
                                Toast.makeText(getApplicationContext(), "ERROR: " + Global.mensaje, Toast.LENGTH_SHORT).show();
                                return;
                            } else {

                                System.out.println("-------RESPUESTA--------" + response.get("result").toString());

                                //   response = new JSONObject(response.get("result").toString());
                                if (response.get("result").toString().equalsIgnoreCase("[]")) {
                                    Toast.makeText(Reestablecer_password.this, "No existe el usuario", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                estadoRespuesta = ((JSONArray) response.get("result")).getJSONObject(0).getString("user_state").toString();
                                codigoRespuesta = ((JSONArray) response.get("result")).getJSONObject(0).getString("user_id_user").toString();
                                correoRespuesta = ((JSONArray) response.get("result")).getJSONObject(0).getString("user_email").toString();
                                identificacionRespuesta = ((JSONArray) response.get("result")).getJSONObject(0).getString("user_identification").toString();

                                layout_enviarRespuesta.setVisibility(View.VISIBLE);
                                txt_userRecuperacion.setText(codigoRespuesta);
                                txt_correoRecuperacion.setText(correoRespuesta);
                                txt_estadoRecuperacion.setText(estadoRespuesta);
                                txt_codigo_usuario.setText("");
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
                        Toast.makeText(Reestablecer_password.this, "ERROR\n " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("userCode", codigo.toUpperCase());

                return params;
            }
        };
        queue.add(jsArrayRequest);
    }


    /**
     * Metodo utilizado para consumir el servicio que consulta el usurio
     * en la cabecera es enviado el cod del usuario
     */
    public void consumirServicioEnviarCorreo() {

        String url = "http://100.25.214.91:3000/PolarisCore/Mail/sendMail";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("correo", correoRespuesta);
            jsonObject.put("identificacion", identificacionRespuesta);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsArrayRequest = new JsonObjectRequest(
                Request.Method.POST,
                url,
                jsonObject,//cuerpo de la peticion
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            System.out.println("respuesta serv: " + response.get("message").toString());
                            if (response.get("status").toString().equalsIgnoreCase("fail")) {
                                Global.mensaje = response.get("message").toString();
                                if(Global.mensaje.equalsIgnoreCase("Mail error")){
                                    Toast.makeText(getApplicationContext(), "ERROR: El correo no existe" , Toast.LENGTH_SHORT).show();
                                    return;
                                } if(response.get("message").toString().equalsIgnoreCase("Query error")){
                                    System.out.println("Menasaje de eeror: "+response.get("error").toString());
                                    System.out.println("codifo"+identificacionRespuesta);
                                    Toast.makeText(getApplicationContext(), "ERROR: "+response.get("error").toString(), Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                Toast.makeText(getApplicationContext(), "ERROR: " + Global.mensaje, Toast.LENGTH_SHORT).show();
                                return;
                            } else {
                                Toast.makeText(Reestablecer_password.this, "Se ha enviado al correo la contraseña temporal", Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(getApplicationContext(), Activity_login.class);
                                startActivity(i);
                                finish();
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
                        Toast.makeText(Reestablecer_password.this, "ERROR\n " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                return params;
            }
        };
        queue.add(jsArrayRequest);
    }
}
