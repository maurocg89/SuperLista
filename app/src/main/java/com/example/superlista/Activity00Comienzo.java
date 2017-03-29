package com.example.superlista;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;

import java.util.Timer;
import java.util.TimerTask;

public class Activity00Comienzo extends AppCompatActivity  {


    private static final long delayBienvenida = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); //establesco la orientacion de la pantalla

        requestWindowFeature(Window.FEATURE_NO_TITLE); //con ésto oculto el titulo

        setContentView(R.layout.activity00_comienzo);

        TimerTask tarea = new TimerTask() {
            @Override
            public void run() {

                //inicia la actividad
                Intent mainIntet = new Intent().setClass(Activity00Comienzo.this, MainActivity.class);
                startActivity(mainIntet);

                finish();
            }
        };

        Timer tiempo = new Timer(); // definimos la duracion
        tiempo.schedule(tarea, delayBienvenida);

    }
}
