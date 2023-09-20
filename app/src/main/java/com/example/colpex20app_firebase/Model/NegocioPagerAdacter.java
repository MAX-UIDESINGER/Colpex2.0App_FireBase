package com.example.colpex20app_firebase.Model;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.colpex20app_firebase.OperadorResponsable.AgregarDatosTrabajores.FragmentBlanqueadoTrabajo;
import com.example.colpex20app_firebase.OperadorResponsable.AgregarDatosTrabajores.FragmentDesgomadoTrabajo;
import com.example.colpex20app_firebase.OperadorResponsable.AgregarDatosTrabajores.FragmentNeutralizadoTrabajo;


public class NegocioPagerAdacter extends FragmentStateAdapter {

    private static int numOfTabs = 3;

    public NegocioPagerAdacter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new FragmentDesgomadoTrabajo();
            case 1:
                return new FragmentNeutralizadoTrabajo();
            default:
                return new FragmentBlanqueadoTrabajo();
        }
    }

    @Override
    public int getItemCount() {
        return numOfTabs;
    }
}
