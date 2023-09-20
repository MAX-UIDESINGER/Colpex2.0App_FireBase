package com.example.colpex20app_firebase.OperadorResponsable.FragmenContenido_op;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.colpex20app_firebase.PresentacionLogin_Registro;
import com.example.colpex20app_firebase.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import cn.pedant.SweetAlert.SweetAlertDialog;
import de.hdodenhof.circleimageview.CircleImageView;

public class FragmentUsuario_op extends Fragment {
    //FireBase
    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    //TooLL bar
    Toolbar toolbar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().setTitle(" Usuario");
        return inflater.inflate(R.layout.fragment_usuario_op, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //Firebase();
        IniciarFirebase();
        //photo y datos de usuario NaviView
        userData(view);
    }

    private void userData(View view) {
        CircleImageView userImage = view.findViewById(R.id.userImage);

        TextView userName = view.findViewById(R.id.userName);
        TextView userEmail = view.findViewById(R.id.userEmail);
        userName.setText(currentUser.getDisplayName());
        userEmail.setText(currentUser.getEmail());

        if (currentUser.getPhotoUrl() != null) {
            Glide.with(this).load(currentUser.getPhotoUrl()).into(userImage);
        } else {
            Glide.with(this).load(R.drawable.vectoruser).into(userImage);
        }
        RelativeLayout relativeLayout = view.findViewById(R.id.CerrarSesion);
        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              showCustomDialog(v);
            }
        });

    }

    private void showCustomDialog(View view) {
        new SweetAlertDialog(this.getContext(), SweetAlertDialog.CUSTOM_IMAGE_TYPE)
                .setTitleText("Alerta")
                .setContentText("¿Quieres cerrar sesión?")
                .setCustomImage(com.microsoft.fluent.mobile.icons.R.drawable.ic_fluent_person_question_mark_24_regular)
                .setConfirmButtonTextColor(Color.parseColor("#000000"))
                .setCancelButtonTextColor(Color.parseColor("#000000"))
                .setConfirmButtonBackgroundColor(Color.parseColor("#FCD01D"))
                .setCancelButtonBackgroundColor(Color.parseColor("#FCD01D"))
                .setConfirmText("Sí, Cerrar")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.dismissWithAnimation();
                        FirebaseAuth.getInstance().signOut();
                        View view = null;
                        //Navigation.findNavController(view).navigate(R.id.nav_Presentacion);

                       Intent intent = new Intent(getContext(), PresentacionLogin_Registro.class);
                        startActivity(intent);
                       getActivity().finish();
                    }
                })
                .setCancelButton("No, Cancelar!", new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.dismissWithAnimation();
                    }
                })
                .show();

    }

    private void IniciarFirebase() {
        // FireBase
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
    }

}