package com.example.superlista;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.superlista.data.SuperListaDbManager;
import com.example.superlista.model.ProductoPorLista;
import com.example.superlista.model.Supermercado;

import java.text.DecimalFormat;
import java.util.ArrayList;


public class FragmentPrecioListaSuper extends Fragment {

    private ListView listView;
    private TextView tvTotal;
    private ArrayList<ProductoPorLista> productoPorListasSupers;
    private ArrayList<ProductoPorLista> productosFaltantes;
    private PrecioListaSuperAdapter myAdapter;
    private Supermercado supermercado;

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
        getActivity().setTitle(supermercado.getNombre());
    }

    // TODO: 22/06/2017 ver decimales
    private void setData() {
        double total = 0;
        productoPorListasSupers = new ArrayList<>();
        productosFaltantes = new ArrayList<>();

        try {
            int id_super = getArguments().getInt(Supermercado._ID);
            supermercado = SuperListaDbManager.getInstance().getSupermercadoById(id_super);
            total = getArguments().getDouble("Total", 0);
            productoPorListasSupers = getArguments().getParcelableArrayList("Productos");
            productosFaltantes = getArguments().getParcelableArrayList("Productos Faltantes");
            if (productosFaltantes.size() > 0) {
                for (ProductoPorLista ppl : productosFaltantes) {
                    ppl.getProducto().setNombre("Producto Faltante: " + ppl.getProducto().getNombre());
                    productoPorListasSupers.add(ppl);
                }
            }
            myAdapter = new PrecioListaSuperAdapter(getActivity(), productoPorListasSupers, supermercado);
            listView.setAdapter(myAdapter);

        } catch (Exception e) {
            e.printStackTrace();
        }
        DecimalFormat df = new DecimalFormat("####.00");
        tvTotal.setText("Total: " + df.format(total));
    }

    private class PrecioListaSuperAdapter extends BaseAdapter {

        private Context context;
        private ArrayList<ProductoPorLista> productoPorListas;
        private LayoutInflater inflater;
        private Supermercado supermercado;


        public PrecioListaSuperAdapter(Context context, ArrayList<ProductoPorLista> productoPorListas, Supermercado supermercado) {
            this.context = context;
            this.productoPorListas = productoPorListas;
            this.supermercado = supermercado;
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

            if (view == null) {
                holder = new ViewHolderPrecioListaSuper();
                view = inflater.inflate(R.layout.list_item_precio_por_super, null);
                holder.producto = (TextView) view.findViewById(R.id.lista_por_super_producto);
                holder.precioTotal = (TextView) view.findViewById(R.id.lista_por_super_precio);
                holder.textoPrecioPorUnidad = (TextView) view.findViewById(R.id.lista_por_super_texto_precio_unidad);
                holder.precioPorUnidad = (TextView) view.findViewById(R.id.lista_por_super_precio_unidad);
                view.setTag(holder);
            } else {
                holder = (ViewHolderPrecioListaSuper) view.getTag();
            }

            ProductoPorLista productoPorLista = productoPorListas.get(i);
            holder.producto.setText(productoPorLista.toString());
            holder.textoPrecioPorUnidad.setText("Precio por " + productoPorLista.getProducto().getUnidad());
            double precioTotal = 0;
            DecimalFormat df = new DecimalFormat("####.00");
            switch (supermercado.getId_supermercado()) {
                case Supermercado.ID_COTO:
                    precioTotal = (productoPorLista.getCantidad() * productoPorLista.getProducto().getPrecio_coto());
                    holder.precioTotal.setText(df.format(precioTotal));
                    holder.precioPorUnidad.setText(String.valueOf(productoPorLista.getProducto().getPrecio_coto()));
                    break;
                case Supermercado.ID_LA_GALLEGA:
                    precioTotal = (productoPorLista.getCantidad() * productoPorLista.getProducto().getPrecio_la_gallega());
                    holder.precioTotal.setText(df.format(precioTotal));
                    holder.precioPorUnidad.setText(df.format(productoPorLista.getProducto().getPrecio_la_gallega()));
                    break;
                case Supermercado.ID_CARREFOUR:
                    holder.precioTotal.setText(String.valueOf
                            (productoPorLista.getCantidad() * productoPorLista.getProducto().getPrecio_carrefour()));
                    holder.precioPorUnidad.setText(String.valueOf(productoPorLista.getProducto().getPrecio_carrefour()));
                    break;
                case Supermercado.ID_OTRO:
                    holder.precioTotal.setText(String.valueOf
                            (productoPorLista.getCantidad() * productoPorLista.getProducto().getPrecio_otro()));
                    holder.precioPorUnidad.setText(String.valueOf(productoPorLista.getProducto().getPrecio_otro()));
                    break;
                default:
                    holder.precioTotal.setText("0");
                    break;
            }
            return view;
        }
    }

    private class ViewHolderPrecioListaSuper {
        TextView producto;
        TextView precioTotal;
        TextView textoPrecioPorUnidad;
        TextView precioPorUnidad;

    }
}
