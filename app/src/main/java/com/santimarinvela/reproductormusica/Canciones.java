package com.santimarinvela.reproductormusica;

import android.graphics.Bitmap;

//CREO LA CLASE CANCIONES PARA CREAR OBJETOS A PARTIR DE ELLA
public class Canciones{

    private String nombre_SMV;
    private String autor_SMV;
    private String album_SMV;
    private String path;
    private Bitmap foto_SMV;

    public Canciones(String nombre_SMV, String autor_SMV, String album_SMV, String path, Bitmap foto_SMV) {
        this.nombre_SMV = nombre_SMV;
        this.autor_SMV = autor_SMV;
        this.album_SMV = album_SMV;
        this.path = path;
        this.foto_SMV = foto_SMV;
    }
    public Canciones(){

    }

    public String getNombre_SMV() {
        return nombre_SMV;
    }

    public void setNombre_SMV(String nombre_SMV) {
        this.nombre_SMV = nombre_SMV;
    }

    public String getAutor_SMV() {
        return autor_SMV;
    }

    public void setAutor_SMV(String autor_SMV) {
        this.autor_SMV = autor_SMV;
    }

    public String getAlbum_SMV() {
        return album_SMV;
    }

    public void setAlbum_SMV(String album_SMV) {
        this.album_SMV = album_SMV;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Bitmap getFoto_SMV() {
        return foto_SMV;
    }

    public void setFoto_SMV(Bitmap foto_SMV) {
        this.foto_SMV = foto_SMV;
    }

}
