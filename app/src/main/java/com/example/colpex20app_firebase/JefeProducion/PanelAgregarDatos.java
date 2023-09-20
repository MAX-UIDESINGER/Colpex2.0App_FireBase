package com.example.colpex20app_firebase.JefeProducion;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.colpex20app_firebase.Adactadores.Adacter_Operador.VPAdapter;
import com.example.colpex20app_firebase.JefeProducion.AgregarDatos.FragmentCliente;
import com.example.colpex20app_firebase.JefeProducion.AgregarDatos.FragmentLote;
import com.example.colpex20app_firebase.JefeProducion.AgregarDatos.FragmentProducto;
import com.example.colpex20app_firebase.R;
import com.google.android.material.tabs.TabLayout;

public class PanelAgregarDatos extends AppCompatActivity {

    private Toolbar toolbar;

    private ViewPager viewPager;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_panel_agregar_datos);
        //Navegacion TAB
        ViewPagerData();
        //toolbar
        //Tool Bar Metodo
        ToolbarMetodos();
    }

    private void ToolbarMetodos() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId()==android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void ViewPagerData() {
        viewPager = findViewById(R.id.view_page);
        tabLayout = findViewById(R.id.tab_layout);

        tabLayout.setupWithViewPager(viewPager);
        VPAdapter vpAdapter = new VPAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        vpAdapter.addFragment(new FragmentLote(), "Lote");
        vpAdapter.addFragment(new FragmentProducto(), "Producto");
        vpAdapter.addFragment(new FragmentCliente(), "cliente");
        viewPager.setAdapter(vpAdapter);

    }


}



