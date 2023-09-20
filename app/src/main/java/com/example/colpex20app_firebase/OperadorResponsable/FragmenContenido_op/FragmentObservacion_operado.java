package com.example.colpex20app_firebase.OperadorResponsable.FragmenContenido_op;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.colpex20app_firebase.Adactadores.Adacter_Operador.LoteAdacter_op;
import com.example.colpex20app_firebase.Model.ObservacionItem;
import com.example.colpex20app_firebase.OperadorResponsable.PanelAgregarLotes;
import com.example.colpex20app_firebase.PrincipalClasses.ClasesJefeProducion.Lote;
import com.example.colpex20app_firebase.R;
import com.google.android.material.behavior.SwipeDismissBehavior;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FragmentObservacion_operado extends Fragment implements ObservacionItem {
    //Metodo ArrayList
    LoteAdacter_op adapter;
    ArrayList<Lote> VersionList;

    LoteAdacter_op adapterListview;
    ArrayList<Lote> VersionListview;

    RecyclerView recyclerView;
    RecyclerView recyclerViewLisview;
    //conexion Firebase
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    //mostrar no hay Internet
    private LinearLayout imgenUpWifi;
    private Button btnconexionWifi;
    //quitar la imagen
    private LinearLayout imgenUp;
    //Actualizar
    SwipeRefreshLayout sr;
    LoteAdacter_op adacter_op;
    //Progresor bar
    private ProgressBar progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().setTitle(" Observación");
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_observacion_op, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //imagen para que no se vea
        initViewModel(view);
        //Mensaje de no hay Imagen
        UPWifi(view);
        //metodo
        InicioRecyclerview(view);
        InicioRecycleLisView(view);
        //Refresh Actualizar
        Actualizacion(view);
        //FireBase
        IniciarFirebase();
        //ListadoDatos Lotes
        ListarDatos();
    }

    //Metodo_buscar Datos
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main_item_buscar_agregar, menu);
        MenuItem item = menu.findItem(R.id.search_menuNLote);
        SearchView searchView = (SearchView) item.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                processBuscar(s);
                return true;
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_Datos:
                // Navigation.findNavController(this.getActivity(), R.id.fragmentContainerView).navigate(R.id.panelAgregarDatos);
                Intent intent = new Intent(getActivity(), PanelAgregarLotes.class);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //Metodo_buscar Datos
    private void processBuscar(String s) {
        ArrayList<Lote> milistas = new ArrayList<>();
        for (Lote obj : VersionList) {
            if (obj.getNro_lote().toLowerCase().contains(s.toLowerCase())) {
                milistas.add(obj);
            } else if (obj.getProducto().toLowerCase().contains(s.toLowerCase())) {
                milistas.add(obj);
            } else if (obj.getCliente().toLowerCase().contains(s.toLowerCase())) {
                milistas.add(obj);
            } else if (obj.getTonelaje().toLowerCase().contains(s.toLowerCase())) {
                milistas.add(obj);
            } else if (obj.getBatch().toLowerCase().contains(s.toLowerCase())) {
                milistas.add(obj);
            } else if (obj.getFecha().toLowerCase().contains(s.toLowerCase())) {
                milistas.add(obj);
            } else if (obj.getAcido_fosforico().toLowerCase().contains(s.toLowerCase())) {
                milistas.add(obj);
            } else if (obj.getTemperatura_trabajo().toLowerCase().contains(s.toLowerCase())) {
                milistas.add(obj);
            } else if (obj.getT_residencia().toLowerCase().contains(s.toLowerCase())) {
                milistas.add(obj);
            } else if (obj.getUserName().toLowerCase().contains(s.toLowerCase())) {
                milistas.add(obj);
            }
        }
       // VersionList.clear();
       // VersionListview.clear();
        LoteAdacter_op adacter = new LoteAdacter_op(milistas);
        recyclerView.setAdapter(adacter);
        recyclerViewLisview.setAdapter(adacter);
    }


    private void Actualizacion(View view) {
        sr = view.findViewById(R.id.swipeRefreshLayout);
        sr.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onRefresh() {
                InicioRecyclerview(view);
                ListarDatos();
                adapter.notifyDataSetChanged();
                Toast.makeText(getContext(), "Actualizado", Toast.LENGTH_LONG).show();
                sr.setRefreshing(false);
            }
        });
    }

    private void ListarDatos() {
        if (!isConnected()) {
            imgenUpWifi.setVisibility(View.VISIBLE);
            imgenUp.setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.GONE);
            btnconexionWifi.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                }
            });
        } else {
            databaseReference.child("Lote").addValueEventListener(new ValueEventListener() {
                @SuppressLint("NotifyDataSetChanged")
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    VersionList.clear();
                    VersionListview.clear();
                    progressBar.setVisibility(View.GONE);
                    imgenUp.setVisibility(View.VISIBLE);
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        Lote lote = dataSnapshot.getValue(Lote.class);
                        //imagen para que no se vea
                        progressBar.setVisibility(View.GONE);
                        imgenUp.setVisibility(View.GONE);
                        imgenUpWifi.setVisibility(View.GONE);
                        VersionList.add(lote);
                        VersionListview.add(lote);
                    }
                    adapter.notifyDataSetChanged();
                    adapterListview.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    progressBar.setVisibility(View.GONE);
                    imgenUp.setVisibility(View.VISIBLE);
                    Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();

                }
            });
        }
    }


    private void initViewModel(View view) {
        progressBar = (ProgressBar) view.findViewById(R.id.progress_bar);
    }

    @Override
    public void onDataChanged() {
        if (progressBar != null) {
            progressBar.setVisibility(View.GONE);
        }
    }

    private void InicioRecyclerview(View view) {
        // RECYCLER VIEW
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerViewLista);
        VersionList = new ArrayList<>();
        //liner layaout
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        recyclerView.setHasFixedSize(true);

        adapter = new LoteAdacter_op(VersionList);
        recyclerView.setAdapter(adapter);
    }

    private void InicioRecycleLisView(View view) {
        // RECYCLER VIEW
        recyclerViewLisview = (RecyclerView) view.findViewById(R.id.recyclerViewLisview);
        VersionListview = new ArrayList<>();
        //liner layaout
        //  recyclerViewLisview.setLayoutManager(new LinearLayoutManager(this.getContext()));
        recyclerViewLisview.setHasFixedSize(true);

        recyclerViewLisview.setLayoutManager(new GridLayoutManager(this.getContext(), 2));
        //Adapter
        adapterListview = new LoteAdacter_op(VersionListview);
        recyclerViewLisview.setAdapter(adapterListview);
    }

    //Wifi_Validar
    private boolean isConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        @SuppressLint("MissingPermission") NetworkInfo wifiConn = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        @SuppressLint("MissingPermission") NetworkInfo mobileConn = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        if ((wifiConn != null && wifiConn.isConnected() || (mobileConn != null && mobileConn.isConnected()))) {
            return true;
        } else {
            return false;
        }
    }

    //Wifi_variables
    private void UPWifi(View view) {
        imgenUp = (LinearLayout) view.findViewById(R.id.imgenUp);
        imgenUpWifi = (LinearLayout) view.findViewById(R.id.imgenUpWifi);
        btnconexionWifi = (Button) view.findViewById(R.id.btnconexionWifi);
    }

    private void IniciarFirebase() {
        FirebaseApp.initializeApp(this.getContext());
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
    }


}