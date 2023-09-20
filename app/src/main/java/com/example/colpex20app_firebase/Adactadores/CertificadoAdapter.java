package com.example.colpex20app_firebase.Adactadores;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.colpex20app_firebase.PrincipalClasses.CertificadosClass;
import com.example.colpex20app_firebase.R;

import java.util.ArrayList;

public class CertificadoAdapter extends RecyclerView.Adapter<CertificadoAdapter.CertifiHolder> {
    ArrayList<CertificadosClass>certifiList;

    public CertificadoAdapter(ArrayList<CertificadosClass> certifiList) {
        this.certifiList = certifiList;
    }

    @NonNull
    @Override
    public CertifiHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.certificado_card_desing,parent,false);
        CertifiHolder certifiHolder = new CertifiHolder(view);
        return certifiHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CertifiHolder holder, int position) {
        CertificadosClass certificadosClass = certifiList.get(position);
        holder.image.setImageResource(certificadosClass.getImage());
        holder.title.setText(certificadosClass.getTitle());
        holder.desc.setText(certificadosClass.getDescription());
    }

    @Override
    public int getItemCount() {
        return certifiList.size();
    }

    public class CertifiHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView title,desc;
        public CertifiHolder(@NonNull View itemView) {
            super(itemView);
            //Hooks
            image = itemView.findViewById(R.id.noticia_image);
            title = itemView.findViewById(R.id.noticia_title);
            desc = itemView.findViewById(R.id.noticia_desc);
        }
    }


}
