package com.example.colpex20app_firebase;

import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class PresentacionLogin_Registro extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //  Window w = getActivity().getWindow();
        // w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
       //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN,WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        setContentView(R.layout.activity_presentacion_login_registro);
    }
    private void showCustomDialogCerrarApp() {
        new SweetAlertDialog(this, SweetAlertDialog.CUSTOM_IMAGE_TYPE)
                .setCustomImage(com.microsoft.fluent.mobile.icons.R.drawable.ic_fluent_question_circle_24_regular)
                .setTitleText("has oprimido el botón atrás")
                .setContentText("¿Quieres cerrar la aplicación?")
                .setCancelText("No, Cancelar!").setConfirmText("Sí, Cerrar")
                .setConfirmButtonTextColor(Color.parseColor("#FFFFFF"))
                .setCancelButtonTextColor(Color.parseColor("#FFFFFF"))
                .setConfirmButtonBackgroundColor(Color.parseColor("#000000"))
                .setCancelButtonBackgroundColor(Color.parseColor("#000000"))
                .showCancelButton(true).setCancelClickListener(sDialog -> {
                    sDialog.dismissWithAnimation();
                    new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE).setTitleText("Operación cancelada")
                            .setConfirmButtonTextColor(Color.parseColor("#FFFFFF"))
                            .setConfirmButtonBackgroundColor(Color.parseColor("#000000"))
                            .setContentText("No saliste de la app")
                            .show();
                }).setConfirmClickListener(sweetAlertDialog -> {
                    sweetAlertDialog.dismissWithAnimation();
                    moveTaskToBack(true);
                  //  System.exit(0);
                    finish();
                }).show();
    }
    @Override
    public void onBackPressed() {
        showCustomDialogCerrarApp();
    }
}