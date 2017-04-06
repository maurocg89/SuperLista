package com.example.superlista.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "producto_por_lista")
public class ProductoPorLista {

    // Nombre de columnas de la tabla en la base de datos
    public static final String _ID = "id_producto_lista";
    public static final String COLUMNA_PRODUCTO_FKEY = "id_producto";
    public static final String COLUMNA_LISTA_FKEY = "id_lista";
    public static final String COLUMNA_CANTIDAD = "cantidad";

    @DatabaseField(columnName = _ID, generatedId = true)
    private int id_producto_lista;

    @DatabaseField(columnName = COLUMNA_CANTIDAD, canBeNull = false, defaultValue = "0")
    private int cantidad;

    // Foreign keys
    @DatabaseField(columnName = COLUMNA_PRODUCTO_FKEY, id = true, foreign = true)
    private Producto producto;
    @DatabaseField(columnName = COLUMNA_LISTA_FKEY, id = true, foreign = true)
    private Lista lista;


    public ProductoPorLista() {
    }

    public ProductoPorLista(Producto producto, Lista lista, int cantidad) {
        this.producto = producto;
        this.lista = lista;
        this.cantidad = cantidad;
    }

    //<editor-fold desc="Getters And Setters">

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public Lista getLista() {
        return lista;
    }

    public void setLista(Lista lista) {
        this.lista = lista;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public int getId_producto_lista() {
        return id_producto_lista;
    }

    public void setId_producto_lista(int id_producto_lista) {
        this.id_producto_lista = id_producto_lista;
    }

    //</editor-fold>
}
