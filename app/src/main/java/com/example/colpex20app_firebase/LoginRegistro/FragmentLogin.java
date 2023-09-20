package com.example.colpex20app_firebase.LoginRegistro;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.airbnb.lottie.LottieAnimationView;
import com.example.colpex20app_firebase.JefeGeneral.PanelJefeGeneral;
import com.example.colpex20app_firebase.JefeProducion.PanelJefeProduccion;
import com.example.colpex20app_firebase.OperadorResponsable.PanelOperador;
import com.example.colpex20app_firebase.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Locale;
import java.util.Objects;
import java.util.regex.Pattern;

import cn.pedant.SweetAlert.SweetAlertDialog;


public class FragmentLogin extends Fragment {

    Button incredencial;
    Button Registarte;
    CoordinatorLayout coordinatorLayout;
    //edittext
    EditText emailEditText, PasswordEditText;
    //FireBase
    FirebaseAuth fAuth;
    FirebaseFirestore fstore;
    //loginAmimacion progreso bar
    LottieAnimationView lottieAnimationView;
    //
    DatabaseReference mDatabase;
    DataSnapshot dataSnapshot;
    //switch
    CheckBox active;
    //contraseña olvidada
    Button IniciarCredenciales;
    boolean valid = true;
    //TexImpuLayout
    TextInputLayout txtInputUsuario, txtInputPassword;
    //TooLL bar
    Toolbar toolbar;
    //Remember me
    CheckBox rememberMe;
    SharedPreferences mPrefs;
    static final String PREFS_NAME = "PrefeFile";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //Tool Bar Metodo
        ToolbarMetodos(view);
        variables(view);
        //Firebase
        InicarFirebase();
        //CAMPOS
        CamposDefinidos(view);
        //constraseña olvidada
        ContraseñaOlvidada(view);
        //button para llevarte a registro
        ButtonyaRegistrado(view);
        //button de Iniciar Sesion
        IniciarSesion();
        //Check
        getPreferencesData();

        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            Navigation.findNavController(view).navigate(R.id.nav_login);
        }
    }

    private void variables(View view) {
        mPrefs = getActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        rememberMe = view.findViewById(R.id.active);
    }

    private void getPreferencesData() {
        SharedPreferences sp = getActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        if (sp.contains("pref_name")) {
            String u = sp.getString("pref_name", "Extraviado");
            emailEditText.setText(u.toString());
        }
        if (sp.contains("pref_pass")) {
            String p = sp.getString("pref_pass", "Extraviado");
            PasswordEditText.setText(p.toString());
        }
        if (sp.contains("pref_check")) {
            Boolean b = sp.getBoolean("pref_check", false);
            rememberMe.setChecked(b);
        }
    }

    private void RememberMe() {
        if (rememberMe.isChecked()) {
            Boolean booleanChecked = rememberMe.isChecked();
            SharedPreferences.Editor editor = mPrefs.edit();
            editor.putString("pref_name", emailEditText.getText().toString());
            editor.putString("pref_pass", PasswordEditText.getText().toString());
            editor.putBoolean("pref_check", booleanChecked);
            editor.apply();
            Toast.makeText(getContext(), "Se han guardado los ajustes..", Toast.LENGTH_SHORT).show();

        } else {
            mPrefs.edit().clear().apply();
        }

    }

    private void ContraseñaOlvidada(View view) {
        IniciarCredenciales = view.findViewById(R.id.IniciarCredenciales);

        IniciarCredenciales.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.nav_inicio_contra);
            }
        });

    }

    private void ToolbarMetodos(View view) {
        toolbar = view.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.nav_presentacion);
            }
        });
    }

    private void IniciarSesion() {
        incredencial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //conecion de Internet
                if (!isConnected()) {
                    showCustomDialog();
                } else {
                    incredencial.setVisibility(View.INVISIBLE);
                    lottieAnimationView.setVisibility(View.VISIBLE);
                    final String email = emailEditText.getText().toString();
                    final String password = PasswordEditText.getText().toString();
                    if (email.isEmpty() || password.isEmpty()) {
                        incredencial.setVisibility(View.VISIBLE);
                        lottieAnimationView.setVisibility(View.INVISIBLE);
                        ShowSnackbar("Verifique todos los campos");
                        validar();
                    } else {
                        RememberMe();
                        inserUserLogin(email, password);
                        // LimpiarText();
                    }
                    EditorTexto();
                }
            }
        });
    }

    private void inserUserLogin(String email, String password) {
        fAuth.signInWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                checkUserAccessLevel(authResult.getUser().getUid());
                ShowSnackbarBlack("Iniciado sesión con éxito");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                incredencial.setVisibility(View.VISIBLE);
                lottieAnimationView.setVisibility(View.INVISIBLE);
                validar();
                Toast.makeText(getContext(), e.getLocalizedMessage().toLowerCase(Locale.ROOT), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void ButtonyaRegistrado(View view) {
        Registarte = view.findViewById(R.id.btnRegistate);
        Registarte.setOnClickListener(v -> {
            Navigation.findNavController(view).navigate(R.id.nav_registro);
        });
    }

    private void CamposDefinidos(View view) {
        //Boton de Iniciar Sesion
        incredencial = view.findViewById(R.id.LoginInicio);
        //EditText
        emailEditText = view.findViewById(R.id.edtgmail);
        PasswordEditText = view.findViewById(R.id.edtPassword);
        //Progrersor Bar
        lottieAnimationView = view.findViewById(R.id.lottie);
        lottieAnimationView.setVisibility(View.INVISIBLE);
        //TxxImputUsuarios
        txtInputUsuario = view.findViewById(R.id.txtInputUsuario);
        txtInputPassword = view.findViewById(R.id.txtInputPassword);
        //Swich
        active = view.findViewById(R.id.active);
    }

    private void InicarFirebase() {
        FirebaseApp.initializeApp(this.getContext());
        fAuth = FirebaseAuth.getInstance();
        fstore = FirebaseFirestore.getInstance();
    }

    private void checkUserAccessLevel(String uid) {
        DocumentReference df = fstore.collection("UserColpex").document(uid);
        // extract the data from the document
        df.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Log.d("TAG", "onSuccess: " + Objects.requireNonNull(documentSnapshot.getData()).toString().toLowerCase(Locale.ROOT));
                // identify the user access level
                if (documentSnapshot.getString("isUser") != null) {
                    // si el usuario es usuario
                    Intent intent = new Intent(getContext(), PanelOperador.class);
                    startActivity(intent);
                    lottieAnimationView.setVisibility(View.INVISIBLE);
                    getActivity().finish();
                }
                if (documentSnapshot.getString("isadmin") != null) {
                    // si el usuario es usuario
                    Intent intent = new Intent(getContext(), PanelJefeProduccion.class);
                    startActivity(intent);
                    lottieAnimationView.setVisibility(View.INVISIBLE);
                    getActivity().finish();
                }
                if (documentSnapshot.getString("isadminGeneral") != null) {
                    // si el usuario es Ingeniero
                    Intent intent = new Intent(getContext(), PanelJefeGeneral.class);
                    startActivity(intent);
                    lottieAnimationView.setVisibility(View.INVISIBLE);
                    getActivity().finish();

                }


            }
        });

    }


    private void LimpiarText() {
        emailEditText.setText("");
        PasswordEditText.setText("");
    }

    private void EditorTexto() {
        emailEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                txtInputUsuario.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        PasswordEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                txtInputPassword.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private Boolean validar() {
        boolean retorno = true;
        String email, password;
        email = emailEditText.getText().toString();
        password = PasswordEditText.getText().toString();

        if (email.isEmpty()) {
            txtInputUsuario.setError("Ingrese su Correo");
            retorno = false;
        } else if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            txtInputUsuario.setError("correo invalido");
            retorno = false;
        } else {
            txtInputUsuario.setError(null);
        }

        if (password.isEmpty()) {
            txtInputPassword.setError("Ingrese su contraseña");
            retorno = false;
        } else if (password.isEmpty() || password.length() < 8) {
            txtInputPassword.setError("Se necesitan más de 8 caracteres");
            retorno = false;
        } else if (!Pattern.compile("[0-9]").matcher(password).find()) {
            txtInputPassword.setError("Al menos un numero");
            retorno = false;
        } else {
            txtInputPassword.setError(null);
        }
        return retorno;

    }

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

    //Alerta para Internet
    private void showCustomDialog() {
        new SweetAlertDialog(this.getContext(), SweetAlertDialog.CUSTOM_IMAGE_TYPE)
                .setTitleText("Ups...")
                .setContentText("Por favor, conéctese a Internet para continuar")
                .setConfirmText("conectarse")
                .setCustomImage(com.microsoft.fluent.mobile.icons.R.drawable.ic_fluent_wifi_off_24_regular)
                .setConfirmButtonTextColor(Color.parseColor("#FFFFFF"))
                .setConfirmButtonBackgroundColor(Color.parseColor("#000000"))
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.dismissWithAnimation();
                        startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                    }
                })
                .show();

    }

    private void ShowSnackbar(String s) {
        Snackbar snackbar = Snackbar.make(incredencial, s, Snackbar.LENGTH_SHORT);
        snackbar.setTextColor(Color.parseColor("#ffffff"));
        snackbar.setBackgroundTint(Color.parseColor("#cc3c3c"));
        snackbar.show();
    }

    private void ShowSnackbarBlack(String s) {
        Snackbar snackbar = Snackbar.make(incredencial, s, Snackbar.LENGTH_SHORT);
        snackbar.show();
    }

}