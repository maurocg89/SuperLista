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
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.example.superlista.data.SuperListaDbManager;
import com.example.superlista.model.Lista;
import java.util.ArrayList;
import java.util.List;



public class FragmentListas extends Fragment {


    private ListView listViewListas;
    private List<Lista> listas;
    private ArrayList<String> nombres;
    private ArrayAdapter<String> myAdapter;
    Fragment fragmento = null;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_listas, container, false);


        llamarFloatingButtonAction(view);

        listViewListas = (ListView) view.findViewById(R.id.lvLista);
        nombres = new ArrayList<String>();
        setData();

        return view;
    }


    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle(R.string.item_lista);
    }


    private void setData(){
        listas = SuperListaDbManager.getInstance().getAllListas();

        for (Lista lista: listas) {
            nombres.add(lista.getNombre());
        }
        myAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, nombres);
        listViewListas.setAdapter(myAdapter);

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
