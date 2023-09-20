package com.example.colpex20app_firebase.PrincipalClasses.ClasesJefeProducion;

public class CuestionCliente {
    private String id;
    private String nombre;
    private String correo;
    private String ciudad;
    private String pais;
    private String Fechayhora;

    public CuestionCliente() {
    }

    public CuestionCliente(String id, String nombre, String correo, String ciudad, String pais, String fechayhora) {
        this.id = id;
        this.nombre = nombre;
        this.correo = correo;
        this.ciudad = ciudad;
        this.pais = pais;
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

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }
}
