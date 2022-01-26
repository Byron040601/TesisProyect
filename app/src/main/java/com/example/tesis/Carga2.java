package com.example.tesis;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class Carga2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.carga2);

        final int duracion = 4000;

        //Código de ejecucion terminado la pantalla de carga
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(Carga2.this, Fallos2.class);
                startActivity(intent);
                finish();
            }
        }, duracion);
    }
}