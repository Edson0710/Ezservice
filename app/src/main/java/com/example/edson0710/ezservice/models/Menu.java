package com.example.edson0710.ezservice.models;

import com.example.edson0710.ezservice.R;

import java.util.ArrayList;

public class Menu {
    private int idImagen;
    private String titulo;

    public Menu(){
        idImagen = 0;
        titulo="";
    }

    public Menu(int idImagen, String titulo) {
        this.idImagen = idImagen;
        this.titulo = titulo;
    }

    public int getIdImagen() {
        return idImagen;
    }

    public void setIdImagen(int idImagen) {
        this.idImagen = idImagen;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

}
