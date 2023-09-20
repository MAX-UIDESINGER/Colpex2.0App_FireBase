package com.example.colpex20app_firebase.LoginRegistro;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.colpex20app_firebase.R;


public class FragmentPresentacion extends Fragment {

    Button iniciar, registro;
    //TooLL bar
    Toolbar toolbar;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_presentacion, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        iniciar = view.findViewById(R.id.login_btn);
        registro = view.findViewById(R.id.registro_btn);
        final NavController navController = Navigation.findNavController(view);
        iniciar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Navigation.findNavController(view).navigate(R.id.nav_login);
                navController.navigate(R.id.nav_login);

            }
        });

        registro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //  Navigation.findNavController(view).navigate(R.id.nav_registro);
                //getActivity().overridePendingTransition(R.anim.rigth_in, R.anim.rigth_out);
                navController.navigate(R.id.nav_registro);
            }
        });

    }
}