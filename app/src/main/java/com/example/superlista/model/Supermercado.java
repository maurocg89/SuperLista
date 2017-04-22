package com.example.superlista.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;



@DatabaseTable(tableName = "supermercado")
public class Supermercado{

    // Nombre de columnas de la tabla en la base de datos
    private static final String _ID = "id_supermercado";
    private static final String COLUMNA_NOMBRE = "nombre";
    private static final String COLUMNA_SUCURSAL = "sucursal";
    private static final String COLUMNA_LOGO = "logo"; //creando variable de imagen en la base


    @DatabaseField(generatedId = true, columnName = _ID)
    private int id_supermercado;

    @DatabaseField(columnName = COLUMNA_NOMBRE, canBeNull = false, uniqueCombo = true)
    private String nombre;

    @DatabaseField(columnName = COLUMNA_SUCURSAL, uniqueCombo = true)
    private String sucursal;

    @DatabaseField(columnName = COLUMNA_LOGO, uniqueCombo = true)//identificaciond de imagen en la base
    private String logo;


    public Supermercado() {
    }

    public Supermercado(String nombre, String sucursal, String logo) {
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

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    //</editor-fold>



}
