package com.landa.alertmodule.interfaces;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.landa.alertmodule.R;
import com.landa.alertmodule.clases.Constante;
import com.landa.alertmodule.clases.Manilla;
import com.landa.alertmodule.clases.ManillaApp;
import com.landa.alertmodule.clases.ManillaUsuario;
import com.landa.alertmodule.clases.Usuario;
import com.landa.alertmodule.com.WEBUtilDomi;

import java.io.IOException;

public class Dispositivo extends Fragment implements View.OnClickListener {

    EditText nombreDis, macDis, idUsuario;
    Button asociar;
    public final static String CONEXION= Constante.CONEXION;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_dispositivo, container,false);
        nombreDis= v.findViewById(R.id.nombreDis);
        macDis=v.findViewById(R.id.txtmac);
        idUsuario=v.findViewById(R.id.txtIdUsuario);

        asociar=v.findViewById(R.id.asociar);

        nombreDis.setText(Constante.getNomreBluetooth());
        macDis.setText(Constante.getMacBluetooth());

        asociar.setOnClickListener(this);

        return v;
    }






    public void añadirmanilla(String nombreDispositivo, String macDispositivo, String idUsuario){

        /*Quita los dos puntos  del mac id y los remplaza por guiones*/
        String MCD= replaceCadena(macDispositivo);

        Manilla mani= existeManilla(MCD);


        if(mani==null){
            /*Se registra la manilla, ya que no existe en la base de datos*/
            Gson gmanil= new Gson();
            Manilla manilla= new Manilla();
            String MCDA= replaceCadena(macDispositivo);
            manilla.setMacId(MCDA);
            manilla.setNombre(nombreDispositivo);
            String manillaJSON=  gmanil.toJson(manilla);
            try {
                WEBUtilDomi.JsonByPOSTrequest(CONEXION+"Manillas", manillaJSON);
            } catch (IOException e) {
                Log.e(">>>>>",""+"error al agregar manilla solamente");

                e.printStackTrace();
            }

        }


    }


    public void añadirManillaUsuario(String macDispositivo, String idUsuario){

        ManillaUsuario mUsu= existeManillaUsuario(idUsuario);
        Gson gson= new Gson();
        ManillaUsuario manillaUsuario= new ManillaUsuario();
        manillaUsuario.setUsuario(idUsuario);
        manillaUsuario.setManilla(macDispositivo);

        String convertirJson= gson.toJson(manillaUsuario);

        try {
            Log.e(">>>>>",""+mUsu);

            if(mUsu==null) {
                Log.e(">>>>>",""+"entro al if");
                WEBUtilDomi.JsonByPOSTrequest(CONEXION + "ManillaUsuarios", convertirJson);
            }else{
                String mac= replaceCadena(macDispositivo);
                mUsu.setManilla(mac);
                String jsonmanilla=  gson.toJson(mUsu);
                WEBUtilDomi.JsonByPUTrequest(CONEXION+"ManillaUsuarioIdUs/"+idUsuario, convertirJson);



            }
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(">>>>>",""+"error al agregar ManillaUsuario: "+ "macId: "+manillaUsuario.getManilla()+ "  UsuarioId: "+ manillaUsuario.getUsuario());

        }


    }


    public ManillaUsuario existeManillaUsuario(String idUsuario){
        ManillaUsuario mani=null;

        try {
           String manil= WEBUtilDomi.GETrequest(CONEXION+"ManillaUsuarioIdUs/"+idUsuario);
            Gson gson = new Gson();
            mani = gson.fromJson(manil, ManillaUsuario.class);


        } catch (IOException e) {
            e.printStackTrace();
            Log.e(">>>>>",""+"erro al trae manilla Usuario con id usuario: "+ idUsuario);

        }

        return mani;

    }

    private String replaceCadena(String macId){
        String cadena= macId.replace(":","-");
        return cadena;
    }


    public Manilla existeManilla(String macDispositivo){
        Gson manilla = new Gson();
        Manilla mll=null;
        String MacId=replaceCadena(macDispositivo);
        try {
            String  manill = WEBUtilDomi.GETrequest(CONEXION+"Manilla/"+MacId);
            mll= manilla.fromJson(manill, Manilla.class);
        } catch (IOException e) {
            Log.e(">>>>>",""+"error al traer manilla"+MacId);
            e.printStackTrace();
        }

        return mll;

    }


    @Override
    public void onClick(View v) {
        if(v.equals( asociar)){

            new Thread(new Runnable() {
                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public void run() {



                    try {

                        if(!nombreDis.getText().toString().equals("")||!macDis.getText().toString().equals("")){

                            añadirmanilla(nombreDis.getText().toString(),macDis.getText().toString(),idUsuario.getText().toString());

                            /* Aqí obtengo la manilla creada, o la que ya existía*/
                            Manilla manilla= existeManilla(macDis.getText().toString());

                            Usuario us= retornarUsuario(idUsuario.getText().toString());



                            añadirManillaUsuario(manilla.getMacId(),us.getId() );

                            String maci= replaceCadena( Constante.getMacBluetooth());
                            añadirManillaApp(Constante.getIdApp(),maci);

                            showToats("se asoció la manilla exitosamente");


                        }else{
                        }




                    } catch (Exception e) {
                        showToats(e.getMessage());
                        Log.e(">>>>>", "" + "error de registro");

                    }
                }
            }).start();

        }
    }



    public ManillaApp existeManillaApp(String idApp){

ManillaApp Map= null;
        try{
            final String respuesta = WEBUtilDomi.GETrequest(CONEXION+"ManillaIdApp/"+idApp);
            Gson gson = new Gson();
            Map = gson.fromJson(respuesta, ManillaApp.class);

        }catch (IOException e){
            Log.e(">>>>>",""+"error al añadir la manillaApp "+"idApp: "+ idApp );
            }

        return Map;
    }

    public void añadirManillaApp(String idApp, String idDispositivo){

        ManillaApp map= existeManillaApp(idApp);
        Gson json= new Gson();
        ManillaApp maniApp= new ManillaApp();
        maniApp.setAppMovil(idApp);
        maniApp.setManilla(idDispositivo);
        String jsonNombre = json.toJson(maniApp);

        try{
            if(map==null) {
                final String respuesta = WEBUtilDomi.JsonByPOSTrequest(CONEXION + "ManillaApps", jsonNombre);
            }else{
                map.setManilla(idDispositivo);
               json= new Gson();
               jsonNombre = json.toJson(map);
                WEBUtilDomi.JsonByPUTrequest(CONEXION + "ManillaIdApp/"+idApp, jsonNombre);
            }

        }catch (IOException e){
            Log.e(">>>>>",""+"error al añadir la manillaApp "+"idApp: "+ idApp +" idMovil: "+ idDispositivo);

        }

    }
    public Usuario retornarUsuario(String numeroDocumento) throws IOException {
        Usuario  usuario=null;
        String Jsonusu = null;
        try {
            Jsonusu = WEBUtilDomi.GETrequest(CONEXION + "usuario/" + numeroDocumento);
            Gson gson = new Gson();
            usuario = gson.fromJson(Jsonusu, Usuario.class);
            if(usuario==null){
                throw new IOException("no existe el usuario con ese número de documento");
            }
        } catch (IOException e) {

            Log.e(">>>>>", "" + "error al tarer Json de usuario "+ numeroDocumento);
            throw new IOException(e.getMessage());


        }
        return usuario;

    }


    private void showToats(final String mensaje) {
       getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getContext(), mensaje, Toast.LENGTH_LONG).show();
            }
        });


    }
}
