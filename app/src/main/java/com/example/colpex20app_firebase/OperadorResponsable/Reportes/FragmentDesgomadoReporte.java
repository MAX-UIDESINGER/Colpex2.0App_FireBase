package com.example.colpex20app_firebase.OperadorResponsable.Reportes;

import static android.widget.Toast.LENGTH_SHORT;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.colpex20app_firebase.Adactadores.AdacterDesgomado.SubItemAdacterDesgomado;
import com.example.colpex20app_firebase.BuildConfig;
import com.example.colpex20app_firebase.Model.RvItemClick;
import com.example.colpex20app_firebase.PrincipalClasses.ClassBlanqueado.ClaseBlanqueado;
import com.example.colpex20app_firebase.PrincipalClasses.ClassDesgomado.ClaseDesgomado;
import com.example.colpex20app_firebase.PrincipalClasses.ClassNeutralizado.ClaseNeutralizado;
import com.example.colpex20app_firebase.R;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;


public class FragmentDesgomadoReporte extends Fragment implements RvItemClick {

    SubItemAdacterDesgomado adapter1, adapter2, adapter3;
    RecyclerView recyclerView;
    TextView textView3, textView2, textView1;
    //Contendio
    ArrayList<ClaseDesgomado> subItemListDesgomado1, subItemListDesgomado2, subItemListDesgomado3;

    ClaseDesgomado reactor;
    //conexion Firebase
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    int numPaginas = 0;

    String Lote;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Lote = getActivity().getIntent().getStringExtra("NumeroLote");
        getActivity().setTitle("Datos de Lote " + Lote);
        return inflater.inflate(R.layout.fragment_desgomado_reporte, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //contenido
        ContenidoImprimir(view);
        //Tool Bar Metodo
        ToolbarMetodos(view);
        //metodo
        InicioRecyclerviewReactor1(view);
        InicioRecyclerviewReactor2(view);
        InicioRecyclerviewReactor3(view);
        //FireBase
        IniciarFirebase();
        ListarDatosDesgomadoReactor1();
        ListarDatosDesgomadoReactor2();
        ListarDatosDesgomadoReactor3();
    }

    private void ContenidoImprimir(View view) {
        textView1 = view.findViewById(R.id.txtreactor1);
        textView2 = view.findViewById(R.id.txtreactor2);
        textView3 = view.findViewById(R.id.txtreactor3);

        Button buttonPDF = view.findViewById(R.id.btnArchivoPDF);
        buttonPDF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    // La aplicación tiene permisos de escritura y lectura en el almacenamiento externo
                    Toast.makeText(getContext(), "Archivo Descargado", Toast.LENGTH_SHORT).show();
                    CreatePDF();

                } else {
                    // No hay permisos, solicítalos
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                }

            }

        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // El usuario ha otorgado los permisos, se puede crear el archivo PDF
                Toast.makeText(getContext(), "Archivo Descargado", Toast.LENGTH_SHORT).show();
                CreatePDF();
            } else {
                // El usuario ha denegado los permisos, muestra un mensaje o realiza otra acción según sea necesario
                Toast.makeText(getContext(), "Los permisos de almacenamiento externo son necesarios para descargar el archivo PDF", Toast.LENGTH_LONG).show();
            }
        }
    }

    @SuppressLint("SimpleDateFormat")
    private void CreatePDF() {

    }

    @SuppressLint("MissingPermission")
    private void notificacion(File file) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri uri = FileProvider.getUriForFile(getActivity(), BuildConfig.APPLICATION_ID + ".fileprovider", file);
        intent.setDataAndType(uri, "application/pdf");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("CHANNEL_ID", "Canal de notificación", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager notificationManager = getContext().getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
        PendingIntent pendingIntent = PendingIntent.getActivity(getContext(), 0, intent, PendingIntent.FLAG_MUTABLE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getActivity(), "CHANNEL_ID")
                .setSmallIcon(R.drawable.ic_descarga)
                .setContentTitle("Descarga completa")
                .setContentText("Se ha descargado el archivo en la carpeta Descargas")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getContext());
        notificationManager.notify(0, builder.build());
    }

    private void ListarDatosDesgomadoReactor3() {
        String Lote = getActivity().getIntent().getStringExtra("NumeroLote");
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Desgomado").child(Lote);
        Query query = ref.orderByChild("reactor").equalTo("3");
        query.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                subItemListDesgomado3.clear();
                textView3.setText("Reactor 3 (No hay Datos)");
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    ClaseDesgomado reactor = dataSnapshot.getValue(ClaseDesgomado.class);
                    subItemListDesgomado3.add(reactor);
                    textView3.setText("Reactor 3");
                }
                adapter3.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), error.getMessage(), LENGTH_SHORT).show();
            }
        });
    }

    private void ListarDatosDesgomadoReactor2() {
        String Lote = getActivity().getIntent().getStringExtra("NumeroLote");
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Desgomado").child(Lote);
        Query query = ref.orderByChild("reactor").equalTo("2");
        query.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                subItemListDesgomado2.clear();
                textView2.setText("Reactor 2 (No hay Datos)");
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    ClaseDesgomado reactor = dataSnapshot.getValue(ClaseDesgomado.class);
                    subItemListDesgomado2.add(reactor);
                    textView2.setText("Reactor 2");
                }
                adapter2.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), error.getMessage(), LENGTH_SHORT).show();
            }
        });
    }

    private void ListarDatosDesgomadoReactor1() {
        String Lote = getActivity().getIntent().getStringExtra("NumeroLote");
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Desgomado").child(Lote);
        Query query = ref.orderByChild("reactor").equalTo("1");
        query.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                subItemListDesgomado1.clear();
                textView1.setText("Reactor 1 (No hay Datos)");
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    reactor = dataSnapshot.getValue(ClaseDesgomado.class);
                    subItemListDesgomado1.add(reactor);
                    textView1.setText("Reactor 1");
                }
                adapter1.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), error.getMessage(), LENGTH_SHORT).show();
            }
        });
    }

    private void IniciarFirebase() {
        FirebaseApp.initializeApp(this.getContext());
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
    }

    private void ToolbarMetodos(View view) {
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void InicioRecyclerviewReactor1(View view) {
        recyclerView = view.findViewById(R.id.recyclerViewReactor1);
        subItemListDesgomado1 = new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        recyclerView.setHasFixedSize(false);
        adapter1 = new SubItemAdacterDesgomado(subItemListDesgomado1, this);
        recyclerView.setAdapter(adapter1);
    }

    private void InicioRecyclerviewReactor2(View view) {
        recyclerView = view.findViewById(R.id.recyclerViewReactor2);
        subItemListDesgomado2 = new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        recyclerView.setHasFixedSize(false);
        adapter2 = new SubItemAdacterDesgomado(subItemListDesgomado2, this);
        recyclerView.setAdapter(adapter2);
    }

    private void InicioRecyclerviewReactor3(View view) {
        recyclerView = view.findViewById(R.id.recyclerViewReactor3);
        subItemListDesgomado3 = new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        recyclerView.setHasFixedSize(false);
        adapter3 = new SubItemAdacterDesgomado(subItemListDesgomado3, this);
        recyclerView.setAdapter(adapter3);
    }

    @Override
    public void onDataChanged() {

    }

    @Override
    public void DesgomadoMenuItem(int position, MenuItem menuItem, ClaseDesgomado item) {

    }

    @Override
    public void NeutralizadoMenuItem(int position, MenuItem menuItem, ClaseNeutralizado item) {

    }

    @Override
    public void BlanqueadoMenuItem(int position, MenuItem menuItem, ClaseBlanqueado item) {

    }
}