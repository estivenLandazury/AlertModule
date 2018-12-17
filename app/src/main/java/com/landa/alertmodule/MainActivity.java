package com.landa.alertmodule;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.landa.alertmodule.clases.AppMovil;
import com.landa.alertmodule.clases.Constante;
import com.landa.alertmodule.com.WEBUtilDomi;
import com.landa.alertmodule.interfaces.Alarma;
import com.landa.alertmodule.interfaces.Dispositivo;
import com.landa.alertmodule.interfaces.crearCuentaActivity;
import com.landa.alertmodule.interfaces.registroUsuario;

import java.io.IOException;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    FragmentManager fragmentManager=getSupportFragmentManager();
    TextView nombre, correo;
    BluetoothAdapter mBluetoAdapter;
    public String nombreDispositivo;
    public String macDispositivo;
    Vibrator vibrar;


    public final static String CONEXION= Constante.CONEXION;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        vibrar= (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fragmentManager.beginTransaction().replace(R.id.fragment_container, new registroUsuario()).commit();



        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

        View hView= navigationView.getHeaderView(0);
        nombre= hView.findViewById(R.id.username);
        correo= hView.findViewById(R.id.textView);

        nombre.setText(Constante.getUsrename());
        correo.setText(Constante.getHotmail());

        navigationView.setNavigationItemSelectedListener(this);

        IntentFilter filterBusqueda= new IntentFilter();
        filterBusqueda.addAction(BluetoothDevice.ACTION_ACL_CONNECTED);
        filterBusqueda.addAction(BluetoothDevice.ACTION_ACL_DISCONNECT_REQUESTED);
        filterBusqueda.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED);

        registerReceiver(mReceiver,filterBusqueda);


    }


    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {

        public void onReceive(Context context, Intent intent) {
            // Filtramos por la accion. que  Nos interesa detectar
            String action = intent.getAction();
            BluetoothDevice device= intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
            switch (action){
                case BluetoothDevice.ACTION_ACL_CONNECTED:
                    showToats("Está conectado al dispositvo "+ device.getName());



                    nombreDispositivo=device.getName();
                    macDispositivo= device.getAddress();

                    Constante.setNomreBluetooth(device.getName());
                    Constante.setMacBluetooth(device.getAddress());





                    break;
                case BluetoothDevice.ACTION_ACL_DISCONNECT_REQUESTED:
                    showToats("Se desconecto del dispositivo--"+ device.getName());

                   /* ArrayAdapter arrayAdapter = new ArrayAdapter<BluetoothDevice>(context,android.R.layout.simple_list_item_1, devicesLista);
                    viewsDevices.setAdapter(arrayAdapter);*/
                    break;
                case BluetoothDevice.ACTION_ACL_DISCONNECTED:
                    showToats("Se ha desconectado del dispositivo"+ device.getName());
                    if(Constante.getEstadoAlrma().equals("activada")){
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                añadirAlarma();
                                vibrar.vibrate(25000);
                                showToats("Se perdió la conexión con la manilla");


                            }
                        }).start();
                    }

                    Log.d("ACTION", "Dispositivo encontrado: " + device.getName() + "; MAC " + device.getAddress());



                    break;

                case BluetoothAdapter.ACTION_STATE_CHANGED:

                    if(mBluetoAdapter.isEnabled()) {
                        showToats("El bluetooh se ha activado ");
                    }

                    if(!mBluetoAdapter.isEnabled()){
                        showToats("El bluetooh se ha desactivado");

                    }
                    break;




            }
        }
    };

    public void  añadirAlarma(){
        Gson gson= new Gson();
        com.landa.alertmodule.clases.Alarma alarma= new com.landa.alertmodule.clases.Alarma();
        alarma.setAppMovil(Constante.getIdApp());
        alarma.setSolucionado(false);
        alarma.setDescripcion("Posible menor de edad en peligro");
        String AlarmaJson=  gson.toJson(alarma);

        try {
            WEBUtilDomi.JsonByPOSTrequest(CONEXION+"Alarmas", AlarmaJson);
        } catch (IOException e) {
            Log.e(">>>>>",""+"error al enviar alarma");

            e.printStackTrace();
        }



    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();



        switch (id){
            case R.id.nav_camera:
                fragmentManager.beginTransaction().replace(R.id.fragment_container, new registroUsuario()).commit();
                break;

            case  R.id.nav_gallery:
                fragmentManager.beginTransaction().replace(R.id.fragment_container, new Dispositivo()).commit();
                break;
            case  R.id.nav_slideshow:
                fragmentManager.beginTransaction().replace(R.id.fragment_container, new Alarma()).commit();
                break;

            case  R.id.nav_share:
                finish();
                break;

            case  R.id.nav_send:
                break;

        }




//        if (id == R.id.nav_camera) {
//            fragmentManager.beginTransaction().replace(R.id.fragment_container, new Dispositivo()).commit();
//
//        } else if (id == R.id.nav_gallery) {
//            fragmentManager.beginTransaction().replace(R.id.fragment_container, new registroUsuario()).commit();
//
//        } else if (id == R.id.nav_slideshow) {
//
//        } else if (id == R.id.nav_manage) {
//
//        } else if (id == R.id.nav_share) {
//
//        } else if (id == R.id.nav_send) {
//
//        }
//
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    private void showToats(final String mensaje) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MainActivity.this, mensaje, Toast.LENGTH_LONG).show();
            }
        });

    }
}
