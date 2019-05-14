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
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wposs_user.polariscoreandroid.Comun.Global;
import com.example.wposs_user.polariscoreandroid.Dialogs.DialogOpcionesConsulta;
import com.example.wposs_user.polariscoreandroid.R;
import com.example.wposs_user.polariscoreandroid.Comun.Tools;

import java.util.Date;
import java.util.Locale;

import static com.example.wposs_user.polariscoreandroid.Actividades.MainActivity.objeto;


public class ConsultaTerminalesFechas extends Fragment {

    private EditText f_inicio;
    private EditText f_fin;
    private TextView text_estado_ter;
    private Button btn_fech_consulta_serial;
    private ImageView buscar_terminales_fecha;
    private LinearLayout layout_estado_terminal;
    View view;

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
        view = inflater.inflate(R.layout.fragment_consulta_terminales_fechas, container, false);
        objeto.setTitle("      BÚSQUEDA POR FECHAS");

        buscar_terminales_fecha = (ImageView) view.findViewById(R.id.btn_buscar_terminalesPorFechas);

        //carga los txt de las fechas al hacer la consulta establecida por fechas

        f_inicio = (EditText) view.findViewById(R.id.txt_fecha_inicio);
        f_fin = (EditText) view.findViewById(R.id.txt_fecha_fin);
        text_estado_ter = (TextView) view.findViewById(R.id.text_estado_ter);
        btn_fech_consulta_serial = (Button) view.findViewById(R.id.btn_fech_consulta_serial);
        layout_estado_terminal = (LinearLayout) view.findViewById(R.id.layout_estado_terminal);

        btn_fech_consulta_serial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                objeto.getSupportFragmentManager().beginTransaction().replace(R.id.contenedor_main, new ConsultaTerminalesSerial()).addToBackStack(null).commit();
            }
        });

        layout_estado_terminal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                opcionesBusqueda();
                text_estado_ter.setText(Global.opcion_consulta);
            }
        });

        f_inicio.setInputType(InputType.TYPE_NULL);
        f_fin.setInputType(InputType.TYPE_NULL);
        f_inicio.setInputType(InputType.TYPE_NULL);


//Mostrar calendario y seleccionar fecha para mostrar en el TExtView
        java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        Date date = new Date();
        String fecha = dateFormat.format(date);
        fecha = fecha.replace("-", "/");
        f_inicio.setText(fecha);
        Fecha1 = f_inicio.getText().toString();
        f_inicio.setText(Tools.dateDDMMYYYYStr2(f_inicio.getText().toString()));


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
                        f_inicio.setText(Tools.dateDDMMYYYYStr2(f_inicio.getText().toString()));
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
                        f_fin.setText(Tools.dateDDMMYYYYStr2(f_fin.getText().toString()));
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


        buscar_terminales_fecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listarTerminalesfecha();

            }
        });
//Fin mostrar calendario


        return view;

    }

    /**
     * BUSQUEDA DE TERMINALES
     **/

    public void opcionesBusqueda() {
        DialogOpcionesConsulta dialog = new DialogOpcionesConsulta();
        dialog.show(objeto.getSupportFragmentManager(), "Dialog");
    }

    private void showDatePickerDialog(final EditText etFecha) {
        DatePickerDialog recogerFecha = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                //Esta variable lo que realiza es aumentar en uno el mes ya que comienza desde 0 = enero
                final int mesActual = month + 1;
                //Formateo el día obtenido: antepone el 0 si son menores de 10
                String diaFormateado = (dayOfMonth < 10) ? CERO + String.valueOf(dayOfMonth) : String.valueOf(dayOfMonth);
                //Formateo el mes obtenido: antepone el 0 si son menores de 10
                String mesFormateado = (mesActual < 10) ? CERO + String.valueOf(mesActual) : String.valueOf(mesActual);
                //Muestro la fecha con el formato deseado
                etFecha.setText(diaFormateado + BARRA + mesFormateado + BARRA + year);


            }
            //Estos valores deben ir en ese orden, de lo contrario no mostrara la fecha actual
            /**
             *También puede cargar los valores que usted desee
             */
        }, anio, mes, dia);
        //Muestro el widget
        recogerFecha.show();
    }

    public boolean validarFecha() {

        String fecha_inicial = f_inicio.getText().toString();
        String[] fecha = fecha_inicial.split("/");
        int dia_inicio = Integer.parseInt(fecha[0]);
        int mes_inicio = Integer.parseInt(fecha[1]);
        int año_inicio = Integer.parseInt(fecha[2]);

        System.out.println("Fecha inicial:  " + "dia:" + dia_inicio + "mes:" + mes_inicio + "año:" + año_inicio);

        String fecha_final = f_fin.getText().toString();
        String[] fechaFin = fecha_final.split("/");
        int dia_fin = Integer.parseInt(fechaFin[0]);
        int mes_fin = Integer.parseInt(fechaFin[1]);
        int año_fin = Integer.parseInt(fechaFin[2]);

        System.out.println("Fecha final:  " + "dia:" + dia_fin + "mes:" + mes_fin + "año:" + año_fin);


        if (año_fin < año_inicio) {
            return false;
        }
        if (mes_fin < mes_inicio) {
            return false;
        }
        if (dia_fin < dia_inicio) {
            return false;
        }

        return true;

    }


    public void listarTerminalesfecha() {


        boolean x = this.validarFecha();

        if (!x) {

            Toast.makeText(view.getContext(), "La fecha final no puede ser menor a la inicial", Toast.LENGTH_SHORT).show();
        }
    }


}
