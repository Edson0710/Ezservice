package com.example.edson0710.ezservice.models;

public class Categoria {

    private String nombre;
    private int id_categoria;
    private String imagen_url;

    public Categoria() {
    }

    public Categoria(String nombre, int id_categoria, String imagen_url) {
        this.nombre = nombre;
        this.id_categoria = id_categoria;
        this.imagen_url = imagen_url;
    }

    public String getNombre() {
        return nombre;
    }

    public int getId_categoria() {
        return id_categoria;
    }

    public String getImagen_url() {
        return imagen_url;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setId_categoria(int id_categoria) {
        this.id_categoria = id_categoria;
    }

    public void setImagen_url(String imagen_url) {
        this.imagen_url = imagen_url;
    }
}
