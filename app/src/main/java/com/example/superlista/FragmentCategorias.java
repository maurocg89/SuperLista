package com.example.superlista;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.superlista.data.SuperListaDbManager;
import com.example.superlista.model.Categoria;
import com.example.superlista.utils.CategoryListAdapter;
import com.example.superlista.utils.CategorySearchAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class FragmentCategorias extends Fragment implements TextView.OnEditorActionListener {

    Fragment fragmento = null;

    private ListView listView;
    private List<Categoria> categorias;
    private ArrayAdapter<Categoria> myAdapter;
    private CategoryListAdapter listAdapter;
    private CategorySearchAdapter searchAdapter;
    private ImageView btnSpeak;
    private EditText etSearch;
    private boolean isSearching;
    private final int REQ_CODE_SPEECH_OUTPUT = 143;
    private int cod_categoria;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_categorias, container, false);

        llamarFloatingButtonAction(view);

        listView = (ListView) view.findViewById(R.id.lvCategorias);
        btnSpeak = (ImageView) view.findViewById(R.id.imgBtnSpeakCat);
        etSearch = (EditText) view.findViewById(R.id.etBuscarCategoria);

        setData();
        implementsListViewClickListeners();
        inicializarComponentes();

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle(R.string.item_categorias);
    }

    private void setData(){
        categorias = SuperListaDbManager.getInstance().getAllCategorias();
        listAdapter = new CategoryListAdapter(getActivity(), categorias);
        searchAdapter = new CategorySearchAdapter(getActivity());

        for(Categoria categoria : categorias){
            searchAdapter.addItem(categoria);
        }

        listView.setAdapter(listAdapter);
        listAdapter.notifyDataSetChanged();
    }

    private void implementsListViewClickListeners(){
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                if (isSearching) {
                    cod_categoria = searchAdapter.getItem(position).getId_categoria();
                } else {
                    cod_categoria = listAdapter.getItem(position).getId_categoria();
                }

                Bundle bundle = new Bundle();
                bundle.putInt(Categoria._ID, cod_categoria);

                FragmentProductos fragmentProductos = new FragmentProductos();
                fragmentProductos.setArguments(bundle);
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.contenedor, fragmentProductos);
                ft.commit();
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {

                return false;

            }
        });
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


    private void setSearchResult(String str){
        searchAdapter = new CategorySearchAdapter(getActivity());

        for (Categoria tmp: categorias){
            if (tmp.toString().toLowerCase().contains(str.toLowerCase())){
                searchAdapter.addItem(tmp);
            }
        }
        listView.setAdapter(searchAdapter);
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




    private void llamarFloatingButtonAction(View vista){

        FloatingActionButton fab = (FloatingActionButton) vista.findViewById(R.id.boton_de_accion_categorias);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                fragmento = new FragmentAgregarCategoria();
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.contenedor, fragmento);
                ft.commit();

            }
        });


    }





}


