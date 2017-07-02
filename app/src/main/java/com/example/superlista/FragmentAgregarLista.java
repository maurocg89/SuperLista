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
import com.example.superlista.model.Lista;

import java.util.List;
import java.util.Objects;


public class FragmentAgregarLista extends Fragment implements View.OnClickListener {

    Fragment fragmento = null;

    private List<Lista> listadoDeListas;
    private LinearLayout linearLayoutAgregarList;
    private EditText nombreLista;
    private Button botonAgregar;
    private String dato, auxiliar;
    private boolean validacion = true;



    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.form_agregar_lista, container, false);


        linearLayoutAgregarList = (LinearLayout) view.findViewById(R.id.linearLayoutAgregarLista);
        linearLayoutAgregarList.setOnClickListener(this);

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

           bajarTeclado();
           dato = nombreLista.getText().toString();
           auxiliar= dato.trim();

           if (auxiliar.length() > 0){ //verificacion para que el campo nombre no sea vacio
                check_add_Lista(dato);
            }else{
                Toast.makeText(getContext(), "Coloque un Nombre a Lista", Toast.LENGTH_SHORT).show();
            }

        }else if (v == linearLayoutAgregarList){
            bajarTeclado();
        }

    }

    private void bajarTeclado(){

        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(nombreLista.getWindowToken (), 0);
        Log.i("Bajamos el teclado", "ahora");

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
            //Toast.makeText(getContext(), "Lista Agregada", Toast.LENGTH_SHORT).show();

            alertDialogEdit("Lista Agregada");
            llamarFragmentListas();

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

    public void llamarFragmentListas() {
        fragmento = new FragmentListas();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.contenedor, fragmento);
        //ft.addToBackStack("ListaAgregada");
        getActivity().getSupportFragmentManager().popBackStack("Listas", FragmentManager.POP_BACK_STACK_INCLUSIVE);
        ft.commit();
    }





}
