package com.example.colpex20app_firebase.Adactadores.AdacterDesgomado;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.colpex20app_firebase.Model.RvItemClick;
import com.example.colpex20app_firebase.OperadorResponsable.AgregarDatosTrabajores.FragmentDesgomadoTrabajo;
import com.example.colpex20app_firebase.OperadorResponsable.Reportes.PanelImprimir;
import com.example.colpex20app_firebase.PrincipalClasses.ClassDesgomado.ItemDesgomado;
import com.example.colpex20app_firebase.R;

import java.util.List;

public abstract class ItemAdacterDesgomado extends RecyclerView.Adapter<ItemAdacterDesgomado.ItemViewHolder>implements RvItemClick {

    private List<ItemDesgomado> itemListNeutralizado;

    public ItemAdacterDesgomado(List<ItemDesgomado> itemListNeutralizado, FragmentDesgomadoTrabajo fragmentDesgomadoTrabajo) {
        this.itemListNeutralizado = itemListNeutralizado;
    }

    @NonNull
    @Override
    public ItemAdacterDesgomado.ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_desgomado_op, parent, false);
        return new ItemViewHolder(view);
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onBindViewHolder(@NonNull ItemAdacterDesgomado.ItemViewHolder holder, int position) {
        ItemDesgomado item = itemListNeutralizado.get(position);
        holder.tvItemTitle.setText(item.getItemTitleLote());

        LinearLayoutManager layoutManager = new LinearLayoutManager(holder.itemView.getContext(), LinearLayoutManager.VERTICAL,false);
        //LinearLayoutManager layoutManager = new LinearLayoutManager(holder.itemView.getContext());
        layoutManager.setInitialPrefetchItemCount(item.getSubItemListDesgomado().size());
        SubItemAdacterDesgomado childAdapter = new SubItemAdacterDesgomado(item.getSubItemListDesgomado(),this);
        holder.childRecyclerView.setHasFixedSize(false);
        holder.childRecyclerView.setLayoutManager(layoutManager);
        holder.childRecyclerView.setAdapter(childAdapter);
        childAdapter.notifyDataSetChanged();

        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Lote = item.getItemTitleLote();
                Intent siguiente = new Intent(view.getContext(), PanelImprimir.class);
                siguiente.putExtra("NumeroLote", String.valueOf(Lote));
                view.getContext().startActivity(siguiente);
            }
        });

    }

    @Override
    public int getItemCount() {
        return itemListNeutralizado.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        private TextView tvItemTitle;
        private RecyclerView childRecyclerView;
        private Button button;
        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            button = itemView.findViewById(R.id.ivMenuPanel);
            tvItemTitle = itemView.findViewById(R.id.tv_item_title);
            childRecyclerView = itemView.findViewById(R.id.rv_sub_item);
        }
    }
}
