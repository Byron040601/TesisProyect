package com.example.tesis;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Estadisticas extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    Button back;
    TextView promedio, fallos_mayoritarios;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.estadisticas);

        back = findViewById(R.id.back);
        promedio = findViewById(R.id.promedio);
        fallos_mayoritarios = findViewById(R.id.fallos_mayoritarios);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent back = new Intent(Estadisticas.this, MainActivity.class);
                startActivity(back);
            }
        });

        getData1();
    }

    public void getData1() {
        List<String> listado = new ArrayList<String>();

        db.collection("vehiculo").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            int EP = 0;
            int EB = 0;
            int EU = 0;
            int EC = 0;
            int total = 0;
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                Map<String, Object> fallos = null;
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        fallos = document.getData();
                        for(Map.Entry<String, Object> map : fallos.entrySet()){
                            listado.add(map.getKey());
                        }

                    }
                }

                for (int i = 0; i < listado.size(); i++) {
                    if(listado.get(i).startsWith("P")) {
                        EP++;
                    }
                    if(listado.get(i).startsWith("B")) {
                        EB++;
                    }
                    if(listado.get(i).startsWith("U")) {
                        EU++;
                    }
                    if(listado.get(i).startsWith("C")) {
                        EC++;
                    }
                }
                total = EP + EB + EU + EC;

                if (EP > EB && EP > EU && EP > EC ){
                    promedio.append("Fallos a nivel de motor." + "\n\n" + "Fallos totales obtenidos: " + total);
                }

                for (int i = 0; i < listado.size(); i++) {
                    if (i == listado.size() - 1) {
                        fallos_mayoritarios.append(listado.get(i).toString());
                    } else {
                        fallos_mayoritarios.append(listado.get(i).toString() + "\n\n");
                    }
                }
            }
        });
    }
}