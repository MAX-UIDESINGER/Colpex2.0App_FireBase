package com.example.colpex20app_firebase.OperadorResponsable.AgregarDatosTrabajores;

import static android.widget.Toast.LENGTH_SHORT;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;


import com.example.colpex20app_firebase.Adactadores.AdacterDesgomado.ItemAdacterDesgomado;
import com.example.colpex20app_firebase.Model.RvItemClick;
import com.example.colpex20app_firebase.PrincipalClasses.ClassBlanqueado.ClaseBlanqueado;
import com.example.colpex20app_firebase.PrincipalClasses.ClassDesgomado.ClaseDesgomado;
import com.example.colpex20app_firebase.PrincipalClasses.ClassDesgomado.ItemDesgomado;
import com.example.colpex20app_firebase.PrincipalClasses.ClassNeutralizado.ClaseNeutralizado;
import com.example.colpex20app_firebase.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
public class FragmentDesgomadoTrabajo extends Fragment implements RvItemClick {
    // texview Desgomado
    EditText edtTonelajeDesgomado, edtAcidoFosforicoDesgomado, edtTempTrabajoDesgomado, edtTResidenciaDesgomado;

    // Inputlayout Desgomado
    private TextInputLayout txtInputReactorDegummed, txtInputTonDegummed, txtInputPhosphoricAcidDegummed,
            txtInputLotDegummed, txtInputWorkTemperatureDegummed, txtInputHomeDegummed;
    //
    RecyclerView recyclerView;
    //Item de Lote
    private List<ItemDesgomado> itemList = new ArrayList<>();
    ItemAdacterDesgomado adapter;
    //Contendio
    List<ClaseDesgomado> subItemListDesgomado = new ArrayList<>();

    //mostrar no hay Internet
    private LinearLayout imgenUpWifi;
    private Button btnconexionWifi;

    //conexion Firebase
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    //quitar la imagen de fondo
    private LinearLayout imgenUp;
    //Actualizar
    SwipeRefreshLayout sr;
    //Progresor bar
    private ProgressBar progressBar;

