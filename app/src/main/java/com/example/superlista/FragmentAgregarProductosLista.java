package com.example.superlista;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.SparseBooleanArray;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.superlista.data.SuperListaDbManager;
import com.example.superlista.model.Categoria;
import com.example.superlista.model.Lista;
import com.example.superlista.model.Producto;
import com.example.superlista.model.ProductoPorLista;
import com.example.superlista.utils.ProductListAdapter;
import com.example.superlista.utils.ProductSearchAdapter;
import com.example.superlista.utils.ToolBarActionModeCallback;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class FragmentAgregarProductosLista extends Fragment implements TextView.OnEditorActionListener {

    private ListView listView1;

    private List<Producto> productos;
    private ProductSearchAdapter searchAdapter;
    private ProductListAdapter productListAdapter;
    private ImageView btnSpeak;
    private EditText etSearch;
    private final int REQ_CODE_SPEECH_OUTPUT = 143;
    private boolean isSearching;
    private int id_lista;
    private Lista lista;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_productos, container, false);
        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.boton_de_accion_productos);
        fab.setVisibility(View.INVISIBLE);
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
        String nombreLista = null;
        try {
            id_lista = getArguments().getInt(Lista._ID);
            lista = SuperListaDbManager.getInstance().getListaById(id_lista);
            nombreLista = lista.getNombre();
        } catch (Exception e){
            e.printStackTrace();
        }
        getActivity().setTitle("Agregar producto a: "+nombreLista);
    }

    //<editor-fold desc="Seteo de componentes">
    private void setData(){

        productos = SuperListaDbManager.getInstance().getAllProductosByNameDistinct();
        productListAdapter = new ProductListAdapter(getActivity(), productos);
        searchAdapter = new ProductSearchAdapter(getActivity());

        for(Producto producto : productos){
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

    private void implementsListViewClickListeners(){
        listView1.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                if (isSearching){
                    Producto prod = searchAdapter.getItem(position);
                    elegirCantidadProducto(prod);
                } else {
                    //long idProd = productListAdapter.getItemId(position);
                    Producto prod = productListAdapter.getItem(position);
                    elegirCantidadProducto(prod);
                }
            }
        });

    }

    private void elegirCantidadProducto(final Producto producto){

        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        final EditText cantidad = new EditText(getContext());
        //cantidad.setLines(1);
        // Sacar comentario cuando se cambie el tipo de dato de cantidad a double
        //cantidad.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        cantidad.setInputType(InputType.TYPE_CLASS_NUMBER);
        builder.setMessage("Cantidad en KG");//producto.getUnidad()
        builder.setTitle("Elije la cantidad");
        builder.setView(cantidad);
        builder.setPositiveButton("Agregar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try{
                    Producto prod = SuperListaDbManager.getInstance().getProductoByNombre(producto.getNombre(), producto.getMarca());
                    ProductoPorLista prodLista = new ProductoPorLista(prod, lista, Integer.parseInt(cantidad.getText().toString()));
                    SuperListaDbManager.getInstance().addProductoLista(prodLista);
                }catch (Exception e){e.printStackTrace();}
                Toast.makeText(getContext(), "Se ha agregado: "+producto, Toast.LENGTH_LONG).show();
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
