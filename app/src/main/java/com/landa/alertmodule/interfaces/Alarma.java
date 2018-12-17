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
import android.widget.TextView;
import android.widget.Toast;

import com.landa.alertmodule.R;
import com.landa.alertmodule.clases.Constante;
import com.landa.alertmodule.clases.Manilla;
import com.landa.alertmodule.clases.Usuario;


public class Alarma extends Fragment implements View.OnClickListener{

TextView estadoAlrma;
Button activar, desactivar;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_alarma, container, false);

        estadoAlrma=v.findViewById(R.id.estado);
        estadoAlrma.setText(Constante.getEstadoAlrma());
        activar=v.findViewById(R.id.activar);
        desactivar=v.findViewById(R.id.desactivar);

        activar.setOnClickListener(this);
        desactivar.setOnClickListener(this);


        return v ;
    }


    @Override
    public void onClick(View v) {
        if(v.equals( activar)){

        Constante.setEstadoAlrma("activada");
        estadoAlrma.setText(Constante.getEstadoAlrma());

            showToats("Activaste la alerta de seguridad ");
        }

        if(v.equals(desactivar)){

            Constante.setEstadoAlrma("desactivada");
            estadoAlrma.setText(Constante.getEstadoAlrma());

            showToats("Desactivaste la alerta de seguridad ");
        }
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
