package com.example.colpex20app_firebase.Adactadores.Adacter_JefeProducion;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.colpex20app_firebase.PrincipalClasses.ClasesJefeProducion.CuestionNLote;
import com.example.colpex20app_firebase.R;

import java.util.ArrayList;

public class NLoteAdacter extends RecyclerView.Adapter<NLoteAdacter.LoteNViewHolder> {
    ArrayList<CuestionNLote> ListaNLote;
    private ItemClickListener mListener;

    public NLoteAdacter(ArrayList<CuestionNLote> listaNLote) {
        ListaNLote = listaNLote;
    }

    @NonNull
    @Override
    public LoteNViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_consulta_nlote, parent, false);
        return new LoteNViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LoteNViewHolder holder, int position) {
        CuestionNLote nLote = ListaNLote.get(position);
        holder.nombreNLote.setText(nLote.getNrolote());
        holder.fecha.setText(nLote.getFechayhora());
    }


    @Override
    public int getItemCount() {
        return ListaNLote.size();
    }


    public void setmListener(ItemClickListener mListener) {
        this.mListener = mListener;
    }

    public interface ItemClickListener {
        void onDeleteItem(CuestionNLote CuestionNLote);
        void onEditItem(CuestionNLote CuestionNLote);
        //   void onCheckItem(CuestionNLote CuestionNLote);
    }

    public class LoteNViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView nombreNLote ,fecha;
        Button editar, eliminar;

        public LoteNViewHolder(@NonNull View itemView) {
            super(itemView);
            nombreNLote = itemView.findViewById(R.id.txvLotesnumero);
            fecha = itemView.findViewById(R.id.txvfecha);
            //botones
            editar = itemView.findViewById(R.id.btnEditar);
            editar.setOnClickListener(this);
            eliminar = itemView.findViewById(R.id.btnDelete);
            eliminar.setOnClickListener(this);

        }

        @SuppressLint("NonConstantResourceId")
        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            switch (v.getId()) {
                case R.id.btnEditar:
                    if (mListener != null)
                        mListener.onEditItem(ListaNLote.get(position));
                    break;
                case R.id.btnDelete:
                    if (mListener != null)
                        mListener.onDeleteItem(ListaNLote.get(position));
                    break;
                default:
                    break;
            }
        }
    }
}
