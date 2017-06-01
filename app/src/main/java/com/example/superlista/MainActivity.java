package com.example.superlista;


import android.net.Uri;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;

import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;

import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import android.widget.ImageView;
import android.widget.Toast;

import com.example.superlista.data.SuperListaDbManager;



public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private long mLastPress = 0;    // Cuándo se pulsó atrás por última vez
    private long mTimeLimit = 2000; // Límite de tiempo entre pulsaciones, en ms

    Fragment fragment = null;

    /*
       TODO: Ver el tema de adaptar la aplicacoin para distintas pantallas

*/

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

        //<editor-fold desc="LOGS DE INFORMACION PARA EXTRAER URIS DE LOS PRODUCTOS">
        /*
        ésto lo probe para extraer la ubicacion del la imagen para guardarla en la BD



        Log.i("BEBIDAS ","------------------------------------------------------------");
        Log.i("01bebida ",Uri.parse("android.resource://" + getPackageName() + "/" + R.drawable.producto_bebida_amargotrestorresblanco15).toString());
        Log.i("02bebida ",Uri.parse("android.resource://" + getPackageName() + "/" + R.drawable.producto_bebida_amargotrestorreslimon15).toString());
        Log.i("03bebida ",Uri.parse("android.resource://" + getPackageName() + "/" + R.drawable.producto_bebida_amargotrestorresnegro15).toString());
        Log.i("04bebida ",Uri.parse("android.resource://" + getPackageName() + "/" + R.drawable.producto_bebida_cocacola225).toString());
        Log.i("05bebida ",Uri.parse("android.resource://" + getPackageName() + "/" + R.drawable.producto_bebida_cocacola3).toString());
        Log.i("06bebida ",Uri.parse("android.resource://" + getPackageName() + "/" + R.drawable.producto_bebida_cocacolaligth3).toString());
        Log.i("07bebida ",Uri.parse("android.resource://" + getPackageName() + "/" + R.drawable.producto_bebida_cocacolazero225).toString());
        Log.i("08bebida ",Uri.parse("android.resource://" + getPackageName() + "/" + R.drawable.producto_bebida_cocacolazero3).toString());
        Log.i("09bebida ",Uri.parse("android.resource://" + getPackageName() + "/" + R.drawable.producto_bebida_jugosobrearcormulti).toString());
        Log.i("10bebida ",Uri.parse("android.resource://" + getPackageName() + "/" + R.drawable.producto_bebida_jugosobrearcornardur).toString());
        Log.i("11bebida ",Uri.parse("android.resource://" + getPackageName() + "/" + R.drawable.producto_bebida_jugosobrebcanana).toString());
        Log.i("12bebida ",Uri.parse("android.resource://" + getPackageName() + "/" + R.drawable.producto_bebida_jugosobrebcnardul).toString());
        Log.i("13bebida ",Uri.parse("android.resource://" + getPackageName() + "/" + R.drawable.producto_bebida_jugosobreclightanana).toString());
        Log.i("14bebida ",Uri.parse("android.resource://" + getPackageName() + "/" + R.drawable.producto_bebida_jugosobreclightnardur).toString());
        Log.i("15bebida ",Uri.parse("android.resource://" + getPackageName() + "/" + R.drawable.producto_bebida_jugosobreclightpera).toString());
        Log.i("16bebida ",Uri.parse("android.resource://" + getPackageName() + "/" + R.drawable.producto_bebida_jugosobretangdur).toString());
        Log.i("17bebida ",Uri.parse("android.resource://" + getPackageName() + "/" + R.drawable.producto_bebida_jugosobretangnar).toString());
        Log.i("18bebida ",Uri.parse("android.resource://" + getPackageName() + "/" + R.drawable.producto_bebida_levitefritillalimon2).toString());
        Log.i("19bebida ",Uri.parse("android.resource://" + getPackageName() + "/" + R.drawable.producto_bebida_levitepomelo2).toString());
        Log.i("20bebida ",Uri.parse("android.resource://" + getPackageName() + "/" + R.drawable.producto_bebida_pepsi225).toString());
        Log.i("21bebida ",Uri.parse("android.resource://" + getPackageName() + "/" + R.drawable.producto_bebida_pepsi3).toString());
        Log.i("22bebida ",Uri.parse("android.resource://" + getPackageName() + "/" + R.drawable.producto_bebida_prittyzero15).toString());
        Log.i("23bebida ",Uri.parse("android.resource://" + getPackageName() + "/" + R.drawable.producto_bebida_redbull025).toString());
        Log.i("24bebida ",Uri.parse("android.resource://" + getPackageName() + "/" + R.drawable.producto_bebida_sprite225).toString());
        Log.i("25bebida ",Uri.parse("android.resource://" + getPackageName() + "/" + R.drawable.producto_bebida_sprite3).toString());
        Log.i("26bebida ",Uri.parse("android.resource://" + getPackageName() + "/" + R.drawable.producto_bebida_spritezero225).toString());

        Log.i("CARNES ","------------------------------------------------------------");
        Log.i("01carne ",Uri.parse("android.resource://" + getPackageName() + "/" + R.drawable.producto_carne_cerdo).toString());
        Log.i("02carne ",Uri.parse("android.resource://" + getPackageName() + "/" + R.drawable.producto_carne_gallina).toString());
        Log.i("03carne ",Uri.parse("android.resource://" + getPackageName() + "/" + R.drawable.producto_carne_vaca).toString());

        Log.i("FRUTAS ","------------------------------------------------------------");
        Log.i("01fruta ",Uri.parse("android.resource://" + getPackageName() + "/" + R.drawable.producto_fruta_anana).toString());
        Log.i("02fruta ",Uri.parse("android.resource://" + getPackageName() + "/" + R.drawable.producto_fruta_banana).toString());
        Log.i("03fruta ",Uri.parse("android.resource://" + getPackageName() + "/" + R.drawable.producto_fruta_cereza).toString());
        Log.i("04fruta ",Uri.parse("android.resource://" + getPackageName() + "/" + R.drawable.producto_fruta_ciruela).toString());
        Log.i("05fruta ",Uri.parse("android.resource://" + getPackageName() + "/" + R.drawable.producto_fruta_durazno).toString());
        Log.i("06fruta ",Uri.parse("android.resource://" + getPackageName() + "/" + R.drawable.producto_fruta_frambuesa).toString());
        Log.i("07fruta ",Uri.parse("android.resource://" + getPackageName() + "/" + R.drawable.producto_fruta_frutilla).toString());
        Log.i("08fruta ",Uri.parse("android.resource://" + getPackageName() + "/" + R.drawable.producto_fruta_granada).toString());
        Log.i("09fruta ",Uri.parse("android.resource://" + getPackageName() + "/" + R.drawable.producto_fruta_kiwi).toString());
        Log.i("10fruta ",Uri.parse("android.resource://" + getPackageName() + "/" + R.drawable.producto_fruta_limon).toString());
        Log.i("11fruta ",Uri.parse("android.resource://" + getPackageName() + "/" + R.drawable.producto_fruta_mandarina).toString());
        Log.i("12fruta ",Uri.parse("android.resource://" + getPackageName() + "/" + R.drawable.producto_fruta_manzanaroja).toString());
        Log.i("13fruta ",Uri.parse("android.resource://" + getPackageName() + "/" + R.drawable.producto_fruta_melon).toString());
        Log.i("14fruta ",Uri.parse("android.resource://" + getPackageName() + "/" + R.drawable.producto_fruta_naranja).toString());
        Log.i("15fruta ",Uri.parse("android.resource://" + getPackageName() + "/" + R.drawable.producto_fruta_pera).toString());
        Log.i("16fruta ",Uri.parse("android.resource://" + getPackageName() + "/" + R.drawable.producto_fruta_sandia).toString());
        Log.i("17fruta ",Uri.parse("android.resource://" + getPackageName() + "/" + R.drawable.producto_fruta_uvamorada).toString());
        Log.i("18fruta ",Uri.parse("android.resource://" + getPackageName() + "/" + R.drawable.producto_fruta_uvaverde).toString());

        Log.i("LACTEOS ","------------------------------------------------------------");
        Log.i("01lacteo ",Uri.parse("android.resource://" + getPackageName() + "/" + R.drawable.producto_lacteos_laserenisimacasancremquesocrema).toString());
        Log.i("02lacteo ",Uri.parse("android.resource://" + getPackageName() + "/" + R.drawable.producto_lacteos_laserenisimaleche1).toString());
        Log.i("03lacteo ",Uri.parse("android.resource://" + getPackageName() + "/" + R.drawable.producto_lacteos_laserenisimaquesountable).toString());
        Log.i("04lacteo ",Uri.parse("android.resource://" + getPackageName() + "/" + R.drawable.producto_lacteos_laserenisimaseremixfrutilla).toString());
        Log.i("05lacteo ",Uri.parse("android.resource://" + getPackageName() + "/" + R.drawable.producto_lacteos_manterinamanteca02).toString());
        Log.i("06lacteo ",Uri.parse("android.resource://" + getPackageName() + "/" + R.drawable.producto_lacteos_mantymanteca02).toString());
        Log.i("07lacteo ",Uri.parse("android.resource://" + getPackageName() + "/" + R.drawable.producto_lacteos_milkautcremaleche05).toString());
        Log.i("08lacteo ",Uri.parse("android.resource://" + getPackageName() + "/" + R.drawable.producto_lacteos_milkautyogurtbebfrutilla1).toString());
        Log.i("09lacteo ",Uri.parse("android.resource://" + getPackageName() + "/" + R.drawable.producto_lacteos_sancorcremaleche05).toString());
        Log.i("10lacteo ",Uri.parse("android.resource://" + getPackageName() + "/" + R.drawable.producto_lacteos_sancorleche1).toString());
        Log.i("11lacteo ",Uri.parse("android.resource://" + getPackageName() + "/" + R.drawable.producto_lacteos_sancormanteca02).toString());
        Log.i("12lacteo ",Uri.parse("android.resource://" + getPackageName() + "/" + R.drawable.producto_lacteos_sancormendicrimquesocrema).toString());
        Log.i("13lacteo ",Uri.parse("android.resource://" + getPackageName() + "/" + R.drawable.producto_lacteos_sancoryogsvainilla1).toString());
        Log.i("14lacteo ",Uri.parse("android.resource://" + getPackageName() + "/" + R.drawable.producto_lacteos_sanignacioquesocrema).toString());
        Log.i("15lacteo ",Uri.parse("android.resource://" + getPackageName() + "/" + R.drawable.producto_lacteos_tholemquesocrema).toString());
        Log.i("16lacteo ",Uri.parse("android.resource://" + getPackageName() + "/" + R.drawable.producto_lacteos_tregarcremaleche05).toString());
        Log.i("17lacteo ",Uri.parse("android.resource://" + getPackageName() + "/" + R.drawable.producto_lacteos_tregarricotta05).toString());
        Log.i("17lacteo ",Uri.parse("android.resource://" + getPackageName() + "/" + R.drawable.producto_lacteos_tregaryogurtfrutilla0125).toString());

        Log.i("VERDURAS ","------------------------------------------------------------");
        Log.i("01verdura ",Uri.parse("android.resource://" + getPackageName() + "/" + R.drawable.producto_verdura_ajo).toString());
        Log.i("02verdura ",Uri.parse("android.resource://" + getPackageName() + "/" + R.drawable.producto_verdura_berenjena).toString());
        Log.i("03verdura ",Uri.parse("android.resource://" + getPackageName() + "/" + R.drawable.producto_verdura_cabollacomun).toString());
        Log.i("04verdura ",Uri.parse("android.resource://" + getPackageName() + "/" + R.drawable.producto_verdura_calabaza).toString());
        Log.i("05verdura ",Uri.parse("android.resource://" + getPackageName() + "/" + R.drawable.producto_verdura_cebollaroja).toString());
        Log.i("06verdura ",Uri.parse("android.resource://" + getPackageName() + "/" + R.drawable.producto_verdura_choclo).toString());
        Log.i("07verdura ",Uri.parse("android.resource://" + getPackageName() + "/" + R.drawable.producto_verdura_lechugarepollada).toString());
        Log.i("08verdura ",Uri.parse("android.resource://" + getPackageName() + "/" + R.drawable.producto_verdura_papablanca).toString());
        Log.i("09verdura ",Uri.parse("android.resource://" + getPackageName() + "/" + R.drawable.producto_verdura_pepino).toString());
        Log.i("10verdura ",Uri.parse("android.resource://" + getPackageName() + "/" + R.drawable.producto_verdura_pimientoamarillo).toString());
        Log.i("11verdura ",Uri.parse("android.resource://" + getPackageName() + "/" + R.drawable.producto_verdura_pimientorojo).toString());
        Log.i("12verdura ",Uri.parse("android.resource://" + getPackageName() + "/" + R.drawable.producto_verdura_pimientoverde).toString());
        Log.i("13verdura ",Uri.parse("android.resource://" + getPackageName() + "/" + R.drawable.producto_verdura_tomateperita).toString());
        Log.i("14verdura ",Uri.parse("android.resource://" + getPackageName() + "/" + R.drawable.producto_verdura_tomateredondo).toString());
        Log.i("15verdura ",Uri.parse("android.resource://" + getPackageName() + "/" + R.drawable.producto_verdura_zanahoria).toString());
        Log.i("16verdura ",Uri.parse("android.resource://" + getPackageName() + "/" + R.drawable.producto_verdura_zapallitoredondo).toString());

        */
        //</editor-fold>



    }

    //TODO: Mandar a fragment anterior, implementar pilas de fragment (backstack manipulation)

    public void onBackPressed() {


        Toast onBackPressedToast = Toast.makeText(this,R.string.presionar_para_salir, Toast.LENGTH_SHORT);
        long currentTime = System.currentTimeMillis();
        super.onBackPressed();
      /*  if (currentTime - mLastPress > mTimeLimit){
            // mandar a fragment listas
           /* FragmentTransaction ft =  getSupportFragmentManager().beginTransaction();
            fragment = new FragmentListas();
            ft.replace(R.id.contenedor, fragment);
            ft.commit();
            onBackPressedToast.show();
            mLastPress = currentTime;
        } else {
            onBackPressedToast.cancel();
            super.onBackPressed();
        }
*/

        /*DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            FragmentTransaction ft =  getSupportFragmentManager().beginTransaction();
            fragment = new FragmentListas();
            ft.replace(R.id.contenedor, fragment);
            ft.commit();
           //super.onBackPressed();
        }*/
    }


    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.activity_principal, menu);   //aca hiria la configuracionde lo settings
        return super.onCreateOptionsMenu(menu);
        //return true;
    }


    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
      //  int id = item.getItemId();
        //noinspection SimplifiableIfStatement
      //  if (id == R.id.action_settings) {
      //      return true;
      //  }

        return super.onOptionsItemSelected(item);
    }


    //metodo que le paso un item del navigation
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();
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
                Toast.makeText(getApplicationContext(), "Bienvenido a los Productos", Toast.LENGTH_SHORT).show();
                break;
            case R.id.item_menu_categoria:
                fragment = new FragmentCategorias();
                Toast.makeText(getApplicationContext(), "Bienvenido a las Categorias", Toast.LENGTH_SHORT).show();
                break;
            case R.id.item_menu_supers:
                fragment = new FragmentSupers();
                Toast.makeText(getApplicationContext(), "Bienvenido a los Supermercados", Toast.LENGTH_SHORT).show();
                break;
            case R.id.item_menu_acercade:
                fragment = new FragmentAcercaDe();
                break;
        }

        if (fragment != null){
            FragmentTransaction ft =  getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.contenedor, fragment);
            ft.addToBackStack(null);
            ft.commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

    }



}
