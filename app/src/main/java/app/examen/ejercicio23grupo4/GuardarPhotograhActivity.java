package app.examen.ejercicio23grupo4;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import app.examen.ejercicio23grupo4.configuracion.BaseDatos;
import de.hdodenhof.circleimageview.CircleImageView;

public class GuardarPhotograhActivity extends AppCompatActivity {


    //View
    private CircleImageView perfilImagen;
    private EditText nameEt;
    private Button btnSalvar;

    //Actionbar
    private ActionBar actionBar;
    //Permiso de la clase Transacciones registros
    private  static final int CAMERA_REQUEST_CODE = 100;
    private static final int STORAGE_REQUEST_CODE = 101;
    //selección de imagen
    private static final int IMAGE_PICK_CAMERA_CODE = 102;
    private static final int IMAGE_PICK_GALLERY_CODE = 103;
    // matrices de permisos
    private String[] cameraPermissions; // cámara y almacenamiento
    private String [] storagePermissions;// solo almacenamiento
    // variables (datos para guardar)
    private Uri imageUri; //toma la direccion de la imagen la url
    private String id, descripcion, addedTime, updatedTime;

    private boolean isEditMode = false;


    //db helper
    private BaseDatos dbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guardar_foto);
        //Inicializacion de los componentes
        actionBar = getSupportActionBar();
        //Titulo
        actionBar.setTitle("Agregar Registro");
        //Boton Negro de retorno
        actionBar.setDisplayHomeAsUpEnabled(true);
        //evita volver atras al hacer clic en el titulo
        actionBar.setDisplayShowHomeEnabled(true);

        perfilImagen = findViewById(R.id.perfilImagen);
         nameEt = findViewById(R.id.nameEt);

         btnSalvar = findViewById(R.id.btnAgregar);

        //obtener los datos de la intencion
        Intent intent = getIntent();
        isEditMode = intent.getBooleanExtra("isEditMode", false);

        //establer la vista de los datos
        if (isEditMode) {

            //Actualizar datos
            actionBar.setTitle("Actualizar Registro");

            id = intent.getStringExtra("ID");
            descripcion = intent.getStringExtra("NAME");
            imageUri = Uri.parse(intent.getStringExtra("IMAGE"));
            addedTime = intent.getStringExtra("ADDEDTIME");
            updatedTime = intent.getStringExtra("UPDATEDTIME");

            //set View data
            nameEt.setText(descripcion);

            //sino se selecciona una imagen al agregr datos; el valor de la imagen ser "NULL"
            if (imageUri.toString().equals("null")) {
                //sino ahi imagen , set default
                perfilImagen.setImageResource(R.drawable.ic_person_black);
            } else {

                perfilImagen.setImageURI(imageUri);
            }

        }
        else {
            //agregar datos
            actionBar.setTitle("Agregar Registro");
        }

        //Inicializar BD Helper
        dbHelper = new BaseDatos(this);

        //Inicializamos Permisos arrays
        cameraPermissions = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};

        perfilImagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // muestra el cuadro de diálogo de selección de imagen
                imagePickDialog();
            }
        });

        btnSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputData();
            }
        });
    }

    private void inputData(){
        //get data
        descripcion = ""+nameEt.getText().toString().trim();

        if (isEditMode){
            //actualizar datos

            String timestamp = ""+System.currentTimeMillis();
            dbHelper.updateRecord(
                    ""+id,
                    ""+descripcion,
                    ""+imageUri,
                    ""+addedTime,//este dato no cambia fecha registro
                    ""+timestamp//Fecha de actualizacion cambia

            );

            Toast.makeText(this, "Actualizando... ", Toast.LENGTH_SHORT).show();

        }else{
            //new datos
            //guarda en la base de datos
            if(validar()==true){

                String timestamp = ""+System.currentTimeMillis();
                long id = dbHelper.insertImagen(
                        ""+descripcion,
                        ""+imageUri,
                        ""+timestamp,
                        ""+timestamp
                );

                Toast.makeText(this, "Registro agregado contra ID: "+id, Toast.LENGTH_SHORT).show();

            }
             }
    }

    public boolean validar() {
        boolean retorno = true;

        String descri = nameEt.getText().toString();


        if (descri.isEmpty()) {
            nameEt.setError("Debe de ingresar la descripcion de la imagen");
            retorno = false;
        }
        return retorno;
    }


    private void imagePickDialog(){
        // opciones para mostrar en el diálogo
        String[] options = {"Camara", "Galeria"};
        //dialogo
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //Titulo
        builder.setTitle("Seleccionar imagen");
        // establecer elementos / opciones
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                // manejar clicks
                if (i==0){
                    //click en camara
                    if (!checkCameraPermission()){
                        requestCameraPermission();
                    }
                    else{
                        // permiso ya otorgado
                        PickFromCamera();
                    }

                }
                else if (i==1){
                    if (!checkStoragePermission()){
                        requestStoragePermission();
                    }
                    else{
                        // permiso ya otorgado
                        PickFromGallery();
                    }
                }
            }
        });

        // Crear / mostrar diálogo
        builder.create().show();
    }

    private void PickFromGallery() {
        // intento de elegir la imagen de la galería, la imagen se devolverá en el método onActivityResult
        Intent galleryIntent = new Intent(Intent.ACTION_PICK);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, IMAGE_PICK_GALLERY_CODE);
    }

    private void PickFromCamera() {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "Titulo de la Imagen");
        values.put(MediaStore.Images.Media.DESCRIPTION, "Descripción de la imagen");
        //put image Uri
        imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        // Intento de abrir la cámara para la imagen
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(cameraIntent, IMAGE_PICK_CAMERA_CODE);
    }

    private boolean checkStoragePermission(){
        //comprobar si el permiso de almacenamiento está habilitado o no
        boolean result = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);

        return result;
    }

    private  void requestStoragePermission(){
        // solicita el permiso de almacenamiento
        ActivityCompat.requestPermissions(this, storagePermissions, STORAGE_REQUEST_CODE);
    }

    private boolean checkCameraPermission(){
        // verifica si el permiso de la cámara está habilitado o no
        boolean result = ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA) == (PackageManager.PERMISSION_GRANTED);
        boolean result1 = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);

        return result && result1;
    }

    private void requestCameraPermission(){
        // solicita el permiso de la cámara
        ActivityCompat.requestPermissions(this, cameraPermissions, CAMERA_REQUEST_CODE);
    }

    @Override
    public boolean onSupportNavigateUp(){
        onBackPressed(); //regrese haciendo clic en el botón de barra de acción
        return super.onSupportNavigateUp();
    }

    //resultado de los permisos
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // resultado del permiso permitido / denegado

        switch (requestCode){
            case CAMERA_REQUEST_CODE:{
                if (grantResults.length>0){

                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean storageAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                    if(cameraAccepted && storageAccepted){
                        // ambos permisos permitidos
                        PickFromCamera();
                    }
                    else{
                        Toast.makeText(this, "Se requieren permisos de cámara y almacenamiento", Toast.LENGTH_SHORT).show();
                    }
                }

            }
            break;
            case STORAGE_REQUEST_CODE:{
                if (grantResults.length>0){

                    // si se permite devolver verdadero de lo contrario falso
                    boolean storageAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if (storageAccepted){
                        // permiso de almacenamiento permitido
                        PickFromGallery();
                    }
                    else{
                        Toast.makeText(this, "Se requiere permiso de almacenamiento", Toast.LENGTH_SHORT).show();
                    }
                }

            }
            break;
        }
    }
    //recorte libreria
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        //la imagen seleccionada de la cámara o la galería se recibirá aqui
        if (resultCode == RESULT_OK){
            //Se elige la imagen
            if(requestCode == IMAGE_PICK_GALLERY_CODE){
                //Picked from gallery

                //delimitar imagen se anada al circle image
                CropImage.activity(data.getData())
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setAspectRatio(1, 1)
                        .start(this);

            }
            else if(requestCode == IMAGE_PICK_CAMERA_CODE){
                //Elegido de la cámara
                //crop Image
                CropImage.activity(imageUri)
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setAspectRatio(1, 1)
                        .start(this);

            }
            else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
                //Imagen recortada recibida
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                if (resultCode == RESULT_OK){
                    Uri resultUri = result.getUri();
                    imageUri = resultUri;
                    //set Image
                    perfilImagen.setImageURI(resultUri);
                }
                else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE){
                    //ERROR
                    Exception error = result.getError();
                    Toast.makeText(this, ""+error, Toast.LENGTH_SHORT).show();
                }

            }

        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}
