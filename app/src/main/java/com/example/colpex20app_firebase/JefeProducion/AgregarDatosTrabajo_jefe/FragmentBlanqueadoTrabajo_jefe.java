package com.example.colpex20app_firebase.JefeProducion.AgregarDatosTrabajo_jefe;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
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

import com.example.colpex20app_firebase.Adactadores.Adacter_JefeProducion.AdacterTrabajoJefe.BlanqueadoAdacter_Jefe;
import com.example.colpex20app_firebase.Model.RvItemClick;
import com.example.colpex20app_firebase.PrincipalClasses.ClassBlanqueado.ClaseBlanqueado;
import com.example.colpex20app_firebase.PrincipalClasses.ClassDesgomado.ClaseDesgomado;
import com.example.colpex20app_firebase.PrincipalClasses.ClassNeutralizado.ClaseNeutralizado;
import com.example.colpex20app_firebase.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.snackbar.Snackbar;
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

import cn.pedant.SweetAlert.SweetAlertDialog;


public class FragmentBlanqueadoTrabajo_jefe extends Fragment implements RvItemClick {
    // texview Blanqueado
    EditText edtCargaBlanqueado, edtVacioBlanqueado, edtTempTrabajoBlanqueado, edtArcillaBlanqueado,
            edtCarbonActivadoBlanqueado, edtSiliceBlanqueado, edtFFAFinalBlanqueado,
            edtColorFinalBlanqueado, edtAndisidinaFinalBlanqueado;

    // Inputlayout BLANQUEDO
    private TextInputLayout txtInputReactorBleached, txtInputLoadBleached, txtInputEmpty,
            txtInputWorkTemperatureBleached, txtInputClayBleached, txtInputLotClayBleached,
            txtInputActivatedCarbonBleached, txtInputLotActivatedCarbonBleached,
            txtInputSilicaBleached, txtInputLotSilicaBleached, txtInputFFAFinalBleached,
            txtInputColorFinalBleached, txtInputAnisidineFinalBleached;

    //Auto Complete Reactor
    AutoCompleteTextView txAutoCompleteReactor;
    ArrayList<String> allNamesReactor;

    //Auto Complete Lote Arcilla
    AutoCompleteTextView txAutoCompleteLoteArcilla;

    //Auto Complete Lote Carbon Activado
    AutoCompleteTextView txAutoCompleteLoteCarbActiva;

    //Auto Complete Lote Silice
    AutoCompleteTextView txAutoCompleteLoteSilice;

    //Metodo ArrayList Blanqueado
    BlanqueadoAdacter_Jefe adapter;
    ArrayList<ClaseBlanqueado> VersionList;
    RecyclerView recyclerView;

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

