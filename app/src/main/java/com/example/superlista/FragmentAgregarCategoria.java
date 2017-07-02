package com.example.superlista;



import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.superlista.data.SuperListaDbManager;
import com.example.superlista.model.Categoria;

import java.util.List;
import java.util.Objects;


public class FragmentAgregarCategoria extends Fragment implements View.OnClickListener {

    Fragment fragmento = null;

    private List<Categoria> listadoDeCategorias;
    private LinearLayout linearLayoutAgregarCategoria;
    private EditText nombreCategoria, descripcionCategoria;
    private Button botonAgregar;
    private String dato, dato2, auxiliar;
    private boolean validacion = true;



    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.form_agregar_categoria, container, false);

        linearLayoutAgregarCategoria = (LinearLayout) view.findViewById(R.id.linearLayoutAgregarCat);
        linearLayoutAgregarCategoria.setOnClickListener(this);

        nombreCategoria = (EditText) view.findViewById(R.id.editTextNombreFormCat);
        descripcionCategoria = (EditText) view.findViewById(R.id.editTextDescrCat);

        botonAgregar = (Button) view.findViewById(R.id.buttonAgregar);
        botonAgregar.setOnClickListener(this);



        return view;
    }


    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle(R.string.nueva_categoria);
    }




    @Override
    public void onClick(View v) {

        if (v == botonAgregar){

           bajarTeclado();
           dato = nombreCategoria.getText().toString();
           dato2 = descripcionCategoria.getText().toString();
           auxiliar= dato.trim();

           if (auxiliar.length() > 0){ //verificacion para que el campo nombre no sea vacio
                check_add_Categoria(dato, dato2);
            }else{
                Toast.makeText(getContext(), "Coloque un Nombre a la Categoria", Toast.LENGTH_SHORT).show();
            }

        }else if (v == linearLayoutAgregarCategoria){
            bajarTeclado();
        }

    }

    private void bajarTeclado(){

        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(nombreCategoria.getWindowToken (), 0);
        imm.hideSoftInputFromWindow(descripcionCategoria.getWindowToken (), 0);
        Log.i("Bajamos el teclado", "ahora");

    }

    public void check_add_Categoria(String nombreCat, String descrCat){

        listadoDeCategorias = SuperListaDbManager.getInstance().getAllCategorias();

        for (Categoria listaCat: listadoDeCategorias) {

            if (Objects.equals(listaCat.getNombre(), nombreCat)) {
                Toast.makeText(getContext(), "Categoria Existente", Toast.LENGTH_SHORT).show();
                validacion = true;
                break;
            }else{
                validacion = false;
            }
        }

        if (!validacion) {
            Categoria nuevaCategoria = new Categoria(nombreCat, descrCat);
            SuperListaDbManager.getInstance().addCategoria(nuevaCategoria);
            //Toast.makeText(getContext(), "Categoría Agregada", Toast.LENGTH_SHORT).show();

            alertDialogEdit("Categoría Agregada");
            llamarFragmentCat();

        }

    }

    public void alertDialogEdit(String info){// metodo que lanza un alert dialog informativo


        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(info)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
        builder.show();

    }

    public void llamarFragmentCat(){

        fragmento = new FragmentCategorias();
        FragmentTransaction ft =  getFragmentManager().beginTransaction();
        ft.replace(R.id.contenedor, fragmento);
        ft.addToBackStack("CategoriaAgregada");
        getActivity().getSupportFragmentManager().popBackStack("Categorias", FragmentManager.POP_BACK_STACK_INCLUSIVE);
        ft.commit();
    }





}
