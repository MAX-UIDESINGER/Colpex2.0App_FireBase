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
import com.example.colpex20app_firebase.Adactadores.AdacterNeutralizado.AdacterNeutralizado;
import com.example.colpex20app_firebase.Model.onClickBatch;
import com.example.colpex20app_firebase.PrincipalClasses.ClassNeutralizado.ClaseNeutralizado;
import com.example.colpex20app_firebase.R;
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


public class FragmentNeutralizado extends Fragment implements onClickBatch {
    // texview Neutalizado
    private EditText edtFFAInicialNeutralizado, edtTempTrabajoNeutralizado, edtConcentracionNaOHNeutralizado, edtAceiteNeutroNeutralizado;
    // Inputlayout Neutralizado
    private TextInputLayout txtInputReactorNeutralized, txtInputFFAInitialNeutralized,
            txtInputWorkTemperatureNeutralized, txtInputNaOHNeutralized,
            txtInputLotNaOHNeutralized, txtInputNeutralOilNeutralized;
    //conexion Firebase
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    //Botton registrar Activity
    private Button Registrardatos;
    //TextView Activity
    TextView tvcantbatch;
    //contador para el registro
    int contadorNeutralizado = 0;
    int cantidad_batch = 0;
    int contador = 0;
    int contadorTotal = 0;
    int CantBatch = 0;
    //datos
    String[] data = {"", "", ""};
    int counter = 0;
    //Auto Complete Reactor
    AutoCompleteTextView txAutoCompleteReactor;
    //Auto Complete Lote
    AutoCompleteTextView txAutoCompleteLote;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_neutralizado, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //Adacter
        ListaAdacterNeutralizado(view);
        //traer Batch
        TraerBatch(view);
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
            notificacion.put("detalle", "Registro Neutralizado");
            notificacion.put("contenido", "Registro Neutralizado con el número de Lote : " + Nlote);
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

    private void ListaAdacterNeutralizado(View view) {
        List<String> items = new LinkedList<>();
        items.add("");

        Button Registrar = view.findViewById(R.id.RegistarNeutralizado);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewLista);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        AdacterNeutralizado adapter = new AdacterNeutralizado(items, this);
        recyclerView.setAdapter(adapter);

        String Batch = getActivity().getIntent().getStringExtra("Batch");
        Integer Batch1 = Integer.parseInt(Batch);
        //------------------------------------------------
        String BatchD = getActivity().getIntent().getStringExtra("BatchNeutralizado");
        Integer cantBatchD = Integer.parseInt(BatchD);

        Integer Calcular = Batch1 - cantBatchD;

        Registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (contadorTotal > CantBatch) {
                    new SweetAlertDialog(getContext(), SweetAlertDialog.ERROR_TYPE).setTitleText("Aviso").setContentText("Ya no hay batch para Agregar").show();
                } else {
                    if (counter == Calcular) {
                        new SweetAlertDialog(getContext(), SweetAlertDialog.ERROR_TYPE).setTitleText("Aviso").setContentText("Maximo a la cantida de batch: " + Batch1).show();
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

    }

    @Override
    public void NeutralizadoMenuItem(int position, String item, View itemView) {
        MetodoGuardar(itemView);
    }

    @Override
    public void BlanqueadoMenuItem(int position, String item, View itemView) {

    }

    private void MetodoGuardar(View itemView) {
        //AutoCompleteTextView
        txAutoCompleteReactor = itemView.findViewById(R.id.edtReactorNeutralized);
        txAutoCompleteLote = itemView.findViewById(R.id.edtLotNaOHNeutralized);
        //texview Neutralizado
        edtFFAInicialNeutralizado = itemView.findViewById(R.id.edtFFAInitialNeutralized);
        edtTempTrabajoNeutralizado = itemView.findViewById(R.id.edtWorkTemperatureNeutralized);
        edtConcentracionNaOHNeutralizado = itemView.findViewById(R.id.edtNaOHNeutralized);
        edtAceiteNeutroNeutralizado = itemView.findViewById(R.id.edtNeutralOilNeutralized);
        //----------------------------------------------------
        txtInputReactorNeutralized = itemView.findViewById(R.id.txtInputReactorNeutralized);
        txtInputFFAInitialNeutralized = itemView.findViewById(R.id.txtInputFFAInitialNeutralized);
        txtInputWorkTemperatureNeutralized = itemView.findViewById(R.id.txtInputWorkTemperatureNeutralized);
        txtInputNaOHNeutralized = itemView.findViewById(R.id.txtInputNaOHNeutralized);
        txtInputLotNaOHNeutralized = itemView.findViewById(R.id.txtInputLotNaOHNeutralized);
        txtInputNeutralOilNeutralized = itemView.findViewById(R.id.txtInputNeutralOilNeutralized);
        //----------------------------------------------------
        Registrardatos = itemView.findViewById(R.id.btnRegistate);

        //----------------------------------------------------
        if (contadorTotal > CantBatch) {
            new SweetAlertDialog(getContext(), SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("Aviso")
                    .setContentText("Ya no hay batch para Agregar")
                    .show();
        } else {
            String Reactor = txAutoCompleteReactor.getText().toString();
            String FFAInicial = edtFFAInicialNeutralizado.getText().toString();
            String TempTrabajo = edtTempTrabajoNeutralizado.getText().toString();
            String ConcentracionNaOH = edtConcentracionNaOHNeutralizado.getText().toString();
            String LoteNaOH = txAutoCompleteLote.getText().toString();
            String AceiteNeutro = edtAceiteNeutroNeutralizado.getText().toString();
            if (Reactor.isEmpty() || FFAInicial.isEmpty() || TempTrabajo.isEmpty()
                    || ConcentracionNaOH.isEmpty() || LoteNaOH.isEmpty() || AceiteNeutro.isEmpty()) {
                //ventana de avisomalo
                validar_neutralizado();
                ShowSnackbar("Por favor, complete todos los campos");
            } else if (Integer.parseInt(ConcentracionNaOH) < 14 || Integer.parseInt(ConcentracionNaOH) > 20 || Double.parseDouble(TempTrabajo) > 65) {
                validar_neutralizado();
                AlertDialogRangos();
            } else if (Integer.parseInt(ConcentracionNaOH) < 14 || Integer.parseInt(ConcentracionNaOH) > 20) {
                //ventana de avisomalo
                Showverificado("Aviso Concentracion NaOH (min 14% o 20º Be)");
                validar_neutralizado();

            } else if (Double.parseDouble(TempTrabajo) > 65) {
                //ventana de avisomalo
                Showverificado("Aviso Temp. Trabajo (Max. 65 º C)");
                validar_neutralizado();
            } else {
                inserccionNeutralizado(Reactor, FFAInicial, TempTrabajo, LoteNaOH, AceiteNeutro);
                ContadortotalBatchReducir();
                llamarMessaje();
                actualizarBatch();
                BatchCalcular();
                Showverificado("Neutralizado Registrado correctamente");
                if (contadorTotal > CantBatch) {
                    //ventana de aviso
                    new SweetAlertDialog(getContext(), SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Aviso")
                            .setContentText("Ya no hay batch para Agregar")
                            .show();
                    RegistroDesabilitado();
                }
            }
            EditorTexto_neutralizado();
        }
    }

    private void BatchCalcular() {
        String Batch = getActivity().getIntent().getStringExtra("Batch");
        CantBatch = Integer.parseInt(Batch);
    }

    private void ContadortotalBatchReducir() {
        contadorNeutralizado--;
        contador = (contadorNeutralizado);
        PreferenciaBatch();
        SharedPreferences preferences = getActivity().getSharedPreferences("GuardarBatchNutralizado", Context.MODE_PRIVATE);
        String ContadorTotal = preferences.getString("contadorTotal", "");
        tvcantbatch.setText(ContadorTotal);
    }

    @SuppressLint("ApplySharedPref")
    private void PreferenciaBatch() {
        SharedPreferences preferences = this.getActivity().getSharedPreferences("GuardarBatchNutralizado1", Context.MODE_PRIVATE);
        String CantidadBatch = preferences.getString("DataBatch", "");
        int cantidad_batch = Integer.parseInt(CantidadBatch);
        //Editor
        contadorTotal = cantidad_batch - contador;
        //Traer el dato del Bacht en sharedPreferent
        SharedPreferences ContadorTotal = this.getActivity().getSharedPreferences("GuardarBatchNutralizado", Context.MODE_PRIVATE);
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
                .child(key).child("batchNeutralizado")
                .setValue(tvcantbatch.getText().toString());
    }

    @SuppressLint("SetTextI18n")
    private void TraerBatch(View view) {
        tvcantbatch = getActivity().findViewById(R.id.edtCantBatchNeutralizado);
        String Batch = getActivity().getIntent().getStringExtra("BatchNeutralizado");
        // se almacenara el dato en TexView
        tvcantbatch.setText(Batch + "");
        cantidad_batch = Integer.parseInt(Batch);
        //se le asigna el valor de Batch y guardarlo en una preferencia
        SharedPreferences preferences = getActivity().getSharedPreferences("GuardarBatchNutralizado1", Context.MODE_PRIVATE);
        String CantidadBatch = String.valueOf(cantidad_batch);
        //Editor
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("DataBatch", CantidadBatch);
        editor.commit();
    }

    private void AlertDialogRangos() {
        String Reactor = txAutoCompleteReactor.getText().toString();
        String FFAInicial = edtFFAInicialNeutralizado.getText().toString();
        String TempTrabajo = edtTempTrabajoNeutralizado.getText().toString();
        String ConcentracionNaOH = edtConcentracionNaOHNeutralizado.getText().toString();
        String LoteNaOH = txAutoCompleteLote.getText().toString();
        String AceiteNeutro = edtAceiteNeutroNeutralizado.getText().toString();

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
                        inserccionNeutralizado(Reactor, FFAInicial, TempTrabajo, LoteNaOH, AceiteNeutro);
                        ContadortotalBatchReducir();
                        limpiar_neutralizado();
                        //mensaje de NOTIFICATION POP
                        llamarMessaje();
                        actualizarBatch();
                        BatchCalcular();
                        Showverificado("Neutralizado Registrado correctamente");
                        sDialog.dismissWithAnimation();
                        if (contadorTotal > CantBatch) {
                            //ventana de aviso
                            new SweetAlertDialog(getContext(), SweetAlertDialog.ERROR_TYPE).setTitleText("Aviso").setContentText("Ya no hay batch para Agregar").show();
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
        EditorTexto_neutralizado();
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

        txAutoCompleteReactor.setEnabled(false);
        edtFFAInicialNeutralizado.setEnabled(false);
        edtTempTrabajoNeutralizado.setEnabled(false);
        edtConcentracionNaOHNeutralizado.setEnabled(false);
        txAutoCompleteLote.setEnabled(false);
        edtAceiteNeutroNeutralizado.setEnabled(false);

        txtInputReactorNeutralized.setEnabled(false);
        txtInputFFAInitialNeutralized.setEnabled(false);
        txtInputWorkTemperatureNeutralized.setEnabled(false);
        txtInputNaOHNeutralized.setEnabled(false);
        txtInputLotNaOHNeutralized.setEnabled(false);
        txtInputNeutralOilNeutralized.setEnabled(false);

    }

    private void inserccionNeutralizado(String reactor, String FFAInicial, String tempTrabajo, String loteNaOH, String aceiteNeutro) {
        //Datos de fecha y hora
        Date date = new Date();
        SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
        String Date = formatoFecha.format(date);
        //datos de Neutralizado
        String Nlote = getActivity().getIntent().getStringExtra("NumeroLote");
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

        //id
        String key = databaseReference.push().getKey();
        //user
        String nombreUsuario = currentUser.getDisplayName().toString();
        String ImgenUri = currentUser.getPhotoUrl().toString();
        //Batch
        String CantBatch = tvcantbatch.getText().toString();

        //  String notification = notificationapp;

        ClaseNeutralizado n = new ClaseNeutralizado();
        n.setId(key);
        //n.setContador(notification);
        n.setNumeroLote(Nlote);
        n.setReactor(reactor);
        n.setContadorBatch(CantBatch);
        n.setFFAInicial(FFAInicial);
        n.setTempTrabajo(tempTrabajo);
        n.setConcentracionNaOH(concentracion);
        n.setPorsentaje(concentracionNaOH);
        n.setLoteNAOH(loteNaOH);
        n.setAceiteNeutro(aceiteNeutro);
        n.setUserName(nombreUsuario);
        n.setImagenUrl(ImgenUri);
        n.setFechaHora(Date);
        databaseReference.child("Neutralizado").child(Nlote).child(key).setValue(n);
        limpiar_neutralizado();
    }

    private void limpiar_neutralizado() {
        txAutoCompleteReactor.setText("");
        edtFFAInicialNeutralizado.setText("");
        edtTempTrabajoNeutralizado.setText("");
        edtConcentracionNaOHNeutralizado.setText("");
        txAutoCompleteLote.setText("");
        edtAceiteNeutroNeutralizado.setText("");
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