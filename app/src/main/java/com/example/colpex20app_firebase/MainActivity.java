package com.example.colpex20app_firebase;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.airbnb.lottie.LottieAnimationView;
import com.example.colpex20app_firebase.JefeGeneral.PanelJefeGeneral;
import com.example.colpex20app_firebase.JefeProducion.PanelJefeProduccion;
import com.example.colpex20app_firebase.OperadorResponsable.PanelOperador;
import com.example.colpex20app_firebase.ScreenPanel.OnBoarding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class MainActivity extends AppCompatActivity {
    //FireBase
    FirebaseAuth fAuth;
    FirebaseFirestore fstore;
    //loginAmimacion progreso bar
    LottieAnimationView lottieAnimationView;
    //ModoSharedPreferences
    SharedPreferences onBoardingScreen;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        //Firebase
        InicarFirebase();
        // PROGRESO CIRCULAR
        lottieAnimationView = findViewById(R.id.lottie);
        lottieAnimationView.setVisibility(View.INVISIBLE);

        // AGREGAR ANIMACIONES
        // LottieAnimationView.().translationY(-1600).setDuration(1000).setStartDelay(4000);

        Animation animacion1 = AnimationUtils.loadAnimation(this, R.anim.desplazamiento_arriba);
        Animation animacion2 = AnimationUtils.loadAnimation(this, R.anim.desplazamiento_abajo);
        Animation animacion3 = AnimationUtils.loadAnimation(this, R.anim.left_in);

        ImageView fondo = findViewById(R.id.colpexAceite);
        final ImageView logoImagen = findViewById(R.id.logocolpex);
        ImageView Titulo = findViewById(R.id.titulo);

        Titulo.setAnimation(animacion3);
        logoImagen.setAnimation(animacion1);
        fondo.setAnimation(animacion2);

        onBoardingScreen = getSharedPreferences("onBoardingScreen", MODE_PRIVATE);
        boolean isFirstTime = onBoardingScreen.getBoolean("firstTime", true);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null) {

                    checkUserAccessLevel(user.getUid());
                    lottieAnimationView.setVisibility(View.VISIBLE);

                } else {

                    if (isFirstTime) {

                        SharedPreferences.Editor editor = onBoardingScreen.edit();
                        editor.putBoolean("firstTime", false);
                        editor.commit();

                        Intent intent = new Intent(getApplicationContext(), OnBoarding.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Intent intent = new Intent(getApplicationContext(), PresentacionLogin_Registro.class);
                        startActivity(intent);
                        finish();
                    }


                }
            }
        }, 3000);
    }

    private void showCustomDialog() {
        new SweetAlertDialog(this, SweetAlertDialog.CUSTOM_IMAGE_TYPE)
                .setTitleText("Ups...")
                .setContentText("Por favor, conéctese a Internet para continuar")
                .setConfirmText("conectarse")
                .setCustomImage(com.microsoft.fluent.mobile.icons.R.drawable.ic_fluent_wifi_off_24_regular)
                .setConfirmButtonTextColor(Color.parseColor("#000000"))
                .setConfirmButtonBackgroundColor(Color.parseColor("#FCD01D"))
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.dismissWithAnimation();
                        startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                    }
                })
                .show();
    }

    private void checkUserAccessLevel(String uid) {
        DocumentReference df = fstore.collection("UserColpex").document(uid);
        // extract the data from the document
        df.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Log.d("TAG", "onSuccess: " + documentSnapshot.getData());
                // identify the user access level
                if (documentSnapshot.getString("isUser") != null) {
                    // si el usuario si es operario
                    Intent intent = new Intent(MainActivity.this, PanelOperador.class);
                    startActivity(intent);
                    lottieAnimationView.setVisibility(View.INVISIBLE);
                    finish();
                }
                if (documentSnapshot.getString("isadmin") != null) {
                    // si el usuario es Ingeniero
                    Intent intent = new Intent(MainActivity.this, PanelJefeProduccion.class);
                    startActivity(intent);
                    lottieAnimationView.setVisibility(View.INVISIBLE);
                    finish();

                }if (documentSnapshot.getString("isadminGeneral") != null) {
                    // si el usuario es Ingeniero
                    Intent intent = new Intent(MainActivity.this, PanelJefeGeneral.class);
                    startActivity(intent);
                    lottieAnimationView.setVisibility(View.INVISIBLE);
                    finish();

                }


            }
        });
    }

    //Wifi_Validar
    private boolean isConnected(MainActivity mainActivity) {
        ConnectivityManager connectivityManager = (ConnectivityManager) mainActivity.getSystemService(Context.CONNECTIVITY_SERVICE);
        @SuppressLint("MissingPermission") NetworkInfo wifiConn = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        @SuppressLint("MissingPermission") NetworkInfo mobileConn = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        if ((wifiConn != null && wifiConn.isConnected() || (mobileConn != null && mobileConn.isConnected()))) {
            return true;
        } else {
            return false;
        }
    }

    private void InicarFirebase() {
        FirebaseApp.initializeApp(this);
        fAuth = FirebaseAuth.getInstance();
        fstore = FirebaseFirestore.getInstance();
    }

}