package com.example.colpex20app_firebase.JefeProducion.FragmenContenido;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.colpex20app_firebase.Adactadores.Adacter_Operador.VPAdapter;
import com.example.colpex20app_firebase.JefeProducion.AgregarDatosTrabajo_jefe.FragmentBlanqueadoTrabajo_jefe;
import com.example.colpex20app_firebase.JefeProducion.AgregarDatosTrabajo_jefe.FragmentDesgomadoTrabajo_jefe;
import com.example.colpex20app_firebase.JefeProducion.AgregarDatosTrabajo_jefe.FragmentNeutralizadoTrabajo_jefe;
import com.example.colpex20app_firebase.R;
import com.google.android.material.tabs.TabLayout;

public class FragmentTrabajores extends Fragment {
    private ViewPager viewPager;
    private TabLayout tabLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().setTitle(" Trabajadores");
        return inflater.inflate(R.layout.fragment_trabajores, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //Navegacion TAB
        ViewPagerData(view);


    }

    private void ViewPagerData(View view) {
        viewPager = view.findViewById(R.id.view_page);
        tabLayout = view.findViewById(R.id.tab_layout);

        tabLayout.setupWithViewPager(viewPager);
        VPAdapter vpAdapter = new VPAdapter(getChildFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        vpAdapter.addFragment(new FragmentDesgomadoTrabajo_jefe(), "Desgomado");
        vpAdapter.addFragment(new FragmentNeutralizadoTrabajo_jefe(), "Neutralizado");
        vpAdapter.addFragment(new FragmentBlanqueadoTrabajo_jefe(), "Blanqueado");
        viewPager.setAdapter(vpAdapter);
    }


}