package com.example.wposs_user.polariscoreandroid.Fragmentos;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wposs_user.polariscoreandroid.Adaptadores.AdapterTipificaciones;
import com.example.wposs_user.polariscoreandroid.Comun.Global;
import com.example.wposs_user.polariscoreandroid.Dialogs.DialogEsRepable;
import com.example.wposs_user.polariscoreandroid.R;
import com.example.wposs_user.polariscoreandroid.Tools;
import com.example.wposs_user.polariscoreandroid.java.Tipificacion;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.INPUT_METHOD_SERVICE;
import static com.example.wposs_user.polariscoreandroid.Actividades.MainActivity.objeto;


public class TipificacionesFragment extends Fragment {

    private TextView lbl_msj_tipificaciones;
    private LinearLayout layout_tipificaciones;
    private AutoCompleteTextView autocomplete_tipificaciones;
    private Button btn_agregarTipificaciones;
    private RecyclerView rv;
    private ArrayList<Tipificacion> listTipificaciones;
    public String descripcionTipificaion;
    private static ArrayList tipificaciones;
    private Button btn_volver_Tipificaciones;
    private Button btn_siguiente_Tipificaciones;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_tipificaciones, container, false);
        descripcionTipificaion = "";
        lbl_msj_tipificaciones = (TextView) v.findViewById(R.id.lbl_msj_tipificaciones);
        layout_tipificaciones = (LinearLayout) v.findViewById(R.id.layout_tipificaciones);
        autocomplete_tipificaciones = (AutoCompleteTextView) v.findViewById(R.id.autocomplete_tipificaciones);
        btn_agregarTipificaciones = (Button) v.findViewById(R.id.btn_agregarTipificaciones);
        btn_volver_Tipificaciones = (Button) v.findViewById(R.id.btn_volver_Tipificaciones);
        btn_siguiente_Tipificaciones = (Button) v.findViewById(R.id.btn_siguiente_Tipificaciones);

        rv = (RecyclerView) v.findViewById(R.id.recycler_view_tipificaciones);

        this.listTipificaciones = new ArrayList<Tipificacion>();

        Global.TIPIFICACIONES_DIAGNOSTICO = new ArrayList<String>();

        //validar si el arreglo de tipificaiones está vacio
        if (Global.TIPIFICACIONES.size() == 0) {
            lbl_msj_tipificaciones.setText("No hay tipificaciones registradas");
            layout_tipificaciones.setVisibility(View.INVISIBLE);
        } else { //si no está vacio, llena el autocomplte
            layout_tipificaciones.setVisibility(View.VISIBLE);
            lbl_msj_tipificaciones.setVisibility(View.INVISIBLE);

            llenarAutocomplete();
            //agregar al RV la tipificación seleccionada
            btn_agregarTipificaciones.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    agregarTipificacion();
                }
            });
        }

        btn_siguiente_Tipificaciones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                siguienteTipificaciones();
            }
        });


        return v;

    }

    //Concierte el arreglode tipificaciones a un arreglo de String
    public String[] getTipificaciones() {

        String[] rep = new String[Global.TIPIFICACIONES.size()];


        for (int i = 0; i < Global.TIPIFICACIONES.size(); i++) {

            rep[i] = Global.TIPIFICACIONES.get(i).getTetv_description();

        }
        return rep;

    }


    public void llenarAutocomplete() {

        final String[] getTipificaciones = this.getTipificaciones();

        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(objeto, android.R.layout.simple_list_item_1, getTipificaciones);

        autocomplete_tipificaciones.setAdapter(adapter);
        autocomplete_tipificaciones.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                descripcionTipificaion = adapter.getItem(i);

                InputMethodManager in = (InputMethodManager) objeto.getSystemService(INPUT_METHOD_SERVICE);
                in.hideSoftInputFromWindow(view.getApplicationWindowToken(), 0);
            }
        });
        autocomplete_tipificaciones.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {

                if (i == EditorInfo.IME_ACTION_DONE) {

                    descripcionTipificaion = adapter.getItem(i);
                    InputMethodManager in = (InputMethodManager) objeto.getSystemService(INPUT_METHOD_SERVICE);
                    in.hideSoftInputFromWindow(textView.getApplicationWindowToken(), 0);
                    return true;
                }
                return false;
            }
        });


    }

    /**
     * Al prsionar agregar tipificacion
     * **/
    private void agregarTipificacion() {

        if (descripcionTipificaion.isEmpty()) {
            Toast.makeText(objeto, "Debe seleccionar una tipificación", Toast.LENGTH_SHORT).show();
            return;
        } else {
            for (Tipificacion tip : Global.TIPIFICACIONES) {
                if (tip != null) {
                    if (tip.getTetv_description().equalsIgnoreCase(descripcionTipificaion)) {
                        if (!buscarArregloRV(tip, listTipificaciones)) {
                            listTipificaciones.add(tip);
                            llenarRVTipificaciones();
                            descripcionTipificaion = "";
                            autocomplete_tipificaciones.setText("");
                        } else {
                            Toast.makeText(objeto, "La tipificación ya fue agregada", Toast.LENGTH_SHORT).show();
                            descripcionTipificaion = "";
                            autocomplete_tipificaciones.setText("");
                            return;
                        }
                    }
                }
            }

        }
    }

    public boolean buscarArregloRV(Tipificacion tip, ArrayList<Tipificacion> list) {
        boolean retorno = false;
        for (Tipificacion t : list) {
            if (tip == t) {
                retorno = true;
            }
        }

        return retorno;
    }


    //Metodo utilizado para llenarel recycler view de tipificaciones
    public void llenarRVTipificaciones() {

        tipificaciones = new ArrayList<>();

        for (Tipificacion ter : listTipificaciones) {
            if (ter != null) {
                tipificaciones.add(ter);
            }
        }
        llenarRv();

    }


    private void llenarRv() {
        rv.setHasFixedSize(true);

        LinearLayoutManager llm = new LinearLayoutManager(Tools.getCurrentContext());
        rv.setLayoutManager(llm);

        final AdapterTipificaciones adapter = new AdapterTipificaciones(tipificaciones, new AdapterTipificaciones.interfaceClick() {//seria termi asoc
            @Override
            public void onClick(List<Tipificacion> terminal, int position) {

                listTipificaciones.remove(position);
                tipificaciones.remove(position);
                llenarRv();
            }
        }, R.layout.panel_tipificaciones);

        rv.setAdapter(adapter);

    }


    /**
     * Muestra el cuadro de dialogo para seleccionar si es reparable
     *           NO-->Llenar el panel de observaciones
     *           SI-->  Mostrar cuadro de dialogo que pregunta si es por USO o FABRICA-->Pasar a la selección de repuestos
     * **/
    public void siguienteTipificaciones() {

        if(llenarTipificacionesDiagnostico()){
            esReparable();
        }

    }

    //Armo el arraylist     que voy a enviar al consumir el servicio de registrar diagnostico
    public boolean llenarTipificacionesDiagnostico() {
        boolean retorno=false;
        String cadena = "";

        if(listTipificaciones.size()==0){
            AlertDialog alertDialog = new AlertDialog.Builder(objeto).create();
            alertDialog.setTitle("¡ATENCIÓN!");
            alertDialog.setMessage("Debe seleccionar al menos una tipificacion");
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "ACEPTAR",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();
            return false;
        }else {
           int  cont=0;
            for (Tipificacion tipi:listTipificaciones){
                if(tipi!=null){
    //        "Tipificaciones":[{"tets_terminal_serial":"212","tets_terminal_type_validation":"sadasdasd","tets_status":"ok"}]
                    cadena = "{\"tets_terminal_serial\": \"<SERIAL>\",\"tets_terminal_type_validation\": \"<TIPO>\",\"tets_status\": \"ok\"}";
                    cadena = cadena.replace("<SERIAL>", tipi.getTetv_id());
                    cadena = cadena.replace("<TIPO>", tipi.getTetv_description());
                    Global.TIPIFICACIONES_DIAGNOSTICO.add(cadena);
                    System.out.println("Pos: "+cont+"  Cadena: "+cadena);
                }
                cont++;
            }
            retorno=true;
        }


        return retorno;
    }

    public void esReparable() {
        DialogEsRepable dialog = new DialogEsRepable();
        dialog.show(objeto.getSupportFragmentManager(), "");
    }



}
