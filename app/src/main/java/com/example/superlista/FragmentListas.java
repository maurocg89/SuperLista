package com.example.superlista;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.example.superlista.data.SuperListaDbManager;
import com.example.superlista.model.Lista;
import java.util.ArrayList;
import java.util.List;



public class FragmentListas extends Fragment {


    private ListView listViewListas;
    private List<Lista> listas;
    private ArrayAdapter<Lista> myAdapter;
    Fragment fragmento = null;
    private int id_lista;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_listas, container, false);


        llamarFloatingButtonAction(view);

        listViewListas = (ListView) view.findViewById(R.id.lvLista);

        setData();
        implementsListViewListener();
        return view;
    }

    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle(R.string.item_lista);
    }


    private void setData(){
        listas = SuperListaDbManager.getInstance().getAllListas();

        myAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, listas);
        listViewListas.setAdapter(myAdapter);

    }

    private void implementsListViewListener(){
        listViewListas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                id_lista = myAdapter.getItem(position).getId_lista();
                Bundle bundle = new Bundle();
                bundle.putInt(Lista._ID, id_lista);

                FragmentProductosDeLista fragmentProductosDeLista = new FragmentProductosDeLista();
                fragmentProductosDeLista.setArguments(bundle);
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.contenedor, fragmentProductosDeLista);
                ft.commit();
            }
        });
    }

    private void llamarFloatingButtonAction(View vista){

        FloatingActionButton fab = (FloatingActionButton) vista.findViewById(R.id.boton_de_accion_listas);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                fragmento = new FragmentAgregarLista();
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.contenedor, fragmento);
                ft.commit();
                //Snackbar.make(view, "aca va la accion", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }
        });


    }


}
