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

import com.example.colpex20app_firebase.Adactadores.AdacterNeutralizado.ItemAdacterNeutralizado;
import com.example.colpex20app_firebase.Model.RvItemClick;
import com.example.colpex20app_firebase.PrincipalClasses.ClassBlanqueado.ClaseBlanqueado;
import com.example.colpex20app_firebase.PrincipalClasses.ClassDesgomado.ClaseDesgomado;
import com.example.colpex20app_firebase.PrincipalClasses.ClassNeutralizado.ClaseNeutralizado;
import com.example.colpex20app_firebase.PrincipalClasses.ClassNeutralizado.ItemNeutralizado;
import com.example.colpex20app_firebase.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;


public class FragmentNeutralizadoTrabajo extends Fragment implements RvItemClick {
    // texview Neutalizado
    private EditText edtFFAInicialNeutralizado, edtTempTrabajoNeutralizado, edtConcentracionNaOHNeutralizado,
            edtAceiteNeutroNeutralizado;

    // Inputlayout Neutralizado
    private TextInputLayout txtInputReactorNeutralized, txtInputFFAInitialNeutralized,
            txtInputWorkTemperatureNeutralized, txtInputNaOHNeutralized,
            txtInputLotNaOHNeutralized, txtInputNeutralOilNeutralized;
    //
    RecyclerView recyclerView;
    //Item de Lote
    private List<ItemNeutralizado> itemList = new ArrayList<>();
    ItemAdacterNeutralizado adapter;
    //Contenido
    ArrayList<ClaseNeutralizado> subItemListNeutralizado = new ArrayList<>();
    ;

    //mosdar no hay Internet
    private LinearLayout imgenUpWifi;
    private Button btnconexionWifi;

    //conexion Firebase
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    private FirebaseAuth mAuth;
    FirebaseUser currentUser;
    //quitar la imagen de fondo
    private LinearLayout imgenUp;
    //Actualizar
    SwipeRefreshLayout sr;
    //Progresor bar
    private ProgressBar progressBar;

