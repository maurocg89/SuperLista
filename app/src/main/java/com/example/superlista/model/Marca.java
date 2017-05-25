package com.example.superlista.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "marca")
public class Marca implements Parcelable {

    public static final String _ID = "id_marca";
    public static final String COLUMNA_NOMBRE = "nombre";
    public static final int ID_MARCA_NINGUNA = 1;

    @DatabaseField(generatedId = true, columnName = _ID)
    private int id_marca;

    @DatabaseField(columnName = COLUMNA_NOMBRE, canBeNull = false, unique = true)
    private String nombre;

    public Marca(){}

    public Marca(String nombre){
        this.nombre = nombre;
    }

    //<editor-fold desc = "Getters and Setters">
    public int getId_Marca() {
        return id_marca;
    }

    public void setId_Marca(int id_marca) {
        this.id_marca = id_marca;
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

        Marca marca = (Marca) o;

        if (id_marca != marca.id_marca) return false;
        return nombre.equals(marca.nombre);

    }

    @Override
    public int hashCode() {
        int result = id_marca;
        result = 31 * result + nombre.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return nombre;
    }

    //</editor-fold>

    //<editor-fold desc="Parcelable">
    public static final Creator<Marca> CREATOR = new Creator<Marca>() {
        @Override
        public Marca createFromParcel(Parcel parcel) {
            return new Marca(parcel);
        }

        @Override
        public Marca[] newArray(int size) {
            return new Marca[size];
        }
    };

    public Marca (Parcel in){
        this.id_marca = in.readInt();
        this.nombre = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id_marca);
        parcel.writeString(nombre);
    }
    //</editor-fold>
}
