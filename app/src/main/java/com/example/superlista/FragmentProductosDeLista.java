package com.example.superlista;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.superlista.data.SuperListaDbManager;
import com.example.superlista.model.Lista;
import com.example.superlista.model.Producto;
import com.example.superlista.model.ProductoPorLista;

import java.util.List;


public class FragmentProductosDeLista extends Fragment {

    private ListView listView;
    private List<ProductoPorLista> productosPorLista;
 //   private List<Producto> productos;
    private ArrayAdapter<ProductoPorLista> myAdapter;
    private int id_lista;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_productos_de_lista, container, false);

        listView = (ListView) view.findViewById(R.id.lvProductosDeLista);
        setData();

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Lista lista = SuperListaDbManager.getInstance().getListaById(id_lista);
        getActivity().setTitle("Productos de "+lista.getNombre());
    }

    private void setData(){
        try {
            id_lista = getArguments().getInt(Lista._ID);
        }catch (Exception e){
            e.printStackTrace();
        }

        if (id_lista != 0){
            //   productos = SuperListaDbManager.getInstance().getProductosPorLista(id_lista);
            //    SuperListaDbManager.getInstance().getProductoById(productosPorLista.get(0).getProducto().getId_producto());
            productosPorLista = SuperListaDbManager.getInstance().getAllProductosListas(id_lista);

            myAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, productosPorLista);
            listView.setAdapter(myAdapter);
        }
    }
}
