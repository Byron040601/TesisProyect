package com.example.tesis;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
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

public class FallosGuardados extends AppCompatActivity {

    TextView fecha, lista_fallos;
    Button back;
    String TAG = "arreglo";

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fallos_guardados);

        lista_fallos = findViewById(R.id.lista_fallos);
        fecha = findViewById(R.id.fecha);
        back = findViewById(R.id.back);

        getData1();

        back.setOnClickListener(v -> {
            Intent back = new Intent(FallosGuardados.this, MainActivity.class);
            startActivity(back);
        });
    }

    public void getData1() {
        List<String> listado = new ArrayList<String>();

        db.collection("vehiculo").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                Map<String, Object> fallos = null;
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        fecha.setText(document.getId());
                        fallos = document.getData();
                        for(Map.Entry<String, Object> map : fallos.entrySet()){
                            listado.add((map.getKey() + " --> " + map.getValue()));
                        }

                    }
                }

                for (int i = 0; i < listado.size(); i++) {
                    if (i == listado.size() - 1) {
                        lista_fallos.append(listado.get(i).toString());
                    } else {
                        lista_fallos.append(listado.get(i).toString() + "\n\n");
                    }
                }
            }
        });
    }
}