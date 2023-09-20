package com.example.colpex20app_firebase.JefeGeneral;

import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.bumptech.glide.Glide;
import com.example.colpex20app_firebase.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import cn.pedant.SweetAlert.SweetAlertDialog;
import de.hdodenhof.circleimageview.CircleImageView;

public class PanelJefeGeneral extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    //botom para Navegar
    BottomNavigationView bottomNavigationView;

    private AppBarConfiguration mAppBarConfiguration;

    private Toolbar toolbar;

    //Navegacion
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    static final float End_SCALE = 0.7f;
    LinearLayout contentView;

    //FireBase
    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_panel_jefe_general);
        //toolbar
        ToolbarMetodo();
        //navigation
        NavigationChipBar();
        //Firebase();
        IniciarFirebase();
        //navegationDrawer
        NavigationDrawer();
        animateNaviagtionDrawer();
        userData();
    }
    private void ToolbarMetodo() {
        toolbar = findViewById(R.id.topAppBar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawerLayout.isDrawerVisible(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else {
                    drawerLayout.openDrawer(GravityCompat.START);
                }
            }

        });
    }

    private void IniciarFirebase() {
        // FireBase
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
    }

    private void NavigationChipBar() {
        bottomNavigationView = findViewById(R.id.bottom_nav_menu);
    }

    @Override
    protected void onStart() {
        super.onStart();
        NavController navController = Navigation.findNavController(this, R.id.fragmentContainerView);
        NavigationUI.setupWithNavController(bottomNavigationView, navController, false);

        NavController navControllerDrawer = Navigation.findNavController(this, R.id.fragmentContainerView);
        NavigationUI.setupWithNavController(navigationView, navControllerDrawer, false);
    }

    private void NavigationDrawer() {
        contentView = findViewById(R.id.content);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        navigationView.bringToFront();
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void animateNaviagtionDrawer() {
        drawerLayout.setScrimColor(getResources().getColor(cn.pedant.SweetAlert.R.color.trans_success_stroke_color));
        drawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {

                final float diffscaledoffset = slideOffset * (1 - End_SCALE);
                final float offsetScale = 1 - diffscaledoffset;
                contentView.setScaleX(offsetScale);
                contentView.setScaleY(offsetScale);

                final float xOffset = drawerView.getWidth() * slideOffset;
                final float xOffserDiff = contentView.getWidth() * diffscaledoffset / 2;
                final float xTranslation = xOffset - xOffserDiff;
                contentView.setTranslationX(xTranslation);
            }

            @Override
            public void onDrawerOpened(@NonNull View drawerView) {

            }

            @Override
            public void onDrawerClosed(@NonNull View drawerView) {

            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return true;
    }
    // valiables

    private void userData() {
        // llamar los paramentros Navigation View
        View vistaheader = navigationView.getHeaderView(0);
        TextView tvNombre = vistaheader.findViewById(R.id.userName),
                tvCorreo = vistaheader.findViewById(R.id.userEmail);
        CircleImageView photoUser = vistaheader.findViewById(R.id.userImage);
        //imformacion del usuario
        tvNombre.setText(currentUser.getDisplayName());
        tvCorreo.setText(currentUser.getEmail());
        //foto de usuario
        if (currentUser.getPhotoUrl() != null) {
            Glide.with(this).load(currentUser.getPhotoUrl()).into(photoUser);
        } else {
            Glide.with(this).load(R.drawable.vectoruser).into(photoUser);
        }
    }

    private void showCustomDialogCerrarApp() {
        new SweetAlertDialog(this, SweetAlertDialog.CUSTOM_IMAGE_TYPE)
                .setCustomImage(com.microsoft.fluent.mobile.icons.R.drawable.ic_fluent_question_circle_24_regular)
                .setTitleText("has oprimido el botón atrás")
                .setContentText("¿Quieres cerrar la aplicación?")
                .setCancelText("No, Cancelar!").setConfirmText("Sí, Cerrar")
                .setConfirmButtonTextColor(Color.parseColor("#000000"))
                .setCancelButtonTextColor(Color.parseColor("#000000"))
                .setConfirmButtonBackgroundColor(Color.parseColor("#FCD01D"))
                .setCancelButtonBackgroundColor(Color.parseColor("#FCD01D"))
                .showCancelButton(true).setCancelClickListener(sDialog -> {
                    sDialog.dismissWithAnimation();
                    new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE).setTitleText("Operación cancelada")
                            .setConfirmButtonTextColor(Color.parseColor("#000000"))
                            .setConfirmButtonBackgroundColor(Color.parseColor("#FCD01D"))
                            .setContentText("No saliste de la app")
                            .show();
                }).setConfirmClickListener(sweetAlertDialog -> {
                    sweetAlertDialog.dismissWithAnimation();
                    moveTaskToBack(true);
                    //System.exit(0);
                    finish();
                }).show();
    }

    @Override
    public void onBackPressed() {
        showCustomDialogCerrarApp();
    }
}