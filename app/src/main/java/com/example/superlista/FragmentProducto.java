package com.example.superlista;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.superlista.data.SuperListaDbManager;
import com.example.superlista.model.Categoria;
import com.example.superlista.model.Marca;
import com.example.superlista.model.Producto;
import com.example.superlista.model.Supermercado;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FragmentProducto extends Fragment {

    private Producto producto;
    private TextView tvNombreProducto;
    private ImageView ivImagenProducto;
    private Spinner spMarcaProducto, spUnidadProducto, spCategoriaProducto;
    private EditText etNombreProducto, etPrecioProductoCoto, etPrecioProductoLaGallega, etPrecioProductoCarrefour, etPrecioProductoOtro;
    private Button buttonModificar, buttonCancelar;
    private MenuItem mEditItem;
    private ArrayAdapter<String> adapterUnidad;
    private ArrayAdapter<Marca> adapterMarcas;
    private ArrayAdapter<Categoria> adapterCategorias;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_producto, container, false);
        inicializarComponentes(view);
        setData();
        setHasOptionsMenu(true);
        return view;
    }

    public void inicializarComponentes(View view){
        tvNombreProducto = (TextView) view.findViewById(R.id.textViewModificarNombreProd);
        ivImagenProducto = (ImageView) view.findViewById(R.id.imageViewModificarFotoProd);
        etNombreProducto = (EditText) view.findViewById(R.id.editTextModificarNombreProd);
        spMarcaProducto = (Spinner) view.findViewById(R.id.spinnerModificarMarca);
        spUnidadProducto = (Spinner) view.findViewById(R.id.spinnerModificarUnidad);
        spCategoriaProducto = (Spinner) view.findViewById(R.id.spinnerModifcarCategoria);
        etPrecioProductoCoto = (EditText) view.findViewById(R.id.editTextValorPrecioCoto);
        etPrecioProductoCarrefour = (EditText) view.findViewById(R.id.editTextValorPrecioCarrefour);
        etPrecioProductoLaGallega = (EditText) view.findViewById(R.id.editTextValorPrecioLaGallega);
        etPrecioProductoOtro = (EditText) view.findViewById(R.id.editTextValorPrecioOtro);
        buttonModificar = (Button) view.findViewById(R.id.buttonModificarProd);
        buttonCancelar = (Button) view.findViewById(R.id.buttonCancelarModifProd);

        spUnidadProducto.setEnabled(false);
        spCategoriaProducto.setEnabled(false);
        spMarcaProducto.setEnabled(false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle(producto.toString());
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_productos, menu);
        mEditItem = menu.findItem(R.id.action_editar_producto);
        mEditItem.setVisible(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_eliminar_producto: eliminarProducto(item); return true;
            case R.id.action_editar_producto: modoEdicion(true); return true;
            default: return super.onOptionsItemSelected(item);
        }

    }

    public void modoEdicion(boolean toggle){
        spUnidadProducto.setEnabled(toggle);
        spCategoriaProducto.setEnabled(toggle);
        spMarcaProducto.setEnabled(toggle);
        etPrecioProductoLaGallega.setEnabled(toggle);
        etPrecioProductoCoto.setEnabled(toggle);
        etPrecioProductoCarrefour.setEnabled(toggle);
        etPrecioProductoOtro.setEnabled(toggle);
        if (toggle) {
            tvNombreProducto.setVisibility(View.INVISIBLE);
            etNombreProducto.setVisibility(View.VISIBLE);
            buttonModificar.setVisibility(View.VISIBLE);
            buttonCancelar.setVisibility(View.VISIBLE);
        }else {
            tvNombreProducto.setVisibility(View.VISIBLE);
            etNombreProducto.setVisibility(View.INVISIBLE);
            buttonModificar.setVisibility(View.INVISIBLE);
            buttonCancelar.setVisibility(View.INVISIBLE);
        }


    }

    public void setData(){
        producto = getArguments().getParcelable("producto");
        if (producto != null) {
            tvNombreProducto.setText(producto.toString());
            etNombreProducto.setText(producto.getNombre());
            etPrecioProductoCoto.setText(String.valueOf(producto.getPrecio_coto()));
            etPrecioProductoLaGallega.setText(String.valueOf(producto.getPrecio_la_gallega()));
            etPrecioProductoCarrefour.setText(String.valueOf(producto.getPrecio_carrefour()));
            etPrecioProductoOtro.setText(String.valueOf(producto.getPrecio_otro()));

            setSpinnerUnidad();
            setSpinnerMarcas();
            setSpinnerCategorias();
            setButtonsListeners();

        }

    }

    public void setButtonsListeners(){
        buttonCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                modoEdicion(false);
            }
        });

        buttonModificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                modificarProducto();
            }
        });
    }

    private void setSpinnerUnidad(){
        adapterUnidad = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, Producto.UNIDADES);
        adapterUnidad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spUnidadProducto.setAdapter(adapterUnidad);

        String unidad = producto.getUnidad();
        int pos = adapterUnidad.getPosition(unidad);
        spUnidadProducto.setSelection(pos);
    }

    private void setSpinnerCategorias(){
        List<Categoria> categorias = SuperListaDbManager.getInstance().getAllCategorias();
        adapterCategorias = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, categorias);
        adapterCategorias.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCategoriaProducto.setAdapter(adapterCategorias);

        Categoria categoria = producto.getCategoria();

        int pos = adapterCategorias.getPosition(categoria);
        spCategoriaProducto.setSelection(pos);

    }

    private void setSpinnerMarcas(){
        List<Marca> marcas = SuperListaDbManager.getInstance().getAllMarcas();
        adapterMarcas = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, marcas);
        adapterMarcas.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spMarcaProducto.setAdapter(adapterMarcas);

        Marca marca = producto.getMarca();
        int pos = adapterMarcas.getPosition(marca);
        spMarcaProducto.setSelection(pos);

    }

    public void eliminarProducto(MenuItem item){
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage("Desea eliminar el producto "+producto.toString()+"?");
        builder.setTitle("Eliminar Producto");

        builder.setPositiveButton("Eliminar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                try {
                    SuperListaDbManager.getInstance().deleteProducto(producto);
                }catch (Exception e){
                    e.printStackTrace();
                }finally {
                    dialogInterface.dismiss();
                    // TODO: 24/05/2017 Mandar a framgent productos
                }

            }
        });

    }

    public void modificarProducto(){
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage("Desea modificar el producto "+producto.toString()+"?");
        builder.setTitle("Modificar Producto");

        builder.setPositiveButton("Modificar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                double precioCoto,precioLaGallega,precioCarrefour,precioOtro;
                precioCoto = Double.parseDouble(etPrecioProductoCoto.getText().toString());
                precioLaGallega = Double.parseDouble(etPrecioProductoLaGallega.getText().toString());
                precioCarrefour = Double.parseDouble(etPrecioProductoCarrefour.getText().toString());
                precioOtro = Double.parseDouble(etPrecioProductoOtro.getText().toString());

                String unidad = spUnidadProducto.getSelectedItem().toString();
                Marca marca = adapterMarcas.getItem(spMarcaProducto.getSelectedItemPosition());
                Categoria categoria = adapterCategorias.getItem(spCategoriaProducto.getSelectedItemPosition());
                String nombre = etNombreProducto.getText().toString();

                try {
                    SuperListaDbManager.getInstance().updateProducto(producto.getId_producto(), nombre, marca, precioCoto,
                            precioLaGallega, precioCarrefour, precioOtro, categoria, unidad, "imagen");

                }catch (Exception e){
                    e.printStackTrace();
                }
                finally {
                    // TODO: 24/05/2017 Mandar a framgent productos
                    dialogInterface.dismiss();
                }
            }
        });

        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });

        builder.show();


    }

}
