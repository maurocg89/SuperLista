package com.example.superlista.data;

import android.content.Context;

import java.io.IOException;

/**
 * Created by user on 03/04/2017.
 */
// Singleton
public class SuperListaDbManager {

    private static SuperListaDbManager instance;
    private SuperListaDbHelper helper;

    private SuperListaDbManager(Context context) {
        helper = new SuperListaDbHelper(context);
        try{
            helper.createDataBase();
        } catch (IOException e){
            throw new Error("No se pudo crear la base de datos");
        }
        try{
            helper.openDataBase();
        } catch (Exception sqle){
            throw sqle;
        }
    }

    private SuperListaDbHelper getHelper(){
        return helper;
    }

    // Se llama una sola vez en el onCreate del activity principal
    public static void init(Context context){
        if(instance == null){
            instance = new SuperListaDbManager(context);
        }
    }

    public static SuperListaDbManager getInstance(){
        return instance;
    }

    // Producto

    public void getAllProductos(){
        //getHelper().getReadableDatabase().query();
    }


}
