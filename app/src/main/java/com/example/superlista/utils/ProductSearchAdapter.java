package com.example.superlista.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.superlista.R;
import com.example.superlista.model.Producto;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ProductSearchAdapter extends BaseAdapter{

    private Context context;
    private List<Producto> data;
    private LayoutInflater inflater;
    private SparseBooleanArray mSelectedItemsIds;

    /*public ProductSearchAdapter(Activity activity) {
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }*/

    public ProductSearchAdapter(Context context, List<Producto> productos) {
        this.context = context;
        this.data = productos;
        mSelectedItemsIds = new SparseBooleanArray();
    }

    public void addItem(Producto item){
        data.add(item);
        notifyDataSetChanged();

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
            convertView = inflater.inflate(R.layout.list_item, null);
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
            convertView = inflater.inflate(R.layout.list_item, null);
            holder.textView = (TextView) convertView.findViewById(R.id.list_item_texto);
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolderProducto) convertView.getTag();
        }
        Producto prod = data.get(position);
        holder.textView.setText(prod.toString());

        // Cambia el color de fondo de los items seleccionados
        convertView.setBackgroundColor(mSelectedItemsIds.get(position) ? Color.parseColor("#a9a9a9") : Color.TRANSPARENT);

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

class ViewHolderProducto{
    TextView textView;
    // CheckBox checkBox;
}

