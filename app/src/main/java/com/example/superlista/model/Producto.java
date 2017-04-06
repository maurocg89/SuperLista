package com.example.superlista.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "producto")
public class Producto {

    // Nombre de columnas de la tabla en la base de datos
    public static final String _ID = "id_producto";
    public static final String COLUMNA_SUPER_FKEY = "id_super";
    public static final String COLUMNA_NOMBRE = "nombre";
    public static final String COLUMNA_MARCA = "marca";
    public static final String COLUMNA_PRECIO = "precio";
    public static final String COLUMNA_CATEGORIA_FKEY = "id_categoria";

    @DatabaseField(generatedId = true, columnName = _ID)
    private int id_producto;

    @DatabaseField(columnName = COLUMNA_NOMBRE, canBeNull = false, uniqueCombo = true)
    private String nombre;

    @DatabaseField(columnName = COLUMNA_MARCA, canBeNull = false, uniqueCombo = true)
    private String marca;

    @DatabaseField(columnName = COLUMNA_PRECIO, canBeNull = false, defaultValue = "0")
    private double precio;

    // Foreign Keys
    @DatabaseField(foreign = true, columnName = COLUMNA_CATEGORIA_FKEY)
    private Categoria categoria;
    @DatabaseField(foreign = true, columnName = COLUMNA_SUPER_FKEY, uniqueCombo = true)
    private Supermercado supermercado;

    public Producto(){}

    public Producto(String nombre, String marca, double precio, Categoria categoria, Supermercado supermercado){

        this.nombre = nombre;
        this.marca = marca;
        this.precio = precio;
        this.categoria = categoria;
        this.supermercado = supermercado;
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
    //</editor-fold>
}
