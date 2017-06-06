package com.example.superlista;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.superlista.data.SuperListaDbManager;
import com.example.superlista.model.Lista;

import java.util.ArrayList;
import java.util.List;

public class FragmentListas extends Fragment {

    private static ActionMode mActionMode;
    private ListView listViewListas;
    private List<Lista> listas;
    private ListasAdapter listasAdapter;
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

        //myAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, listas);
        listasAdapter = new ListasAdapter(getContext(), listas);
        //listViewListas.setAdapter(myAdapter);
        listViewListas.setAdapter(listasAdapter);

    }

    private void implementsListViewListener(){
        listViewListas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                if (mActionMode != null){
                    onListItemSelect(position);
                } else {
                    //id_lista = myAdapter.getItem(position).getId_lista();
                    id_lista = listasAdapter.getItem(position).getId_lista();
                    Bundle bundle = new Bundle();
                    bundle.putInt(Lista._ID, id_lista);

                    FragmentProductosDeLista fragmentProductosDeLista = new FragmentProductosDeLista();
                    fragmentProductosDeLista.setArguments(bundle);
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.replace(R.id.contenedor, fragmentProductosDeLista);
                    ft.addToBackStack("ProductosDeLista");
                    ft.commit();
                }
            }
        });

        listViewListas.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                onListItemSelect(position);
                return true;
            }
        });
    }


    private void onListItemSelect(int position){
        listasAdapter.toggleSelection(position);
        boolean hasCheckedItems = listasAdapter.getSelectedCount() > 0; // Se fija si hay algun item seleccionado
        if (hasCheckedItems && mActionMode == null){
            mActionMode = ((AppCompatActivity) getActivity()).startSupportActionMode(new ToolBarActionModeCallbackListas(getActivity(), listasAdapter, listas, this));
        } else if (!hasCheckedItems && mActionMode != null){
            // no hay ningun item seleccionado, termino el action mode
            mActionMode.finish();
            setNullToActionMode();

        }
        // Pongo la cantidad de items seleccionados
        if (mActionMode != null){
            mActionMode.setTitle(String.valueOf(listasAdapter.getSelectedCount()) + " seleccionado(s)");
        }

    }

    public void eliminarListas(MenuItem item, Context context){

        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("Desea eliminar la(s) lista(s) seleccionada(s)?");
        builder.setTitle("Eliminar");

        builder.setPositiveButton("Eliminar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try{
                    SparseBooleanArray array = listasAdapter.getSelectedIds();
                    ArrayList<Lista> seleccion = new ArrayList<>();
                    for (int i = 0; i < array.size(); i++){
                        // Posicion de la lista en el adaptador
                        int pos = array.keyAt(i);
                        if(array.valueAt(i)) {
                            seleccion.add(listasAdapter.getItem(pos));
                        }
                    }
                    SuperListaDbManager.getInstance().deleteListas(seleccion);
                    // Borro los productos por lista asociados a la lista eliminada
                    SuperListaDbManager.getInstance().deleteProductosPorListas(seleccion);
                    listas.removeAll(seleccion);
                    listasAdapter.notifyDataSetChanged();
                    mActionMode.finish();
                }catch (Exception e){
                    e.printStackTrace();
                }
                dialog.dismiss();

            }

        });

        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });

        builder.show();

    }

    private void llamarFloatingButtonAction(View vista){

        FloatingActionButton fab = (FloatingActionButton) vista.findViewById(R.id.boton_de_accion_listas);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                fragmento = new FragmentAgregarLista();
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.contenedor, fragmento);
                ft.addToBackStack("AgregarLista");
                ft.commit();
                //Snackbar.make(view, "aca va la accion", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }
        });


    }

    // Seteo en null el action mode despues de usarlo
    public void setNullToActionMode(){
        if (mActionMode != null){
            mActionMode = null;
        }
    }

    private class ToolBarActionModeCallbackListas implements ActionMode.Callback {

        private Context context;
        private ListasAdapter adapter;
        private List<Lista> mensajes;
        private FragmentListas fragmentListas;

        public ToolBarActionModeCallbackListas(Context context, ListasAdapter adapter, List<Lista> mensajes, FragmentListas fragmentListas) {
            this.context = context;
            this.adapter = adapter;
            this.mensajes = mensajes;
            this.fragmentListas = fragmentListas;

        }

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            mode.getMenuInflater().inflate(R.menu.menu_productos, menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            menu.findItem(R.id.action_eliminar_producto).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch (item.getItemId()){
                case R.id.action_eliminar_producto:
                    fragmentListas.eliminarListas(item, this.context);
                    return true;
            }

            return false;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            adapter.removeSelection();
            fragmentListas.setNullToActionMode();

        }
    }

    private class ListasAdapter extends BaseAdapter{

        private Context context;
        private List<Lista> data;
        private LayoutInflater inflater;
        private SparseBooleanArray mSelectedItemsIds;

        public ListasAdapter(Context context, List<Lista> listas) {
            this.context = context;
            this.data = listas;
            mSelectedItemsIds = new SparseBooleanArray();
        }


        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public Lista getItem(int position){
            return data.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            ViewHolderList holder;

            if (convertView == null){
                holder = new ViewHolderList();
                convertView = inflater.inflate(R.layout.list_item_lista, null);
                holder.nombre_lista = (TextView) convertView.findViewById(R.id.list_item_lista);
                convertView.setTag(holder);
            }
            else {
                holder = (ViewHolderList) convertView.getTag();
            }

            holder.nombre_lista.setText(data.get(position).getNombre());
            // Cambia el color de fondo de los items seleccionados
            convertView.setBackgroundColor(mSelectedItemsIds.get(position) ? 0x9934B5E4 : Color.TRANSPARENT);

            return convertView;
        }


        public void toggleSelection(int position){
            selectView(position, !mSelectedItemsIds.get(position));
        }

        // Borra las selecciones
        public void removeSelection(){
            mSelectedItemsIds = new SparseBooleanArray();
            notifyDataSetChanged();
        }

        // Agrega o borra la posicion seleccionada en el array SparseBoolean
        public void selectView(int position, boolean value) {
            if (value){
                mSelectedItemsIds.put(position, value);
            } else{
                mSelectedItemsIds.delete(position);
            }
            notifyDataSetChanged();
        }

        // La cantidad de items seleccionados
        public int getSelectedCount(){
            return mSelectedItemsIds.size();
        }

        // Todos los ids de los items seleccionados
        public SparseBooleanArray getSelectedIds(){
            return mSelectedItemsIds;
        }

    }

    private class ViewHolderList{
        TextView nombre_lista;
    }
}
