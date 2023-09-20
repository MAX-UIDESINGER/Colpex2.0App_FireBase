package com.example.colpex20app_firebase.Adactadores.Adacter_Operador.AdacterLote;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.colpex20app_firebase.PrincipalClasses.ClaseLote.LoteNaOHClass;
import com.example.colpex20app_firebase.R;

import java.util.ArrayList;


public class LoteNaOHAdacter extends RecyclerView.Adapter<LoteNaOHAdacter.NaOHViewHolder> {

    ArrayList<LoteNaOHClass> NaOHList;
    private ItemClickListener mListener;

    public LoteNaOHAdacter(ArrayList<LoteNaOHClass> naOHList) {
        NaOHList = naOHList;
    }

    @NonNull
    @Override
    public NaOHViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_naoh, parent, false);
        return new NaOHViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NaOHViewHolder holder, int position) {
        LoteNaOHClass nLoteNaOH = NaOHList.get(position);
        holder.nombreNLote.setText(nLoteNaOH.getNroloteNaOH());
        holder.fecha.setText(nLoteNaOH.getFechayhora());
    }

    @Override
    public int getItemCount() {
        return NaOHList.size();
    }
    public void setmListener(ItemClickListener mListener) {
        this.mListener = mListener;
    }

    public interface ItemClickListener {
        void onDeleteItem(LoteNaOHClass LoteNaOHClass);

        void onEditItem(LoteNaOHClass LoteNaOHClass);
        // void onCheckItem(CuestionNLote CuestionNLote);
    }

    public class NaOHViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView nombreNLote ,fecha;
        Button editar, eliminar;

        public NaOHViewHolder(@NonNull View itemView) {
            super(itemView);
            nombreNLote = itemView.findViewById(R.id.txvLotesnumero);
            fecha = itemView.findViewById(R.id.txvfecha);
            //botones
            editar = itemView.findViewById(R.id.btnEditar);
            editar.setOnClickListener(this);
            eliminar = itemView.findViewById(R.id.btnDelete);
            eliminar.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            switch (v.getId()) {
                case R.id.btnEditar:
                    if (mListener != null)
                        mListener.onEditItem(NaOHList.get(position));
                    break;
                case R.id.btnDelete:
                    if (mListener != null)
                        mListener.onDeleteItem(NaOHList.get(position));
                    break;
                default:
                    break;
            }
        }
    }
}
