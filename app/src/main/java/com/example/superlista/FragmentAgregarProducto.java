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
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.example.superlista.data.SuperListaDbManager;
import com.example.superlista.model.Categoria;
import com.example.superlista.model.Marca;
import com.example.superlista.model.Producto;
import com.example.superlista.model.Supermercado;



import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import static android.app.Activity.RESULT_OK;
import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;


public class FragmentAgregarProducto extends Fragment implements View.OnClickListener, TextView.OnEditorActionListener {

    Fragment fragmento = null;

    private List<Categoria> listCategorias;
    private List<Supermercado> listSupers;
    private List<Marca> marcas;

    private ArrayAdapter<String> adapterUnidad;
    private ArrayAdapter<Categoria> adapterCategoria;
    private ArrayAdapter<Marca> adapterMarca;
    private ArrayAdapter<Supermercado> adapterSuper;

    private static String APP_DIRECTORY = "SuperListaApp/";
    private static String MEDIA_DIRECTORY = APP_DIRECTORY + "PictureApp";

    private final int REQ_CODE_SPEECH_OUTPUT = 143;

    private final int MY_PERMISSIONS = 100; // constante que sirve para los permisos
    private final int PHOTO_CODE = 200; // sirve para cuando mandemos a llamar la aplicacion de fotos
    private final int SELECT_PICTURE = 300;

    private String mPath, unidad, direccion_imagen, nombreProducto, aux; //mPath lo voy a usar para saber en que ruta se guardo la imagen
    private Categoria categoria;
    private Supermercado supermercado;
    private Marca marca;

