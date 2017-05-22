package com.example.superlista;



import android.Manifest;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
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
import com.example.superlista.model.Producto;
import com.example.superlista.model.Supermercado;


import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import static android.app.Activity.RESULT_OK;
import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;


public class FragmentAgregarProducto extends Fragment implements View.OnClickListener, TextView.OnEditorActionListener{

    Fragment fragmento = null;

    private List<Categoria> listCategorias;
    private List<Producto> listProductos;
    private List<Producto> listProductos2;
    private List<Supermercado> listSupers;

    private ArrayList<String> nombresCategorias, nombresMarcas, nombresSupers, nombresUnidades;

    private ArrayAdapter<String> adapterUnidad;
    private ArrayAdapter adapterCategoria;
    private ArrayAdapter<String> adapterMarca;
    private ArrayAdapter<String> adapterSuper ;

    HashSet<String> hs;

    private static String APP_DIRECTORY = "SuperListaApp/";
    private static String MEDIA_DIRECTORY = APP_DIRECTORY + "PictureApp";

    private final int REQ_CODE_SPEECH_OUTPUT = 143;

    private final int MY_PERMISSIONS = 100; // constante que sirve para los permisos
    private final int PHOTO_CODE = 200; // sirve para cuando mandemos a llamar la aplicacion de fotos
    private final int SELECT_PICTURE = 300;

    private String cadCategoria, cadUnidad, cadMarca, cadSuper, mPath, direccion_imagen, nom, aux;    //mPath lo voy a usar para saber en que ruta se guardo la imagen
    private Categoria categoria;
    private Supermercado supermercado;
    private boolean validacionProd = true;

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



