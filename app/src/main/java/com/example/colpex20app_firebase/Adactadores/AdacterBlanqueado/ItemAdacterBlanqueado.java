package com.example.colpex20app_firebase.Adactadores.AdacterBlanqueado;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.colpex20app_firebase.Model.RvItemClick;
import com.example.colpex20app_firebase.OperadorResponsable.AgregarDatosTrabajores.FragmentBlanqueadoTrabajo;
import com.example.colpex20app_firebase.PrincipalClasses.ClassBlanqueado.ItemBlanqueado;
import com.example.colpex20app_firebase.R;

import java.util.ArrayList;
import java.util.List;

public abstract class ItemAdacterBlanqueado extends RecyclerView.Adapter<ItemAdacterBlanqueado.ItemViewHolder> implements RvItemClick {

    private List<ItemBlanqueado> itemListBlanqueado;

    protected ItemAdacterBlanqueado(List<ItemBlanqueado> itemListBlanqueado, FragmentBlanqueadoTrabajo fragmentBlanqueadoTrabajo) {
        this.itemListBlanqueado = itemListBlanqueado;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_blanqueado_op, parent, false);
        return new ItemViewHolder(view);
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        ItemBlanqueado item = itemListBlanqueado.get(position);
        holder.tvItemTitle.setText(item.getItemTitleLote());

        LinearLayoutManager layoutManager = new LinearLayoutManager(holder.itemView.getContext(), LinearLayoutManager.VERTICAL, false);
        layoutManager.setInitialPrefetchItemCount(item.getSubItemListBlanqueado().size());
        SubItemAdacterBlanqueado childAdapter = new SubItemAdacterBlanqueado(item.getSubItemListBlanqueado(), this);
        holder.childRecyclerView.setHasFixedSize(false);
        holder.childRecyclerView.setLayoutManager(layoutManager);
        holder.childRecyclerView.setAdapter(childAdapter);
        childAdapter.notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return itemListBlanqueado.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        private TextView tvItemTitle;
        private RecyclerView childRecyclerView;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            tvItemTitle = itemView.findViewById(R.id.tv_item_title);
            childRecyclerView = itemView.findViewById(R.id.rv_sub_item);
        }
    }
}