    //Auto Complete
    AutoCompleteTextView txAutoCompleteReactor;
    ArrayList<String> allNamesReactor;
    //Auto Complete Lote
    AutoCompleteTextView txAutoCompleteLote;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_neutralizado_trabajo, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //imagen para que no se vea
        initViewModel(view);
        //
        DatosItemNeutralizado();
        //Mensaje de no hay Imagen
        UPWifi(view);
        //metodo
        InicioRecyclerview(view);
        //Refresh Actualizar
        Actualizacion(view);
        //FireBase
        IniciarFirebase();
        //ListadoDatos Lotes
        ListarDatosNeutralizado();
    }
    private void DatosItemNeutralizado() {
        adapter = new ItemAdacterNeutralizado(itemList, this) {
            @Override
            public void onDataChanged() {
                if (progressBar != null) {
                    progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void DesgomadoMenuItem(int position, MenuItem menuItem, ClaseDesgomado item) {

            }

            @Override
            public void NeutralizadoMenuItem(int position, MenuItem menuItem, ClaseNeutralizado item) {
                switch (menuItem.getItemId()) {
                    case R.id.edit:
                        MetodoEditar(item);
                        break;
                    case R.id.delete:
                        MetodoDelete(item, position);
                        break;
                }
            }

            @Override
            public void BlanqueadoMenuItem(int position, MenuItem menuItem, ClaseBlanqueado item) {

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
                    DatosItemNeutralizado();
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }
                return true;
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }

    //Metodo_buscar Datos
    @SuppressLint("NotifyDataSetChanged")
    private void filterData(String query) {
        List<ItemNeutralizado> filteredItemList = new ArrayList<>();
        for (ItemNeutralizado item : itemList) {
            String itemTitleLote = item.getItemTitleLote().toLowerCase();
            if (itemTitleLote.contains(query.toLowerCase())) {
                filteredItemList.add(item);
            } else {
                List<ClaseNeutralizado> subItemListNeutralizado = item.getSubItemListNeutralizado();
                List<ClaseNeutralizado> filteredSubItemList = new ArrayList<>();
                for (ClaseNeutralizado dato : subItemListNeutralizado) {
                    String searchValue = query.toLowerCase();
                    if (dato.getNumeroLote().toLowerCase().contains(searchValue) ||
                            dato.getReactor().toLowerCase().contains(searchValue) ||
                            dato.getContadorBatch().toLowerCase().contains(searchValue) ||
                            dato.getFFAInicial().toLowerCase().contains(searchValue) ||
                            dato.getTempTrabajo().toLowerCase().contains(searchValue) ||
                            dato.getConcentracionNaOH().toLowerCase().contains(searchValue) ||
                            dato.getLoteNAOH().toLowerCase().contains(searchValue) ||
                            dato.getAceiteNeutro().toLowerCase().contains(searchValue) ||
                            dato.getUserName().toLowerCase().contains(searchValue) ||
                            dato.getFechaHora().toLowerCase().contains(searchValue)) {
                        filteredSubItemList.add(dato);
                    }
                }
                if (!filteredSubItemList.isEmpty()) {
                    ItemNeutralizado filteredItem = new ItemNeutralizado(itemTitleLote, filteredSubItemList);
                    filteredItemList.add(filteredItem);
                }
            }
        }
        ItemAdacterNeutralizado adapter = new ItemAdacterNeutralizado(filteredItemList, this) {
            @Override
            public void onDataChanged() {
                if (progressBar != null) {
                    progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void DesgomadoMenuItem(int position, MenuItem menuItem, ClaseDesgomado item) {
            }

            @SuppressLint("NonConstantResourceId")
            @Override
            public void NeutralizadoMenuItem(int position, MenuItem menuItem, ClaseNeutralizado item) {
                switch (menuItem.getItemId()) {
                    case R.id.edit:
                        MetodoEditar(item);
                        break;
                    case R.id.delete:
                        MetodoDelete(item, position);
                        break;
                }
            }

            @Override
            public void BlanqueadoMenuItem(int position, MenuItem menuItem, ClaseBlanqueado item) {
            }
        };
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private void inserccion(ClaseNeutralizado claseNeutralizado) {
        //datos de Neutralizado
        String Reactor = txAutoCompleteReactor.getText().toString();
        String FFAInicial = edtFFAInicialNeutralizado.getText().toString();
        String TempTrabajo = edtTempTrabajoNeutralizado.getText().toString();
        String LoteNaOH = txAutoCompleteLote.getText().toString();
        String AceiteNeutro = edtAceiteNeutroNeutralizado.getText().toString();
        //consentacion NaoH
        String concentracionNaOH;
        String concentracion = edtConcentracionNaOHNeutralizado.getText().toString();
        try {
            int valorConcentracion = Integer.parseInt(concentracion);
            if (valorConcentracion >= 20) {
                concentracionNaOH = " °Be";
            } else if (valorConcentracion >= 14) {
                concentracionNaOH = " %";
            } else {
                concentracionNaOH = " Concentración fuera de rango";
            }
        } catch (NumberFormatException e) {
            // Manejar el caso en que la entrada no es un número
            concentracionNaOH = " Entrada no válida";
        }

        ClaseNeutralizado n = new ClaseNeutralizado();
        n.setId(claseNeutralizado.getId());
        n.setNumeroLote(claseNeutralizado.getNumeroLote());
        n.setReactor(Reactor);
        n.setContadorBatch(claseNeutralizado.getContadorBatch());
        n.setFFAInicial(FFAInicial);
        n.setTempTrabajo(TempTrabajo);
        n.setConcentracionNaOH(concentracion);
        n.setPorsentaje(concentracionNaOH);
        n.setLoteNAOH(LoteNaOH);
        n.setAceiteNeutro(AceiteNeutro);
        n.setUserName(claseNeutralizado.getUserName());
        n.setImagenUrl(claseNeutralizado.getImagenUrl());
        n.setFechaHora(claseNeutralizado.getFechaHora());
        databaseReference.child("Neutralizado").child(claseNeutralizado.getNumeroLote()).child(claseNeutralizado.getId()).setValue(n);
    }

    private void ListarDatosNeutralizado() {
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
            databaseReference.child("Neutralizado").addValueEventListener(new ValueEventListener() {
                @SuppressLint("NotifyDataSetChanged")
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    itemList.clear();
                    progressBar.setVisibility(View.GONE);
                    imgenUp.setVisibility(View.VISIBLE);
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        String itemTitleLote = dataSnapshot.getKey();
                        subItemListNeutralizado = new ArrayList<>();
                        for (DataSnapshot subSnapshot : dataSnapshot.getChildren()) {
                            ClaseNeutralizado dato = subSnapshot.getValue(ClaseNeutralizado.class);
                            subItemListNeutralizado.add(dato);
                        }
                        ItemNeutralizado item = new ItemNeutralizado(itemTitleLote, subItemListNeutralizado);
                        progressBar.setVisibility(View.GONE);
                        imgenUp.setVisibility(View.GONE);
                        itemList.add(item);
                    }
                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(getContext(), error.getMessage(), LENGTH_SHORT).show();
                }
            });
        }
    }

    private void initViewModel(View view) {
        progressBar = (ProgressBar) view.findViewById(R.id.progress_bar);
    }

    private void InicioRecyclerview(View view) {
        // RECYCLER VIEW
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
                InicioRecyclerview(view);
                ListarDatosNeutralizado();
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

    private boolean validar_neutralizado() {
        boolean retorno = true;
        String Reactor, Batch, FFAInicial, TempTrabajo, ConcentracionNaOH, LoteNaOH, AceiteNeutro;

        Reactor = txAutoCompleteReactor.getText().toString();
        FFAInicial = edtFFAInicialNeutralizado.getText().toString();
        TempTrabajo = edtTempTrabajoNeutralizado.getText().toString();
        ConcentracionNaOH = edtConcentracionNaOHNeutralizado.getText().toString();
        LoteNaOH = txAutoCompleteLote.getText().toString();
        AceiteNeutro = edtAceiteNeutroNeutralizado.getText().toString();

        if (Reactor.isEmpty()) {
            txtInputReactorNeutralized.setError("Ingrese su Reactor");
            retorno = false;
        } else {
            txtInputReactorNeutralized.setErrorEnabled(false);
        }
        if (FFAInicial.isEmpty()) {
            txtInputFFAInitialNeutralized.setError("Ingrese su FFA Inicial");
            retorno = false;
        } else {
            txtInputFFAInitialNeutralized.setErrorEnabled(false);
        }
        if (TempTrabajo.isEmpty()) {
            txtInputWorkTemperatureNeutralized.setError("Ingresar su Concentracion NaOH");
            retorno = false;
        } else if (Double.parseDouble(TempTrabajo) > 65) {
            txtInputWorkTemperatureNeutralized.setError("Fuera de Rango (Max. 65 º C)");
            retorno = false;
        } else {
            txtInputWorkTemperatureNeutralized.setErrorEnabled(false);
        }
        if (ConcentracionNaOH.isEmpty()) {
            txtInputNaOHNeutralized.setError("Ingresar su Temp. Trabajo");
            retorno = false;
        } else if (Integer.parseInt(ConcentracionNaOH) < 14 || Integer.parseInt(ConcentracionNaOH) > 20) {
            txtInputNaOHNeutralized.setError("Fuera de Rango (min 14% o 20º Be)");
            retorno = false;
        } else {
            txtInputNaOHNeutralized.setErrorEnabled(false);
        }

        if (LoteNaOH.isEmpty()) {
            txtInputLotNaOHNeutralized.setError("Ingrese su Lote NaOH");
            retorno = false;
        } else {
            txtInputLotNaOHNeutralized.setErrorEnabled(false);
        }
        if (AceiteNeutro.isEmpty()) {
            txtInputNeutralOilNeutralized.setError("Ingrese su Aceite Neutro");
            retorno = false;
        } else {
            txtInputNeutralOilNeutralized.setErrorEnabled(false);
        }
        return retorno;
    }

    private void EditorTexto_neutralizado() {

        txAutoCompleteReactor.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                txtInputReactorNeutralized.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        edtFFAInicialNeutralizado.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                txtInputFFAInitialNeutralized.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        edtTempTrabajoNeutralizado.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                txtInputWorkTemperatureNeutralized.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        edtConcentracionNaOHNeutralizado.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                txtInputNaOHNeutralized.setErrorEnabled(false);
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
                txtInputLotNaOHNeutralized.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        edtAceiteNeutroNeutralizado.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                txtInputNeutralOilNeutralized.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private void MetodoDelete(ClaseNeutralizado item, int position) {
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
                        ClaseNeutralizado n = new ClaseNeutralizado();
                        n.setId(item.getId());
                        n.setNumeroLote(item.getNumeroLote());
                        databaseReference.child("Neutralizado").child(n.getNumeroLote()).child(n.getId()).removeValue();
                        Toast.makeText(getContext(), "Dato eliminado correctamente", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(getContext(), "Dato no eliminado", Toast.LENGTH_SHORT).show();
                    }
                });
        dialog.show();
    }

    private void MetodoEditar(ClaseNeutralizado item) {
        BottomSheetDialog sheetDialog = new BottomSheetDialog(getContext());
        sheetDialog.setContentView(R.layout.update_popup_neutralizado);
        // sheetDialog.setCanceledOnTouchOutside(false);

        txtInputReactorNeutralized = sheetDialog.findViewById(R.id.txtInputReactorNeutralized);
        txtInputFFAInitialNeutralized = sheetDialog.findViewById(R.id.txtInputFFAInitialNeutralized);
        txtInputWorkTemperatureNeutralized = sheetDialog.findViewById(R.id.txtInputWorkTemperatureNeutralized);
        txtInputNaOHNeutralized = sheetDialog.findViewById(R.id.txtInputNaOHNeutralized);
        txtInputLotNaOHNeutralized = sheetDialog.findViewById(R.id.txtInputLotNaOHNeutralized);
        txtInputNeutralOilNeutralized = sheetDialog.findViewById(R.id.txtInputNeutralOilNeutralized);


        txAutoCompleteReactor = sheetDialog.findViewById(R.id.edtReactorNeutralized);
        edtFFAInicialNeutralizado = sheetDialog.findViewById(R.id.edtFFAInitialNeutralized);
        edtTempTrabajoNeutralizado = sheetDialog.findViewById(R.id.edtWorkTemperatureNeutralized);
        edtConcentracionNaOHNeutralizado = sheetDialog.findViewById(R.id.edtNaOHNeutralized);
        txAutoCompleteLote = sheetDialog.findViewById(R.id.edtLotNaOHNeutralized);
        edtAceiteNeutroNeutralizado = sheetDialog.findViewById(R.id.edtNeutralOilNeutralized);
        Button btn_guardar = sheetDialog.findViewById(R.id.btnRegistate);

        txAutoCompleteReactor.setText(item.getReactor());
        edtFFAInicialNeutralizado.setText(item.getFFAInicial());
        edtTempTrabajoNeutralizado.setText(item.getTempTrabajo());
        edtConcentracionNaOHNeutralizado.setText(item.getConcentracionNaOH());
        txAutoCompleteLote.setText(item.getLoteNAOH());
        edtAceiteNeutroNeutralizado.setText(item.getAceiteNeutro());

        allNamesReactor = Reactor();
        ArrayAdapter<String> autoCompleteLotNumber = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, allNamesReactor);
        txAutoCompleteReactor.setAdapter(autoCompleteLotNumber);

        databaseReference = FirebaseDatabase.getInstance().getReference();
        final ArrayAdapter<String> getNameNaOH = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1);
        databaseReference.child("LotesNaOH").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                getNameNaOH.clear();
                //Agregardato.setEnabled(true);
                for (DataSnapshot areaSnapshot : dataSnapshot.getChildren()) {
                    String areaName = areaSnapshot.child("nroloteNaOH").getValue(String.class);
                    getNameNaOH.add(areaName);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        AutoCompleteTextView ACTV = (AutoCompleteTextView) txAutoCompleteLote;
        ACTV.setAdapter(getNameNaOH);

        btn_guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String TempTrabajo = edtTempTrabajoNeutralizado.getText().toString();
                String ConcentracionNaOH = edtConcentracionNaOHNeutralizado.getText().toString();

                if (txAutoCompleteReactor.getText().toString().trim().equalsIgnoreCase("")
                        || edtFFAInicialNeutralizado.getText().toString().trim().equalsIgnoreCase("")
                        || edtTempTrabajoNeutralizado.getText().toString().trim().equalsIgnoreCase("")
                        || edtConcentracionNaOHNeutralizado.getText().toString().trim().equalsIgnoreCase("")
                        || txAutoCompleteLote.getText().toString().trim().equalsIgnoreCase("")
                        || edtAceiteNeutroNeutralizado.getText().toString().trim().equalsIgnoreCase("")) {
                    // VALIDAR DATOS
                    validar_neutralizado();
                } else if (Integer.parseInt(ConcentracionNaOH) < 14 || Integer.parseInt(ConcentracionNaOH) > 20 || Double.parseDouble(TempTrabajo) > 65) {
                    validar_neutralizado();
                    AlertDialogRangos(item, sheetDialog);
                } else if (Integer.parseInt(ConcentracionNaOH) < 14 || Integer.parseInt(ConcentracionNaOH) > 20) {
                    //ventana de avisomalo
                    validar_neutralizado();

                } else if (Double.parseDouble(TempTrabajo) > 65) {
                    //ventana de avisomalo
                    validar_neutralizado();
                } else {
                    inserccion(item);
                    Toast.makeText(getContext(), "Dato editado", Toast.LENGTH_SHORT).show();
                    sheetDialog.dismiss();
                }
                EditorTexto_neutralizado();

            }
        });

        sheetDialog.show();
    }

    private void AlertDialogRangos(ClaseNeutralizado item, BottomSheetDialog sheetDialog) {
        new SweetAlertDialog(this.getContext(), SweetAlertDialog.CUSTOM_IMAGE_TYPE)
                .setCustomImage(com.microsoft.fluent.mobile.icons.R.drawable.ic_fluent_question_circle_24_regular)
                .setTitleText("Aviso")
                .setContentText("¡Fuera de Rango! ... ¿Estás seguro de continuar?")
                .setCancelText("No")
                .setConfirmButtonTextColor(Color.parseColor("#000000"))
                .setCancelButtonTextColor(Color.parseColor("#000000"))
                .setConfirmButtonBackgroundColor(Color.parseColor("#FCD01D"))
                .setCancelButtonBackgroundColor(Color.parseColor("#FCD01D"))
                .setConfirmText("Sí")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        validar_neutralizado();
                        inserccion(item);
                        Toast.makeText(getContext(), "Dato editado", Toast.LENGTH_SHORT).show();
                        sheetDialog.dismiss();
                        sDialog.dismissWithAnimation();
                    }
                })
                .showCancelButton(true)
                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.cancel();
                    }
                })
                .show();
        EditorTexto_neutralizado();
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
    public void DesgomadoMenuItem(int position, MenuItem menuItem, com.example.colpex20app_firebase.PrincipalClasses.ClassDesgomado.ClaseDesgomado item) {

    }

    @Override
    public void NeutralizadoMenuItem(int position, MenuItem menuItem, com.example.colpex20app_firebase.PrincipalClasses.ClassNeutralizado.ClaseNeutralizado item) {

    }

    @Override
    public void BlanqueadoMenuItem(int position, MenuItem menuItem, ClaseBlanqueado item) {

    }
}