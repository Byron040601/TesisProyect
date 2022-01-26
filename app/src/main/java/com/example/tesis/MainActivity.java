package com.example.tesis;

import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

    Button charge, failure, saved, profiles, stats;
    BluetoothAdapter adapter;
    TextView data;
    ListView listFallos1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Declaraciones de botones
        charge = findViewById(R.id.carga);
        stats = findViewById(R.id.stats);
        failure = findViewById(R.id.failure);
        saved = findViewById(R.id.saved);
        profiles = findViewById(R.id.profiles);
        data = findViewById(R.id.datos);

//        adapter = BluetoothAdapter.getDefaultAdapter();
//
//        if (adapter == null) {
//            // Device doesn't support Bluetooth
//        }

        charge.setOnClickListener(new View.OnClickListener() {
            String[] typeVehicle;
            ArrayList failures;
            String vehiculo;
            JSONObject jsonObject = new JSONObject();
            JSONArray jsonArray = new JSONArray();

            @Override
            public void onClick(View v) {
//                if(!adapter.isEnabled()){
//                    message("Espere un momento mientras activamos la conexión Bluetooth.");
//                    Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
//                    startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
//                }
//                else{
//                    message("La conexión Bluetooth ya está activa.");
//                }
//                if (adapter.isEnabled()){
//                    deviceConected.setText("Dispositivos vinculados");
//                    Set<BluetoothDevice> devices = adapter.getBondedDevices();
//                    for (BluetoothDevice device: devices){
//                        deviceConected.append("\n" + device.getName() + "\n Dirección MAC " + device.getAddress());
//                    }
//                }
                try {
                    cargaDatos();
                    typeVehicle = datos[2].split(":");
                    vehiculo = typeVehicle[1];
                    data.append(vehiculo);

                    Map<String, Object> fallos = new HashMap<>();
                    fallos.put("Alternator field terminal - circuit high", "P0626");
                    fallos.put("Knock sensor (KS) 1, bank 1 - low input","P0327");
                    fallos.put("Crankshaft position (CKP) sensor - circuit intermittent", "P0339");

//
//                    jsonArray.put(failures);
//
//                    jsonObject.put("vehiculo", vehiculo);
//                    jsonObject.put("fallos", jsonArray);

                    db.collection("vehiculo").document(vehiculo).set(fallos);

//                    data.append(failures.toString());

                } catch (IOException e) {
                    Toast.makeText(MainActivity.this, "No se pudo cargar los datos", Toast.LENGTH_SHORT).show();
//                    e.printStackTrace();
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

        profiles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent profiles = new Intent(MainActivity.this, Perfiles.class);
                startActivity(profiles);
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

