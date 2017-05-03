package com.example.superlista.utils;

import android.content.Context;
import android.graphics.Color;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.superlista.R;
import com.example.superlista.model.Categoria;

import java.util.List;

public class CategoryListAdapter extends BaseAdapter {


    private Context context;
    private List<Categoria> data;
    private LayoutInflater inflater;
    private SparseBooleanArray mSelectedItemsIds;

    public CategoryListAdapter(Context context, List<Categoria> categorias) {
        this.context = context;
        this.data = categorias;
        mSelectedItemsIds = new SparseBooleanArray();
    }


    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Categoria getItem(int position){
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ViewHolder holder;

        if (convertView == null){
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.list_item_category, null);
            holder.textView = (TextView) convertView.findViewById(R.id.list_item_categoria);
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }

        Categoria categoria = data.get(position);
        holder.textView.setText(categoria.toString());

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
