package com.example.colpex20app_firebase.JefeProducion.AgregarDatos;

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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SearchView;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.colpex20app_firebase.Adactadores.Adacter_JefeProducion.ClienteAdacter;
import com.example.colpex20app_firebase.Model.ObservacionItem;
import com.example.colpex20app_firebase.PrincipalClasses.ClasesJefeProducion.CuestionCliente;
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

public class FragmentCliente extends Fragment implements ObservacionItem {

    //AlertDialog
    private MaterialAlertDialogBuilder dialogBuilder;
    private AlertDialog dialog;
    //variables para el autocompletetextview
    AutoCompleteTextView txAutoCompleteCiudades;
    ArrayList<String> allNamesCities;
    //EditText
    private EditText edtNombre, edtCorreo, edtPais;
    private Button btnRegistrarCliente;
    //botones InputNombre
    private TextInputLayout txtInputNombrecliente, txtInputedtCorreo, txtInputCiudad, txtInputPais;
    //Metodo ArrayList
    ClienteAdacter Adapter;
    ArrayList<CuestionCliente> VersionCliente;
    RecyclerView recyclerView;
    NestedScrollView nestedScrollView;

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
    //buttom
    FloatingActionButton fab;
    //Progresor bar
    private ProgressBar progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_cliente, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //imagen para que no se vea
        initViewModel(view);
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

