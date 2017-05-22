package com.example.superlista;


import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

public class FragmentAlertDialogAddMarca extends DialogFragment implements TextView.OnEditorActionListener {


    private EditText mEditText;




    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return super.onCreateDialog(savedInstanceState);
    }

    public interface NuevoDialogListener {

        void FinalizaCuadroDialogo(String texto);
    }

    // El contructor vacio es requerido para el dialogFragment
    public FragmentAlertDialogAddMarca() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.alert_dialog_add_marca, container);

        mEditText = (EditText) view.findViewById(R.id.edit_text_marca);

        // creamos una instancia para el escuchador de eventos para la accion de Edicion
        mEditText.setOnEditorActionListener(this);
        mEditText.requestFocus();
        //getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        getDialog().setTitle(" Nueva Marca");

        return view;
    }


    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

        NuevoDialogListener activity = (NuevoDialogListener) getActivity();
        activity.FinalizaCuadroDialogo(mEditText.getText().toString());
        this.dismiss();
        return true;

    }
}
