package com.example.colpex20app_firebase.Adactadores.Adacter_Operador.AdacterLote;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.colpex20app_firebase.PrincipalClasses.ClaseLote.LoteArcillaClass;
import com.example.colpex20app_firebase.R;

import java.util.ArrayList;


public class LoteArcillaAdacter extends RecyclerView.Adapter<LoteArcillaAdacter.ArcillaViewHolder>{

    ArrayList<LoteArcillaClass> ArcillaList;
    private ItemClickListener mListener;

    public LoteArcillaAdacter(ArrayList<LoteArcillaClass> arcillaList) {
        ArcillaList = arcillaList;
    }

    @NonNull
    @Override
    public ArcillaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_arcilla, parent, false);
        return new ArcillaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ArcillaViewHolder holder, int position) {
        LoteArcillaClass nLoteArcilla = ArcillaList.get(position);
        holder.nombreNLote.setText(nLoteArcilla.getNroloteArcilla());
        holder.fecha.setText(nLoteArcilla.getFechayhora());
    }

    @Override
    public int getItemCount() {
        return ArcillaList.size();
    }
    public void setmListener(ItemClickListener mListener) {
        this.mListener = mListener;
    }

    public interface ItemClickListener {
        void onDeleteItem(LoteArcillaClass LoteArcillaClass);

        void onEditItem(LoteArcillaClass LoteArcillaClass);
        // void onCheckItem(CuestionNLote CuestionNLote);
    }

    public class ArcillaViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView nombreNLote ,fecha;
        Button editar, eliminar;


        public ArcillaViewHolder(@NonNull View itemView) {
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
                        mListener.onEditItem(ArcillaList.get(position));
                    break;
                case R.id.btnDelete:
                    if (mListener != null)
                        mListener.onDeleteItem(ArcillaList.get(position));
                    break;
                default:
                    break;
            }
        }
    }
}
