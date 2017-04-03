package com.example.superlista.model;

/**
 * Created by user on 31/03/2017.
 */

public class Supermercado {

    private int id_supermercado;
    private String nombre;
    private String sucursal;

    public Supermercado() {
    }

    public Supermercado(String nombre, String sucursal) {
        this.nombre = nombre;
        this.sucursal = sucursal;
    }

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
}
