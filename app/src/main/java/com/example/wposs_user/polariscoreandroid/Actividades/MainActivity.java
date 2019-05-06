package com.example.wposs_user.polariscoreandroid.Actividades;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wposs_user.polariscoreandroid.Adaptadores.AdapterTerminal;
import com.example.wposs_user.polariscoreandroid.Adaptadores.AdapterTerminal_asociada;
import com.example.wposs_user.polariscoreandroid.Comun.Global;
import com.example.wposs_user.polariscoreandroid.Comun.Messages;
import com.example.wposs_user.polariscoreandroid.Comun.Utils;
import com.example.wposs_user.polariscoreandroid.Dialogs.DialogOpcionesConsulta;
import com.example.wposs_user.polariscoreandroid.Fragmentos.ActualizarClave_perfil;
import com.example.wposs_user.polariscoreandroid.Fragmentos.ConsultaTerminalesSerial;
import com.example.wposs_user.polariscoreandroid.Fragmentos.EtapasTerminal;
import com.example.wposs_user.polariscoreandroid.Fragmentos.InicialFragment;
import com.example.wposs_user.polariscoreandroid.Fragmentos.ObservacionesFragment;
import com.example.wposs_user.polariscoreandroid.Fragmentos.PerfilFragment;
import com.example.wposs_user.polariscoreandroid.Fragmentos.ProductividadFragment;
import com.example.wposs_user.polariscoreandroid.Fragmentos.Registro_diagnostico;
import com.example.wposs_user.polariscoreandroid.Fragmentos.StockFragment;
import com.example.wposs_user.polariscoreandroid.Fragmentos.TipificacionesFragment;
import com.example.wposs_user.polariscoreandroid.R;
import com.example.wposs_user.polariscoreandroid.TCP.TCP;
import com.example.wposs_user.polariscoreandroid.Comun.Tools;
import com.example.wposs_user.polariscoreandroid.Fragmentos.ValidacionesTerminalesAsociadas;
import com.example.wposs_user.polariscoreandroid.java.Etapas;
import com.example.wposs_user.polariscoreandroid.java.Repuesto;
import com.example.wposs_user.polariscoreandroid.java.Terminal;
import com.example.wposs_user.polariscoreandroid.java.Validacion;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Vector;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private AppBarLayout appBar;
    private TabLayout tabs;
    private ViewPager viewPager;
    public RecyclerView recyclerView;
    public RecyclerView.LayoutManager layoutManager;
    private Vector<Repuesto> repuestos;
    private AutoCompleteTextView autocomplete_tipificaciones;
    private Vector<Etapas> etapas;



    private TextView serial;

    private Spinner spinner_estado_terminal;
    private EditText f_inicio;
    private EditText f_fin;
    FragmentManager fragmentManager;
    public static MainActivity objeto;
    private LinearLayout layout_terminal_etapas;
    private TextView usuario_drawer;
    private TextView correo_drawer;
    private ImageView imageView_perfil;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragmentManager = getSupportFragmentManager();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        usuario_drawer=(TextView)findViewById(R.id.usuario_drawer);
        correo_drawer=(TextView)findViewById(R.id.correo_drawer);
        imageView_perfil=(ImageView) findViewById(R.id.imageView_perfil);
        setTitle(null);
        setSupportActionBar(toolbar);

        objeto = this;

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        /*usuario_drawer.setText(Global.NOMBRE);
        correo_drawer.setText(Global.EMAIL);

        imageView_perfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentManager.beginTransaction().replace(R.id.contenedor_main, new PerfilFragment()).commit();
            }
        });*/

        //CARGAR LA FOTO DE PERFIL DEL DRAWER


      /*  //extraemos el drawable en un bitmap
        Drawable originalDrawable = getResources().getDrawable(R.drawable.foto_perfil);
        Bitmap originalBitmap = ((BitmapDrawable) originalDrawable).getBitmap();

        //creamos el drawable redondeado
        RoundedBitmapDrawable roundedDrawable =
                RoundedBitmapDrawableFactory.create(getResources(), originalBitmap);

        //asignamos el CornerRadius
        roundedDrawable.setCornerRadius(originalBitmap.getHeight());

        ImageView imageView = (ImageView) findViewById(R.id.imageView_perfil);

        imageView.setImageDrawable(roundedDrawable.getCurrent());*/
     fragmentManager.beginTransaction().replace(R.id.contenedor_main, new InicialFragment()).commit();

    }

    @Override
    public void onBackPressed() {

        int count = getSupportFragmentManager().getBackStackEntryCount();

        if (count == 0) {
            super.onBackPressed();
            //additional code
        } else {
            getSupportFragmentManager().popBackStack();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent i;
        FragmentManager fragmentManager = getSupportFragmentManager();
        switch (item.getItemId()) {
            case R.id.btn_search:
                fragmentManager.beginTransaction().replace(R.id.contenedor_main, new ConsultaTerminalesSerial()).commit();//Buscar
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // AL SELECCIONAR ALGUUNA OPCION DEL MENU
        // FragmentManager fragmentManager = getSupportFragmentManager();
        int id = item.getItemId();
       /* if (id == R.id.imageView_perfil) {
            fragmentManager.beginTransaction().replace(R.id.contenedor_main, new PerfilFragment()).addToBackStack(null).commit();
            // cargarDatosPerfil();
        }else */
        if (id == R.id.nav_perfil) {
            fragmentManager.beginTransaction().replace(R.id.contenedor_main, new PerfilFragment()).addToBackStack(null).commit();
            // cargarDatosPerfil();
        } else if (id == R.id.nav_stock) {
            fragmentManager.beginTransaction().replace(R.id.contenedor_main, new StockFragment()).commit();
        } else if (id == R.id.nav_consultar_terminales_reparadas) {
            fragmentManager.beginTransaction().replace(R.id.contenedor_main, new ConsultaTerminalesSerial()).commit();

        } else if (id == R.id.nav_productividad) {
            fragmentManager.beginTransaction().replace(R.id.contenedor_main, new ProductividadFragment()).commit();

        } else if (id == R.id.nav_cerrar_sesion) {
            Global.WEB_SERVICE = "/PolarisCore/Users/close";

            new TaskCerrarSesion().execute();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return false;
    }






    /*************************************************************************************
     * CLASE QUE CONSUME EL SERVICIO PARA LISTAR LAS VALIDACIONES
     *
     ***************************************************** **/

//******************consumir servicio cerrar sesion
    class TaskCerrarSesion extends AsyncTask<String, Void, Boolean> {

        ProgressDialog progressDialog;
        int trans = 0;


        /*******************************************************************************
         Método       : onPreExecute
         Description  : Se ejecuta antes de realizar el proceso, muestra una ventana con uin msj de espera
         *******************************************************************************/
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(MainActivity.this, R.style.MyAlertDialogStyle);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setCancelable(false);
            progressDialog.setMessage("Finalizando sesión...");
            progressDialog.show();
        }


        /*******************************************************************************
         Método       : doInBackground
         Description  : Se ejecuta para realizar la transacción y verificar coenxión
         *******************************************************************************/
        @Override
        protected Boolean doInBackground(String... strings) {
            Messages.packMsgCerrarSesion();

            trans = TCP.transaction(Global.outputLen);
            // Verifica la transacción
            if (trans == Global.TRANSACTION_OK) {
                return true;
            } else
                return false;
        }

        /*******************************************************************************
         Método       : onPostExecute
         Description  : Se ejecuta después de realizar el doInBackground
         *******************************************************************************/
        @Override
        protected void onPostExecute(Boolean value) {

            progressDialog.dismiss();

            if (value) {
                if (Messages.unPackMsgCerrarSesion(objeto)) {
                    Global.enSesion = true;
                    Global.StatusExit = true;
                    Intent i = new Intent(objeto, Activity_login.class);
                    startActivity(i);
                    finish();
                } else {
                    // Si el login no es OK, manda mensaje de error
                    try {
                        // Utils.GoToNextActivity(Activity_login.this, DialogError.class, Global.StatusExit);
                        Toast.makeText(MainActivity.this, Global.mensaje, Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            } else {
                switch (Utils.validateErrorsConexion(false, trans, MainActivity.this)) {

                    case 0:                                                                         // En caso de que continue = true y error data
                        break;

                    case 1:                                                                         // En caso de que continue = false y error data
                        break;

                    default:                                                                        // Errores de conexion
                        Global.MsgError = Global.MSG_ERR_CONEXION;
                        Global.mensaje = Global.MsgError;
                        Global.StatusExit = false;
                        Toast.makeText(MainActivity.this, Global.mensaje, Toast.LENGTH_LONG).show();
                        break;
                }

                Toast.makeText(MainActivity.this, Global.mensaje, Toast.LENGTH_LONG).show();
            }
            // TCP.disconnect();
        }


    }

    public RecyclerView getRecyclerView() {
        return recyclerView;
    }

    public void setRecyclerView(RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
    }

    public RecyclerView.LayoutManager getLayoutManager() {
        return layoutManager;
    }

    public void setLayoutManager(RecyclerView.LayoutManager layoutManager) {
        this.layoutManager = layoutManager;
    }

    public EditText getF_inicio() {
        return f_inicio;
    }

    public void setF_inicio(EditText f_inicio) {
        this.f_inicio = f_inicio;
    }

    public EditText getF_fin() {
        return f_fin;
    }

    public void setF_fin(EditText f_fin) {
        this.f_fin = f_fin;
    }


}