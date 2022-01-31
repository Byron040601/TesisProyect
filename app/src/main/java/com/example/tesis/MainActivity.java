package com.example.tesis;

import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_ENABLE_BT = 0;
    public String[] datos;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    Button charge, failure, saved, stats;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Declaraciones de botones
        charge = findViewById(R.id.carga);
        stats = findViewById(R.id.stats);
        failure = findViewById(R.id.failure);
        saved = findViewById(R.id.saved);

        charge.setOnClickListener(new View.OnClickListener() {
            String[] typeVehicle;
            String vehiculo;

            @Override
            public void onClick(View v) {
                try {
                    cargaDatos();
                    typeVehicle = datos[2].split(":");
                    vehiculo = typeVehicle[1];

                    Map<String, Object> fallos = new HashMap<>();
                    fallos.put("P0626", "Alternator field terminal - circuit high");
                    fallos.put("P0327","Knock sensor (KS) 1 - bank 1 - low input");
                    fallos.put("P0339", "Crankshaft position (CKP) sensor - circuit intermittent");

//                    Map<String, Object> fallos2 = new HashMap<>();
//                    fallos2.put("P0960", "Pressure control solenoid 'A' control circuit open");
//                    fallos2.put("P0876","Transmission Fluid Pressure (TFP) Sensor / Switch 'D' Range / Performance");
//                    fallos2.put("P0797", "Transmission Fluid Pressure (TFP) Solenoid C - Stuck");

                    db.collection("vehiculo").document(vehiculo).set(fallos);
//                    db.collection("vehiculo").document("Grand Vitara").set(fallos2);

                } catch (IOException e) {
                    Toast.makeText(MainActivity.this, "No se pudo cargar los datos", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        });

        stats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent stats = new Intent(MainActivity.this, Estadisticas.class);
                startActivity(stats);
            }
        });

        failure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent failure = new Intent(MainActivity.this, Fallos1.class);
                startActivity(failure);
            }
        });

        saved.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent saved = new Intent(MainActivity.this, FallosGuardados.class);
                startActivity(saved);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode){
            case REQUEST_ENABLE_BT:
                if (resultCode == REQUEST_ENABLE_BT){
                    message("No se puedo activar la conexión Bluetooth");
                }
                else {
                    message("La conexion ya está activa.");
                }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void message (String msg){
        Context context = getApplicationContext();
        int duration = Toast.LENGTH_LONG;
        Toast toast = Toast.makeText(context, msg, duration);
        toast.show();
    }

    public void cargaDatos () throws IOException {
        List<String> listado = new ArrayList<String>();
        String linea;

        InputStream is = this.getResources().openRawResource(R.raw.vehiculo1);
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));

        if (is != null){
            while ((linea = reader.readLine()) != null){
                listado.add(linea.split("\n")[0]);

            }
        }

        is.close();

        datos = listado.toArray(new String[listado.size()]);
    }
}

