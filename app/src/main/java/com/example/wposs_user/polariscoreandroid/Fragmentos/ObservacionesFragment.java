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
import android.support.v4.app.FragmentManager;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.wposs_user.polariscoreandroid.Actividades.MainActivity;
import com.example.wposs_user.polariscoreandroid.Comun.Global;
import com.example.wposs_user.polariscoreandroid.Comun.Messages;
import com.example.wposs_user.polariscoreandroid.Comun.Utils;
import com.example.wposs_user.polariscoreandroid.R;
import com.example.wposs_user.polariscoreandroid.TCP.TCP;
import com.example.wposs_user.polariscoreandroid.java.Observacion;
import com.example.wposs_user.polariscoreandroid.java.Repuesto;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Method;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import static com.example.wposs_user.polariscoreandroid.Actividades.MainActivity.objeto;

public class ObservacionesFragment extends Fragment {
    private View v;
    private TextView btn_tomar_foto;
    private TextView btn_tomar_foto2;
    private TextView txt_observacion;
    private Button btn_cargarFotos;
    private ImageView imagen_observación;
    private ImageView imagen_observación2;
    private Button finalizar;

    private String nombre_foto1;
    private String nombre_foto2;
    Bitmap bitmap_foto1;
    Bitmap bitmap_foto2;
    private RequestQueue queue;
    private StringRequest stringRequest;
    private int foto;

    private LinearLayout layout_fotos;
    private LinearLayout layout_finalizar_diagnostico;

