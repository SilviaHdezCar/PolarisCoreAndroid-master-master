package com.example.wposs_user.polariscoreandroid.Fragmentos;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.wposs_user.polariscoreandroid.R;

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
        if(s.getSelectedItem().toString().equals("Dia")){
            objeto.getSupportFragmentManager().beginTransaction().replace(R.id.contenedor_main, new Productividad_dia()).addToBackStack(null).commit();
        }

        if(s.getSelectedItem().toString().equals("Mes")){
            objeto.getSupportFragmentManager().beginTransaction().replace(R.id.contenedor_main, new Productividad_mes()).addToBackStack(null).commit();
        }

        if(s.getSelectedItem().toString().equals("Año")){
            objeto.getSupportFragmentManager().beginTransaction().replace(R.id.contenedor_main, new Productividad_anio()).addToBackStack(null).commit();
        }



    }




}
