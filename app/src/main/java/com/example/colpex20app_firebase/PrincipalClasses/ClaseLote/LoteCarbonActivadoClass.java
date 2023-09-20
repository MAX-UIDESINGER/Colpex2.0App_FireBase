package com.example.colpex20app_firebase.PrincipalClasses.ClaseLote;

public class LoteCarbonActivadoClass {
    private String id;
    private String nroloteCarbonActivado;
    private String Fechayhora;

    public LoteCarbonActivadoClass() {
    }

    public LoteCarbonActivadoClass(String id, String nroloteCarbonActivado, String fechayhora) {
        this.id = id;
        this.nroloteCarbonActivado = nroloteCarbonActivado;
        this.Fechayhora = fechayhora;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNroloteCarbonActivado() {
        return nroloteCarbonActivado;
    }

    public void setNroloteCarbonActivado(String nroloteCarbonActivado) {
        this.nroloteCarbonActivado = nroloteCarbonActivado;
    }

    public String getFechayhora() {
        return Fechayhora;
    }

    public void setFechayhora(String fechayhora) {
        Fechayhora = fechayhora;
    }
}
