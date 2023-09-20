package com.example.colpex20app_firebase.Adactadores.AdacterNeutralizado;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.colpex20app_firebase.Model.onClickBatch;
import com.example.colpex20app_firebase.R;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class AdacterNeutralizado extends RecyclerView.Adapter<AdacterNeutralizado.ViewHolder> {
    private List<String> ItemList;
    private final onClickBatch clickBatch;
    ArrayList<String> allNamesReactor;
    //conexion Firebase
    DatabaseReference ref;

    public AdacterNeutralizado(List<String> itemList, onClickBatch clickBatch) {
        ItemList = itemList;
        this.clickBatch = clickBatch;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_neutralizado, parent, false);
        return new ViewHolder(view).linkAdapter(this);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //  --------------------------------------------
        holder.Reactor.setText(ItemList.get(position));
        holder.FFAInicial.setText(ItemList.get(position));
        holder.TempTrabajo.setText(ItemList.get(position));
        holder.Lote.setText(ItemList.get(position));
        holder.ConcentracionNaOH.setText(ItemList.get(position));
        holder.AceiteNeutro.setText(ItemList.get(position));
        //----------------------------------------------------
        allNamesReactor = Reactor();
        ArrayAdapter<String> autoCompleteLotNumber = new ArrayAdapter<>(holder.Reactor.getContext(), android.R.layout.simple_list_item_1, allNamesReactor);
        holder.Reactor.setAdapter(autoCompleteLotNumber);
        //----------------------------------------------------
        ref = FirebaseDatabase.getInstance().getReference();
        final ArrayAdapter<String> getNameAcidoFosforico = new ArrayAdapter<>(holder.Lote.getContext(), android.R.layout.simple_list_item_1);
        ref.child("LotesNaOH").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                getNameAcidoFosforico.clear();
                holder.progressBar.setVisibility(View.GONE);
                for (DataSnapshot areaSnapshot : dataSnapshot.getChildren()) {
                    String areaName = areaSnapshot.child("nroloteNaOH").getValue(String.class);
                    getNameAcidoFosforico.add(areaName);
                    holder.progressBar.setVisibility(View.GONE);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        AutoCompleteTextView ACTV = (AutoCompleteTextView) holder.Lote;
        ACTV.setAdapter(getNameAcidoFosforico);
        //-------------------------------------------------
        String Batch1 = ((Activity) holder.context).getIntent().getStringExtra("Batch");
        Integer Batch = Integer.parseInt(Batch1);
        //------------------------------------------------
        String BatchD = ((Activity) holder.context).getIntent().getStringExtra("BatchNeutralizado");
        Integer cantBatch = Integer.parseInt(BatchD);
        //------------------------------------------------
        Button Registrar =  ((Activity) holder.context).findViewById(R.id.RegistarNeutralizado);
        if (cantBatch > Batch) {

            Registrar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new SweetAlertDialog(holder.context, SweetAlertDialog.ERROR_TYPE).setTitleText("Aviso").setContentText("Ya no hay batch para Agregar").show();
                }
            });
            holder.Registrardatos.setEnabled(false);

            holder.txtInputReactor.setEnabled(false);
            holder.txtInputFFAInitial.setEnabled(false);
            holder.txtInputWorkTemperature.setEnabled(false);
            holder.txtInputNaOH.setEnabled(false);
            holder.txtInputLotNaOH.setEnabled(false);
            holder.txtInputNeutralOil.setEnabled(false);

            holder.Reactor.setEnabled(false);
            holder.FFAInicial.setEnabled(false);
            holder.TempTrabajo.setEnabled(false);
            holder.ConcentracionNaOH.setEnabled(false);
            holder.Lote.setEnabled(false);
            holder.AceiteNeutro.setEnabled(false);
        }
    }

    private ArrayList<String> Reactor() {
        ArrayList<String> allNameReactor = new ArrayList<>();
        allNameReactor.add("1");
        allNameReactor.add("2");
        allNameReactor.add("3");
        return allNameReactor;
    }

    @Override
    public int getItemCount() {
        return ItemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        // Inputlayout Neutralizado
        private final TextInputLayout txtInputReactor;
        private final TextInputLayout txtInputFFAInitial;
        private final TextInputLayout txtInputWorkTemperature;
        private final TextInputLayout txtInputNaOH;
        private final TextInputLayout txtInputLotNaOH;
        private final TextInputLayout txtInputNeutralOil;
        //----------------------------------------------------
        Button Registrardatos;
        EditText FFAInicial, TempTrabajo, ConcentracionNaOH,
                AceiteNeutro;
        private AdacterNeutralizado adapter;
        AutoCompleteTextView Reactor, Lote;
        ProgressBar progressBar;
        private final Context context;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            context = itemView.getContext();
            //----------------------------------------------------
            txtInputReactor = itemView.findViewById(R.id.txtInputReactorNeutralized);
            txtInputFFAInitial = itemView.findViewById(R.id.txtInputFFAInitialNeutralized);
            txtInputWorkTemperature = itemView.findViewById(R.id.txtInputWorkTemperatureNeutralized);
            txtInputNaOH = itemView.findViewById(R.id.txtInputNaOHNeutralized);
            txtInputLotNaOH = itemView.findViewById(R.id.txtInputLotNaOHNeutralized);
            txtInputNeutralOil = itemView.findViewById(R.id.txtInputNeutralOilNeutralized);
            //----------------------------------------------------
            //AutoCompleteTextView
            Reactor = itemView.findViewById(R.id.edtReactorNeutralized);
            Lote = itemView.findViewById(R.id.edtLotNaOHNeutralized);
            //texview Neutralizado
            FFAInicial = itemView.findViewById(R.id.edtFFAInitialNeutralized);
            TempTrabajo = itemView.findViewById(R.id.edtWorkTemperatureNeutralized);
            ConcentracionNaOH = itemView.findViewById(R.id.edtNaOHNeutralized);
            AceiteNeutro = itemView.findViewById(R.id.edtNeutralOilNeutralized);
            //----------------------------------------------------
            progressBar = itemView.findViewById(R.id.progress_bar);
            Registrardatos = itemView.findViewById(R.id.btnRegistate);
            Registrardatos.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (clickBatch != null) {
                        int position = getAdapterPosition();

                        if (position != RecyclerView.NO_POSITION) {
                            clickBatch.NeutralizadoMenuItem(position, ItemList.get(position), itemView);
                        }
                    }
                }
            });
            //----------------------------------------------------
            itemView.findViewById(R.id.btnDelete).setOnClickListener(v -> {
                adapter.ItemList.remove(getAdapterPosition());
                adapter.notifyItemRemoved(getAdapterPosition());
            });
        }

        public ViewHolder linkAdapter(AdacterNeutralizado adapter) {
            this.adapter = adapter;
            return this;
        }
    }

}
