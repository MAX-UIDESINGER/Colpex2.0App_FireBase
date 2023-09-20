package com.example.colpex20app_firebase.Adactadores.Adacter_JefeProducion.AdacterTrabajoJefe;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.colpex20app_firebase.Model.RvItemClick;
import com.example.colpex20app_firebase.PrincipalClasses.ClassNeutralizado.ClaseNeutralizado;
import com.example.colpex20app_firebase.R;

import java.util.ArrayList;

public class NeutralizadoAdacter_jefe extends RecyclerView.Adapter<NeutralizadoAdacter_jefe.versionVH> {

    ArrayList<ClaseNeutralizado> neutralizadosList;
    private final RvItemClick rvItemClick;

    public NeutralizadoAdacter_jefe(ArrayList<ClaseNeutralizado> neutralizadosList, RvItemClick rvItemClick) {
        this.neutralizadosList = neutralizadosList;
        this.rvItemClick = rvItemClick;
    }

    @NonNull
    @Override
    public versionVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_neutralizado_jefe, parent, false);
        return new versionVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull versionVH holder, int position) {
        ClaseNeutralizado blanqueado = neutralizadosList.get(position);

        holder.Lote.setText(blanqueado.getNumeroLote());
        holder.Reactor.setText(blanqueado.getReactor());
        holder.Batch.setText(blanqueado.getContadorBatch());
        holder.FFAInicial.setText(blanqueado.getFFAInicial());
        holder.TempTrabajo.setText(blanqueado.getTempTrabajo());
        holder.ConcentracionNaOH.setText(blanqueado.getConcentracionNaOH());
        holder.porsentaje.setText(blanqueado.getPorsentaje());
        holder.LoteNAOH.setText(blanqueado.getLoteNAOH());
        holder.AceiteNeutro.setText(blanqueado.getAceiteNeutro());

        String Imagneurl = blanqueado.getImagenUrl();
        if (!Imagneurl.equals("")) {
            Glide.with(holder.userImage.getContext())
                    .load(blanqueado.getImagenUrl())
                    .apply(RequestOptions.circleCropTransform())
                    .into(holder.userImage);
        } else {
            //errorImgein
            Glide.with(holder.userImage.getContext())
                    .load(R.drawable.vectoruser)
                    .into(holder.userImage);
        }
        holder.Nombre.setText(blanqueado.getUserName());
        holder.Fecha.setText(blanqueado.getFechaHora());

        //Expandir panel
        boolean isExpandable = neutralizadosList.get(position).isExpandable();
        holder.expanderLayout.setVisibility(isExpandable ? View.VISIBLE : View.GONE);
        if (isExpandable){
            holder.ivMenu.setImageResource(com.microsoft.fluent.mobile.icons.R.drawable.ic_fluent_chevron_up_24_regular);
        }else {
            holder.ivMenu.setImageResource(com.microsoft.fluent.mobile.icons.R.drawable.ic_fluent_chevron_down_24_regular);
        }
    }

    @Override
    public int getItemCount() {
        return neutralizadosList.size();
    }


    public class versionVH extends RecyclerView.ViewHolder implements View.OnClickListener, PopupMenu.OnMenuItemClickListener {

        TextView Reactor, Batch, FFAInicial, TempTrabajo, ConcentracionNaOH,
                LoteNAOH, AceiteNeutro,porsentaje;
        //usuario
        ImageView userImage;
        TextView Nombre;
        //Lote  y fecha
        TextView Lote, Fecha;
        //button Editar y eliminar
        ImageView ivMenu;
        //expandir texto
        LinearLayout linearLayout;
        RelativeLayout expanderLayout;

        public versionVH(@NonNull View itemView) {
            super(itemView);
            Lote = itemView.findViewById(R.id.txvnumerlote);
            Reactor = itemView.findViewById(R.id.txvReactor);
            Batch = itemView.findViewById(R.id.txvBatch);
            FFAInicial = itemView.findViewById(R.id.txvFFAInicial);
            TempTrabajo = itemView.findViewById(R.id.txvTempTrabajo);
            ConcentracionNaOH = itemView.findViewById(R.id.txvConcentracionNaOH);
            porsentaje = itemView.findViewById(R.id.txvConcentracionNaOH_porsentaje);
            LoteNAOH = itemView.findViewById(R.id.txvLoteNaOH);
            AceiteNeutro = itemView.findViewById(R.id.txvAceiteNeutro1);
            Fecha = itemView.findViewById(R.id.txvfecha);

            //usuario
            userImage = itemView.findViewById(R.id.userImage);
            Nombre = itemView.findViewById(R.id.NameUser);

            //buttom
            ivMenu = itemView.findViewById(R.id.ivMenu);

            //expander
            linearLayout=itemView.findViewById(R.id.linear_layout);
            expanderLayout=itemView.findViewById(R.id.expandable_layout);

            linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ClaseNeutralizado version = neutralizadosList.get(getAdapterPosition());
                    version.setExpandable(!version.isExpandable());
                    notifyItemChanged(getAdapterPosition());
                }
            });

        }
        @SuppressLint("NonConstantResourceId")
        @Override
        public void onClick(View v) {
            PopupMenu popupMenu = new PopupMenu(v.getContext(), v);
            popupMenu.getMenuInflater().inflate(R.menu.add_edit_item, popupMenu.getMenu());
            popupMenu.setOnMenuItemClickListener(this);
            popupMenu.show();
        }

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            return false;
        }
    }
}
