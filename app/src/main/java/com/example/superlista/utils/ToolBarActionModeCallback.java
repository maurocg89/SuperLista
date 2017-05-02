package com.example.superlista.utils;

import android.content.Context;
import android.support.v7.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;

import com.example.superlista.FragmentProductos;
import com.example.superlista.R;
import com.example.superlista.model.Producto;

import java.util.List;

public class ToolBarActionModeCallback implements ActionMode.Callback {

    private Context context;
    private ProductListAdapter adapter;
    private List<Producto> mensajes;
    //private boolean isListViewFragment;

    public ToolBarActionModeCallback(Context context, ProductListAdapter adapter, List<Producto> mensajes) {
        this.context = context;
        this.adapter = adapter;
        this.mensajes = mensajes;
        //this.isListViewFragment = isListViewFragment;

    }

    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        mode.getMenuInflater().inflate(R.menu.menu_productos, menu);
        return true;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        menu.findItem(R.id.action_eliminar_producto).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        //menu.findItem(R.id.forward).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS); para deseleccionar items
        return false;
    }

    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_eliminar_producto:
                FragmentProductos fragment = new FragmentProductos();
                //fragment.deleteRows();
                break;
        }

        return false;
    }

    @Override
    public void onDestroyActionMode(ActionMode mode) {
        adapter.removeSelection();
        FragmentProductos fragmentProductos = new FragmentProductos();
        fragmentProductos.setNullToActionMode();

    }
}
