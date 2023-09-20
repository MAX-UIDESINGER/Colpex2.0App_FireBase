package com.example.colpex20app_firebase.Adactadores.Adater_Jefe_General;


import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.colpex20app_firebase.PrincipalClasses.ClasesJefeProducion.Lote;
import com.example.colpex20app_firebase.R;

import java.util.ArrayList;
import java.util.Objects;

public class LoteAdacter_Jefe_General extends RecyclerView.Adapter<LoteAdacter_Jefe_General.versionVH> {

    ArrayList<Lote> LoteList;
    private ItemClickListener lListener;

    public LoteAdacter_Jefe_General(ArrayList<Lote> versionList) {
        LoteList = versionList;
    }

    @NonNull
    @Override
    public versionVH onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_observacion_jefe_general, parent, false);
        return new versionVH(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(versionVH holder, int position) {
        Lote nLote = LoteList.get(position);

        holder.NLote.setText(nLote.getNro_lote());
        holder.Producto.setText(nLote.getProducto());
        holder.Cliente.setText(nLote.getCliente());
        holder.Tonelaje.setText(nLote.getTonelaje());
        holder.NBatch.setText("" + nLote.getBatch());
        holder.Fecha.setText(nLote.getFecha());
        holder.AcidoFosforico.setText(nLote.getAcido_fosforico());
        holder.TemTrabajo.setText(nLote.getTemperatura_trabajo());
        holder.Tresidencia.setText(nLote.getT_residencia());
        holder.Descripcion.setText(nLote.getDescripcion());
        //minutos
        holder.minutos.setText(nLote.getMinutos());
        //tex view observaciones
        holder.Descripcion.setText(nLote.getDescripcion());
        String descripcion = nLote.getDescripcion();
        if (Objects.equals(descripcion, "No hay observación")) {
            holder.itemMinuto.setVisibility(View.GONE);
        } else if (descripcion == null){
            holder.Descripcion.setText("No hay observación");
            holder.itemMinuto.setVisibility(View.GONE);
        }else {
            holder.itemMinuto.setVisibility(View.VISIBLE);
        }
        //cargao del usuario
        holder.AreaTrabajo.setText(nLote.getCargoArea());
        String areaTrabajo = nLote.getCargoArea();
        if (Objects.equals(areaTrabajo, "Jefe General")) {
            holder.CheckmarkIngAArea.setVisibility(View.VISIBLE);
        }else {
            holder.CheckmarkIngAArea.setVisibility(View.GONE);
        }
        //Imagen View
        String Imagneurl = nLote.getImagenUrl();
        if (!Imagneurl.equals("")) {
            Glide.with(holder.userImage.getContext())
                    .load(nLote.getImagenUrl())
                    .apply(RequestOptions.circleCropTransform())
                    .into(holder.userImage);
        } else {
            //errorImgein
            Glide.with(holder.userImage.getContext())
                    .load(R.drawable.vectoruser)
                    .into(holder.userImage);
        }
        holder.Nombre.setText(nLote.getUserName());

        //Expandir panel
        boolean isExpandable = LoteList.get(position).isExpandable();
        holder.expanderLayout.setVisibility(isExpandable ? View.VISIBLE : View.GONE);
        if (isExpandable) {
            holder.ivMenu.setImageResource(com.microsoft.fluent.mobile.icons.R.drawable.ic_fluent_chevron_up_24_regular);
        } else {
            holder.ivMenu.setImageResource(com.microsoft.fluent.mobile.icons.R.drawable.ic_fluent_chevron_down_24_regular);
        }
    }

    @Override
    public int getItemCount() {
        return LoteList.size();
    }

    public void setlListener(ItemClickListener lListener) {
        this.lListener = lListener;
    }

    public interface ItemClickListener {
        void onDeleteItem(Lote Lote);

        void onEditItem(Lote Lote);
        // void onCheckItem(CuestionNLote CuestionNLote);
    }

    public class versionVH extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView DatosLote, NLote, Producto, Cliente, Tonelaje, Fecha, NBatch, AcidoFosforico, TemTrabajo,
                Tresidencia, Descripcion,AreaTrabajo;
        ImageView userImage;
        //usuario
        TextView Nombre;
        //button Editar y eliminar
        Button editar, eliminar;
        //minutos
        LinearLayout itemMinuto ;
        TextView minutos;

        //imagen Editar y eliminar
        ImageView ivMenu,CheckmarkIngAArea;

        //expandir texto
        RelativeLayout linearLayout;
        RelativeLayout expanderLayout;

        public versionVH(View itemView) {
            super(itemView);

            DatosLote = itemView.findViewById(R.id.datos);
            NLote = itemView.findViewById(R.id.txvLotesnumero);
            Producto = itemView.findViewById(R.id.txvproducto);
            Cliente = itemView.findViewById(R.id.txvcliente);
            Tonelaje = itemView.findViewById(R.id.txvTonelada);
            Fecha = itemView.findViewById(R.id.txvfecha);
            NBatch = itemView.findViewById(R.id.txvNbatch);
            AcidoFosforico = itemView.findViewById(R.id.txvAcidoFosforico);
            TemTrabajo = itemView.findViewById(R.id.txvTempTrabajo);
            Tresidencia = itemView.findViewById(R.id.txvTResidencia);
            userImage = itemView.findViewById(R.id.userImage);
            Nombre = itemView.findViewById(R.id.NameUser);
            //buttom
            editar = itemView.findViewById(R.id.btnEditar);
            editar.setOnClickListener(this);
            eliminar = itemView.findViewById(R.id.btnDelete);
            eliminar.setOnClickListener(this);

            //tex view observaciones
            Descripcion = itemView.findViewById(R.id.txvLotestext);
            //Area Trabajo
            AreaTrabajo= itemView.findViewById(R.id.areaTrabjo);
            //Imagen
            ivMenu = itemView.findViewById(R.id.ivMenu);
            CheckmarkIngAArea = itemView.findViewById(R.id.CheckmarkIngAArea);

            //minutos
            itemMinuto = itemView.findViewById(R.id.itemMinuto);
            minutos = itemView.findViewById(R.id.txvminutos);

            //expander
            linearLayout = itemView.findViewById(R.id.linear_layout);
            expanderLayout = itemView.findViewById(R.id.expandable_layout);

            linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Lote version = LoteList.get(getAdapterPosition());
                    version.setExpandable(!version.isExpandable());
                    notifyItemChanged(getAdapterPosition());
                }
            });

        }

        @SuppressLint("NonConstantResourceId")
        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            switch (v.getId()) {
                case R.id.btnEditar:
                    if (lListener != null)
                        lListener.onEditItem(LoteList.get(position));
                    break;
                case R.id.btnDelete:
                    if (lListener != null)
                        lListener.onDeleteItem(LoteList.get(position));
                    break;
                default:
                    break;
            }
        }
    }
}
