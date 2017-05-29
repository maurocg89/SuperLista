package com.example.superlista;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
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

public class FragmentProductosDeLista extends Fragment {

    private ListView listView;
    private List<ProductoPorLista> productosPorLista;
    private ArrayList<ProductoPorLista> productosPorSuper;
    private ArrayList<ProductoPorLista> productosFaltantes;
    private ProductosDeListaAdapter myAdapter;

    private String nombreLista;
    private int id_lista;
    private ImageView btnCoto;
    private ImageView btnLaGallega;
    private ImageView btnCarrefour;
    private FragmentPrecioListaSuper fragmentListaPorSuper;

    private MenuItem mEditItem;

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
        listViewListeners();
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
        mEditItem = menu.findItem(R.id.action_editar_producto);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_eliminar_producto: eliminarProducto(item); return true;
            case R.id.action_editar_producto: modificarCantidadProducto(item); return true;
            default: return super.onOptionsItemSelected(item);
        }
    }
    
    private void eliminarProducto(MenuItem item){

        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage("Desea eliminar el/los producto(s) seleccionado(s)?");
        builder.setTitle("Eliminar");

        builder.setPositiveButton("Eliminar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try{
                    SparseBooleanArray array = listView.getCheckedItemPositions();
                    ArrayList<ProductoPorLista> seleccion = new ArrayList<>();
                    for (int i = 0; i < array.size(); i++){
                        // Posicion del contacto en el adaptador
                        int pos = array.keyAt(i);
                        if(array.valueAt(i)) {
                            seleccion.add(myAdapter.getItem(pos));
                        }
                    }
                    SuperListaDbManager.getInstance().deleteProductosDeLista(seleccion);
                    productosPorLista.removeAll(seleccion);
                    myAdapter.notifyDataSetChanged();
                    listView.clearChoices();
                    mEditItem.setVisible(false);
                }catch (Exception e){
                    e.printStackTrace();
                }
                dialog.dismiss();

            }

        });

        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });

        builder.show();

    }

    private void modificarCantidadProducto(MenuItem item){
        SparseBooleanArray array = listView.getCheckedItemPositions();
        ArrayList<ProductoPorLista> seleccion = new ArrayList<>();
        for (int i = 0; i < array.size(); i++){
            // Posicion del contacto en el adaptador
            int pos = array.keyAt(i);
            if(array.valueAt(i)) {
                seleccion.add(myAdapter.getItem(pos));
            }
        }
        final ProductoPorLista productoPorLista = seleccion.get(0);
        //String unidad = SuperListaDbManager.getInstance().getProductoByNombre(productoPorLista.getProducto().getNombre(),
        //       productoPorLista.getProducto().getMarca()).getUnidad();
        String unidad = productoPorLista.getProducto().getUnidad();

        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        final EditText cantidad = new EditText(getContext());
        //cantidad.setLines(1);

        cantidad.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);

        builder.setMessage("Cantidad en "+unidad);//producto.getUnidad()
        builder.setTitle("Cambiar cantidad del producto: "+productoPorLista.getProducto());
        builder.setView(cantidad);

        builder.setPositiveButton("Cambiar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try{
                    SuperListaDbManager.getInstance().updateCantidadProductoLista(productoPorLista,
                            Double.parseDouble(cantidad.getText().toString()));
                }catch (Exception e){
                    e.printStackTrace();
                }
                dialog.dismiss();
                myAdapter.notifyDataSetChanged();
                listView.clearChoices();
                mEditItem.setVisible(false);
            }

        });

        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
                /*myAdapter.notifyDataSetChanged();
                listView.clearChoices();
                mEditItem.setVisible(false);*/
            }
        });

        builder.show();

    }

    private void listViewListeners(){
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                int count = listView.getCheckedItemCount();
                if (count == 1){
                    mEditItem.setVisible(true);
                }
                else {
                    mEditItem.setVisible(false);
                }
            }
        });
    }

    private void setData(){
        try {
            id_lista = getArguments().getInt(Lista._ID);
        }catch (Exception e){
            e.printStackTrace();
        }

        if (id_lista != 0){
            // Borro los productos que están en null
            SuperListaDbManager.getInstance().deleteProductosDeListas();
            productosPorLista = SuperListaDbManager.getInstance().getAllProductosDeLista(id_lista);
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
            double cantidad = prod.getCantidad();

            if (id_super == Supermercado.ID_COTO){
                if (prod.getProducto().getPrecio_coto() > 0){
                    total += prod.getProducto().getPrecio_coto() * cantidad;
                    productosPorSuper.add(prod);
                } else {
                    productosFaltantes.add(prod);
                }
            }
            else if (id_super == Supermercado.ID_LA_GALLEGA){
                if (prod.getProducto().getPrecio_la_gallega() > 0){
                    total += prod.getProducto().getPrecio_la_gallega() * cantidad;
                    productosPorSuper.add(prod);
                } else {
                    productosFaltantes.add(prod);
                }
            }
            else if (id_super == Supermercado.ID_CARREFOUR){
                if (prod.getProducto().getPrecio_carrefour() > 0){
                    total += prod.getProducto().getPrecio_carrefour() * cantidad;
                    productosPorSuper.add(prod);
                } else {
                    productosFaltantes.add(prod);
                }
            }
            else if (id_super == Supermercado.ID_OTRO){
                if (prod.getProducto().getPrecio_otro() > 0){
                    total += prod.getProducto().getPrecio_otro() * cantidad;
                    productosPorSuper.add(prod);
                } else {
                    productosFaltantes.add(prod);
                }
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
                bundle.putInt(Supermercado._ID,  Supermercado.ID_LA_GALLEGA);
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

                bundle.putInt(Supermercado._ID,  Supermercado.ID_COTO);
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

                bundle.putInt(Supermercado._ID, Supermercado.ID_CARREFOUR);
                bundle.putDouble("Total", getTotal(Supermercado.ID_CARREFOUR));
                bundle.putParcelableArrayList("Productos", productosPorSuper);
                bundle.putParcelableArrayList("Productos Faltantes", productosFaltantes);
                fragmentListaPorSuper.setArguments(bundle);

                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.contenedor, fragmentListaPorSuper);
                ft.commit();
            }
        });
        // TODO: 24/05/2017 Agregar super otro
    }

    private void llamarFloatingButtonAction(View vista) {

        FloatingActionButton fab = (FloatingActionButton) vista.findViewById(R.id.boton_agregar_productos_listas);
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
            }
        });
    }

    private class ProductosDeListaAdapter extends BaseAdapter{

        private Context context;
        private List<ProductoPorLista> productoPorListas;
        private LayoutInflater inflater;
        private SparseBooleanArray mSelectedItemsIds;

        public ProductosDeListaAdapter(Context context, List<ProductoPorLista> productoPorListas){
            this.context = context;
            this.productoPorListas = productoPorListas;
            mSelectedItemsIds = new SparseBooleanArray();
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

     /*   public void toggleSelection(int position){
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
        }*/
    }

    private class ViewHolderProductoDeLista{
        TextView producto;
        CheckBox checkBox;

    }
}
