package com.example.wposs_user.polariscoreandroid.Fragmentos;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
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
import com.android.volley.toolbox.Volley;
import com.example.wposs_user.polariscoreandroid.Adaptadores.AdapterTerminalStock;
import com.example.wposs_user.polariscoreandroid.Comun.Global;
import com.example.wposs_user.polariscoreandroid.Dialogs.DialogOpcionesConsulta;
import com.example.wposs_user.polariscoreandroid.R;
import com.example.wposs_user.polariscoreandroid.Comun.Tools;
import com.example.wposs_user.polariscoreandroid.java.MyValueFormatter;
import com.example.wposs_user.polariscoreandroid.java.Productividad;
import com.example.wposs_user.polariscoreandroid.java.Terminal;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;
import static com.example.wposs_user.polariscoreandroid.Actividades.MainActivity.objeto;


public class ConsultaTerminalesFechas extends Fragment {

    private EditText f_inicio;
    private EditText f_fin;
    private TextView text_estado_ter;
    private Button btn_fech_consulta_serial;
    private ImageView buscar_terminales_fecha;
    private LinearLayout layout_estado_terminal;
    private RecyclerView rv;
    ArrayList<Terminal> terminales;
    private RequestQueue queue;
    View view;

    public static String Fecha1, Fecha2;

    boolean isChanged = false;
    private static final String CERO = "0";
    private static final String BARRA = "/";

    FragmentManager fragmentManager;
    public final java.util.Calendar c = java.util.Calendar.getInstance();
    final int mes = c.get(java.util.Calendar.MONTH);
    final int dia = c.get(java.util.Calendar.DAY_OF_MONTH);
    final int anio = c.get(java.util.Calendar.YEAR);


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_consulta_terminales_fechas, container, false);
        objeto.setTitulo("BÚSQUEDA POR FECHAS");
        buscar_terminales_fecha=(ImageView)view.findViewById(R.id.btn_buscar_terminalesPorFechas) ;

        terminales= new ArrayList<>();
        queue = Volley.newRequestQueue(objeto);

        rv= (RecyclerView)view.findViewById(R.id.recycler_view_consultaTerminalesFecha);
        buscar_terminales_fecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                consumirServicioBusquedaFecha();
            }
        });

        //carga los txt de las fechas al hacer la consulta establecida por fechas

        f_inicio = (EditText) view.findViewById(R.id.txt_fecha_inicio);
        f_fin = (EditText) view.findViewById(R.id.txt_fecha_fin);

        btn_fech_consulta_serial = (Button) view.findViewById(R.id.btn_fech_consulta_serial);


        btn_fech_consulta_serial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                objeto.getSupportFragmentManager().beginTransaction().replace(R.id.contenedor_main, new ConsultaTerminalesSerial()).addToBackStack(null).commit();
            }
        });



        f_inicio.setInputType(InputType.TYPE_NULL);
        f_fin.setInputType(InputType.TYPE_NULL);
        f_inicio.setInputType(InputType.TYPE_NULL);


//Mostrar calendario y seleccionar fecha para mostrar en el TExtView
        java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        Date date = new Date();
        String fecha = dateFormat.format(date);
        fecha = fecha.replace("-", "/");



        f_inicio.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (isChanged) {
                    return;
                }
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        Fecha1 = f_inicio.getText().toString();
                        f_inicio.setText(Tools.dateDDMMYYYYStr2(f_inicio.getText().toString()));
                        return;
                    }
                }, 70);
                isChanged = true;
            }
        });
        f_fin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (isChanged) {
                    return;
                }
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        Fecha2 = f_fin.getText().toString();
                        f_fin.setText(Tools.dateDDMMYYYYStr2(f_fin.getText().toString()));
                        return;
                    }
                }, 70);
                isChanged = true;
            }
        });
        f_inicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isChanged = false;
                showDatePickerDialog(f_inicio);
            }
        });
        f_fin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isChanged = false;
                showDatePickerDialog(f_fin);
            }
        });



