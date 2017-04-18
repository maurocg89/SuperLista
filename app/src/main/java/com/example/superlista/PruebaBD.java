package com.example.superlista;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.Layout;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.superlista.data.SuperListaDbManager;
import com.example.superlista.model.Categoria;
import com.example.superlista.model.Lista;
import com.example.superlista.model.Producto;
import com.example.superlista.model.ProductoPorLista;
import com.example.superlista.model.Supermercado;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class PruebaBD extends AppCompatActivity implements TextView.OnEditorActionListener{

    private ListView listView1;

    private List<Producto> productos;
    private List<Lista> listas;
    private List<Categoria> categorias;
    private List<ProductoPorLista> productoPorListas;
    private List<Supermercado> supermercados;
    private ArrayList<String>nombres;
    private ArrayAdapter<String> myAdapter;

    private SearchAdapter searchAdapter;

    private Button btnSpeak;
    private EditText etSearch;
    private final int REQ_CODE_SPEECH_OUTPUT = 143;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prueba_bd);

        SuperListaDbManager.init(this);

        listView1 = (ListView) findViewById(R.id.lvLista);
        btnSpeak = (Button) findViewById(R.id.btnSpeak);
        etSearch = (EditText) findViewById(R.id.etTextHint);
        searchAdapter = new SearchAdapter(this);

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
    }

    @Override
    protected void onResume() {
        super.onResume();
        setData();
    }



    private void setData(){
        searchAdapter = new SearchAdapter(this);
        listas = SuperListaDbManager.getInstance().getAllListas();
        productos = SuperListaDbManager.getInstance().getAllProductos();
        //productos = SuperListaDbManager.getInstance().getAllProductosByNameDistinct();
        categorias = SuperListaDbManager.getInstance().getAllCategorias();
        supermercados = SuperListaDbManager.getInstance().getAllSupermercados();
        nombres = new ArrayList<String>();

        /*for (Lista lista: listas) {
            nombres.add(lista.getNombre());
            searchAdapter.addItem(lista.getNombre());
        }*/

        for(Producto producto : productos){
            nombres.add(producto.getNombre());
            searchAdapter.addItem(producto.getNombre());
        }

        //myAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, nombres);
        listView1.setAdapter(searchAdapter);

    }

    private void setSearchResult(String str){
        searchAdapter = new SearchAdapter(this);
        for (String tmp: nombres){
            if (tmp.toLowerCase().contains(str.toLowerCase())){
                searchAdapter.addItem(tmp);
            }
        }
        listView1.setAdapter(searchAdapter);
    }

    private void checkVoiceRecognition(){
        PackageManager pm = getPackageManager();
        List<ResolveInfo> activities = pm.queryIntentActivities(new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH),0);
        if (activities.size() == 0){
            btnSpeak.setEnabled(false);
            Toast.makeText(this, "Reconocimiento de voz no está disponible", Toast.LENGTH_LONG).show();
        }
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case REQ_CODE_SPEECH_OUTPUT:
                if (resultCode == RESULT_OK && data != null){
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

    // TODO: Agregar checkbox a los item lists
    public class SearchAdapter extends BaseAdapter {

        private ArrayList<String> data = new ArrayList<String>();
        private LayoutInflater inflater;

        public SearchAdapter(Activity activity) {
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
            ViewHolder holder = null;
            if (convertView == null){
                holder = new ViewHolder();
                convertView = inflater.inflate(R.layout.list_item, null);
                holder.textView = (TextView) convertView.findViewById(R.id.list_item_texto);
                convertView.setTag(holder);
            }
            else {
                holder = (ViewHolder) convertView.getTag();
            }
            String str = data.get(position);
            holder.textView.setText(str);
            return convertView;
        }
    }

    public class ViewHolder{
        public TextView textView;
    }
}
