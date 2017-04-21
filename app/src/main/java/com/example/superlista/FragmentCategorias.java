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
import com.example.superlista.model.Categoria;

import java.util.ArrayList;
import java.util.List;


public class FragmentCategorias extends Fragment {

    private ListView listView;
    private ArrayList<String> nombreCategorias;
    private List<Categoria> categorias;
    private ArrayAdapter<String> myAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_categorias, container, false);
        listView = (ListView) view.findViewById(R.id.lvCategorias);
        setData();
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle(R.string.item_categorias);
    }

    private void setData(){
        categorias = SuperListaDbManager.getInstance().getAllCategorias();
        nombreCategorias = new ArrayList<>();

        for(Categoria categoria : categorias){
            nombreCategorias.add(categoria.getNombre());
        }

        myAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, nombreCategorias);
        listView.setAdapter(myAdapter);
    }
}
