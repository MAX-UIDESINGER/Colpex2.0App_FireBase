package com.example.colpex20app_firebase.Adactadores.Adacter_JefeProducion;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.colpex20app_firebase.PrincipalClasses.ClasesJefeProducion.CuestionCliente;
import com.example.colpex20app_firebase.R;

import java.util.ArrayList;

public class ClienteAdacter extends RecyclerView.Adapter<ClienteAdacter.ClienteViewHolder> {
    ArrayList<CuestionCliente> ListaCliente;
   private ItemClickListener CListener;

    public ClienteAdacter(ArrayList<CuestionCliente> listaCliente) {
        ListaCliente = listaCliente;
    }

    @NonNull
    @Override
    public ClienteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_consulta_cliente,parent,false);
        return new ClienteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ClienteViewHolder holder, int position) {
        CuestionCliente cliente = ListaCliente.get(position);
        holder.nombre.setText(cliente.getNombre());
        holder.correo.setText(cliente.getCorreo());
        holder.ciudad.setText(cliente.getCiudad());
        holder.pais.setText(cliente.getPais());
        holder.fecha.setText(cliente.getFechayhora());
    }

    @Override
    public int getItemCount() {
        return ListaCliente.size();
    }


    public void setCListener(ItemClickListener CListener) {
        this.CListener = CListener;
    }

    public interface ItemClickListener {
        void onDeleteItem(CuestionCliente CuestionCliente);
        void onEditItem(CuestionCliente CuestionCliente);
        //   void onCheckItem(CuestionNLote CuestionNLote);
    }


    public class ClienteViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView nombre,correo,ciudad,pais,fecha;
        Button editar, eliminar;

        public ClienteViewHolder(@NonNull View itemView) {
            super(itemView);
            nombre = itemView.findViewById(R.id.txvLotesnumero);
            correo = itemView.findViewById(R.id.txvproducto);
            ciudad = itemView.findViewById(R.id.txvcliente);
            pais = itemView.findViewById(R.id.txvPais);
            fecha = itemView.findViewById(R.id.txvfecha);
            //buttom
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
                    if (CListener != null)
                        CListener.onEditItem(ListaCliente.get(position));
                    break;
                case R.id.btnDelete:
                    if (CListener != null)
                        CListener.onDeleteItem(ListaCliente.get(position));
                    break;
                default:
                    break;
            }
        }
    }
}
