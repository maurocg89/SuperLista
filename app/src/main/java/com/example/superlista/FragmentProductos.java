package com.example.superlista;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.superlista.data.SuperListaDbManager;
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
    private SearchAdapterProducto searchAdapter;
    private ImageView btnSpeak;
    private EditText etSearch;
    private final int REQ_CODE_SPEECH_OUTPUT = 143;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_productos, container, false);

        listView1 = (ListView) view.findViewById(R.id.lvProductos);
        btnSpeak = (ImageView) view.findViewById(R.id.imgBtnSpeak);
        etSearch = (EditText) view.findViewById(R.id.etBuscar);
        searchAdapter = new SearchAdapterProducto(getActivity());
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
        searchAdapter = new SearchAdapterProducto(getActivity());
        productos = SuperListaDbManager.getInstance().getAllProductosByNameDistinct();
        nombres = new ArrayList<String>();

        for(Producto producto : productos){
            nombres.add(producto.getNombre() + " " + producto.getMarca());
            searchAdapter.addItem(producto.getNombre() + " " + producto.getMarca());
        }

        //myAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, nombres);
        //listView1.setAdapter(myAdapter);
        listView1.setAdapter(searchAdapter);


    }

    private void setSearchResult(String str){
        searchAdapter = new SearchAdapterProducto(getActivity());
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

    public class SearchAdapterProducto extends BaseAdapter{

        private ArrayList<String> data = new ArrayList<String>();
        private LayoutInflater inflater;

        public SearchAdapterProducto(Activity activity) {
            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        public void addItem(String item){
            data.add(item);
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public String getItem(int position) {
            return data.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolderProducto holder;
            if (convertView == null){
                holder = new ViewHolderProducto();
                convertView = inflater.inflate(R.layout.list_item, null);
                holder.textView = (TextView) convertView.findViewById(R.id.list_item_texto);
                convertView.setTag(holder);
            }
            else {
                holder = (ViewHolderProducto) convertView.getTag();
            }
            String str = data.get(position);
            holder.textView.setText(str);
            return convertView;
        }
    }

    public class ViewHolderProducto{
        TextView textView;
       // CheckBox checkBox;
    }
}
