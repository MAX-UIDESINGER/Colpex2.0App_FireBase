package com.example.colpex20app_firebase.LoginRegistro.RecuperarContraseña;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.colpex20app_firebase.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;


public class Fragment_set_new_password extends Fragment {
    //TooLL bar
    Toolbar toolbar;
    //boton siguente
    Button siguente;
    EditText ediEmail;
    FirebaseAuth fAuth;
    FirebaseFirestore fstore;
    private  String email = "";

    private ProgressDialog mDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_set_new_password, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //Tool Bar Metodo
        ToolbarMetodos(view);
        // bottom siguente
        MetodoSiguiente(view);
        //Firebase
        InicarFirebase();

    }

    private void MetodoSiguiente(View view) {
        //text
        ediEmail = view.findViewById(R.id.edtTextEmail);
        //button
        siguente = view.findViewById(R.id.siguente_btn);
        //dialogo
        mDialog = new ProgressDialog(getContext());

        siguente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 email = ediEmail.getText().toString();

                if (!email.isEmpty()){
                    mDialog.setMessage("Espere un momento...");
                    mDialog.setCanceledOnTouchOutside(false);
                    mDialog.show();
                    resetPassword();
                }else {
                    Toast.makeText(getContext(), "Debe ingresar el email", Toast.LENGTH_SHORT).show();
                }

               // Navigation.findNavController(v).navigate(R.id.nav_selecciom);
            }
        });
    }

    private void resetPassword() {
        fAuth.setLanguageCode("es");
        fAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(getContext(), "Se ha enviado un correo para restaurar tu contraseña al correo: "+ email, Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(getContext(), "No se pudo enviar el correo de restablecer contraseña", Toast.LENGTH_SHORT).show();
                }
                mDialog.dismiss();
            }
        });
    }

    private void ToolbarMetodos(View view) {
        toolbar = view.findViewById(R.id.toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.nav_login);
            }
        });
    }

    private void InicarFirebase() {
        FirebaseApp.initializeApp(this.getContext());
        fAuth = FirebaseAuth.getInstance();
        fstore = FirebaseFirestore.getInstance();
    }



}