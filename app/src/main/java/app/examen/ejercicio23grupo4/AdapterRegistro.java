package app.examen.ejercicio23grupo4;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import app.examen.ejercicio23grupo4.Tablas.ModeloRegistro;
import app.examen.ejercicio23grupo4.configuracion.BaseDatos;

public class AdapterRegistro extends RecyclerView.Adapter<AdapterRegistro.HolderRecord>{

    //Variables
    private Context context;
    private ArrayList<ModeloRegistro> recordsList;

    //Base de datos
    BaseDatos dbHelper;

    //Constructor
    public AdapterRegistro(Context context, ArrayList<ModeloRegistro> recordsList){
        this.context = context;
        this.recordsList = recordsList;
        dbHelper = new BaseDatos(context);
    }

    @NonNull
    @Override
    public HolderRecord onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //inflate layout
        View view = LayoutInflater.from(context).inflate(R.layout.mostrar_registro, parent, false);

        return new HolderRecord(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderRecord holder, @SuppressLint("RecyclerView") final int position) {

        // obtener datos, establecer datos, ver clics en el método

        //Obtener datos
        ModeloRegistro model = recordsList.get(position);
        final String id = model.getId();
        String descripcion = model.getDescripcion();
        String image = model.getImage();
        String addedTime = model.getAddedTime();
        String updatedTime = model.getUpdatedTime();

        //Establecer Datos
        holder.descripcionTv.setText(descripcion);


        // si el usuario no adjunta la imagen, imageUri será nulo, por lo tanto,
        // configure una imagen predeterminada en ese caso
        if (image.equals("null")){
            // no hay imagen en el registro, establecer predeterminado
            holder.perfilImagen.setImageResource(R.drawable.ic_person_black);
        }
        else {
            // tener imagen en el registro
            holder.perfilImagen.setImageURI(Uri.parse(image));
        }


        // manejar clicks de elementos (ir a la actividad de registro de detalles)

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Pass record id to next activity to show details of thet record
                Intent intent = new Intent(context, DetalleRegistroActivity.class);
                intent.putExtra("RECORD_ID", id);
                context.startActivity(intent);
            }
        });

        //manejar clicks de botones (mostrar opciones como editar, eliminar)

        holder.moreBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //mostrar el menu de opciones
                showMoreDialog(  ""+position,
                        ""+id,
                        ""+descripcion,
                        ""+image,
                        ""+addedTime,
                        ""+updatedTime);
            }
        });
    }

    public void showMoreDialog( String position, final String id, final String descripcion,final String image, final String addedTime, final String updatedTime){
        //opciones para mostrar en el dialogo
        String [] options = {"Editar", "Eliminar"};
        //Dialogo
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        //Agregar elementos al dialogo
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0){
                    //se hace click en editar
                    //inicie la actividad para actualizar los registros existentes
                    Intent intent = new Intent(context, GuardarPhotograhActivity.class);
                    intent.putExtra("ID", id);
                    intent.putExtra("NAME", descripcion);
                    intent.putExtra("IMAGE", image);
                    intent.putExtra("ADDEDTIME", addedTime);
                    intent.putExtra("UPDATEDTIME", updatedTime);
                    intent.putExtra("isEditMode", true);//nesecita para establecer datos existente
                    context.startActivity(intent);
                }
                else if (which == 1){
                    //Hace click en la opcion eliminar
                    dbHelper.deleteData(id);
                    //Actualizar registro llamando actividade en el metodo de reaunudad
                    ((MainActivity)context).onResume();
                }

            }

        });
        //Mostrar el dialogo
        builder.create().show();
    }


    @Override
    public int getItemCount() {
        return recordsList.size();// devuelve el tamaño de la lista / número o registros
    }

    class HolderRecord extends RecyclerView.ViewHolder{
        //vistas
        ImageView perfilImagen;
        TextView descripcionTv;
        ImageButton moreBtn;
        public HolderRecord(@NonNull View itemView){
            super(itemView);

            //Inicializamos la vistas
            perfilImagen = itemView.findViewById(R.id.perfilImagen);
            descripcionTv = itemView.findViewById(R.id.descripcionTv);
            moreBtn = itemView.findViewById(R.id.moreBtn);

        }
    }
}
