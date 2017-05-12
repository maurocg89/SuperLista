package com.example.superlista.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "lista")
public class Lista implements Parcelable{

    // Nombre de columnas de la tabla en la base de datos
    public static final String _ID = "id_lista";
    public static final String COLUMNA_NOMBRE = "nombre";

    @DatabaseField(generatedId = true, columnName = _ID)
    private int id_lista;

    @DatabaseField(columnName = COLUMNA_NOMBRE, canBeNull = false, unique = true)
    private String nombre;

    public Lista() {
    }

    public Lista(String nombre) {
        this.nombre = nombre;
    }

    //<editor-fold desc = "Getters and Setters">
    public int getId_lista() {
        return id_lista;
    }

    public void setId_lista(int id_lista) {
        this.id_lista = id_lista;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    //</editor-fold>


    //<editor-fold desc="Equals HashCode y ToString">
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Lista lista = (Lista) o;

        if (id_lista != lista.id_lista) return false;
        return nombre.equals(lista.nombre);

    }

    @Override
    public int hashCode() {
        int result = id_lista;
        result = 31 * result + nombre.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return nombre;
    }

    //</editor-fold>

    //<editor-fold desc="Parcelable">
    public static final Creator<Lista> CREATOR = new Creator<Lista>() {
        @Override
        public Lista createFromParcel(Parcel parcel) {
            return new Lista(parcel);
        }

        @Override
        public Lista[] newArray(int size) {
            return new Lista[size];
        }
    };

    public Lista (Parcel in){
        this.id_lista = in.readInt();
        this.nombre = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id_lista);
        parcel.writeString(nombre);
    }
    //</editor-fold>
}
