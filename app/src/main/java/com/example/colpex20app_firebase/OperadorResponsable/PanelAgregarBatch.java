package com.example.colpex20app_firebase.OperadorResponsable;


import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;


import com.example.colpex20app_firebase.Adactadores.Adacter_Operador.PagerAdapter;
import com.example.colpex20app_firebase.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;


public class PanelAgregarBatch extends AppCompatActivity {

    TabLayout tabLayout;
    ViewPager2 viewPager2;
    PagerAdapter pagerAdapter;
    FloatingActionButton fabDesgomado , fabNeutralizado , fabBlanqueado;
    TextView textDesgomado,textNeutralizado,textBlanqueado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_panel_agregar_batch);
        //animacionFab();
        // traer y mostar numero se lote
        numberBatch();
        MostarNLote();
        tabLayoutDatos();

    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId()==android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void tabLayoutDatos() {

        tabLayout = findViewById(R.id.tab_layout);
        viewPager2 = findViewById(R.id.view_page2);

        FragmentManager fm = getSupportFragmentManager();
        pagerAdapter = new PagerAdapter(fm, getLifecycle());
        viewPager2.setAdapter(pagerAdapter);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager2.setCurrentItem(tab.getPosition());
                batchPosition(tab.getPosition());
                //animateFab(tab.getPosition());
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
                //animateFab(position);

            }
        });
    }

    private void MostarNLote() {
        TextView txvnumerlote = (TextView) findViewById(R.id.txvnumerlote);
        String NLote = getIntent().getStringExtra("NumeroLote");
        txvnumerlote.setText(NLote);
    }

    private void numberBatch() {
        textDesgomado = findViewById(R.id.edtCantBatchDesgomado);
        textNeutralizado = findViewById(R.id.edtCantBatchNeutralizado);
        textBlanqueado = findViewById(R.id.edtCantBatchBlanqueado);
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

    private void animacionFab() {
        //fabDesgomado = findViewById(R.id.RegistarDesgomado);
        //fabNeutralizado = findViewById(R.id.RegistarNeutralizado);
       // fabBlanqueado = findViewById(R.id.RegistarBlanqueado);
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
