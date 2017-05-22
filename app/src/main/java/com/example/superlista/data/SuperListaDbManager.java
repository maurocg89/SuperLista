package com.example.superlista.data;

import android.content.Context;

import com.example.superlista.model.Categoria;
import com.example.superlista.model.Lista;
import com.example.superlista.model.Producto;
import com.example.superlista.model.ProductoPorLista;
import com.example.superlista.model.Supermercado;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.PreparedDelete;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;
import com.j256.ormlite.stmt.query.In;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.sql.SQLException;

// Singleton
public class SuperListaDbManager {

    private static SuperListaDbManager instance;
    private SuperListaDbHelper helper;

    //private SuperListaDbManager(Context context) {
    //    helper = new SuperListaDbHelper(context);
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

    // TODO: No setea los ids de los productos solo la marca y el nombre
    public List<Producto> getAllProductosByNameDistinct(){
        QueryBuilder<Producto, Integer> queryBuilder = getHelper().getProductoDao().queryBuilder();
        queryBuilder.distinct().selectColumns(Producto.COLUMNA_MARCA, Producto.COLUMNA_NOMBRE);
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

    public Producto getProductoByNombre(String nombre, String marca){
        QueryBuilder<Producto, Integer> queryBuilder = getHelper().getProductoDao().queryBuilder();
        //List<Producto> productos = null;
        Producto producto = null;
        try {
            queryBuilder.where().eq(Producto.COLUMNA_NOMBRE, nombre).and().eq(Producto.COLUMNA_MARCA, marca);
            producto = queryBuilder.queryForFirst();
            //productos = queryBuilder.query();
        }catch (SQLException e){
            e.printStackTrace();
        }
        return producto;
    }


    public Producto getProductoByNombreSuper(String nombre, String marca, int id_super){
        QueryBuilder<Producto, Integer> queryBuilder = getHelper().getProductoDao().queryBuilder();
       // QueryBuilder<Supermercado, Integer> queryBuilderSuper = getHelper().getSupermercadoDao().queryBuilder();
        Producto producto = null;
        Where<Producto, Integer> where = queryBuilder.where();

        try {

            where.eq(Producto.COLUMNA_NOMBRE, nombre);
            where.eq(Producto.COLUMNA_MARCA, marca);
            where.eq(Producto.COLUMNA_SUPER_FKEY, id_super);
            where.and(3);

            //queryBuilder.where().eq(Producto.COLUMNA_NOMBRE, nombre).and().eq(Producto.COLUMNA_MARCA, marca);
            //queryBuilderSuper.where().eq(Supermercado._ID, id_super);
            //queryBuilder.join(queryBuilderSuper);
            producto = queryBuilder.queryForFirst();
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
/*
    public void deleteProductoByNombre(String nombre, String marca){
        try {
            getHelper().getProductoDao().deleteBuilder();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
*/
    public void deleteProductoById(int id_producto){
        try {
            getHelper().getProductoDao().deleteById(id_producto);
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void deleteProductos(ArrayList<Producto>productos){
        try {
            getHelper().getProductoDao().delete(productos);
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void deleteProductosByNombre(ArrayList<Producto>productos){
        DeleteBuilder<Producto, Integer> deleteBuilder = getHelper().getProductoDao().deleteBuilder();
        try {
            for (Producto p: productos) {
                 deleteBuilder.where().eq(Producto.COLUMNA_NOMBRE, p.getNombre())
                        .and().eq(Producto.COLUMNA_MARCA, p.getMarca());
                deleteBuilder.delete();
            }
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

    public List<Producto> getProductoByCategoriaDistinct(int id_categoria){
        List<Producto> productos = null;
        try {
            QueryBuilder<Producto, Integer> queryBuilder = getHelper().getProductoDao().queryBuilder();
            queryBuilder.where().eq(Producto.COLUMNA_CATEGORIA_FKEY, id_categoria);
            queryBuilder.distinct().selectColumns(Producto.COLUMNA_NOMBRE, Producto.COLUMNA_MARCA);
            /*GenericRawResults<String[]> rawResults =
                    getHelper().getProductoDao().queryRaw("SELECT DISTINCT "+Producto.COLUMNA_NOMBRE+" FROM producto");*/
            productos = queryBuilder.query();
        }catch (SQLException e){
            e.printStackTrace();
        }
        return productos;
    }

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

    public Categoria getCategoriaByNombre(String nombre_categoria){
        Categoria categoria = null;
        QueryBuilder<Categoria, Integer> queryBuilder = getHelper().getCategoriaDao().queryBuilder();
        try {
            queryBuilder.where().eq(Categoria.COLUMNA_NOMBRE, nombre_categoria);
            categoria = queryBuilder.queryForFirst();
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

    public Supermercado getSupermercadoByNombre(String nombre_supermercado){
        Supermercado supermercado = null;
        QueryBuilder<Supermercado, Integer> queryBuilder = getHelper().getSupermercadoDao().queryBuilder();
        try {
            queryBuilder.where().eq(Supermercado.COLUMNA_NOMBRE, nombre_supermercado);
            supermercado = queryBuilder.queryForFirst();
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

    // Devuelve todos los productos de una lista y sus cantidades
    public List<ProductoPorLista> getAllProductosDeLista(int id_lista){
        List<ProductoPorLista> productos = null;
        try {
            productos = getHelper().getProductoPorListaDao().queryForEq(Lista._ID, id_lista);
        }catch (SQLException e){
            e.printStackTrace();
        }
        return productos;

    }

    //// TODO: Cambiar modelo producto, agregar 4 precios (uno por super)


    // Devuelve todos los productos de una lista
    public List<Producto> getProductosPorLista(int id_lista){

        List<Producto> productos = null;
        QueryBuilder<Producto, Integer> queryBuilderProducto = getHelper().getProductoDao().queryBuilder();
        QueryBuilder<ProductoPorLista, Integer> queryBuilderProdLista = getHelper().getProductoPorListaDao().queryBuilder();
        QueryBuilder<Lista, Integer> queryBuilderLista = getHelper().getListaDao().queryBuilder();

        try {
            queryBuilderLista.where().eq(Lista._ID, id_lista);
            queryBuilderProdLista.join(queryBuilderLista);
            productos = queryBuilderProducto.join(queryBuilderProdLista).query();

            // SELECT * FROM producto INNER JOIN productoporlista
            // ON producto.id = productoporlista.id_producto WHERE productoporlista.id_lista = id_lista;


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

    // Borra los productos que fueron eliminados desde el fragment de Productos
    public void deleteProductosDeListas(){
        List<ProductoPorLista> productosPorListas = SuperListaDbManager.getInstance().getAllProductosListas();
        ArrayList<ProductoPorLista> productosABorrar = new ArrayList<>();
        try {
            for (ProductoPorLista prod : productosPorListas) {
                if (prod.getProducto() == null){
                    productosABorrar.add(prod);
                }
            }
            getHelper().getProductoPorListaDao().delete(productosABorrar);
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void deleteProductosDeLista(ArrayList<ProductoPorLista>productoPorListas){
        try{
            getHelper().getProductoPorListaDao().delete(productoPorListas);
        } catch (SQLException e){
            e.printStackTrace();
        }

    }

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

    public void updateCantidadProductoLista(ProductoPorLista prod, int cantidad){
        try {
            prod.setCantidad(cantidad);
            getHelper().getProductoPorListaDao().update(prod);
        }catch (SQLException e){
            e.printStackTrace();
        }
    }


    // </editor-fold>


}
