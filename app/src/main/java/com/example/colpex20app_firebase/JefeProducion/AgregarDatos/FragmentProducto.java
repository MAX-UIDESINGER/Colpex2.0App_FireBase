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
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.colpex20app_firebase.Adactadores.Adacter_JefeProducion.ProductoAdacter;
import com.example.colpex20app_firebase.Model.ObservacionItem;
import com.example.colpex20app_firebase.PrincipalClasses.ClasesJefeProducion.CuestionProducto;
import com.example.colpex20app_firebase.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
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

public class FragmentProducto extends Fragment implements ObservacionItem {
    //Navegacion
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    static final float End_SCALE = 0.7f;
    LinearLayout contentView;
    //AlertDialog
    private MaterialAlertDialogBuilder dialogBuilder;
    private AlertDialog dialog;
    //botones InputNombre
    private TextInputLayout txtInputNombreProducto, txtInputedtDescription, txtInputStock;
    //EditText
    private EditText edtNombre, edtDescripcion, edtStock;
    //Buttom
    private Button btnRegistrarProducto;
    //Metodo ArrayList
    ProductoAdacter Adapter;
    ArrayList<CuestionProducto> VersionProducto;
    RecyclerView recyclerView;
    //botom para Navegar
    BottomNavigationView bottomNavigationView;
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
    //TooLL bar
    Toolbar toolbar;
    FloatingActionButton fab;
    //Progresor bar
    private ProgressBar progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_producto, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //imagen para que no se vea
        initViewModel(view);
        // MENU NO TOCAR
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
        imgenUp = (LinearLayout) view.findViewById(R.id.imgenUp);
        imgenUpWifi = (LinearLayout) view.findViewById(R.id.imgenUpWifi);
        btnconexionWifi = (Button) view.findViewById(R.id.btnconexionWifi);
    }

    //Wifi_Validar
    private boolean isConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiConn = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobileConn = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

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
        ArrayList<CuestionProducto> milista = new ArrayList<>();
        for (CuestionProducto obj : VersionProducto) {
            if (obj.getNombre().toLowerCase().contains(s.toLowerCase())) {
                milista.add(obj);
            } else if (obj.getFechayhora().toLowerCase().contains(s.toLowerCase())) {
                milista.add(obj);
            }

        }
        ProductoAdacter adacter = new ProductoAdacter(milista);
        recyclerView.setAdapter(adacter);
        adacter.setpListener(new ProductoAdacter.ItemClickListener() {
            @Override
            public void onDeleteItem(CuestionProducto CuestionProducto) {
                new SweetAlertDialog(getContext(), SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Alerta")
                        .setContentText("Estás segura de que quieres eliminar?")
                        .setConfirmButtonTextColor(Color.parseColor("#000000"))
                        .setCancelButtonTextColor(Color.parseColor("#000000"))
                        .setConfirmButtonBackgroundColor(Color.parseColor("#FCD01D"))
                        .setCancelButtonBackgroundColor(Color.parseColor("#FCD01D"))
                        .setConfirmText("Sí, Borrar")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                CuestionProducto p = new CuestionProducto();
                                p.setId(CuestionProducto.getId());
                                databaseReference.child("Producto").child(p.getId()).removeValue();
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
            public void onEditItem(CuestionProducto CuestionProducto) {
                dialogBuilder = new MaterialAlertDialogBuilder(getContext());
                final View contactoPoupView = getLayoutInflater().inflate(R.layout.dialog_producto, null);

                //TextInputLayout
                txtInputNombreProducto = contactoPoupView.findViewById(R.id.txtInputNombreProducto);
                txtInputedtDescription = contactoPoupView.findViewById(R.id.txtInputedtDescription);
                txtInputStock = contactoPoupView.findViewById(R.id.txtInputStock);

                //Textos POP UP
                edtNombre = (EditText) contactoPoupView.findViewById(R.id.edtName);
                //traer el dato al texview
                edtNombre.setText(CuestionProducto.getNombre());

                edtDescripcion = (EditText) contactoPoupView.findViewById(R.id.edtDescription);
                //traer el dato al texview
                edtDescripcion.setText(CuestionProducto.getDescripcion());

                edtStock = (EditText) contactoPoupView.findViewById(R.id.edtStock);
                //traer el dato al texview
                edtStock.setText(CuestionProducto.getStock());

                //botones
                btnRegistrarProducto = contactoPoupView.findViewById(R.id.btnRegisterproducto);

                dialogBuilder.setView(contactoPoupView);
                dialog = dialogBuilder.create();
                dialog.setTitle("Editar Producto");
                dialog.show();

                //botones
                btnRegistrarProducto.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Agregar
                        String Nombre = edtNombre.getText().toString();
                        String Descripcion = edtDescripcion.getText().toString();
                        String Stock = edtStock.getText().toString();
                        if (Nombre.equalsIgnoreCase("")
                                || Descripcion.equalsIgnoreCase("")
                                || Stock.equalsIgnoreCase("")) {
                            // VALIDAR DATOS
                            validar();
                        } else {
                            //Datos de producto
                            CuestionProducto p = new CuestionProducto();
                            p.setId(CuestionProducto.getId());
                            p.setNombre(Nombre);
                            p.setDescripcion(Descripcion);
                            p.setStock(Stock);
                            p.setFechayhora(CuestionProducto.getFechayhora());
                            databaseReference.child("Producto").child(p.getId()).setValue(p);
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
                        VersionProducto.clear();
                        databaseReference.child("Producto").removeValue();
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
        Adapter.setpListener(new ProductoAdacter.ItemClickListener() {
            @Override
            public void onDeleteItem(CuestionProducto CuestionProducto) {
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
                                CuestionProducto p = new CuestionProducto();
                                p.setId(CuestionProducto.getId());
                                databaseReference.child("Producto").child(p.getId()).removeValue();
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
            public void onEditItem(CuestionProducto CuestionProducto) {
                dialogBuilder = new MaterialAlertDialogBuilder(getContext());
                final View contactoPoupView = getLayoutInflater().inflate(R.layout.dialog_producto, null);

                //TextInputLayout
                txtInputNombreProducto = contactoPoupView.findViewById(R.id.txtInputNombreProducto);
                txtInputedtDescription = contactoPoupView.findViewById(R.id.txtInputedtDescription);
                txtInputStock = contactoPoupView.findViewById(R.id.txtInputStock);

                //Textos POP UP
                edtNombre = (EditText) contactoPoupView.findViewById(R.id.edtName);
                //traer el dato al texview
                edtNombre.setText(CuestionProducto.getNombre());

                edtDescripcion = (EditText) contactoPoupView.findViewById(R.id.edtDescription);
                //traer el dato al texview
                edtDescripcion.setText(CuestionProducto.getDescripcion());

                edtStock = (EditText) contactoPoupView.findViewById(R.id.edtStock);
                //traer el dato al texview
                edtStock.setText(CuestionProducto.getStock());

                //botones
                btnRegistrarProducto = contactoPoupView.findViewById(R.id.btnRegisterproducto);

                dialogBuilder.setView(contactoPoupView);
                dialog = dialogBuilder.create();
                dialog.setTitle("Editar Producto");
                dialog.show();

                //botones
                btnRegistrarProducto.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Agregar
                        String Nombre = edtNombre.getText().toString();
                        String Descripcion = edtDescripcion.getText().toString();
                        String Stock = edtStock.getText().toString();
                        if (Nombre.equalsIgnoreCase("")
                                || Descripcion.equalsIgnoreCase("")
                                || Stock.equalsIgnoreCase("")) {
                            imgenUp.setVisibility(View.INVISIBLE);
                            // VALIDAR DATOS
                            validar();
                        } else {
                            //Datos de producto
                            CuestionProducto p = new CuestionProducto();
                            p.setId(CuestionProducto.getId());
                            p.setNombre(Nombre);
                            p.setDescripcion(Descripcion);
                            p.setStock(Stock);
                            p.setFechayhora(CuestionProducto.getFechayhora());
                            databaseReference.child("Producto").child(p.getId()).setValue(p);
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
            databaseReference.child("Producto").addValueEventListener(new ValueEventListener() {
                @SuppressLint("NotifyDataSetChanged")
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    VersionProducto.clear();
                    progressBar.setVisibility(View.GONE);
                    imgenUp.setVisibility(View.VISIBLE);
                    imgenUpWifi.setVisibility(View.INVISIBLE);
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        CuestionProducto Producto = dataSnapshot.getValue(CuestionProducto.class);
                        VersionProducto.add(Producto);
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
        recyclerView = (RecyclerView) view.findViewById(R.id.lvProduct);
        VersionProducto = new ArrayList<>();
        //liner layaout
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        recyclerView.setHasFixedSize(true);
        //Adapter
        Adapter = new ProductoAdacter(VersionProducto);
        recyclerView.setAdapter(Adapter);
    }

    public void openDialog(View view) {
        fab = view.findViewById(R.id.AgregarProducto);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogBuilder = new MaterialAlertDialogBuilder(getContext());
                final View contactoPoupView = getLayoutInflater().inflate(R.layout.dialog_producto, null);

                //TextInputLayout
                txtInputNombreProducto = contactoPoupView.findViewById(R.id.txtInputNombreProducto);
                txtInputedtDescription = contactoPoupView.findViewById(R.id.txtInputedtDescription);
                txtInputStock = contactoPoupView.findViewById(R.id.txtInputStock);

                //Textos POP UP
                edtNombre = (EditText) contactoPoupView.findViewById(R.id.edtName);
                edtDescripcion = (EditText) contactoPoupView.findViewById(R.id.edtDescription);
                edtStock = (EditText) contactoPoupView.findViewById(R.id.edtStock);

                //botones
                btnRegistrarProducto = contactoPoupView.findViewById(R.id.btnRegisterproducto);

                dialogBuilder.setView(contactoPoupView);
                dialog = dialogBuilder.create();
                dialog.setTitle("Registro Producto");
                dialog.show();

                //botones
                btnRegistrarProducto.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Agregar
                        registrar_producto();
                    }
                });
            }
        });
    }

    private void registrar_producto() {
        String Nombre = edtNombre.getText().toString();
        String Descripcion = edtDescripcion.getText().toString();
        String Stock = edtStock.getText().toString();
        if (Nombre.equalsIgnoreCase("")
                || Descripcion.equalsIgnoreCase("")
                || Stock.equalsIgnoreCase("")) {
            imgenUp.setVisibility(View.INVISIBLE);
            // VALIDAR DATOS
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
        String Nombre = edtNombre.getText().toString();
        String Descripcion = edtDescripcion.getText().toString();
        String Stock = edtStock.getText().toString();
        String key = databaseReference.push().getKey();
        CuestionProducto p = new CuestionProducto();
        p.setId(key);
        p.setNombre(Nombre);
        p.setDescripcion(Descripcion);
        p.setStock(Stock);
        p.setFechayhora(Date);
        databaseReference.child("Producto").child(key).setValue(p);

    }

    private void limpiar() {
        edtNombre.setText("");
        edtDescripcion.setText("");
        edtStock.setText("");
        edtNombre.requestFocus();
    }

    private boolean validar() {
        boolean retorno = true;
        String nombre, Descripcion, Stock;

        nombre = edtNombre.getText().toString();
        Descripcion = edtDescripcion.getText().toString();
        Stock = edtStock.getText().toString();

        if (nombre.isEmpty()) {
            txtInputNombreProducto.setError("Ingrese su Producto");
            retorno = false;
        } else {
            txtInputNombreProducto.setErrorEnabled(false);
        }
        if (Descripcion.isEmpty()) {
            txtInputedtDescription.setError("Ingrese su Descripciòn");
            retorno = false;
        } else {
            txtInputedtDescription.setErrorEnabled(false);
        }
        if (Stock.isEmpty()) {
            txtInputStock.setError("Ingrese su Stock");
            retorno = false;
        } else {
            txtInputStock.setErrorEnabled(false);
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
                txtInputNombreProducto.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        edtDescripcion.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                txtInputedtDescription.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        edtStock.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                txtInputStock.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

}