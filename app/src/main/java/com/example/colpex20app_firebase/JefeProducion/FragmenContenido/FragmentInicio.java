package com.example.colpex20app_firebase.JefeProducion.FragmenContenido;

import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.colpex20app_firebase.Adactadores.CertificadoAdapter;
import com.example.colpex20app_firebase.Adactadores.NoticasAdapter;
import com.example.colpex20app_firebase.Adactadores.SliderAdapter;
import com.example.colpex20app_firebase.Model.SliderItem;
import com.example.colpex20app_firebase.PrincipalClasses.CertificadosClass;
import com.example.colpex20app_firebase.PrincipalClasses.NoticiasClass;
import com.example.colpex20app_firebase.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;
import java.util.List;

public class FragmentInicio extends Fragment {
    private SliderView svCarrusel;
    private SliderAdapter sliderAdapter;
    //FireBase
    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    //Recycleview Noticias
    RecyclerView noticiaRecycler;
    NoticasAdapter adapter;
    //Recycleview Certificado
    RecyclerView CertificadoRecycler;
    CertificadoAdapter Certifiadapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().setTitle(" Inicio");
        return inflater.inflate(R.layout.fragment_inicio, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //RecyclerView Noticiass
        noticiasRecycler(view);
        PresentacionRecycler(view);
        //Firebase();
        IniciarFirebase();
        IniciarAdapter(view);
        loadData();
        //photo y datos de usuario NaviView
        // userData(view);
    }


    private void noticiasRecycler(View view) {
        noticiaRecycler = view.findViewById(R.id.gvNoticias);
        noticiaRecycler.setHasFixedSize(true);
        noticiaRecycler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        List<NoticiasClass> noticiasClasses = new ArrayList<>();

        noticiasClasses.add(new NoticiasClass(R.drawable.logofoto, "Omega 3 para niños", "Omega 3 para niños Investigacion de National Geographic.", Uri.parse("https://www.youtube.com/watch?v=1WeYHP9ckHk&ab_channel=GlicobiologiaComunicacionCelular")));
        noticiasClasses.add(new NoticiasClass(R.drawable.logofatsui, "Grasas de la vida", "Un centro para profesionales de la salud sobre...", Uri.parse("https://www.fatsoflife.com/")));
        noticiasClasses.add(new NoticiasClass(R.drawable.logoiffo, "La organización de comercio...", "¿Por qué la gente no aprovecha al máximo...", Uri.parse("https://www.iffo.com/")));
        noticiasClasses.add(new NoticiasClass(R.drawable.logogoe, "La organización mundial...", "GOED representa a la industria mundial...", Uri.parse("https://www.goedomega3.com/")));

        adapter = new NoticasAdapter(noticiasClasses, getContext());
        noticiaRecycler.setAdapter(adapter);
    }

    private void PresentacionRecycler(View view) {
        CertificadoRecycler = view.findViewById(R.id.Certificados);
        CertificadoRecycler.setHasFixedSize(true);
        CertificadoRecycler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));


        ArrayList<CertificadosClass> certificadoAdapters = new ArrayList<>();
        certificadoAdapters.add(new CertificadosClass(R.drawable.certificado_sgs_y_ukas_color, "SGS", "Servicios de inspección, verificación, ensayos y certificación."));
        certificadoAdapters.add(new CertificadosClass(R.drawable.certificado_intertek_color, "Intertek", "Líder en Aseguramiento Total de la Calidad para las industrias."));
        certificadoAdapters.add(new CertificadosClass(R.drawable.certificado_marin_color, "MarinTrust", "Permite a los productores marinos tener mejor calidad."));
        certificadoAdapters.add(new CertificadosClass(R.drawable.certificado_goed_color, "GOED", "Organización Global para los Omega-3 EPA y DHA (GOED)."));
        certificadoAdapters.add(new CertificadosClass(R.drawable.certificado_iffo_color, "IFFO", "Organización global de confianza y proactiva de los ingredientes marinos."));

        Certifiadapter = new CertificadoAdapter(certificadoAdapters);
        CertificadoRecycler.setAdapter(Certifiadapter);
    }


    private void IniciarFirebase() {
        // FireBase
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
    }


    private void IniciarAdapter(View view) {
        //Carrusel de Imágenes
        svCarrusel = view.findViewById(R.id.svCarrusel);
        sliderAdapter = new SliderAdapter(getContext());
        svCarrusel.setSliderAdapter(sliderAdapter);
        //efectos
        svCarrusel.setIndicatorAnimation(IndicatorAnimationType.SCALE_DOWN); //set indicator animation by using IndicatorAnimationType. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        svCarrusel.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION  );
        svCarrusel.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
        svCarrusel.setIndicatorSelectedColor(Color.TRANSPARENT);
        svCarrusel.setIndicatorUnselectedColor(Color.TRANSPARENT);
        svCarrusel.setScrollTimeInSec(4); //set scroll delay in seconds :
        svCarrusel.startAutoCycle();

    }


    private void loadData() {
        List<SliderItem> lista = new ArrayList<>();
        lista.add(new SliderItem("", R.drawable.homeinicial));
        lista.add(new SliderItem("", R.drawable.homebarco));
        lista.add(new SliderItem("", R.drawable.homeomega));
        lista.add(new SliderItem("", R.drawable.homebuque));
        lista.add(new SliderItem("", R.drawable.homerempresa));
        sliderAdapter.updateItem(lista);
    }


}