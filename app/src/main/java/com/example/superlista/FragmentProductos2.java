package com.example.superlista;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.support.v7.widget.SearchView.OnQueryTextListener;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.superlista.data.SuperListaDbManager;
import com.example.superlista.model.Producto;


import java.util.ArrayList;
import java.util.List;


public class FragmentProductos2 extends ListFragment implements SearchView.OnQueryTextListener, MenuItem.OnActionExpandListener{

    private List<Producto> productos;
    private ArrayList<String> nombres;
    private ArrayAdapter<String> myAdapter;
    private ListView listView1;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_productos_prueba, container, false);
        listView1 = (ListView) view.findViewById(android.R.id.list);
        setData();
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle(R.string.item_productos);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_search, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(this);
        searchView.setQueryHint("Buscar");

        super.onCreateOptionsMenu(menu, inflater);

        //super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        String item = (String) listView1.getAdapter().getItem(position);
        if(getActivity() instanceof OnItemSelectedListener1){
            ((OnItemSelectedListener1) getActivity()).OnItemSelectedListener1(item);
        }
        getFragmentManager().popBackStack();
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if(newText == null || newText.trim().isEmpty()){
            resetSearch();
            return false;
        }
        List<String> filteredValues = new ArrayList<String>(nombres);
        for (String value : nombres){
            if (!value.toLowerCase().contains(newText.toLowerCase())){
                filteredValues.remove(value);
            }
        }

        myAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, filteredValues);
        setListAdapter(myAdapter);

        return false;
    }

    @Override
    public boolean onMenuItemActionExpand(MenuItem menuItem) {
        return true;
    }

    @Override
    public boolean onMenuItemActionCollapse(MenuItem menuItem) {
        return true;
    }

    private void setData(){
        //searchAdapter = new SearchAdapterProducto(getActivity());
        productos = SuperListaDbManager.getInstance().getAllProductosByNameDistinct();
        nombres = new ArrayList<String>();

        for(Producto producto : productos){
            nombres.add(producto.getNombre() + " " + producto.getMarca());
            //searchAdapter.addItem(producto.getNombre() + " " + producto.getMarca());
        }

        myAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, nombres);
        //listView1.setAdapter(searchAdapter);
        listView1.setAdapter(myAdapter);

    }

    private void resetSearch(){
        myAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, nombres);
    }

    public interface OnItemSelectedListener1{
        void OnItemSelectedListener1(String item);
    }

}
