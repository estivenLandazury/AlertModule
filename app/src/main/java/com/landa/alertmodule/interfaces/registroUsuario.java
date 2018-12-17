package com.landa.alertmodule.interfaces;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.landa.alertmodule.R;
import com.landa.alertmodule.clases.AppMovil;
import com.landa.alertmodule.clases.Constante;
import com.landa.alertmodule.clases.Encargado;
import com.landa.alertmodule.clases.RolUsuario;
import com.landa.alertmodule.clases.User;
import com.landa.alertmodule.clases.Usuario;
import com.landa.alertmodule.clases.UsuarioDocumento;
import com.landa.alertmodule.com.WEBUtilDomi;

import java.io.IOException;


public class registroUsuario extends Fragment implements View.OnClickListener {
    public final static String CONEXION= Constante.CONEXION;

    EditText  nombre, apellido,numeroDocumento;
    Spinner tipodocumentos,tipoUsuario;
    String documentoSelecionado;
    String TipoUsuarioSelecionado;
    Button enviar;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


View v = inflater.inflate(R.layout.fragment_registro_usuario, container,false);

        tipodocumentos= v.findViewById(R.id.tipoDC);
        tipoUsuario= v.findViewById(R.id.tipoUsu);

        nombre= v.findViewById(R.id.txtUserName);
        apellido=v.findViewById(R.id.txtApellido);
        numeroDocumento=v.findViewById(R.id.numDoc);
        enviar= v.findViewById(R.id.btnRegister);

        final ArrayAdapter<String> myAdapter= new ArrayAdapter<String>(this.getActivity(),android.R.layout.simple_spinner_item,getResources().getStringArray(R.array.TipoDocumento));
        myAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        tipodocumentos.setAdapter(myAdapter);

