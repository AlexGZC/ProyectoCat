package es.app.alexandercontreras.proyectocat.valoracion;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
public class Sitios implements Serializable {
    private int id;
    @SerializedName("nombre")
    private String nombre;
    @SerializedName("descripcion")
    private String descripcion;
    @SerializedName("estrellas")
    private Float estrellas;
    public Sitios(int id, String nombre, String descripcion, Float estrellas) {
        this.setId(id);
        this.setNombre(nombre);
        this.setDescripcion(descripcion);
        this.setEstrellas(estrellas);
    }
    public Sitios(){}
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public String getDescripcion() {
        return descripcion;
    }
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    public Float getEstrellas() {
        return estrellas;
    }
    public void setEstrellas(Float estrellas) {
        this.estrellas = estrellas;
    }
}
