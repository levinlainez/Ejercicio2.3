package app.examen.ejercicio23grupo4.Tablas;

/* Modelo de la clase para RecyclerView*/
public class ModeloRegistro {
    //Variables
    String id, descripcion, image,addedTime, updatedTime;

    //Constructor

    public ModeloRegistro(String id, String descripcion, String image, String addedTime, String updatedTime) {
        this.id = id;
        this.descripcion = descripcion;
        this.image = image;
        this.addedTime = addedTime;
        this.updatedTime = updatedTime;
    }

    //Getter y Setter

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getAddedTime() {
        return addedTime;
    }

    public void setAddedTime(String addedTime) {
        this.addedTime = addedTime;
    }

    public String getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(String updatedTime) {
        this.updatedTime = updatedTime;
    }
}
