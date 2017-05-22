package com.example.superlista.utils;

import android.content.Context;
import android.support.v7.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;

import com.example.superlista.FragmentProductos;
import com.example.superlista.MainActivity;
import com.example.superlista.R;
import com.example.superlista.model.Producto;

import java.util.List;

public class ToolBarActionModeCallback implements ActionMode.Callback {

    private Context context;
    private ProductListAdapter adapter;
    private List<Producto> mensajes;
    private FragmentProductos fragmentProductos;

    public ToolBarActionModeCallback(Context context, ProductListAdapter adapter, List<Producto> mensajes, FragmentProductos fragmentProductos) {
        this.context = context;
        this.adapter = adapter;
        this.mensajes = mensajes;
        this.fragmentProductos = fragmentProductos;

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
                /*FragmentProductos fragment = new FragmentProductos();
                fragment.eliminarProducto(item, this.context);*/
                fragmentProductos.eliminarProducto(item, this.context);
                return true;
        }

        return false;
    }

    @Override
    public void onDestroyActionMode(ActionMode mode) {
        adapter.removeSelection();
       /* FragmentProductos fragmentProductos = new FragmentProductos();
        fragmentProductos.setNullToActionMode();*/
       fragmentProductos.setNullToActionMode();

    }
}
