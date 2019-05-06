package com.example.wposs_user.polariscoreandroid.Fragmentos;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.example.wposs_user.polariscoreandroid.Adaptadores.AdapterValidaciones;
import com.example.wposs_user.polariscoreandroid.Comun.Global;
import com.example.wposs_user.polariscoreandroid.R;
import com.example.wposs_user.polariscoreandroid.Comun.Tools;
import com.example.wposs_user.polariscoreandroid.java.Terminal;
import com.example.wposs_user.polariscoreandroid.java.Validacion;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.wposs_user.polariscoreandroid.Actividades.MainActivity.objeto;


public class ValidacionesTerminalesAsociadas extends Fragment {//CREO QUE ACA SE DEBE LLENAR EL RCV

    private TextView marca_ter_validaciones;
    private TextView modelo_ter_validaciones;
    private TextView serial_ter_validaciones;
    private TextView tecno_ter_validaciones;
    private TextView estado_ter_validaciones;
    private TextView garantia_ter_validaciones;
    private TextView fechal_ans_ter_validaciones;

    private Fragment fragment;

    private Button siguiente;
    private RecyclerView rv;
    private LinearLayout layout_encabezado_vali;
    private RequestQueue queue;
    private static Validacion v;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_validaciones_terminales_asociadas, container, false);
        System.out.println("infló panel");
        objeto.setTitle("VALIDACIONES");

        marca_ter_validaciones = (TextView) v.findViewById(R.id.marca_ter_validaciones);
        modelo_ter_validaciones = (TextView) v.findViewById(R.id.modelo_ter_validaciones);
        serial_ter_validaciones = (TextView) v.findViewById(R.id.serial_ter_validaciones);
        tecno_ter_validaciones = (TextView) v.findViewById(R.id.tecno_ter_validaciones);
        estado_ter_validaciones = (TextView) v.findViewById(R.id.estado_ter_validaciones);
        garantia_ter_validaciones = (TextView) v.findViewById(R.id.garantia_ter_validaciones);
        fechal_ans_ter_validaciones = (TextView) v.findViewById(R.id.fechal_ans_ter_validaciones);
        rv = (RecyclerView) v.findViewById(R.id.recycler_view_validaciones);
        layout_encabezado_vali = (LinearLayout) v.findViewById(R.id.layout_encabezado_vali);
        siguiente = (Button) v.findViewById(R.id.btn_siguiente_validaciones);
        queue = Volley.newRequestQueue(objeto);
        objeto.setTitle("Validaciones");

        //voy a recorrer el arreglo de terminales para que me liste la informacion de la terminal selecciona

        for (Terminal ter : Global.TERMINALES_ASOCIADAS) {
            if (ter.getTerm_serial().equalsIgnoreCase(Global.serial_ter)) {
                marca_ter_validaciones.setText(ter.getBrand());
                modelo_ter_validaciones.setText(ter.getTerm_model());
                serial_ter_validaciones.setText(ter.getTerm_serial());
                tecno_ter_validaciones.setText(ter.getTerm_technology());
                estado_ter_validaciones.setText(ter.getTerm_status());

                if (Integer.parseInt(ter.getTerm_warranty_time()) >= 0) {
                    garantia_ter_validaciones.setText("Con garantía");
                } else {
                    garantia_ter_validaciones.setText("Si garantía");
                }

                fechal_ans_ter_validaciones.setText(ter.getTerm_date_finish());

            }
        }
        siguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (llenarValidacionesDiagnostico()) {//valida que estén todas las validaciones marcadas
                    objeto.getSupportFragmentManager().beginTransaction().replace(R.id.contenedor_main, new TipificacionesFragment()).addToBackStack(null).commit();
                    return;
                }
            }
        });

        consumirServicio();


        return v;

    }

    /**
     * Metodo utilizado para consumir el servicio que lista las validaciones
     * en el encabezado se envía el token
     **/
    private void consumirServicio() {
        v = null;
        Global.VALIDACIONES = null;
        Global.VALIDACIONES = new ArrayList<Validacion>();
        final Gson gson = new GsonBuilder().create();

        String url = "http://100.25.214.91:3000/PolarisCore/Terminals/validatorTerminal";

        JsonObjectRequest jsArrayRequest = new JsonObjectRequest(
                Request.Method.POST,
                url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Global.STATUS_SERVICE = response.get("status").toString();
                            System.out.println("status:  " + Global.STATUS_SERVICE);

                            if (Global.STATUS_SERVICE.equalsIgnoreCase("fail")) {
                                Global.mensaje = response.get("message").toString();
                                return;
                            }


                            response = new JSONObject(response.get("data").toString());


                            JSONArray jsonArray = response.getJSONArray("validaciones");


                            if (jsonArray.length() == 0) {
                                layout_encabezado_vali.setVisibility(View.INVISIBLE);
                                Toast.makeText(objeto, "No hay validaciones registradas", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            layout_encabezado_vali.setVisibility(View.VISIBLE);

                            String ter = null;

                            for (int i = 0; i < jsonArray.length(); i++) {
                                ter = jsonArray.getString(i);

                                v = gson.fromJson(ter, Validacion.class);
                                if (v != null) {
                                }
                                Global.VALIDACIONES.add(v);
                            }
                            llenarRVValidaciones(Global.VALIDACIONES);
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


    //este metodo llena el recycler view con las terminales obtenidas al consumir el servicio

    public void llenarRVValidaciones(List<Validacion> validacionesRecibidas) {
        //************************SE MUESTRA LA LISTA DE TERMINALES ASOCIADAS
        rv.setHasFixedSize(true);

        LinearLayoutManager llm = new LinearLayoutManager(Tools.getCurrentContext());
        rv.setLayoutManager(llm);

        final ArrayList validaciones = new ArrayList<>();

        for (Validacion val : validacionesRecibidas) {
            if (val != null) {
                validaciones.add(val);
            }


            final AdapterValidaciones adapter = new AdapterValidaciones(validaciones, new AdapterValidaciones.interfaceClick() {
                @Override
                public void onClick(List<Validacion> listValidaciones, int position, int pos_radio) {
                    System.out.println("al dar clic---- position " + position + "pos_radio " + pos_radio);

                }


            }, R.layout.panel_validaciones);

            rv.setAdapter(adapter);

        }
        System.out.println("Tamaño del arreglo " + validaciones.size());
    }


    //Armo el arraylist     que voy a enviar al consumir el servicio de registrar diagnostico
    public boolean llenarValidacionesDiagnostico() {
        boolean retorno = false;
        Global.VALIDACIONES_DIAGNOSTICO = new ArrayList<Validacion>();
        String cadena = "";
        for (Validacion val : Global.VALIDACIONES) {
            if (val != null) {

                if (val.getEstado() == null) {
                    AlertDialog alertDialog = new AlertDialog.Builder(objeto).create();
                    alertDialog.setTitle("¡ATENCIÓN!");
                    alertDialog.setMessage("Debe marcar todas las validaciones");
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "ACEPTAR",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();
                    return false;
                } else {
                    Validacion v = new Validacion(Global.serial_ter, val.getTeva_description(), val.getEstado());
                    Global.VALIDACIONES_DIAGNOSTICO.add(v);
                    retorno = true;
                }

            }
        }
        return retorno;
    }

}
