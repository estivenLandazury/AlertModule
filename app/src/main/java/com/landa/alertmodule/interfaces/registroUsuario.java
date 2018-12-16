package com.landa.alertmodule.interfaces;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.landa.alertmodule.R;


public class registroUsuario extends Fragment implements View.OnClickListener {

    EditText  nombre;
    Spinner tipodocumentos,tipoUsuario;
    Button enviar;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


View v = inflater.inflate(R.layout.fragment_registro_usuario, container,false);


        tipodocumentos= v.findViewById(R.id.tipoDC);
        tipoUsuario= v.findViewById(R.id.tipoUsu);
        nombre= v.findViewById(R.id.txtUserName);
        enviar= v.findViewById(R.id.btnRegister);


        final ArrayAdapter<String> myAdapter= new ArrayAdapter<String>(this.getActivity(),android.R.layout.simple_spinner_item,getResources().getStringArray(R.array.TipoDocumento));
        myAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        tipodocumentos.setAdapter(myAdapter);

        final ArrayAdapter<String> myAdapter2= new ArrayAdapter<String>(this.getActivity(),android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.TipoUsuario));
        myAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        tipoUsuario.setAdapter(myAdapter2);

        enviar.setOnClickListener(this);
        // Inflate the layout for this fragment
        return v;
    }



    private void showToats(final String mensaje) {
                Toast.makeText(getContext(), mensaje, Toast.LENGTH_LONG).show();

    }


    @Override
    public void onClick(View v) {

        if(v.equals(enviar)){

            showToats("el mensaje es "+ nombre.getText().toString());
        }

    }
}