    private Observacion obser;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_observaciones, container, false);
        objeto.setTitle("               OBSERVACIÓN");


        imagen_observación = (ImageView) v.findViewById(R.id.imagen_observación);
        imagen_observación2 = (ImageView) v.findViewById(R.id.imagen_observación2);
        queue = Volley.newRequestQueue(objeto);
        finalizar = (Button) v.findViewById(R.id.btn_finalizar_observacion);
        layout_fotos = (LinearLayout) v.findViewById(R.id.layout_fotos);
        layout_finalizar_diagnostico = (LinearLayout) v.findViewById(R.id.layout_finalizar_diagnostico);

        layout_finalizar_diagnostico.setVisibility(View.INVISIBLE);

        txt_observacion = (TextView) v.findViewById(R.id.txt_observacion_fin);
        btn_tomar_foto = (TextView) v.findViewById(R.id.lbl_cargarFoto);
        btn_tomar_foto2 = (TextView) v.findViewById(R.id.lbl_cargarFoto2);
        btn_cargarFotos = (Button) v.findViewById(R.id.btn_cargarFotos);

        btn_tomar_foto.setPaintFlags(btn_tomar_foto.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        // nomFoto = Global.ID + ".jpg";
        bitmap_foto1 = null;
        bitmap_foto2 = null;

        foto = 0;

        btn_tomar_foto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                foto = 1;
                Intent intento1 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intento1, 0);
            }
        });

        btn_tomar_foto2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                foto = 2;
                Intent intento1 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intento1, 0);
            }
        });

        btn_cargarFotos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (bitmap_foto1 == null) {
                    Toast.makeText(objeto, "Por favor tome la primera foto", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (bitmap_foto2 == null) {
                    Toast.makeText(objeto, "Por favor tome la segunda foto", Toast.LENGTH_SHORT).show();
                    return;
                }
                consumirServicioEnviarFotos();

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


    /**
     * Este método se utiliza para cargar subir las fotos y activa el boton para finalizar el diagnostico
     **/
    public void consumirServicioEnviarFotos() {
        String url = "http://100.25.214.91:3000/PolarisCore/upload/uploadImgObservations/";
        JSONObject jsonObject = new JSONObject();

        String img1 = convertirImagenString(bitmap_foto1);
        String img2 = convertirImagenString(bitmap_foto2);
        //  String img1 = getString(R.string.img2_base64);

        System.out.println("id-->" + Global.ID);
        System.out.println("serial-->" + Global.serial_ter);

        Log.i("IMG1-->", img1);
        Log.i("IMG2-->", img2);
        //System.out.println("IMG2-->"+img2);
        try {
            jsonObject.put("identificacion", Global.ID);
            jsonObject.put("serial", Global.serial_ter);
            jsonObject.put("firstPhoto", img1);
            jsonObject.put("secondPhoto", img2);
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
                            System.out.println("RESPUESTA SERVIDOR-->" + response.get("message").toString());
                            if (!response.get("status").toString().equalsIgnoreCase("ok")) {
                                Global.mensaje = response.get("message").toString();
                                Toast.makeText(objeto, "Error al cargar las fotos", Toast.LENGTH_SHORT).show();
                                System.out.println("Error al cargar las fotos");
                                return;
                            } else {

                                response = new JSONObject(response.get("data").toString());

                                nombre_foto1 = response.get("image1").toString();
                                nombre_foto2 = response.get("image2").toString();

                                Toast.makeText(objeto, "Fotos cargadas correctamente", Toast.LENGTH_SHORT).show();
                                layout_fotos.setVisibility(View.INVISIBLE);
                                layout_finalizar_diagnostico.setVisibility(View.VISIBLE);
                                System.out.println("Fotos cargadas correctamente");
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {
                      @Override
                      public void onErrorResponse(VolleyError error) {
                         System.out.println("ERROR!!!!" + error.getMessage());
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


    /**
     * este método se utiliza pára convertir la imagen a String, para ser enviada al servisor
     **/
    public String convertirImagenString(Bitmap bitmap) {
        String img = "";
        ByteArrayOutputStream array = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, array);
        byte[] imageByte = array.toByteArray();
        img = Base64.encodeToString(imageByte, Base64.DEFAULT);

        return img;
    }


    //este metodo se utiliza para obetenr la foto tomada
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (foto == 1) {
            bitmap_foto1 = (Bitmap) data.getExtras().get("data");
            imagen_observación.setImageBitmap(bitmap_foto1);
        } else if (foto == 2) {
            bitmap_foto2 = (Bitmap) data.getExtras().get("data");
            imagen_observación2.setImageBitmap(bitmap_foto2);
        }


    }


    //Metodo utilizado para finalizar el diagnostico (Consume el servicio de finalizar diagnostico)
    public void finalizarDiagnostico() {
        String observacion = txt_observacion.getText().toString();
        String nomFotos = nombre_foto1 + "/" + nombre_foto2;
        if (observacion.isEmpty()) {
            Toast.makeText(objeto, "Por favor ingrese la observación", Toast.LENGTH_SHORT).show();
            return;
        } else {
            //consumir servicio finalizar diagnostico

            obser = new Observacion("", observacion, "", "", "", Global.serial_ter);
            consumirServicioDiagnostico();

        }
    }

    /**
     * Metodo utilizados para consumir el servicio  de listar terminales asociadas mediante una petición REST
     * En el encabezado va el token-> Authenticator
     **/
    public void consumirServicioDiagnostico() {


        String url = "http://100.25.214.91:3000/PolarisCore/Terminals/savediagnosis";
        System.out.println("TIPIFICACIONES ENVIADAS FINALIZAR DIAGNOSTICO: ");

        JSONObject jsonObject = new JSONObject();
        JSONObject obj2 = new JSONObject();
        try {

            JSONArray val = this.getValidaciones();
            jsonObject.put("validaciones", val);
            JSONArray tip = this.getTipificaciones();
            jsonObject.put("tipificaciones", tip);
            jsonObject.put("reparable", Global.reparable);
            jsonObject.put("observacion", obser.getObj());
            jsonObject.put("falla", "");
            obj2.put("tesw_serial", Global.serial_ter);
            JSONArray o = this.getRepuestos();
            obj2.put("tesw_repuestos", o);
            jsonObject.put("repuestos", obj2);


            Log.d("RESPUESTA", jsonObject.toString());


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
                            System.out.println("STATUS-->" + response.get("status").toString());
                            if (response.get("status").toString().equals("fail")) {
                                AlertDialog alertDialog = new AlertDialog.Builder(objeto).create();
                                alertDialog.setTitle("Informacion");
                                alertDialog.setMessage("Error: " + response.get("message").toString());
                                alertDialog.setCancelable(true);
                                alertDialog.show();

                            } else {
                                System.out.println(obser.toString());
                                System.out.println("creó bien el diagnostico");
                                AlertDialog alertDialog = new AlertDialog.Builder(objeto).create();
                                alertDialog.setTitle("Informacion");
                                alertDialog.setMessage("Diagnóstico registrado exitosamente");
                                alertDialog.setCancelable(true);
                                alertDialog.show();
                                eliminarPila();
                                objeto.getSupportFragmentManager().beginTransaction().replace(R.id.contenedor_main, new InicialFragment()).addToBackStack(null).commit();
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

    public void eliminarPila() {
        FragmentManager fm = getActivity().getSupportFragmentManager();
        for (int i = 0; i < fm.getBackStackEntryCount(); ++i) {
            fm.popBackStack();
        }

    }

    /**
     * METODO QUE RECORRE LA LISTA DE VALIDACIONES Y LAS AGREGA AL ARREGLO
     **/
    public JSONArray getValidaciones() throws JSONException {

        JSONArray listas = new JSONArray();

        for (int i = 0; i < Global.VALIDACIONES_DIAGNOSTICO.size(); i++) {
            JSONObject ob = Global.VALIDACIONES_DIAGNOSTICO.get(i).getObj();
            listas.put(ob);
        }

        return listas;
    }

    public JSONArray getTipificaciones() throws JSONException {

        JSONArray listas = new JSONArray();
        for (int i = 0; i < Global.TIPIFICACIONES_DIAGNOSTICO.size(); i++) {
            JSONObject ob = Global.TIPIFICACIONES_DIAGNOSTICO.get(i).getObj();
            listas.put(ob);
        }

        return listas;
    }

    public JSONArray getRepuestos() throws JSONException {
        Global.REPUESTOS_DIAGONOSTICO = null;
        Global.REPUESTOS_DIAGONOSTICO = new ArrayList<Repuesto>();
        JSONArray listas = new JSONArray();

        for (int i = 0; i < Global.REPUESTOS_DIAGONOSTICO.size(); i++) {
            JSONObject ob = Global.REPUESTOS_DIAGONOSTICO.get(i).getObj();
            listas.put(ob);
        }

        return listas;
    }

}
