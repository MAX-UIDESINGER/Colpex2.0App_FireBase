package com.example.colpex20app_firebase.OperadorResponsable;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;


import com.example.colpex20app_firebase.Adactadores.Adacter_Operador.PagerAdapter;
import com.example.colpex20app_firebase.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

public class FragmentPanelBatch extends Fragment {

    TabLayout tabLayout;
    ViewPager2 viewPager2;
    PagerAdapter pagerAdapter;
    FloatingActionButton fabDesgomado, fabNeutralizado, fabBlanqueado;
    TextView textDesgomado, textNeutralizado, textBlanqueado;
    //TooLL bar
    Toolbar toolbar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_panel_batch, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //Tool Bar Metodo
        ToolbarMetodos(view);
        // animacionFab(view);
        // traer y mostar numero se lote
        numberBatch(view);
        MostarNLote(view);
        tabLayoutDatos(view);

    }

    private void ToolbarMetodos(View view) {
        toolbar = view.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void tabLayoutDatos(View view) {

        tabLayout = view.findViewById(R.id.tab_layout);
        viewPager2 = view.findViewById(R.id.view_page2);

        FragmentManager fm = getParentFragmentManager();
        pagerAdapter = new PagerAdapter(fm, getLifecycle());
        viewPager2.setAdapter(pagerAdapter);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager2.setCurrentItem(tab.getPosition());
                batchPosition(tab.getPosition());
                // animateFab(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                tabLayout.selectTab(tabLayout.getTabAt(position));
                batchPosition(position);
                // animateFab(position);

            }
        });
    }

    private void MostarNLote(View view) {
        TextView txBatch = (TextView) view.findViewById(R.id.txtBatch);
        String Batch = getActivity().getIntent().getStringExtra("Batch");
        txBatch.setText(Batch);
        //---------------------------------------------------------
        TextView txvnumerlote = (TextView) view.findViewById(R.id.txvnumerlote);
        String NLote = getActivity().getIntent().getStringExtra("NumeroLote");
        txvnumerlote.setText(NLote);

    }

    private void numberBatch(View view) {
        textDesgomado = view.findViewById(R.id.edtCantBatchDesgomado);
        textNeutralizado = view.findViewById(R.id.edtCantBatchNeutralizado);
        textBlanqueado = view.findViewById(R.id.edtCantBatchBlanqueado);
    }

    private void batchPosition(int position) {
        switch (position) {
            case 1:
                textNeutralizado.setVisibility(View.VISIBLE);
                textBlanqueado.setVisibility(View.GONE);
                textDesgomado.setVisibility(View.GONE);

                break;
            case 2:
                textBlanqueado.setVisibility(View.VISIBLE);
                textNeutralizado.setVisibility(View.GONE);
                textDesgomado.setVisibility(View.GONE);

                break;
            case 0:
            default:
                textDesgomado.setVisibility(View.VISIBLE);
                textNeutralizado.setVisibility(View.GONE);
                textBlanqueado.setVisibility(View.GONE);
                break;
        }
    }

    private void animacionFab(View view) {
        //    fabDesgomado = view.findViewById(R.id.RegistarDesgomado);
        //    fabNeutralizado = view.findViewById(R.id.RegistarNeutralizado);
        //   fabBlanqueado = view.findViewById(R.id.RegistarBlanqueado);
    }

    private void animateFab(int position) {
        switch (position) {
            case 1:
                fabNeutralizado.show();
                fabBlanqueado.hide();
                fabDesgomado.hide();

                break;
            case 2:
                fabBlanqueado.show();
                fabNeutralizado.hide();
                fabDesgomado.hide();

                break;
            case 0:
            default:
                fabDesgomado.show();
                fabNeutralizado.hide();
                fabBlanqueado.hide();
                break;
        }
    }

}