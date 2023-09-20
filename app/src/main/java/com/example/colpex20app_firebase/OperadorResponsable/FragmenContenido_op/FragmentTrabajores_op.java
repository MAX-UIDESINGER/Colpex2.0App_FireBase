package com.example.colpex20app_firebase.OperadorResponsable.FragmenContenido_op;

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
import com.example.colpex20app_firebase.OperadorResponsable.AgregarDatosTrabajores.FragmentBlanqueadoTrabajo;
import com.example.colpex20app_firebase.OperadorResponsable.AgregarDatosTrabajores.FragmentDesgomadoTrabajo;
import com.example.colpex20app_firebase.OperadorResponsable.AgregarDatosTrabajores.FragmentNeutralizadoTrabajo;
import com.example.colpex20app_firebase.R;
import com.google.android.material.tabs.TabLayout;


public class FragmentTrabajores_op extends Fragment {

    private ViewPager viewPager;
    private TabLayout tabLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().setTitle(" Trabajadores");
        return inflater.inflate(R.layout.fragment_trabajores_op, container, false);
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
        vpAdapter.addFragment(new FragmentDesgomadoTrabajo(), "Desgomado");
        vpAdapter.addFragment(new FragmentNeutralizadoTrabajo(), "Neutralizado");
        vpAdapter.addFragment(new FragmentBlanqueadoTrabajo(), "Blanqueado");
        viewPager.setAdapter(vpAdapter);

        //viewPager2.setAdapter(new NegocioPagerAdacter((FragmentActivity) requireContext()));

      //  mostrarTabs();
    }

    private void mostrarTabs() {
     //  TabLayoutMediator tabLayoutMediator = new TabLayoutMediator(
      //          tabLayout, viewPager, new TabLayoutMediator.TabConfigurationStrategy() {
      //      @Override
       //     public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
        //        switch (position) {
        //            case 0: {
          //              tab.setText("Desgomado");
         //               //tab.setIcon(R.drawable.colpexvertical);
          //              break;
        //            }
          //          case 1: {
           //             tab.setText("Neutralizado");
          //              break;
           //         }
             //       case 2: {
              //          tab.setText("Blanqueado");
               //         BadgeDrawable badgeDrawable = tab.getOrCreateBadge();
             //           //badgeDrawable.setBackgroundColor(ContextCompat.getColor(getActivity().getApplicationContext(), R.color.colorAccent));
             //           badgeDrawable.setVisible(true);
             //           badgeDrawable.setNumber(position);
             //           badgeDrawable.setMaxCharacterCount(3);
             //           break;
                    }

          //      }

        //    }
      //  }
    //    );
    //    tabLayoutMediator.attach();
        //quitar notification
     //   viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
      //      @Override
      //      public void onPageSelected(int position) {
          //      super.onPageSelected(position);
           //     BadgeDrawable badgeDrawable = tabLayout.getTabAt(position)
           //             .getOrCreateBadge();
          //      badgeDrawable.setVisible(false);
         //   }
       // });
    //}
}