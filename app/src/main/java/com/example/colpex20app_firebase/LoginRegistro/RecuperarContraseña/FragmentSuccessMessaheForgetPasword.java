package com.example.colpex20app_firebase.LoginRegistro.RecuperarContraseña;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.colpex20app_firebase.R;


public class FragmentSuccessMessaheForgetPasword extends Fragment {

    Button RegresarLogin;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_success_messahe_forget_pasword, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //Tool Bar Metodo
        ToolbarMetodos(view);
        //metodo volver al login
        MetodoRegLogin(view);
        //Firebase
        InicarFirebase();

    }

    private void MetodoRegLogin(View view) {
        RegresarLogin = view.findViewById(R.id.login_btn);
        RegresarLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             //   Navigation.findNavController(view).navigate(R.id.nav_login);
            }
        });

    }

    private void InicarFirebase() {

    }

    private void ToolbarMetodos(View view) {

    }
}