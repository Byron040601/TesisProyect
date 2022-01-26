package com.example.tesis;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Fallos2 extends AppCompatActivity {

    private final static String URL = "https://codigosdtc.com/listado-de-codigos-dtc-obdii/";
    private final static String URL2 = "https://codigosdtc.com/listado-de-codigos-dtc-obdii/";
    private final static String URL3 = "https://codigosdtc.com/listado-de-codigos-dtc-obdii/";
    private final static String URL4 = "https://codigosdtc.com/listado-de-codigos-dtc-obdii/";
    Button back, vehiculo1;
    ListView listFallos1, listFallos2, listFallos3, listFallos4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fallos2);

        back = findViewById(R.id.back);
        listFallos1 = findViewById(R.id.listFallos1);
        vehiculo1 = findViewById(R.id.Vehiculo1);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent back = new Intent(Fallos2.this, MainActivity.class);
                startActivity(back);
            }
        });

        vehiculo1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    cargaDatos(v);
                } catch (IOException e) {
                    Toast.makeText(Fallos2.this, "No se pudo cargar los datos", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        });
    }

    public void cargaDatos (View v) throws IOException {
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

        String datos[] = listado.toArray(new String[listado.size()]);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, datos);
        listFallos1.setAdapter(adapter);

        listFallos1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                if (parent.getItemAtPosition(position).equals("P0626") ||
                        parent.getItemAtPosition(position).equals("P0327") ||
                        parent.getItemAtPosition(position).equals("P0339")){
                    intent.setData(Uri.parse(URL));
                    startActivity(intent);
                }
            }
        });
    }
}