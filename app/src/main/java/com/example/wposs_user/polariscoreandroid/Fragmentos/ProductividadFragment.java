package com.example.wposs_user.polariscoreandroid.Fragmentos;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.wposs_user.polariscoreandroid.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;


public class ProductividadFragment extends Fragment {

    private BarChart grafica;
    private View v;
    private Object MyValueFormatter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v= inflater.inflate(R.layout.fragment_productividad, container, false);
        grafica=(BarChart)v.findViewById(R.id.grafica_productividad);

       //Creamos los valores de entrada

        List<BarEntry> entradas= new ArrayList<>();
        entradas.add(new BarEntry(1,4));
        entradas.add(new BarEntry(2,6));
        entradas.add(new BarEntry(3,8));
        entradas.add(new BarEntry(4,7));
        entradas.add(new BarEntry(5,5));
        entradas.add(new BarEntry(6,3));
        entradas.add(new BarEntry(7,5));



        //Enviamos los datos para crear la grafica

        BarDataSet datos = new BarDataSet(entradas,"");

      


       datos.setValueFormatter( new MyValueFormatter());



        Description des = grafica.getDescription();
        des.setEnabled(false);

         BarData data= new BarData(datos);

        //Colocamos color a cada Barra

        datos.setColors(ColorTemplate.COLORFUL_COLORS);

        //Separacion entre barras
        data.setBarWidth(0.9f);


        grafica.setData(data);


        //pone las barras centradas
        grafica.setFitBars(true);

        grafica.invalidate();//hacer refresh






        return v;
    }
}
