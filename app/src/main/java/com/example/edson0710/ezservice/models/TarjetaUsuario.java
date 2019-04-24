package com.example.edson0710.ezservice.models;

public class TarjetaUsuario {

    private String imagen;
    private String nombre;
    private int edad;
    private int id;
    private double calificacion;
    private String descripcion;

    public TarjetaUsuario() {
        this.imagen = imagen;
        this.nombre = nombre;
        this.edad = edad;
        this.calificacion = calificacion;
        this.descripcion = descripcion;
        this.id = id;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getEdad() {
        return edad;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }

    public double getCalificacion() {
        return calificacion;
    }

    public void setCalificacion(double calificacion) {
        this.calificacion = calificacion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
