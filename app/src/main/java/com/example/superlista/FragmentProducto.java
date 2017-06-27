package com.example.superlista;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.speech.RecognizerIntent;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
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
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.superlista.data.SuperListaDbManager;
import com.example.superlista.model.Categoria;
import com.example.superlista.model.Marca;
import com.example.superlista.model.Producto;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.app.Activity.RESULT_OK;

public class FragmentProducto extends Fragment implements TextView.OnEditorActionListener{

    private Producto producto;
    private TextView tvNombreProducto;
    private ImageView ivImagenProducto, ivMicrofono;
    private Spinner spMarcaProducto, spUnidadProducto, spCategoriaProducto;
    private EditText etNombreProducto, etPrecioProductoCoto, etPrecioProductoLaGallega, etPrecioProductoCarrefour, etPrecioProductoOtro;
    private Button buttonModificar, buttonCancelar;
    private MenuItem mEditItem;
    private ArrayAdapter<String> adapterUnidad;
    private ArrayAdapter<Marca> adapterMarcas;
    private ArrayAdapter<Categoria> adapterCategorias;
    private LinearLayout linearLayoutProd;

    private String mPath, direccion_imagen;
    private static String APP_DIRECTORY = "SuperListaApp/";
    private static String MEDIA_DIRECTORY = APP_DIRECTORY + "PictureApp";
    private final int MY_PERMISSIONS = 100; // constante que sirve para los permisos
    private final int PHOTO_CODE = 200; // sirve para cuando mandemos a llamar la aplicacion de fotos
    private final int SELECT_PICTURE = 300;
    private final int REQ_CODE_SPEECH_OUTPUT = 143;


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
        ivMicrofono = (ImageView) view.findViewById(R.id.imageViewModificarProdMicrofono);
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
        linearLayoutProd = (LinearLayout) view.findViewById(R.id.linearFragmentProducto);

