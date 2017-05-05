package com.example.superlista;


import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.example.superlista.data.SuperListaDbManager;
import com.example.superlista.model.Categoria;
import com.example.superlista.model.Producto;
import com.example.superlista.model.Supermercado;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;



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


        }
    }

    public void onImageClickFotoProd(View view){

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



    private void setSpinnerCategoria(){

        nombresCategorias = new ArrayList<String>();

        listCategorias = SuperListaDbManager.getInstance().getAllCategorias();

        nombresCategorias.add(0, "-N/D-");

        for (Categoria listaCat: listCategorias) {

            nombresCategorias.add(listaCat.getNombre());
        }

        adapterCategoria = new ArrayAdapter(getContext(), android.R.layout.simple_dropdown_item_1line, nombresCategorias);
        adapterCategoria.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sCategoria.setAdapter(adapterCategoria);

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


    }

    private void acomodarArrayList (ArrayList<String> arrayList){

        //elimino elementos duplicados del ArrayList
        hs = new HashSet<String>();
        hs.addAll(arrayList);
        arrayList.clear();
        arrayList.add(0, "-N/D-");
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

        nombresSupers.add(0, "-N/D-");

        for (Supermercado listaSup: listSupers) {

            nombresSupers.add(listaSup.getNombre());
        }

        adapterSuper = new ArrayAdapter(getContext(), android.R.layout.simple_dropdown_item_1line, nombresSupers);
        adapterSuper.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sSupermercado.setAdapter(adapterSuper);

    }


}
