package com.example.superlista.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "producto")
public class Producto implements Parcelable{

    // Nombre de columnas de la tabla en la base de datos
    public static final String _ID = "id_producto";
    public static final String COLUMNA_SUPER_FKEY = "id_super";
    public static final String COLUMNA_NOMBRE = "nombre";
    public static final String COLUMNA_MARCA = "marca";
    public static final String COLUMNA_PRECIO = "precio";
    public static final String COLUMNA_CATEGORIA_FKEY = "id_categoria";
    public static final String COLUMNA_IMAGEN_PROD = "imagen_producto";
    public static final String COLUMNA_UNIDAD_PROD = "unidad";
    public static final String[] UNIDADES = new String[]{"Kg", "Unidades"};

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

    @DatabaseField(columnName = COLUMNA_UNIDAD_PROD)
    private String unidad;

    public Producto(){}

    public Producto(String nombre, String marca, double precio, Categoria categoria, Supermercado supermercado, String imagen, String unidad){
        this.nombre = nombre;
        this.marca = marca;
        this.precio = precio;
        this.categoria = categoria;
        this.supermercado = supermercado;
        this.imagen = imagen;
        this.unidad = unidad;
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

    public String getUnidad() {
        return unidad;
    }

    public void setUnidad(String unidad) {
        this.unidad = unidad;
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
        if (marca != null ? !marca.equals(producto.marca) : producto.marca != null) return false;
        if (categoria != null ? !categoria.equals(producto.categoria) : producto.categoria != null)
            return false;
        if (supermercado != null ? !supermercado.equals(producto.supermercado) : producto.supermercado != null)
            return false;
        if (imagen != null ? !imagen.equals(producto.imagen) : producto.imagen != null)
            return false;
        return unidad.equals(producto.unidad);

    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = id_producto;
        result = 31 * result + nombre.hashCode();
        result = 31 * result + (marca != null ? marca.hashCode() : 0);
        temp = Double.doubleToLongBits(precio);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (categoria != null ? categoria.hashCode() : 0);
        result = 31 * result + (supermercado != null ? supermercado.hashCode() : 0);
        result = 31 * result + (imagen != null ? imagen.hashCode() : 0);
        result = 31 * result + unidad.hashCode();
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
        this.unidad = in.readString();
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
        parcel.writeString(unidad);
    }
    //</editor-fold>
}
