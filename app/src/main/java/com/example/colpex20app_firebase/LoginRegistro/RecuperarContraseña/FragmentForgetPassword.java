package com.example.colpex20app_firebase.LoginRegistro.RecuperarContraseña;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.example.colpex20app_firebase.R;


public class FragmentForgetPassword extends Fragment {

    Button ok;

    //TooLL bar
    Toolbar toolbar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_forget_password, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //Tool Bar Metodo
        ToolbarMetodos(view);
        //medoto bottom iniciar el ok
        MetodookIniciar(view);
        //Firebase
        InicarFirebase();

    }

    private void MetodookIniciar(View view) {
        ok = view.findViewById(R.id.ok_btn);

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigation.findNavController(view).navigate(R.id.nav_actualizado);
            }
        });
    }
    private void ToolbarMetodos(View view) {
        toolbar = view.findViewById(R.id.toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             //   Navigation.findNavController(view).navigate(R.id.nav_selecciom);
            }
        });
    }


    private void InicarFirebase() {



    }
}