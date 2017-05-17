package com.example.superlista.utils;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.superlista.R;
import com.example.superlista.model.Producto;

import org.w3c.dom.Text;

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
        holder.nombreProducto.setText(prod.getNombre());
        holder.marcaProducto.setText(prod.getMarca());
        // TODO: Acá se le asigna la imagen al producto
        if(prod.getImagen() != null) {
            holder.imagenProducto.setImageURI(Uri.parse(prod.getImagen()));
        }
        return convertView;
    }

    private class ViewHolderProducto{
        TextView nombreProducto;
        TextView marcaProducto;
        ImageView imagenProducto;
    }
}


