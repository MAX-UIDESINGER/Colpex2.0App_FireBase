package com.example.colpex20app_firebase.ScreenPanel;

import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.colpex20app_firebase.R;


public class FragmentOnBoarding extends Fragment {
    //Variables de slider
    ViewPager viewPager;
    LinearLayout dotsLayout;
    SliderAdapter sliderAdapter;
    TextView[] dots;
    //buttom
    Button letsGetStarted;
    //amimacion
    Animation animation;
    //
    int currentPos;
    //
    Button skip;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_on_boarding, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        varibles(view);
        MetodoCall();
    }

    private void varibles(View view) {
        //Hooks
        viewPager = view.findViewById(R.id.slider);
        dotsLayout = view.findViewById(R.id.dots);
        letsGetStarted = view.findViewById(R.id.get_started_btn);
        //BOTONES
        skip = view.findViewById(R.id.skip_btn);
    }

    private void MetodoCall() {
        //call adapter
        sliderAdapter = new SliderAdapter(this.getContext());
        viewPager.setAdapter(sliderAdapter);
        addDots(0);
        viewPager.addOnPageChangeListener(changeListener);

        letsGetStarted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             //   Intent intent = new Intent(OnBoarding.this, PresentacionLogin_Registro.class);
             //   startActivity(intent);
             //   finish();
            }
        });

    }
    public void skipUI(View view) {
       // Intent intent = new Intent(OnBoarding.this, PresentacionLogin_Registro.class);
       // startActivity(intent);
       // finish();
    }
    public void next(View view){
        viewPager.setCurrentItem(currentPos+1);
    }


    private void addDots(int position) {
        dots = new TextView[7];
        dotsLayout.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(this.getContext());
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(35);

            dotsLayout.addView(dots[i]);
        }
        if (dots.length > 0) {
            dots[position].setTextColor(getResources().getColor(R.color.colorPrimary));
        }

    }

    ViewPager.OnPageChangeListener changeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            addDots(position);

            currentPos = position;

            if (position == 0) {
                letsGetStarted.setVisibility(View.INVISIBLE);
            } else if (position == 1) {
                letsGetStarted.setVisibility(View.INVISIBLE);
            } else if (position == 2) {
                letsGetStarted.setVisibility(View.INVISIBLE);
            }else if (position == 3) {
                letsGetStarted.setVisibility(View.INVISIBLE);
            }else if (position == 4) {
                letsGetStarted.setVisibility(View.INVISIBLE);
            }else if (position == 5) {
                letsGetStarted.setVisibility(View.INVISIBLE);
            } else {
                animation = AnimationUtils.loadAnimation(getContext(),R.anim.bottom_anim);
                letsGetStarted.setAnimation(animation);
                letsGetStarted.setVisibility(View.VISIBLE);
            }
        }
        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

}