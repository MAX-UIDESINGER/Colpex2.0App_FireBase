package com.example.colpex20app_firebase.OperadorResponsable.FragmenContenido_op;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;


import com.example.colpex20app_firebase.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


public class FragmentReporteDocument extends Fragment {
    //CArview
    CardView Lote;
    CardView Desgmomado;
    CardView Neutralizado;
    CardView Blanqueado;

    //conexion Firebase
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    //FireBase
    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    Bitmap btmp, scaledbmp;
    int PageWidth = 1200;
    Date dateObj;
    DateFormat dateFormat;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().setTitle(" Reportes");
        return inflater.inflate(R.layout.fragment_reporte_document, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Reporte(view);
    }

    private void Reporte(View view) {
        Lote = view.findViewById(R.id.ReporLote);
        Desgmomado = view.findViewById(R.id.ReporDesgomado);
        Neutralizado = view.findViewById(R.id.ReporNeutralizado);
        Blanqueado = view.findViewById(R.id.ReporBlanqueado);

        btmp = BitmapFactory.decodeResource(getResources(), R.drawable.colpexvertical);
        scaledbmp = Bitmap.createScaledBitmap(btmp, 120, 518, false);

        ActivityCompat.requestPermissions(this.getActivity(), new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED);

        Lote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dateObj = new Date();

                CreatePDF();
                Toast.makeText(getContext(), "Archivo Descargado", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void CreatePDF() {

        PdfDocument myPdfDocument = new PdfDocument();
        Paint myPaint = new Paint();
        Paint titlePaint = new Paint();

        PdfDocument.PageInfo myPageInfo1 = new PdfDocument.PageInfo.Builder(1200, 2010, 1).create();
        PdfDocument.Page myPage1 = myPdfDocument.startPage(myPageInfo1);
        Canvas canvas = myPage1.getCanvas();

        canvas.drawBitmap(scaledbmp, 0, 0, myPaint);
        //Encabazado
        titlePaint.setTextAlign(Paint.Align.CENTER);
        titlePaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        titlePaint.setTextSize(70);
        canvas.drawText("Dimond pizze",PageWidth/2,270,titlePaint);
        // DESCRIBCIOM
        myPaint.setColor(Color.rgb(0,113,188));
        myPaint.setTextSize(30f);
        myPaint.setTextAlign(Paint.Align.RIGHT);
        canvas.drawText("call : 022-41414242",1160,40,myPaint);
        canvas.drawText("022-86684866",1160,80,myPaint);
        //titulo
        titlePaint.setTextAlign(Paint.Align.CENTER);
        titlePaint.setTypeface(Typeface.create(Typeface.DEFAULT,Typeface.ITALIC));
        titlePaint.setTextSize(70);
        canvas.drawText("Invoice",PageWidth/2,500,titlePaint);
        //Contendio
        myPaint.setTextAlign(Paint.Align.LEFT);
        myPaint.setTextSize(35f);
        myPaint.setColor(Color.BLACK);
        canvas.drawText("Customer name :" , 20,590,myPaint);
        canvas.drawText("Contact No.:" , 20,640,myPaint);

        myPaint.setTextAlign(Paint.Align.RIGHT);
        canvas.drawText("Invoice No.: " +"sdsadsa",PageWidth-20,590,myPaint);

        dateFormat = new SimpleDateFormat("dd/MM/yy");
        canvas.drawText("Date:"+dateFormat.format(dateObj),PageWidth-20,640,myPaint);

        dateFormat = new SimpleDateFormat("HH:mm:ss");
        canvas.drawText("Time:"+dateFormat.format(dateObj),PageWidth-20,690,myPaint);

        // Grip tabla
        myPaint.setStyle(Paint.Style.STROKE);
        myPaint.setStrokeWidth(2);
        canvas.drawRect(20,780,PageWidth-20,860,myPaint);

        myPaint.setTextAlign(Paint.Align.LEFT);
        myPaint.setStyle(Paint.Style.FILL);
        canvas.drawText("si. NO.",40,830,myPaint);
        canvas.drawText("Item Description",200,830,myPaint);
        canvas.drawText("Price",700,830,myPaint);
        canvas.drawText("Qty.",900,830,myPaint);
        canvas.drawText("Total.",150,830,myPaint);

        canvas.drawLine(180,790,180,840,myPaint);
        canvas.drawLine(680,790,680,840,myPaint);
        canvas.drawLine(880,790,680,840,myPaint);
        canvas.drawLine(1030,790,1030,840,myPaint);



        myPdfDocument.finishPage(myPage1);

        File file = new File(Environment.getExternalStorageDirectory(), "/Lote.pdf");

        try {
            myPdfDocument.writeTo(new FileOutputStream(file));
        } catch (IOException e) {
            e.printStackTrace();
        }
        myPdfDocument.close();
    }
}