package com.example.superlista;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.superlista.data.SuperListaDbManager;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    Fragment fragment = null;


    /* TODO: Fede: Fragment de listas, supermercados, floating button de todas activities. Acerca de (Activity). Formularios para agregar  productos, listas y categorias.
       TODO: Mauro: Terminar activity productos(icono de microfono, search adapter en el fragment, checkbox), menu cuando se mantiene presionado un item. Activity categoria con boton de ver productos pasandole al fragment de productos el id de la categoria
       TODO: Agregar marca en los list view que tengan productos*/
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Instancia unica de la base de datos
        SuperListaDbManager.init(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);

        //TODO: cuando se pone el telefono en horizontal en un fragment que no sea el de listas vuelve automaticamente a las listas
        //colocamos el fragment de listas en el principio

        //fragment = new FragmentListas();
        //FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        //ft.replace(R.id.contenedor, fragment);
        //ft.commit();




    }




    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.activity_principal, menu);   //aca hiria la configuracionde lo settings
        return true;
    }


    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
      //  int id = item.getItemId();

        //noinspection SimplifiableIfStatement
      //  if (id == R.id.action_settings) {
            return true;
      //  }

      //  return super.onOptionsItemSelected(item);
    }


    //metodo que le paso un item del navigation
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();
      /*  FragmentManager fragmentManager = getSupportFragmentManager();

        if (id == R.id.item_menu_lista) {

            //Intent i = new Intent(MainActivity.this, PruebaBD.class);
            //startActivity(i);
            fragmentManager.beginTransaction().replace(R.id.contenedor, new FragmentListas()).commit();


            Toast.makeText(getApplicationContext(), "Bienvenido a las Listas", Toast.LENGTH_SHORT).show();

        } else if (id == R.id.item_menu_productos) {
            Toast.makeText(getApplicationContext(), "Bienvenido a los Productos", Toast.LENGTH_SHORT).show();

        } else if (id == R.id.item_menu_categoria) {
            Toast.makeText(getApplicationContext(), "Bienvenido a las Categorias", Toast.LENGTH_SHORT).show();

        } else if (id == R.id.item_menu_supers) {
            Toast.makeText(getApplicationContext(), "Bienvenido a los Supermercados", Toast.LENGTH_SHORT).show();

        } else if (id == R.id.item_menu_acercade) {
            Toast.makeText(getApplicationContext(), R.string.version_apk, Toast.LENGTH_SHORT).show();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);*/
        displaySelectedScreen(id);
        return true;
    }

    private void displaySelectedScreen(int id){


        switch (id){
            case R.id.item_menu_lista:
                fragment = new FragmentListas();
                Toast.makeText(getApplicationContext(), "Bienvenido a las Listas", Toast.LENGTH_SHORT).show();
                break;
            case R.id.item_menu_productos:
                fragment = new FragmentProductos();
                //fragment = new FragmentProductos2(); // Probando busqueda en el menu
                Toast.makeText(getApplicationContext(), "Bienvenido a los Productos", Toast.LENGTH_SHORT).show();
                break;
            case R.id.item_menu_categoria:

                Toast.makeText(getApplicationContext(), "Bienvenido a las Categorias", Toast.LENGTH_SHORT).show();
                break;
            case R.id.item_menu_supers:
                fragment = new FragmentSupers();
                Toast.makeText(getApplicationContext(), "Bienvenido a los Supermercados", Toast.LENGTH_SHORT).show();
                break;
            case R.id.item_menu_acercade:
                fragment = new FragmentAcercaDe();
                //Toast.makeText(getApplicationContext(), R.string.version_apk, Toast.LENGTH_SHORT).show();
                break;

        }

        if (fragment != null){
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.contenedor, fragment);
            ft.commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

    }



}
