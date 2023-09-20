package com.example.colpex20app_firebase.JefeProducion.FragmenContenido;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
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
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.colpex20app_firebase.Model.ObservacionItem;
import com.example.colpex20app_firebase.PrincipalClasses.ClasesJefeProducion.Lote;
import com.example.colpex20app_firebase.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class FragmentProduccion extends Fragment implements ObservacionItem {
    Button Registrardatos;
    static int REQUESCODE = 1;
    //TexviewInput
    private TextInputLayout txtInputnumeroLote, txtInputproducto, txtInputCliente,
            txtInputTonelada, txtInputBatch, txtInputAcidoFosforico, txtInputTTrabajo,
            txtInputTResidencia;
    //Autocomplete
    AutoCompleteTextView txAutoCompleteProductos;
    AutoCompleteTextView txAutoCompleteClientes;
    AutoCompleteTextView txAutoCompleteNroLotes;
    //EditText
    EditText edtTonelada, edtAcidoFosforico, edtTemperaturaTrabajo, edtTResidencia, edtCantidadBatch;
    //conexion Firebase
    DatabaseReference databaseReference;
    FirebaseDatabase firebaseDatabase;
    private FirebaseAuth mAuth;
    FirebaseFirestore fstore;
    FirebaseUser currentUser;
    //Progresor bar
    private ProgressBar progressBar;
    LinearLayout layoutSection1;
    //mosdar no hay Internet
    private LinearLayout imgenUpWifi;
    private Button btnconexionWifi;
    private Button Agregardato;
    //User area
    String userArea;

    //botom para Navegar Badge
    int count = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        getActivity().setTitle(" Producción");
        return inflater.inflate(R.layout.fragment_produccion, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //imagen para que no se vea
        initViewModel(view);
        //Mensaje de no hay Imagen
        UPWifi(view);
        //Datos y variable
        VariablesIngresar(view);
        // FireBaseAutetification
        FirebaseUserDatos();
        checkUserAccessLevel();
        //fechaHora
        //AUTOCOMPLETE textview
        AutoCompleteNroLotes(view);
        AutoCompleteClientes(view);
        AutoCompleteProductos(view);
        registrar_datos_lote(view);
        //mesaje a FireBaseMessaging
        eviarMensaje();
        //Iniciar FireBase
        iniciarFirebase();
    }

    private void eviarMensaje() {
        FirebaseMessaging.getInstance().subscribeToTopic("enviaratodos").addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                //Toast.makeText(getContext(),"Registrado",Toast.LENGTH_SHORT).show();
            }
        });
    }

    //Metodo_buscar Datos
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_agregardatos, menu);
        // MenuItem item = menu.findItem(R.id.search_menuNLote);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_Datos:
                MenuAgregar();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void MenuAgregar() {

        Navigation.findNavController(this.getActivity(), R.id.fragmentContainerView).navigate(R.id.panelAgregarDatos);
        //   Intent intent = new Intent(this.getContext(), PanelAgregarDatos.class);
        //   startActivity(intent);
    }

    private void VariablesIngresar(View view) {
        //FireRegistro
        Registrardatos = view.findViewById(R.id.extended_fab);

        //autocompletetextview
        txAutoCompleteNroLotes = (AutoCompleteTextView) view.findViewById(R.id.dropdownLote);
        txAutoCompleteProductos = (AutoCompleteTextView) view.findViewById(R.id.dropdownProducto);
        txAutoCompleteClientes = (AutoCompleteTextView) view.findViewById(R.id.dropdowncliente);
        // MODO textInputLayout
        edtTonelada = view.findViewById(R.id.edtTon);
        edtCantidadBatch = view.findViewById(R.id.edtLotQuantity);
        edtAcidoFosforico = view.findViewById(R.id.edtPhosphoricAcid);
        edtTemperaturaTrabajo = view.findViewById(R.id.edtWorkingTemperature);
        edtTResidencia = view.findViewById(R.id.edtTHome);
        //TextInputLayout
        txtInputnumeroLote = view.findViewById(R.id.txtInputnumeroLote);
        txtInputproducto = view.findViewById(R.id.txtInputproducto);
        txtInputCliente = view.findViewById(R.id.txtInputCliente);
        txtInputTonelada = view.findViewById(R.id.txtInputTonelada);
        txtInputBatch = view.findViewById(R.id.txtInputBatch);
        txtInputAcidoFosforico = view.findViewById(R.id.txtInputAcidoFosforico);
        txtInputTTrabajo = view.findViewById(R.id.txtInputTTrabajo);
        txtInputTResidencia = view.findViewById(R.id.txtInputTResidencia);
    }

    private void FirebaseUserDatos() {
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        fstore = FirebaseFirestore.getInstance();
    }

    public void registrar_datos_lote(View view) {
        Agregardato = view.findViewById(R.id.extended_fab);
        Agregardato.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Lotes = txAutoCompleteNroLotes.getText().toString();
                String Productos = txAutoCompleteProductos.getText().toString();
                String Clientes = txAutoCompleteClientes.getText().toString();
                String Tonelada = edtTonelada.getText().toString();
                String CantidadBatch = edtCantidadBatch.getText().toString();
                String AcidoFosforico = edtAcidoFosforico.getText().toString();
                String TemperaturaTrabajo = edtTemperaturaTrabajo.getText().toString();
                String TResidencia = edtTResidencia.getText().toString();
                if (Lotes.equalsIgnoreCase("")
                        || Productos.equalsIgnoreCase("")
                        || Clientes.equalsIgnoreCase("")
                        || Tonelada.equalsIgnoreCase("")
                        || CantidadBatch.equalsIgnoreCase("")
                        || AcidoFosforico.equalsIgnoreCase("")
                        || TemperaturaTrabajo.equalsIgnoreCase("")
                        || TResidencia.equalsIgnoreCase("")) {

                    // VALIDAR DATOS
                    validar();
                    ShowSnackbar("Verifique todos los campos");


                } else if (Integer.parseInt(CantidadBatch) < 1
                        || Integer.parseInt(CantidadBatch) > 10
                        || Integer.parseInt(AcidoFosforico) < 5
                        || Integer.parseInt(AcidoFosforico) > 10
                        || Integer.parseInt(TemperaturaTrabajo) < 30
                        || Integer.parseInt(TemperaturaTrabajo) > 60) {
                    // MensajeDatos
                    validar();
                    AlertDialogRangos();
                } else {
                    inserccion();
                    //mensaje de NOTIFICATION POP
                    llamarMessaje();
                    ShowSnackbarBlack("Datos ingresados correctamente");
                    //mensaje de NOTIFICATION POP
                    // llamarMessaje();
                    limpiar();

                }
                EditorTexto();

            }
        });
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

    private void checkUserAccessLevel() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DocumentReference df = fstore.collection("UserColpex").document(user.getUid());
        // extract the data from the document
        df.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Log.d("TAG", "onSuccess: " + Objects.requireNonNull(documentSnapshot.getData()).toString().toLowerCase(Locale.ROOT));
                // identify the user access level
                if (documentSnapshot.getString("isUser") != null) {
                    userArea = "Operador";
                }
                if (documentSnapshot.getString("isadmin") != null) {
                    userArea = "Jefe Producción";
                }
                if (documentSnapshot.getString("isadminGeneral") != null) {

                    userArea = "Jefe General";
                }

            }
        });

    }


    private void inserccion() {
        //bage
        count++;
        //Datos de fecha y hora
        Date date = new Date();
        SimpleDateFormat formatoFecha = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
        String Date = formatoFecha.format(date);
        //datos de Lote
        String Lotes = txAutoCompleteNroLotes.getText().toString();
        String Productos = txAutoCompleteProductos.getText().toString();
        String Clientes = txAutoCompleteClientes.getText().toString();
        String Tonelada = edtTonelada.getText().toString();
        // Integer CantidadBatch = Integer.parseInt(edtCantidadBatch.getText().toString());
        String CantidadBatch = edtCantidadBatch.getText().toString();
        String AcidoFosforico = edtAcidoFosforico.getText().toString();
        String TemperaturaTrabajo = edtTemperaturaTrabajo.getText().toString();
        String TResidencia = edtTResidencia.getText().toString();
        String nombreUsuario = currentUser.getDisplayName().toString();
        String ImgenUri = currentUser.getPhotoUrl().toString();
        //id
        String key = databaseReference.push().getKey();
        // String ImagemUri = userImage.getDrawable().toString();
        Lote l = new Lote();
        l.setId(key);
        l.setNro_lote(Lotes);
        l.setProducto(Productos);
        l.setCliente(Clientes);
        l.setTonelaje(Tonelada);
        l.setBatch(CantidadBatch);
        l.setBatchDesgomado(CantidadBatch);
        l.setBatchNeutralizado(CantidadBatch);
        l.setBatchBlanqueado(CantidadBatch);
        l.setFecha(Date);
        l.setAcido_fosforico(String.valueOf(Double.parseDouble(AcidoFosforico) / 100 * 100));
        l.setTemperatura_trabajo(TemperaturaTrabajo);
        l.setT_residencia(TResidencia);
        l.setCargoArea(userArea);
        l.setBadgenotification(String.valueOf(count));
        l.setUserName(nombreUsuario);
        l.setImagenUrl(ImgenUri);
        databaseReference.child("Lote").child(key).setValue(l);

    }


    private void llamarMessaje() {
        String Lotes = txAutoCompleteNroLotes.getText().toString();
        RequestQueue myrequest = Volley.newRequestQueue(getContext());
        JSONObject json = new JSONObject();
        try {
            //user
            String nombreUsuario = currentUser.getDisplayName().toString();
            String ImgenUri = currentUser.getPhotoUrl().toString();

            json.put("to", "/topics/" + "enviaratodos");
            JSONObject notificacion = new JSONObject();
            notificacion.put("titulo", nombreUsuario + " Jefe Producción");
            notificacion.put("detalle", "Registro Lote");
            notificacion.put("contenido", "Registro número de Lote : " + Lotes);
            notificacion.put("foto", ImgenUri);

            json.put("data", notificacion);
            String URL = "https://fcm.googleapis.com/fcm/send";
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, URL, json, null, null) {
                @Override
                public Map<String, String> getHeaders() {
                    Map<String, String> header = new HashMap<>();
                    header.put("content-type", "application/json");
                    header.put("authorization", "key=AAAA8v7-UtQ:APA91bGbE-ns_ma1M3N-Qa16v-7NJ7kA7e4P4gNykaV7FDl086y0DdJzl-hA5_Um61QHQNIpQmzZxVM_ybcGCe8cA_SN4QiR-eypbxTvS1cFt7bYFSwcupFJi89Eg7nGghw-n7IxixH_");
                    return header;

                }
            };
            myrequest.add(request);


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private void AlertDialogRangos() {
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
                        inserccion();
                        //mensaje de NOTIFICATION POP
                        llamarMessaje();
                        limpiar();
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


    private void AutoCompleteNroLotes(View view) {

        databaseReference = FirebaseDatabase.getInstance().getReference();
        final ArrayAdapter<String> getNameLotNumber = new ArrayAdapter<>(this.getContext(), android.R.layout.simple_list_item_1);
        databaseReference.child("NLotes").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                getNameLotNumber.clear();
                progressBar.setVisibility(View.GONE);
                layoutSection1.setVisibility(View.VISIBLE);
                Agregardato.setVisibility(View.VISIBLE);
                for (DataSnapshot areaSnapshot : dataSnapshot.getChildren()) {
                    String areaName = areaSnapshot.child("nrolote").getValue(String.class);
                    getNameLotNumber.add(areaName);
                    progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        AutoCompleteTextView ACTV = (AutoCompleteTextView) view.findViewById(R.id.dropdownLote);
        ACTV.setAdapter(getNameLotNumber);

    }

    private void AutoCompleteClientes(View view) {

        databaseReference = FirebaseDatabase.getInstance().getReference();
        final ArrayAdapter<String> getNameClient = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1);
        databaseReference.child("Clientes").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                getNameClient.clear();
                layoutSection1.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
                Agregardato.setVisibility(View.VISIBLE);
                for (DataSnapshot areaSnapshot : dataSnapshot.getChildren()) {
                    String areaName = areaSnapshot.child("nombre").getValue(String.class);
                    getNameClient.add(areaName);
                    progressBar.setVisibility(View.GONE);
                    layoutSection1.setVisibility(View.VISIBLE);

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                progressBar.setVisibility(View.GONE);
                layoutSection1.setVisibility(View.VISIBLE);
                Toast.makeText(getContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        AutoCompleteTextView ACTV = (AutoCompleteTextView) view.findViewById(R.id.dropdowncliente);
        ACTV.setAdapter(getNameClient);
    }

    private void AutoCompleteProductos(View view) {

        databaseReference = FirebaseDatabase.getInstance().getReference();
        final ArrayAdapter<String> getNameProduct = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1);
        databaseReference.child("Producto").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                getNameProduct.clear();
                progressBar.setVisibility(View.GONE);
                layoutSection1.setVisibility(View.VISIBLE);
                Agregardato.setVisibility(View.VISIBLE);
                //Agregardato.setEnabled(true);
                for (DataSnapshot areaSnapshot : dataSnapshot.getChildren()) {
                    String areaName = areaSnapshot.child("nombre").getValue(String.class);
                    getNameProduct.add(areaName);
                    progressBar.setVisibility(View.GONE);
                    // layoutSection1.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        AutoCompleteTextView ACTV = (AutoCompleteTextView) view.findViewById(R.id.dropdownProducto);
        ACTV.setAdapter(getNameProduct);

    }

    //Wifi_variables
    private void UPWifi(View view) {
        imgenUpWifi = view.findViewById(R.id.imgenUpWifi);
        btnconexionWifi = view.findViewById(R.id.btnconexionWifi);
        layoutSection1 = view.findViewById(R.id.layoutSection1);
        //Agregardato.setEnabled(false);
    }


    private void EditorTexto() {
        txAutoCompleteNroLotes.addTextChangedListener(new TextWatcher() {
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
        txAutoCompleteProductos.addTextChangedListener(new TextWatcher() {
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
        txAutoCompleteClientes.addTextChangedListener(new TextWatcher() {
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

    private void limpiar() {
        txAutoCompleteNroLotes.setText("");
        txAutoCompleteProductos.setText("");
        txAutoCompleteClientes.setText("");
        edtTonelada.setText("");
        edtCantidadBatch.setText("");
        edtAcidoFosforico.setText("");
        edtTemperaturaTrabajo.setText("");
        edtTResidencia.setText("");
        txAutoCompleteNroLotes.requestFocus();
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
        } else if (AcidoFosforico.isEmpty() || Integer.parseInt(AcidoFosforico) < 5 || Integer.parseInt(AcidoFosforico) > 10) {
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

    private void iniciarFirebase() {
        FirebaseApp.initializeApp(this.getContext());
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
    }

    private void ShowSnackbar(String s) {
        Snackbar snackbar = Snackbar.make(Registrardatos, s, Snackbar.LENGTH_SHORT);
        snackbar.setTextColor(Color.parseColor("#ffffff"));
        snackbar.setBackgroundTint(Color.parseColor("#cc3c3c"));
        snackbar.show();
    }

    private void ShowSnackbarBlack(String s) {
        Snackbar snackbar = Snackbar.make(Registrardatos, s, Snackbar.LENGTH_SHORT);
        snackbar.show();
    }

}