package com.example.superlista;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FragmentListas.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class FragmentListas extends Fragment {

    private OnFragmentInteractionListener mListener;

    private ListView listView1;
    private List<Producto> productos;
    private List<Lista> listas;
    private List<Categoria> categorias;
    private List<ProductoPorLista> productoPorListas;
    private List<Supermercado> supermercados;
    private ArrayList<String> nombres;
    private ArrayAdapter<String> myAdapter;


    public FragmentListas() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_productos, container, false);

        SuperListaDbManager.init(getContext());

        listView1 = (ListView) view.findViewById(R.id.lvLista);
        nombres = new ArrayList<String>();
        setData();


        return view;
    }


    private void setData(){
        listas = SuperListaDbManager.getInstance().getAllListas();
        productos = SuperListaDbManager.getInstance().getAllProductos();
        categorias = SuperListaDbManager.getInstance().getAllCategorias();
        supermercados = SuperListaDbManager.getInstance().getAllSupermercados();

        for (Lista lista: listas) {
            nombres.add(lista.getNombre());
        }
        myAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, nombres);
        listView1.setAdapter(myAdapter);

    }

    @Override
    public void onResume() {
        super.onResume();
        setData();
    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
