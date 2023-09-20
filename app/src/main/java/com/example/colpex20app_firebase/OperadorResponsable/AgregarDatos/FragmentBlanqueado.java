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
import com.example.colpex20app_firebase.Adactadores.AdacterBlanqueado.AdacterBlanqueado;
import com.example.colpex20app_firebase.Model.onClickBatch;
import com.example.colpex20app_firebase.PrincipalClasses.ClassBlanqueado.ClaseBlanqueado;
import com.example.colpex20app_firebase.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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


public class FragmentBlanqueado extends Fragment implements onClickBatch {
    // texview Blanqueado
    EditText edtCargaBlanqueado, edtVacioBlanqueado, edtTempTrabajoBlanqueado, edtArcillaBlanqueado,
            edtCarbonActivadoBlanqueado, edtSiliceBlanqueado, edtFFAFinalBlanqueado,
            edtColorFinalBlanqueado, edtAndisidinaFinalBlanqueado;

    // Inputlayout BLANQUEDO
    TextInputLayout txtInputReactorBleached, txtInputLoadBleached, txtInputEmpty,
            txtInputWorkTemperatureBleached, txtInputClayBleached, txtInputLotClayBleached,
            txtInputActivatedCarbonBleached, txtInputLotActivatedCarbonBleached,
            txtInputSilicaBleached, txtInputLotSilicaBleached, txtInputFFAFinalBleached,
            txtInputColorFinalBleached, txtInputAnisidineFinalBleached;

    //conexion Firebase
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;

    //Botton registrar Activity
    private Button Registrardatos;
    //  TextView Activity
    TextView tvcantbatch;

    //contador para el registro
    int contadorBlanqueado = 0;
    int cantidad_batch = 0;
    int contador = 0;
    int contadorTotal = 0;
    int CantBatch = 0;
    //dato
    String[] data = {"", "", ""};
    int counter = 0;

