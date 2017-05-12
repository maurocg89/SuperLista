package com.example.superlista;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.superlista.data.SuperListaDbManager;
import com.example.superlista.model.Lista;
import com.example.superlista.model.Producto;
import com.example.superlista.model.ProductoPorLista;
import com.example.superlista.model.Supermercado;

import java.util.ArrayList;
import java.util.List;


public class FragmentPrecioListaSuper extends Fragment {

    private ListView listView;
    private TextView tvTotal;
    private ArrayList<ProductoPorLista> productoPorListasSupers;
    private ArrayList<ProductoPorLista> productosFaltantes;
    //private ArrayAdapter<ProductoPorLista> myAdapter;
    private PrecioListaSuperAdapter myAdapter;
    private String supermercado;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lista_precio_por_super, container, false);

        listView = (ListView) view.findViewById(R.id.lvListaPorSuper);
        tvTotal = (TextView) view.findViewById(R.id.tvTotalPorSuper);
        setData();

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        supermercado = getArguments().getString(Supermercado.COLUMNA_NOMBRE);
        getActivity().setTitle(supermercado);
    }

    private void setData(){
        double total = 0;
        productoPorListasSupers = new ArrayList<>();
        productosFaltantes = new ArrayList<>();

        try {

            total = getArguments().getDouble("Total", 0);
            productoPorListasSupers = getArguments().getParcelableArrayList("Productos");
            productosFaltantes = getArguments().getParcelableArrayList("Productos Faltantes");
            for (ProductoPorLista ppl: productosFaltantes) {
                ppl.getProducto().setPrecio(0);
                ppl.getProducto().setNombre("Producto Faltante: "+ppl.getProducto().getNombre());
                productoPorListasSupers.add(ppl);
            }
            myAdapter = new PrecioListaSuperAdapter(getActivity(), productoPorListasSupers);

            listView.setAdapter(myAdapter);
        }catch (Exception e){
            e.printStackTrace();
        }

        tvTotal.setText("Total: "+total);
    }

    private class PrecioListaSuperAdapter extends BaseAdapter{

        private Context context;
        private ArrayList<ProductoPorLista> productoPorListas;
        private LayoutInflater inflater;


        public PrecioListaSuperAdapter(Context context, ArrayList<ProductoPorLista> productoPorListas){
            this.context = context;
            this.productoPorListas = productoPorListas;
        }

        @Override
        public int getCount() {
            return productoPorListas.size();
        }

        @Override
        public ProductoPorLista getItem(int i) {
            return productoPorListas.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(final int i, View view, ViewGroup viewGroup) {
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            ViewHolderPrecioListaSuper holder;

            if (view == null){
                holder = new ViewHolderPrecioListaSuper();
                view = inflater.inflate(R.layout.list_item_precio_por_super, null);
                holder.producto = (TextView) view.findViewById(R.id.lista_por_super_producto);
                holder.precioTotal = (TextView) view.findViewById(R.id.lista_por_super_precio);
                holder.textoPrecioPorUnidad = (TextView) view.findViewById(R.id.lista_por_super_texto_precio_unidad);
                holder.precioPorUnidad = (TextView) view.findViewById(R.id.lista_por_super_precio_unidad);
                view.setTag(holder);
            }else{
                holder = (ViewHolderPrecioListaSuper) view.getTag();
            }

            ProductoPorLista productoPorLista = productoPorListas.get(i);
            holder.producto.setText(productoPorLista.toString());
            holder.precioTotal.setText(String.valueOf(productoPorLista.getCantidad() * productoPorLista.getProducto().getPrecio()));
            return view;
        }
    }

    private class ViewHolderPrecioListaSuper{
        TextView producto;
        TextView precioTotal;
        TextView textoPrecioPorUnidad;
        TextView precioPorUnidad;

    }
}
