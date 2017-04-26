package com.example.superlista.utils;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.superlista.R;

import java.util.ArrayList;

/**
 * Created by user on 25/04/2017.
 */

public class ProductSearchAdapter extends BaseAdapter{

    private ArrayList<String> data = new ArrayList<>();
    private LayoutInflater inflater;

    public ProductSearchAdapter(Activity activity) {
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void addItem(String item){
        data.add(item);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public String getItem(int position) {
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
            convertView = inflater.inflate(R.layout.list_item, null);
            holder.textView = (TextView) convertView.findViewById(R.id.list_item_texto);
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolderProducto) convertView.getTag();
        }
        String str = data.get(position);
        holder.textView.setText(str);
        return convertView;
    }
}

class ViewHolderProducto{
    TextView textView;
    // CheckBox checkBox;
}

