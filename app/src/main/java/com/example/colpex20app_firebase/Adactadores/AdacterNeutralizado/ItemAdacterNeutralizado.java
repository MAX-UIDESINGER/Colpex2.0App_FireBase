package com.example.colpex20app_firebase.Adactadores.AdacterNeutralizado;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.colpex20app_firebase.Model.RvItemClick;
import com.example.colpex20app_firebase.OperadorResponsable.AgregarDatosTrabajores.FragmentNeutralizadoTrabajo;
import com.example.colpex20app_firebase.PrincipalClasses.ClassNeutralizado.ItemNeutralizado;
import com.example.colpex20app_firebase.R;

import java.util.List;

public abstract class ItemAdacterNeutralizado extends RecyclerView.Adapter<ItemAdacterNeutralizado.ItemViewHolder> implements RvItemClick {

    private List<ItemNeutralizado> itemListNeutralizado;

    public ItemAdacterNeutralizado(List<ItemNeutralizado> itemListNeutralizado, FragmentNeutralizadoTrabajo fragmentNeutralizadoTrabajo) {
        this.itemListNeutralizado = itemListNeutralizado;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_neutralizado_op, parent, false);
        return new ItemViewHolder(view);
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        ItemNeutralizado item = itemListNeutralizado.get(position);
        holder.tvItemTitle.setText(item.getItemTitleLote());

        LinearLayoutManager layoutManager = new LinearLayoutManager(holder.itemView.getContext(), LinearLayoutManager.VERTICAL,false);
        layoutManager.setInitialPrefetchItemCount(item.getSubItemListNeutralizado().size());
        SubItemAdacterNeutralizado childAdapter = new SubItemAdacterNeutralizado(item.getSubItemListNeutralizado(),this);
        holder.childRecyclerView.setHasFixedSize(false);
        holder.childRecyclerView.setLayoutManager(layoutManager);
        holder.childRecyclerView.setAdapter(childAdapter);
        childAdapter.notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (itemListNeutralizado != null){
            return itemListNeutralizado.size();
        }else{
            return 0;
        }
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
