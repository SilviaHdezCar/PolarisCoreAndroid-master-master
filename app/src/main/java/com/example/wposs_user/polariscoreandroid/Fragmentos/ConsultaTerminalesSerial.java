package com.example.wposs_user.polariscoreandroid.Fragmentos;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

import com.example.wposs_user.polariscoreandroid.Actividades.MainActivity;


public class ConsultaTerminalesSerial extends Fragment {

    private MainActivity obj;

    private AutoCompleteTextView serial;
    private TextView lbl_msj_buscar_serial;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment_consultar_terminales_serial, null);
               // inflater.inflate(R.layout.fragment_consultar_terminales_serial, container, false);
/*



        serial = (AutoCompleteTextView) findViewById(R.id.serial);
        lbl_msj_buscar_serial = (TextView) findViewById(R.id.lbl_busq_serial);
        recyclerView=(RecyclerView) view.findViewById(R.id.recycler_view_consultaTerminales_por_serial);
        ArrayList<String> terminales = new ArrayList<>();
        for (Terminal ter : obj.getTerminales()) {
            terminales.add(ter.getSerial());
            System.out.println("Termi9nal: " + ter);
        }


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, terminales);
        serial.setAdapter(adapter);
        serial.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                InputMethodManager in = (InputMethodManager) obj.getSystemService(INPUT_METHOD_SERVICE);
                in.hideSoftInputFromWindow(view.getApplicationWindowToken(), 0);
                buscarPorSerial(serial.getText().toString());
            }
        });
        serial.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_DONE) {
                    InputMethodManager in = (InputMethodManager) obj.getSystemService(INPUT_METHOD_SERVICE);
                    in.hideSoftInputFromWindow(textView.getApplicationWindowToken(), 0);
                    buscarPorSerial(serial.getText().toString());
                    return true;
                }
                return false;
            }
        });
*/


        return view;


    }
/*
    private void buscarPorSerial(String serial ){

        Vector<Terminal> terminales= new Vector<>();
        for (Terminal ter : obj.getTerminales()) {
            if (ter.getEstado().equalsIgnoreCase("Asociada")) {
                terminales.add(ter);
            }

        }
        recyclerView.setAdapter(new AdapterTerminal(getContext(), terminales));//le pasa los datos-> lista de usuarios

        layoutManager = new LinearLayoutManager(getContext());// en forma de lista
        recyclerView.setLayoutManager(layoutManager);
    }*/
}
