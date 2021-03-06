package com.example.superlista.utils;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.superlista.R;
import com.example.superlista.data.SuperListaDbManager;
import com.example.superlista.model.Producto;

import java.util.List;

public class ProductListAdapter extends BaseAdapter{

    private Context context;
    private List<Producto> data;
    private LayoutInflater inflater;
    private SparseBooleanArray mSelectedItemsIds;

   /* public ProductListAdapter(Activity activity) {
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }*/

    public ProductListAdapter(Context context, List<Producto> productos) {
        this.context = context;
        this.data = productos;
        mSelectedItemsIds = new SparseBooleanArray();
    }


    @Override
    public int getCount() {
        return data.size();
    }

   @Override
    public Producto getItem(int position){
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

  /*  @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolderProducto holder;
        if (convertView == null){
            holder = new ViewHolderProducto();
            convertView = inflater.inflate(R.layout.list_item_product, null);
            holder.textView = (TextView) convertView.findViewById(R.id.list_item_texto);
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolderProducto) convertView.getTag();
        }
        Producto prod = data.get(position);
        holder.textView.setText(prod.toString());
        return convertView;
    }*/

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ViewHolderProducto holder;

        if (convertView == null){
            holder = new ViewHolderProducto();
            convertView = inflater.inflate(R.layout.list_item_product, null);
            holder.nombreProducto = (TextView) convertView.findViewById(R.id.list_item_texto);
            holder.marcaProducto = (TextView) convertView.findViewById(R.id.list_item_marca);
            holder.imagenProducto = (ImageView) convertView.findViewById(R.id.list_item_imagen);
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolderProducto) convertView.getTag();
        }

        Producto prod = data.get(position);
        String marcaProd = prod.getMarca().getNombre();
        holder.nombreProducto.setText(prod.getNombre());
        if (marcaProd.equalsIgnoreCase("ninguna")){
            holder.marcaProducto.setText("");
        } else {
            holder.marcaProducto.setText(prod.getMarca().toString());
        }

        if(prod.getImagen() != null) {
            String direc_imagen = prod.getImagen();
            Uri imagen = Uri.parse(direc_imagen);
            holder.imagenProducto.setImageURI(imagen);
        }

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

    private class ViewHolderProducto{
        TextView nombreProducto;
        TextView marcaProducto;
        ImageView imagenProducto;
    }

}



