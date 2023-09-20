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

import com.example.colpex20app_firebase.Adactadores.Adacter_JefeProducion.AdacterTrabajoJefe.NeutralizadoAdacter_jefe;
import com.example.colpex20app_firebase.Model.RvItemClick;
import com.example.colpex20app_firebase.PrincipalClasses.ClassBlanqueado.ClaseBlanqueado;
import com.example.colpex20app_firebase.PrincipalClasses.ClassDesgomado.ClaseDesgomado;
import com.example.colpex20app_firebase.PrincipalClasses.ClassNeutralizado.ClaseNeutralizado;
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

import cn.pedant.SweetAlert.SweetAlertDialog;


public class FragmentNeutralizadoTrabajo_jefe extends Fragment implements RvItemClick {
    // texview Neutalizado
    private EditText edtFFAInicialNeutralizado, edtTempTrabajoNeutralizado, edtConcentracionNaOHNeutralizado,
            edtAceiteNeutroNeutralizado;

    // Inputlayout Neutralizado
    private TextInputLayout txtInputReactorNeutralized, txtInputFFAInitialNeutralized,
            txtInputWorkTemperatureNeutralized, txtInputNaOHNeutralized,
            txtInputLotNaOHNeutralized, txtInputNeutralOilNeutralized;

    //Metodo ArrayList Blanqueado
    NeutralizadoAdacter_jefe adapter;
    ArrayList<ClaseNeutralizado> VersionList;
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

