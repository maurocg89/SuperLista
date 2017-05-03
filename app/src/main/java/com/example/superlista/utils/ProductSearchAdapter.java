package com.example.superlista.utils;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.superlista.R;
import com.example.superlista.model.Producto;

import java.util.ArrayList;

public class ProductSearchAdapter extends BaseAdapter{

    private ArrayList<Producto> data = new ArrayList<>();
    private LayoutInflater inflater;

    public ProductSearchAdapter(Activity activity) {
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null){
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.list_item_product, null);
            holder.textView = (TextView) convertView.findViewById(R.id.list_item_texto);
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }
        Producto prod = data.get(position);
        holder.textView.setText(prod.toString());
        return convertView;
    }
}


