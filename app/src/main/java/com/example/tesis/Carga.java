package com.example.tesis;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class Carga extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.carga);

        final int duracion = 3000;

        //Código de ejecucion terminado la pantalla de carga
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(Carga.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }, duracion);
    }
}