    //Auto Complete
    AutoCompleteTextView txAutoCompleteReactor;
    ArrayList<String> allNamesReactor;
    //Auto Complete Lote
    AutoCompleteTextView txAutoCompleteLote;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_neutralizado_trabajo_jefe, container, false);
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
        ListarDatosNeutralizado();
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
        ArrayList<ClaseNeutralizado> milistas = new ArrayList<>();
        for (ClaseNeutralizado obj : VersionList) {
            if (obj.getNumeroLote().toLowerCase().contains(s.toLowerCase())) {
                milistas.add(obj);
            } else if (obj.getReactor().toLowerCase().contains(s.toLowerCase())) {
                milistas.add(obj);
            } else if (obj.getContadorBatch().toLowerCase().contains(s.toLowerCase())) {
                milistas.add(obj);
            } else if (obj.getFFAInicial().toLowerCase().contains(s.toLowerCase())) {
                milistas.add(obj);
            } else if (obj.getTempTrabajo().toLowerCase().contains(s.toLowerCase())) {
                milistas.add(obj);
            } else if (obj.getConcentracionNaOH().toLowerCase().contains(s.toLowerCase())) {
                milistas.add(obj);
            } else if (obj.getLoteNAOH().toLowerCase().contains(s.toLowerCase())) {
                milistas.add(obj);
            } else if (obj.getAceiteNeutro().toLowerCase().contains(s.toLowerCase())) {
                milistas.add(obj);
            } else if (obj.getUserName().toLowerCase().contains(s.toLowerCase())) {
                milistas.add(obj);
            } else if (obj.getFechaHora().toLowerCase().contains(s.toLowerCase())) {
                milistas.add(obj);
            }
        }
        NeutralizadoAdacter_jefe adacter = new NeutralizadoAdacter_jefe(milistas, this);
        recyclerView.setAdapter(adacter);
    }


    private void inserccion(ClaseNeutralizado claseNeutralizado) {
        //datos de Neutralizado
        String Reactor = txAutoCompleteReactor.getText().toString();
        String FFAInicial = edtFFAInicialNeutralizado.getText().toString();
        String TempTrabajo = edtTempTrabajoNeutralizado.getText().toString();
        String LoteNaOH = txAutoCompleteLote.getText().toString();
        String AceiteNeutro = edtAceiteNeutroNeutralizado.getText().toString();
        //consentacion NaoH
        String concentracionNaOH = "";
        String ConcentracionNaOH = edtConcentracionNaOHNeutralizado.getText().toString();
        if (Integer.parseInt(ConcentracionNaOH) >= 14) {
            concentracionNaOH = " %";
        } else if (Integer.parseInt(ConcentracionNaOH) < 14) {
            concentracionNaOH = " %";
        } else if (Integer.parseInt(ConcentracionNaOH) == 20) {
            concentracionNaOH = " °Be";
        }else if (Integer.parseInt(ConcentracionNaOH) > 20){
            concentracionNaOH = " °Be";
        }
        ClaseNeutralizado n = new ClaseNeutralizado();
        n.setId(claseNeutralizado.getId());
        n.setNumeroLote(claseNeutralizado.getNumeroLote());
        n.setReactor(Reactor);
        n.setContadorBatch(claseNeutralizado.getContadorBatch());
        n.setFFAInicial(FFAInicial);
        n.setTempTrabajo(TempTrabajo);
        n.setConcentracionNaOH(ConcentracionNaOH);
        n.setLoteNAOH(LoteNaOH);
        n.setAceiteNeutro(AceiteNeutro);
        n.setUserName(claseNeutralizado.getUserName());
        n.setImagenUrl(claseNeutralizado.getImagenUrl());
        n.setFechaHora(claseNeutralizado.getFechaHora());
        databaseReference.child("Neutralizado").child(claseNeutralizado.getId()).setValue(n);
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
                    VersionList.clear();
                    progressBar.setVisibility(View.GONE);
                    imgenUp.setVisibility(View.VISIBLE);
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        ClaseNeutralizado neutralizado = dataSnapshot.getValue(ClaseNeutralizado.class);
                        //imagen para que no se vea
                        progressBar.setVisibility(View.GONE);
                        imgenUp.setVisibility(View.GONE);
                        VersionList.add(neutralizado);

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
        recyclerView = (RecyclerView) view.findViewById(R.id.lvDesgomado);
        VersionList = new ArrayList<>();
        //liner layaout
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        recyclerView.setHasFixedSize(true);

        adapter = new NeutralizadoAdacter_jefe(VersionList, this);
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
        String Reactor, FFAInicial, TempTrabajo, ConcentracionNaOH, LoteNaOH, AceiteNeutro;
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
            txtInputWorkTemperatureNeutralized.setError("Ingrese su Temp. Trabajo");
            retorno = false;
        } else {
            txtInputWorkTemperatureNeutralized.setErrorEnabled(false);
        }
        if (ConcentracionNaOH.isEmpty()) {
            txtInputNaOHNeutralized.setError("Ingrese su Concentracion NaOH");
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

    @Override
    public void DesgomadoMenuItem(int position, MenuItem menuItem, ClaseDesgomado item) {

    }

    //botonos de editar y eliminar para el Recycleview
    @Override
    public void NeutralizadoMenuItem(int position, MenuItem menuItem, com.example.colpex20app_firebase.PrincipalClasses.ClassNeutralizado.ClaseNeutralizado item) {
        switch (menuItem.getItemId()) {
            case R.id.edit:
                //MetodoEditar(item);
                break;
            case R.id.delete:
              //  MetodoDelete(item);
                break;
        }
    }

    @Override
    public void BlanqueadoMenuItem(int position, MenuItem menuItem, ClaseBlanqueado item) {

    }

    private void MetodoDelete(ClaseNeutralizado item) {
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
                        ClaseNeutralizado n = new ClaseNeutralizado();
                        n.setId(item.getId());
                        databaseReference.child("Neutralizado").child(n.getId()).removeValue();
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
                if (txAutoCompleteReactor.getText().toString().trim().equalsIgnoreCase("")
                        || edtFFAInicialNeutralizado.getText().toString().trim().equalsIgnoreCase("")
                        || edtTempTrabajoNeutralizado.getText().toString().trim().equalsIgnoreCase("")
                        || edtConcentracionNaOHNeutralizado.getText().toString().trim().equalsIgnoreCase("")
                        || txAutoCompleteLote.getText().toString().trim().equalsIgnoreCase("")
                        || edtAceiteNeutroNeutralizado.getText().toString().trim().equalsIgnoreCase("")) {
                    // VALIDAR DATOS
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

    private ArrayList<String> Reactor() {
        ArrayList<String> allNameReactor = new ArrayList<>();
        allNameReactor.add("1");
        allNameReactor.add("2");
        allNameReactor.add("3");
        return allNameReactor;
    }
}