    //Metodo_buscar
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
        ArrayList<CuestionCliente> milista = new ArrayList<>();
        for (CuestionCliente obj : VersionCliente) {
            if (obj.getNombre().toLowerCase().contains(s.toLowerCase())) {
                milista.add(obj);
            } else if (obj.getFechayhora().toLowerCase().contains(s.toLowerCase())) {
                milista.add(obj);
            }
        }
        ClienteAdacter adacter = new ClienteAdacter(milista);
        recyclerView.setAdapter(adacter);
        adacter.setCListener(new ClienteAdacter.ItemClickListener() {
            @Override
            public void onDeleteItem(CuestionCliente CuestionCliente) {
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
                                CuestionCliente p = new CuestionCliente();
                                p.setId(CuestionCliente.getId());
                                databaseReference.child("Clientes").child(p.getId()).removeValue();
                                Toast.makeText(getContext(), "Dato eliminado correctamente", Toast.LENGTH_SHORT).show();
                                sDialog.dismissWithAnimation();
                            }
                        })
                        .setCancelButton("No, Borrar!", new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.dismissWithAnimation();
                            }
                        })
                        .show();
            }

            @Override
            public void onEditItem(CuestionCliente CuestionCliente) {
                dialogBuilder = new MaterialAlertDialogBuilder(getContext());
                final View contactoPoupView = getLayoutInflater().inflate(R.layout.dialog_cliente, null);

                //TextInputLayout
                txtInputNombrecliente = contactoPoupView.findViewById(R.id.txtInputNombrecliente);
                txtInputedtCorreo = contactoPoupView.findViewById(R.id.txtInputedtCorreo);
                txtInputCiudad = contactoPoupView.findViewById(R.id.txtInputCiudad);
                txtInputPais = contactoPoupView.findViewById(R.id.txtInputPais);

                //Textos POP UP
                edtNombre = (EditText) contactoPoupView.findViewById(R.id.edtName);
                //traer el dato al texview
                edtNombre.setText(CuestionCliente.getNombre());

                edtCorreo = (EditText) contactoPoupView.findViewById(R.id.edtMail);
                //traer el dato al texview
                edtCorreo.setText(CuestionCliente.getCorreo());

                edtPais = (EditText) contactoPoupView.findViewById(R.id.edtCountry);
                //traer el dato al texview
                edtPais.setText(CuestionCliente.getPais());

                //ciudades
                txAutoCompleteCiudades = (AutoCompleteTextView) contactoPoupView.findViewById(R.id.dropdownCity);
                txAutoCompleteCiudades.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        switch (Integer.parseInt(String.valueOf(position + 1))) {
                            case 1:
                                edtPais.setText("Bangkok");
                                break;
                            case 2:
                                edtPais.setText("Bogotá");
                                break;
                            case 3:
                                edtPais.setText("Brasilia");
                                break;
                            case 4:
                                edtPais.setText("Bruselas");
                                break;
                            case 5:
                                edtPais.setText("Canberra");
                                break;
                            case 6:
                                edtPais.setText("Caracas");
                                break;
                            case 7:
                                edtPais.setText("Copenhague");
                                break;
                            case 8:
                                edtPais.setText("Hanói");
                                break;
                            case 9:
                                edtPais.setText("Lima");
                                break;
                            case 10:
                                edtPais.setText("Londres");
                                break;
                            case 11:
                                edtPais.setText("Madrid");
                                break;
                            case 12:
                                edtPais.setText("Manila");
                                break;
                            case 13:
                                edtPais.setText("Nueva york");
                                break;
                            case 14:
                                edtPais.setText("Oslo");
                                break;
                            case 15:
                                edtPais.setText("Ottawa");
                                break;
                            case 16:
                                edtPais.setText("Paris");
                                break;
                            case 17:
                                edtPais.setText("Pekín");
                                break;
                            case 18:
                                edtPais.setText("Santiago de Chile");
                                break;
                            case 19:
                                edtPais.setText("Tokio");
                                break;
                            case 20:
                                edtPais.setText("Yakarta");
                                break;
                        }
                    }
                });
                //traer el dato al texview
                txAutoCompleteCiudades.setText(CuestionCliente.getCiudad());

                //autocompletetextviewArea
                allNamesCities = getNameCities();
                ArrayAdapter<String> autoCompleteCity = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, allNamesCities);
                txAutoCompleteCiudades.setAdapter(autoCompleteCity);

                //botones
                btnRegistrarCliente = contactoPoupView.findViewById(R.id.btnRegisterClente);

                dialogBuilder.setView(contactoPoupView);
                dialog = dialogBuilder.create();
                dialog.setTitle("Editar Cliente");
                dialog.show();
                //botones
                btnRegistrarCliente.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Agregar
                        String Nombre = edtNombre.getText().toString();
                        String correo = edtCorreo.getText().toString();
                        String ciudad = txAutoCompleteCiudades.getText().toString();
                        String Pais = edtPais.getText().toString();

                        if (Nombre.equalsIgnoreCase("")
                                || correo.equalsIgnoreCase("")
                                || ciudad.equalsIgnoreCase("")
                                || Pais.equalsIgnoreCase("")) {
                            // VALIDAR DATOS
                            validar();
                        } else {
                            CuestionCliente p = new CuestionCliente();
                            p.setId(CuestionCliente.getId());
                            p.setNombre(edtNombre.getText().toString().trim());
                            p.setCorreo(edtCorreo.getText().toString().trim());
                            p.setCiudad(txAutoCompleteCiudades.getText().toString().trim());
                            p.setFechayhora(CuestionCliente.getFechayhora());
                            p.setPais(edtPais.getText().toString().trim());
                            databaseReference.child("Clientes").child(p.getId()).setValue(p);
                            Toast.makeText(getContext(), "Dato editado", Toast.LENGTH_SHORT).show();
                            limpiar();
                            //cancelar
                            dialog.cancel();
                        }
                        EditorTexto();
                    }
                });
            }
        });

    }

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
        new SweetAlertDialog(this.getContext(), SweetAlertDialog.WARNING_TYPE)
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
                        VersionCliente.clear();
                        databaseReference.child("Clientes").removeValue();
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
        Adapter.setCListener(new ClienteAdacter.ItemClickListener() {
            @Override
            public void onDeleteItem(CuestionCliente CuestionCliente) {
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
                                CuestionCliente p = new CuestionCliente();
                                p.setId(CuestionCliente.getId());
                                databaseReference.child("Clientes").child(p.getId()).removeValue();
                                Toast.makeText(getContext(), "Dato eliminado correctamente", Toast.LENGTH_SHORT).show();
                                sDialog.dismissWithAnimation();
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

            @Override
            public void onEditItem(CuestionCliente CuestionCliente) {
                dialogBuilder = new MaterialAlertDialogBuilder(getContext());
                final View contactoPoupView = getLayoutInflater().inflate(R.layout.dialog_cliente, null);

                //TextInputLayout
                txtInputNombrecliente = contactoPoupView.findViewById(R.id.txtInputNombrecliente);
                txtInputedtCorreo = contactoPoupView.findViewById(R.id.txtInputedtCorreo);
                txtInputCiudad = contactoPoupView.findViewById(R.id.txtInputCiudad);
                txtInputPais = contactoPoupView.findViewById(R.id.txtInputPais);

                //Textos POP UP
                edtNombre = (EditText) contactoPoupView.findViewById(R.id.edtName);
                //traer el dato al texview
                edtNombre.setText(CuestionCliente.getNombre());

                edtCorreo = (EditText) contactoPoupView.findViewById(R.id.edtMail);
                //traer el dato al texview
                edtCorreo.setText(CuestionCliente.getCorreo());

                edtPais = (EditText) contactoPoupView.findViewById(R.id.edtCountry);
                //traer el dato al texview
                edtPais.setText(CuestionCliente.getPais());

                //ciudades
                txAutoCompleteCiudades = (AutoCompleteTextView) contactoPoupView.findViewById(R.id.dropdownCity);
                txAutoCompleteCiudades.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        switch (Integer.parseInt(String.valueOf(position + 1))) {
                            case 1:
                                edtPais.setText("Bangkok");
                                break;
                            case 2:
                                edtPais.setText("Bogotá");
                                break;
                            case 3:
                                edtPais.setText("Brasilia");
                                break;
                            case 4:
                                edtPais.setText("Bruselas");
                                break;
                            case 5:
                                edtPais.setText("Canberra");
                                break;
                            case 6:
                                edtPais.setText("Caracas");
                                break;
                            case 7:
                                edtPais.setText("Copenhague");
                                break;
                            case 8:
                                edtPais.setText("Hanói");
                                break;
                            case 9:
                                edtPais.setText("Lima");
                                break;
                            case 10:
                                edtPais.setText("Londres");
                                break;
                            case 11:
                                edtPais.setText("Madrid");
                                break;
                            case 12:
                                edtPais.setText("Manila");
                                break;
                            case 13:
                                edtPais.setText("Nueva york");
                                break;
                            case 14:
                                edtPais.setText("Oslo");
                                break;
                            case 15:
                                edtPais.setText("Ottawa");
                                break;
                            case 16:
                                edtPais.setText("Paris");
                                break;
                            case 17:
                                edtPais.setText("Pekín");
                                break;
                            case 18:
                                edtPais.setText("Santiago de Chile");
                                break;
                            case 19:
                                edtPais.setText("Tokio");
                                break;
                            case 20:
                                edtPais.setText("Yakarta");
                                break;
                        }
                    }
                });
                //traer el dato al texview
                txAutoCompleteCiudades.setText(CuestionCliente.getCiudad());

                //autocompletetextviewArea
                allNamesCities = getNameCities();
                ArrayAdapter<String> autoCompleteCity = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, allNamesCities);
                txAutoCompleteCiudades.setAdapter(autoCompleteCity);

                //botones
                btnRegistrarCliente = contactoPoupView.findViewById(R.id.btnRegisterClente);

                dialogBuilder.setView(contactoPoupView);
                dialog = dialogBuilder.create();
                dialog.setTitle("Editar Cliente");
                dialog.show();
                //botones
                btnRegistrarCliente.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Agregar
                        String Nombre = edtNombre.getText().toString();
                        String correo = edtCorreo.getText().toString();
                        String ciudad = txAutoCompleteCiudades.getText().toString();
                        String Pais = edtPais.getText().toString();

                        if (Nombre.equalsIgnoreCase("")
                                || correo.equalsIgnoreCase("")
                                || ciudad.equalsIgnoreCase("")
                                || Pais.equalsIgnoreCase("")) {
                            // VALIDAR DATOS
                            validar();
                        } else {
                            CuestionCliente p = new CuestionCliente();
                            p.setId(CuestionCliente.getId());
                            p.setNombre(edtNombre.getText().toString().trim());
                            p.setCorreo(edtCorreo.getText().toString().trim());
                            p.setCiudad(txAutoCompleteCiudades.getText().toString().trim());
                            p.setFechayhora(CuestionCliente.getFechayhora());
                            p.setPais(edtPais.getText().toString().trim());
                            databaseReference.child("Clientes").child(p.getId()).setValue(p);
                            Toast.makeText(getContext(), "Dato editado", Toast.LENGTH_SHORT).show();
                            limpiar();
                            //cancelar
                            dialog.cancel();
                        }
                        EditorTexto();
                    }
                });

            }
        });
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

    private void IniciarFirebase() {
        FirebaseApp.initializeApp(this.getContext());
        firebaseDatabase = FirebaseDatabase.getInstance();
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
            databaseReference.child("Clientes").addValueEventListener(new ValueEventListener() {
                @SuppressLint("NotifyDataSetChanged")
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    VersionCliente.clear();
                    progressBar.setVisibility(View.GONE);
                    imgenUp.setVisibility(View.VISIBLE);
                    imgenUpWifi.setVisibility(View.INVISIBLE);
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        CuestionCliente cliente = dataSnapshot.getValue(CuestionCliente.class);
                        VersionCliente.add(cliente);
                        // imagen para que no se vea
                        progressBar.setVisibility(View.GONE);
                        imgenUp.setVisibility(View.GONE);
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
        recyclerView = (RecyclerView) view.findViewById(R.id.lvClient);
        VersionCliente = new ArrayList<>();
        //liner layaout
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        recyclerView.setHasFixedSize(true);
        //Adacter
        Adapter = new ClienteAdacter(VersionCliente);
        recyclerView.setAdapter(Adapter);

    }

    public void openDialog(View view) {
        fab = view.findViewById(R.id.AgregarCliente);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogBuilder = new MaterialAlertDialogBuilder(getContext());
                final View contactoPoupView = getLayoutInflater().inflate(R.layout.dialog_cliente, null);

                //TextInputLayout
                txtInputNombrecliente = contactoPoupView.findViewById(R.id.txtInputNombrecliente);
                txtInputedtCorreo = contactoPoupView.findViewById(R.id.txtInputedtCorreo);
                txtInputCiudad = contactoPoupView.findViewById(R.id.txtInputCiudad);
                txtInputPais = contactoPoupView.findViewById(R.id.txtInputPais);

                //Textos POP UP
                edtNombre = (EditText) contactoPoupView.findViewById(R.id.edtName);
                edtCorreo = (EditText) contactoPoupView.findViewById(R.id.edtMail);
                edtPais = (EditText) contactoPoupView.findViewById(R.id.edtCountry);

                //ciudades
                txAutoCompleteCiudades = (AutoCompleteTextView) contactoPoupView.findViewById(R.id.dropdownCity);
                txAutoCompleteCiudades.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        switch (Integer.parseInt(String.valueOf(position + 1))) {
                            case 1:
                                edtPais.setText("Bangkok");
                                break;
                            case 2:
                                edtPais.setText("Bogotá");
                                break;
                            case 3:
                                edtPais.setText("Brasilia");
                                break;
                            case 4:
                                edtPais.setText("Bruselas");
                                break;
                            case 5:
                                edtPais.setText("Canberra");
                                break;
                            case 6:
                                edtPais.setText("Caracas");
                                break;
                            case 7:
                                edtPais.setText("Copenhague");
                                break;
                            case 8:
                                edtPais.setText("Hanói");
                                break;
                            case 9:
                                edtPais.setText("Lima");
                                break;
                            case 10:
                                edtPais.setText("Londres");
                                break;
                            case 11:
                                edtPais.setText("Madrid");
                                break;
                            case 12:
                                edtPais.setText("Manila");
                                break;
                            case 13:
                                edtPais.setText("Nueva york");
                                break;
                            case 14:
                                edtPais.setText("Oslo");
                                break;
                            case 15:
                                edtPais.setText("Ottawa");
                                break;
                            case 16:
                                edtPais.setText("Paris");
                                break;
                            case 17:
                                edtPais.setText("Pekín");
                                break;
                            case 18:
                                edtPais.setText("Santiago de Chile");
                                break;
                            case 19:
                                edtPais.setText("Tokio");
                                break;
                            case 20:
                                edtPais.setText("Yakarta");
                                break;
                        }
                    }
                });

                //autocompletetextviewArea
                allNamesCities = getNameCities();
                ArrayAdapter<String> autoCompleteCity = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, allNamesCities);
                txAutoCompleteCiudades.setAdapter(autoCompleteCity);

                //botones
                btnRegistrarCliente = contactoPoupView.findViewById(R.id.btnRegisterClente);

                dialogBuilder.setView(contactoPoupView);
                dialog = dialogBuilder.create();
                dialog.setTitle("Registro Cliente");
                dialog.show();

                //botones
                btnRegistrarCliente.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Agregar
                        registrar_cliente();
                    }
                });
            }
        });
    }

    private ArrayList<String> getNameCities() {
        ArrayList<String> allNameCities = new ArrayList<>();
        allNameCities.add("Tailandia");
        allNameCities.add("Colombia");
        allNameCities.add("Brasil");
        allNameCities.add("Bélgica");
        allNameCities.add("Australia");
        allNameCities.add("Venezuela");
        allNameCities.add("Dinamarca");
        allNameCities.add("Vietnam");
        allNameCities.add("Perú");
        allNameCities.add("Reino Unido");
        allNameCities.add("España");
        allNameCities.add("Filipinas");
        allNameCities.add("Estados Unidos");
        allNameCities.add("Noruega");
        allNameCities.add("Canadá");
        allNameCities.add("Francia");
        allNameCities.add("China");
        allNameCities.add("Chile");
        allNameCities.add("Japón");
        allNameCities.add("Indonesia");
        return allNameCities;
    }

    private void registrar_cliente() {
        String Nombre = edtNombre.getText().toString();
        String correo = edtCorreo.getText().toString();
        String ciudad = txAutoCompleteCiudades.getText().toString();
        String Pais = edtPais.getText().toString();

        if (Nombre.equalsIgnoreCase("")
                || correo.equalsIgnoreCase("")
                || ciudad.equalsIgnoreCase("")
                || Pais.equalsIgnoreCase("")) {
            // VALIDAR DATOS
            validar();
        } else {
            inserccion();
            Toast.makeText(this.getContext(), "Agregado", Toast.LENGTH_SHORT).show();
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
        String Nombre = edtNombre.getText().toString();
        String correo = edtCorreo.getText().toString();
        String ciudad = txAutoCompleteCiudades.getText().toString();
        String Pais = edtPais.getText().toString();
        String key = databaseReference.push().getKey();
        CuestionCliente p = new CuestionCliente();
        p.setId(key);
        p.setNombre(Nombre);
        p.setCorreo(correo);
        p.setCiudad(ciudad);
        p.setPais(Pais);
        p.setFechayhora(Date);
        p.setFechayhora(Date);
        databaseReference.child("Clientes").child(key).setValue(p);
    }

    private void limpiar() {
        edtNombre.setText("");
        edtCorreo.setText("");
        txAutoCompleteCiudades.setText("");
        edtPais.setText("");
        edtNombre.requestFocus();
    }

    private boolean validar() {
        boolean retorno = true;
        String Nombre, Correo, CompleteCiudades, Pais;

        Nombre = edtNombre.getText().toString();
        Correo = edtCorreo.getText().toString();
        CompleteCiudades = txAutoCompleteCiudades.getText().toString();
        Pais = edtPais.getText().toString();

        if (Nombre.isEmpty()) {
            txtInputNombrecliente.setError("Ingrese su  Cliente");
            retorno = false;
        } else {
            txtInputNombrecliente.setErrorEnabled(false);
        }
        if (Correo.isEmpty()) {
            txtInputedtCorreo.setError("Ingrese su Correo");
            retorno = false;
        } else {
            txtInputedtCorreo.setErrorEnabled(false);
        }
        if (CompleteCiudades.isEmpty()) {
            txtInputCiudad.setError("Seleccionar su Ciudad");
            retorno = false;
        } else {
            txtInputCiudad.setErrorEnabled(false);
        }
        if (Pais.isEmpty()) {
            txtInputPais.setError("Seleccionar su Pais");
            retorno = false;
        } else {
            txtInputPais.setErrorEnabled(false);
        }

        return retorno;
    }

    private void EditorTexto() {
        edtNombre.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                txtInputNombrecliente.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        edtCorreo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                txtInputedtCorreo.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        txAutoCompleteCiudades.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                txtInputCiudad.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        edtPais.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                txtInputPais.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }


}