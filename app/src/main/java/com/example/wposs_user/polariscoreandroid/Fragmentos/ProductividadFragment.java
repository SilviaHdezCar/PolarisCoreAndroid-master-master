package com.example.wposs_user.polariscoreandroid.Fragmentos;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
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
import com.example.wposs_user.polariscoreandroid.Comun.Tools;
import com.example.wposs_user.polariscoreandroid.Dialogs.DialogOpcionesConsulta;
import com.example.wposs_user.polariscoreandroid.Productividad_dia;
import com.example.wposs_user.polariscoreandroid.R;
import com.example.wposs_user.polariscoreandroid.java.MyValueFormatter;
import com.example.wposs_user.polariscoreandroid.java.Productividad;
import com.example.wposs_user.polariscoreandroid.java.Repuesto;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static com.example.wposs_user.polariscoreandroid.Actividades.MainActivity.objeto;


public class ProductividadFragment extends Fragment {
View v;
Spinner s;
Button buscar;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        v= inflater.inflate(R.layout.fragment_productividad, container, false);
        s= (Spinner)v.findViewById(R.id.tipo_consulta);
        buscar= (Button)v.findViewById(R.id.btn_busqueda);
        buscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buscar_productividad();
            }
        });




        return v;
    }



    public void buscar_productividad() {

        if(s.getSelectedItem().toString().equals("Seleccione")){

            Toast.makeText(v.getContext(),"Debe seleccionar un criterio válido",Toast.LENGTH_SHORT).show();
            return;
        }
        if(s.getSelectedItem().toString().equals("dia")){
            objeto.getSupportFragmentManager().beginTransaction().replace(R.id.contenedor_main, new Productividad_dia()).addToBackStack(null).commit();


        }



    }




}
