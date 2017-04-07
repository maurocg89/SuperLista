package com.example.superlista;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.superlista.data.SuperListaDbManager;
import com.example.superlista.model.Categoria;
import com.example.superlista.model.Lista;
import com.example.superlista.model.Producto;
import com.example.superlista.model.ProductoPorLista;
import com.example.superlista.model.Supermercado;

import java.util.ArrayList;
import java.util.List;

public class PruebaBD extends AppCompatActivity {

    private ListView listView1;
    private List<Producto> productos;
    private List<Lista> listas;
    private List<Categoria> categorias;
    private List<ProductoPorLista> productoPorListas;
    private List<Supermercado> supermercados;
    private ArrayList<String>nombres;
    private ArrayAdapter<String> myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prueba_bd);
        SuperListaDbManager.init(this);

        listView1 = (ListView) findViewById(R.id.lvLista);
        nombres = new ArrayList<String>();
        setData();
    }

    private void setData(){
        listas = SuperListaDbManager.getInstance().getAllListas();
        productos = SuperListaDbManager.getInstance().getAllProductos();
        categorias = SuperListaDbManager.getInstance().getAllCategorias();
        supermercados = SuperListaDbManager.getInstance().getAllSupermercados();

        for (Lista lista: listas) {
            nombres.add(lista.getNombre());
        }
        myAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, nombres);
        listView1.setAdapter(myAdapter);

    }



}