    //Auto Complete Reactor
    AutoCompleteTextView txAutoCompleteReactor;
    //Auto Complete Lote Arcilla
    AutoCompleteTextView txAutoCompleteLoteArcilla;
    //Auto Complete Lote Carbon Activado
    AutoCompleteTextView txAutoCompleteLoteCarbActiva;
    //Auto Complete Lote Silice
    AutoCompleteTextView txAutoCompleteLoteSilice;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_blanqueado, container, false);
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
            notificacion.put("detalle", "Registro Blanqueado");
            notificacion.put("contenido", "Registro Blanqueado con el número de Lote : " + Nlote);
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

        Button Registrar = view.findViewById(R.id.RegistarBlanqueado);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewLista);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        AdacterBlanqueado adapter = new AdacterBlanqueado(items, this);
        recyclerView.setAdapter(adapter);

        String Batch = getActivity().getIntent().getStringExtra("Batch");
        Integer Batch1 = Integer.parseInt(Batch);
        //------------------------------------------------
        String BatchD = getActivity().getIntent().getStringExtra("BatchBlanqueado");
        Integer cantBatchD = Integer.parseInt(BatchD);

        Integer Calcular = Batch1 - cantBatchD;

        Registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cantBatchD > Batch1) {
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

    }

    @Override
    public void BlanqueadoMenuItem(int position, String item, View itemView) {
        MetodoGuardar(itemView);
    }

    private void MetodoGuardar(View itemView) {
        //AutoCompleteTextView
        txAutoCompleteReactor = itemView.findViewById(R.id.edtReactorBleached);
        // lote arcilla
        txAutoCompleteLoteArcilla = itemView.findViewById(R.id.edtLotClayBleached);
        //lote carbon Activado
        txAutoCompleteLoteCarbActiva = itemView.findViewById(R.id.edtLotActivatedCarbonBleached);
        //lote silice
        txAutoCompleteLoteSilice = itemView.findViewById(R.id.edtLotSilicaBleached);
        //texview Blanqueado
        edtCargaBlanqueado = itemView.findViewById(R.id.edtLoadBleached);
        edtVacioBlanqueado = itemView.findViewById(R.id.edtEmptyBleached);
        edtTempTrabajoBlanqueado = itemView.findViewById(R.id.edtWorkTemperatureBleached);
        edtArcillaBlanqueado = itemView.findViewById(R.id.edtClayBleached);
        edtCarbonActivadoBlanqueado = itemView.findViewById(R.id.edtActivatedCarbonBleached);
        edtSiliceBlanqueado = itemView.findViewById(R.id.edtSilicaBleached);
        edtFFAFinalBlanqueado = itemView.findViewById(R.id.edtFFAFinalBleached);
        edtColorFinalBlanqueado = itemView.findViewById(R.id.edtColorFinalBleached);
        edtAndisidinaFinalBlanqueado = itemView.findViewById(R.id.edtAnisidineFinalBleached);
        //----------------------------------------------------
        txtInputReactorBleached = itemView.findViewById(R.id.txtInputReactorBleached);
        txtInputLoadBleached = itemView.findViewById(R.id.txtInputLoadBleached);
        txtInputEmpty = itemView.findViewById(R.id.txtInputEmpty);
        txtInputWorkTemperatureBleached = itemView.findViewById(R.id.txtInputWorkTemperatureBleached);
        txtInputClayBleached = itemView.findViewById(R.id.txtInputClayBleached);
        txtInputLotClayBleached = itemView.findViewById(R.id.txtInputLotClayBleached);
        txtInputActivatedCarbonBleached = itemView.findViewById(R.id.txtInputActivatedCarbonBleached);
        txtInputLotActivatedCarbonBleached = itemView.findViewById(R.id.txtInputLotActivatedCarbonBleached);
        txtInputSilicaBleached = itemView.findViewById(R.id.txtInputSilicaBleached);
        txtInputLotSilicaBleached = itemView.findViewById(R.id.txtInputLotSilicaBleached);
        txtInputFFAFinalBleached = itemView.findViewById(R.id.txtInputFFAFinalBleached);
        txtInputColorFinalBleached = itemView.findViewById(R.id.txtInputColorFinalBleached);
        txtInputAnisidineFinalBleached = itemView.findViewById(R.id.txtInputAnisidineFinalBleached);
        //----------------------------------------------------
        Registrardatos = itemView.findViewById(R.id.btnRegistate);

        if (contadorTotal > CantBatch) {
            new SweetAlertDialog(getContext(), SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("Aviso")
                    .setContentText("Ya no hay batch para Agregar").show();
        } else {

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

            }  else if (Integer.parseInt(Vacio) < 20 || Integer.parseInt(Vacio) > 25
                    || Integer.parseInt(TempTrabajo) < 90 || Integer.parseInt(TempTrabajo) > 105
                    || Integer.parseInt(Arcilla) / 100 < 3
                    || Double.parseDouble(CarbonActivado) / 100 < 0.5 || Double.parseDouble(CarbonActivado) / 100 > 1
                    || Double.parseDouble(Silice) / 100 < 0.05 || Double.parseDouble(Silice) / 100 > 0.2) {

                validar_blanqueado();
                AlertDialogRangos();
            } else if (Integer.parseInt(Vacio) < 20 || Integer.parseInt(Vacio) > 25) {
                //ventana de avisomalo
                Showverificado("Aviso Vacio (20 - 25 pul Hg)");
                validar_blanqueado();
            } else if (Integer.parseInt(TempTrabajo) < 90 || Integer.parseInt(TempTrabajo) > 105) {
                //ventana de avisomalo
                Showverificado("Aviso Temp. Trabajo (90º - 105º C)");
                validar_blanqueado();
            } else if (Integer.parseInt(Arcilla) / 100 < 3) {
                //ventana de avisomalo
                Showverificado("Aviso Arcilla (Min 3%)");
                validar_blanqueado();
            } else if (Double.parseDouble(CarbonActivado) / 100 < 0.5 || Double.parseDouble(CarbonActivado) / 100 > 1) {
                //ventana de avisomalo
                Showverificado("Aviso Carbon Activado (0,5 - 1 %)");
                validar_blanqueado();
            } else if (Double.parseDouble(Silice) / 100 < 0.05 || Double.parseDouble(Silice) / 100 > 0.2) {
                //ventana de avisomalo
                Showverificado("Aviso Silice (0,05 - 0,2 %)");
                validar_blanqueado();
            } else {
                inserccionBlanqueado(Reactor, Carga, Vacio, TempTrabajo, Arcilla,
                        ALLote, CarbonActivado, CALote, Silice, SLote, FFAFinal, ColorFinal, AndisidinaFinal);
                ContadortotalBatchReducir();
                //mensaje de NOTIFICATION POP
                llamarMessaje();
                actualizarBatch();
                BatchCalcular();
                Showverificado("Blanqueado Registrado correctamente");
                if (contadorTotal > CantBatch) {
                    //ventana de aviso
                    new SweetAlertDialog(getContext(), SweetAlertDialog.ERROR_TYPE).setTitleText("Aviso").setContentText("Ya no hay batch para Agregar").show();
                    RegistroDesabilitado();
                }
            }
            EditorTexto_blanqueado();
        }
    }

    private void BatchCalcular() {
        String Batch = getActivity().getIntent().getStringExtra("Batch");
        CantBatch = Integer.parseInt(Batch);
    }

    private void ContadortotalBatchReducir() {
        contadorBlanqueado--;
        contador = contadorBlanqueado;
        PreferenciaBatch();
        SharedPreferences preferences = getActivity().getSharedPreferences("GuardarBatchBlanqueado", Context.MODE_PRIVATE);
        String ContadorTotal = preferences.getString("contadorTotal", "");
        tvcantbatch.setText(ContadorTotal);
    }

    @SuppressLint("ApplySharedPref")
    private void PreferenciaBatch() {
        SharedPreferences preferences = this.getActivity().getSharedPreferences("GuardarBatchBlanqueado1", Context.MODE_PRIVATE);
        String CantidadBatch = preferences.getString("DataBatch", "");
        int cantidad_batch = Integer.parseInt(CantidadBatch);
        //Editor
        contadorTotal = cantidad_batch - contador;
        //Traer el dato del Bacht en sharedPreferent
        SharedPreferences ContadorTotal = this.getActivity().getSharedPreferences("GuardarBatchBlanqueado", Context.MODE_PRIVATE);
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
                .child(key).child("batchBlanqueado")
                .setValue(tvcantbatch.getText().toString());
    }

    @SuppressLint("SetTextI18n")
    private void TraerBatch() {
        tvcantbatch = getActivity().findViewById(R.id.edtCantBatchBlanqueado);
        String Batch = getActivity().getIntent().getStringExtra("BatchBlanqueado");
        tvcantbatch.setText(Batch + "");
        //se le asigna el valor de dato 2
        cantidad_batch = Integer.parseInt(Batch);
        //se le asigna el valor de Batch y guardarlo en una preferencia
        SharedPreferences preferences = getActivity().getSharedPreferences("GuardarBatchBlanqueado1", Context.MODE_PRIVATE);
        String CantidadBatch = String.valueOf(cantidad_batch);
        //Editor
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("DataBatch", CantidadBatch);
        editor.commit();
    }

    private void AlertDialogRangos() {
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
                        validar_blanqueado();
                        inserccionBlanqueado(Reactor, Carga, Vacio, TempTrabajo, Arcilla,
                                ALLote, CarbonActivado, CALote, Silice, SLote, FFAFinal, ColorFinal, AndisidinaFinal);
                        ContadortotalBatchReducir();
                        limpiar_blanqueado();
                        //mensaje de NOTIFICATION POP
                        llamarMessaje();
                        actualizarBatch();
                        BatchCalcular();
                        Showverificado("Blanqueado Registrado correctamente");
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
        EditorTexto_blanqueado();
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

        txtInputReactorBleached.setEnabled(false);
        txtInputLoadBleached.setEnabled(false);
        txtInputEmpty.setEnabled(false);
        txtInputWorkTemperatureBleached.setEnabled(false);
        txtInputClayBleached.setEnabled(false);
        txtInputLotClayBleached.setEnabled(false);
        txtInputActivatedCarbonBleached.setEnabled(false);
        txtInputLotActivatedCarbonBleached.setEnabled(false);
        txtInputSilicaBleached.setEnabled(false);
        txtInputLotSilicaBleached.setEnabled(false);
        txtInputFFAFinalBleached.setEnabled(false);
        txtInputColorFinalBleached.setEnabled(false);
        txtInputAnisidineFinalBleached.setEnabled(false);

        txAutoCompleteReactor.setEnabled(false);
        edtCargaBlanqueado.setEnabled(false);
        edtVacioBlanqueado.setEnabled(false);
        edtTempTrabajoBlanqueado.setEnabled(false);
        edtArcillaBlanqueado.setEnabled(false);
        txAutoCompleteLoteArcilla.setEnabled(false);
        edtCarbonActivadoBlanqueado.setEnabled(false);
        txAutoCompleteLoteCarbActiva.setEnabled(false);
        edtSiliceBlanqueado.setEnabled(false);
        txAutoCompleteLoteSilice.setEnabled(false);
        edtFFAFinalBlanqueado.setEnabled(false);
        edtColorFinalBlanqueado.setEnabled(false);
        edtAndisidinaFinalBlanqueado.setEnabled(false);
    }

    private void inserccionBlanqueado(String reactor, String carga, String vacio, String tempTrabajo, String arcilla, String ALLote, String carbonActivado, String CALote, String silice, String SLote, String FFAFinal, String colorFinal, String andisidinaFinal) {
        //Datos de fecha y hora
        Date date = new Date();
        SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
        String Date = formatoFecha.format(date);
        //datos de numero lote
        String Nlote = getActivity().getIntent().getStringExtra("NumeroLote");
        //id
        String key = databaseReference.push().getKey();
        //user
        String nombreUsuario = currentUser.getDisplayName().toString();
        String ImgenUri = currentUser.getPhotoUrl().toString();
        //Batch
        String CantBatch = tvcantbatch.getText().toString();
        //set insertardatos
        ClaseBlanqueado b = new ClaseBlanqueado();
        b.setId(key);
        b.setNumeroLote(Nlote);
        b.setReactor(reactor);
        b.setCarga(carga);
        b.setVacio(vacio + " pul Hg");
        b.setTempTrabajo(tempTrabajo + " °C");
        b.setArcilla(arcilla + " %");
        b.setALLote(ALLote);
        b.setCarbonActivado(carbonActivado + " %");
        b.setCALote(CALote);
        b.setSilice(silice + " %");
        b.setSLote(SLote);
        b.setFFAFinal(FFAFinal);
        b.setColorFinal(colorFinal);
        b.setAndisidinaFinal(andisidinaFinal);
        b.setUserName(nombreUsuario);
        b.setImagenUrl(ImgenUri);
        b.setContadorBatch(CantBatch);
        b.setFechaHora(Date);
        databaseReference.child("Blanqueado").child(Nlote).child(key).setValue(b);
        limpiar_blanqueado();
    }

    private void limpiar_blanqueado() {
        txAutoCompleteReactor.setText("");
        edtCargaBlanqueado.setText("");
        edtVacioBlanqueado.setText("");
        edtTempTrabajoBlanqueado.setText("");
        edtArcillaBlanqueado.setText("");
        txAutoCompleteLoteArcilla.setText("");
        edtCarbonActivadoBlanqueado.setText("");
        txAutoCompleteLoteCarbActiva.setText("");
        edtSiliceBlanqueado.setText("");
        txAutoCompleteLoteSilice.setText("");
        edtFFAFinalBlanqueado.setText("");
        edtColorFinalBlanqueado.setText("");
        edtAndisidinaFinalBlanqueado.setText("");
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
        } else if (Integer.parseInt(Vacio) < 20 || Integer.parseInt(Vacio) > 25) {
            txtInputEmpty.setError("Fuera de Rango (20 -25 pul Hg)");
            retorno = false;
        } else {
            txtInputEmpty.setErrorEnabled(false);
        }

        if (TempTrabajo.isEmpty()) {
            txtInputWorkTemperatureBleached.setError("Ingresar su Temp. Trabajo");
            retorno = false;
        } else if (Integer.parseInt(TempTrabajo) < 90 || Integer.parseInt(TempTrabajo) > 105) {
            txtInputWorkTemperatureBleached.setError("Fuera de Rango (90º - 105º C)");
            retorno = false;
        } else {
            txtInputWorkTemperatureBleached.setErrorEnabled(false);
        }

        if (Arcilla.isEmpty()) {
            txtInputClayBleached.setError("Ingresar su Arcilla");
            retorno = false;
        } else if (Integer.parseInt(Arcilla) / 100 < 3) {
            txtInputClayBleached.setError("Fuera de Rango (Min 3%)");
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
        } else if (Double.parseDouble(CarbonActivado) / 100 < 0.5 || Double.parseDouble(CarbonActivado) / 100 > 1) {
            txtInputActivatedCarbonBleached.setError("Fuera de Rango (0,5 - 1 %)");
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
        } else if (Double.parseDouble(Silice) / 100 < 0.05 || Double.parseDouble(Silice) / 100 > 0.2) {
            txtInputSilicaBleached.setError("Fuera de Rango (0,05 - 0,2 %)");
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