    //editar dato
    Button btn_guardar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_blanqueado_trabajo_jefe, container, false);
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
        //Refresh Actualizar
        Actualizacion(view);
        //FireBase
        IniciarFirebase();
        //ListadoDatos Lotes
        ListarDatosBlanqueado();
    }

    //Metodo_buscar Datos
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main_item_buscar_datos, menu);
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

    //Metodo_buscar Datos
    private void processBuscar(String s) {
        ArrayList<ClaseBlanqueado> milistas = new ArrayList<>();
        for (ClaseBlanqueado obj : VersionList) {
            if (obj.getNumeroLote().toLowerCase().contains(s.toLowerCase())) {
                milistas.add(obj);
            } else if (obj.getReactor().toLowerCase().contains(s.toLowerCase())) {
                milistas.add(obj);
            } else if (obj.getCarga().toLowerCase().contains(s.toLowerCase())) {
                milistas.add(obj);
            } else if (obj.getVacio().toLowerCase().contains(s.toLowerCase())) {
                milistas.add(obj);
            } else if (obj.getTempTrabajo().toLowerCase().contains(s.toLowerCase())) {
                milistas.add(obj);
            } else if (obj.getArcilla().toLowerCase().contains(s.toLowerCase())) {
                milistas.add(obj);
            } else if (obj.getALLote().toLowerCase().contains(s.toLowerCase())) {
                milistas.add(obj);
            } else if (obj.getCarbonActivado().toLowerCase().contains(s.toLowerCase())) {
                milistas.add(obj);
            } else if (obj.getCALote().toLowerCase().contains(s.toLowerCase())) {
                milistas.add(obj);
            } else if (obj.getSilice().toLowerCase().contains(s.toLowerCase())) {
                milistas.add(obj);
            } else if (obj.getSLote().toLowerCase().contains(s.toLowerCase())) {
                milistas.add(obj);
            } else if (obj.getFFAFinal().toLowerCase().contains(s.toLowerCase())) {
                milistas.add(obj);
            } else if (obj.getColorFinal().toLowerCase().contains(s.toLowerCase())) {
                milistas.add(obj);
            } else if (obj.getAndisidinaFinal().toLowerCase().contains(s.toLowerCase())) {
                milistas.add(obj);
            } else if (obj.getFechaHora().toLowerCase().contains(s.toLowerCase())) {
                milistas.add(obj);
            } else if (obj.getContadorBatch().toLowerCase().contains(s.toLowerCase())) {
                milistas.add(obj);
            } else if (obj.getUserName().toLowerCase().contains(s.toLowerCase())) {
                milistas.add(obj);
            }
        }
        BlanqueadoAdacter_Jefe adacter = new BlanqueadoAdacter_Jefe(milistas, this);
        recyclerView.setAdapter(adacter);
    }

    private void inserccion(ClaseBlanqueado claseBlanqueado, String reactor, String carga, String vacio, String tempTrabajo, String arcilla, String ALLote, String carbonActivado, String CALote, String silice, String SLote, String FFAFinal, String colorFinal, String andisidinaFinal) {

        ClaseBlanqueado b = new ClaseBlanqueado();
        b.setId(claseBlanqueado.getId());
        b.setNumeroLote(claseBlanqueado.getNumeroLote());
        b.setReactor(reactor);
        b.setCarga(carga);
        b.setVacio(vacio);
        b.setTempTrabajo(tempTrabajo);
        b.setArcilla(arcilla);
        b.setALLote(ALLote);
        b.setCarbonActivado(carbonActivado);
        b.setCALote(CALote);
        b.setSilice(silice);
        b.setSLote(SLote);
        b.setFFAFinal(FFAFinal);
        b.setColorFinal(colorFinal);
        b.setAndisidinaFinal(andisidinaFinal);
        b.setUserName(claseBlanqueado.getUserName());
        b.setImagenUrl(claseBlanqueado.getImagenUrl());
        b.setContadorBatch(claseBlanqueado.getContadorBatch());
        b.setFechaHora(claseBlanqueado.getFechaHora());
        databaseReference.child("Blanqueado").child(claseBlanqueado.getId()).setValue(b);

    }

    private void ListarDatosBlanqueado() {
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
            databaseReference.child("Blanqueado").addValueEventListener(new ValueEventListener() {
                @SuppressLint("NotifyDataSetChanged")
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    VersionList.clear();
                    progressBar.setVisibility(View.GONE);
                    imgenUp.setVisibility(View.VISIBLE);
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        ClaseBlanqueado blanqueado = dataSnapshot.getValue(ClaseBlanqueado.class);
                        //imagen para que no se vea
                        progressBar.setVisibility(View.GONE);
                        imgenUp.setVisibility(View.GONE);
                        VersionList.add(blanqueado);
                    }
                    adapter.notifyDataSetChanged();
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
        recyclerView = (RecyclerView) view.findViewById(R.id.lvBlanqueado);
        VersionList = new ArrayList<>();

        //liner layaout
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        recyclerView.setHasFixedSize(true);
        //int  numberOfColumns = 2;
        //recyclerView.setLayoutManager(new GridLayoutManager(this, numberOfColumns));
        //Adapter
        adapter = new BlanqueadoAdacter_Jefe(VersionList, this);
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
                ListarDatosBlanqueado();
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

    private boolean validar_blanqueado() {
        boolean retorno = true;
        String Reactor, Carga, Vacio, TempTrabajo, Arcilla, Lote1, CarbonActivado, Lote2,
                Silice, Lote3, FFAFinal, ColorFinal, AnisidinaFinal;

        Reactor = txAutoCompleteReactor.getText().toString();
        Carga = edtCargaBlanqueado.getText().toString();
        Vacio = edtVacioBlanqueado.getText().toString();
        TempTrabajo = edtTempTrabajoBlanqueado.getText().toString();
        Arcilla = edtArcillaBlanqueado.getText().toString();
        Lote1 = txAutoCompleteLoteArcilla.getText().toString();
        CarbonActivado = edtCarbonActivadoBlanqueado.getText().toString();
        Lote2 = txAutoCompleteLoteCarbActiva.getText().toString();
        Silice = edtSiliceBlanqueado.getText().toString();
        Lote3 = txAutoCompleteLoteSilice.getText().toString();
        FFAFinal = edtFFAFinalBlanqueado.getText().toString();
        ColorFinal = edtColorFinalBlanqueado.getText().toString();
        AnisidinaFinal = edtAndisidinaFinalBlanqueado.getText().toString();

        if (Reactor.isEmpty()) {
            txtInputReactorBleached.setError("Ingrese su Reactor");
            retorno = false;
        } else {
            txtInputReactorBleached.setErrorEnabled(false);
        }
        if (Carga.isEmpty()) {
            txtInputLoadBleached.setError("Ingrese su Carga");
            retorno = false;
        } else {
            txtInputLoadBleached.setErrorEnabled(false);
        }

        if (Vacio.isEmpty()) {
            txtInputEmpty.setError("Ingresar su Vacio");
            retorno = false;
        } else {
            txtInputEmpty.setErrorEnabled(false);
        }

        if (TempTrabajo.isEmpty()) {
            txtInputWorkTemperatureBleached.setError("Ingresar su Temp. Trabajo");
            retorno = false;
        } else {
            txtInputWorkTemperatureBleached.setErrorEnabled(false);
        }

        if (Arcilla.isEmpty()) {
            txtInputClayBleached.setError("Ingresar su Arcilla");
            retorno = false;
        } else {
            txtInputClayBleached.setErrorEnabled(false);
        }

        if (Lote1.isEmpty()) {
            txtInputLotClayBleached.setError("Ingrese su Lote");
            retorno = false;
        } else {
            txtInputLotClayBleached.setErrorEnabled(false);
        }

        if (CarbonActivado.isEmpty()) {
            txtInputActivatedCarbonBleached.setError("Ingresar su Carbon Activado");
            retorno = false;
        } else {
            txtInputActivatedCarbonBleached.setErrorEnabled(false);
        }

        if (Lote2.isEmpty()) {
            txtInputLotActivatedCarbonBleached.setError("Ingrese su Lote");
            retorno = false;
        } else {
            txtInputLotActivatedCarbonBleached.setErrorEnabled(false);
        }

        if (Silice.isEmpty()) {
            txtInputSilicaBleached.setError("Ingresar su Carbon Activado");
            retorno = false;
        } else {
            txtInputSilicaBleached.setErrorEnabled(false);
        }

        if (Lote3.isEmpty()) {
            txtInputLotSilicaBleached.setError("Ingrese su Lote");
            retorno = false;
        } else {
            txtInputLotSilicaBleached.setErrorEnabled(false);
        }
        if (FFAFinal.isEmpty()) {
            txtInputFFAFinalBleached.setError("Ingrese su FFA Final");
            retorno = false;
        } else {
            txtInputFFAFinalBleached.setErrorEnabled(false);
        }
        if (ColorFinal.isEmpty()) {
            txtInputColorFinalBleached.setError("Ingrese su Color Final");
            retorno = false;
        } else {
            txtInputColorFinalBleached.setErrorEnabled(false);
        }
        if (AnisidinaFinal.isEmpty()) {
            txtInputAnisidineFinalBleached.setError("Ingrese su Anisidina Final");
            retorno = false;
        } else {
            txtInputAnisidineFinalBleached.setErrorEnabled(false);
        }
        return retorno;
    }

    private void EditorTexto_blanqueado() {
        txAutoCompleteReactor.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                txtInputReactorBleached.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        edtCargaBlanqueado.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                txtInputLoadBleached.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        edtVacioBlanqueado.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                txtInputEmpty.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        edtTempTrabajoBlanqueado.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                txtInputWorkTemperatureBleached.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        edtArcillaBlanqueado.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                txtInputClayBleached.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        txAutoCompleteLoteArcilla.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                txtInputLotClayBleached.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        edtCarbonActivadoBlanqueado.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                txtInputActivatedCarbonBleached.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        txAutoCompleteLoteCarbActiva.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                txtInputLotActivatedCarbonBleached.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        edtSiliceBlanqueado.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                txtInputSilicaBleached.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        txAutoCompleteLoteSilice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                txtInputLotSilicaBleached.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        edtFFAFinalBlanqueado.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                txtInputFFAFinalBleached.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        edtColorFinalBlanqueado.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                txtInputColorFinalBleached.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        edtAndisidinaFinalBlanqueado.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                txtInputAnisidineFinalBleached.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }


    @Override
    public void DesgomadoMenuItem(int position, MenuItem menuItem, ClaseDesgomado item) {

    }

    @Override
    public void NeutralizadoMenuItem(int position, MenuItem menuItem, ClaseNeutralizado item) {

    }

    //botonos de editar y eliminar para el Recycleview
    @Override
    public void BlanqueadoMenuItem(int position, MenuItem menuItem, ClaseBlanqueado item) {
        switch (menuItem.getItemId()) {
            case R.id.edit:
              //  MetodoEditar(item);
                break;
            case R.id.delete:
              //  MetodoDelete(item);
                break;
        }
    }

    private void MetodoDelete(ClaseBlanqueado item) {
        new SweetAlertDialog(getContext(), SweetAlertDialog.ERROR_TYPE)
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
                        ClaseBlanqueado b = new ClaseBlanqueado();
                        b.setId(item.getId());
                        databaseReference.child("Blanqueado").child(b.getId()).removeValue();
                        Toast.makeText(getContext(), "Dato eliminado correctamente", Toast.LENGTH_SHORT).show();
                        sDialog.dismissWithAnimation();
                    }
                })
                .setCancelButton("No, Borrar!", new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.dismissWithAnimation();
                        Toast.makeText(getContext(), "Dato no eliminado", Toast.LENGTH_SHORT).show();
                    }
                })
                .show();
    }

    private void MetodoEditar(ClaseBlanqueado item) {
        BottomSheetDialog sheetDialog = new BottomSheetDialog(getContext());
        sheetDialog.setContentView(R.layout.update_popup_blanqueado);

        txtInputReactorBleached = sheetDialog.findViewById(R.id.txtInputReactorBleached);
        txtInputLoadBleached = sheetDialog.findViewById(R.id.txtInputLoadBleached);
        txtInputEmpty = sheetDialog.findViewById(R.id.txtInputEmpty);
        txtInputWorkTemperatureBleached = sheetDialog.findViewById(R.id.txtInputWorkTemperatureBleached);
        txtInputClayBleached = sheetDialog.findViewById(R.id.txtInputClayBleached);
        txtInputLotClayBleached = sheetDialog.findViewById(R.id.txtInputLotClayBleached);
        txtInputActivatedCarbonBleached = sheetDialog.findViewById(R.id.txtInputActivatedCarbonBleached);
        txtInputLotActivatedCarbonBleached = sheetDialog.findViewById(R.id.txtInputLotActivatedCarbonBleached);
        txtInputSilicaBleached = sheetDialog.findViewById(R.id.txtInputSilicaBleached);
        txtInputLotSilicaBleached = sheetDialog.findViewById(R.id.txtInputLotSilicaBleached);
        txtInputFFAFinalBleached = sheetDialog.findViewById(R.id.txtInputFFAFinalBleached);
        txtInputColorFinalBleached = sheetDialog.findViewById(R.id.txtInputColorFinalBleached);
        txtInputAnisidineFinalBleached = sheetDialog.findViewById(R.id.txtInputAnisidineFinalBleached);

        //AutoCompleteTextView
        // Reactor
        txAutoCompleteReactor = sheetDialog.findViewById(R.id.edtReactorBleached);
        // lote arcilla
        txAutoCompleteLoteArcilla = sheetDialog.findViewById(R.id.edtLotClayBleached);
        //lote carbon Activado
        txAutoCompleteLoteCarbActiva = sheetDialog.findViewById(R.id.edtLotActivatedCarbonBleached);
        //lote silice
        txAutoCompleteLoteSilice = sheetDialog.findViewById(R.id.edtLotSilicaBleached);

        //texview
        edtCargaBlanqueado = sheetDialog.findViewById(R.id.edtLoadBleached);
        edtVacioBlanqueado = sheetDialog.findViewById(R.id.edtEmptyBleached);
        edtTempTrabajoBlanqueado = sheetDialog.findViewById(R.id.edtWorkTemperatureBleached);
        edtArcillaBlanqueado = sheetDialog.findViewById(R.id.edtClayBleached);

        edtCarbonActivadoBlanqueado = sheetDialog.findViewById(R.id.edtActivatedCarbonBleached);
        edtSiliceBlanqueado = sheetDialog.findViewById(R.id.edtSilicaBleached);

        edtFFAFinalBlanqueado = sheetDialog.findViewById(R.id.edtFFAFinalBleached);
        edtColorFinalBlanqueado = sheetDialog.findViewById(R.id.edtColorFinalBleached);
        edtAndisidinaFinalBlanqueado = sheetDialog.findViewById(R.id.edtAnisidineFinalBleached);
        btn_guardar = sheetDialog.findViewById(R.id.btnRegistate);

        txAutoCompleteReactor.setText(item.getReactor());
        edtCargaBlanqueado.setText(item.getCarga());
        edtVacioBlanqueado.setText(item.getVacio());
        edtTempTrabajoBlanqueado.setText(item.getTempTrabajo());
        edtArcillaBlanqueado.setText(item.getArcilla());
        txAutoCompleteLoteArcilla.setText(item.getALLote());
        edtCarbonActivadoBlanqueado.setText(item.getCarbonActivado());
        txAutoCompleteLoteCarbActiva.setText(item.getCALote());
        edtSiliceBlanqueado.setText(item.getSilice());
        txAutoCompleteLoteSilice.setText(item.getSLote());
        edtFFAFinalBlanqueado.setText(item.getFFAFinal());
        edtColorFinalBlanqueado.setText(item.getColorFinal());
        edtAndisidinaFinalBlanqueado.setText(item.getAndisidinaFinal());


        allNamesReactor = (ArrayList<String>) Reactor();
        ArrayAdapter<String> autoCompleteLotNumber = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, allNamesReactor);
        txAutoCompleteReactor.setAdapter(autoCompleteLotNumber);

        databaseReference = FirebaseDatabase.getInstance().getReference();
        final ArrayAdapter<String> getNameArcilla = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1);
        databaseReference.child("LotesArcilla").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                getNameArcilla.clear();
                for (DataSnapshot areaSnapshot : dataSnapshot.getChildren()) {
                    String areaName = areaSnapshot.child("nroloteArcilla").getValue(String.class);
                    getNameArcilla.add(areaName);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        AutoCompleteTextView ACTV = (AutoCompleteTextView) txAutoCompleteLoteArcilla;
        ACTV.setAdapter(getNameArcilla);

        databaseReference = FirebaseDatabase.getInstance().getReference();
        final ArrayAdapter<String> getNameClient = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1);
        databaseReference.child("LotesCarbonActivado").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                getNameClient.clear();
                for (DataSnapshot areaSnapshot : dataSnapshot.getChildren()) {
                    String areaName = areaSnapshot.child("nroloteCarbonActivado").getValue(String.class);
                    getNameClient.add(areaName);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        AutoCompleteTextView ACT = (AutoCompleteTextView) txAutoCompleteLoteCarbActiva;
        ACT.setAdapter(getNameClient);

        databaseReference = FirebaseDatabase.getInstance().getReference();
        final ArrayAdapter<String> getNameProduct = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1);
        databaseReference.child("LotesSilice").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                getNameProduct.clear();
                for (DataSnapshot areaSnapshot : dataSnapshot.getChildren()) {
                    String areaName = areaSnapshot.child("nroloteSillice").getValue(String.class);
                    getNameProduct.add(areaName);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        AutoCompleteTextView AC = (AutoCompleteTextView) txAutoCompleteLoteSilice;
        AC.setAdapter(getNameProduct);

        btn_guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Reactor = txAutoCompleteReactor.getText().toString();
                String Carga = edtCargaBlanqueado.getText().toString();
                String Vacio = edtVacioBlanqueado.getText().toString();
                String TempTrabajo = edtTempTrabajoBlanqueado.getText().toString();
                String Arcilla = edtArcillaBlanqueado.getText().toString();
                String ALLote = txAutoCompleteLoteArcilla.getText().toString();
                String CarbonActivado = edtCarbonActivadoBlanqueado.getText().toString();
                String CALote = txAutoCompleteLoteCarbActiva.getText().toString();
                String Silice = edtSiliceBlanqueado.getText().toString();
                String SLote = txAutoCompleteLoteSilice.getText().toString();
                String FFAFinal = edtFFAFinalBlanqueado.getText().toString();
                String ColorFinal = edtColorFinalBlanqueado.getText().toString();
                String AndisidinaFinal = edtAndisidinaFinalBlanqueado.getText().toString();

                if (Reactor.isEmpty() || Carga.isEmpty() || Vacio.isEmpty() || TempTrabajo.isEmpty()
                        || Arcilla.isEmpty() || ALLote.isEmpty() || CarbonActivado.isEmpty()
                        || CALote.isEmpty() || Silice.isEmpty() || SLote.isEmpty()
                        || FFAFinal.isEmpty() || ColorFinal.isEmpty() || AndisidinaFinal.isEmpty()) {
                    // VALIDAR DATOS
                    validar_blanqueado();
                    ShowSnackbar("Verifique todos los campos");

                } else {

                    inserccion(item, Reactor, Carga, Vacio, TempTrabajo, Arcilla,
                            ALLote, CarbonActivado, CALote, Silice, SLote, FFAFinal, ColorFinal, AndisidinaFinal);
                    Toast.makeText(getContext(), "Dato editado", Toast.LENGTH_SHORT).show();
                    sheetDialog.dismiss();
                }
                EditorTexto_blanqueado();

            }
        });

        sheetDialog.show();

    }

    private Object Reactor() {
        ArrayList<String> allNameReactor = new ArrayList<>();
        allNameReactor.add("1");
        allNameReactor.add("2");
        allNameReactor.add("3");
        return allNameReactor;
    }

    private void ShowSnackbar(String s) {
        Snackbar snackbar = Snackbar.make(btn_guardar, s, Snackbar.LENGTH_SHORT);
        snackbar.setTextColor(Color.parseColor("#ffffff"));
        snackbar.setBackgroundTint(Color.parseColor("#cc3c3c"));
        snackbar.show();
    }

    private void Showverificado(String s) {
        Snackbar snackbar = Snackbar.make(btn_guardar, s, Snackbar.LENGTH_SHORT);
        snackbar.show();
    }
}