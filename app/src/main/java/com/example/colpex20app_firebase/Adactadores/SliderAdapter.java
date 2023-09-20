package com.example.colpex20app_firebase.Adactadores;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.colpex20app_firebase.Model.SliderItem;
import com.example.colpex20app_firebase.R;
import com.orhanobut.dialogplus.ViewHolder;
import com.smarteist.autoimageslider.SliderViewAdapter;

import java.util.ArrayList;
import java.util.List;

public class SliderAdapter extends SliderViewAdapter<SliderAdapter.SliderAdapterVH> {
    private Context context;
    private List<SliderItem> mSliderItems = new ArrayList<>();

    public SliderAdapter(Context context) {
        this.context = context;
    }

    @Override
    public SliderAdapterVH onCreateViewHolder(ViewGroup parent) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_slider_layout_item, null);
        return new SliderAdapterVH(inflate);
    }

    @Override
    public void onBindViewHolder(SliderAdapterVH viewHolder, int position) {
        SliderItem sliderItem = mSliderItems.get(position);
        viewHolder.textView.setText(sliderItem.getTitulo());
        viewHolder.textView.setTextSize(16);
        viewHolder.textView.setTextColor(Color.WHITE);
        //Imagen
        Glide.with(viewHolder.itemView)
                .load(sliderItem.getImagen())
                .fitCenter()
                .into(viewHolder.imageView);
    }

    @Override
    public int getCount() {
        return mSliderItems.size();
    }

    public void updateItem(List<SliderItem> lista) {
        mSliderItems.clear();
        mSliderItems.addAll(lista);
        this.notifyDataSetChanged();
    }

    protected class SliderAdapterVH extends ViewHolder {
        View itemView;
        ImageView imageView;
        TextView textView;

        public SliderAdapterVH(View itemView){
            super(itemView);
            imageView = itemView.findViewById(R.id.iv_auto_image_slider);
            textView = itemView.findViewById(R.id.tv_auto_image_slider);
            this.itemView = itemView;
        }
    }

}
