package com.example.colpex20app_firebase.LoginRegistro.RecuperarContraseña;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.colpex20app_firebase.R;


public class FragmentverifiOTP extends Fragment {

    //PinView pinFromUser;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_fragmentverifi_o_t_p, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        variableOTP(view);

    }

    private void variableOTP(View view) {
      //  pinFromUser = view.findViewById(R.id.pin_view);

    }
}