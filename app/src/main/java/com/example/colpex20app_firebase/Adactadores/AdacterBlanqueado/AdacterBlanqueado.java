package com.example.colpex20app_firebase.Adactadores.AdacterBlanqueado;

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

public class AdacterBlanqueado extends RecyclerView.Adapter<AdacterBlanqueado.ViewHolder> {
    private final List<String> ItemList;
    private final onClickBatch clickBatch;
    ArrayList<String> allNamesReactor;
    //conexion Firebase
    DatabaseReference ref;

    public AdacterBlanqueado(List<String> itemList, onClickBatch clickBatch) {
        ItemList = itemList;
        this.clickBatch = clickBatch;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_blanqueado, parent, false);
        return new ViewHolder(view).linkAdapter(this);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //  --------------------------------------------
        holder.Reactor.setText(ItemList.get(position));
        holder.LoteArcilla.setText(ItemList.get(position));
        holder.LoteCarbActiva.setText(ItemList.get(position));
        holder.LoteSilice.setText(ItemList.get(position));
        //  --------------------------------------------
        holder.Carga.setText(ItemList.get(position));
        holder.Vacio.setText(ItemList.get(position));
        holder.TempTrabajo.setText(ItemList.get(position));
        holder.Arcilla.setText(ItemList.get(position));
        holder.CarbonActivado.setText(ItemList.get(position));
        holder.Silice.setText(ItemList.get(position));
        holder.FFAFinal.setText(ItemList.get(position));
        holder.ColorFinal.setText(ItemList.get(position));
        holder.AndisidinaFinal.setText(ItemList.get(position));
        //  --------------------------------------------
        allNamesReactor = Reactor();
        ArrayAdapter<String> autoCompleteLotNumber = new ArrayAdapter<>(holder.Reactor.getContext(), android.R.layout.simple_list_item_1, allNamesReactor);
        holder.Reactor.setAdapter(autoCompleteLotNumber);
        //----------------------------------------------------
        ref = FirebaseDatabase.getInstance().getReference();
        final ArrayAdapter<String> getNameArcilla = new ArrayAdapter<>(holder.LoteArcilla.getContext(), android.R.layout.simple_list_item_1);
        ref.child("LotesArcilla").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                getNameArcilla.clear();
                holder.progressBar.setVisibility(View.GONE);
                for (DataSnapshot areaSnapshot : dataSnapshot.getChildren()) {
                    String areaName = areaSnapshot.child("nroloteArcilla").getValue(String.class);
                    getNameArcilla.add(areaName);
                    holder.progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        AutoCompleteTextView ACTV = (AutoCompleteTextView) holder.LoteArcilla;
        ACTV.setAdapter(getNameArcilla);
        //----------------------------------------------------
        ref = FirebaseDatabase.getInstance().getReference();
        final ArrayAdapter<String> getNameeCabActivado = new ArrayAdapter<>(holder.LoteCarbActiva.getContext(), android.R.layout.simple_list_item_1);
        ref.child("LotesCarbonActivado").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                getNameeCabActivado.clear();
                holder.progressBar.setVisibility(View.GONE);
                for (DataSnapshot areaSnapshot : dataSnapshot.getChildren()) {
                    String areaName = areaSnapshot.child("nroloteCarbonActivado").getValue(String.class);
                    getNameeCabActivado.add(areaName);
                    holder.progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        AutoCompleteTextView ACTV1 = (AutoCompleteTextView) holder.LoteCarbActiva;
        ACTV1.setAdapter(getNameeCabActivado);
        //----------------------------------------------------
        ref = FirebaseDatabase.getInstance().getReference();
        final ArrayAdapter<String> getNameeSilice = new ArrayAdapter<>(holder.LoteSilice.getContext(), android.R.layout.simple_list_item_1);
        ref.child("LotesSilice").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                getNameeSilice.clear();
                holder.progressBar.setVisibility(View.GONE);
                for (DataSnapshot areaSnapshot : dataSnapshot.getChildren()) {
                    String areaName = areaSnapshot.child("nroloteSillice").getValue(String.class);
                    getNameeSilice.add(areaName);
                    holder.progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        AutoCompleteTextView ACTV2 = (AutoCompleteTextView) holder.LoteSilice;
        ACTV2.setAdapter(getNameeSilice);
        //------------------------------------------------
        String Batch1 = ((Activity) holder.context).getIntent().getStringExtra("Batch");
        Integer Batch = Integer.parseInt(Batch1);
        //------------------------------------------------
        String BatchD = ((Activity) holder.context).getIntent().getStringExtra("BatchBlanqueado");
        Integer cantBatch = Integer.parseInt(BatchD);
        //------------------------------------------------
        Button Registrar =  ((Activity) holder.context).findViewById(R.id.RegistarBlanqueado);
        if (cantBatch > Batch) {
            Registrar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new SweetAlertDialog(holder.context, SweetAlertDialog.ERROR_TYPE).setTitleText("Aviso").setContentText("Ya no hay batch para Agregar").show();
                }
            });
            holder.Registrardatos.setEnabled(false);

            holder.txtInputReactor.setEnabled(false);
            holder.txtInputLoad.setEnabled(false);
            holder.txtInputEmpty.setEnabled(false);
            holder.txtInputWorkTemperature.setEnabled(false);
            holder.txtInputClay.setEnabled(false);
            holder.txtInputLotClay.setEnabled(false);
            holder.txtInputActivatedCarbon.setEnabled(false);
            holder.txtInputLotActivatedCarbon.setEnabled(false);
            holder.txtInputSilica.setEnabled(false);
            holder.txtInputLotSilica.setEnabled(false);
            holder.txtInputFFAFinal.setEnabled(false);
            holder.txtInputColorFinal.setEnabled(false);
            holder.txtInputAnisidineFinal.setEnabled(false);

            holder.Reactor.setEnabled(false);
            holder.Carga.setEnabled(false);
            holder.Vacio.setEnabled(false);
            holder.TempTrabajo.setEnabled(false);
            holder.Arcilla.setEnabled(false);
            holder.LoteArcilla.setEnabled(false);
            holder.CarbonActivado.setEnabled(false);
            holder.LoteCarbActiva.setEnabled(false);
            holder.Silice.setEnabled(false);
            holder.LoteSilice.setEnabled(false);
            holder.FFAFinal.setEnabled(false);
            holder.ColorFinal.setEnabled(false);
            holder.AndisidinaFinal.setEnabled(false);
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
        // Inputlayout BLANQUEDO
        TextInputLayout txtInputReactor, txtInputLoad, txtInputEmpty,
                txtInputWorkTemperature, txtInputClay, txtInputLotClay,
                txtInputActivatedCarbon, txtInputLotActivatedCarbon,
                txtInputSilica, txtInputLotSilica, txtInputFFAFinal,
                txtInputColorFinal, txtInputAnisidineFinal;
        //----------------------------------------------------
        EditText Carga, Vacio, TempTrabajo, Arcilla,
                CarbonActivado, Silice, FFAFinal,
                ColorFinal, AndisidinaFinal;
        AutoCompleteTextView Reactor, LoteArcilla, LoteCarbActiva, LoteSilice;
        //----------------------------------------------------
        Button Registrardatos;
        private AdacterBlanqueado adapter;
        ProgressBar progressBar;
        private final Context context;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            context = itemView.getContext();
            //----------------------------------------------------
            txtInputReactor = itemView.findViewById(R.id.txtInputReactorBleached);
            txtInputLoad = itemView.findViewById(R.id.txtInputLoadBleached);
            txtInputEmpty = itemView.findViewById(R.id.txtInputEmpty);
            txtInputWorkTemperature = itemView.findViewById(R.id.txtInputWorkTemperatureBleached);
            txtInputClay = itemView.findViewById(R.id.txtInputClayBleached);
            txtInputLotClay = itemView.findViewById(R.id.txtInputLotClayBleached);
            txtInputActivatedCarbon = itemView.findViewById(R.id.txtInputActivatedCarbonBleached);
            txtInputLotActivatedCarbon = itemView.findViewById(R.id.txtInputLotActivatedCarbonBleached);
            txtInputSilica = itemView.findViewById(R.id.txtInputSilicaBleached);
            txtInputLotSilica = itemView.findViewById(R.id.txtInputLotSilicaBleached);
            txtInputFFAFinal = itemView.findViewById(R.id.txtInputFFAFinalBleached);
            txtInputColorFinal = itemView.findViewById(R.id.txtInputColorFinalBleached);
            txtInputAnisidineFinal = itemView.findViewById(R.id.txtInputAnisidineFinalBleached);
            //----------------------------------------------------
            //AutoCompleteTextView
            Reactor = itemView.findViewById(R.id.edtReactorBleached);
            // lote arcilla
            LoteArcilla = itemView.findViewById(R.id.edtLotClayBleached);
            //lote carbon Activado
            LoteCarbActiva = itemView.findViewById(R.id.edtLotActivatedCarbonBleached);
            //lote silice
            LoteSilice = itemView.findViewById(R.id.edtLotSilicaBleached);
            //texview Blanqueado
            Carga = itemView.findViewById(R.id.edtLoadBleached);
            Vacio = itemView.findViewById(R.id.edtEmptyBleached);
            TempTrabajo = itemView.findViewById(R.id.edtWorkTemperatureBleached);
            Arcilla = itemView.findViewById(R.id.edtClayBleached);
            CarbonActivado = itemView.findViewById(R.id.edtActivatedCarbonBleached);
            Silice = itemView.findViewById(R.id.edtSilicaBleached);
            FFAFinal = itemView.findViewById(R.id.edtFFAFinalBleached);
            ColorFinal = itemView.findViewById(R.id.edtColorFinalBleached);
            AndisidinaFinal = itemView.findViewById(R.id.edtAnisidineFinalBleached);
            //----------------------------------------------------
            progressBar = itemView.findViewById(R.id.progress_bar);
            Registrardatos = itemView.findViewById(R.id.btnRegistate);
            Registrardatos.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (clickBatch != null) {
                        int position = getAdapterPosition();

                        if (position != RecyclerView.NO_POSITION) {
                            clickBatch.BlanqueadoMenuItem(position, ItemList.get(position), itemView);
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

        public ViewHolder linkAdapter(AdacterBlanqueado adapter) {
            this.adapter = adapter;
            return this;
        }
    }

}