    private LinearLayout linearLayoutProd;
    private EditText nomProd, valorPrecio;
    private Button agregarProducto;
    private Spinner sCategoria, sMarca, sSupermercado, sUnidad;
    private ImageView imageProd, botonMicrofono;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.form_nuevo_producto, container, false);

        iniciarIU(view);  //metodo que relaciona la parte logica con la grafica


        return view;
    }


    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle(R.string.nueva_producto);
    }


    private void iniciarIU(View vista) {

        linearLayoutProd = (LinearLayout) vista.findViewById(R.id.linearProducto);

        botonMicrofono = (ImageView) vista.findViewById(R.id.imageViewMicrofono);
        botonMicrofono.setOnClickListener(this);

        nomProd = (EditText) vista.findViewById(R.id.editTextNombreProd);
        valorPrecio = (EditText) vista.findViewById(R.id.editTextValorPrecio);

        imageProd = (ImageView) vista.findViewById(R.id.imageViewFotoProd);
        imageProd.setOnClickListener(this);


        if (mayRequestStoragePermission()) {
            imageProd.setEnabled(true);
        } else {
            imageProd.setEnabled(false);
        }


        agregarProducto = (Button) vista.findViewById(R.id.buttonAgregarProd);
        agregarProducto.setOnClickListener(this);

        sUnidad = (Spinner) vista.findViewById(R.id.spinnerUnidad);
        sCategoria = (Spinner) vista.findViewById(R.id.spinnerCategoria);
        sMarca = (Spinner) vista.findViewById(R.id.spinnerMarca);
        sSupermercado = (Spinner) vista.findViewById(R.id.spinnerSuper);

        setSpinnerUnidad();
        setSpinnerCategoria();
        setSpinnerMarca();
        setSpinnerSupermercado();


    }

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

    @Override
    public void onClick(View v) {

        if (v == agregarProducto) {

            nombreProducto = nomProd.getText().toString();
            aux = nombreProducto.trim();

            if (aux.length() > 0) { //verificacion para que el campo nombre no sea vacio
                check_exist_addProducto(nombreProducto, marca);
            } else {
                Toast.makeText(getContext(), "El Nombre no puede estar vacio", Toast.LENGTH_SHORT).show();
            }

        } else if (v == imageProd) {

            mostrarOpciones();

        } else if (v == botonMicrofono) {
            btnOpenMic();
        }
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

    @Override
    //con el metodo onActivityResult manejamos las respuestas de la camara, galeria, speaker etc
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

                    imageProd.setImageBitmap(escaladoDeImagen(mPath));
                    break;

                case SELECT_PICTURE:

                    Uri path = data.getData();// en esta linea trae la ruta de la imagen pero en uri que es otro formato
                    String pathCompleto = getRealPathFromURI(path);

                    imageProd.setImageBitmap(escaladoDeImagen(pathCompleto));
                    break;

                case REQ_CODE_SPEECH_OUTPUT:
                    if (data != null) {
                        ArrayList<String> voiceInText = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                        nomProd.setText(voiceInText.get(0));
                    }
                    break;

            }
        }

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
    //</editor-fold>


    private String getRealPathFromURI(Uri contentURI) {// metodo que ve devuelve la ruta completa de la Uri
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







    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == MY_PERMISSIONS) {
            if (grantResults.length == 2 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getContext(), "Permisos Aceptados", Toast.LENGTH_SHORT).show();
                imageProd.setEnabled(true);
            }
        }
    }


    //<editor-fold desc="Verificaciones y agregar Producto">
    public void check_exist_addProducto(String nombre, Marca m) {
        Producto prod = SuperListaDbManager.getInstance().getProductoByNombre(nombre, m);
        if (prod != null) {
            final CharSequence[] opcion = {"OK"};
            final AlertDialog.Builder builder2 = new AlertDialog.Builder(getContext());
            builder2.setTitle("Ya hay registrado un producto de igual nombre");
            builder2.setItems(opcion, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (opcion[which] == "OK") {
                        dialog.dismiss();
                    }
                }
            });
            builder2.show();
        } else {
            unidad = sUnidad.getSelectedItem().toString();
            categoria = adapterCategoria.getItem(sCategoria.getSelectedItemPosition());
            marca = adapterMarca.getItem(sMarca.getSelectedItemPosition());
            supermercado = adapterSuper.getItem(sSupermercado.getSelectedItemPosition());
            double precio = Double.parseDouble(valorPrecio.getText().toString());

            Producto nuevoProd = new Producto(nombreProducto, marca, 0, 0, 0, 0, categoria, direccion_imagen, unidad);
            switch (supermercado.getId_supermercado()){
                case Supermercado.ID_COTO: nuevoProd.setPrecio_coto(precio); break;
                case Supermercado.ID_CARREFOUR: nuevoProd.setPrecio_carrefour(precio); break;
                case Supermercado.ID_LA_GALLEGA: nuevoProd.setPrecio_la_gallega(precio); break;
                case Supermercado.ID_OTRO: nuevoProd.setPrecio_otro(precio); break;
            }
            SuperListaDbManager.getInstance().addProducto(nuevoProd);
            Toast.makeText(getContext(), "Producto Agregado", Toast.LENGTH_SHORT).show();


            llamarFragmentProd();
        }
    }


    //</editor-fold>


    public void llamarFragmentProd() {

        fragmento = new FragmentProductos();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.contenedor, fragmento);
        ft.commit();
    }

    //<editor-fold desc="Spinners">
    private void setSpinnerUnidad() {
        adapterUnidad = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, Producto.UNIDADES);
        adapterUnidad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sUnidad.setAdapter(adapterUnidad);

    }

    private void setSpinnerCategoria() {
        listCategorias = SuperListaDbManager.getInstance().getAllCategorias();

        Comparator<Categoria>comparator = new Comparator<Categoria>() {
            @Override
            public int compare(Categoria s, Categoria t1) {
                // Pongo la categoria otros primera en la lsita
                if (s.getId_categoria() == Categoria.ID_CATEGORIA_OTROS){
                    return -1;
                }
                else if (t1.getId_categoria() == Categoria.ID_CATEGORIA_OTROS){
                    return 1;
                }
                return s.getNombre().compareToIgnoreCase(t1.getNombre());
            }
        };

        Collections.sort(listCategorias, comparator);


        adapterCategoria = new ArrayAdapter<>(getContext(), android.R.layout.simple_dropdown_item_1line, listCategorias);
        adapterCategoria.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sCategoria.setAdapter(adapterCategoria);
        // Asigno la opcion "otros" por defecto
        int pos = adapterCategoria.getPosition(SuperListaDbManager.getInstance().getCategoriaById(Categoria.ID_CATEGORIA_OTROS));
        sCategoria.setSelection(pos);

    }

    private void setSpinnerMarca() {
        marcas = SuperListaDbManager.getInstance().getAllMarcas();
        Comparator<Marca>comparator = new Comparator<Marca>() {
            @Override
            public int compare(Marca s, Marca t1) {
                if (s.getId_Marca() == Marca.ID_MARCA_NINGUNA){
                    return -1;
                }
                else if (t1.getId_Marca() == Marca.ID_MARCA_NINGUNA){
                    return 1;
                }
                return s.getNombre().compareToIgnoreCase(t1.getNombre());
            }
        };

        Collections.sort(marcas, comparator);

        Marca nuevaMarca = new Marca("Nueva Marca");
        marcas.add(0, nuevaMarca);

        adapterMarca = new ArrayAdapter<>(getContext(), android.R.layout.simple_dropdown_item_1line, marcas);
        adapterMarca.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sMarca.setAdapter(adapterMarca);

        // Asigno la opcion "ninguna" por defecto
        int pos = adapterMarca.getPosition(SuperListaDbManager.getInstance().getMarcaById(Marca.ID_MARCA_NINGUNA));
        sMarca.setSelection(pos);

        sMarca.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                marca = adapterMarca.getItem(sMarca.getSelectedItemPosition());
                //if (marcaSelected != null){}
                if (marca.getNombre().equalsIgnoreCase("Nueva Marca")) {

                    View v = LayoutInflater.from(getActivity()).inflate(R.layout.alert_dialog_add_marca, null);

                    final EditText mEditText = (EditText) v.findViewById(R.id.edit_text_marca);
                    final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setView(v);
                    builder.setTitle("Agregar Marca:")
                            .setPositiveButton("Agregar", null)//sobreescribo el metodo setPositiveButton para que no se cierre el alertDialog si se produce una invalidacion
                            .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.dismiss();
                                }
                            });
                    //builder.show();
                    final AlertDialog dialog = builder.create();

                    dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                        @Override
                        public void onShow(DialogInterface dialogInterface) {
                            Button positivo = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
                            positivo.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    String nuevaMarca = mEditText.getText().toString();
                                    Marca validMarca;
                                    if (nuevaMarca.trim().length() > 0) {
                                        // Busca en la base de datos si hay alguna marca con ese nombre
                                        validMarca = SuperListaDbManager.getInstance().getMarcaByNombre(nuevaMarca);
                                        if (validMarca != null) {
                                            Toast.makeText(getContext(), "Marca existente.", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Marca marcaNueva = new Marca(nuevaMarca);
                                            SuperListaDbManager.getInstance().addMarca(marcaNueva);
                                            marcas.add(0, marcaNueva);
                                            adapterMarca.notifyDataSetChanged();
                                            Toast.makeText(getContext(), "Marca agregada", Toast.LENGTH_SHORT).show();
                                            dialog.dismiss();
                                            sMarca.setSelection(0);
                                            //sMarca.setAdapter(adapterMarca);
                                        }

                                    } else {
                                        Toast.makeText(getContext(), "Debe colocar una Marca.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    });

                    dialog.show();

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    private void setSpinnerSupermercado() {
        listSupers = SuperListaDbManager.getInstance().getAllSupermercados();
        Comparator<Supermercado>comparator = new Comparator<Supermercado>() {
            @Override
            public int compare(Supermercado s, Supermercado t1) {
                if (s.getId_supermercado() == Supermercado.ID_OTRO){
                    return -1;
                }
                else if (t1.getId_supermercado() == Supermercado.ID_OTRO){
                    return 1;
                }
                return s.getNombre().compareToIgnoreCase(t1.getNombre());
            }
        };

        Collections.sort(listSupers, comparator);

        adapterSuper = new ArrayAdapter<>(getContext(), android.R.layout.simple_dropdown_item_1line, listSupers);
        adapterSuper.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sSupermercado.setAdapter(adapterSuper);

        // Asigno la opcion "otro" por defecto
        int pos = adapterSuper.getPosition(SuperListaDbManager.getInstance().getSupermercadoById(Supermercado.ID_OTRO));
        sSupermercado.setSelection(pos);

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


}
