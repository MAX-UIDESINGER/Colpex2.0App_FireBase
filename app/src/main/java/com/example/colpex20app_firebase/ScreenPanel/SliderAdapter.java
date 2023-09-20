package com.example.colpex20app_firebase.ScreenPanel;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.PagerAdapter;

import com.airbnb.lottie.LottieAnimationView;
import com.example.colpex20app_firebase.R;


public class SliderAdapter extends PagerAdapter {
    Context context;
    LayoutInflater layoutInflater;

    public SliderAdapter(Context context) {
        this.context = context;
    }

    int images[] = {
            R.raw.p_bienvenida,
            R.raw.p_datosordenados,
            R.raw.p_tiemporecor,
            R.raw.p_responsive,
            R.raw.p_enviodedatos,
            R.raw.p_data_servidor,
            R.raw.p_buscardatos,

           // R.raw.homeinicial
    };
    int headings[] = {
            R.string.tituloBienvenido,
            R.string.tituloAutomatiza,
            R.string.tituloDatos,
            R.string.tituloresponsive,
            R.string.titulonotificado,
            R.string.tituloenlínea,
            R.string.tituloBúsquedas,

    };
    int descriptions[] = {
            R.string.ContenidoBienvenida,
            R.string.ContenidoAutomatiza,
            R.string.ContenidoDatos,
            R.string.Contenidoresponsive,
            R.string.Contenidonotificado,
            R.string.Contenidolínea,
            R.string.ContenidoBúsquedas,
    };

    @Override
    public int getCount() {
        return headings.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == (ConstraintLayout) object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.slider_layout,container,false);

        //Hooks
        LottieAnimationView lottieAnimationView = view.findViewById(R.id.lottie);
        TextView heading = view.findViewById(R.id.slider_heading);
        TextView desc = view.findViewById(R.id.slider_desc);

        lottieAnimationView.setAnimation(images[position]);
        heading.setText(headings[position]);
        desc.setText(descriptions[position]);
        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((ConstraintLayout)object);
    }
}
