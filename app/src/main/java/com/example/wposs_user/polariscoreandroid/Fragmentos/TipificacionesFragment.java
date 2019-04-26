package com.example.wposs_user.polariscoreandroid.Fragmentos;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wposs_user.polariscoreandroid.Adaptadores.AdapterTipificaciones;
import com.example.wposs_user.polariscoreandroid.Comun.Global;
import com.example.wposs_user.polariscoreandroid.R;
import com.example.wposs_user.polariscoreandroid.Tools;
import com.example.wposs_user.polariscoreandroid.java.Tabla;
import com.example.wposs_user.polariscoreandroid.java.Tipificacion;
import com.example.wposs_user.polariscoreandroid.java.Validacion;

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


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_tipificaciones, container, false);
        descripcionTipificaion = "";
        lbl_msj_tipificaciones = (TextView) v.findViewById(R.id.lbl_msj_tipificaciones);
        layout_tipificaciones = (LinearLayout) v.findViewById(R.id.layout_tipificaciones);
        autocomplete_tipificaciones = (AutoCompleteTextView) v.findViewById(R.id.autocomplete_tipificaciones);
        btn_agregarTipificaciones = (Button) v.findViewById(R.id.btn_agregarTipificaciones);
        rv = (RecyclerView) v.findViewById(R.id.recycler_view_tipificaciones);
        this.listTipificaciones = new ArrayList<Tipificacion>();

        //cada vez que inicie, el arreglo vacía el arreglo de tipificaciones que se va a enviar


        Global.TIPIFICACIONES_DIAGNOSTICO = new ArrayList<Tipificacion>();

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

    private void agregarTipificacion() {

        if (descripcionTipificaion.isEmpty()) {
            Toast.makeText(objeto, "Debe seleccionar una tipificación", Toast.LENGTH_SHORT).show();
            return;
        } else {
            Toast.makeText(objeto, descripcionTipificaion, Toast.LENGTH_SHORT).show();
            for (Tipificacion tip : Global.TIPIFICACIONES) {
                if (tip != null) {
                    if (tip.getTetv_description().equalsIgnoreCase(descripcionTipificaion)) {
                        if (!buscarArregloRV(tip, listTipificaciones)) {
                            listTipificaciones.add(tip);
                            llenarRVTipificaciones();
                            descripcionTipificaion = "";
                        }else {
                            Toast.makeText(objeto, "La tipificación ya fue agregada", Toast.LENGTH_SHORT).show();
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


    //Metodo utilizado para llenarel rect¿ycler view de tipificaciones
    public void llenarRVTipificaciones() {
        rv.setHasFixedSize(true);

        LinearLayoutManager llm = new LinearLayoutManager(Tools.getCurrentContext());
        rv.setLayoutManager(llm);

        ArrayList tipificaciones = new ArrayList<>();

        //recorro la lista obtenida y la agg a la lista

        for (Tipificacion ter : listTipificaciones) {
            if (ter != null) {
                tipificaciones.add(ter);//  butons.add(new ButtonCard(nombre, "","",icon,idVenta));
            }
        }

        final AdapterTipificaciones adapter = new AdapterTipificaciones(tipificaciones, new AdapterTipificaciones.interfaceClick() {//seria termi asoc
            @Override
            public void onClick(List<Tipificacion> terminal, int position) {


            /*serialObtenido = terminal.get(position).getTerm_serial();
            Global.modelo = terminal.get(position).getTerm_model();

            listarObservacionesTerminal(serialObtenido);*/
            }
        }, R.layout.panel_tipificaciones);

        rv.setAdapter(adapter);

    }


}
