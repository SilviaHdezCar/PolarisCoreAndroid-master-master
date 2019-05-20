package com.example.wposs_user.polariscoreandroid.Dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
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
import com.example.wposs_user.polariscoreandroid.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.example.wposs_user.polariscoreandroid.java.SharedPreferencesClass.eliminarValues;
import static com.example.wposs_user.polariscoreandroid.java.SharedPreferencesClass.saveValueStrPreference;
import static com.example.wposs_user.polariscoreandroid.java.SharedPreferencesClass.saveValueStrPreferenceLogueoHuella;

public class DialogGuardarCredenciales extends DialogFragment {

    private View view;
    private String correo;
    private String clave;
    private RequestQueue queue;

    private EditText email;
    private EditText contrasenia;
    private Button btn_aceptar;
    private Button btn_cancelar;
    private Button btn_activar_huella;
    private Button btn_cancelar_huella;

    private LinearLayout layout_informacion;
    private LinearLayout layout_credenciales;


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        view = getActivity().getLayoutInflater().inflate(R.layout.dialog_iniciar_sesion, null);
        email = (EditText) view.findViewById(R.id.txtCorreo_guardar_sesion);
        contrasenia = (EditText) view.findViewById(R.id.txtPass_guardar_sesion);
        btn_aceptar = (Button) view.findViewById(R.id.btn_guardarCredenciales);
        btn_cancelar = (Button) view.findViewById(R.id.btn_cancelar_Credenciales);
        btn_activar_huella = (Button) view.findViewById(R.id.btn_activar_huella);
        btn_cancelar_huella = (Button) view.findViewById(R.id.btn_cancelar_huella);


        layout_informacion = (LinearLayout) view.findViewById(R.id.layout_informacion);
        layout_credenciales = (LinearLayout) view.findViewById(R.id.layout_credenciales);

        layout_credenciales.setVisibility(View.GONE);


