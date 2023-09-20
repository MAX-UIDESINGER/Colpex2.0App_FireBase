package com.example.colpex20app_firebase.JefeGeneral.FragmentContenido_General_Jefe;

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
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.colpex20app_firebase.Adactadores.Adater_Jefe_General.LoteAdacter_Jefe_General;
import com.example.colpex20app_firebase.Model.ObservacionItem;
import com.example.colpex20app_firebase.PrincipalClasses.ClasesJefeProducion.Lote;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import cn.pedant.SweetAlert.SweetAlertDialog;
public class FragmentObservacion_Jefe_General extends Fragment implements ObservacionItem {
    //Metodo ArrayList
    LoteAdacter_Jefe_General adapter;
    ArrayList<Lote> VersionList;

    LoteAdacter_Jefe_General adapterListview;
    ArrayList<Lote> VersionListview;

    RecyclerView recyclerView;
    RecyclerView recyclerViewLisview;
    //conexion Firebase
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    private FirebaseAuth mAuth;
    FirebaseUser currentUser;
    //quitar la imagen de fondo
    private LinearLayout imgenUp;
    //mosdar no hay Internet
    private LinearLayout imgenUpWifi;
    private Button btnconexionWifi;
    //Actualizar
    SwipeRefreshLayout sr;
    //TexviewInput
    private TextInputLayout txtInputnumeroLote, txtInputproducto, txtInputCliente,
            txtInputTonelada, txtInputBatch, txtInputAcidoFosforico, txtInputTTrabajo,
            txtInputTResidencia, txtInputobservacion;
    //Autocomplete
    AutoCompleteTextView txAutoCompleteProductos;
    AutoCompleteTextView txAutoCompleteClientes;
    AutoCompleteTextView txAutoCompleteNroLotes;
    //
    //EditText
    EditText dropdownLotes, dropdownProductos, dropdownclientes;
    EditText edtTonelada, edtAcidoFosforico, edtTemperaturaTrabajo, edtTResidencia, edtCantidadBatch, Fecha, edtObservacion;
    //TooLL bar
    Toolbar toolbar;
    //Progresor bar
    private ProgressBar progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().setTitle(" Observación");
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_observacion__jefe__general, container, false);
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
        //botonos de editar y eliminar para el Recycleview
        BottonesAdapter();
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
        ArrayList<Lote> milista = new ArrayList<>();
        for (Lote obj : VersionList) {
            if (obj.getNro_lote().toLowerCase().contains(s.toLowerCase())) {
                milista.add(obj);
            } else if (obj.getProducto().toLowerCase().contains(s.toLowerCase())) {
                milista.add(obj);
            } else if (obj.getCliente().toLowerCase().contains(s.toLowerCase())) {
                milista.add(obj);
            } else if (obj.getTonelaje().toLowerCase().contains(s.toLowerCase())) {
                milista.add(obj);
            } else if (obj.getFecha().toLowerCase().contains(s.toLowerCase())) {
                milista.add(obj);
            } else if (obj.getAcido_fosforico().toLowerCase().contains(s.toLowerCase())) {
                milista.add(obj);
            } else if (obj.getTemperatura_trabajo().toLowerCase().contains(s.toLowerCase())) {
                milista.add(obj);
            } else if (obj.getT_residencia().toLowerCase().contains(s.toLowerCase())) {
                milista.add(obj);
            } else if (obj.getUserName().toLowerCase().contains(s.toLowerCase())) {
                milista.add(obj);
            } else if (obj.getBatch().toLowerCase().contains(s.toLowerCase())) {
                milista.add(obj);
            }
        }
        // VersionList.clear();
        // VersionListview.clear();
        LoteAdacter_Jefe_General adacter = new LoteAdacter_Jefe_General(milista);
        recyclerView.setAdapter(adacter);
        recyclerViewLisview.setAdapter(adacter);
        adacter.setlListener(new LoteAdacter_Jefe_General.ItemClickListener() {
            @Override
            public void onDeleteItem(Lote Lote) {
                DeleteMetodo(Lote);
            }

            @Override
            public void onEditItem(Lote Lote) {
                EditMetotodo(Lote);
            }
        });
    }

    private void DeleteMetodo(Lote lote) {

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
                        VersionList.clear();
                        VersionListview.clear();
                        Lote p = new Lote();
                        p.setId(lote.getId());
                        databaseReference.child("Lote").child(p.getId()).removeValue();
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
                        VersionList.clear();
                        VersionListview.clear();
                        databaseReference.child("Lote").removeValue();
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

    //botonos de editar y eliminar para el Recycleview
    private void BottonesAdapter() {
        adapter.setlListener(new LoteAdacter_Jefe_General.ItemClickListener() {
            @Override
            public void onDeleteItem(Lote Lote) {
                DeleteMetodo(Lote);
            }

            @Override
            public void onEditItem(Lote Lote) {
                EditMetotodo(Lote);
            }
        });

        adapterListview.setlListener(new LoteAdacter_Jefe_General.ItemClickListener() {
            @Override
            public void onDeleteItem(Lote Lote) {
                DeleteMetodo(Lote);
            }

            @Override
            public void onEditItem(Lote Lote) {
                EditMetotodo(Lote);
            }
        });

    }

    private void EditMetotodo(Lote lote) {

        BottomSheetDialog sheetDialog = new BottomSheetDialog(getContext());
        sheetDialog.setContentView(R.layout.update_popup_jefe_general);
        //sheetDialog.setCanceledOnTouchOutside(false);
        txtInputnumeroLote = sheetDialog.findViewById(R.id.txtInputnumeroLote);
        txtInputproducto = sheetDialog.findViewById(R.id.txtInputproducto);
        txtInputCliente = sheetDialog.findViewById(R.id.txtInputCliente);

        txtInputTonelada = sheetDialog.findViewById(R.id.txtInputTonelada);
        txtInputBatch = sheetDialog.findViewById(R.id.txtInputBatch);
        txtInputAcidoFosforico = sheetDialog.findViewById(R.id.txtInputAcidoFosforico);
        txtInputTTrabajo = sheetDialog.findViewById(R.id.txtInputTTrabajo);
        txtInputTResidencia = sheetDialog.findViewById(R.id.txtInputTResidencia);


        dropdownLotes = sheetDialog.findViewById(R.id.dropdownLote);
        dropdownProductos = sheetDialog.findViewById(R.id.dropdownProducto);
        dropdownclientes = sheetDialog.findViewById(R.id.dropdowncliente);

        edtTonelada = sheetDialog.findViewById(R.id.edtTon);
        edtCantidadBatch = sheetDialog.findViewById(R.id.edtLotQuantity);
        EditText fecha = sheetDialog.findViewById(R.id.edtDate);
        edtAcidoFosforico = sheetDialog.findViewById(R.id.edtPhosphoricAcid);
        edtTemperaturaTrabajo = sheetDialog.findViewById(R.id.edtWorkingTemperature);
        edtTResidencia = sheetDialog.findViewById(R.id.edtTHome);
        edtObservacion = sheetDialog.findViewById(R.id.edtObservacion);
        Button btn_guardar = sheetDialog.findViewById(R.id.btnRegistate);

        dropdownLotes.setText(lote.getNro_lote());
        dropdownProductos.setText(lote.getProducto());
        dropdownclientes.setText(lote.getCliente());

        edtTonelada.setText(lote.getTonelaje());
        edtCantidadBatch.setText(lote.getBatch());
        fecha.setText(lote.getFecha());
        edtAcidoFosforico.setText(lote.getAcido_fosforico());
        edtTemperaturaTrabajo.setText(lote.getTemperatura_trabajo());
        edtTResidencia.setText(lote.getT_residencia());
        edtObservacion.setText(lote.getDescripcion());
        //Auto complete Producto
        txAutoCompleteProductos = sheetDialog.findViewById(R.id.dropdownProducto);
        txAutoCompleteClientes = sheetDialog.findViewById(R.id.dropdowncliente);
        txAutoCompleteNroLotes = sheetDialog.findViewById(R.id.dropdownLote);

        databaseReference = FirebaseDatabase.getInstance().getReference();
        final ArrayAdapter<String> getNameLotNumber = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1);
        databaseReference.child("NLotes").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                getNameLotNumber.clear();
                for (DataSnapshot areaSnapshot : dataSnapshot.getChildren()) {
                    String areaName = areaSnapshot.child("nrolote").getValue(String.class);
                    getNameLotNumber.add(areaName);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        AutoCompleteTextView ACTV = (AutoCompleteTextView) txAutoCompleteNroLotes;
        ACTV.setAdapter(getNameLotNumber);

        databaseReference = FirebaseDatabase.getInstance().getReference();
        final ArrayAdapter<String> getNameClient = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1);
        databaseReference.child("Clientes").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                getNameClient.clear();
                for (DataSnapshot areaSnapshot : dataSnapshot.getChildren()) {
                    String areaName = areaSnapshot.child("nombre").getValue(String.class);
                    getNameClient.add(areaName);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        AutoCompleteTextView ACT = (AutoCompleteTextView) txAutoCompleteClientes;
        ACT.setAdapter(getNameClient);

        databaseReference = FirebaseDatabase.getInstance().getReference();
        final ArrayAdapter<String> getNameProduct = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1);
        databaseReference.child("Producto").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                getNameProduct.clear();
                for (DataSnapshot areaSnapshot : dataSnapshot.getChildren()) {
                    String areaName = areaSnapshot.child("nombre").getValue(String.class);
                    getNameProduct.add(areaName);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        AutoCompleteTextView AC = (AutoCompleteTextView) txAutoCompleteProductos;
        AC.setAdapter(getNameProduct);


        btn_guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String CantidadBatch = edtCantidadBatch.getText().toString();
                String AcidoFosforico = edtAcidoFosforico.getText().toString();
                String TemperaturaTrabajo = edtTemperaturaTrabajo.getText().toString();

                if (dropdownLotes.getText().toString().equalsIgnoreCase("")
                        || dropdownProductos.getText().toString().equalsIgnoreCase("")
                        || dropdownclientes.getText().toString().equalsIgnoreCase("")
                        || edtTonelada.getText().toString().equalsIgnoreCase("")
                        || edtCantidadBatch.getText().toString().equalsIgnoreCase("")
                        || fecha.getText().toString().equalsIgnoreCase("")
                        || edtAcidoFosforico.getText().toString().equalsIgnoreCase("")
                        || edtTemperaturaTrabajo.getText().toString().equalsIgnoreCase("")
                        || edtTResidencia.getText().toString().equalsIgnoreCase("")) {

                    // VALIDAR DATOS
                    validar();

                } else if (Integer.parseInt(CantidadBatch) < 1
                        || Integer.parseInt(CantidadBatch) > 10
                        || Double.parseDouble(AcidoFosforico) < 5 || Double.parseDouble(AcidoFosforico) > 10
                        || Integer.parseInt(TemperaturaTrabajo) < 30 || Integer.parseInt(TemperaturaTrabajo) > 60) {
                    // MensajeDatos
                    validar();
                    AlertDialogRangos(lote, sheetDialog);

                } else {
                    inserccion(lote);
                    Toast.makeText(getContext(), "Dato editado", Toast.LENGTH_SHORT).show();
                    sheetDialog.dismiss();
                }
                EditorTexto();

            }
        });

        sheetDialog.show();

    }

    private void AlertDialogRangos(Lote lote, BottomSheetDialog sheetDialog) {
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
                        validar();
                        inserccion(lote);
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
        EditorTexto();
    }

    @SuppressLint("SetTextI18n")
    private void inserccion(Lote lote) {
        //minutos
        Date Minutos = new Date();
        SimpleDateFormat formatominutos = new SimpleDateFormat("mm");
        String minutos = formatominutos.format(Minutos);
        //datos de Lote
        String Lotes = dropdownLotes.getText().toString();
        String Productos = dropdownProductos.getText().toString();
        String Clientes = dropdownclientes.getText().toString();
        String Tonelada = edtTonelada.getText().toString();
        //Integer CantidadBatch = Integer.parseInt(edtCantidadBatch.getText().toString());
        String CantidadBatch = edtCantidadBatch.getText().toString();
        String AcidoFosforico = edtAcidoFosforico.getText().toString();
        String TemperaturaTrabajo = edtTemperaturaTrabajo.getText().toString();
        String TResidencia = edtTResidencia.getText().toString();
        String Descripcion = edtObservacion.getText().toString();
        if (Descripcion.equals("")) {
            Descripcion = "No hay observación";
        } else {
            Descripcion.toString();
        }
        Lote l = new Lote();
        l.setId(lote.getId());
        l.setNro_lote(Lotes);
        l.setProducto(Productos);
        l.setCliente(Clientes);
        l.setTonelaje(Tonelada);
        l.setBatch(CantidadBatch);
        l.setBatchBlanqueado(CantidadBatch);
        l.setBatchDesgomado(CantidadBatch);
        l.setBatchNeutralizado(CantidadBatch);
        l.setFecha(lote.getFecha());
        l.setMinutos(minutos + " min");
        l.setAcido_fosforico(String.valueOf(Double.parseDouble(AcidoFosforico) / 100 * 100));
        l.setTemperatura_trabajo(TemperaturaTrabajo);
        l.setT_residencia(TResidencia);
        l.setCargoArea(lote.getCargoArea());
        l.setDescripcion(Descripcion);
        l.setBadgenotification(lote.getBadgenotification());
        l.setUserName(lote.getUserName());
        l.setImagenUrl(lote.getImagenUrl());
        databaseReference.child("Lote").child(l.getId()).setValue(l);
    }


    private boolean validar() {
        boolean retorno = true;
        String NroLotes, Productos, cliente, Tonelada, CantidadBatch,
                AcidoFosforico, TemperaturaTrabajo, TResidencia;

        NroLotes = txAutoCompleteNroLotes.getText().toString();
        Productos = txAutoCompleteProductos.getText().toString();
        cliente = txAutoCompleteClientes.getText().toString();

        Tonelada = edtTonelada.getText().toString();
        CantidadBatch = edtCantidadBatch.getText().toString();
        AcidoFosforico = edtAcidoFosforico.getText().toString();
        TemperaturaTrabajo = edtTemperaturaTrabajo.getText().toString();
        TResidencia = edtTResidencia.getText().toString().trim();

        if (NroLotes.isEmpty()) {
            txtInputnumeroLote.setError("Seleccionar Número de Lote");
            retorno = false;
        } else {
            txtInputnumeroLote.setErrorEnabled(false);
        }

        if (Productos.isEmpty()) {
            txtInputproducto.setError("Seleccionar Producto");
            retorno = false;
        } else {
            txtInputproducto.setErrorEnabled(false);
        }

        if (cliente.isEmpty()) {
            txtInputCliente.setError("Seleccionar el Cliente");
            retorno = false;
        } else {
            txtInputCliente.setErrorEnabled(false);
        }

        if (Tonelada.isEmpty()) {
            txtInputTonelada.setError("Ingresar Tonelada");
            retorno = false;
        } else {
            txtInputTonelada.setErrorEnabled(false);
        }

        if (CantidadBatch.isEmpty()) {
            txtInputBatch.setError("Ingresar Cantidad de Batch");
            retorno = false;
        } else if (CantidadBatch.isEmpty() || Integer.parseInt(CantidadBatch) < 1 || Integer.parseInt(CantidadBatch) > 10) {
            txtInputBatch.setError("Maxima cantidad de Batch '10'");
            retorno = false;
        } else {
            txtInputBatch.setErrorEnabled(false);
        }

        if (AcidoFosforico.isEmpty()) {
            txtInputAcidoFosforico.setError("Ingresar Acido Fosfórico");
            retorno = false;
        } else if (AcidoFosforico.isEmpty() || Double.parseDouble(AcidoFosforico) < 5 || Double.parseDouble(AcidoFosforico) > 10) {
            txtInputAcidoFosforico.setError("Fuera de Rango 0,05% - 0,10%");
            retorno = false;
        } else {
            txtInputAcidoFosforico.setErrorEnabled(false);
        }

        if (TemperaturaTrabajo.isEmpty()) {
            txtInputTTrabajo.setError("Ingresar Temperatura de Trabajo");
            retorno = false;
        } else if (TemperaturaTrabajo.isEmpty() || Integer.parseInt(TemperaturaTrabajo) < 30 || Integer.parseInt(TemperaturaTrabajo) > 60) {
            txtInputTTrabajo.setError("Fuera de Rango 30º - 60º C");
            retorno = false;
        } else {
            txtInputTTrabajo.setErrorEnabled(false);
        }


        if (TResidencia.isEmpty()) {
            txtInputTResidencia.setError("Ingresar T.Residencia ");
            retorno = false;
        } else {
            txtInputTResidencia.setErrorEnabled(false);
        }

        return retorno;
    }

    private void EditorTexto() {
        dropdownLotes.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                txtInputnumeroLote.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        dropdownProductos.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                txtInputproducto.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        dropdownclientes.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                txtInputCliente.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        edtTonelada.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                txtInputTonelada.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        edtCantidadBatch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                txtInputBatch.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        edtAcidoFosforico.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                txtInputAcidoFosforico.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        edtTemperaturaTrabajo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                txtInputTTrabajo.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        edtTResidencia.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                txtInputTResidencia.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {
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
                InicioRecycleLisView(view);
                BottonesAdapter();
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
                    imgenUp.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
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

        //recyclerView.setLayoutManager(new GridLayoutManager(this.getContext(), 2));
        //Adapter
        adapter = new LoteAdacter_Jefe_General(VersionList);
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
        adapterListview = new LoteAdacter_Jefe_General(VersionListview);
        recyclerViewLisview.setAdapter(adapterListview);
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

    private void IniciarFirebase() {
        FirebaseApp.initializeApp(this.getContext());
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
    }
}