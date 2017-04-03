package com.example.superlista.model;

/**
 * Created by user on 31/03/2017.
 */

public class Producto {

    private int id_producto;
    private String nombre;
    private String marca;
    private double precio;
    // Foreign Keys
    private Categoria categoria;
    private Supermercado supermercado;

    public Producto(){}

    public Producto(String nombre, String marca, double precio, Categoria categoria, Supermercado supermercado){

        this.nombre = nombre;
        this.marca = marca;
        this.precio = precio;
        this.categoria = categoria;
        this.supermercado = supermercado;
    }

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
}
