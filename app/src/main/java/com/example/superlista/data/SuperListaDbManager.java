package com.example.superlista.data;

import android.content.Context;

import com.example.superlista.model.Categoria;
import com.example.superlista.model.Lista;
import com.example.superlista.model.Producto;
import com.example.superlista.model.ProductoPorLista;
import com.example.superlista.model.Supermercado;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.GenericRawResults;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.QueryBuilder;

import java.io.IOException;
import java.util.List;
import java.sql.SQLException;

// Singleton
public class SuperListaDbManager {

    private static SuperListaDbManager instance;
    private SuperListaDbHelper helper;

    private SuperListaDbManager(Context context) {
        helper = new SuperListaDbHelper(context);
        try{
            helper.createDataBase();
        } catch (IOException e){
            throw new Error("No se pudo crear la base de datos");
        }
        try{
            helper.openDataBase();
        } catch (Exception sqle){
            throw sqle;
        }
    }

    private SuperListaDbHelper getHelper(){
        return helper;
    }

    // Se llama una sola vez en el onCreate del activity principal
    public static void init(Context context){
        if(instance == null){
            instance = new SuperListaDbManager(context);
        }
    }

    public static SuperListaDbManager getInstance(){
        return instance;
    }

    // <editor-fold desc = "Acciones Listas">
    public List<Lista>getAllListas(){
        List<Lista> listas = null;
        try {
            listas = getHelper().getListaDao().queryForAll();
        }catch (SQLException e){
            e.printStackTrace();
        }
        return listas;
    }

    public Lista getListaById(int id_lista){
        Lista lista = null;
        try {
            lista = getHelper().getListaDao().queryForId(id_lista);
        }catch (SQLException e){
            e.printStackTrace();
        }
        return lista;
    }

