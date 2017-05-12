package com.example.superlista.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;
import java.sql.Blob;


@DatabaseTable(tableName = "supermercado")
public class Supermercado implements Parcelable{

    // Nombre de columnas de la tabla en la base de datos
    public static final String _ID = "id_supermercado";
    public static final String COLUMNA_NOMBRE = "nombre";
    public static final String COLUMNA_SUCURSAL = "sucursal";
    public static final String COLUMNA_LOGO = "logo"; //creando variable de imagen en la base
    public static final int ID_LA_GALLEGA = 1;
    public static final int ID_COTO = 2;
    public static final int ID_CARREFOUR = 3;

    @DatabaseField(generatedId = true, columnName = _ID)
    private int id_supermercado;

    @DatabaseField(columnName = COLUMNA_NOMBRE, canBeNull = false, uniqueCombo = true)
    private String nombre;

    @DatabaseField(columnName = COLUMNA_SUCURSAL, uniqueCombo = true)
    private String sucursal;

    @DatabaseField(columnName = COLUMNA_LOGO, uniqueCombo = true)//identificaciond de imagen en la base
    private int logo;


    public Supermercado() {
    }

    public Supermercado(String nombre, String sucursal, int logo) {
        this.nombre = nombre;
        this.sucursal = sucursal;
        this.logo = logo;
    }

    //<editor-fold desc = "Getters and Setters">
    public int getId_supermercado() {
        return id_supermercado;
    }

    public void setId_supermercado(int id_supermercado) {
        this.id_supermercado = id_supermercado;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getSucursal() {
        return sucursal;
    }

    public void setSucursal(String sucursal) {
        this.sucursal = sucursal;
    }

    public int getLogo() {
        return logo;
    }

    public void setLogo(int logo) {
        this.logo = logo;
    }

    //</editor-fold>

    //<editor-fold desc="Equals y Hash Code">
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Supermercado that = (Supermercado) o;

        if (id_supermercado != that.id_supermercado) return false;
        if (logo != that.logo) return false;
        if (!nombre.equals(that.nombre)) return false;
        return sucursal != null ? sucursal.equals(that.sucursal) : that.sucursal == null;

    }

    @Override
    public int hashCode() {
        int result = id_supermercado;
        result = 31 * result + nombre.hashCode();
        result = 31 * result + (sucursal != null ? sucursal.hashCode() : 0);
        result = 31 * result + logo;
        return result;
    }
    //</editor-fold>

    //<editor-fold desc="Parcelable">
    public static final Creator<Supermercado> CREATOR = new Creator<Supermercado>() {
        @Override
        public Supermercado createFromParcel(Parcel parcel) {
            return new Supermercado(parcel);
        }

        @Override
        public Supermercado[] newArray(int size) {
            return new Supermercado[size];
        }
    };

    public Supermercado (Parcel in){
        this.id_supermercado = in.readInt();
        this.nombre = in.readString();
        this.sucursal = in.readString();
        this.logo = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id_supermercado);
        parcel.writeString(nombre);
        parcel.writeString(sucursal);
        parcel.writeInt(logo);

    }
    //</editor-fold>
}