    //Auto Complete Reactor
    AutoCompleteTextView txAutoCompleteReactor;
    ArrayList<String> allNamesReactor;
    //Auto Complete Lote
    AutoCompleteTextView txAutoCompleteLote;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
         setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_desgomado_trabajo, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //imagen para que no se vea
        initViewModel(view);
        //
        DatosItemDesgomados();
        //Mensaje de no hay Imagen
        UPWifi(view);
        //metodo
        InicioRecyclerview(view);
        //Refresh Actualizar
        Actualizacion(view);
        //FireBase
        IniciarFirebase();
        ListarDatosDesgomado(view);
    }

    private void DatosItemDesgomados() {

        adapter = new ItemAdacterDesgomado(itemList,this) {
            @Override
            public void DesgomadoMenuItem(int position, MenuItem menuItem, ClaseDesgomado item) {
                switch (menuItem.getItemId()) {
                    case R.id.edit:
                        MetodoEditar(item);
                        break;
                    case R.id.delete:
                        MetodoDelete(item, position);
                }
            }

            @Override
            public void NeutralizadoMenuItem(int position, MenuItem menuItem, ClaseNeutralizado item) {

            }

            @Override
            public void BlanqueadoMenuItem(int position, MenuItem menuItem, ClaseBlanqueado item) {

            }

            @Override
            public void onDataChanged() {
                if (progressBar != null) {
                    progressBar.setVisibility(View.GONE);
                }

            }
        };
    }


    //Metodo_buscar Datos
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main_item_buscar_datos, menu);
        MenuItem item = menu.findItem(R.id.search_menuNLote);
        SearchView searchView = (SearchView) item.getActionView();
        searchView.setSubmitButtonEnabled(false);
        searchView.setQueryHint(" Escriba aquí para Buscar");
        searchView.setBackgroundResource(R.drawable.roundedsearch);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public boolean onQueryTextChange(String s) {
                if (s.length() > 0) {
                    filterData(s);
                } else {
                    DatosItemDesgomados();
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }
                return true;
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }

    @SuppressLint("NotifyDataSetChanged")
    private void filterData(String query) {
        List<ItemDesgomado> filteredItemList = new ArrayList<>();
        for (ItemDesgomado item : itemList) {
            String itemTitleLote = item.getItemTitleLote().toLowerCase();
            if (itemTitleLote.contains(query.toLowerCase())) {
                filteredItemList.add(item);
            } else {
                List<com.example.colpex20app_firebase.PrincipalClasses.ClassDesgomado.ClaseDesgomado> subItemListDesgomado = item.getSubItemListDesgomado();
                List<com.example.colpex20app_firebase.PrincipalClasses.ClassDesgomado.ClaseDesgomado> filteredSubItemList = new ArrayList<>();
                for (com.example.colpex20app_firebase.PrincipalClasses.ClassDesgomado.ClaseDesgomado dato : subItemListDesgomado) {
                    String searchValue = query.toLowerCase();
                    if (dato.getNumeroLote().toLowerCase().contains(searchValue) ||
                            dato.getReactor().toLowerCase().contains(searchValue) ||
                            dato.getContadorBatch().toLowerCase().contains(searchValue) ||
                            dato.getTonelaje().toLowerCase().contains(searchValue) ||
                            dato.getAcidoFosforico().toLowerCase().contains(searchValue) ||
                            dato.getLoteacido().toLowerCase().contains(searchValue) ||
                            dato.getLoteacido().toLowerCase().contains(searchValue) ||
                            dato.getTempTrabajo().toLowerCase().contains(searchValue) ||
                            dato.getTResidencia().toLowerCase().contains(searchValue) ||
                            dato.getUserName().toLowerCase().contains(searchValue) ||
                            dato.getFechaHora().toLowerCase().contains(searchValue)) {
                        filteredSubItemList.add(dato);
                    }
                }
                if (!filteredSubItemList.isEmpty()) {
                    ItemDesgomado filteredItem = new ItemDesgomado(itemTitleLote, filteredSubItemList);
                    filteredItemList.add(filteredItem);
                }
            }
        }
        // Create and set adapter for inner RecyclerView
        ItemAdacterDesgomado adapter = new ItemAdacterDesgomado(filteredItemList, this) {
            @Override
            public void onDataChanged() {
                if (progressBar != null) {
                    progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void DesgomadoMenuItem(int position, MenuItem menuItem, ClaseDesgomado item) {
                switch (menuItem.getItemId()) {
                    case R.id.edit:
                        MetodoEditar(item);
                        break;
                    case R.id.delete:
                        MetodoDelete(item, position);
                }
            }

            @Override
            public void NeutralizadoMenuItem(int position, MenuItem menuItem, ClaseNeutralizado item) {

            }

            @Override
            public void BlanqueadoMenuItem(int position, MenuItem menuItem, ClaseBlanqueado item) {

            }
        };
        //adapter.filterList(filteredItemList);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    //botonos de editar y eliminar para el Recycleview
    private void inserccion(ClaseDesgomado claseDesgomado) {
        //datos de Desgomado
        String Reactor = txAutoCompleteReactor.getText().toString();
        String Tonelaje = edtTonelajeDesgomado.getText().toString();
        String AcidoFosforico = edtAcidoFosforicoDesgomado.getText().toString();
        String Lote = txAutoCompleteLote.getText().toString();
        String TempTrabajo = edtTempTrabajoDesgomado.getText().toString();
        String TResidencia = edtTResidenciaDesgomado.getText().toString();
        //varibles de insercion set
        ClaseDesgomado d = new ClaseDesgomado();
        d.setId(claseDesgomado.getId());
        d.setNumeroLote(claseDesgomado.getNumeroLote());
        d.setReactor(Reactor);
        d.setContadorBatch(claseDesgomado.getContadorBatch());
        d.setTonelaje(Tonelaje);
        d.setAcidoFosforico(AcidoFosforico);
        d.setLoteacido(Lote);
        d.setTempTrabajo(TempTrabajo);
        d.setTResidencia(TResidencia);
        d.setUserName(claseDesgomado.getUserName());
        d.setImagenUrl(claseDesgomado.getImagenUrl());
        d.setFechaHora(claseDesgomado.getFechaHora());
        databaseReference.child("Desgomado").child(claseDesgomado.getNumeroLote()).child(claseDesgomado.getId()).setValue(d);
    }

    private void ListarDatosDesgomado(View view) {
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
            databaseReference.child("Desgomado").addValueEventListener(new ValueEventListener() {
                @SuppressLint("NotifyDataSetChanged")
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    itemList.clear();
                    progressBar.setVisibility(View.GONE);
                    imgenUp.setVisibility(View.VISIBLE);
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        String itemTitleLote = dataSnapshot.getKey();
                        subItemListDesgomado = new ArrayList<>();
                        for (DataSnapshot subSnapshot : dataSnapshot.getChildren()) {
                            ClaseDesgomado dato = subSnapshot.getValue(ClaseDesgomado.class);
                            subItemListDesgomado.add(dato);
                        }
                        ItemDesgomado item = new ItemDesgomado(itemTitleLote, subItemListDesgomado);
                        progressBar.setVisibility(View.GONE);
                        imgenUp.setVisibility(View.GONE);
                        itemList.add(item);
                    }
                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    imgenUp.setVisibility(View.VISIBLE);
                    Toast.makeText(getContext(), error.getMessage(), LENGTH_SHORT).show();
                }
            });
        }
    }

    private void initViewModel(View view) {
        progressBar = (ProgressBar) view.findViewById(R.id.progress_bar);
    }


    private void InicioRecyclerview(View view) {
        recyclerView = (RecyclerView) view.findViewById(R.id.lvDesgomado);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
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

    private void Actualizacion(View view) {
        sr = view.findViewById(R.id.swipeRefreshLayout);
        sr.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onRefresh() {
                //  InicioRecyclerview(view);
                ListarDatosDesgomado(view);
                adapter.notifyDataSetChanged();
                Toast.makeText(getContext(), "Actualizado", Toast.LENGTH_LONG).show();
                sr.setRefreshing(false);
            }
        });
    }

    private void IniciarFirebase() {
        FirebaseApp.initializeApp(this.getContext());
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
    }

    private boolean validar_desgomado() {
        boolean retorno = true;
        String Reactor, Tonelaje, AcidoFosforico, Lote, TempTrabajo, TResidencia;
        Reactor = txAutoCompleteReactor.getText().toString();
        Tonelaje = edtTonelajeDesgomado.getText().toString();
        AcidoFosforico = edtAcidoFosforicoDesgomado.getText().toString();
        Lote = txAutoCompleteLote.getText().toString();
        TempTrabajo = edtTempTrabajoDesgomado.getText().toString();
        TResidencia = edtTResidenciaDesgomado.getText().toString();

        if (Reactor.isEmpty()) {
            txtInputReactorDegummed.setError("Ingrese su Reactor");
            retorno = false;
        } else {
            txtInputReactorDegummed.setErrorEnabled(false);
        }
        if (Tonelaje.isEmpty()) {
            txtInputTonDegummed.setError("Ingrese su Tonelaje ");
            retorno = false;
        } else {
            txtInputTonDegummed.setErrorEnabled(false);
        }
        if (AcidoFosforico.isEmpty()) {
            txtInputPhosphoricAcidDegummed.setError("Ingrese su Acido Fosfórico ");
            retorno = false;
        } else {
            txtInputPhosphoricAcidDegummed.setErrorEnabled(false);
        }
        if (Lote.isEmpty()) {
            txtInputLotDegummed.setError("Ingrese su lote");
            retorno = false;
        } else {
            txtInputLotDegummed.setErrorEnabled(false);
        }
        if (TempTrabajo.isEmpty()) {
            txtInputWorkTemperatureDegummed.setError("Ingrese su Temp. Trabajo");
            retorno = false;
        } else {
            txtInputWorkTemperatureDegummed.setErrorEnabled(false);
        }
        if (TResidencia.isEmpty()) {
            txtInputHomeDegummed.setError("Ingrese su T. Residencia");
            retorno = false;
        } else {
            txtInputHomeDegummed.setErrorEnabled(false);
        }
        return retorno;
    }

    private void EditorTexto_desgomado() {

        txAutoCompleteReactor.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                txtInputReactorDegummed.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        edtTonelajeDesgomado.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                txtInputTonDegummed.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        edtAcidoFosforicoDesgomado.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                txtInputPhosphoricAcidDegummed.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        txAutoCompleteLote.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                txtInputLotDegummed.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        edtTempTrabajoDesgomado.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                txtInputWorkTemperatureDegummed.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        edtTResidenciaDesgomado.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                txtInputHomeDegummed.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

    }


    private void MetodoDelete(ClaseDesgomado item, int position) {
        ProgressDialog progressDialog = new ProgressDialog(getContext());
        SweetAlertDialog dialog = new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Alerta")
                .setContentText("Estás segura de que quieres eliminar este dato?")
                .setConfirmButtonTextColor(Color.parseColor("#000000"))
                .setCancelButtonTextColor(Color.parseColor("#000000"))
                .setConfirmButtonBackgroundColor(Color.parseColor("#FCD01D"))
                .setCancelButtonBackgroundColor(Color.parseColor("#FCD01D"))
                .setConfirmText("Sí, Borrar")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        ClaseDesgomado n = new ClaseDesgomado();
                        n.setId(item.getId());
                        n.setNumeroLote(item.getNumeroLote());
                        databaseReference.child("Desgomado").child(n.getNumeroLote()).child(n.getId()).removeValue();
                        Toast.makeText(getContext(), "Dato eliminado correctamente", LENGTH_SHORT).show();
                        sDialog.dismissWithAnimation();

                        // Mostrar el ProgressDialog en el UI thread
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                progressDialog.setMessage("Eliminando dato...");
                                progressDialog.setCancelable(false);
                                progressDialog.show();
                            }
                        });

                        // Ocultar el ProgressDialog en el UI thread después de un retraso
                        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                adapter.notifyItemRemoved(position);
                                progressDialog.dismiss();
                            }
                        }, 2000);

                    }
                })
                .setCancelButton("No, Borrar!", new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.dismissWithAnimation();
                        Toast.makeText(getContext(), "Dato no eliminado", LENGTH_SHORT).show();
                    }
                });

        dialog.show();

    }


    private void MetodoEditar(ClaseDesgomado item) {
        BottomSheetDialog sheetDialog = new BottomSheetDialog(getContext(), R.style.ThemeOverlay_Catalog_BottomSheetDialog_Scrollable);
        View view = LayoutInflater.from(getContext()).inflate(R.layout.update_popup_desgomado, null);
        sheetDialog.setContentView(view);


        txtInputReactorDegummed = sheetDialog.findViewById(R.id.txtInputReactorDegummed);
        txtInputTonDegummed = sheetDialog.findViewById(R.id.txtInputTonDegummed);
        txtInputPhosphoricAcidDegummed = sheetDialog.findViewById(R.id.txtInputPhosphoricAcidDegummed);
        txtInputLotDegummed = sheetDialog.findViewById(R.id.txtInputLotDegummed);
        txtInputWorkTemperatureDegummed = sheetDialog.findViewById(R.id.txtInputWorkTemperatureDegummed);
        txtInputHomeDegummed = sheetDialog.findViewById(R.id.txtInputHomeDegummed);

        //texview Desgomado
        txAutoCompleteReactor = sheetDialog.findViewById(R.id.edtReactorDegummed);
        edtTonelajeDesgomado = sheetDialog.findViewById(R.id.edtTonDegummed);
        edtAcidoFosforicoDesgomado = sheetDialog.findViewById(R.id.edtPhosphoricAcidDegummed);
        txAutoCompleteLote = sheetDialog.findViewById(R.id.edtLotDegummed);
        edtTempTrabajoDesgomado = sheetDialog.findViewById(R.id.edtWorkTemperatureDegummed);
        edtTResidenciaDesgomado = sheetDialog.findViewById(R.id.edtHomeDegummed);

        Button btn_guardar = sheetDialog.findViewById(R.id.btnRegistate);

        txAutoCompleteReactor.setText(item.getReactor());
        edtTonelajeDesgomado.setText(item.getTonelaje());
        edtAcidoFosforicoDesgomado.setText(item.getAcidoFosforico());
        txAutoCompleteLote.setText(item.getLoteacido());
        edtTempTrabajoDesgomado.setText(item.getTempTrabajo());
        edtTResidenciaDesgomado.setText(item.getTResidencia());

        allNamesReactor = Reactor();
        ArrayAdapter<String> autoCompleteLotNumber = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, allNamesReactor);
        txAutoCompleteReactor.setAdapter(autoCompleteLotNumber);

        databaseReference = FirebaseDatabase.getInstance().getReference();
        final ArrayAdapter<String> getNameClient = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1);
        databaseReference.child("LotesAcidoFosforico").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                getNameClient.clear();
                for (DataSnapshot areaSnapshot : dataSnapshot.getChildren()) {
                    String areaName = areaSnapshot.child("nroloteAcidoFosforico").getValue(String.class);
                    getNameClient.add(areaName);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        AutoCompleteTextView ACT = (AutoCompleteTextView) txAutoCompleteLote;
        ACT.setAdapter(getNameClient);


        btn_guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (txAutoCompleteReactor.getText().toString().trim().equalsIgnoreCase("")
                        || edtTonelajeDesgomado.getText().toString().trim().equalsIgnoreCase("")
                        || edtAcidoFosforicoDesgomado.getText().toString().trim().equalsIgnoreCase("")
                        || txAutoCompleteLote.getText().toString().trim().equalsIgnoreCase("")
                        || edtTempTrabajoDesgomado.getText().toString().trim().equalsIgnoreCase("")
                        || edtTResidenciaDesgomado.getText().toString().trim().equalsIgnoreCase("")) {
                    // VALIDAR DATOS
                    validar_desgomado();
                } else {
                    inserccion(item);
                    Toast.makeText(getContext(), "Dato editado", LENGTH_SHORT).show();
                    sheetDialog.dismiss();
                }
                EditorTexto_desgomado();

            }
        });

        sheetDialog.show();
    }

    private ArrayList<String> Reactor() {
        ArrayList<String> allNameReactor = new ArrayList<>();
        allNameReactor.add("1");
        allNameReactor.add("2");
        allNameReactor.add("3");
        return allNameReactor;
    }

    @Override
    public void onDataChanged() {

    }

    @Override
    public void DesgomadoMenuItem(int position, MenuItem menuItem, ClaseDesgomado item) {
    }

    @Override
    public void NeutralizadoMenuItem(int position, MenuItem menuItem, ClaseNeutralizado item) {

    }

    @Override
    public void BlanqueadoMenuItem(int position, MenuItem menuItem, ClaseBlanqueado item) {

    }
}