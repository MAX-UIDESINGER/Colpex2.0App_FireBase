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
import com.example.colpex20app_firebase.PrincipalClasses.ClassDesgomado.ClaseDesgomado;
import com.example.colpex20app_firebase.R;

import java.util.ArrayList;

public class DesgomadoAdacter_jefe extends RecyclerView.Adapter<DesgomadoAdacter_jefe.versionVH> {

    ArrayList<ClaseDesgomado> DesgomadoList;
    private final RvItemClick rvItemClick;

    public DesgomadoAdacter_jefe(ArrayList<ClaseDesgomado> desgomadoList, RvItemClick rvItemClick) {
        DesgomadoList = desgomadoList;
        this.rvItemClick = rvItemClick;
    }


    @NonNull
    @Override
    public versionVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_desgomado_jefe, parent, false);
        return new versionVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull versionVH holder, int position) {
        ClaseDesgomado desgomado = DesgomadoList.get(position);

        holder.Lote.setText(desgomado.getNumeroLote());
        holder.Reactor.setText(desgomado.getReactor());
        holder.ContadorBatch.setText(desgomado.getContadorBatch());
        holder.Tonelaje.setText(desgomado.getTonelaje());
        holder.AcidoFosforico.setText(desgomado.getAcidoFosforico());
        holder.Loteacido.setText(desgomado.getLoteacido());
        holder.TempTrabajo.setText(desgomado.getTempTrabajo());
        holder.TResidencia.setText(desgomado.getTResidencia());


        String Imagneurl = desgomado.getImagenUrl();
        if (!Imagneurl.equals("")) {
            Glide.with(holder.userImage.getContext())
                    .load(desgomado.getImagenUrl())
                    .apply(RequestOptions.circleCropTransform())
                    .into(holder.userImage);
        } else {
            //errorImgein
            Glide.with(holder.userImage.getContext())
                    .load(R.drawable.vectoruser)
                    .into(holder.userImage);
        }
        holder.Nombre.setText(desgomado.getUserName());
        holder.Fecha.setText(desgomado.getFechaHora());

        //Expandir panel
        boolean isExpandable = DesgomadoList.get(position).isExpandable();
        holder.expanderLayout.setVisibility(isExpandable ? View.VISIBLE : View.GONE);
        if (isExpandable){
            holder.ivMenu.setImageResource(com.microsoft.fluent.mobile.icons.R.drawable.ic_fluent_chevron_up_24_regular);
        }else {
            holder.ivMenu.setImageResource(com.microsoft.fluent.mobile.icons.R.drawable.ic_fluent_chevron_down_24_regular);
        }
    }

    @Override
    public int getItemCount() {
        return DesgomadoList.size();
    }


    public class versionVH extends RecyclerView.ViewHolder implements View.OnClickListener,
            PopupMenu.OnMenuItemClickListener {

        TextView Reactor, ContadorBatch, Tonelaje, AcidoFosforico, Loteacido,
                TempTrabajo, TResidencia;
        //usuario
        ImageView userImage;
        TextView Nombre;
        //imagen Editar y eliminar
        ImageView ivMenu;
        //Lote  y fecha
        TextView Lote, Fecha;
        //expandir texto
        LinearLayout linearLayout;
        RelativeLayout expanderLayout;

        public versionVH(@NonNull View itemView) {
            super(itemView);
            Lote = itemView.findViewById(R.id.txvnumerlote);
            Reactor = itemView.findViewById(R.id.txvReactor);
            ContadorBatch = itemView.findViewById(R.id.txvBatch);
            Tonelaje = itemView.findViewById(R.id.txvTonelada);
            AcidoFosforico = itemView.findViewById(R.id.txvAcidoFosforico);
            Loteacido = itemView.findViewById(R.id.txvLote);
            TempTrabajo = itemView.findViewById(R.id.txvTempTrabajo);
            TResidencia = itemView.findViewById(R.id.txvTResidencia);
            Fecha = itemView.findViewById(R.id.txvfecha);

            //usuario
            userImage = itemView.findViewById(R.id.userImage);
            Nombre = itemView.findViewById(R.id.NameUser);

            //buttom
            ivMenu = itemView.findViewById(R.id.ivMenu);

            //expander
            linearLayout = itemView.findViewById(R.id.linear_layout);
            expanderLayout = itemView.findViewById(R.id.expandable_layout);

            linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ClaseDesgomado version = DesgomadoList.get(getAdapterPosition());
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
        public boolean onMenuItemClick(MenuItem item) {
            return false;
        }
    }
}
