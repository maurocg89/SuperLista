package com.example.superlista.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "producto")
public class Producto implements Parcelable{
    // TODO: Agregar campo unidad
    // Nombre de columnas de la tabla en la base de datos
    public static final String _ID = "id_producto";
    public static final String COLUMNA_SUPER_FKEY = "id_super";
    public static final String COLUMNA_NOMBRE = "nombre";
    public static final String COLUMNA_MARCA = "marca";
    public static final String COLUMNA_PRECIO = "precio";
    public static final String COLUMNA_CATEGORIA_FKEY = "id_categoria";
    public static final String COLUMNA_IMAGEN_PROD = "imagen_producto";

    @DatabaseField(generatedId = true, columnName = _ID)
    private int id_producto;

    @DatabaseField(columnName = COLUMNA_NOMBRE, canBeNull = false, uniqueCombo = true)
    private String nombre;

    @DatabaseField(columnName = COLUMNA_MARCA, uniqueCombo = true)
    private String marca;

    @DatabaseField(columnName = COLUMNA_PRECIO, canBeNull = false, defaultValue = "0")
    private double precio;

    // Foreign Keys
    @DatabaseField(foreign = true, columnName = COLUMNA_CATEGORIA_FKEY, foreignAutoRefresh = true)
    private Categoria categoria;

    @DatabaseField(foreign = true, columnName = COLUMNA_SUPER_FKEY, uniqueCombo = true, foreignAutoRefresh = true)
    private Supermercado supermercado;

    @DatabaseField(columnName = COLUMNA_IMAGEN_PROD)
    private String imagen;

    public Producto(){}

    public Producto(String nombre, String marca, double precio, Categoria categoria, Supermercado supermercado, String imagen){
        this.nombre = nombre;
        this.marca = marca;
        this.precio = precio;
        this.categoria = categoria;
        this.supermercado = supermercado;
        this.imagen = imagen;
    }

    //<editor-fold desc = "Getters and Setters">
    public int getId_producto() {
        return id_producto;
    }

    public void setId_producto(int id_producto) {
        this.id_producto = id_producto;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public Supermercado getSupermercado() {
        return supermercado;
    }

    public void setSupermercado(Supermercado supermercado) {
        this.supermercado = supermercado;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    //</editor-fold>

    //<editor-fold desc="Equals y HashCode">
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Producto producto = (Producto) o;

        if (id_producto != producto.id_producto) return false;
        if (Double.compare(producto.precio, precio) != 0) return false;
        if (!nombre.equals(producto.nombre)) return false;
        if (!marca.equals(producto.marca)) return false;
        if (!categoria.equals(producto.categoria)) return false;
        return supermercado.equals(producto.supermercado);

    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = id_producto;
        result = 31 * result + nombre.hashCode();
        result = 31 * result + marca.hashCode();
        temp = Double.doubleToLongBits(precio);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + categoria.hashCode();
        result = 31 * result + supermercado.hashCode();
        return result;
    }
    //</editor-fold>

    @Override
    public String toString() {
        return nombre + " " + marca;
    }

    //<editor-fold desc="Parcelable">

    public static final Creator<Producto> CREATOR = new Creator<Producto>() {
        @Override
        public Producto createFromParcel(Parcel parcel) {
            return new Producto(parcel);
        }

        @Override
        public Producto[] newArray(int size) {
            return new Producto[size];
        }
    };

    public Producto (Parcel in){
        this.id_producto = in.readInt();
        this.nombre = in.readString();
        this.marca = in.readString();
        this.precio = in.readDouble();
        this.categoria = in.readParcelable(Categoria.class.getClassLoader());
        this.supermercado = in.readParcelable(Supermercado.class.getClassLoader());
        this.imagen = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id_producto);
        parcel.writeString(nombre);
        parcel.writeString(marca);
        parcel.writeDouble(precio);
        parcel.writeParcelable(categoria, i);
        parcel.writeParcelable(supermercado, i);
        parcel.writeString(imagen);
    }
    //</editor-fold>
}
