package com.example.superlista;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.example.superlista.data.SuperListaDbManager;
import com.example.superlista.model.Categoria;
import com.example.superlista.model.Lista;
import com.example.superlista.model.Producto;
import com.example.superlista.model.ProductoPorLista;
import com.example.superlista.model.Supermercado;

import java.util.ArrayList;
import java.util.List;

public class FragmentProductos extends Fragment {

    private ListView listView1;

    private List<Producto> productos;
    private List<Lista> listas;
    private List<Categoria> categorias;
    private List<ProductoPorLista> productoPorListas;
    private List<Supermercado> supermercados;
    private ArrayList<String> nombres;
    private ArrayAdapter<String> myAdapter;

    private PruebaBD.SearchAdapter searchAdapter;

    private Button btnSpeak;
    private EditText etSearch;
    private final int REQ_CODE_SPEECH_OUTPUT = 143;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_productos, container, false);

        listView1 = (ListView) view.findViewById(R.id.lvLista);
        btnSpeak = (Button) view.findViewById(R.id.btnSpeak);
        etSearch = (EditText) view.findViewById(R.id.etTextHint);
       // searchAdapter = new PruebaBD.SearchAdapter(this);

        setData();

        return view;

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle(R.string.item_productos);
    }
    // TODO: Ver porque no trae todos los productos de la base de datos (trae uno solo de los que tienen el mismo nombre)
    private void setData(){
       // searchAdapter = new PruebaBD.SearchAdapter(this);
        productos = SuperListaDbManager.getInstance().getAllProductos();
        //productos = SuperListaDbManager.getInstance().getAllProductosByNameDistinct();
        nombres = new ArrayList<String>();

        for(Producto producto : productos){
            nombres.add(producto.getNombre() + " " + producto.getMarca());
            //searchAdapter.addItem(producto.getNombre());
        }

        myAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, nombres);
        //listView1.setAdapter(searchAdapter);
        listView1.setAdapter(myAdapter);

    }
}
