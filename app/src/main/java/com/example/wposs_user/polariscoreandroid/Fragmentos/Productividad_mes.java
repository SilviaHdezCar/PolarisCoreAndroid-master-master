package com.example.wposs_user.polariscoreandroid.Fragmentos;

import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
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
import com.example.wposs_user.polariscoreandroid.R;
import com.example.wposs_user.polariscoreandroid.java.MyValueFormatter;
import com.example.wposs_user.polariscoreandroid.java.Productividad;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.PointsGraphSeries;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;
import static com.example.wposs_user.polariscoreandroid.Actividades.MainActivity.objeto;
import static com.github.mikephil.charting.components.XAxis.XAxisPosition.BOTTOM;
import static java.util.Collections.sort;




public class Productividad_mes extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private View v;
    private RequestQueue queue;
    private ArrayList<Productividad> productividad;
    private Spinner mes;
    private Spinner año;
    private Button produc;
    private BarChart grafica;
    private String [] anios;
    private ArrayAdapter comboAdapter;
    private int promedioReparadas;
    private int promedioDiagnosticadas;
    private TextView promreparadas;
    private TextView promDiagnosticadas;
    private LinearLayout prom;
    private LinearLayout prom2;
    private int[] productividadMesReparadas;
    private int[] productividadMesDiagnosticadas;



   @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       productividadMesDiagnosticadas= new int[5];
       productividadMesReparadas= new int[5];

      promedioReparadas = 0;
       promedioDiagnosticadas=0;
        v = inflater.inflate(R.layout.fragment_productividad_mes, container, false);
        obtenerAnios();
        promreparadas= (TextView)v.findViewById(R.id.txt_promRep);
        promDiagnosticadas=(TextView)v.findViewById(R.id.txt_promDiag);
        comboAdapter = new ArrayAdapter<String>(objeto,R.layout.spinner_sytle, anios);
        objeto.setTitulo("PRODUCTIVIDAD POR MES");
        mes = (Spinner) v.findViewById(R.id.spin_mesxmes);
        año = (Spinner) v.findViewById(R.id.spiner_añoxmes);
        año.setAdapter(comboAdapter);
         produc = (Button) v.findViewById(R.id.produc_mes);
        grafica = (BarChart) v.findViewById(R.id.grafica_mes);
        prom=(LinearLayout)v.findViewById(R.id.linea_promDia) ;
        prom2=(LinearLayout)v.findViewById(R.id.linea_promRep) ;
        queue = Volley.newRequestQueue(objeto);


        produc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                consumirServicioProductividadMes();

            }
        });


        return v;
    }


    /********************Metodo usado para mostrar la productividad en un mes y año dado**********************/

    public void consumirServicioProductividadMes() {


        grafica.clear();

        final String mesDado = mes.getSelectedItem().toString();
        String añoDado = año.getSelectedItem().toString();


        productividad = new ArrayList<>();


        if (mes.getSelectedItem() == null || año.getSelectedItem() == null) {
            Toast.makeText(v.getContext(), "Debe selecccionar el mes y año a consultar", Toast.LENGTH_SHORT).show();
            grafica.clear();
            grafica.setVisibility(INVISIBLE);
            prom.setVisibility(INVISIBLE);
            prom2.setVisibility(INVISIBLE);
            return;

        }

        if (mes.getSelectedItem().toString().equals("Selecccione") || año.getSelectedItem().equals("Seleccione")) {
            Toast.makeText(v.getContext(), "Seleccione un mes y año valido", Toast.LENGTH_SHORT).show();
            grafica.clear();
            grafica.setVisibility(INVISIBLE);
            prom.setVisibility(INVISIBLE);
            prom2.setVisibility(INVISIBLE);
            return;

        }

        final int mes_selecionado = this.getMes(mesDado);
        final int anioDado=Integer.parseInt(añoDado);



        if(!validarFechaActual(mes_selecionado,anioDado)){

            Toast.makeText(v.getContext(), "El mes y año debe ser anterior al actual", Toast.LENGTH_SHORT).show();
            grafica.clear();
            grafica.setVisibility(INVISIBLE);
            prom.setVisibility(VISIBLE);
            prom2.setVisibility(VISIBLE);
            return;

        }

        final int can_dias = this.getDiasMes(mes_selecionado);
        String fecha_inicio = mes_selecionado + "/" + 1 + "/" + añoDado;
        String fecha_fin = mes_selecionado + "/" + can_dias + "/" + añoDado;



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
                                Toast.makeText(v.getContext(), Global.mensaje, Toast.LENGTH_SHORT).show();
                                ;
                                return;
                            }


                            response = new JSONObject(response.get("data").toString());

                            JSONArray jsonArray = response.getJSONArray("productividad");


                            if (jsonArray.length() == 0) {
                                Global.mensaje = "No se encontraron registros para el mes y año seleccionado";
                                Toast.makeText(v.getContext(), Global.mensaje, Toast.LENGTH_SHORT).show();
                                grafica.setVisibility(INVISIBLE);
                                grafica.clear();
                                return;
                            }
                            Productividad pro;

                            for (int i = 0; i < jsonArray.length(); i++) {
                                String res = jsonArray.getString(i);

                                pro = gson.fromJson(res, Productividad.class);

                                if (pro != null) {
                                    productividad.add(pro);

                                }

                            }
                            obtenerProductSemana();

                            this.pintarGrafica();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Log.d("RESPUESTA", response.toString());
                    }


                    private void pintarGrafica() {

                        obtenerProductSemana();


                        if(mesDado.equalsIgnoreCase("Febrero")){

                             ArrayList<BarEntry> diagnosticadas = new ArrayList<>();
                            diagnosticadas.add(new BarEntry(1,productividadMesDiagnosticadas[0]));
                            diagnosticadas.add(new BarEntry(2,productividadMesDiagnosticadas[1]));
                            diagnosticadas.add(new BarEntry(3,productividadMesDiagnosticadas[2]));
                            diagnosticadas.add(new BarEntry(4,productividadMesDiagnosticadas[3]));

                            ArrayList<BarEntry> reparadas = new ArrayList<>();
                            reparadas.add(new BarEntry(1,productividadMesReparadas[0]));
                            reparadas.add(new BarEntry(1,productividadMesReparadas[1]));
                            reparadas.add(new BarEntry(1,productividadMesReparadas[2]));
                            reparadas.add(new BarEntry(1,productividadMesReparadas[3]));

                            BarDataSet datos = new BarDataSet(diagnosticadas, "Diagnosticadas");
                            BarDataSet valores = new BarDataSet(reparadas, "Reparadas");

                            String[] fecha = new String[] {"Semana1","Semana 2", "Semana 3", "Semana 4"};

                            datos.setColor(Color.parseColor("#9EC2C9"));
                            valores.setColor(Color.parseColor("#BDBDBD"));
                            datos.setValueFormatter(new MyValueFormatter());
                            valores.setValueFormatter(new MyValueFormatter());
                            grafica.setFitBars(true);
                            BarData datosGrafica = new BarData(datos, valores);

                            datosGrafica.setValueTextSize(10);
                            grafica.setData(datosGrafica);

                            XAxis x = grafica.getXAxis();
                            x.setValueFormatter(new IndexAxisValueFormatter(fecha));
                            x.setPosition(XAxis.XAxisPosition.BOTH_SIDED);


                            grafica.setScaleY(1);
                            grafica.setScaleX(1);
                            x.setCenterAxisLabels(true);
                            grafica.getAxisRight().setEnabled(false);
                            x.setLabelCount(5);
                            x.setDrawLabels(true);
                            x.setPosition(XAxis.XAxisPosition.BOTTOM);
                            x.setGranularity(1);
                            x.setGranularityEnabled(true);
                            grafica.setDragEnabled(true);
                            grafica.setVisibleXRangeMaximum(5);
                            float barSpace = 0.02f;
                            float groupSpace = 0.6f;
                            datosGrafica.setBarWidth(0.4f);
                            grafica.getXAxis().setAxisMinimum(0);
                            grafica.getXAxis().setAxisMaximum(5);
                            grafica.groupBars(0, groupSpace, barSpace);
                            grafica.invalidate();
                            grafica.getDescription().setEnabled(false);
                            grafica.getAxisRight().setGranularity(1);
                            calcularPromedioDiagnosticadas();
                            calcularPromedioReparadas();
                            prom.setVisibility(VISIBLE);
                            prom2.setVisibility(VISIBLE);
                            promDiagnosticadas.setText(" "+promedioDiagnosticadas);
                            promreparadas.setText(""+promedioReparadas);
                            grafica.setVisibility(VISIBLE);


                        }


                        ArrayList<BarEntry> diagnosticadas = new ArrayList<>();
                        diagnosticadas.add(new BarEntry(1,productividadMesDiagnosticadas[0]));
                        diagnosticadas.add(new BarEntry(2,productividadMesDiagnosticadas[1]));
                        diagnosticadas.add(new BarEntry(3,productividadMesDiagnosticadas[2]));
                        diagnosticadas.add(new BarEntry(4,productividadMesDiagnosticadas[3]));
                        diagnosticadas.add(new BarEntry(5,productividadMesDiagnosticadas[4]));

                        ArrayList<BarEntry> reparadas = new ArrayList<>();
                        reparadas.add(new BarEntry(1,productividadMesReparadas[0]));
                        reparadas.add(new BarEntry(2,productividadMesReparadas[1]));
                        reparadas.add(new BarEntry(3,productividadMesReparadas[2]));
                        reparadas.add(new BarEntry(4,productividadMesReparadas[3]));
                        reparadas.add(new BarEntry(5,productividadMesReparadas[4]));

                        BarDataSet datos = new BarDataSet(diagnosticadas, "Diagnosticadas");
                        BarDataSet valores = new BarDataSet(reparadas, "Reparadas");

                        String[] fecha = new String[] {"Semana1","Semana 2", "Semana 3", "Semana 4", "Semana 5"};

                        datos.setColor(Color.parseColor("#9EC2C9"));
                        valores.setColor(Color.parseColor("#BDBDBD"));
                        datos.setValueFormatter(new MyValueFormatter());
                        valores.setValueFormatter(new MyValueFormatter());
                        grafica.setFitBars(true);
                        BarData datosGrafica = new BarData(datos, valores);

                        datosGrafica.setValueTextSize(10);
                        grafica.setData(datosGrafica);

                        XAxis x = grafica.getXAxis();
                        x.setValueFormatter(new IndexAxisValueFormatter(fecha));
                        x.setPosition(XAxis.XAxisPosition.BOTH_SIDED);


                        grafica.setScaleY(1);
                        grafica.setScaleX(1);
                        x.setCenterAxisLabels(true);
                        grafica.getAxisRight().setEnabled(false);
                        x.setLabelCount(6);
                        x.setDrawLabels(true);
                        x.setPosition(XAxis.XAxisPosition.BOTTOM);
                        x.setGranularity(1);
                        x.setGranularityEnabled(true);
                        grafica.setDragEnabled(true);
                        grafica.setVisibleXRangeMaximum(6);
                        float barSpace = 0.02f;
                        float groupSpace = 0.6f;
                        datosGrafica.setBarWidth(0.2f);
                        grafica.getXAxis().setAxisMinimum(0);
                        grafica.getXAxis().setAxisMaximum(6);
                        grafica.groupBars(0, groupSpace, barSpace);
                        grafica.invalidate();
                        grafica.getDescription().setEnabled(false);
                        grafica.getAxisRight().setGranularity(1);
                        calcularPromedioDiagnosticadas();
                        calcularPromedioReparadas();
                        prom.setVisibility(VISIBLE);
                        prom2.setVisibility(VISIBLE);
                        promDiagnosticadas.setText(" "+promedioDiagnosticadas);
                        promreparadas.setText(""+promedioReparadas);
                        grafica.setVisibility(VISIBLE);





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

    /*****************metodo para convertir el mes a int*****************/

    public int getMes(String meses) {

        if (meses.equals("Enero")) {
            return 1;
        }
        if (meses.equals("Febrero")) {
            return 2;
        }
        if (meses.equals("Marzo")) {
            return 3;
        }
        if (meses.equals("Abril")){
            return 4;}
        if (meses.equals("Mayo")) {
            return 5;
        }
        if (meses.equals("Junio")) {
            return 6;
        }
        if (meses.equals("Julio")) {
            return 7;
        }
        if (meses.equals("Agosto")) {
            return 8;
        }
        if (meses.equals("Septiembre")) {
            return 9;
        }
        if (meses.equals("Octubre")) {
            return 10;
        }
        if (meses.equals("Noviembre")) {
            return 11;
        }
        if (meses.equals("Diciembre")) {
            return 12;
        }

        return 0;

    }


    /****************metodo para obtener el numero de dias en cada mes*********************/

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


    public void  obtenerAnios() {

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Date date = new Date();
        String fecha = dateFormat.format(date);

        String[] fechas = fecha.split("-");

        int anioActual = Integer.parseInt(fechas[0]);

        anios = new String[21];
        anios[0]= "Seleccione";


        for (int i = 1; i < anios.length; i++) {

            anios[i] = String.valueOf(anioActual);
            anioActual--;

        }

    }

    public boolean validarFechaActual(int mes, int anio){

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Date date = new Date();
        String fechaActual = dateFormat.format(date);
        String []fechaAct= fechaActual.split("-");
        int anioA=Integer.parseInt(fechaAct[0]);
        int mesA=Integer.parseInt(fechaAct[1]);

        if(anio>anioA){

            return false;
        }

        if(anio<anioA){

            return true;
        }

        if(mesA>mes){
            return true;

        }

        return mesA >= mes;


    }

    public void obtenerProductSemana() {


        for (int i = 1; i < 8; i++) {


            for (Productividad p : productividad) {

                String[] fecha = p.getUste_date().split("/");
                int dia = Integer.parseInt(fecha[0]);

                if (i == dia) {
                    if (p.getUste_associated_terminals().equalsIgnoreCase("NaN")){
                        p.setUste_associated_terminals("0");

                    }
                    if (p.getUste_completed_terminals().equalsIgnoreCase("NaN")){
                        p.setUste_completed_terminals("0");

                    }

                    productividadMesDiagnosticadas[0] = productividadMesDiagnosticadas[0] + Integer.parseInt(p.getUste_associated_terminals());
                    productividadMesReparadas[0]= productividadMesReparadas[0]+Integer.parseInt(p.getUste_completed_terminals());
                }

            }

        }

        for (int i = 8; i < 15; i++) {


            for (Productividad p : productividad) {

                String[] fecha = p.getUste_date().split("/");
                int dia = Integer.parseInt(fecha[0]);

                if (i == dia) {
                    if (p.getUste_associated_terminals().equalsIgnoreCase("NaN")){
                        p.setUste_associated_terminals("0");

                    }
                    if (p.getUste_completed_terminals().equalsIgnoreCase("NaN")){
                        p.setUste_completed_terminals("0");

                    }

                    productividadMesDiagnosticadas[1] = productividadMesDiagnosticadas[1] + Integer.parseInt(p.getUste_associated_terminals());
                    productividadMesReparadas[1]= productividadMesReparadas[1]+Integer.parseInt(p.getUste_completed_terminals());

                }

            }

        }

        for (int i = 15; i < 22; i++) {


            for (Productividad p : productividad) {

                String[] fecha = p.getUste_date().split("/");
                int dia = Integer.parseInt(fecha[0]);

                if (i == dia) {
                    if (p.getUste_associated_terminals().equalsIgnoreCase("NaN")){
                        p.setUste_associated_terminals("0");

                    }
                    if (p.getUste_completed_terminals().equalsIgnoreCase("NaN")){
                        p.setUste_completed_terminals("0");

                    }

                    productividadMesDiagnosticadas[2] = productividadMesDiagnosticadas[2] + Integer.parseInt(p.getUste_associated_terminals());
                    productividadMesReparadas[2]= productividadMesReparadas[2]+Integer.parseInt(p.getUste_completed_terminals());
                }

            }

        }



        for (int i = 22; i < 29; i++) {


            for (Productividad p : productividad) {

                String[] fecha = p.getUste_date().split("/");
                int dia = Integer.parseInt(fecha[0]);

                if (i == dia) {
                    if (p.getUste_associated_terminals().equalsIgnoreCase("NaN")){
                        p.setUste_associated_terminals("0");

                    }
                    if (p.getUste_completed_terminals().equalsIgnoreCase("NaN")){
                        p.setUste_completed_terminals("0");

                    }

                    productividadMesDiagnosticadas[3] = productividadMesDiagnosticadas[3] + Integer.parseInt(p.getUste_associated_terminals());
                    productividadMesReparadas[3]= productividadMesReparadas[3]+Integer.parseInt(p.getUste_completed_terminals());
                }

            }

        }

        for (int i = 29; i < 32; i++) {


            for (Productividad p : productividad) {

                String[] fecha = p.getUste_date().split("/");
                int dia = Integer.parseInt(fecha[0]);

                if (i == dia) {
                    if (p.getUste_associated_terminals().equalsIgnoreCase("NaN")){
                        p.setUste_associated_terminals("0");

                    }
                    if (p.getUste_completed_terminals().equalsIgnoreCase("NaN")){
                        p.setUste_completed_terminals("0");

                    }

                    productividadMesDiagnosticadas[4] = productividadMesDiagnosticadas[4] + Integer.parseInt(p.getUste_associated_terminals());
                    productividadMesReparadas[4]= productividadMesReparadas[4]+Integer.parseInt(p.getUste_completed_terminals());
                }

            }

        }

    }


    public void calcularPromedioDiagnosticadas(){

        int sumaDiag=0;
        int meses= getMes(mes.getSelectedItem().toString());
        int cantDias=getDiasMes(meses);

        for(int i=0;i<productividadMesDiagnosticadas.length;i++){
            sumaDiag+=productividadMesDiagnosticadas[i];

        }


        if(mes.getSelectedItem().toString().equalsIgnoreCase("Febrero")){

            promedioDiagnosticadas=sumaDiag/28;
        }

        promedioDiagnosticadas=sumaDiag/cantDias;

    }

    public void calcularPromedioReparadas(){

        int sumaDiag=0;
        int meses= getMes(mes.getSelectedItem().toString());
        int cantDias=getDiasMes(meses);

        for(int i=0;i<productividadMesReparadas.length;i++){
            sumaDiag+=productividadMesReparadas[i];

        }


        if(mes.getSelectedItem().toString().equalsIgnoreCase("Febrero")){

            promedioReparadas=sumaDiag/28;
        }

        promedioReparadas=sumaDiag/cantDias;





    }




}
