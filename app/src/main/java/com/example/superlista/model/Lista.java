package com.example.superlista.model;

/**
 * Created by user on 31/03/2017.
 */

public class Lista {

    private int id_lista;
    private String nombre;

    public Lista() {
    }

    public Lista(String nombre) {
        this.nombre = nombre;
    }

    public int getId_lista() {
        return id_lista;
    }

    public void setId_lista(int id_lista) {
        this.id_lista = id_lista;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
