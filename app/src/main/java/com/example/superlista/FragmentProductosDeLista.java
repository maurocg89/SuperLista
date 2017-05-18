package com.example.superlista;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
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
    //private ArrayAdapter<ProductoPorLista> myAdapter;
    private ProductosDeListaAdapter myAdapter;

    private String nombreLista;
    private int id_lista;
    private ImageView btnCoto;
    private ImageView btnLaGallega;
    private ImageView btnCarrefour;
    private FragmentPrecioListaSuper fragmentListaPorSuper;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_productos_de_lista, container, false);

        llamarFloatingButtonAction(view);
        listView = (ListView) view.findViewById(R.id.lvProductosDeLista);
        btnCoto = (ImageView) view.findViewById(R.id.imgBtnCoto);
        btnCarrefour = (ImageView) view.findViewById(R.id.imgBtnCarrefour);
        btnLaGallega = (ImageView) view.findViewById(R.id.imgBtnLaGallega);

        setHasOptionsMenu(true);
        setData();
        imageViewListeners();
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        nombreLista = SuperListaDbManager.getInstance().getListaById(id_lista).getNombre();

        getActivity().setTitle("Productos de "+nombreLista);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_productos, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_eliminar_producto: eliminarProducto(item); return true;
            default: return super.onOptionsItemSelected(item);
        }
    }

    private void eliminarProducto(MenuItem item){
        SparseBooleanArray array = listView.getCheckedItemPositions();
        ArrayList<ProductoPorLista> seleccion = new ArrayList<>();
        for (int i = 0; i < array.size(); i++){
            // Posicion del contacto en el adaptador
            int pos = array.keyAt(i);
            if(array.valueAt(i)) {
                seleccion.add(myAdapter.getItem(pos));
            }
            SuperListaDbManager.getInstance().deleteProductosDeLista(seleccion);
            listView.clearChoices();

        }
    }

    // TODO: Agregar un menu para poder modificar la cantidad cuando solo hay un producto seleccionado
    
    
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
           // myAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, productosPorLista);
            myAdapter = new ProductosDeListaAdapter(getActivity(), productosPorLista);
            myAdapter.notifyDataSetChanged();
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

    private void llamarFloatingButtonAction(View vista) {

        FloatingActionButton fab = (FloatingActionButton) vista.findViewById(R.id.boton_de_accion_productos);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Bundle bundle = new Bundle();
                bundle.putInt(Lista._ID, id_lista);
                FragmentAgregarProductosLista fragmentoNewProd = new FragmentAgregarProductosLista();
                fragmentoNewProd.setArguments(bundle);


                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.contenedor, fragmentoNewProd);
                ft.commit();
                //Snackbar.make(view, "aca va la accion", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }
        });
    }

    private class ProductosDeListaAdapter extends BaseAdapter{

        private Context context;
        private List<ProductoPorLista> productoPorListas;
        private LayoutInflater inflater;

        public ProductosDeListaAdapter(Context context, List<ProductoPorLista> productoPorListas){
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
            ViewHolderProductoDeLista holder;

            if (view == null){
                holder = new ViewHolderProductoDeLista();
                view = inflater.inflate(R.layout.list_item_producto_de_lista, null);
                holder.producto = (TextView) view.findViewById(R.id.tvProductoDeLista);
                holder.checkBox = (CheckBox) view.findViewById(R.id.checkBoxProducto);
                view.setTag(holder);
            }else{
                holder = (ViewHolderProductoDeLista) view.getTag();
            }

            ProductoPorLista prod = productoPorListas.get(i);
            holder.producto.setText(prod.toString());

            return view;
        }
    }

    private class ViewHolderProductoDeLista{
        TextView producto;
        CheckBox checkBox;

    }
}
