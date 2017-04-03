package com.example.superlista.model;

/**
 * Created by user on 31/03/2017.
 */

public class ProductoPorLista {

    private Producto producto;
    private Lista lista;
    private int cantidad;

    public ProductoPorLista() {
    }

    public ProductoPorLista(Producto producto, Lista lista, int cantidad) {
        this.producto = producto;
        this.lista = lista;
        this.cantidad = cantidad;
    }

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
}
