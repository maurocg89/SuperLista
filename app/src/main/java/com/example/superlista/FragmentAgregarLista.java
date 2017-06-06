package com.example.superlista;



import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.superlista.data.SuperListaDbManager;
import com.example.superlista.model.Lista;

import java.util.List;
import java.util.Objects;


public class FragmentAgregarLista extends Fragment implements View.OnClickListener {

    Fragment fragmento = null;

    private List<Lista> listadoDeListas;
    private EditText nombreLista;
    private Button botonAgregar;
    private String dato, auxiliar;
    private boolean validacion = true;



    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.form_agregar_lista, container, false);


        nombreLista = (EditText) view.findViewById(R.id.editTextNombreForm);
        botonAgregar = (Button) view.findViewById(R.id.buttonAgregar);
        botonAgregar.setOnClickListener(this);



        return view;
    }


    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle(R.string.nueva_lista);
    }




    @Override
    public void onClick(View v) {

        if (v == botonAgregar){

           dato = nombreLista.getText().toString();
           auxiliar= dato.trim();

           if (auxiliar.length() > 0){ //verificacion para que el campo nombre no sea vacio
                check_add_Lista(dato);
            }else{
                Toast.makeText(getContext(), "Coloque un Nombre a Lista", Toast.LENGTH_SHORT).show();
            }

        }

    }

    public void check_add_Lista(String nombreList){

        listadoDeListas = SuperListaDbManager.getInstance().getAllListas();

        for (Lista lista: listadoDeListas) {

            if (Objects.equals(lista.getNombre(), nombreList)) {
                Toast.makeText(getContext(), "Nombre Existente", Toast.LENGTH_SHORT).show();
                validacion = true;
                break;
            }else{
                validacion = false;
            }
        }

        if (!validacion) {
            Lista nuevaLista = new Lista(nombreList);
            SuperListaDbManager.getInstance().addLista(nuevaLista);
            Toast.makeText(getContext(), "Lista Agregada", Toast.LENGTH_SHORT).show();
            llamarFragmentListas();

        }

    }

    public void llamarFragmentListas() {

        fragmento = new FragmentListas();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.contenedor, fragmento);
        ft.addToBackStack("ListaAgregada");
        getActivity().getSupportFragmentManager().popBackStack("Listas", FragmentManager.POP_BACK_STACK_INCLUSIVE);
        ft.commit();
    }





}
