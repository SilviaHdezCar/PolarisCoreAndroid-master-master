package com.example.wposs_user.polariscoreandroid.Fragmentos;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
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
import com.example.wposs_user.polariscoreandroid.Comun.Utils;
import com.example.wposs_user.polariscoreandroid.R;
import com.example.wposs_user.polariscoreandroid.java.MyValueFormatter;
import com.example.wposs_user.polariscoreandroid.java.Productividad;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
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
import static android.view.View.SCROLLBARS_OUTSIDE_INSET;
import static android.view.View.VISIBLE;
import static com.example.wposs_user.polariscoreandroid.Actividades.MainActivity.objeto;


public class ProductividadSemana extends Fragment {

    private BarChart graficaSeman;
    private String[] aniosObt;
    private String [] semanasMes;
    private String[] mesesAnio;
    private TextView titulo;
    private View v;
    private Spinner anios;
    private Spinner meses;
    private Spinner semanas;
    private RequestQueue queue;

    private ArrayAdapter comboAnio;
    private ArrayAdapter comboMes;
    private ArrayAdapter comboSemana;
    private ArrayList<Productividad> productividad;
    private Button buscar_produc;
    private int [] producSem;



    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        v= inflater.inflate(R.layout.fragment_productividad_semana, container, false);
        obtenerAnios();

        graficaSeman=(BarChart)v.findViewById(R.id.grafica_semana);
        anios=(Spinner)v.findViewById(R.id.spin_semana_anio);
        meses=(Spinner)v.findViewById(R.id.spin_semana_mes);
        semanas=(Spinner)v.findViewById(R.id.spin_semana_semana);
        buscar_produc=(Button)v.findViewById(R.id.btn_productividad_semana);
        titulo=(TextView)v.findViewById(R.id.txt_tituloSemana);
        queue = Volley.newRequestQueue(objeto);

        mesesAnio= new String[]{"   ","Enero","Febrero","Marzo","Abril","Mayo","Junio","Julio","Agosto","Septiembre","Octubre","Noviembre","Diciembre"};

        comboAnio= new ArrayAdapter(objeto,R.layout.spiner_style_center,aniosObt);
        comboMes= new ArrayAdapter(objeto,R.layout.spiner_style_center,mesesAnio);

        anios.setAdapter(comboAnio);
        meses.setAdapter(comboMes);



        meses.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String r =mesesAnio[position];



                if(r.equals("Febrero")){

                    semanasMes= new String []{ "   ","Semana 1","Semana 2","Semana 3","Semana 4"};
                    comboSemana= new ArrayAdapter<String>(objeto,R.layout.spiner_style_center,semanasMes);
                    semanas.setAdapter(comboSemana);


                }
                if(!r.equals("Febrero")){

                    semanasMes= new String []{ "   ","Semana 1","Semana 2","Semana 3","Semana 4","Semana 5"};
                    comboSemana= new ArrayAdapter<String>(objeto,R.layout.spiner_style_center,semanasMes);
                    semanas.setAdapter(comboSemana);


                }

