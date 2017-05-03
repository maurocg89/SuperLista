package com.example.superlista.utils;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.superlista.R;
import com.example.superlista.model.Categoria;
import com.example.superlista.model.Producto;

import java.util.ArrayList;

public class CategorySearchAdapter extends BaseAdapter{

    private ArrayList<Categoria> data = new ArrayList<>();
    private LayoutInflater inflater;

    public CategorySearchAdapter(Activity activity) {
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void addItem(Categoria item){
        data.add(item);
        notifyDataSetChanged();
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
        return convertView;
    }
}


