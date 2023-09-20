package com.example.colpex20app_firebase.Adactadores.Adacter_Operador.AdacterLote;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.colpex20app_firebase.PrincipalClasses.ClaseLote.LoteCarbonActivadoClass;
import com.example.colpex20app_firebase.R;

import java.util.ArrayList;


public class LoteCarbonActivoAdacter extends RecyclerView.Adapter<LoteCarbonActivoAdacter.CarbonActivoViewHolder> {

    ArrayList<LoteCarbonActivadoClass> CarbonActivoList;
    private ItemClickListener mListener;

    public LoteCarbonActivoAdacter(ArrayList<LoteCarbonActivadoClass> carbonActivoList) {
        CarbonActivoList = carbonActivoList;
    }

    @NonNull
    @Override
    public CarbonActivoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_carbon_activado, parent, false);
        return new CarbonActivoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CarbonActivoViewHolder holder, int position) {
        LoteCarbonActivadoClass nLoteCarbonActivo = CarbonActivoList.get(position);
        holder.nombreNLote.setText(nLoteCarbonActivo.getNroloteCarbonActivado());
        holder.fecha.setText(nLoteCarbonActivo.getFechayhora());
    }

    @Override
    public int getItemCount() {
        return CarbonActivoList.size();
    }

    public void setmListener(ItemClickListener mListener) {
        this.mListener = mListener;
    }

    public interface ItemClickListener {
        void onDeleteItem(LoteCarbonActivadoClass LoteCarbonActivadoClass);

        void onEditItem(LoteCarbonActivadoClass LoteCarbonActivadoClass);
        // void onCheckItem(CuestionNLote CuestionNLote);
    }

    public class CarbonActivoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView nombreNLote ,fecha;
        Button editar, eliminar;

        public CarbonActivoViewHolder(@NonNull View itemView) {
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
                        mListener.onEditItem(CarbonActivoList.get(position));
                    break;
                case R.id.btnDelete:
                    if (mListener != null)
                        mListener.onDeleteItem(CarbonActivoList.get(position));
                    break;
                default:
                    break;
            }
        }
    }
}
