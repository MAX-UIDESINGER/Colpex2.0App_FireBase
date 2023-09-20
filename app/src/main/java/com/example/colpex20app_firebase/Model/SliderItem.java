package com.example.colpex20app_firebase.Model;

public class SliderItem {
    private String titulo;
    private int imagen;

    public SliderItem() {
    }

    public SliderItem(String titulo, int imagen) {
        this.titulo = titulo;
        this.imagen = imagen;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public int getImagen() {
        return imagen;
    }

    public void setImagen(int imagen) {
        this.imagen = imagen;
    }
}
