package com.example.wposs_user.polariscoreandroid.Fragmentos;

import android.app.ProgressDialog;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.wposs_user.polariscoreandroid.Adaptadores.AdapterTerminal;
import com.example.wposs_user.polariscoreandroid.Adaptadores.AdapterTerminal_asociada;
import com.example.wposs_user.polariscoreandroid.Comun.Global;
import com.example.wposs_user.polariscoreandroid.Comun.Messages;
import com.example.wposs_user.polariscoreandroid.Comun.Tools;
import com.example.wposs_user.polariscoreandroid.Comun.Utils;
import com.example.wposs_user.polariscoreandroid.R;
import com.example.wposs_user.polariscoreandroid.TCP.TCP;
import com.example.wposs_user.polariscoreandroid.java.Terminal;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import static com.example.wposs_user.polariscoreandroid.Actividades.MainActivity.objeto;


public class StockFragment extends Fragment {


private View v;
private ImageView foto_perfil;
    private RequestQueue queue;

    public StockFragment() {
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        queue = Volley.newRequestQueue(objeto);
         v = inflater.inflate(R.layout.fragment_stock, container, false);
         foto_perfil=(ImageView)v.findViewById(R.id.img_photo_perfil);
         return v;

    }

    public void buscarFoto() {
        Global.WEB_SERVICE = "/PolarisCore/upload/view/:1093.jpg ";



    }


    /**********************************Servicio para obtener la foto*///////////////////////////
/*    public void consumirServicioFoto() {


        final Gson gson = new GsonBuilder().create();
        String url = "http://100.25.214.91:3000/PolarisCore/upload/view/:1093.jpg";
         JSONObject jsonObject = new JSONObject();

           JsonObjectRequest jsArrayRequest = new JsonObjectRequest(
                Request.Method.GET,
                url,
                jsonObject,
                new Response.Listener<Image>() {
                    @Override
                    public void onResponse(Image response) {

                        System.out.println("status:  " + Global.STATUS_SERVICE);

                        System.out.println("status:  " + Global.STATUS_SERVICE);

                        response = new JSONObject();



                        System.out.println("status:  " + response.length());



                        System.out.println("status:  " + response.toString());


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
                                return params;
            }
        };

        queue.add(jsArrayRequest);

    }*/


    }


