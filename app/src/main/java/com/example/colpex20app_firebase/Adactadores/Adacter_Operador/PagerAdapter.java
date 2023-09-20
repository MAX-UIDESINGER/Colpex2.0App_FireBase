package com.example.colpex20app_firebase.Adactadores.Adacter_Operador;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.colpex20app_firebase.OperadorResponsable.AgregarDatos.FragmentBlanqueado;
import com.example.colpex20app_firebase.OperadorResponsable.AgregarDatos.FragmentDesgomado;
import com.example.colpex20app_firebase.OperadorResponsable.AgregarDatos.FragmentNeutralizado;

public class PagerAdapter extends FragmentStateAdapter {


    public PagerAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
       switch (position){
           case 1:
               return new FragmentNeutralizado();//2
           case 2:
               return  new FragmentBlanqueado();//3
           default:
               return new FragmentDesgomado();//1
       }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
