package com.example.superlista;


import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.superlista.data.SuperListaDbManager;
import com.example.superlista.model.Categoria;
import com.example.superlista.model.Producto;
import com.example.superlista.model.Supermercado;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import static android.app.Activity.RESULT_OK;


public class FragmentAgregarProducto extends Fragment implements View.OnClickListener {


    private List<Categoria> listCategorias;
    private List<Producto> listProductos;
    private List<Supermercado> listSupers;

    private ArrayList<String> nombresCategorias, nombresMarcas, nombresSupers;

    private ArrayAdapter adapterCategoria;
    private ArrayAdapter<String> adapterMarca;
    private ArrayAdapter<String> adapterSuper ;

    HashSet<String> hs;

    private int request_code = 1;
    private int numberIDcat, numberIDsup;
    private String cadCategoria, cadMarca, cadSuper;

    private EditText nomProd, valorPrecio;
    private Button agregarProducto;
    private Spinner sCategoria, sMarca, sSupermercado;
    private ImageView imageProd;



    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.form_nuevo_producto, container, false);

        iniciarIU(view);  //metodo que relaciona la parte logica con la grafica



        return view;
    }


    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle(R.string.nueva_producto);
    }



    private void iniciarIU(View vista){

        nomProd =  (EditText) vista.findViewById(R.id.editTextNombreProd);
        valorPrecio =  (EditText) vista.findViewById(R.id.editTextValorPrecio);

        imageProd = (ImageView) vista.findViewById(R.id.imageViewFotoProd);

        agregarProducto = (Button) vista.findViewById(R.id.buttonAgregarProd);
        agregarProducto.setOnClickListener(this);

        sCategoria = (Spinner) vista.findViewById(R.id.spinnerCategoria);
        sMarca = (Spinner) vista.findViewById(R.id.spinnerMarca);
        sSupermercado = (Spinner) vista.findViewById(R.id.spinnerSuper);

        setSpinnerCategoria();
        setSpinnerMarca();
        setSpinnerSupermercado();


    }

    @Override
    public void onClick(View v) {

        if (v == agregarProducto){

            add_Producto();

            //limpiarCampos();



        }else if(v == imageProd){

            Intent intent = null;

            if(Build.VERSION.SDK_INT < 19){ // verificacion para version de android 4.3 a anterior
                intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);

            }else{
                intent =  new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
            }

            intent.setType("image/*");
            startActivityForResult(intent, request_code);
        }
    }

    public void add_Producto(){ //TODO: verificar como le paso la categoria y el supermercado

        Categoria category = new Categoria();
        category.setId_categoria(numberIDcat);
        Supermercado market = new Supermercado();
        market.setId_supermercado(numberIDsup);

        Producto nuevoProd = new Producto(
                nomProd.getText().toString(),
                cadMarca,
                Double.parseDouble(valorPrecio.getText().toString()),
                category,
                market,
                (String) imageProd.getTag()
        );
        SuperListaDbManager.getInstance().addProducto(nuevoProd);

    }

    public void limpiarCampos(){

        nomProd.getText().clear();
        valorPrecio.getText().clear();
        imageProd.setImageResource(R.drawable.ic_local_grocery_store_black_24dp);
        setSpinnerMarca();
        setSpinnerCategoria();
        setSpinnerSupermercado();

    }






    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(resultCode == RESULT_OK && requestCode == request_code){

            imageProd.setImageURI(data.getData());
            //Utilizamos el atributo TAG para almacenar la uri al acrchivo seleccionado
            imageProd.setTag(data.getData());
        }

    }

    private void setSpinnerCategoria(){

        nombresCategorias = new ArrayList<String>();

        listCategorias = SuperListaDbManager.getInstance().getAllCategorias();

        for (Categoria listaCat: listCategorias) {

            nombresCategorias.add(listaCat.getNombre());
        }
        Collections.sort(nombresCategorias);

        adapterCategoria = new ArrayAdapter(getContext(), android.R.layout.simple_dropdown_item_1line, nombresCategorias);
        adapterCategoria.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sCategoria.setAdapter(adapterCategoria);

        sCategoria.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                cadCategoria = String.valueOf(sCategoria.getSelectedItem());

                for (Categoria listaCat: listCategorias) {
                    if (listaCat.getNombre().equals(cadCategoria)){
                        numberIDcat =  listaCat.getId_categoria();
                        break;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }



    private void setSpinnerMarca(){

        nombresMarcas = new ArrayList<String>();

        listProductos =  SuperListaDbManager.getInstance().getAllProductos();

        for (Producto listaMar: listProductos) {

            nombresMarcas.add(listaMar.getMarca());
        }

        acomodarArrayList(nombresMarcas);

        adapterMarca = new ArrayAdapter(getContext(), android.R.layout.simple_dropdown_item_1line, nombresMarcas);
        adapterMarca.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sMarca.setAdapter(adapterMarca);


        sMarca.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                cadMarca = String.valueOf(sMarca.getSelectedItem());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    private void acomodarArrayList (ArrayList<String> arrayList){

        //elimino elementos duplicados del ArrayList
        hs = new HashSet<String>();
        hs.addAll(arrayList);
        arrayList.clear();
        arrayList.add(0, "-No Especificado-");
        arrayList.addAll(hs);

        //ordeno alfabeticamente los elementos del ArrayList
        Collections.sort(arrayList);

        //elimino campos vacios del ArrayList
        for (int i = 0; i < arrayList.size(); i++) {

            if(arrayList.get(i).trim().length() == 0){
                arrayList.remove(i);
            }
        }
    }

    private void setSpinnerSupermercado(){

        nombresSupers = new ArrayList<String>();

        listSupers = SuperListaDbManager.getInstance().getAllSupermercados();

        for (Supermercado listaSup: listSupers) {

            nombresSupers.add(listaSup.getNombre());
        }
        Collections.sort(nombresSupers);

        adapterSuper = new ArrayAdapter(getContext(), android.R.layout.simple_dropdown_item_1line, nombresSupers);
        adapterSuper.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sSupermercado.setAdapter(adapterSuper);

        sSupermercado.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                cadSuper = String.valueOf(sSupermercado.getSelectedItem());

                for (Supermercado listaSup: listSupers) {
                    if (listaSup.getNombre().equals(cadSuper)){
                        numberIDsup =  listaSup.getId_supermercado();
                        break;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }


}
