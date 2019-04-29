package com.example.edson0710.ezservice.models;

public class Lista {
    String nombre, profesion, imagen, estado, id_firebase, username;
    int id, id_us;

    public Lista(){
    }
    public Lista(String nombre, String profesion, String imagen, int id, int id_us, String estado, String id_firebase, String username) {
        this.nombre = nombre;
        this.profesion = profesion;
        this.imagen = imagen;
        this.id = id;
        this.id_us = id_us;
        this.estado = estado;
        this.id_firebase = id_firebase;
        this.username = username;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getProfesion() {
        return profesion;
    }

    public void setProfesion(String profesion) {
        this.profesion = profesion;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId_us() {
        return id_us;
    }

    public void setId_us(int id_us) {
        this.id_us = id_us;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getId_firebase() {
        return id_firebase;
    }

    public void setId_firebase(String id_firebase) {
        this.id_firebase = id_firebase;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
