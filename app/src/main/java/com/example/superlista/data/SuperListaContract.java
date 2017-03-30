package com.example.superlista.data;

import android.provider.BaseColumns;

/**
 * Created by user on 30/03/2017.
 *
 * Clase que define constantes con los nombres de las columnas y tablas de la base de datos
 */

public final class SuperListaContract {

    // Para prevenir que se instancie por accidente esta clase, se pone privado el constructor
    private SuperListaContract(){}

    // Clase interna que define el contenido de la tabla producto
    public static final class ProductoEntry implements BaseColumns{

        public static final String TABLA_NOMBRE = "producto";

        public static final String _ID = "id_producto";
        public static final String COLUMNA_SUPER_FKEY = "id_super";
        public static final String COLUMNA_NOMBRE = "nombre";
        public static final String COLUMNA_MARCA = "marca";
        public static final String COLUMNA_PRECIO = "precio";
        public static final String COLUMNA_CATEGORIA_FKEY = "id_categoria";

    }

    // Clase interna que define el contenido de la tabla categoria
    public static final class CategoriaEntry implements BaseColumns{

        public static final String TABLA_NOMBRE = "categoria";

        public static final String _ID = "id_categoria";
        public static final String COLUMNA_NOMBRE = "nombre";
        public static final String COLUMNA_DESCRIPCION = "descripcion";

    }

    // Clase interna que define el contenido de la tabla supermercado
    public static final class SupermercadoEntry implements BaseColumns{

        public static final String TABLA_NOMBRE = "supermercado";

        public static final String _ID = "id_supermercado";
        public static final String COLUMNA_NOMBRE = "nombre";
        public static final String COLUMNA_SUCURSAL = "sucursal";

    }

    // Clase interna que define el contenido de la tabla lista
    public static final class ListaEntry implements BaseColumns{

        public static final String TABLA_NOMBRE = "lista";

        public static final String _ID = "id_lista";
        public static final String COLUMNA_NOMBRE = "nombre";

    }

    // Clase interna que define el contenido de la tabla producto_por_lista
    public static final class ProductoPorListaEntry implements BaseColumns{

        public static final String TABLA_NOMBRE = "producto_por_lista";

        public static final String COLUMNA_PRODUCTO_FKEY = "id_producto";
        public static final String COLUMNA_LISTA_FKEY = "id_lista";
        public static final String COLUMNA_CANTIDAD_FKEY = "cantidad";

    }


}
