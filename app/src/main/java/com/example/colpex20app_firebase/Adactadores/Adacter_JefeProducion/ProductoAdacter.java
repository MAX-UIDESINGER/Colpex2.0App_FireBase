package com.example.colpex20app_firebase.Adactadores.Adacter_JefeProducion;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.colpex20app_firebase.PrincipalClasses.ClasesJefeProducion.CuestionProducto;
import com.example.colpex20app_firebase.R;

import java.util.ArrayList;

public class ProductoAdacter extends RecyclerView.Adapter<ProductoAdacter.ProductoViewHolder>{
    ArrayList<CuestionProducto> ListaProducto;
    private ItemClickListener pListener;
    public ProductoAdacter(ArrayList<CuestionProducto> listaProducto) {
        ListaProducto = listaProducto;
    }

    @NonNull
    @Override
    public ProductoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_consulta_producto,parent,false);
        return new ProductoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductoViewHolder holder, int position) {
        CuestionProducto producto = ListaProducto.get(position);
        holder.nombre.setText(producto.getNombre());
        holder.descripcion.setText(producto.getDescripcion());
        holder.stock.setText(producto.getStock());
        holder.fecha.setText(producto.getFechayhora());
    }

    @Override
    public int getItemCount() {
        return ListaProducto.size();
    }


    public void setpListener(ItemClickListener pListener) {
        this.pListener = pListener;
    }

    public interface ItemClickListener {
        void onDeleteItem(CuestionProducto CuestionProducto);
        void onEditItem(CuestionProducto CuestionProducto);
        //   void onCheckItem(CuestionProducto CuestionProducto);
    }

    public class ProductoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView nombre,descripcion,stock,fecha;
        Button editar,eliminar;
        public ProductoViewHolder(@NonNull View itemView) {
            super(itemView);
            nombre = itemView.findViewById(R.id.txvLotesnumero);
            descripcion = itemView.findViewById(R.id.txvproducto);
            stock = itemView.findViewById(R.id.txvcliente);
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
                    if (pListener != null)
                        pListener.onEditItem(ListaProducto.get(position));
                    break;
                case R.id.btnDelete:
                    if (pListener != null)
                        pListener.onDeleteItem(ListaProducto.get(position));
                    break;
                default:
                    break;
            }
        }
    }
}
