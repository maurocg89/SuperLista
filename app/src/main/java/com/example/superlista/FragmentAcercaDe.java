package com.example.superlista;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


public class FragmentAcercaDe extends Fragment implements View.OnClickListener{

    private Button botonEnviarCom;
    Intent intent = null, chooser = null;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_acerca_de, container, false);

        botonEnviarCom = (Button) view.findViewById(R.id.buttonEnviarComentario);
        botonEnviarCom.setOnClickListener(this);



        return view;
    }


    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle(R.string.item_acercade);
    }


    public void onClick(View view){
        if (view == botonEnviarCom){
            enviarComentario();
        }

    }

    public void enviarComentario(){

        intent = new Intent(Intent.ACTION_SEND);
        intent.setData(Uri.parse("mailto:"));
        String[] destinatario = {"federicoborda3@gmail.com", "maurocg89@gmail.com"};
        intent.putExtra(Intent.EXTRA_EMAIL, destinatario);
        intent.putExtra(Intent.EXTRA_SUBJECT, "escribe tu nombre y el asunto");
        intent.putExtra(Intent.EXTRA_TEXT, "escribe aquí tu comentario");
        intent.setType("message/rfc822");
        chooser = Intent.createChooser(intent, "Enviar Comentario");
        startActivity(chooser);



    }

}
