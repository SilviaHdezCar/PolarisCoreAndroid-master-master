package com.example.wposs_user.polariscoreandroid.Fragmentos;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wposs_user.polariscoreandroid.Comun.Global;
import com.example.wposs_user.polariscoreandroid.R;
import com.example.wposs_user.polariscoreandroid.java.Tabla;
import com.example.wposs_user.polariscoreandroid.java.Tipificacion;

import java.util.ArrayList;

import static android.content.Context.INPUT_METHOD_SERVICE;
import static com.example.wposs_user.polariscoreandroid.Actividades.MainActivity.objeto;


public class TipificacionesFragment extends Fragment {

    private TextView lbl_msj_tipificaciones;
    private LinearLayout layout_tipificaciones;
    private AutoCompleteTextView autocomplete_tipificaciones;

    Tabla tabla;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_tipificaciones, container, false);

        lbl_msj_tipificaciones=(TextView) v.findViewById(R.id.lbl_msj_tipificaciones);
        layout_tipificaciones=(LinearLayout)v.findViewById(R.id.layout_tipificaciones);
        autocomplete_tipificaciones=(AutoCompleteTextView)v.findViewById(R.id.autocomplete_tipificaciones);
        tabla = new Tabla(objeto, (TableLayout)v.findViewById(R.id.tablaTipificaciones));
        tabla.agregarCabecera(R.array.tabla_tipificaciones);
        llenarFilasTabla();

        //cada vez que inicie, el arreglo vacía el arreglo de tipificaciones que se va a enviar

        Global.TIPIFICACIONES_DIAGNOSTICO=new ArrayList<Tipificacion>();

//validar si el arreglo de tipificaiones está vacio
        if(Global.TIPIFICACIONES.size()==0){
            lbl_msj_tipificaciones.setText("No hay tipificaciones registradas");
            layout_tipificaciones.setVisibility(View.INVISIBLE);
        }else{ //si no está vacio, llena el autocomplte
            lbl_msj_tipificaciones.setVisibility(View.INVISIBLE);
            llenarAutocomplete();

        }

        return v;

    }

//Concierte el arreglode tipificaciones a un arreglo de String
    public String[] getTipificaciones(){

        String[] rep  = new String[Global.TIPIFICACIONES.size()];


        for(int i =0;i<Global.TIPIFICACIONES.size();i++){

            rep[i]= Global.TIPIFICACIONES.get(i).getTetv_description();

        }
        return rep;

    }


    public void llenarAutocomplete(){


        final String [] getTipificaciones = this.getTipificaciones();

        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(objeto, android.R.layout.simple_list_item_1, getTipificaciones);

        autocomplete_tipificaciones.setAdapter(adapter);
        autocomplete_tipificaciones.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Global.descripcionTipificaion=adapter.getItem(i);

                System.out.println(" Global.descripcionTipificaion=getTipificaciones[i].toString();"+Global.descripcionTipificaion);
                InputMethodManager in = (InputMethodManager) objeto.getSystemService(INPUT_METHOD_SERVICE);
                in.hideSoftInputFromWindow(view.getApplicationWindowToken(), 0);
            }
        });
        autocomplete_tipificaciones.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {

                if(i == EditorInfo.IME_ACTION_DONE){

                    Global.descripcionTipificaion=adapter.getItem(i);

                    System.out.println(" Global.descripcionTipificaion=getTipificaciones[i].toString();"+Global.descripcionTipificaion);
                    InputMethodManager in = (InputMethodManager) objeto.getSystemService(INPUT_METHOD_SERVICE);
                    in.hideSoftInputFromWindow(textView.getApplicationWindowToken(), 0);
                    return true;
                }
                return false;
            }
        });


    }


    //en este metodo se agrega la tipificacion al recicler View de tipificaciones
    public void agregarTipificacionTabla(View v) {

        String tipificacion=autocomplete_tipificaciones.getText().toString();
        if(tipificacion.isEmpty()){
            Toast.makeText(objeto, "Por favor seleccione el tipo de tipificación", Toast.LENGTH_SHORT).show();
            return;
        }


        for (Tipificacion tip:Global.TIPIFICACIONES){
            System.out.println("tip.getTetv_description() "+tip.getTetv_description());
            System.out.println("tipificacion "+tipificacion);
            if(tip.getTetv_description().equalsIgnoreCase(tipificacion)){
                Global.TIPIFICACIONES_DIAGNOSTICO.add(tip);

            }else{
                Toast.makeText(objeto, "Tipificación no encontrada", Toast.LENGTH_SHORT).show();

            }
        }
    }

    public void llenarFilasTabla(){


        for(int i = 0; i < 15; i++)
        {
            ArrayList<String> elementos = new ArrayList<String>();
            elementos.add(Integer.toString(i));
            elementos.add("Casilla [" + i + ", 0]");
            elementos.add("Casilla [" + i + ", 1]");
            elementos.add("Casilla [" + i + ", 2]");
            elementos.add("Casilla [" + i + ", 3]");
            tabla.agregarFilaTabla(elementos);
        }
    }
}
