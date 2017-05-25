package com.example.superlista.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "producto")
public class Producto implements Parcelable{

    // Nombre de columnas de la tabla en la base de datos
    public static final String _ID = "id_producto";
    public static final String COLUMNA_NOMBRE = "nombre";
    public static final String COLUMNA_MARCA_FKEY = "id_marca";
    public static final String COLUMNA_PRECIO_COTO = "precio_coto";
    public static final String COLUMNA_PRECIO_LA_GALLEGA = "precio_la_gallega";
    public static final String COLUMNA_PRECIO_CARREFOUR = "precio_carrefour";
    public static final String COLUMNA_PRECIO_OTRO = "precio_otro";
    public static final String COLUMNA_CATEGORIA_FKEY = "id_categoria";
    public static final String COLUMNA_IMAGEN_PROD = "imagen_producto";
    public static final String COLUMNA_UNIDAD_PROD = "unidad";
    public static final String[] UNIDADES = new String[]{"Kg.", "Un."};

    @DatabaseField(generatedId = true, columnName = _ID)
    private int id_producto;

    @DatabaseField(columnName = COLUMNA_NOMBRE, canBeNull = false, uniqueCombo = true)
    private String nombre;

    @DatabaseField(columnName = COLUMNA_PRECIO_COTO, canBeNull = false, defaultValue = "0")
    private double precio_coto;

    @DatabaseField(columnName = COLUMNA_PRECIO_LA_GALLEGA, canBeNull = false, defaultValue = "0")
    private double precio_la_gallega;

    @DatabaseField(columnName = COLUMNA_PRECIO_CARREFOUR, canBeNull = false, defaultValue = "0")
    private double precio_carrefour;

    @DatabaseField(columnName = COLUMNA_PRECIO_OTRO, canBeNull = false, defaultValue = "0")
    private double precio_otro;

    @DatabaseField(columnName = COLUMNA_IMAGEN_PROD)
    private String imagen;

    @DatabaseField(columnName = COLUMNA_UNIDAD_PROD)
    private String unidad;


    @DatabaseField(foreign = true, columnName = COLUMNA_CATEGORIA_FKEY, foreignAutoRefresh = true)
    private Categoria categoria;

    @DatabaseField(foreign = true, columnName = COLUMNA_MARCA_FKEY, uniqueCombo = true, foreignAutoRefresh = true)
    private Marca marca;


    public Producto(){}

    public Producto(String nombre, Marca marca, double precio_coto, double precio_la_gallega, double precio_carrefour,
                    double precio_otro, Categoria categoria, String imagen, String unidad){
        this.nombre = nombre;
        this.marca = marca;
        this.precio_coto = precio_coto;
        this.precio_la_gallega = precio_la_gallega;
        this.precio_carrefour = precio_carrefour;
        this.precio_otro = precio_otro;
        this.categoria = categoria;
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

    public Marca getMarca() {
        return marca;
    }

    public void setMarca(Marca marca) {
        this.marca = marca;
    }

    public double getPrecio_coto() {
        return precio_coto;
    }

    public void setPrecio_coto(double precio_coto) {
        this.precio_coto = precio_coto;
    }

    public double getPrecio_la_gallega() {
        return precio_la_gallega;
    }

    public void setPrecio_la_gallega(double precio_la_gallega) {
        this.precio_la_gallega = precio_la_gallega;
    }

    public double getPrecio_carrefour() {
        return precio_carrefour;
    }

    public void setPrecio_carrefour(double precio_carrefour) {
        this.precio_carrefour = precio_carrefour;
    }

    public double getPrecio_otro() {
        return precio_otro;
    }

    public void setPrecio_otro(double precio_otro) {
        this.precio_otro = precio_otro;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
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
        if (!nombre.equals(producto.nombre)) return false;
        if (!categoria.equals(producto.categoria)) return false;
        return marca.equals(producto.marca);

    }

    @Override
    public int hashCode() {
        int result = id_producto;
        result = 31 * result + nombre.hashCode();
        result = 31 * result + unidad.hashCode();
        result = 31 * result + categoria.hashCode();
        result = 31 * result + marca.hashCode();
        return result;
    }
    //</editor-fold>

    @Override
    public String toString() {
        if (marca.getNombre().equalsIgnoreCase("ninguna")){
            return nombre;
        }
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
        this.marca = in.readParcelable(Marca.class.getClassLoader());
        this.precio_coto = in.readDouble();
        this.precio_carrefour = in.readDouble();
        this.precio_la_gallega = in.readDouble();
        this.precio_otro = in.readDouble();
        this.categoria = in.readParcelable(Categoria.class.getClassLoader());
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
        parcel.writeDouble(precio_coto);
        parcel.writeDouble(precio_carrefour);
        parcel.writeDouble(precio_la_gallega);
        parcel.writeDouble(precio_otro);
        parcel.writeParcelable(categoria, i);
        parcel.writeParcelable(marca, i);
        parcel.writeString(imagen);
        parcel.writeString(unidad);
    }
    //</editor-fold>
}
