package app.examen.ejercicio23grupo4.configuracion;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;

import app.examen.ejercicio23grupo4.Tablas.ModeloRegistro;

public class BaseDatos extends SQLiteOpenHelper {

    //constructor
    public BaseDatos(@Nullable Context context) {
        super(context, TransaccionesRegistro.DB_NAME, null, TransaccionesRegistro.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        //Crea la tabla de la base de datos
        db.execSQL(TransaccionesRegistro.CREATE_TABLE);
    }



    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){

        // actualizar la base de datos

        //descartar la tabla anterior si existe
        db.execSQL("DROP TABLE IF EXISTS "+ TransaccionesRegistro.TABLE_NAME);
        //crear tabla de nuevo
        onCreate(db);
    }

    //Inserta datos a la base de datos
    public long insertImagen(String descripcion, String image,String addedTime, String updatedTime ){

        //get databse grabable porque queremos escribir datos

        SQLiteDatabase db =this.getWritableDatabase();

        ContentValues values = new ContentValues();


        // la identificación se insertará automáticamente cuando configuremos AUTOINCREMENTO en la consulta

        // inserta datos
        values.put(TransaccionesRegistro.C_DESCRIPCION, descripcion);
        values.put(TransaccionesRegistro.C_IMAGE, image);
        values.put(TransaccionesRegistro.C_ADDED_TIMESTAMP, addedTime);
        values.put(TransaccionesRegistro.C_UPDATED_TIMESTAMP, updatedTime);

        // insertar fila, devolverá la identificación del registro guardado
        long id = db.insert(TransaccionesRegistro.TABLE_NAME, null, values);


        // cerrar db Connection
        db.close();


        // devuelve la identificación del registro insertado
        return id;

    }

    //Obtener todos datos
    public ArrayList<ModeloRegistro> getAllRegistros(String orderBy){
        // la orden de consulta permitirá ordenar los datos más nuevo / más antiguo primero, nombre ascendente / descendente
        // devolverá la lista o registros ya que hemos utilizado return tipo ArrayList <ModelRecord>

        ArrayList<ModeloRegistro> listaRegistros = new ArrayList<>();
        // consulta para seleccionar registros
        String selectQuery = " SELECT * FROM " + TransaccionesRegistro.TABLE_NAME + " ORDER BY " + orderBy;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // recorrer todos los registros y agregarlos a la lista
        if ( cursor.moveToFirst()){

            do {
                @SuppressLint("Range") ModeloRegistro modeloRegistro = new ModeloRegistro(
                        ""+cursor.getInt(cursor.getColumnIndex(TransaccionesRegistro.C_ID)),
                        ""+cursor.getString(cursor.getColumnIndex(TransaccionesRegistro.C_DESCRIPCION)),
                        ""+cursor.getString(cursor.getColumnIndex(TransaccionesRegistro.C_IMAGE)),
                        ""+cursor.getString(cursor.getColumnIndex(TransaccionesRegistro.C_ADDED_TIMESTAMP)),
                        ""+cursor.getString(cursor.getColumnIndex(TransaccionesRegistro.C_UPDATED_TIMESTAMP)));

                // Añadir registro a la list
                listaRegistros.add(modeloRegistro);
            }while (cursor.moveToNext());
        }

        //cierre de conexión db

        db.close();

        //retorna la lista
        return listaRegistros;
    }


    //Buscar todos datos
    public ArrayList<ModeloRegistro> buscarRegistros(String query){
        // la orden de consulta permitirá ordenar los datos más nuevo / más antiguo primero, nombre ascendente / descendente
        // devolverá la lista o registros ya que hemos utilizado return tipo ArrayList <ModelRecord>

        ArrayList<ModeloRegistro> listaRegistros = new ArrayList<>();
        // consulta para seleccionar registros
        String selectQuery = " SELECT * FROM " + TransaccionesRegistro.TABLE_NAME + " WHERE " + TransaccionesRegistro.C_DESCRIPCION + " LIKE '%" + query +"%'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // recorrer todos los registros y agregarlos a la lista
        if ( cursor.moveToFirst()){
            do {
                @SuppressLint("Range") ModeloRegistro modeloRegistro = new ModeloRegistro(
                        ""+cursor.getInt(cursor.getColumnIndex(TransaccionesRegistro.C_ID)),
                        ""+cursor.getString(cursor.getColumnIndex(TransaccionesRegistro.C_DESCRIPCION)),
                        ""+cursor.getString(cursor.getColumnIndex(TransaccionesRegistro.C_IMAGE)),
                        ""+cursor.getString(cursor.getColumnIndex(TransaccionesRegistro.C_ADDED_TIMESTAMP)),
                        ""+cursor.getString(cursor.getColumnIndex(TransaccionesRegistro.C_UPDATED_TIMESTAMP)));

                // Añadir registro a la list
                listaRegistros.add(modeloRegistro);
            }while (cursor.moveToNext());
        }

        //cierre de conexión db

        db.close();

        //retorna la lista
        return listaRegistros;
    }

    //eliminar datos usando id
    public void deleteData(String id){
        SQLiteDatabase db=getWritableDatabase();
        db.delete(TransaccionesRegistro.TABLE_NAME, TransaccionesRegistro.C_ID +" = ? ", new String[]{id});
        db.close();
    }
    //eliminar todos los dtos
    public void deleteAllData(){
        SQLiteDatabase db= getWritableDatabase();
        db.execSQL(" DELETE FROM "+ TransaccionesRegistro.TABLE_NAME);
        db.close();
    }

    //Obtener el numero de registros
    public int getRecordsCount(){
        String countQuery = " SELECT * FROM " + TransaccionesRegistro.TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();

        cursor.close();

        return count;
    }

    //Inserta datos a la base de datos
    public void updateRecord(String id, String descripcion, String image,String addedTime, String updatedTime ){

        //get databse grabable porque queremos escribir datos

        SQLiteDatabase db =this.getWritableDatabase();

        ContentValues values = new ContentValues();


        // la identificación se insertará automáticamente cuando configuremos AUTOINCREMENTO en la consulta

        // inserta datos
        values.put(TransaccionesRegistro.C_DESCRIPCION, descripcion);
        values.put(TransaccionesRegistro.C_IMAGE, image);
        values.put(TransaccionesRegistro.C_ADDED_TIMESTAMP, addedTime);
        values.put(TransaccionesRegistro.C_UPDATED_TIMESTAMP, updatedTime);

        //Update de datos
        db.update(TransaccionesRegistro.TABLE_NAME, values, TransaccionesRegistro.C_ID+" = ? ", new String[]{id});

        // cerrar db Connection
        db.close();


    }



}
