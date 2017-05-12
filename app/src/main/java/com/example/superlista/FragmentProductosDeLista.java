package com.example.superlista;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.superlista.data.SuperListaDbManager;
import com.example.superlista.model.Lista;
import com.example.superlista.model.Producto;
import com.example.superlista.model.ProductoPorLista;
import com.example.superlista.model.Supermercado;

import java.util.ArrayList;
import java.util.List;

// TODO: Agregar relative layout checkable y sumar los totales. Agregar menu para borrar producto
public class FragmentProductosDeLista extends Fragment {

    private ListView listView;
    private List<ProductoPorLista> productosPorLista;
    private ArrayList<ProductoPorLista> productosPorSuper;
    private ArrayList<ProductoPorLista> productosFaltantes;
    private ArrayAdapter<ProductoPorLista> myAdapter;
    private int id_lista;
    private ImageView btnCoto;
    private ImageView btnLaGallega;
    private ImageView btnCarrefour;
    private FragmentPrecioListaSuper fragmentListaPorSuper;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_productos_de_lista, container, false);

        listView = (ListView) view.findViewById(R.id.lvProductosDeLista);
        btnCoto = (ImageView) view.findViewById(R.id.imgBtnCoto);
        btnCarrefour = (ImageView) view.findViewById(R.id.imgBtnCarrefour);
        btnLaGallega = (ImageView) view.findViewById(R.id.imgBtnLaGallega);

        setData();
        imageViewListeners();
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Lista lista = SuperListaDbManager.getInstance().getListaById(id_lista);
        getActivity().setTitle("Productos de "+lista.getNombre());
    }

    private void setData(){
        try {
            id_lista = getArguments().getInt(Lista._ID);
        }catch (Exception e){
            e.printStackTrace();
        }

        if (id_lista != 0){
            // productos = SuperListaDbManager.getInstance().getProductosPorLista(id_lista);
            //    SuperListaDbManager.getInstance().getProductoById(productosPorLista.get(0).getProducto().getId_producto());
            productosPorLista = SuperListaDbManager.getInstance().getAllProductosListas(id_lista);
            myAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, productosPorLista);
            listView.setAdapter(myAdapter);

        }
    }

    private double getTotal(int id_super){

        productosPorSuper = new ArrayList<>();
        productosFaltantes = new ArrayList<>();

        if (productosPorLista.size() == 0){return 0;}

        double total = 0;
        for (ProductoPorLista prod: productosPorLista) {
            int cantidad = prod.getCantidad();
            Producto p = SuperListaDbManager.getInstance().getProductoByNombreSuper(prod.getProducto().getNombre(),
                    prod.getProducto().getMarca(), id_super);
            if (p == null){
                productosFaltantes.add(prod);
            } else {
                total += p.getPrecio() * cantidad;
                prod.getProducto().setPrecio(p.getPrecio());
                productosPorSuper.add(prod);
            }
        }
        return total;
    }
    
    private void imageViewListeners(){
        fragmentListaPorSuper = new FragmentPrecioListaSuper();
        final Bundle bundle = new Bundle();

        btnLaGallega.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             //   Toast.makeText(getContext(), "Total en La Gallega: "+getTotal(Supermercado.ID_LA_GALLEGA), Toast.LENGTH_LONG).show();

                bundle.putString(Supermercado.COLUMNA_NOMBRE,"La Gallega");
                bundle.putDouble("Total", getTotal(Supermercado.ID_LA_GALLEGA));
                bundle.putParcelableArrayList("Productos", productosPorSuper);
                bundle.putParcelableArrayList("Productos Faltantes", productosFaltantes);

                fragmentListaPorSuper.setArguments(bundle);

                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.contenedor, fragmentListaPorSuper);
                ft.commit();
            }
        });

        btnCoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             //   Toast.makeText(getContext(), "Total en Coto: "+getTotal(Supermercado.ID_COTO), Toast.LENGTH_LONG).show();

                bundle.putString(Supermercado.COLUMNA_NOMBRE,"Coto");
                bundle.putDouble("Total", getTotal(Supermercado.ID_COTO));
                bundle.putParcelableArrayList("Productos", productosPorSuper);
                bundle.putParcelableArrayList("Productos Faltantes", productosFaltantes);
                fragmentListaPorSuper.setArguments(bundle);

                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.contenedor, fragmentListaPorSuper);
                ft.commit();

            }
        });

        btnCarrefour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // Toast.makeText(getContext(), "Total en Carrefour: "+getTotal(Supermercado.ID_CARREFOUR), Toast.LENGTH_LONG).show();

                bundle.putString(Supermercado.COLUMNA_NOMBRE,"Carrefour");
                bundle.putDouble("Total", getTotal(Supermercado.ID_CARREFOUR));
                bundle.putParcelableArrayList("Productos", productosPorSuper);
                bundle.putParcelableArrayList("Productos Faltantes", productosFaltantes);
                fragmentListaPorSuper.setArguments(bundle);

                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.contenedor, fragmentListaPorSuper);
                ft.commit();
            }
        });
    }

    private void listViewListener(){

    }
}
