package com.example.superlista;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.Toolbar;
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
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView;
import android.widget.AbsListView.MultiChoiceModeListener;

import com.example.superlista.utils.ProductSearchAdapter;
import com.example.superlista.data.SuperListaDbManager;
import com.example.superlista.model.Categoria;
import com.example.superlista.model.Producto;
import com.example.superlista.utils.ToolBarActionModeCallback;


import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class FragmentProductos extends Fragment implements TextView.OnEditorActionListener {

    private ListView listView1;

    private static ActionMode mActionMode;
    private List<Producto> productos;
    private List<Producto> listaProductos = new ArrayList<>();
    private ProductSearchAdapter searchAdapter;
    private ProductSearchAdapter adapter;
    private ImageView btnSpeak;
    private EditText etSearch;
    private int cod_categoria;
    private final int REQ_CODE_SPEECH_OUTPUT = 143;

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

        return view;

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle(R.string.item_productos);
    }


    //<editor-fold desc="Android Contextual Action Mode">
    private void implementsListViewClickListeners(){
        listView1.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                if (mActionMode != null){
                    onListItemSelect(position);
                }
            }
        });

        listView1.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
               // mActionMode = ((AppCompatActivity)getActivity()).startSupportActionMode(new ToolBarActionModeCallback(getActivity(), searchAdapter, productos));
                onListItemSelect(position);
                return true;

            }
        });
    }

    private void onListItemSelect(int position){
        searchAdapter.toggleSelection(position);
        boolean hasCheckedItems = searchAdapter.getSelectedCount() > 0; // Se fija si hay algun item seleccionado
        if (hasCheckedItems && mActionMode == null){
            mActionMode = ((AppCompatActivity) getActivity()).startSupportActionMode(new ToolBarActionModeCallback(getActivity(), searchAdapter, productos));
        } else if (!hasCheckedItems && mActionMode != null){
            // no hay ningun item seleccionado, termino el action mode
            mActionMode.finish();
            setNullToActionMode();

        }
        // Pongo la cantidad de items seleccionados
        if (mActionMode != null){
            mActionMode.setTitle(String.valueOf(searchAdapter.getSelectedCount()) + " seleccionado(s)");

        }

    }

    // Seteo en null el action mode despues de usarlo
    public void setNullToActionMode(){
        if (mActionMode != null){
            mActionMode = null;
        }
    }

    // Falta implementacion para borrar productos de la base de datos
    public void deleteRows(){
        SparseBooleanArray seleceted = searchAdapter.getSelectedIds();
        for (int i = (seleceted.size() - 1); i >= 0; i--){
            if (seleceted.valueAt(i)){
                productos.remove(seleceted.keyAt(i));
                searchAdapter.notifyDataSetChanged();
            }
        }

        Toast.makeText(getActivity(), seleceted.size() + " productos eliminados", Toast.LENGTH_LONG).show();
        mActionMode.finish();
    }
    //</editor-fold>

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
        searchAdapter = new ProductSearchAdapter(getActivity(), productos);
     //   searchAdapter = new ProductSearchAdapter(getActivity());


    /*    for(Producto producto : productos){
            searchAdapter.addItem(producto);
        }
*/
        listView1.setAdapter(searchAdapter);
        searchAdapter.notifyDataSetChanged();

    }


    private void setSearchResult(String str){
        searchAdapter = new ProductSearchAdapter(getActivity(), productos);
      //  searchAdapter = new ProductSearchAdapter(getActivity());

      /*  for (Producto tmp: productos){
            if (tmp.toString().toLowerCase().contains(str.toLowerCase())){
                searchAdapter.addItem(tmp);
            }
        }*/
        listView1.setAdapter(searchAdapter);
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




    private void llamarFloatingButtonAction(View vista) {

        FloatingActionButton fab = (FloatingActionButton) vista.findViewById(R.id.boton_de_accion_productos);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                fragmentoNewProd = new FragmentAgregarProducto();
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.contenedor, fragmentoNewProd);
                ft.commit();
                //Snackbar.make(view, "aca va la accion", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }
        });
    }



    /*private void eliminarProducto(MenuItem item){
        SparseBooleanArray array = listView1.getCheckedItemPositions();
        List seleccion = new List();
        for (int i = 0; i < array.size(); i++){
            // Posicion del producto en el adaptador
            int posicion = array.keyAt(i);
            if (array.valueAt(i)){
                seleccion.add(searchAdapter.getItem(posicion));
            }

            //Intent intent = new Intent()
        }
    }*/


}
