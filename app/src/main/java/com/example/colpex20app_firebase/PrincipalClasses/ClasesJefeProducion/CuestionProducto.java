package com.example.colpex20app_firebase.PrincipalClasses.ClasesJefeProducion;

public class CuestionProducto {
    private String id;
    private String nombre;
    private String descripcion;
    private String stock;
    private String Fechayhora;

    public CuestionProducto() {

    }

    public CuestionProducto(String id, String nombre, String descripcion, String stock, String fechayhora) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.stock = stock;
        this.Fechayhora = fechayhora;
    }

    public String getFechayhora() {
        return Fechayhora;
    }

    public void setFechayhora(String fechayhora) {
        Fechayhora = fechayhora;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
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

    public String getStock() {
        return stock;
    }

    public void setStock(String stock) {
        this.stock = stock;
    }
}
