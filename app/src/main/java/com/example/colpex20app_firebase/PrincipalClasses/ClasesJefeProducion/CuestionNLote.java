package com.example.colpex20app_firebase.PrincipalClasses.ClasesJefeProducion;

public class CuestionNLote {
    private String id;
    private String nrolote;
    private String Fechayhora;

    public CuestionNLote() {
    }

    public CuestionNLote(String id, String nrolote, String fechayhora) {
        this.id = id;
        this.nrolote = nrolote;
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

    public String getNrolote() {
        return nrolote;
    }

    public void setNrolote(String nrolote) {
        this.nrolote = nrolote;
    }
}