                if(r.equals("   ")){

                    semanasMes= new String []{ "   "};
                    comboSemana= new ArrayAdapter<String>(objeto,R.layout.spiner_style_center,semanasMes);
                    semanas.setAdapter(comboSemana);


                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        buscar_produc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                consumirServicioProductividadSemana();
            }
        });


        return v;
    }

    public void  obtenerAnios(){

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Date date = new Date();
        String fecha = dateFormat.format(date);

        String[] fechas= fecha.split("-");

        int anioActual= Integer.parseInt(fechas[0]);

        aniosObt= new  String[21];
        aniosObt[0]="  ";


        for(int i =1;i<aniosObt.length;i++){

            aniosObt[i]= String.valueOf(anioActual);
            anioActual--;

        }

    }




    /********************Metodo usado para obtener la productividad en una semana seleccionada*************************************/////////
    public void consumirServicioProductividadSemana() {

        graficaSeman.clear();

        productividad = new ArrayList<>();

        String anioSel= anios.getSelectedItem().toString();
        String mesSel= meses.getSelectedItem().toString();
        final String semanaSel= semanas.getSelectedItem().toString();


        if(anioSel.equals("   ")||mesSel.equals("   ")||semanaSel.equals("   ")){
            graficaSeman.setVisibility(View.GONE);
            graficaSeman.clear();
            titulo.setVisibility(INVISIBLE);
            semanas.setSelection(0);
            meses.setSelection(0);
            anios.setSelection(0);
            Toast.makeText(v.getContext(), "Debe seleccionar el año, mes y semana para realizar la consulta", Toast.LENGTH_SHORT).show();

            return;

        }

        final int diaInicio= getDiaInicio(semanaSel);
        final int diaFin= diaInicio+6;
        final int mesSelec= Utils.obtenerNumMes(mesSel.trim());
        final int anioSele=Integer.parseInt(anioSel.trim());


      if(!validarFechaActual(diaInicio,mesSelec)){

            Toast.makeText(v.getContext(), "La semana selecionada no puede ser posterior a la actual", Toast.LENGTH_SHORT).show();
            graficaSeman.clear();
            graficaSeman.setVisibility(View.GONE);
            titulo.setVisibility(INVISIBLE);
            semanas.setSelection(0);
            meses.setSelection(0);
            anios.setSelection(0);
            return;
        }



        String fecha_inicio = mesSelec+ "/" + diaInicio + "/" + anioSel;
         final String fecha_fin= mesSelec+"/" + diaFin + "/" + anioSel;


        System.out.println("FECHA DE INICIO DE LA SEMANA**  "+fecha_inicio);
        System.out.println("FECHA DE FIN DE LA SEMANA**  "+fecha_fin);

        final Gson gson = new GsonBuilder().create();

        String url = "http://100.25.214.91:3000/PolarisCore/Terminals/productivity";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user", Global.CODE);
            jsonObject.put("fechaInicial", fecha_inicio);
            jsonObject.put("fechaFinal", fecha_fin);
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


                            response = new JSONObject(response.get("data").toString());
                            System.out.println("REPUESTA DEL SERVICIO****************" + response.toString());


                            JSONArray jsonArray = response.getJSONArray("productividad");


                            if (jsonArray.length() == 0) {
                                Toast.makeText(v.getContext(), "No existen registros para la semana seleccionada", Toast.LENGTH_SHORT).show();
                                graficaSeman.clear();
                                graficaSeman.setVisibility(View.GONE);
                                titulo.setVisibility(INVISIBLE);
                                semanas.setSelection(0);
                                meses.setSelection(0);
                                anios.setSelection(0);

                                return;
                            }
                            Productividad pro;


                            System.out.println();

                            int inicial=diaInicio-1;

                            for(int i=0;i<8;i++){


                                if(productividad.size()<i){

                                    String fechaT= (inicial+i)+"/"+mesSelec+"/"+anioSele;
                                    Productividad r = new Productividad("",fechaT,"0","0","0","0");
                                    productividad.add(r);
                                }

                            }


                            for (int i = 0; i < jsonArray.length(); i++) {
                                String res = jsonArray.getString(i);

                                pro = gson.fromJson(res, Productividad.class);

                                if (pro != null) {

                                    if(pro.getUste_diagnosed_terminals()==null){
                                        pro.setUste_diagnosed_terminals("0");

                                    }

                                    if(pro.getUste_repaired_terminals()==null){
                                        pro.setUste_repaired_terminals("0");
                                    }

                                    String[]fecha2 = pro.getUste_date().split("/");
                                    int dia1= Integer.parseInt(fecha2[0]);
                                    int mes1= Integer.parseInt(fecha2[1]);
                                    int anio2= Integer.parseInt(fecha2[2]);

                                    String fechaTot= dia1+"/"+mes1+"/"+anio2;

                                   for(int j=0;i<8;i++){

                                       String[]fecha = productividad.get(j).getUste_date().split("/");
                                       int dia3= Integer.parseInt(fecha2[0]);
                                       int mes3= Integer.parseInt(fecha2[1]);
                                       int anio3= Integer.parseInt(fecha2[2]);

                                      if(dia1==dia3){
                                          System.out.println("DIA OBTENIDO"+ dia1);
                                           String[] fech= productividad.get(j).getUste_date().split("/");
                                           int difDia= dia1-diaInicio;
                                            productividad.set(difDia,pro);
                                       }

                                   }
                                }
                            }
                            System.out.println("PRODUCTIVIDAD PARA MOSTRAR**"+ productividad.toString());

                            this.pintarGrafica();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Log.d("RESPUESTA", response.toString());
                    }

                    private void pintarGrafica() {


                        if(totalProdu()==0){

                            Toast.makeText(v.getContext(), "No existen registros para la semana seleccionada", Toast.LENGTH_SHORT).show();
                            graficaSeman.clear();
                            graficaSeman.setVisibility(View.GONE);
                            titulo.setVisibility(INVISIBLE);
                            semanas.setSelection(0);
                            meses.setSelection(0);
                            anios.setSelection(0);
                            return;

                        }

                        String []fechas= new String[7];

                       int cantDias= getDiasMes(mesSelec);

                        ArrayList<BarEntry> reparadas= new ArrayList<>();
                        ArrayList<BarEntry> diagnosticadas = new ArrayList<>();

                        if(semanaSel.equals("Semana 5") && cantDias==31){


                            Productividad p= productividad.get(0);
                            diagnosticadas.add(new BarEntry(1,Integer.parseInt(p.getUste_diagnosed_terminals())));
                            reparadas.add(new BarEntry(1,Integer.parseInt(p.getUste_repaired_terminals())));
                            fechas[0]= p.getUste_date();// dia obtenido

                            String fechaT="";
                            String [] fechaObtenida= fechas[0].split("/");
                            int diaO =Integer.parseInt(fechaObtenida[0]);

                            Productividad r= productividad.get(1);
                            diagnosticadas.add(new BarEntry(2,Integer.parseInt(r.getUste_diagnosed_terminals())));
                            reparadas.add(new BarEntry(2,Integer.parseInt(r.getUste_repaired_terminals())));
                            fechas[1]= r.getUste_date();


                            Productividad pr= productividad.get(2);
                            diagnosticadas.add(new BarEntry(3,Integer.parseInt(pr.getUste_diagnosed_terminals())));
                            reparadas.add(new BarEntry(3,Integer.parseInt(pr.getUste_repaired_terminals())));
                            fechas[2]= pr.getUste_date();


                            BarDataSet datos2= new BarDataSet(reparadas, "Reparadas");
                            BarDataSet datos3= new BarDataSet(diagnosticadas, "Diagnosticadas");

                            datos2.setColor(Color.parseColor("#45A5F3"));
                            datos3.setColor(Color.parseColor("#BDBDBD"));

                            datos2.setDrawValues(true);
                            datos3.setDrawValues(true);

                            datos2.setValueFormatter(new MyValueFormatter());
                            datos3.setValueFormatter(new MyValueFormatter());

                            graficaSeman.setFitBars(true);


                            BarData datosGrafica = new BarData(datos2,datos3);
                            datosGrafica.setValueTextSize(10);
                            graficaSeman.setData(datosGrafica);

                            // para que inicie desde 0 en y

                            YAxis yAxis = graficaSeman.getAxis(YAxis.AxisDependency.LEFT);
                            yAxis.setCenterAxisLabels(true);
                            yAxis.setAxisMinimum(0);
                            yAxis.setGranularity(1);


                            XAxis x = graficaSeman.getXAxis();
                            //  x.setValueFormatter(new IndexAxisValueFormatter(meses));

                            x.setValueFormatter(new IndexAxisValueFormatter(fechas));
                            x.setPosition(XAxis.XAxisPosition.BOTH_SIDED);
                            titulo.setVisibility(VISIBLE);
                            graficaSeman.setScaleY(1);
                            graficaSeman.setScaleX(1);
                            x.setCenterAxisLabels(true);
                            graficaSeman.getAxisRight().setEnabled(false);
                            x.setLabelCount(3);
                            x.setDrawLabels(true);
                            x.setPosition(XAxis.XAxisPosition.BOTTOM);
                            x.setGranularity(1);
                            x.setGranularityEnabled(true);
                            graficaSeman.setDragEnabled(true);
                            graficaSeman.setVisibleXRangeMaximum(2);
                            float barSpace = 0.02f;
                            float groupSpace = 0.6f;
                            datosGrafica.setBarWidth(0.2f);
                            graficaSeman.getXAxis().setAxisMinimum(0);
                            graficaSeman.getXAxis().setAxisMaximum(3);
                            graficaSeman.groupBars(0, groupSpace, barSpace);
                            graficaSeman.invalidate();
                            graficaSeman.getDescription().setEnabled(false);
                            graficaSeman.getAxisRight().setGranularity(1);
                            graficaSeman.setVisibility(VISIBLE);
                            meses.setSelection(0);
                            anios.setSelection(0);
                            semanas.setSelection(0);
                            graficaSeman.invalidate();
                            titulo.setVisibility(VISIBLE);


                        }


                        if(semanaSel.equals("Semana 5") && cantDias==30){



                            Productividad p= productividad.get(0);
                           diagnosticadas.add(new BarEntry(1,Integer.parseInt(p.getUste_associated_terminals())));
                            reparadas.add(new BarEntry(1,Integer.parseInt(p.getUste_repaired_terminals())));
                            fechas[0]= p.getUste_date();


                            String [] fechaObtenida= fechas[0].split("/");
                            int diaO =Integer.parseInt(fechaObtenida[0]);
                            String fechaT="";

                            Productividad r= productividad.get(1);


                            reparadas.add(new BarEntry(2,Integer.parseInt(r.getUste_repaired_terminals())));
                            diagnosticadas.add(new BarEntry(2,Integer.parseInt(r.getUste_diagnosed_terminals())));
                            fechas[1]= r.getUste_date();


                            BarDataSet datos2= new BarDataSet(reparadas, "Reparadas");
                            BarDataSet datos3= new BarDataSet(diagnosticadas, "Diagnosticadas");

                            datos2.setColor(Color.parseColor("#45A5F3"));
                            datos3.setColor(Color.parseColor("#BDBDBD"));

                            datos2.setDrawValues(true);
                            datos3.setDrawValues(true);

                            datos2.setValueFormatter(new MyValueFormatter());
                            datos3.setValueFormatter(new MyValueFormatter());

                            graficaSeman.setFitBars(true);


                            BarData datosGrafica = new BarData(datos2,datos3);
                            datosGrafica.setValueTextSize(10);
                            graficaSeman.setData(datosGrafica);

                            // para que inicie desde 0 en y

                            YAxis yAxis = graficaSeman.getAxis(YAxis.AxisDependency.LEFT);
                            yAxis.setCenterAxisLabels(true);
                            yAxis.setAxisMinimum(0);
                            yAxis.setGranularity(1);


                            XAxis x = graficaSeman.getXAxis();
                            //  x.setValueFormatter(new IndexAxisValueFormatter(meses));

                            x.setValueFormatter(new IndexAxisValueFormatter(fechas));
                            x.setPosition(XAxis.XAxisPosition.BOTH_SIDED);
                            titulo.setVisibility(VISIBLE);
                            graficaSeman.setScaleY(1);
                            graficaSeman.setScaleX(1);
                            x.setCenterAxisLabels(true);
                            graficaSeman.getAxisRight().setEnabled(false);
                            x.setLabelCount(2);
                            x.setDrawLabels(true);
                            x.setPosition(XAxis.XAxisPosition.BOTTOM);
                            x.setGranularity(1);
                            x.setGranularityEnabled(true);
                            graficaSeman.setDragEnabled(true);
                            graficaSeman.setVisibleXRangeMaximum(2);
                            float barSpace = 0.02f;
                            float groupSpace = 0.6f;
                            datosGrafica.setBarWidth(0.2f);
                            graficaSeman.getXAxis().setAxisMinimum(0);
                            graficaSeman.getXAxis().setAxisMaximum(3);
                            graficaSeman.groupBars(0, groupSpace, barSpace);
                            graficaSeman.invalidate();
                            graficaSeman.getDescription().setEnabled(false);
                            graficaSeman.getAxisRight().setGranularity(1);
                            graficaSeman.setVisibility(VISIBLE);
                            meses.setSelection(0);
                            anios.setSelection(0);
                            semanas.setSelection(0);
                            graficaSeman.invalidate();
                            titulo.setVisibility(VISIBLE);

                        }

                       Productividad p = productividad.get(0);
                       reparadas.add(new BarEntry(1, Integer.parseInt(p.getUste_repaired_terminals())));
                       diagnosticadas.add(new BarEntry(1, Integer.parseInt(p.getUste_diagnosed_terminals())));
                        fechas[0]= p.getUste_date();

                        Productividad r= productividad.get(1);
                        reparadas.add(new BarEntry(2,Integer.parseInt(r.getUste_repaired_terminals())));
                        diagnosticadas.add(new BarEntry(2,Integer.parseInt(r.getUste_diagnosed_terminals())));
                        fechas[1]= r.getUste_date();


                        Productividad pr= productividad.get(2);
                        reparadas.add(new BarEntry(3,Integer.parseInt(pr.getUste_repaired_terminals())));
                        diagnosticadas.add(new BarEntry(3,Integer.parseInt(pr.getUste_diagnosed_terminals())));
                        fechas[2]= pr.getUste_date();

                        Productividad q= productividad.get(3);
                        reparadas.add(new BarEntry(4,Integer.parseInt(q.getUste_repaired_terminals())));
                        diagnosticadas.add(new BarEntry(4,Integer.parseInt(q.getUste_diagnosed_terminals())));
                        fechas[3]= q.getUste_date();

                        Productividad qr= productividad.get(4);
                        reparadas.add(new BarEntry(5,Integer.parseInt(qr.getUste_repaired_terminals())));
                        diagnosticadas.add(new BarEntry(5,Integer.parseInt(qr.getUste_diagnosed_terminals())));
                        fechas[4]= qr.getUste_date();

                        Productividad ss= productividad.get(5);
                        reparadas.add(new BarEntry(6,Integer.parseInt(ss.getUste_repaired_terminals())));
                        diagnosticadas.add(new BarEntry(6,Integer.parseInt(ss.getUste_diagnosed_terminals())));
                        fechas[5]= ss.getUste_date();

                        Productividad tr= productividad.get(6);
                        reparadas.add(new BarEntry(7,Integer.parseInt(tr.getUste_repaired_terminals())));
                        diagnosticadas.add(new BarEntry(7,Integer.parseInt(tr.getUste_diagnosed_terminals())));
                        fechas[6]= tr.getUste_date();


                        BarDataSet datos2= new BarDataSet(reparadas, "Reparadas");
                        BarDataSet datos3= new BarDataSet(diagnosticadas, "Diagnosticadas");

                        datos2.setColor(Color.parseColor("#45A5F3"));
                        datos3.setColor(Color.parseColor("#BDBDBD"));

                        datos2.setDrawValues(true);
                        datos3.setDrawValues(true);

                        datos2.setValueFormatter(new MyValueFormatter());
                        datos3.setValueFormatter(new MyValueFormatter());

                        graficaSeman.setFitBars(true);


                        BarData datosGrafica = new BarData(datos2,datos3);
                        datosGrafica.setValueTextSize(10);
                        graficaSeman.setData(datosGrafica);

                        // para que inicie desde 0 en y

                        YAxis yAxis = graficaSeman.getAxis(YAxis.AxisDependency.LEFT);
                        yAxis.setCenterAxisLabels(true);
                        yAxis.setAxisMinimum(0);
                        yAxis.setGranularity(1);


                        XAxis x = graficaSeman.getXAxis();
                        //  x.setValueFormatter(new IndexAxisValueFormatter(meses));

                        x.setValueFormatter(new IndexAxisValueFormatter(fechas));
                        x.setPosition(XAxis.XAxisPosition.BOTH_SIDED);
                        titulo.setVisibility(VISIBLE);
                        graficaSeman.setScaleY(1);
                        graficaSeman.setScaleX(1);
                        x.setCenterAxisLabels(true);
                        graficaSeman.getAxisRight().setEnabled(false);
                        x.setLabelCount(7);
                        x.setDrawLabels(true);
                        x.setPosition(XAxis.XAxisPosition.BOTTOM);
                        x.setGranularity(1);
                        x.setGranularityEnabled(true);
                        graficaSeman.setDragEnabled(true);
                        graficaSeman.setVisibleXRangeMaximum(2);
                        float barSpace = 0.02f;
                        float groupSpace = 0.6f;
                        datosGrafica.setBarWidth(0.2f);
                        graficaSeman.getXAxis().setAxisMinimum(0);
                        graficaSeman.getXAxis().setAxisMaximum(7);
                        graficaSeman.groupBars(0, groupSpace, barSpace);
                        graficaSeman.invalidate();
                        graficaSeman.getDescription().setEnabled(false);
                        graficaSeman.getAxisRight().setGranularity(1);
                        graficaSeman.setVisibility(VISIBLE);
                        meses.setSelection(0);
                        anios.setSelection(0);
                        semanas.setSelection(0);
                        graficaSeman.invalidate();
                        titulo.setVisibility(VISIBLE);





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



    public int getDiaInicio(String sem){

        int inicio=0;

        if (sem.equals("Semana 1")) {
            inicio=1;
        }

        if (sem.equals("Semana 2")) {
            inicio=8;
        }

        if (sem.equals("Semana 3")) {
            inicio=15;
        }


        if (sem.equals("Semana 4")) {
            inicio=22;
        }

        if (sem.equals("Semana 5")) {
            inicio=29;
        }

        return inicio;

    }

    public boolean validarFechaActual(int dia, int mes){

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Date date = new Date();
        String fechaActual = dateFormat.format(date);
        String []fechaAct= fechaActual.split("-");
        int anioAct=Integer.parseInt( fechaAct[0]);
        int mesAct=Integer.parseInt( fechaAct[1]);
        int diaAct= Integer.parseInt(fechaAct[2]);

        System.out.println("dia actual"+diaAct);
        System.out.println("dia final de la semana"+dia);

        if(mes>mesAct){return false;}

        if(mes==mesAct){
            if(dia>diaAct){

                return false;
            }
        }


        return true;


    }

    public int getDiasMes(int mesdia) {

        if (mesdia == 1) {
            return 31;
        }
        if (mesdia == 2) {
            return 28;
        }
        if (mesdia == 3) {
            return 31;
        }
        if (mesdia == 4) {
            return 30;
        }
        if (mesdia == 5) {
            return 31;
        }
        if (mesdia == 6) {
            return 30;
        }
        if (mesdia == 7) {
            return 31;
        }
        if (mesdia == 8) {
            return 31;
        }
        if (mesdia == 9) {
            return 30;
        }
        if (mesdia == 10) {
            return 31;
        }
        if (mesdia == 11) {
            return 30;
        }
        if (mesdia == 12) {
            return 31;
        }

        return 0;


    }


    public int totalProdu(){

        int i=0;

        for(Productividad p: productividad){

            i+=Integer.parseInt( p.getUste_diagnosed_terminals());
            i+=Integer.parseInt(p.getUste_repaired_terminals());

        }

        return i;

    }




}
