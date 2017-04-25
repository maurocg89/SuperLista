package com.example.superlista;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.SparseBooleanArray;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.superlista.utils.ProductSearchAdapter;
import com.example.superlista.data.SuperListaDbManager;
import com.example.superlista.model.Categoria;
import com.example.superlista.model.Producto;


import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

// TODO: Fijarse porque no filtra bien

public class FragmentProductos extends Fragment implements TextView.OnEditorActionListener {

    private ListView listView1;

    private List<Producto> productos;
    private ArrayList<String> nombres;
    private ArrayAdapter<String> myAdapter;
    private ProductSearchAdapter searchAdapter;
    private ImageView btnSpeak;
    private EditText etSearch;
    private int cod_categoria;
    private final int REQ_CODE_SPEECH_OUTPUT = 143;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_productos, container, false);

        listView1 = (ListView) view.findViewById(R.id.lvProductos);
        btnSpeak = (ImageView) view.findViewById(R.id.imgBtnSpeak);
        etSearch = (EditText) view.findViewById(R.id.etBuscar);
        searchAdapter = new ProductSearchAdapter(getActivity());
        setData();

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
                    String spnId = etSearch.getText().toString();
                    setSearchResult(spnId);
                }
                else {
                    setData();
                }
            }
        });

        return view;

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle(R.string.item_productos);
    }

    private void setData(){

        /*try{
            cod_categoria = getActivity().getIntent().getExtras().getInt(Categoria._ID);
        } catch (Exception e){
            e.printStackTrace();
        }
        if (cod_categoria == 0){
            productos = SuperListaDbManager.getInstance().getAllProductosByNameDistinct();
        } else {
            productos = SuperListaDbManager.getInstance().getProductoByCategoriaDistinct(cod_categoria);
        }*/

        productos = SuperListaDbManager.getInstance().getAllProductosByNameDistinct();
        searchAdapter = new ProductSearchAdapter(getActivity());
        nombres = new ArrayList<>();

        for(Producto producto : productos){
            nombres.add(producto.getNombre() + " " + producto.getMarca());
            searchAdapter.addItem(producto.getNombre() + " " + producto.getMarca());
        }

        //myAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, nombres);
        //listView1.setAdapter(myAdapter);
        listView1.setAdapter(searchAdapter);


    }

    private void setSearchResult(String str){
        searchAdapter = new ProductSearchAdapter(getActivity());
        for (String tmp: nombres){
            if (tmp.toLowerCase().contains(str.toLowerCase())){
                searchAdapter.addItem(tmp);
            }
        }
        listView1.setAdapter(searchAdapter);
    }

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

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_productos, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_eliminar_producto: return true; // metodo eliminar producto
            default: return super.onOptionsItemSelected(item);
        }
    }

    private void eliminarProducto(MenuItem item){
        SparseBooleanArray array = listView1.getCheckedItemPositions();
        ArrayList<String> seleccion = new ArrayList<>();
        for (int i = 0; i < array.size(); i++){
            // Posicion del producto en el adaptador
            int posicion = array.keyAt(i);
            if (array.valueAt(i)){
                seleccion.add(myAdapter.getItem(posicion));
            }

            //Intent intent = new Intent()
        }
    }


}
