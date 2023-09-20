package com.example.colpex20app_firebase.OperadorResponsable.AgregarDatos;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import com.example.colpex20app_firebase.Adactadores.AdacterDesgomado.AdacterDesgomado;
import com.example.colpex20app_firebase.Model.onClickBatch;
import com.example.colpex20app_firebase.PrincipalClasses.ClassDesgomado.ClaseDesgomado;
import com.example.colpex20app_firebase.R;;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class FragmentDesgomado extends Fragment implements onClickBatch {
    // texview Desgomado
    EditText edtTonelajeDesgomado, edtAcidoFosforicoDesgomado, edtTempTrabajoDesgomado, edtTResidenciaDesgomado;
    // Inputlayout Desgomado
    private TextInputLayout txtInputReactorDegummed, txtInputTonDegummed, txtInputPhosphoricAcidDegummed,
            txtInputLotDegummed, txtInputWorkTemperatureDegummed, txtInputHomeDegummed;
    //conexion Firebase
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    private FirebaseAuth mAuth;
    FirebaseUser currentUser;
    //Botton registrar Activity
    private Button Registrardatos;
    //TextView Activity Batch
    TextView tvcantbatch;
    //contador para el registro
    int contadorDesgomado = 0;
    int cantidad_batch = 0;
    int contador = 0;
    int contadorTotal = 0;
    int CantBatch = 0;
    //dato
    String[] data = {"", "", ""};
    int counter = 0;
    //Auto Complete Reactor
    AutoCompleteTextView txAutoCompleteReactor;
    //Auto Complete Lote
    AutoCompleteTextView txAutoCompleteLote;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_desgomado, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //Adacter
        ListaAdacterDesgomado(view);
        //traer Batch
        TraerBatch();
        //Firebase Autification
        FirebaseUserDatos();
        //mesaje a FireBaseMessaging
        eviarMensaje();
        //Iniciar FireBase
        iniciarFirebase();
    }
    private void eviarMensaje() {
        FirebaseMessaging.getInstance().subscribeToTopic("enviaratodos");
    }

    private void llamarMessaje() {
        String Nlote = getActivity().getIntent().getStringExtra("NumeroLote");
        RequestQueue myrequest = Volley.newRequestQueue(getContext());
        JSONObject json = new JSONObject();
        try {
            //user
            String nombreUsuario = currentUser.getDisplayName().toString();
            String ImgenUri = currentUser.getPhotoUrl().toString();

            json.put("to", "/topics/" + "enviaratodos");
            JSONObject notificacion = new JSONObject();
            notificacion.put("titulo", nombreUsuario + " Operador");
            notificacion.put("detalle", "Registro Desgomado");
            notificacion.put("contenido", "Registro Desgomado con el número de Lote : " + Nlote);
            notificacion.put("foto", ImgenUri);

            json.put("data", notificacion);
            String URL = "https://fcm.googleapis.com/fcm/send";
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, URL, json, null, null) {
                @Override
                public Map<String, String> getHeaders() {
                    Map<String, String> header = new HashMap<>();
                    header.put("content-type", "application/json");
                    header.put("authorization", "key=AAAA2grOCOk:APA91bEmvbRW8ohijXDyx8J8-mbIWW2nb_L5Fr9YpkEk-a2cXypR6kKVAEhkB1xzCwDwkKBlJq1OTb1XF_h52k-NZh31JWF9YcegoyyIAIHBi6ABUECX2FzfP5Ak6xxyDwrVH4XJLcqI");
                    return header;

                }
            };
            myrequest.add(request);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void ListaAdacterDesgomado(View view) {
        List<String> items = new LinkedList<>();
        items.add("");

        Button Registrar = view.findViewById(R.id.RegistarDesgomado);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewLista);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        AdacterDesgomado adapter = new AdacterDesgomado(items, this);
        recyclerView.setAdapter(adapter);

        String Batch = getActivity().getIntent().getStringExtra("Batch");
        Integer Batch1 = Integer.parseInt(Batch);
        //------------------------------------------------
        String BatchD = getActivity().getIntent().getStringExtra("BatchDesgomado");
        Integer cantBatchD = Integer.parseInt(BatchD);

        Integer Calcular = Batch1 - cantBatchD;

        Registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (contadorTotal > CantBatch) {
                    new SweetAlertDialog(getContext(), SweetAlertDialog.ERROR_TYPE).setTitleText("Aviso").setContentText("Ya no hay batch para Agregar").show();
                } else {
                    if (counter == Calcular) {
                        new SweetAlertDialog(getContext(), SweetAlertDialog.ERROR_TYPE).setTitleText("Aviso").setContentText("Maximo a la cantida de batch: " + Calcular).show();
                    } else {
                        items.add(data[counter % 3]);
                        counter++;
                        adapter.notifyItemInserted(items.size() - 1);
                    }

                }

            }
        });

    }

    @Override
    public void DesgomadoMenuItem(int position, String item, View itemView) {
        MetodoGuardar(itemView);
    }

    @Override
    public void NeutralizadoMenuItem(int position, String item, View itemView) {

    }

    @Override
    public void BlanqueadoMenuItem(int position, String item, View itemView) {
    }

    private void MetodoGuardar(View itemView) {
        //AutoCompleteTextView
        txAutoCompleteReactor = itemView.findViewById(R.id.edtReactorDegummed);
        txAutoCompleteLote = itemView.findViewById(R.id.edtLotDegummed);
        //texview Desgomado
        edtTonelajeDesgomado = itemView.findViewById(R.id.edtTonDegummed);
        edtAcidoFosforicoDesgomado = itemView.findViewById(R.id.edtPhosphoricAcidDegummed);
        edtTempTrabajoDesgomado = itemView.findViewById(R.id.edtWorkTemperatureDegummed);
        edtTResidenciaDesgomado = itemView.findViewById(R.id.edtHomeDegummed);
        //----------------------------------------------------
        txtInputReactorDegummed = itemView.findViewById(R.id.txtInputReactorDegummed);
        txtInputTonDegummed = itemView.findViewById(R.id.txtInputTonDegummed);
        txtInputPhosphoricAcidDegummed = itemView.findViewById(R.id.txtInputPhosphoricAcidDegummed);
        txtInputLotDegummed = itemView.findViewById(R.id.txtInputLotDegummed);
        txtInputWorkTemperatureDegummed = itemView.findViewById(R.id.txtInputWorkTemperatureDegummed);
        txtInputHomeDegummed = itemView.findViewById(R.id.txtInputHomeDegummed);
        //----------------------------------------------------
        Registrardatos = itemView.findViewById(R.id.btnRegistate);

        if (contadorTotal > CantBatch) {
            new SweetAlertDialog(getContext(), SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("Aviso")
                    .setContentText("Ya no hay batch para Agregar")
                    .show();
        } else {

            String Reactor = txAutoCompleteReactor.getText().toString();
            String Tonelaje = edtTonelajeDesgomado.getText().toString();
            String AcidoFosforico = edtAcidoFosforicoDesgomado.getText().toString();
            String Lote = txAutoCompleteLote.getText().toString();
            String TempTrabajo = edtTempTrabajoDesgomado.getText().toString();
            String TResidencia = edtTResidenciaDesgomado.getText().toString();
            double acidofosfori = Double.parseDouble(AcidoFosforico);
            Integer tempTrab = Integer.parseInt(TempTrabajo);
            if (Reactor.isEmpty() || Tonelaje.isEmpty() || AcidoFosforico.isEmpty()
                    || Lote.isEmpty() || TempTrabajo.isEmpty() || TResidencia.isEmpty()) {
                //ventana de avisomalo
                validar_desgomado();
                //ventana de avisomalo
                ShowSnackbar("Por favor, complete todos los campos");
            } else if (acidofosfori < 5 || acidofosfori > 10
                    || tempTrab < 30 || tempTrab > 60) {
                AlertDialogRangos();
                validar_desgomado();
            } else if (acidofosfori < 5 || acidofosfori > 10) {
                //ventana de avisomalo
                Showverificado("Aviso Acido Fosfórico (0,05% - 0,10%)");
                validar_desgomado();
            } else if (tempTrab < 30 || tempTrab > 60) {
                Showverificado("Aviso Temp. Trabajo (30º - 60º C)");
                validar_desgomado();
            } else {
                inserccionDesgomado(Reactor, Tonelaje, AcidoFosforico, Lote, TempTrabajo, TResidencia);
                ContadortotalBatchReducir();
                llamarMessaje();
                actualizarBatch();
                BatchCalcular();
                Showverificado("Desgomado Registrado correctamente");
                if (contadorTotal > CantBatch) {
                    //ventana de aviso
                    new SweetAlertDialog(getContext(), SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Aviso")
                            .setContentText("Ya no hay batch para Agregar")
                            .show();
                    RegistroDesabilitado();
                }
            }
            EditorTexto_desgomado();
        }
    }

    private void BatchCalcular() {
        String Batch = getActivity().getIntent().getStringExtra("Batch");
        CantBatch = Integer.parseInt(Batch);
    }

    private void ContadortotalBatchReducir() {
        contadorDesgomado--;
        contador = (contadorDesgomado);
        PreferenciaBatch();
        SharedPreferences preferences = getActivity().getSharedPreferences("GuardarBatchDesgomado", Context.MODE_PRIVATE);
        String ContadorTotal = preferences.getString("contadorTotal", "");
        tvcantbatch.setText(ContadorTotal);
    }

    @SuppressLint("ApplySharedPref")
    private void PreferenciaBatch() {
        SharedPreferences preferences = this.getActivity().getSharedPreferences("GuardarBatchDesgomado1", Context.MODE_PRIVATE);
        String CantidadBatch = preferences.getString("DataBatch", "");
        int cantidad_batch = Integer.parseInt(CantidadBatch);
        //Editor
        contadorTotal = cantidad_batch - contador;
        //Traer el dato del Bacht en sharedPreferent
        SharedPreferences ContadorTotal = this.getActivity().getSharedPreferences("GuardarBatchDesgomado", Context.MODE_PRIVATE);
        //Calcular y Restrar Batrch en una referencia
        String contadortotal = String.valueOf(contadorTotal);
        //Editor
        SharedPreferences.Editor editor = ContadorTotal.edit();
        editor.putString("contadorTotal", contadortotal);
        editor.commit();
    }

    private void actualizarBatch() {
        String key = getActivity().getIntent().getStringExtra("id");
        databaseReference.child("Lote")
                .child(key).child("batchDesgomado")
                .setValue(tvcantbatch.getText().toString());
    }

    @SuppressLint("ApplySharedPref")
    private void TraerBatch() {
        tvcantbatch = getActivity().findViewById(R.id.edtCantBatchDesgomado);
        String Batch = getActivity().getIntent().getStringExtra("BatchDesgomado");
        // se almacenara el dato en TexView
        tvcantbatch.setText(Batch + "");
        cantidad_batch = Integer.parseInt(Batch);
        //se le asigna el valor de Batch y guardarlo en una preferencia
        SharedPreferences preferences = getActivity().getSharedPreferences("GuardarBatchDesgomado1", Context.MODE_PRIVATE);
        String CantidadBatch = String.valueOf(cantidad_batch);
        //Editor
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("DataBatch", CantidadBatch);
        editor.commit();
    }

    private void AlertDialogRangos() {
        String Reactor = txAutoCompleteReactor.getText().toString();
        String Tonelaje = edtTonelajeDesgomado.getText().toString();
        String AcidoFosforico = edtAcidoFosforicoDesgomado.getText().toString();
        String Lote = txAutoCompleteLote.getText().toString();
        String TempTrabajo = edtTempTrabajoDesgomado.getText().toString();
        String TResidencia = edtTResidenciaDesgomado.getText().toString();

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
                        validar_desgomado();
                        inserccionDesgomado(Reactor, Tonelaje, AcidoFosforico, Lote, TempTrabajo, TResidencia);
                        ContadortotalBatchReducir();
                        //mensaje de NOTIFICATION POP
                        llamarMessaje();
                        actualizarBatch();
                        BatchCalcular();
                        Showverificado("Neutralizado Registrado correctamente");
                        sDialog.dismissWithAnimation();
                        if (contadorTotal > CantBatch) {
                            //ventana de aviso
                            new SweetAlertDialog(getContext(), SweetAlertDialog.ERROR_TYPE)
                                    .setTitleText("Aviso")
                                    .setContentText("Ya no hay batch para Agregar")
                                    .show();
                            RegistroDesabilitado();
                        }
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
        EditorTexto_desgomado();
    }

    private void iniciarFirebase() {
        FirebaseApp.initializeApp(this.getContext());
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
    }

    private void FirebaseUserDatos() {
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
    }

    private void RegistroDesabilitado() {
        Registrardatos.setEnabled(false);

        edtAcidoFosforicoDesgomado.setText("");
        edtTempTrabajoDesgomado.setText("");
        edtTResidenciaDesgomado.setText("");

        txtInputReactorDegummed.setEnabled(false);
        txtInputTonDegummed.setEnabled(false);
        txtInputPhosphoricAcidDegummed.setEnabled(false);
        txtInputLotDegummed.setEnabled(false);
        txtInputWorkTemperatureDegummed.setEnabled(false);
        txtInputHomeDegummed.setEnabled(false);

        txAutoCompleteReactor.setEnabled(false);
        edtTonelajeDesgomado.setEnabled(false);
        edtAcidoFosforicoDesgomado.setEnabled(false);
        txAutoCompleteLote.setEnabled(false);
        edtTempTrabajoDesgomado.setEnabled(false);
        edtTResidenciaDesgomado.setEnabled(false);

    }

    private void inserccionDesgomado(String reactor, String tonelaje, String acidoFosforico, String lote, String tempTrabajo, String TResidencia) {
        //Datos de fecha y hora
        Date date = new Date();
        SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
        String Date = formatoFecha.format(date);
        //datos de Desgomado
        String Nlote = getActivity().getIntent().getStringExtra("NumeroLote");
        //id
        //  String key = getActivity().getIntent().getStringExtra("id");
        String key1 = databaseReference.push().getKey();
        String key = databaseReference.push().getKey();
        //user
        String nombreUsuario = currentUser.getDisplayName().toString();
        String ImgenUri = currentUser.getPhotoUrl().toString();
        //Batch
        String CantBatch = tvcantbatch.getText().toString();
        //varibles de insercion set
        ClaseDesgomado d = new ClaseDesgomado();
        // ItemDesgomado i = new ItemDesgomado();
        d.setId(key);
        //i.setItemTitleLote(Nlote);
        d.setNumeroLote(Nlote);
        d.setReactor(reactor);
        d.setContadorBatch(CantBatch);
        d.setTonelaje(tonelaje);
        d.setAcidoFosforico(acidoFosforico);
        d.setLoteacido(lote);
        d.setTempTrabajo(tempTrabajo);
        d.setTResidencia(TResidencia);
        d.setUserName(nombreUsuario);
        d.setImagenUrl(ImgenUri);
        d.setFechaHora(Date);
        databaseReference.child("Desgomado").child(Nlote).child(key).setValue(d);
        limpiar_desgomado();
    }

    private void limpiar_desgomado() {
        txAutoCompleteReactor.setText("");
        edtTonelajeDesgomado.setText("");
        txAutoCompleteLote.setText("");
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
            txtInputPhosphoricAcidDegummed.setError("Ingresar su Acido Fosfórico");
            retorno = false;
        } else if (Double.parseDouble(AcidoFosforico) < 5 || Double.parseDouble(AcidoFosforico) > 10) {
            txtInputPhosphoricAcidDegummed.setError("Fuera de Rango (0,05% - 0,10%)");
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
            txtInputWorkTemperatureDegummed.setError("Ingresar su Temp. Trabajo");
            retorno = false;
        } else if (Integer.parseInt(TempTrabajo) < 30 || Integer.parseInt(TempTrabajo) > 60) {
            txtInputWorkTemperatureDegummed.setError("Fuera de Rango (30º - 60º C)");
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

    private void ShowSnackbar(String s) {
        Snackbar snackbar = Snackbar.make(Registrardatos, s, Snackbar.LENGTH_SHORT);
        snackbar.setTextColor(Color.parseColor("#ffffff"));
        snackbar.setBackgroundTint(Color.parseColor("#cc3c3c"));
        snackbar.show();
    }

    private void Showverificado(String s) {
        Snackbar snackbar = Snackbar.make(Registrardatos, s, Snackbar.LENGTH_SHORT);
        snackbar.show();
    }

}