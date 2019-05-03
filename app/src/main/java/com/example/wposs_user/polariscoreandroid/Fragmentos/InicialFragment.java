package com.example.wposs_user.polariscoreandroid.Fragmentos;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.textclassifier.TextLinks;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.wposs_user.polariscoreandroid.Actividades.MainActivity;
import com.example.wposs_user.polariscoreandroid.Adaptadores.AdapterTerminal_asociada;
import com.example.wposs_user.polariscoreandroid.Comun.Global;
import com.example.wposs_user.polariscoreandroid.Comun.Tools;
import com.example.wposs_user.polariscoreandroid.R;
import com.example.wposs_user.polariscoreandroid.java.Terminal;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static com.example.wposs_user.polariscoreandroid.Actividades.MainActivity.objeto;

public class InicialFragment extends Fragment {
    private RecyclerView rv;
    private String serialObtenido;
    private RequestQueue queue;
    private Button btn_asociadas;
    private Button btn_autorizadas;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_inicial, container, false);
        btn_asociadas = (Button) v.findViewById(R.id.btn_terminales_asociadas);
        btn_autorizadas = (Button) v.findViewById(R.id.btn_terminales_autorizadas);
        rv = (RecyclerView) v.findViewById(R.id.recycler_view_consultaTerminales_inicial);
        serialObtenido = "";
        Global.TERMINALES_ASOCIADAS = new ArrayList<Terminal>();
        queue = Volley.newRequestQueue(objeto);
        btn_asociadas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //colocar que a lo que seleccione cierto botn, cambie el color de la linea de abajo
                btn_autorizadas.setBackgroundColor(getResources().getColor(R.color.azul_nav_bar_transparencia));//azul_nav_bar_transparencia

                btn_asociadas.setBackgroundColor(getResources().getColor(R.color.azul_claro_nav_bar));
            }
        });

        consumirServicio();


        return v;

    }

    static Terminal t;

    public void consumirServicio() {
        t = null;
        final Gson gson = new GsonBuilder().create();

    /*    TaskListarAsociadas task = new TaskListarAsociadas(objeto, "http://100.25.214.91:3000/PolarisCore/Terminals//associatedsWithDiagnosis", Global.CODE);
        task.execute();*/
        Log.d("ENTRO", "si");

        String url = "http://100.25.214.91:3000/PolarisCore/Terminals//associatedsWithDiagnosis";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("code", Global.CODE);
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
                            Global.STATUS_SERVICE = response.get("status").toString();
                            System.out.println("status:  " + Global.STATUS_SERVICE);

                            if (Global.STATUS_SERVICE.equalsIgnoreCase("fail")) {
                                Global.mensaje = response.get("message").toString();
                                return;
                            }


                            response = new JSONObject(response.get("data").toString());


                            JSONArray jsonArray = response.getJSONArray("terminales");
                            System.out.println("Tamaño Array: " + jsonArray.length());


                            if (jsonArray.length() == 0) {
                                Global.mensaje = "No tiene terminales asociadas";
                                return;
                            }
                            String ter = null;

                            for (int i = 0; i < jsonArray.length(); i++) {
                                ter = jsonArray.getString(i);

                                t = gson.fromJson(ter, Terminal.class);
                                if (t != null) {
                                    System.out.println("terminal " + i + ": " + t.toString());
                                }
                                Global.TERMINALES_ASOCIADAS.add(t);
                                System.out.println("tamaño Global.TERMINALES_ASOCIADAS.add(t):  " + Global.TERMINALES_ASOCIADAS.size());
                            }
                            llenarRVAsociadas(Global.TERMINALES_ASOCIADAS);
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

                    }
                }

        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authenticator", Global.TOKEN);
                //...

                return params;
            }
        };

        queue.add(jsArrayRequest);

    }

    public void llenarRVAsociadas(List<Terminal> terminalesRecibidas) {
        System.out.println("tamaño lista ter: " + Global.TERMINALES_ASOCIADAS.size());
        if (Global.TERMINALES_ASOCIADAS == null || Global.TERMINALES_ASOCIADAS.size() == 0) {
            Toast.makeText(objeto, Global.CODE + " No tiene terminales asociadas", Toast.LENGTH_SHORT).show();
            return;
        } else {

            rv.setHasFixedSize(true);

            LinearLayoutManager llm = new LinearLayoutManager(Tools.getCurrentContext());
            rv.setLayoutManager(llm);

            ArrayList terminals = new ArrayList<>();

            for (Terminal ter : terminalesRecibidas) {
                if (ter != null) {
                    terminals.add(ter);//  butons.add(new ButtonCard(nombre, "","",icon,idVenta));
                }
            }


            final AdapterTerminal_asociada adapter = new AdapterTerminal_asociada(terminals, new AdapterTerminal_asociada.interfaceClick() {//seria termi asoc
                @Override
                public void onClick(List<Terminal> terminal, int position) {


                    serialObtenido = terminal.get(position).getTerm_serial();
                    Global.modelo = terminal.get(position).getTerm_model();

                    objeto.listarObservacionesTerminal(serialObtenido);
                }
            }, R.layout.panel_terminal_asociada);

            rv.setAdapter(adapter);
        }
    }






 /*

    public class TaskListarAsociadas extends AsyncTask<Void, Void, String> {

        private Context httpContext;
        ProgressDialog progressDialog;
        public String resultadoapi = "";
        public String linkrequestAPI = ""; //link para consumir el servicio rest
        private String codigo = "";
        private String token = "";


        public TaskListarAsociadas(Context httpContext, String linkAPI, String code) {
            this.httpContext = httpContext;
            this.linkrequestAPI = linkAPI;
            this.codigo = code;
            this.token = token;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(httpContext, "Procesando solicitud", "por favor espere");
            Toast.makeText(httpContext, resultadoapi, Toast.LENGTH_SHORT).show();
        }


        @Override
        protected String doInBackground(Void... voids) {
            String result = null;

            *//*String wsURL = linkrequestAPI;
            URL url = null;
            try {
                //se crea la conexion al api: http://100.25.214.91:3000/PolarisCore/Terminals//associatedsWithDiagnosis
                url = new URL(wsURL);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestProperty("Authenticator", token);
                //crear el obj json para enviar por post
                JSONObject parametrosPost = new JSONObject();;
                parametrosPost.put("code", codigo);

                //Parametros de conexion
                urlConnection.setReadTimeout(15000);
                urlConnection.setConnectTimeout(15000);
                urlConnection.setRequestMethod("POST"); //delete, put, etc...
                urlConnection.setDoInput(true);
                urlConnection.setDoOutput(true);

                //obtener resultado request

                OutputStream os = urlConnection.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                writer.write(getPostDataString(parametrosPost));
                writer.flush();
                writer.close();
                os.close();

                int responseCode = urlConnection.getResponseCode();//ok?
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuffer sb = new StringBuffer("");
                    String linea = "";
                    while ((linea = in.readLine()) != null) {
                        sb.append(linea);
                        break;
                    }
                    in.close();
                    result = sb.toString();
                } else {
                    result = new String("Error: " + responseCode);
                }

            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }*//*

//            peticionPost("http://100.25.214.91:3000/PolarisCore/Terminals//associatedsWithDiagnosis", "{\"code\":\"PANGEL123\"}");
            return "";
        }



        public int peticionPost(String strUrl, String data){
            HttpURLConnection http=null;
            int responseCode=-1;

            URL url= null;
            try {
                url = new URL(strUrl);
                http=(HttpURLConnection)url.openConnection();
                http.setRequestMethod("POST");
                http.setRequestProperty("Authenticator", Global.TOKEN);
                http.setRequestProperty("Content-Type", "application/json");
                http.setDoOutput(true);
                PrintWriter writer =new PrintWriter(http.getOutputStream());
                writer.print(data);
                writer.flush();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                if(http!=null)
                    http.disconnect();
            }
            return responseCode;

        }

        //FUNCIONES
        //Transformar object a String

        public String getPostDataString(JSONObject params) throws Exception {
            StringBuilder result = new StringBuilder();
            boolean first = true;
            Iterator<String> itr = params.keys();
            while (itr.hasNext()) {
                String key = itr.next();
                Object value = params.get(key);

                if (first)
                    first = false;
                else
                    result.append("&");

                result.append(URLEncoder.encode(key, "UTF-8"));
                result.append("=");
                result.append(URLEncoder.encode(value.toString(), "UTF-8"));
            }
            return result.toString();
        }


    }*/

}