//Fin mostrar calendario


        return view;

    }

    /**
     * BUSQUEDA DE TERMINALES
     **/

    public void opcionesBusqueda() {
        DialogOpcionesConsulta dialog = new DialogOpcionesConsulta();
        dialog.show(objeto.getSupportFragmentManager(), "Dialog");
    }

    private void showDatePickerDialog(final EditText etFecha) {
        DatePickerDialog recogerFecha = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                //Esta variable lo que realiza es aumentar en uno el mes ya que comienza desde 0 = enero
                final int mesActual = month + 1;
                //Formateo el día obtenido: antepone el 0 si son menores de 10
                String diaFormateado = (dayOfMonth < 10) ? CERO + String.valueOf(dayOfMonth) : String.valueOf(dayOfMonth);
                //Formateo el mes obtenido: antepone el 0 si son menores de 10
                String mesFormateado = (mesActual < 10) ? CERO + String.valueOf(mesActual) : String.valueOf(mesActual);
                //Muestro la fecha con el formato deseado
                etFecha.setText(diaFormateado + BARRA + mesFormateado + BARRA + year);


            }
            //Estos valores deben ir en ese orden, de lo contrario no mostrara la fecha actual
            /**
             *También puede cargar los valores que usted desee
             */
        }, anio, mes, dia);
        //Muestro el widget
        recogerFecha.show();
    }






    public boolean validarFecha() {

        String fecha_inicial = f_inicio.getText().toString();
        String[] fecha = fecha_inicial.split("/");
        int dia_inicio = Integer.parseInt(fecha[0]);
        int mes_inicio = Integer.parseInt(fecha[1]);
        int año_inicio = Integer.parseInt(fecha[2]);


        String fecha_final = f_fin.getText().toString();
        String[] fechaFin = fecha_final.split("/");
        int dia_fin = Integer.parseInt(fechaFin[0]);
        int mes_fin = Integer.parseInt(fechaFin[1]);
        int año_fin = Integer.parseInt(fechaFin[2]);


        if (año_fin < año_inicio) {  return false; }

         if (mes_fin < mes_inicio) {  return false; }

        if (dia_fin < dia_inicio) {   return false; }

        return true;

    }



    /********************Metodo usado para obtener el numero de terminales en un rango de fechas dado*************************************/////////
    public void consumirServicioBusquedaFecha() {



           String data_inicio = f_inicio.getText().toString();
           String data_fin = f_fin.getText().toString();


        if (data_inicio.isEmpty()||data_fin.isEmpty()) {
            Toast.makeText(view.getContext(), "Debe seleccionar la fecha de inicio y fin", Toast.LENGTH_SHORT).show();
            rv.setAdapter(null);
            terminales= new ArrayList<>();
            return;

        }



        String fecha_inicial = f_inicio.getText().toString();
        String[] fecha = fecha_inicial.split("/");
        final int dia_inicio = Integer.parseInt(fecha[0]);
        final int mes_inicio = Integer.parseInt(fecha[1]);
        final int anio_inicio = Integer.parseInt(fecha[2]);


        String fecha_final = f_fin.getText().toString();
        String[] fechaFin = fecha_final.split("/");
        final int dia_final = Integer.parseInt(fechaFin[0]);
        final int mes_final = Integer.parseInt(fechaFin[1]);
        final int anio_final = Integer.parseInt(fechaFin[2]);

        String fecha_inicio = mes_inicio+"/"+dia_inicio+"/"+anio_inicio;
        String fecha_fin = mes_final+"/"+dia_final+"/"+anio_final;

        if(!validarFechaActual(dia_inicio,mes_inicio,anio_inicio)){

            Toast.makeText(objeto, "La fecha debe ser igual o anterior a la fecha actual", Toast.LENGTH_SHORT).show();
            f_inicio.setText(" ");
            f_fin.setText(" ");
            rv.setAdapter(null);
            terminales= new ArrayList<>();
            f_inicio.setText("");
            f_fin.setText("");

            return;

        }

        if(!validarFechaActual(dia_final,mes_final,anio_final)){

            Toast.makeText(objeto, "La fecha debe ser igual o anterior a la fecha actual", Toast.LENGTH_SHORT).show();
            f_inicio.setText(" ");
            f_fin.setText(" ");
            rv.setAdapter(null);
            terminales= new ArrayList<>();
            f_inicio.setText("");
            f_fin.setText("");
            return;

        }

        if(!this.validarFecha()){

            Toast.makeText(view.getContext(), "La fecha de inicio debe ser anterior a la final", Toast.LENGTH_SHORT).show();
            f_inicio.setText(" ");
            f_fin.setText(" ");
            rv.setAdapter(null);
            terminales= new ArrayList<>();
            f_inicio.setText("");
            f_fin.setText("");
            return;

        }


        final Gson gson = new GsonBuilder().create();

        String url = "http://100.25.214.91:3000/PolarisCore/Terminals/finddate";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user", Global.CODE);
            jsonObject.put("fechaInicio", fecha_inicio);
            jsonObject.put("fechaFin", fecha_fin);
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

                            if (Global.STATUS_SERVICE.equalsIgnoreCase("fail")) {
                                Global.mensaje = response.get("message").toString();
                                if (Global.mensaje.equalsIgnoreCase("token no valido")) {
                                    Toast.makeText(objeto, "Su sesión ha expirado, debe iniciar sesión nuevamente", Toast.LENGTH_SHORT).show();
                                    objeto.consumirSercivioCerrarSesion();
                                    return;
                                }
                                return;
                            }


                            response = new JSONObject(response.toString());


                            JSONArray jsonArray = response.getJSONArray("diagnosticos");
                            JSONArray jsonArray2 = response.getJSONArray("reparaciones");


                            if (jsonArray.length() == 0 && jsonArray2.length() == 0) {
                                Global.mensaje = "No se encontraron registros para la fecha seleccionada";
                                Toast.makeText(view.getContext(), Global.mensaje, Toast.LENGTH_SHORT).show();

                          }

                            Terminal ter;

                            for (int i = 0; i < jsonArray.length(); i++) {
                                String res = jsonArray.getString(i);

                               ter = gson.fromJson(res, Terminal.class);

                                if (ter != null) {
                                }
                                terminales.add(ter);
                            }

                            for (int i = 0; i < jsonArray2.length(); i++) {
                                String res = jsonArray2.getString(i);

                                ter = gson.fromJson(res, Terminal.class);

                                if (ter != null) {

                                    terminales.add(ter);
                                }

                            }



                            rv.setHasFixedSize(true);
                            LinearLayoutManager llm = new LinearLayoutManager(Tools.getCurrentContext());
                            rv.setLayoutManager(llm);
                            AdapterTerminalStock adapter= new AdapterTerminalStock(view.getContext(),terminales);
                            rv.setAdapter(adapter);
                            f_inicio.setText("");
                            f_fin.setText("");


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
                        if (error.getMessage() != null) {
                            if (!error.getMessage().isEmpty()){
                                Toast.makeText(objeto, "ERROR\n " + error.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
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

    public boolean validarFechaActual(int dia, int mes, int año){

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Date date = new Date();
        String fechaActual = dateFormat.format(date);
        String []fechaAct= fechaActual.split("-");
        int anioAct=Integer.parseInt( fechaAct[0]);
        int mesAct=Integer.parseInt( fechaAct[1]);
        int diaAct= Integer.parseInt(fechaAct[2]);

        if(año>anioAct){ return false;}

        if(anio<anioAct){ return true;}


        if(anio==anioAct){

            if(mes>mesAct){return false;}
            if(mes<mesAct){return true;}
            if(mes==mesAct){
                if(dia>diaAct){return false;}
            }
        }

        return true;


    }





}
