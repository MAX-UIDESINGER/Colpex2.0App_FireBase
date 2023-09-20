package com.example.colpex20app_firebase.Adactadores.Adacter_Operador;


import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;

import com.bumptech.glide.request.target.Target;
import com.example.colpex20app_firebase.OperadorResponsable.PanelAgregarBatch;
import com.example.colpex20app_firebase.PrincipalClasses.ClasesJefeProducion.Lote;
import com.example.colpex20app_firebase.R;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Objects;

public class LoteAdacter_op extends RecyclerView.Adapter<LoteAdacter_op.versionVH> {

    ArrayList<Lote> LoteList;

    public LoteAdacter_op(ArrayList<Lote> versionList) {
        LoteList = versionList;
    }

    @NonNull
    @Override
    public versionVH onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_observacion_op, parent, false);
        return new versionVH(view);
    }

    @Override
    public void onBindViewHolder(versionVH holder, int position) {
        Lote nLote = LoteList.get(position);
        holder.NLote.setText(nLote.getNro_lote());
        holder.Producto.setText(nLote.getProducto());
        holder.Cliente.setText(nLote.getCliente());
        holder.Tonelaje.setText(nLote.getTonelaje());
        //  --------------------------------------------
        holder.Nbatch.setText(nLote.getBatch());
        //  --------------------------------------------
        holder.NBatchDesgomado.setText(nLote.getBatchDesgomado());
        holder.NBatchNeutralizado.setText(nLote.getBatchNeutralizado());
        holder.NBatchBlanqueado.setText(nLote.getBatchBlanqueado());
        //  --------------------------------------------
        String Batch = nLote.getBatch();
        String Batchdesgomado = nLote.getBatchDesgomado();
        String Batchneutralizado = nLote.getBatchNeutralizado();
        String Batchblanqueado = nLote.getBatchBlanqueado();

        int cant_batch = Integer.parseInt(Batch);
        int cant_batch_desgomado = Integer.parseInt(Batchdesgomado);
        int cant_batch_neutralizado = Integer.parseInt(Batchneutralizado);
        int cant_batch_blanqueado = Integer.parseInt(Batchblanqueado);
        if (cant_batch_desgomado > cant_batch && cant_batch_neutralizado > cant_batch && cant_batch_blanqueado > cant_batch) {
            //ventana de aviso de bien
            holder.ckecBacht.setVisibility(View.INVISIBLE);
            holder.DismissBacht.setVisibility(View.VISIBLE);
        } else {
            holder.ckecBacht.setVisibility(View.VISIBLE);
            holder.DismissBacht.setVisibility(View.INVISIBLE);
        }

        holder.Fecha.setText(nLote.getFecha());
        holder.AcidoFosforico.setText(nLote.getAcido_fosforico());
        holder.TemTrabajo.setText(nLote.getTemperatura_trabajo());
        holder.Tresidencia.setText(nLote.getT_residencia());
        String Imagneurl = nLote.getImagenUrl();
        if (!Imagneurl.equals("")) {
            Glide.with(holder.userImage.getContext())
                    .load(nLote.getImagenUrl())
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
        holder.Nombre.setText(nLote.getUserName());
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  --------------------------------------------
                String Batch = nLote.getBatch();
                //  --------------------------------------------
                String Identificar_id = nLote.getId();
                String BatchDesgomado = nLote.getBatchDesgomado();
                String BatchNeutralizado = nLote.getBatchNeutralizado();
                String BatchBlanqueado = nLote.getBatchBlanqueado();
                String Lote = nLote.getNro_lote();

                String Acidofosforico = nLote.getAcido_fosforico();
                String Temptrabajo = nLote.getTemperatura_trabajo();
                String Tresidencia = nLote.getT_residencia();

                CardView carview = holder.cardView;

                int cant_batch = Integer.parseInt(Batch);
                int cant_batch_desgomado = Integer.parseInt(BatchDesgomado);
                int cant_batch_neutralizado = Integer.parseInt(BatchNeutralizado);
                int cant_batch_blanqueado = Integer.parseInt(BatchBlanqueado);

                if (cant_batch_desgomado > cant_batch && cant_batch_neutralizado > cant_batch && cant_batch_blanqueado > cant_batch) {
                    Snackbar.make(carview, "Ya no te quedan batch por Registrar : " + cant_batch_desgomado, Snackbar.LENGTH_LONG).show();
                } else {
                    Toast.makeText(v.getContext(), "Lote Seleccionado : " + Lote, Toast.LENGTH_SHORT).show();
                    Intent siguiente = new Intent(v.getContext(), PanelAgregarBatch.class);
                    siguiente.putExtra("id", String.valueOf(Identificar_id));
                    siguiente.putExtra("Batch", String.valueOf(cant_batch));
                    siguiente.putExtra("BatchDesgomado", String.valueOf(cant_batch_desgomado));
                    siguiente.putExtra("BatchNeutralizado", String.valueOf(cant_batch_neutralizado));
                    siguiente.putExtra("BatchBlanqueado", String.valueOf(cant_batch_blanqueado));
                    siguiente.putExtra("NumeroLote", String.valueOf(Lote));
                    siguiente.putExtra("AcidoFosforico", String.valueOf(Acidofosforico));
                    siguiente.putExtra("TempTrabajo", String.valueOf(Temptrabajo));
                    siguiente.putExtra("TResidencia", String.valueOf(Tresidencia));
                    v.getContext().startActivity(siguiente);
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return LoteList.size();
    }

    public class versionVH extends RecyclerView.ViewHolder {
        //LInearLayoutCar
        CardView cardView;
        //Nuevo
        TextView Nbatch;
        ProgressBar actualizar;
        //
        TextView DatosLote, NLote, Producto, Cliente, Tonelaje, Fecha, AcidoFosforico, TemTrabajo, Tresidencia;
        //BACTCH
        TextView NBatchDesgomado, NBatchNeutralizado, NBatchBlanqueado;
        ImageView userImage;
        //usuario
        TextView Nombre;
        //Imagenckeck
        ImageView ckecBacht, DismissBacht;

        public versionVH(View itemView) {
            super(itemView);
            //cargar imagen
            actualizar = itemView.findViewById(R.id.progress_bar);
            //
            cardView = itemView.findViewById(R.id.cardView);
            DatosLote = itemView.findViewById(R.id.datos);
            NLote = itemView.findViewById(R.id.txvLotesnumero);
            Producto = itemView.findViewById(R.id.txvproducto);
            Cliente = itemView.findViewById(R.id.txvcliente);
            Tonelaje = itemView.findViewById(R.id.txvTonelada);
            Fecha = itemView.findViewById(R.id.txvfecha);
            //Batch
            Nbatch = itemView.findViewById(R.id.txvNbatch);
            //Batch d , n ,b
            NBatchDesgomado = itemView.findViewById(R.id.txvNbatchDesgomado1);
            NBatchNeutralizado = itemView.findViewById(R.id.txvNbatchNeutralizado1);
            NBatchBlanqueado = itemView.findViewById(R.id.txvNbatchBlanqueado1);
            //
            AcidoFosforico = itemView.findViewById(R.id.txvAcidoFosforico);
            TemTrabajo = itemView.findViewById(R.id.txvTempTrabajo);
            Tresidencia = itemView.findViewById(R.id.txvTResidencia);
            userImage = itemView.findViewById(R.id.userImage);
            Nombre = itemView.findViewById(R.id.NameUser);
            //verificarBacht
            ckecBacht = itemView.findViewById(R.id.ckecBacht);
            DismissBacht = itemView.findViewById(R.id.DismissBacht);

        }

    }

}
