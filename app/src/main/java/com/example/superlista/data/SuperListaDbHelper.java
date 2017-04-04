package com.example.superlista.data;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by user on 30/03/2017.
 */

public class SuperListaDbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "superLista.db";
    String DATABASE_PATH = null;
    private final Context myContext;
    private SQLiteDatabase myDataBase;

    public SuperListaDbHelper(Context context){

        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.myContext = context;
        this.DATABASE_PATH =  "/data/data/" + context.getPackageName() + "/databases/";
        //Log.e("Path 1: ", DATABASE_PATH);
    }

    // Si la base de datos no existe en el dispositivo, copia la que cargamos en la carpeta assets
    public void createDataBase() throws IOException{
        boolean dbExist = checkDataBase();
        if (dbExist){}
        else{
            // Se crea una base de datos vacia en el path por defecto de la aplicacion
            // que despues se va a sobreescribir con la que tenemos en assets
            this.getReadableDatabase();
            try {
                copyDataBase();
            }catch (IOException e){
                throw new Error("Error copiando la base de datos");
            }
        }
    }

    // Comprueba si la base de datos ya existe para evitar copiar el archivo cada vez que se abre la aplicacion
    private boolean checkDataBase(){
        SQLiteDatabase checkDB = null;
        try{
            String myPath = DATABASE_PATH + DATABASE_NAME;
            checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
        } catch (SQLiteException e){

        }
        if (checkDB != null){
            checkDB.close();
        }

        return checkDB != null ? true : false;
    }

    // Copia la base de datos guardada en la carpeta assets sobreescribiendo
    // la base de datos vacia que creamos anteriormente
    private void copyDataBase() throws IOException{
        InputStream myInput = myContext.getAssets().open(DATABASE_NAME);
        String outFileName = DATABASE_PATH + DATABASE_NAME;
        OutputStream myOutput = new FileOutputStream(outFileName);
        byte[] buffer = new byte[1024];
        int length;
        while((length = myInput.read(buffer)) > 0){
            myOutput.write(buffer, 0, length);
        }
        myOutput.flush();
        myOutput.close();
        myInput.close();

    }

    public void openDataBase() throws SQLException{
        String myPath = DATABASE_PATH + DATABASE_NAME;
        myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);

    }

    @Override
    public synchronized void close(){
        if(myDataBase != null){
            myDataBase.close();
        }
        super.close();
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        if (newVersion > oldVersion){
            try{
                copyDataBase();
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }

}
