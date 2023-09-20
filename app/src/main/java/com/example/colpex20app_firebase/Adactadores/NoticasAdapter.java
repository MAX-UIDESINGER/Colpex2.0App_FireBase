package com.example.colpex20app_firebase.Adactadores;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.colpex20app_firebase.PrincipalClasses.NoticiasClass;
import com.example.colpex20app_firebase.R;

import java.util.ArrayList;
import java.util.List;

public class NoticasAdapter extends RecyclerView.Adapter<NoticasAdapter.NoticiasHolder> {
    ArrayList<NoticiasClass>noticasList;
    Context c;
    List<NoticasAdapter> montes;

    public NoticasAdapter(List<NoticiasClass> noticasList, Context c) {
        this.noticasList = (ArrayList<NoticiasClass>) noticasList;
        this.c = c;
    }

    @NonNull
    @Override
    public NoticiasHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.noticias_card_desing,parent,false);
        NoticiasHolder noticiasHolder = new NoticiasHolder(view);
        return noticiasHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull NoticiasHolder holder, final int position) {
        NoticiasClass noticiasClass = noticasList.get(position);
        holder.image.setImageResource(noticiasClass.getImage());
        holder.title.setText(noticiasClass.getTitle());
        holder.desc.setText(noticiasClass.getDescription());

        holder.masInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent launchBrowser = new Intent(Intent.ACTION_VIEW,noticiasClass.masInfo);
                c.startActivity(launchBrowser);
            }
        });

    }

    @Override
    public int getItemCount() {
        return noticasList.size();
    }

    public class NoticiasHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView title,desc,masInfo;

        public NoticiasHolder(@NonNull View itemView) {
            super(itemView);
            //Hooks
            image = itemView.findViewById(R.id.noticia_image);
            title = itemView.findViewById(R.id.noticia_title);
            desc = itemView.findViewById(R.id.noticia_desc);
            masInfo = itemView.findViewById(R.id.row_info);

        }
    }
}
