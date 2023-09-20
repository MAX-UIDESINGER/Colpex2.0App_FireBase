package com.example.colpex20app_firebase.Adactadores.AdacterDesgomado;

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

public class AdacterDesgomado extends RecyclerView.Adapter<AdacterDesgomado.ViewHolder> {
    private final List<String> ItemList;
    private final onClickBatch clickBatch;
    ArrayList<String> allNamesReactor;
    //conexion Firebase
    DatabaseReference ref;

    public AdacterDesgomado(List<String> itemList, onClickBatch clickBatch) {
        ItemList = itemList;
        this.clickBatch = clickBatch;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_desgomado, parent, false);
        return new ViewHolder(view).linkAdapter(this);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //  --------------------------------------------
        holder.Reactor.setText(ItemList.get(position));
        holder.Tonelaje.setText(ItemList.get(position));
        holder.AcidoFosforico.setText(ItemList.get(position));
        holder.Lote.setText(ItemList.get(position));
        holder.TempTrabajo.setText(ItemList.get(position));
        holder.TResidencia.setText(ItemList.get(position));
        //----------------------------------------------------
        allNamesReactor = Reactor();
        ArrayAdapter<String> autoCompleteLotNumber = new ArrayAdapter<>(holder.Reactor.getContext(), android.R.layout.simple_list_item_1, allNamesReactor);
        holder.Reactor.setAdapter(autoCompleteLotNumber);
        //----------------------------------------------------
        ref = FirebaseDatabase.getInstance().getReference();
        final ArrayAdapter<String> getNameAcidoFosforico = new ArrayAdapter<>(holder.Lote.getContext(), android.R.layout.simple_list_item_1);
        ref.child("LotesAcidoFosforico").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                getNameAcidoFosforico.clear();
                holder.progressBar.setVisibility(View.GONE);
                for (DataSnapshot areaSnapshot : dataSnapshot.getChildren()) {
                    String areaName = areaSnapshot.child("nroloteAcidoFosforico").getValue(String.class);
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
        String AcidoFosforico = ((Activity) holder.context).getIntent().getStringExtra("AcidoFosforico");
        holder.AcidoFosforico.setText(AcidoFosforico);
        //-------------------------------------------------
        String TempTrabajo = ((Activity) holder.context).getIntent().getStringExtra("TempTrabajo");
        holder.TempTrabajo.setText(TempTrabajo);
        //-------------------------------------------------
        String TResidencia = ((Activity) holder.context).getIntent().getStringExtra("TResidencia");
        holder.TResidencia.setText(TResidencia);
        //------------------------------------------------
        String Batch1 = ((Activity) holder.context).getIntent().getStringExtra("Batch");
        Integer Batch = Integer.parseInt(Batch1);
        //------------------------------------------------
        String BatchD = ((Activity) holder.context).getIntent().getStringExtra("BatchDesgomado");
        Integer cantBatch = Integer.parseInt(BatchD);
        //------------------------------------------------
        Button Registrar =  ((Activity) holder.context).findViewById(R.id.RegistarDesgomado);

        if (cantBatch > Batch) {
            Registrar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new SweetAlertDialog(holder.context, SweetAlertDialog.ERROR_TYPE).setTitleText("Aviso").setContentText("Ya no hay batch para Agregar").show();
                }
            });
            holder.Registrardatos.setEnabled(false);

            holder.AcidoFosforico.setText("");
            holder.TempTrabajo.setText("");
            holder.TResidencia.setText("");

            holder.txtInputReactor.setEnabled(false);
            holder.txtInputTonelaje.setEnabled(false);
            holder.txtInputAcidoFosforico.setEnabled(false);
            holder.txtInputLote.setEnabled(false);
            holder.txtInputTrabajo.setEnabled(false);
            holder.txtInputTResidencia.setEnabled(false);

            holder.Reactor.setEnabled(false);
            holder.Tonelaje.setEnabled(false);
            holder.AcidoFosforico.setEnabled(false);
            holder.Lote.setEnabled(false);
            holder.TempTrabajo.setEnabled(false);
            holder.TResidencia.setEnabled(false);
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
        // Inputlayout Desgomado
        private final TextInputLayout txtInputTonelaje;
        private final TextInputLayout txtInputAcidoFosforico;
        private final TextInputLayout txtInputTrabajo;
        private final TextInputLayout txtInputTResidencia;
        private final TextInputLayout txtInputReactor;
        private final TextInputLayout txtInputLote;
        //----------------------------------------------------
        Button Registrardatos;
        EditText Tonelaje, AcidoFosforico, TempTrabajo, TResidencia;
        AutoCompleteTextView Reactor, Lote;
        private AdacterDesgomado adapter;
        ProgressBar progressBar;
        private final Context context;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            context = itemView.getContext();
            //----------------------------------------------------
            txtInputReactor = itemView.findViewById(R.id.txtInputReactorDegummed);
            txtInputTonelaje = itemView.findViewById(R.id.txtInputTonDegummed);
            txtInputAcidoFosforico = itemView.findViewById(R.id.txtInputPhosphoricAcidDegummed);
            txtInputLote = itemView.findViewById(R.id.txtInputLotDegummed);
            txtInputTrabajo = itemView.findViewById(R.id.txtInputWorkTemperatureDegummed);
            txtInputTResidencia = itemView.findViewById(R.id.txtInputHomeDegummed);
            //----------------------------------------------------
            //AutoCompleteTextView
            Reactor = itemView.findViewById(R.id.edtReactorDegummed);
            Lote = itemView.findViewById(R.id.edtLotDegummed);
            //texview Desgomado
            Tonelaje = itemView.findViewById(R.id.edtTonDegummed);
            AcidoFosforico = itemView.findViewById(R.id.edtPhosphoricAcidDegummed);
            TempTrabajo = itemView.findViewById(R.id.edtWorkTemperatureDegummed);
            TResidencia = itemView.findViewById(R.id.edtHomeDegummed);
            //----------------------------------------------------
            progressBar = itemView.findViewById(R.id.progress_bar);
            Registrardatos = itemView.findViewById(R.id.btnRegistate);
            Registrardatos.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (clickBatch != null) {
                        int position = getAdapterPosition();

                        if (position != RecyclerView.NO_POSITION) {
                            clickBatch.DesgomadoMenuItem(position, ItemList.get(position), itemView);
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

        public ViewHolder linkAdapter(AdacterDesgomado adapter) {
            this.adapter = adapter;
            return this;
        }

    }
}