        final ArrayAdapter<String> myAdapter2= new ArrayAdapter<String>(this.getActivity(),android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.TipoUsuario));
        myAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        tipoUsuario.setAdapter(myAdapter2);




        /** Tipo Usuario*/
        tipoUsuario.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TipoUsuarioSelecionado=myAdapter2.getItem(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        /** Tipo documentos select*/
        tipodocumentos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                documentoSelecionado= myAdapter.getItem(position);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        enviar.setOnClickListener(this);
        // Inflate the layout for this fragment
        return v;
    }






    private void showToats(final String mensaje) {


                Toast.makeText(getActivity(), mensaje, Toast.LENGTH_LONG).show();




    }

    public Usuario retornarUsuario(String numeroDocumento)  {
        Usuario  usuario=null;
        String Jsonusu = null;
        try {
            Jsonusu = WEBUtilDomi.GETrequest(CONEXION + "usuario/" + numeroDocumento);
            Gson gson = new Gson();
            usuario = gson.fromJson(Jsonusu, Usuario.class);
        } catch (IOException e) {

            Log.e(">>>>>", "" + "error al tarer Json de usuario "+ numeroDocumento);
            e.printStackTrace();

        }
        return usuario;

    }


    public void AñadirUsuario(String nombre, String apellido, String idCuenta,String numeroDocumento) throws IOException {

        Gson gson = new Gson();
        Usuario usuario = new Usuario();
        usuario.setNombre(nombre);
        usuario.setApellido(apellido);
        usuario.setNumeroDocumento(numeroDocumento);
        usuario.setUser(idCuenta);
        String jsonNombre = gson.toJson(usuario);

        /*se envía el usuario por el JSON*/
        try {

            Usuario us= retornarUsuario(numeroDocumento);
            Log.e(">>>>>",""+"usurio que retorna: "+ us);

            if(us==null) {
                final String respuesta = WEBUtilDomi.JsonByPOSTrequest(CONEXION + "usuarios", jsonNombre);
            }else{
                throw new IOException("El usuario con el numero documento: " + numeroDocumento + "\n" + "ya se encuentra registrado");
            }
        }catch (IOException e){
            throw new IOException(e.getMessage());
        }


    }


    public void añadirRolUsuario(String idUsuario, String rolUsuario){
        Gson gson = new Gson();
        RolUsuario rol= new RolUsuario();
        rol.setTipoUsuario(rolUsuario);
        rol.setUsuario(idUsuario);
        String jsonNombre = gson.toJson(rol);
        ;
        try {
            final String respuesta = WEBUtilDomi.JsonByPOSTrequest(CONEXION+"rolUsuarios", jsonNombre);
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(">>>>>",""+"error al añadir rolUsuario");

        }
    }


    public void añadirUsuarioDocumento(String idUsuario, String tipoDocumento) {
        Gson gson = new Gson();
        UsuarioDocumento usud = new UsuarioDocumento();
        usud.setTipoDocumento(tipoDocumento);
        usud.setUsuario(idUsuario);
        String jsonNombre = gson.toJson(usud);

        try {
            final String respuesta = WEBUtilDomi.JsonByPOSTrequest(CONEXION + "UsuarioDocumentos", jsonNombre);

        } catch (IOException e) {
            Log.e(">>>>>", "" + "error al añadir UsuarioDocumento");

        }


    }


    public int tipoDocumentoSeleccionado(){
        int tipo=0;
        if(documentoSelecionado.equals("cedula")){
            tipo= Constante.TIPO_DOC_CEDULA;
        }else if(documentoSelecionado.equals("Tarjeta Identidad")){
            tipo= Constante.TIPO_DOC_IDENTIDAD;
        }

        return tipo;
    }

    public int tipoUsuarioSeleccionado(){

        int tipo=0;

        if(TipoUsuarioSelecionado.equals("Ambulatorio")){
            tipo= Constante.ROL_AMBULATORIO;
        }else
        if(TipoUsuarioSelecionado.equals("Hospitalario")){
            tipo= Constante.ROL_HOSPITALARIO;
        }else if(TipoUsuarioSelecionado.equals("Acompañante")){
            tipo= Constante.ROL_ACOMPAÑANTE;
        }



        return tipo;
    }


    public void añadirEncargado(String idEncargado, String idUsuario){

        Gson gson= new Gson();
        Encargado encargado= new Encargado();
        encargado.setIdEncargado(idEncargado);
        encargado.setIdUsuario(idUsuario);
        String jsonNombre = gson.toJson(encargado);
        try{
            final String respuesta = WEBUtilDomi.JsonByPOSTrequest(CONEXION+"Encargados", jsonNombre);

        }catch (IOException e){
            Log.e(">>>>>",""+"error al añadir la manillaApp "+"idEncargado: "+ idEncargado +" idUsuario: "+ idUsuario);

        }


    }






    @Override
    public void onClick(View v) {

        if(v.equals(enviar)){

            new Thread(new Runnable() {
                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public void run() {
                    try {


                        AñadirUsuario(nombre.getText().toString(), apellido.getText().toString(), null, numeroDocumento.getText().toString());

                        Usuario usu = retornarUsuario( numeroDocumento.getText().toString());
                        /* Aquí se añade el rol de usuario encargado por defualt creado*/
                        int tipoUsuario= tipoUsuarioSeleccionado();
                        añadirRolUsuario(usu.getId(), tipoUsuario + "");

                        /* Aquí se añade el tipo documento que el usuario utiliza*/
                        int tipoDocumento= tipoDocumentoSeleccionado();
                        añadirUsuarioDocumento(usu.getId(),""+tipoDocumento );

                        Constante.setIdUsuarioPediatrico(usu.getId());

                        /*Realiza la asociacón del usuario encargado y el usuario pediatríco*/
                        /* Aquí se añade el rol de usuario creado anteriormente*/
                        Log.e(">>>>>", "" + Constante.getIdUsuarioEncargado()+"  "+usu.getId());

                        añadirEncargado(Constante.getIdUsuarioEncargado(),""+ usu.getId());


                    } catch (IOException e) {
                        e.printStackTrace();
                        showToats(e.getMessage());

                        Log.e(">>>>>", "" + "error de registro");
                    } catch (Exception e) {
                        showToats(e.getMessage());

                    }
                }
            }).start();



            showToats("se agregó el usuario correctamente");


        }

    }
}
