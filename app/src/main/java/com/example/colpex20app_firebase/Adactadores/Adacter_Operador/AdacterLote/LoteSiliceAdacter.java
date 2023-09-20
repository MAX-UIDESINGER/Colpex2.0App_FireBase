package com.example.colpex20app_firebase.Adactadores.Adacter_Operador.AdacterLote;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.colpex20app_firebase.PrincipalClasses.ClaseLote.LoteSilliceClass;
import com.example.colpex20app_firebase.R;

import java.util.ArrayList;

public class LoteSiliceAdacter extends RecyclerView.Adapter<LoteSiliceAdacter.SiliceViewHolder> {
    ArrayList<LoteSilliceClass> SilliceList;
    private ItemClickListener mListener;

    public LoteSiliceAdacter(ArrayList<LoteSilliceClass> silliceList) {
        SilliceList = silliceList;
    }

    @NonNull
    @Override
    public SiliceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_silice, parent, false);
        return new SiliceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SiliceViewHolder holder, int position) {
        LoteSilliceClass nLoteSillice = SilliceList.get(position);
        holder.nombreNLote.setText(nLoteSillice.getNroloteSillice());
        holder.fecha.setText(nLoteSillice.getFechayhora());
    }

    @Override
    public int getItemCount() {
        return SilliceList.size();
    }

    public void setmListener(ItemClickListener mListener) {
        this.mListener = mListener;
    }

    public interface ItemClickListener {
        void onDeleteItem(LoteSilliceClass LoteSilliceClass);

        void onEditItem(LoteSilliceClass LoteSilliceClass);
    }

    public class SiliceViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView nombreNLote, fecha;
        Button editar, eliminar;

        public SiliceViewHolder(@NonNull View itemView) {
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
                        mListener.onEditItem(SilliceList.get(position));
                    break;
                case R.id.btnDelete:
                    if (mListener != null)
                        mListener.onDeleteItem(SilliceList.get(position));
                    break;
                default:
                    break;
            }
        }
    }
}