        spUnidadProducto.setEnabled(false);
        spCategoriaProducto.setEnabled(false);
        spMarcaProducto.setEnabled(false);
        ivImagenProducto.setEnabled(false);

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
            ivMicrofono.setVisibility(View.VISIBLE);
        }else {
            tvNombreProducto.setVisibility(View.VISIBLE);
            etNombreProducto.setVisibility(View.INVISIBLE);
            buttonModificar.setVisibility(View.INVISIBLE);
            buttonCancelar.setVisibility(View.INVISIBLE);
            ivMicrofono.setVisibility(View.INVISIBLE);
        }

        if (mayRequestStoragePermission() && toggle){
            ivImagenProducto.setEnabled(true);
        } else {
            ivImagenProducto.setEnabled(false);
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
            ivImagenProducto.setImageURI(Uri.parse(producto.getImagen()));
            direccion_imagen = producto.getImagen();

            setSpinnerUnidad();
            setSpinnerMarcas();
            setSpinnerCategorias();
            setButtonsListeners();
            setImageListener();

        }

    }

    public void setImageListener(){
        ivImagenProducto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarOpciones();
            }
        });

        ivMicrofono.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnOpenMic();
            }
        });
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

    //<editor-fold desc="Spinners">
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
    //</editor-fold>

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
                    FragmentProductos fragmentProductos = new FragmentProductos();

                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.replace(R.id.contenedor, fragmentProductos);
                    ft.commit();
                }

            }
        });

        builder.show();

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
                            precioLaGallega, precioCarrefour, precioOtro, categoria, unidad, direccion_imagen);

                }catch (Exception e){
                    e.printStackTrace();
                }
                finally {
                    dialogInterface.dismiss();
                    FragmentProductos fragmentProductos = new FragmentProductos();

                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.replace(R.id.contenedor, fragmentProductos);
                    ft.addToBackStack("ProductoModificado");
                    getActivity().getSupportFragmentManager().popBackStack("Productos", FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    ft.commit();
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

    //<editor-fold desc="Imagen">
    private boolean mayRequestStoragePermission() {//con este metodo le otorgo los permisos de acuerdo a la version de android

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {// toda version menor a la 6.0 no necesita permisos, los mismo son agarrados del manifest

            return true;
        }
        if (((ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)) == PackageManager.PERMISSION_GRANTED)
                && ((ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA)) == PackageManager.PERMISSION_GRANTED)) {
            //si los permisos ya estan aceptados regresamos un true
            return true;
        }
        if ((shouldShowRequestPermissionRationale(WRITE_EXTERNAL_STORAGE)) || (shouldShowRequestPermissionRationale(CAMERA))) {
            Snackbar.make(linearLayoutProd, "Los permisos son nacesarios para poder usar la app", Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE, CAMERA}, MY_PERMISSIONS);
                        }
                    }).show();

        } else {
            requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE, CAMERA}, MY_PERMISSIONS);
        }

        return false;
    }

    private void mostrarOpciones() {

        final CharSequence[] opcion = {"-Tomar Foto", "-Elegir de Galeria", "-Cancelar"};
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Elije una Opción:");
        builder.setItems(opcion, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (opcion[which] == "-Tomar Foto") {
                    abrirCamara();
                } else if (opcion[which] == "-Elegir de Galeria") {
                    //ACTION_PICK tiene la opcion de abrir un archivo, y external content uri lanza el volumen de almacenamiento del dispositivo
                    Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*"); //aca le decimos que muestre todos los archivos de tipo imagen
                    startActivityForResult(intent.createChooser(intent, "Selecciona app de imágen:"), SELECT_PICTURE);
                } else {
                    dialog.dismiss(); // esta perteneceria a la opcion de cancelar
                }
            }

        });
        builder.show();

    }

    //<editor-fold desc="Escalado de la Imagen y Guardado en nueva Ruta">
    public Bitmap escaladoDeImagen(String ruta_imagen){

        //reescalo la imagen
        BitmapFactory.Options bmOptions = new BitmapFactory.Options(); //llamo al metodo de opciones de imagen
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(ruta_imagen, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        int factorEscala = Math.min(photoW/200, photoH/200);

        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = factorEscala;
        bmOptions.inPurgeable = true;

        Bitmap imagenBitmap = BitmapFactory.decodeFile(ruta_imagen, bmOptions);

        //Le asigno un nuevo nombre y lo guardo en su nueva ruta
        Long timestamp = System.currentTimeMillis() / 1000;
        String nombreImagen = timestamp.toString() + ".jpg";
        String nuevaRutaDestino = Environment.getExternalStorageDirectory() + File.separator + MEDIA_DIRECTORY + File.separator + nombreImagen;
        Uri.parse(nuevaRutaDestino);
        File archivo = new File(nuevaRutaDestino);

        FileOutputStream salida = null;
        try {
            salida = new FileOutputStream(archivo);
            imagenBitmap.compress(Bitmap.CompressFormat.JPEG, 100, salida);
            salida.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        direccion_imagen = nuevaRutaDestino;

        Log.i("Direccion Path ", "-> " + ruta_imagen);
        Log.i("Direccion Uri ", "-> " + nuevaRutaDestino);
        Log.i("Direccion Final ", "-> " + direccion_imagen);

        return imagenBitmap;
    }

    // Metodo que ve devuelve la ruta completa de la Uri
    private String getRealPathFromURI(Uri contentURI) {
        String resultado;
        Cursor cursor = getContext().getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) {
            resultado = contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            resultado = cursor.getString(idx);
            cursor.close();
        }
        return resultado;
    }
    //</editor-fold>

    private void abrirCamara() {

        File file = new File(Environment.getExternalStorageDirectory(), MEDIA_DIRECTORY); //file guarda toda la ruta del almacenamiento externo del dispositivo
        boolean isDirectoryCreated = file.exists(); //esto nos va a decir si PictureApp ya esta creado

        if (!isDirectoryCreated) {
            isDirectoryCreated = file.mkdirs();
        }

        if (isDirectoryCreated) {
            Long timestamp = System.currentTimeMillis() / 1000; //con esto obtengo la fecha
            String imageName = timestamp.toString() + ".jpg"; //aca asigno el nombre a la imagen y le pongo la extension jpg

            //aca le estamos diciendo explicitamente donde queremos que se guarde nuestra imagen
            //seria  SuperListaApp/PictureApp/imageName
            mPath = Environment.getExternalStorageDirectory() + File.separator + MEDIA_DIRECTORY + File.separator + imageName;

            File newFile = new File(mPath);

            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);//lanzamos en intent para abrir la camara
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(newFile));//le enviamos un uri a la aplicacion de camara y del file sacamos el uri
            startActivityForResult(intent, PHOTO_CODE);
        }

    }
    //</editor-fold>

    //<editor-fold desc="Voice to text">
    private void btnOpenMic() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Hable ahora...");

        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_OUTPUT);
        } catch (ActivityNotFoundException anfe) {
            anfe.printStackTrace();
        }
    }

    @Override
    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
        return false;
    }


    //</editor-fold>

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {//el RESULT_OK significa si la respuesta que nos llega esta bien o sea sin errores
            switch (requestCode) {
                case PHOTO_CODE:
                    //el Scan lo que hace es que una vez que guarda la imagen, escanea la ruta ya que sino la misma no apararece en la galeria
                    MediaScannerConnection.scanFile(
                            getContext(),
                            new String[]{mPath},
                            null,
                            new MediaScannerConnection.OnScanCompletedListener() {
                                @Override
                                public void onScanCompleted(String path, Uri uri) {
                                    //estos logs son meramente informativos
                                    Log.i("Almacenamiento Externo", "Escaneada " + path);
                                    Log.i("Almacenamiento Externo", "-> Uri = " + uri);

                                }
                            });

                    ivImagenProducto.setImageBitmap(escaladoDeImagen(mPath));
                    break;

                case SELECT_PICTURE:

                    Uri path = data.getData();// en esta linea trae la ruta de la imagen pero en uri que es otro formato
                    String pathCompleto = getRealPathFromURI(path);

                    ivImagenProducto.setImageBitmap(escaladoDeImagen(pathCompleto));
                    break;

                case REQ_CODE_SPEECH_OUTPUT:
                    if (data != null) {
                        ArrayList<String> voiceInText = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                        etNombreProducto.setText(voiceInText.get(0));
                    }
                    break;

            }
        }

    }
}
