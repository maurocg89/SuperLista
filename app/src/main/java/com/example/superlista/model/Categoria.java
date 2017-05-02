package com.example.superlista.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "categoria")
public class Categoria {

    // Nombre de columnas de la tabla en la base de datos
    public static final String _ID = "id_categoria";
    public static final String COLUMNA_NOMBRE = "nombre";
    public static final String COLUMNA_DESCRIPCION = "descripcion";

    @DatabaseField(generatedId = true, columnName = _ID)
    private int id_categoria;

    @DatabaseField(columnName = COLUMNA_NOMBRE, canBeNull = false, unique = true)
    private String nombre;

    @DatabaseField(columnName = COLUMNA_DESCRIPCION)
    private String descripcion;

    public Categoria() {
    }

    public Categoria(String nombre, String descripcion) {
        this.nombre = nombre;
        this.descripcion = descripcion;
    }

    //<editor-fold desc = "Getters and Setters">
    public int getId_categoria() {
        return id_categoria;
    }

    public void setId_categoria(int id_categoria) {
        this.id_categoria = id_categoria;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    //</editor-fold>

    @Override
    public String toString() {
        return nombre;
    }

    //<editor-fold desc="Equals y Hash Code">
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Categoria categoria = (Categoria) o;

        if (id_categoria != categoria.id_categoria) return false;
        if (!nombre.equals(categoria.nombre)) return false;
        return descripcion != null ? descripcion.equals(categoria.descripcion) : categoria.descripcion == null;

    }

    @Override
    public int hashCode() {
        int result = id_categoria;
        result = 31 * result + nombre.hashCode();
        result = 31 * result + (descripcion != null ? descripcion.hashCode() : 0);
        return result;
    }
    //</editor-fold>
}