    public void addLista(Lista lista){
        try {
            getHelper().getListaDao().create(lista);
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void deleteLista(int id_lista){
        try {
            getHelper().getListaDao().deleteById(id_lista);
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void updateLista(int id_lista, String nombre_nuevo){
        try {
            Lista lista = getListaById(id_lista);
            lista.setNombre(nombre_nuevo);
            getHelper().getListaDao().update(lista);
        }catch (SQLException e){
            e.printStackTrace();
        }

    }

    // </editor-fold>

    // <editor-fold desc = "Acciones Productos">
    public List<Producto> getAllProductos(){
        List<Producto> productos = null;
        try {
            productos = getHelper().getProductoDao().queryForAll();
        }catch (SQLException e){
            e.printStackTrace();
        }
        return productos;
    }

    public List<Producto> getAllProductosByName(){
        QueryBuilder<Producto, Integer> queryBuilder = getHelper().getProductoDao().queryBuilder();
        queryBuilder.distinct().selectColumns(Producto.COLUMNA_NOMBRE);
        List<Producto> productos = null;
        try {
            /*GenericRawResults<String[]> rawResults =
                    getHelper().getProductoDao().queryRaw("SELECT DISTINCT "+Producto.COLUMNA_NOMBRE+" FROM producto");*/
            productos = queryBuilder.query();
        }catch (SQLException e){
            e.printStackTrace();
        }
        return productos;
    }

    public Producto getProductoById(int id_producto){
        Producto producto = null;
        try {
            producto = getHelper().getProductoDao().queryForId(id_producto);
        }catch (SQLException e){
            e.printStackTrace();
        }
        return producto;
    }

    public void addProducto(Producto producto){
        try {
            getHelper().getProductoDao().create(producto);
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void deleteProducto(int id_producto){
        try {
            getHelper().getProductoDao().deleteById(id_producto);
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void updateProducto(int id_producto, String nombre_nuevo, String marca_nueva, double precio_nuevo, Categoria categoria_nueva, Supermercado supermercado_nuevo){
        try {
            Producto producto = getProductoById(id_producto);
            producto.setNombre(nombre_nuevo);
            producto.setMarca(marca_nueva);
            producto.setPrecio(precio_nuevo);
            producto.setCategoria(categoria_nueva);
            producto.setSupermercado(supermercado_nuevo);
            getHelper().getProductoDao().update(producto);
        }catch (SQLException e){
            e.printStackTrace();
        }

    }

    //TODO: Agregar metodo que devuelva los productos por categoria sin repetir nombres(distinct)
    // </editor-fold>

    // <editor-fold desc = "Acciones Categorias">
    public List<Categoria> getAllCategorias(){
        List<Categoria> categorias = null;
        try {
            categorias = getHelper().getCategoriaDao().queryForAll();
        }catch (SQLException e){
            e.printStackTrace();
        }
        return categorias;
    }

    public Categoria getCategoriaById(int id_categoria){
        Categoria categoria = null;
        try {
            categoria = getHelper().getCategoriaDao().queryForId(id_categoria);
        }catch (SQLException e){
            e.printStackTrace();
        }
        return categoria;
    }

    public void addCategoria(Categoria categoria){
        try {
            getHelper().getCategoriaDao().create(categoria);
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void deleteCategoria(int id_categoria){
        try {
            getHelper().getCategoriaDao().deleteById(id_categoria);
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void updateCategoria(int id_categoria, String nombre_nuevo, String descripcion_nueva){
        try {
            Categoria categoria = getCategoriaById(id_categoria);
            categoria.setNombre(nombre_nuevo);
            categoria.setDescripcion(descripcion_nueva);
            getHelper().getCategoriaDao().update(categoria);
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    // </editor-fold>

    // <editor-fold desc = "Acciones Supermercados">
    public List<Supermercado> getAllSupermercados(){
        List<Supermercado> supermercados = null;
        try {
            supermercados = getHelper().getSupermercadoDao().queryForAll();
        }catch (SQLException e){
            e.printStackTrace();
        }
        return supermercados;
    }
    public Supermercado getSupermercadoById(int id_supermercado){
        Supermercado supermercado = null;
        try {
            supermercado = getHelper().getSupermercadoDao().queryForId(id_supermercado);
        }catch (SQLException e){
            e.printStackTrace();
        }
        return supermercado;
    }

    // </editor-fold>

    // <editor-fold desc = "Acciones Producto Por Lista">
    // Devuelve todos los productos de todas las listas
    public List<ProductoPorLista> getAllProductosListas(){
        List<ProductoPorLista> productosPorLista = null;
        try {
            productosPorLista = getHelper().getProductoPorListaDao().queryForAll();
        }catch (SQLException e){
            e.printStackTrace();
        }
        return productosPorLista;
    }

    // Devuelve todos los productos de una lista
    public List<ProductoPorLista> getAllProductosListaS(int id_lista){
        List<ProductoPorLista> productos = null;
        try {
            productos = getHelper().getProductoPorListaDao().queryForEq(Lista._ID, id_lista);
        }catch (SQLException e){
            e.printStackTrace();
        }
        return productos;

    }

    public ProductoPorLista getProductosDeListaById(int id_producto_lista){
        ProductoPorLista productoPorLista = null;
        try {
         productoPorLista = getHelper().getProductoPorListaDao().queryForId(id_producto_lista);
        }catch (SQLException e){
            e.printStackTrace();
        }
        return productoPorLista;

    }

    public void addProductoLista(ProductoPorLista productoPorLista){
        try {
            getHelper().getProductoPorListaDao().create(productoPorLista);
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void deleteProductoLista(int id_producto_lista){
        try {
            getHelper().getProductoPorListaDao().deleteById(id_producto_lista);
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    // Borra todos los productos de una lista
    public void deleteAllProductosDeLista(int id_lista){
        try {
            Dao<ProductoPorLista, Integer> produtoListaDao = getHelper().getProductoPorListaDao();
            DeleteBuilder<ProductoPorLista, Integer> db = produtoListaDao.deleteBuilder();
            db.where().eq(ProductoPorLista.COLUMNA_LISTA_FKEY, id_lista);
            produtoListaDao.delete(db.prepare());

        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    // Modifica la cantidad de un producto de una lista
    public void updateProductoLista(int id_producto_lista, int cantidad_nueva){
        try {
            ProductoPorLista productoPorLista = getProductosDeListaById(id_producto_lista);
            productoPorLista.setCantidad(cantidad_nueva);
            getHelper().getProductoPorListaDao().update(productoPorLista);
        }catch (SQLException e){
            e.printStackTrace();
        }
    }


    // </editor-fold>


}
