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
import com.example.superlista.model.Supermercado;

import java.util.ArrayList;
import java.util.List;


public class FragmentSupers extends Fragment {


    private ListView listView1;
    private List<Supermercado> supermercados;
    private ArrayList<String> nombres;
    private ArrayAdapter<String> myAdapter;



    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_supers, container, false);

        //SuperListaDbManager.init(getContext());

        listView1 = (ListView) view.findViewById(R.id.lvLista);
        nombres = new ArrayList<String>();
        setData();

        return view;
    }


    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle(R.string.item_supers);
    }


    private void setData(){

        supermercados = SuperListaDbManager.getInstance().getAllSupermercados();

        for (Supermercado supermercado: supermercados) {
            nombres.add(supermercado.getNombre() + " " + supermercado.getSucursal());
        }
        myAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, nombres);
        listView1.setAdapter(myAdapter);

    }


}
