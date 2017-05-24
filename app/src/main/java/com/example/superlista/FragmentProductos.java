package com.example.superlista;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Debug;
import android.speech.RecognizerIntent;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.SparseBooleanArray;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView;

import com.example.superlista.model.Categoria;
import com.example.superlista.model.ProductoPorLista;
import com.example.superlista.utils.ProductListAdapter;
import com.example.superlista.data.SuperListaDbManager;
import com.example.superlista.model.Producto;
import com.example.superlista.utils.ProductSearchAdapter;
import com.example.superlista.utils.ToolBarActionModeCallback;


import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class FragmentProductos extends Fragment implements TextView.OnEditorActionListener {

    private ListView listView1;

    private static ActionMode mActionMode;
    private List<Producto> productos;
    private ProductSearchAdapter searchAdapter;
    private ProductListAdapter productListAdapter;
    private ImageView btnSpeak;
    private EditText etSearch;
    private int cod_categoria = 0;
    private final int REQ_CODE_SPEECH_OUTPUT = 143;
    private boolean isSearching;

    Fragment fragmentoNewProd = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_productos, container, false);

        llamarFloatingButtonAction(view);
        listView1 = (ListView) view.findViewById(R.id.lvProductos);
        btnSpeak = (ImageView) view.findViewById(R.id.imgBtnSpeak);
        etSearch = (EditText) view.findViewById(R.id.etBuscar);
        setData();

        implementsListViewClickListeners();
        inicializarComponentes();


        return view;

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle(R.string.item_productos);
        if (cod_categoria != 0){
            Categoria categoria = SuperListaDbManager.getInstance().getCategoriaById(cod_categoria);
            getActivity().setTitle("Productos de " + categoria.getNombre());
        }
    }

    //<editor-fold desc="Seteo de componentes">
    private void setData(){
        try{
            cod_categoria = getArguments().getInt(Categoria._ID, 0);
        } catch (Exception e){
            e.printStackTrace();
        }
        if (cod_categoria != 0) {
            productos = SuperListaDbManager.getInstance().getProductoByCategoriaDistinct(cod_categoria);
        }
        else{
            productos = SuperListaDbManager.getInstance().getAllProductosByNameDistinct();
        }

        productListAdapter = new ProductListAdapter(getActivity(), productos);
        searchAdapter = new ProductSearchAdapter(getActivity());

        for(Producto producto : productos){
//            System.out.println(producto.getId_producto());
            searchAdapter.addItem(producto);
        }

        productListAdapter.notifyDataSetChanged();
        listView1.setAdapter(productListAdapter);


    }

    private void inicializarComponentes(){
        btnSpeak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnOpenMic();
            }
        });

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (mActionMode != null){
                    mActionMode.finish();
                }
                if (etSearch.getText().length() != 0){
                    isSearching = true;
                    String spnId = etSearch.getText().toString();
                    setSearchResult(spnId);
                }
                else {
                    isSearching = false;
                    setData();
                    implementsListViewClickListeners();
                }
            }
        });

        // Esconder el teclado una vez que se busca
        etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                boolean action = false;
                if (actionId == EditorInfo.IME_ACTION_SEARCH){
                    InputMethodManager inputMethodManager = (InputMethodManager) textView.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(textView.getWindowToken(), 0);
                    action = true;
                }

                return action;
            }
        });
    }
    //</editor-fold>

    private void setSearchResult(String str){
        searchAdapter = new ProductSearchAdapter(getActivity());
      //  searchAdapter = new ProductListAdapter(getActivity());

        for (Producto tmp: productos){
            if (tmp.toString().toLowerCase().contains(str.toLowerCase())){
                searchAdapter.addItem(tmp);
            }
        }
        listView1.setAdapter(searchAdapter);
    }

    private void llamarFloatingButtonAction(View vista) {

        FloatingActionButton fab = (FloatingActionButton) vista.findViewById(R.id.boton_de_accion_productos);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                fragmentoNewProd = new FragmentAgregarProducto();
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.contenedor, fragmentoNewProd);
                ft.commit();

            }
        });
    }

    //<editor-fold desc="Android Contextual Action Mode">
    private void implementsListViewClickListeners(){
        listView1.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                // Si se esta buscando un producto se desactiva el actionmode
              /*  if (isSearching && mActionMode != null){
                    //mActionMode.finish();
                    return;
                }*/
                if (mActionMode != null){
                    onListItemSelect(position);
                } else{
                    FragmentProducto fragmentProducto = new FragmentProducto();
                    Bundle bundle = new Bundle();
                    Producto prod = null;
                    if (isSearching){
                        prod = searchAdapter.getItem(position);
                        prod = SuperListaDbManager.getInstance().getProductoByNombre(prod.getNombre(), prod.getMarca());
                    } else {
                        prod = productListAdapter.getItem(position);
                        prod = SuperListaDbManager.getInstance().getProductoByNombre(prod.getNombre(), prod.getMarca());
                    }
                    bundle.putParcelable("producto", prod);
                    fragmentProducto.setArguments(bundle);
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.replace(R.id.contenedor, fragmentProducto);
                    ft.commit();
                }
            }
        });

        listView1.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
               if(isSearching){return false;}
                onListItemSelect(position);
                return true;

            }
        });
    }

    private void onListItemSelect(int position){
        productListAdapter.toggleSelection(position);
        boolean hasCheckedItems = productListAdapter.getSelectedCount() > 0; // Se fija si hay algun item seleccionado
        if (hasCheckedItems && mActionMode == null){
            mActionMode = ((AppCompatActivity) getActivity()).startSupportActionMode(new ToolBarActionModeCallback(getActivity(), productListAdapter, productos, this));
        } else if (!hasCheckedItems && mActionMode != null){
            // no hay ningun item seleccionado, termino el action mode
            mActionMode.finish();
            setNullToActionMode();

        }
        // Pongo la cantidad de items seleccionados
        if (mActionMode != null){
            mActionMode.setTitle(String.valueOf(productListAdapter.getSelectedCount()) + " seleccionado(s)");

        }

    }

    // Seteo en null el action mode despues de usarlo
    public void setNullToActionMode(){
        if (mActionMode != null){
            mActionMode = null;
        }
    }

    public void eliminarProducto(MenuItem item, Context context){

        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("Desea eliminar el/los producto(s) seleccionado(s)?");
        builder.setTitle("Eliminar");

        builder.setPositiveButton("Eliminar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try{
                    SparseBooleanArray array = productListAdapter.getSelectedIds();
                    ArrayList<Producto> seleccion = new ArrayList<>();
                    List<ProductoPorLista> productosPorLista = SuperListaDbManager.getInstance().getAllProductosListas();
                    for (int i = 0; i < array.size(); i++){
                        // Posicion del contacto en el adaptador
                        int pos = array.keyAt(i);
                        if(array.valueAt(i)) {
                            seleccion.add(productListAdapter.getItem(pos));
                        }
                    }
                    SuperListaDbManager.getInstance().deleteProductosByNombre(seleccion);
                    productos.removeAll(seleccion);
                    productListAdapter.notifyDataSetChanged();
                    mActionMode.finish();
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
    //</editor-fold>

    //<editor-fold desc="Voice to text">
    private void btnOpenMic(){
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Hable ahora...");


        try{
            startActivityForResult(intent, REQ_CODE_SPEECH_OUTPUT);
        } catch (ActivityNotFoundException anfe){
            anfe.printStackTrace();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case REQ_CODE_SPEECH_OUTPUT:
                if (resultCode == Activity.RESULT_OK && data != null){
                    ArrayList<String> voiceInText = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    etSearch.setText(voiceInText.get(0));
                }
                break;
        }
    }

    @Override
    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
        return false;
    }
    //</editor-fold>


}
