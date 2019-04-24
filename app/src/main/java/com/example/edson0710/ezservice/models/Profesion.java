package com.example.edson0710.ezservice.models;

public class Profesion {
    private String nombre;
    private int id_profesion;
    //private String imagen_url;

    public Profesion() {
    }

    public Profesion(String nombre, int id_profesion) {
        this.nombre = nombre;
        this.id_profesion = id_profesion;
        //this.imagen_url = imagen_url;
    }

    public String getNombre2() {
        return nombre;
    }

    public int getId_profesion2() {
        return id_profesion;
    }


    public void setNombre2(String nombre) {
        this.nombre = nombre;
    }

    public void setId_profesion2(int id_profesion) {
        this.id_profesion = id_profesion;
    }

}
