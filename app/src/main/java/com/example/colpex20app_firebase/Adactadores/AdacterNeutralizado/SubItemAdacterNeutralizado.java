package com.example.colpex20app_firebase.Adactadores.AdacterNeutralizado;

import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.example.colpex20app_firebase.Model.RvItemClick;
import com.example.colpex20app_firebase.PrincipalClasses.ClassNeutralizado.ClaseNeutralizado;
import com.example.colpex20app_firebase.R;

import java.util.List;

public class SubItemAdacterNeutralizado extends RecyclerView.Adapter<SubItemAdacterNeutralizado.SubItemViewHolder> {
    private List<ClaseNeutralizado> subItemList;
    private final RvItemClick rvItemClick;

    public SubItemAdacterNeutralizado(List<ClaseNeutralizado> subItemList, RvItemClick rvItemClick) {
        this.subItemList = subItemList;
        this.rvItemClick = rvItemClick;
    }

    @NonNull
    @Override
    public SubItemAdacterNeutralizado.SubItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sub_item_neutralizado_op, parent, false);
        return new SubItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SubItemAdacterNeutralizado.SubItemViewHolder holder, int position) {
      ClaseNeutralizado blanqueado = subItemList.get(position);

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
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            holder.actualizar.setVisibility(View.GONE);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            holder.actualizar.setVisibility(View.GONE);
                            return false;
                        }
                    })
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
        boolean isExpandable = subItemList.get(position).isExpandable();
        holder.expanderLayout.setVisibility(isExpandable ? View.VISIBLE : View.GONE);
        ViewGroup.LayoutParams layoutParams = holder.expanderLayout.getLayoutParams();
        if (isExpandable) {
            layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            holder.expanderLayout.setVisibility(View.VISIBLE);
            holder.iconExpand.setImageResource(com.microsoft.fluent.mobile.icons.R.drawable.ic_fluent_chevron_up_24_regular);
        } else {

            layoutParams.height = 0;
            holder.expanderLayout.setVisibility(View.GONE);
            holder.iconExpand.setImageResource(com.microsoft.fluent.mobile.icons.R.drawable.ic_fluent_chevron_down_24_regular);
        }
        holder.expanderLayout.setLayoutParams(layoutParams);
    }

    @Override
    public int getItemCount() {
        return subItemList.size();
    }

    public class SubItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, PopupMenu.OnMenuItemClickListener{
        TextView Reactor, Batch, FFAInicial, TempTrabajo, ConcentracionNaOH,
                LoteNAOH, AceiteNeutro,porsentaje;
        //usuario
        ImageView userImage;
        TextView Nombre;
        //Lote  y fecha
        TextView Lote, Fecha;
        //button Editar y eliminar
        Button ivMenu;
        //expandir texto
        LinearLayout linearLayout;
        RelativeLayout expanderLayout;
        //Imagen expander
        ImageView iconExpand;
        //Preogress Bar
        ProgressBar actualizar;
        public SubItemViewHolder(@NonNull View itemView) {
            super(itemView);
            //cargar imagen
            actualizar = itemView.findViewById(R.id.progress_bar);
            //imagen expander
            iconExpand = itemView.findViewById(R.id.expand_collapse_icon);
            //
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
            ivMenu.setOnClickListener(this);

            //expander
            linearLayout=itemView.findViewById(R.id.linear_layout);
            expanderLayout=itemView.findViewById(R.id.expandable_layout);

            linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    com.example.colpex20app_firebase.PrincipalClasses.ClassNeutralizado.ClaseNeutralizado version = subItemList.get(getAdapterPosition());
                    version.setExpandable(!version.isExpandable());
                    notifyItemChanged(getAdapterPosition());
                }
            });
        }


        @Override
        public void onClick(View v) {
            PopupMenu popupMenu = new PopupMenu(v.getContext(), v);
            popupMenu.getMenuInflater().inflate(R.menu.add_edit_item, popupMenu.getMenu());
            popupMenu.setOnMenuItemClickListener(this);
            popupMenu.show();
        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            int position = getAdapterPosition();
            rvItemClick.NeutralizadoMenuItem(position, item, subItemList.get(position));
            return false;
        }
    }
}
