package com.example.colpex20app_firebase.Adactadores.AdacterBlanqueado;

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
import com.example.colpex20app_firebase.PrincipalClasses.ClassBlanqueado.ClaseBlanqueado;
import com.example.colpex20app_firebase.R;

import java.util.Collections;
import java.util.List;


public class SubItemAdacterBlanqueado extends RecyclerView.Adapter<SubItemAdacterBlanqueado.versionVH> {

    private List<ClaseBlanqueado> subItemList;
    private final RvItemClick rvItemClick;
    public SubItemAdacterBlanqueado(List<ClaseBlanqueado> subItemListBlanqueado, ItemAdacterBlanqueado rvItemClick) {
        this.subItemList = subItemListBlanqueado;
        this.rvItemClick = rvItemClick;
    }

    @NonNull
    @Override
    public versionVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sub_item_blanqueado_op, parent, false);
        return new versionVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull versionVH holder, int position) {
      ClaseBlanqueado blanqueado = subItemList.get(position);

        holder.Lote.setText(blanqueado.getNumeroLote());
        holder.Reactor.setText(blanqueado.getReactor());
        holder.Batch.setText(blanqueado.getContadorBatch());
        holder.Carga.setText(blanqueado.getCarga());
        holder.Vacio.setText(blanqueado.getVacio());
        holder.TempTrabajo.setText(blanqueado.getTempTrabajo());
        holder.Arcilla.setText(blanqueado.getArcilla());
        holder.LoteArcilla.setText(blanqueado.getALLote());
        holder.CarbonActivado.setText(blanqueado.getCarbonActivado());
        holder.lotecarbon.setText(blanqueado.getCALote());
        holder.silice.setText(blanqueado.getSilice());
        holder.Lotesilice.setText(blanqueado.getSLote());
        holder.FFAFinal.setText(blanqueado.getFFAFinal());
        holder.ColorFinal.setText(blanqueado.getColorFinal());
        holder.AnisidinaFinal.setText(blanqueado.getAndisidinaFinal());

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

    public class versionVH extends RecyclerView.ViewHolder implements View.OnClickListener, PopupMenu.OnMenuItemClickListener {

        TextView Reactor, Batch, Carga, Vacio, TempTrabajo, Arcilla,
                LoteArcilla, CarbonActivado, lotecarbon, silice,
                Lotesilice, FFAFinal, ColorFinal, AnisidinaFinal;
        ImageView userImage;
        //usuario
        TextView Nombre;
        TextView Lote, Fecha;
        //imagen Editar y eliminar
        Button ivMenu;
        //expandir texto
        LinearLayout linearLayout;
        RelativeLayout expanderLayout;
        //Imagen expander
        ImageView iconExpand;
        //Preogress Bar
        ProgressBar actualizar;

        public versionVH(@NonNull View itemView) {
            super(itemView);
            //cargar imagen
            actualizar = itemView.findViewById(R.id.progress_bar);
            //imagen expander
            iconExpand = itemView.findViewById(R.id.expand_collapse_icon);
            //
            Lote = itemView.findViewById(R.id.txvnumerlote);
            Reactor = itemView.findViewById(R.id.txvReactor);
            Batch = itemView.findViewById(R.id.txvBatch);
            Carga = itemView.findViewById(R.id.txvCarga);
            Vacio = itemView.findViewById(R.id.txvVacio);
            TempTrabajo = itemView.findViewById(R.id.txvTemp_Trabajo);
            Arcilla = itemView.findViewById(R.id.txvArcilla);
            LoteArcilla = itemView.findViewById(R.id.txvLoteArcilla);
            CarbonActivado = itemView.findViewById(R.id.txvCarbon_Activado);
            lotecarbon = itemView.findViewById(R.id.txvcarbonLote);
            silice = itemView.findViewById(R.id.txvsilice);
            Lotesilice = itemView.findViewById(R.id.txvsilicelote);
            FFAFinal = itemView.findViewById(R.id.txvFFAFinal);
            ColorFinal = itemView.findViewById(R.id.txvColorFinal);
            AnisidinaFinal = itemView.findViewById(R.id.txvAnisidinaFinal);
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
                    ClaseBlanqueado version = subItemList.get(getAdapterPosition());
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
            rvItemClick.BlanqueadoMenuItem(position, item, subItemList.get(position));
            return false;
        }
    }
}
