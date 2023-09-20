package com.example.colpex20app_firebase.OperadorResponsable.AgregarLoteBlanqueado;

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
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;


import com.example.colpex20app_firebase.Adactadores.Adacter_Operador.AdacterLote.LoteArcillaAdacter;
import com.example.colpex20app_firebase.Model.ObservacionItem;
import com.example.colpex20app_firebase.PrincipalClasses.ClaseLote.LoteArcillaClass;
import com.example.colpex20app_firebase.R;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class FragmentLoteArcilla extends Fragment implements ObservacionItem {
    //AlertDialog
    private MaterialAlertDialogBuilder dialogBuilder;
    private AlertDialog dialog;
    //EditText
    private EditText edtNroLote;
    //Buttom
    private Button btnGenerar;
    //botones InputNombre
    private TextInputLayout txtInputLotNumber;
    //Metodo ArrayList
    LoteArcillaAdacter Adapter;
    ArrayList<LoteArcillaClass> VersionNLote;
    RecyclerView recyclerView;
    //conexion Firebase
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    //quitar la imagen
    private LinearLayout imgenUp;
    //mosdar no hay Internet
    private LinearLayout imgenUpWifi;
    private Button btnconexionWifi;
    //Actualizar
    SwipeRefreshLayout sr;
    //booton Agregar
    FloatingActionButton fab;
    //Progresor bar
    private ProgressBar progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_lote_arcilla, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //Mensaje de no hay Imagen
        UPWifi(view);
        // FireBaseAutetification
        IniciarFirebaseAutification();
        //Ingresar Datos
        openDialog(view);
        //Refresh Actualizar
        Actualizacion(view);
        //FireBase
        IniciarFirebase();
        //ListadoDatos Lotes
        ListarDatos();
        //Iniciar recicleview
        InicioRecyclerview(view);
        //botonos de editar y eliminar para el Recycleview
        BottonesAdapter();
    }

    private void IniciarFirebaseAutification() {
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
    }

    //Wifi_variables
    private void UPWifi(View view) {
        progressBar = view.findViewById(R.id.progress_bar);
        imgenUp = view.findViewById(R.id.imgenUp);
        imgenUpWifi = view.findViewById(R.id.imgenUpWifi);
        btnconexionWifi = view.findViewById(R.id.btnconexionWifi);
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

    //Metodo_buscar Datos
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main_item_buscar, menu);
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
        ArrayList<LoteArcillaClass> milista = new ArrayList<>();
        for (LoteArcillaClass obj : VersionNLote) {
            if (obj.getNroloteArcilla().toLowerCase().contains(s.toLowerCase())) {
                milista.add(obj);
            } else if (obj.getFechayhora().toLowerCase().contains(s.toLowerCase())) {
                milista.add(obj);
            }
        }
        LoteArcillaAdacter adacter = new LoteArcillaAdacter(milista);
        recyclerView.setAdapter(adacter);

        adacter.setmListener(new LoteArcillaAdacter.ItemClickListener() {
            @Override
            public void onDeleteItem(LoteArcillaClass LoteArcillaClass) {
                InserDelete(LoteArcillaClass);
            }

            @Override
            public void onEditItem(LoteArcillaClass LoteArcillaClass) {
                inserEdit(LoteArcillaClass);
            }
        });


    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {

            case R.id.delete_All:
                MessagerDateDeleteList();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void MessagerDateDeleteList() {
        new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Alerta")
                .setContentText("Estás segura de que quieres eliminar todo los datos?")
                .setCustomImage(com.microsoft.fluent.mobile.icons.R.drawable.ic_fluent_person_question_mark_24_regular)
                .setConfirmButtonTextColor(Color.parseColor("#000000"))
                .setCancelButtonTextColor(Color.parseColor("#000000"))
                .setConfirmButtonBackgroundColor(Color.parseColor("#FCD01D"))
                .setCancelButtonBackgroundColor(Color.parseColor("#FCD01D"))
                .setConfirmText("Sí, Borrar")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        VersionNLote.clear();
                        databaseReference.child("LotesArcilla").removeValue();
                        Toast.makeText(getContext(), "Datos eliminado correctamente", Toast.LENGTH_SHORT).show();
                        sDialog.dismissWithAnimation();

                    }
                })
                .setCancelButton("No, Borrar", new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.dismissWithAnimation();
                    }
                })
                .show();
    }

    private void BottonesAdapter() {
        Adapter.setmListener(new LoteArcillaAdacter.ItemClickListener() {
            @Override
            public void onDeleteItem(LoteArcillaClass LoteArcillaClass) {
                InserDelete(LoteArcillaClass);
            }

            @Override
            public void onEditItem(LoteArcillaClass LoteArcillaClass) {
                inserEdit(LoteArcillaClass);
            }
        });


    }

    private void inserEdit(LoteArcillaClass loteArcillaClass) {
        dialogBuilder = new MaterialAlertDialogBuilder(getContext());
        final View contactoPoupView = getLayoutInflater().inflate(R.layout.dialog_lote_datos, null);

        //TextInputLayout
        txtInputLotNumber = contactoPoupView.findViewById(R.id.txtInputLotNumber);

        //Textos POP UP
        edtNroLote = (EditText) contactoPoupView.findViewById(R.id.edtLotNumber);
        //traer el dato al texview
        edtNroLote.setText(loteArcillaClass.getNroloteArcilla());

        //botones
        btnGenerar = contactoPoupView.findViewById(R.id.btnRegisterLote);

        dialogBuilder.setView(contactoPoupView);
        dialog = dialogBuilder.create();
        dialog.setTitle("Editar Lote Arcilla");
        dialog.show();

        //botones
        btnGenerar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Agregar
                String NLote = edtNroLote.getText().toString();
                if (NLote.equalsIgnoreCase("")) {
                    // VALIDAR DATOS
                    validar();
                } else {
                    //datos Lotes
                    LoteArcillaClass a = new LoteArcillaClass();
                    a.setId(loteArcillaClass.getId());
                    a.setNroloteArcilla(edtNroLote.getText().toString().trim());
                    a.setFechayhora(loteArcillaClass.getFechayhora());
                    databaseReference.child("LotesArcilla").child(a.getId()).setValue(a);
                    Toast.makeText(getContext(), "Dato editado", Toast.LENGTH_SHORT).show();
                    limpiar();
                    //cancelar
                    dialog.cancel();
                }
                EditorTexto();
            }
        });
    }

    private void InserDelete(LoteArcillaClass loteArcillaClass) {
        new SweetAlertDialog(getContext(), SweetAlertDialog.ERROR_TYPE)
                .setTitleText("Aviso")
                .setContentText("Estás segura de que quieres eliminar este dato?")
                .setConfirmButtonTextColor(Color.parseColor("#000000"))
                .setCancelButtonTextColor(Color.parseColor("#000000"))
                .setConfirmButtonBackgroundColor(Color.parseColor("#FCD01D"))
                .setCancelButtonBackgroundColor(Color.parseColor("#FCD01D"))
                .setConfirmText("Sí, Borrar")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        LoteArcillaClass a = new LoteArcillaClass();
                        a.setId(loteArcillaClass.getId());
                        databaseReference.child("LotesArcilla").child(a.getId()).removeValue();
                        Toast.makeText(getContext(), "Dato eliminado correctamente", Toast.LENGTH_SHORT).show();
                        sDialog.dismissWithAnimation();
                        // ingredar datos
                    }
                })
                .setCancelButton("No, Borrar!", new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        Toast.makeText(getContext(), "Dato no eliminado", Toast.LENGTH_SHORT).show();
                        sDialog.dismissWithAnimation();
                    }
                })
                .show();
    }

    private void IniciarFirebase() {
        FirebaseApp.initializeApp(getContext());
        firebaseDatabase = FirebaseDatabase.getInstance();
        // firebaseDatabase.setPersistenceEnabled(true);
        databaseReference = firebaseDatabase.getReference();
    }

    private void ListarDatos() {
        if (!isConnected()) {
            imgenUpWifi.setVisibility(View.VISIBLE);
            imgenUp.setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.GONE);
            fab.setEnabled(false);
            btnconexionWifi.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                }
            });
        } else {
            databaseReference.child("LotesArcilla").addValueEventListener(new ValueEventListener() {
                @SuppressLint("NotifyDataSetChanged")
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    VersionNLote.clear();
                    progressBar.setVisibility(View.GONE);
                    imgenUp.setVisibility(View.VISIBLE);
                    imgenUpWifi.setVisibility(View.INVISIBLE);
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        LoteArcillaClass loteArcillaClass  = dataSnapshot.getValue(LoteArcillaClass.class);
                        VersionNLote.add(loteArcillaClass);
                        // imagen para que no se vea
                        progressBar.setVisibility(View.GONE);
                        imgenUp.setVisibility(View.INVISIBLE);
                        imgenUpWifi.setVisibility(View.INVISIBLE);
                    }
                    Adapter.notifyDataSetChanged();
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

    @Override
    public void onDataChanged() {
        if (progressBar != null) {
            progressBar.setVisibility(View.GONE);
        }
    }

    private void Actualizacion(View view) {
        sr = view.findViewById(R.id.swipeRefreshLayout);
        sr.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onRefresh() {
                InicioRecyclerview(view);
                BottonesAdapter();
                ListarDatos();
                Adapter.notifyDataSetChanged();
                Toast.makeText(getContext(), "Actualizado", Toast.LENGTH_LONG).show();
                sr.setRefreshing(false);
            }
        });
    }

    private void InicioRecyclerview(View view) {
        // RECYCLER VIEW
        recyclerView = view.findViewById(R.id.lvLotNumber);
        VersionNLote = new ArrayList<>();
        //liner layaout
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        recyclerView.setHasFixedSize(true);
        //Adapter
        Adapter = new LoteArcillaAdacter(VersionNLote);
        recyclerView.setAdapter(Adapter);
        //AgregarDato();
    }

    public void openDialog(View view) {
        fab = view.findViewById(R.id.Agregarlote);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogBuilder = new MaterialAlertDialogBuilder(getContext());
                final View contactoPoupView = getLayoutInflater().inflate(R.layout.dialog_lote_datos, null);

                //TextInputLayout
                txtInputLotNumber = contactoPoupView.findViewById(R.id.txtInputLotNumber);

                //Textos POP UP
                edtNroLote = (EditText) contactoPoupView.findViewById(R.id.edtLotNumber);

                //botones
                btnGenerar = contactoPoupView.findViewById(R.id.btnRegisterLote);

                dialogBuilder.setView(contactoPoupView);
                dialog = dialogBuilder.create();
                dialog.setTitle("Registro Lote Arcilla");
                dialog.show();

                //botones
                btnGenerar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Agregar
                        registrar_nrolote();
                    }
                });
            }
        });

    }

    private void registrar_nrolote() {
        String NLote = edtNroLote.getText().toString();
        if (NLote.equalsIgnoreCase("")) {
            validar();
        } else {
            inserccion();
            Toast.makeText(getContext(), "Agregado", Toast.LENGTH_SHORT).show();
            limpiar();
            //cancelar
            dialog.cancel();
        }
        EditorTexto();
    }

    private void inserccion() {
        //Datos de fecha y hora
        Date date = new Date();
        SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
        String Date = formatoFecha.format(date);

        //Datos de Lotes
        String nlote = edtNroLote.getText().toString();
        String key = databaseReference.push().getKey();
        LoteArcillaClass a = new LoteArcillaClass();
        a.setId(key);
        a.setNroloteArcilla(nlote);
        a.setFechayhora(Date);
        databaseReference.child("LotesArcilla").child(key).setValue(a);
    }

    private void limpiar() {
        edtNroLote.setText("");
        edtNroLote.requestFocus();
    }

    private boolean validar() {
        boolean retorno = true;
        String Lote;

        Lote = edtNroLote.getText().toString();

        if (Lote.isEmpty()) {
            txtInputLotNumber.setError("Ingrese su NªLote");
            retorno = false;
        } else {
            txtInputLotNumber.setErrorEnabled(false);
        }
        return retorno;
    }

    private void EditorTexto() {
        edtNroLote.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                txtInputLotNumber.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }
}