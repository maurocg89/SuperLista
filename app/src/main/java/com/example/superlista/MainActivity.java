package com.example.superlista;

import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {


    private Toolbar toolbar;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;


    Button boton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Fragmento fragmento = new Fragmento();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame, fragmento);
        transaction.commit();



        /*BOTON QUE CREO MAURO
        boton = (Button) findViewById(R.id.buttonPruebaDb);
        boton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, PruebaBD.class);
                startActivity(i);
            }
        });*/
    }
}
