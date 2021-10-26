package app.examen.ejercicio23grupo4.configuracion;

public class TransaccionesRegistro {
    // nombre base de datos
    public static final String DB_NAME = "PM01G4";
    //version de base de datos
    public static final int DB_VERSION = 1;
    //nombre de la tabla
    public static final String TABLE_NAME = "REGISTRO";
    //columnas/campos de la tabla
    public static final String C_ID = "ID";
    public static final String C_DESCRIPCION = "DESCRIPCION";
    public static  final String C_IMAGE = "IMAGE";
    public static final String C_ADDED_TIMESTAMP = "ADDED_TIME_STAMP";
    public static final String C_UPDATED_TIMESTAMP = "UPDATED_TIME_STAMP";

    //Crea la tabla Query
    public static  final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME +"("
            + C_ID+ " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + C_DESCRIPCION+ " TEXT,"
            + C_IMAGE+ " TEXT,"
            + C_ADDED_TIMESTAMP+ " TEXT,"
            + C_UPDATED_TIMESTAMP+ " TEXT"
            + ")";
}