    private void iniciarIU(View vista){


        linearLayoutProd = (LinearLayout) vista.findViewById(R.id.linearProducto);

        botonMicrofono = (ImageView) vista.findViewById(R.id.imageViewMicrofono);
        botonMicrofono.setOnClickListener(this);

        nomProd =  (EditText) vista.findViewById(R.id.editTextNombreProd);
        valorPrecio =  (EditText) vista.findViewById(R.id.editTextValorPrecio);

        imageProd = (ImageView) vista.findViewById(R.id.imageViewFotoProd);
        imageProd.setOnClickListener(this);


        if (mayRequestStoragePermission()){
            imageProd.setEnabled(true);
        }else{
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

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M){// toda version menor a la 6.0 no necesita permisos, los mismo son agarrados del manifest

            return true;
        }
        if (((ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)) == PackageManager.PERMISSION_GRANTED)
                && ((ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA)) == PackageManager.PERMISSION_GRANTED)){
                //si los permisos ya estan aceptados regresamos un true
            return true;
         }
         if((shouldShowRequestPermissionRationale(WRITE_EXTERNAL_STORAGE)) || (shouldShowRequestPermissionRationale(CAMERA))){
             Snackbar.make(linearLayoutProd, "Los permisos son nacesarios para poder usar la app", Snackbar.LENGTH_INDEFINITE)
                     .setAction(android.R.string.ok, new View.OnClickListener() {
                         @Override
                         public void onClick(View v) {
                             requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE, CAMERA}, MY_PERMISSIONS);
                         }
                     }).show();

         }else{
             requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE, CAMERA}, MY_PERMISSIONS);
         }

       return false;
    }


    @Override
    public void onClick(View v) {

        if (v == agregarProducto){

            nom = nomProd.getText().toString();
            aux= nom.trim();

            if (aux.length() > 0){ //verificacion para que el campo nombre no sea vacio

                check_exist_addProducto(
                        nom,
                        cadMarca,
                        Double.parseDouble(valorPrecio.getText().toString()),
                        categoria,
                        supermercado,
                        direccion_imagen,
                        cadUnidad
                );
            }else{
                Toast.makeText(getContext(), "El Nombre no puede estar vacio", Toast.LENGTH_SHORT).show();
            }

        }else if(v == imageProd){

            mostrarOpciones();

        }else if(v == botonMicrofono){
            btnOpenMic();
        }
    }



    private void mostrarOpciones(){

        final CharSequence[] opcion = {"-Tomar Foto", "-Elegir de Galeria", "-Cancelar"};
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Elije una Opción:");
        builder.setItems(opcion, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (opcion[which] == "-Tomar Foto"){
                    abrirCamara();
                }else if(opcion[which] == "-Elegir de Galeria"){
                    //ACTION_PICK tiene la opcion de abrir un archivo, y external content uri lanza el volumen de almacenamiento del dispositivo
                    Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*"); //aca le decimos que muestre todos los archivos de tipo imagen
                    startActivityForResult(intent.createChooser(intent, "Selecciona app de imágen:"), SELECT_PICTURE);
                }else{
                    dialog.dismiss(); // esta perteneceria a la opcion de cancelar
                }
            }

        });
        builder.show();

    }

    private void abrirCamara() {

        File file = new File(Environment.getExternalStorageDirectory(), MEDIA_DIRECTORY); //file guarda toda la ruta del almacenamiento externo del dispositivo
        boolean isDirectoryCreated = file.exists(); //esto nos va a decir si PictureApp ya esta creado

        if (!isDirectoryCreated){
            isDirectoryCreated = file.mkdirs();
        }

        if (isDirectoryCreated){
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

        if (resultCode == RESULT_OK){//el RESULT_OK significa si la respuesta que nos llega esta bien o sea sin errores
            switch (requestCode){
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
                                    Log.i("Almacenamiento Externo", "Escaneada " + path );
                                    Log.i("Almacenamiento Externo", "-> Uri = " + uri );
                                    direccion_imagen = uri.toString();
                                }
                            });

                    //una vez escaneada la hay que ponerlo en el imageview
                    Bitmap bitmap = BitmapFactory.decodeFile(mPath); // lo que hace esta linea es traer la ruta en donde esta la imagen y la decodifica y la guarda en un bitmap
                    imageProd.setImageBitmap(bitmap);
                    Log.i("mPath", "Escaneada " + mPath );

                    break;

                case SELECT_PICTURE:
                    Uri path = data.getData();// en esta linea trae la ruta de la imagen pero en uri que es otro formato
                    imageProd.setImageURI(path);
                    Log.i("SELECT_PICTURE ", "-> Uri = " + path);

                    direccion_imagen = path.toString();

                    break;

                case REQ_CODE_SPEECH_OUTPUT:
                    if (data != null){
                        ArrayList<String> voiceInText = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                        nomProd.setText(voiceInText.get(0));
                    }
                    break;

            }
        }

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == MY_PERMISSIONS){
            if (grantResults.length == 2 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(getContext(), "Permisos Aceptados", Toast.LENGTH_SHORT).show();
                imageProd.setEnabled(true);
            }
        }
    }



    //<editor-fold desc="Verificaciones y agregar Producto">
    public void check_exist_addProducto(String nombre, String marca, double precio, Categoria categoria, Supermercado supermercado, String imagen, String unidad){

       listProductos =  SuperListaDbManager.getInstance().getAllProductos();

       for (Producto listaProd: listProductos) {

           if(Objects.equals(listaProd.getNombre(), nombre)
                   && Objects.equals(listaProd.getMarca(), marca)
                   && Objects.equals(listaProd.getPrecio(), precio)
                   && Objects.equals(listaProd.getCategoria(), categoria)
                   && Objects.equals(listaProd.getSupermercado(), supermercado)
                   && Objects.equals(listaProd.getUnidad(), unidad)){

               final CharSequence[] opcion = {"OK"};
               final AlertDialog.Builder builder2 = new AlertDialog.Builder(getContext());
               builder2.setTitle("Ya hay registrado un producto igual");
               builder2.setItems(opcion, new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialog, int which) {
                       if (opcion[which] == "OK") {
                           dialog.dismiss();
                       }
                   }
               });
               builder2.show();
               validacionProd = true;
               break;
           }else{
               validacionProd = false;
           }
       }

       if (!validacionProd) {

           Producto nuevoProd = new Producto(nombre, marca, precio, categoria, supermercado, imagen, unidad);
           SuperListaDbManager.getInstance().addProducto(nuevoProd);
           Toast.makeText(getContext(), "Producto Agregado", Toast.LENGTH_SHORT).show();

           //TODO: no me gusta como esta implementado limpiarCampos aca, hay que ver si limpiamos los campos para que cargue otro o lo mandamos directamente al FagmentProdutos
           //limpiarCampos();
           llamarFragmentProd();

       }

   }
    //</editor-fold>


    public void limpiarCampos(){//metodo que setea todos los campos nuevamente por default

        nomProd.getText().clear();
        valorPrecio.getText().clear();
        imageProd.setImageResource(R.drawable.addimage128);
        setSpinnerUnidad();
        setSpinnerMarca();
        setSpinnerCategoria();
        setSpinnerSupermercado();

    }

    public void llamarFragmentProd(){

        fragmento = new FragmentProductos();
        FragmentTransaction ft =  getFragmentManager().beginTransaction();
        ft.replace(R.id.contenedor, fragmento);
        ft.commit();
    }


    //<editor-fold desc="Spinner Unidad">
    private void setSpinnerUnidad(){

        nombresUnidades = new ArrayList<String>();

        listProductos2 =  SuperListaDbManager.getInstance().getAllProductos();

        for (Producto listaUnid: listProductos2) {

            nombresUnidades.add(listaUnid.getUnidad());
        }

        acomodarArrayList(nombresUnidades);

        adapterUnidad = new ArrayAdapter(getContext(), android.R.layout.simple_dropdown_item_1line, nombresUnidades);
        adapterUnidad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sUnidad.setAdapter(adapterUnidad);


        sUnidad.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                cadUnidad = String.valueOf(sUnidad.getSelectedItem());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }
    //</editor-fold>

    //<editor-fold desc="Spinner Categoria">
    private void setSpinnerCategoria(){

        nombresCategorias = new ArrayList<String>();

        listCategorias = SuperListaDbManager.getInstance().getAllCategorias();

        for (Categoria listaCat: listCategorias) {

            nombresCategorias.add(listaCat.getNombre());
        }
        Collections.sort(nombresCategorias);

        adapterCategoria = new ArrayAdapter(getContext(), android.R.layout.simple_dropdown_item_1line, nombresCategorias);
        adapterCategoria.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sCategoria.setAdapter(adapterCategoria);

        sCategoria.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                cadCategoria = String.valueOf(sCategoria.getSelectedItem());
                categoria = SuperListaDbManager.getInstance().getCategoriaByNombre(cadCategoria);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }
    //</editor-fold>

    //<editor-fold desc="Spinner Marca">
    private void setSpinnerMarca(){

        nombresMarcas = new ArrayList<String>();

        listProductos =  SuperListaDbManager.getInstance().getAllProductos();

        for (Producto listaMar: listProductos) {

            nombresMarcas.add(listaMar.getMarca());
        }

        acomodarArrayList(nombresMarcas);
        nombresMarcas.add(1, "Nueva Marca");

        adapterMarca = new ArrayAdapter(getContext(), android.R.layout.simple_dropdown_item_1line, nombresMarcas);
        adapterMarca.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sMarca.setAdapter(adapterMarca);


        sMarca.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                cadMarca = String.valueOf(sMarca.getSelectedItem());
                if (cadMarca == "Nueva Marca"){

                    //dialogoPersonalizado();

                    //Toast.makeText(getContext(), "Hola pianola", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }
    //</editor-fold>

    //<editor-fold desc="Acomodar ArrayList">
    private void acomodarArrayList (ArrayList<String> arrayList){

        //elimino elementos duplicados del ArrayList
        hs = new HashSet<String>();
        hs.addAll(arrayList);
        arrayList.clear();
        arrayList.add(0, "-Otro-");
        arrayList.addAll(hs);

        //ordeno alfabeticamente los elementos del ArrayList
        Collections.sort(arrayList);

        //elimino campos vacios del ArrayList
        for (int i = 0; i < arrayList.size(); i++) {

            if(arrayList.get(i).trim().length() == 0){
                arrayList.remove(i);
            }
        }
    }
    //</editor-fold>

    //<editor-fold desc="Spinner Supermercado">
    private void setSpinnerSupermercado(){

        nombresSupers = new ArrayList<String>();

        listSupers = SuperListaDbManager.getInstance().getAllSupermercados();

        for (Supermercado listaSup: listSupers) {

            nombresSupers.add(listaSup.getNombre());
        }
        Collections.sort(nombresSupers);

        adapterSuper = new ArrayAdapter(getContext(), android.R.layout.simple_dropdown_item_1line, nombresSupers);
        adapterSuper.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sSupermercado.setAdapter(adapterSuper);

        sSupermercado.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                cadSuper = String.valueOf(sSupermercado.getSelectedItem());
                supermercado = SuperListaDbManager.getInstance().getSupermercadoByNombre(cadSuper);
               /* for (Supermercado listaSup: listSupers) {
                    if (listaSup.getNombre().equals(cadSuper)){
                        numberIDsup =  listaSup.getId_supermercado();
                        break;
                    }
                }*/
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }
    //</editor-fold>

    //<editor-fold desc="Voice to text">
    private void btnOpenMic(){
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Hable ahora...");


        try{
            startActivityForResult(intent, REQ_CODE_SPEECH_OUTPUT);
        } catch (ActivityNotFoundException anfe){
            anfe.printStackTrace();
        }
    }

    @Override
    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
        return false;
    }


    //</editor-fold>

    /*
    public void dialogoPersonalizado() {
        FragmentAlertDialogAddMarca dialogoPersonalizado = new FragmentAlertDialogAddMarca();
        dialogoPersonalizado.show(getFragmentManager(), "personalizado");

        Fragment frag = getFragmentManager().findFragmentByTag("personalizado");

        if (frag != null) {
            getFragmentManager().beginTransaction().remove(frag).commit();
        }
    }

    @Override
    public void FinalizaCuadroDialogo(String texto) {
        Toast.makeText(getContext(), texto ,Toast.LENGTH_SHORT).show();
    }

    */
}