        queue = Volley.newRequestQueue(getContext());
        btn_activar_huella.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout_informacion.setVisibility(View.GONE);
                layout_credenciales.setVisibility(View.VISIBLE);
            }
        });
        btn_cancelar_huella.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        btn_aceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                aceptar();
            }
        });
        btn_cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        builder.setView(view);


        return builder.create();
    }

    public void aceptar() {
        correo = email.getText().toString();
        clave = contrasenia.getText().toString();

        if (correo.isEmpty() && clave.isEmpty()) {
            Toast.makeText(getContext(), "Ingrese correo y contraseña", Toast.LENGTH_SHORT).show();
            return;
        }
        if (correo.isEmpty()) {
            Toast.makeText(getContext(), "Debe ingresar el correo", Toast.LENGTH_SHORT).show();
            return;
        }
        if (clave.isEmpty()) {
            Toast.makeText(getContext(), "Ingrese la contraseña", Toast.LENGTH_SHORT).show();
            return;
        } else {
            consumirServicoLogin();
            dismiss();
        }
    }

    /**
     * mETODO UTILIZADO PARA CONSUMIR EL SEVICIO QUE PERMITE INICIAR SESIÓN
     */
    public void consumirServicoLogin() {

        String url = "http://100.25.214.91:3000/PolarisCore/Users/login";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user_email", this.correo);
            jsonObject.put("user_password", this.clave);
            jsonObject.put("gethash", "true");
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


                            if (response.get("message").toString().equalsIgnoreCase("error")) {
                                try {
                                    Global.mensaje = response.get("description").toString();
                                    Toast.makeText(getContext(), Global.mensaje, Toast.LENGTH_SHORT).show();
                                    System.out.println("description " + response.get("description").toString());
                                    if (Global.mensaje.equalsIgnoreCase("Contraseña inválida")) {
                                        email.setText("");
                                    } else {
                                        limpiarLogin();
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                return;

                            } else {
                                try {
                                    Global.TOKEN = response.get("token").toString();
                                    // Global.MESSAGE = lineastrama[1].substring(11, lineastrama[1].length() - 1);
                                    Global.ROL = response.get("roles").toString();
                                    Global.LOGIN = response.get("login").toString();
                                    Global.ID = response.get("id").toString();//CEDULA
                                    Global.STATUS = response.get("status").toString();
                                    Global.POSITION = response.get("position").toString();
                                    Global.CODE = response.get("code").toString();
                                    Global.NOMBRE = response.get("name").toString();
                                    Global.EMAIL = response.get("email").toString();
                                    Global.LOCATION = response.get("location").toString();
                                    Global.PHONE = response.get("phone").toString();
                                    // Global.PHOTO = jsonObject.get("photo").toString();;


                                    Log.i("------------STATUS: ", "" + Global.STATUS);
                                    Log.i("------------POSITION: ", "" + Global.POSITION);
                                    Log.i("------------TOKEN: ", "" + Global.TOKEN);
                                    Log.i("--------CODE: ", "" + Global.CODE);
                                    Log.i("-------NAME: ", "" + Global.NOMBRE);

                                    if (!Global.POSITION.equalsIgnoreCase("TÉCNICO")) {
                                        Global.mensaje = "El usuario no tiene permisos";
                                        Toast.makeText(getContext(), Global.mensaje, Toast.LENGTH_SHORT).show();
                                        limpiarLogin();
                                        cerrarSesion();
                                        return;
                                    } else if (Global.STATUS.equalsIgnoreCase("INACTIVO")) {
                                        Global.mensaje = "El usuario está inactivo";
                                        limpiarLogin();
                                        Toast.makeText(getContext(), Global.mensaje, Toast.LENGTH_SHORT).show();
                                        return;
                                    }

                                    guardarSesion();
                                    guardarLogueoHuella();


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                if (Integer.parseInt(Global.LOGIN) == 0) {
                                    Intent i = new Intent(getContext(), Activity_UpdatePassword.class);
                                    startActivity(i);
                                    getActivity().finish();
                                } else {
                                    Intent i = new Intent(getContext(), MainActivity.class);
                                    startActivity(i);
                                    getActivity().finish();
                                }
                                return;
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
                        Toast.makeText(getContext(), "ERROR\n " + error.getMessage(), Toast.LENGTH_SHORT).show();
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


    public void guardarLogueoHuella() { //guarda la clave y el usuario
        saveValueStrPreferenceLogueoHuella(getContext(), "email_user", correo);
        saveValueStrPreferenceLogueoHuella(getContext(), "pass", clave);

    }


    public void guardarSesion() {
        saveValueStrPreferenceLogueoHuella(getContext(), "token", Global.TOKEN);
        saveValueStrPreferenceLogueoHuella(getContext(), "rol", Global.ROL);
        saveValueStrPreferenceLogueoHuella(getContext(), "login", Global.LOGIN);
        saveValueStrPreferenceLogueoHuella(getContext(), "id", Global.ID);
        saveValueStrPreferenceLogueoHuella(getContext(), "status", Global.STATUS);
        saveValueStrPreferenceLogueoHuella(getContext(), "position", Global.POSITION);
        saveValueStrPreferenceLogueoHuella(getContext(), "code", Global.CODE);
        saveValueStrPreferenceLogueoHuella(getContext(), "nombre", Global.NOMBRE);
        saveValueStrPreferenceLogueoHuella(getContext(), "email", Global.EMAIL);
        saveValueStrPreferenceLogueoHuella(getContext(), "location", Global.LOCATION);
        saveValueStrPreferenceLogueoHuella(getContext(), "phone", Global.PHONE);
    }


    public void cerrarSesion() {
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
                                    Toast.makeText(getContext(), Global.mensaje, Toast.LENGTH_SHORT).show();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            eliminarValues(getContext());

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
                        Toast.makeText(getContext(), "ERROR\n " + error.getMessage(), Toast.LENGTH_SHORT).show();
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

    private void limpiarLogin() {
        this.email.setText("");
        this.contrasenia.setText("");
    }
}
