package com.example.colpex20app_firebase.LoginRegistro.RecuperarContraseña;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.example.colpex20app_firebase.R;


public class Fragment_make_selection extends Fragment {

    RelativeLayout iniciartelefono;
    RelativeLayout iniciarCorreo;
    //TooLL bar
    Toolbar toolbar;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_make_selection, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //Tool Bar Metodo
        ToolbarMetodos(view);
        //metodo de llefar al sms y correo
        Metodosmscorreo(view);
        //Firebase
        InicarFirebase();


    }

    private void Metodosmscorreo(View view) {

        iniciartelefono = view.findViewById(R.id.mobile);
        iniciarCorreo = view.findViewById(R.id.mail);

        iniciartelefono.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //   Navigation.findNavController(view).navigate(R.id.nav_credenciales);
            }
        });
        iniciarCorreo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            //    Navigation.findNavController(view).navigate(R.id.nav_inicio_contra);

            }
        });


    }

    private void ToolbarMetodos(View view) {
        toolbar = view.findViewById(R.id.toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             //   Navigation.findNavController(view).navigate(R.id.nav_inicio_contra);
            }
        });
    }

    private void InicarFirebase() {

    }


}