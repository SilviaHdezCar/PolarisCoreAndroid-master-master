package com.example.wposs_user.polariscoreandroid.Fragmentos;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;

import com.example.wposs_user.polariscoreandroid.Actividades.MainActivity;
import com.example.wposs_user.polariscoreandroid.Tools;

import java.util.Date;
import java.util.Locale;


public class ConsultaTerminalesReparadasFragm extends Fragment {

    public static MainActivity objeto;
    private EditText f_inicio;
    private EditText f_fin;

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
        View view=inflater.inflate(R.layout.fragment_terminales_reparada, container, false);

        //carga los txt de las fechas al hacer la consulta establecida por fechas

        f_inicio = (EditText) view.findViewById(R.id.txt_fecha_inicio);
        f_fin = (EditText) view.findViewById(R.id.txt_fecha_fin);

        f_inicio.setInputType(InputType.TYPE_NULL);
        f_fin.setInputType(InputType.TYPE_NULL);


        java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        Date date = new Date();
        String fecha = dateFormat.format(date);
        fecha = fecha.replace("-", "/");
        f_inicio.setText(fecha);
        Fecha1 = f_inicio.getText().toString();
        f_inicio.setText(Tools.dateYYYYMMDDStr2(f_inicio.getText().toString()));


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
                        f_inicio.setText(Tools.dateYYYYMMDDStr2(f_inicio.getText().toString()));
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
                        f_fin.setText(Tools.dateYYYYMMDDStr2(f_fin.getText().toString()));
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





        return view;

    }



    private void showDatePickerDialog(final EditText etFecha) {
        DatePickerDialog recogerFecha = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                //Esta variable lo que realiza es aumentar en uno el mes ya que comienza desde 0 = enero
                final int mesActual = month + 1;
                //Formateo el día obtenido: antepone el 0 si son menores de 10
                String diaFormateado = (dayOfMonth < 10)? CERO + String.valueOf(dayOfMonth):String.valueOf(dayOfMonth);
                //Formateo el mes obtenido: antepone el 0 si son menores de 10
                String mesFormateado = (mesActual < 10)? CERO + String.valueOf(mesActual):String.valueOf(mesActual);
                //Muestro la fecha con el formato deseado
                etFecha.setText(diaFormateado + BARRA + mesFormateado + BARRA + year);


            }
            //Estos valores deben ir en ese orden, de lo contrario no mostrara la fecha actual
            /**
             *También puede cargar los valores que usted desee
             */
        },anio, mes, dia);
        //Muestro el widget
        recogerFecha.show();
    }


}
