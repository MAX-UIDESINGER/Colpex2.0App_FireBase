package com.example.colpex20app_firebase.PrincipalClasses.ClaseLote;

public class LoteNaOHClass {
    private String id;
    private String nroloteNaOH;
    private String Fechayhora;

    public LoteNaOHClass() {
    }

    public LoteNaOHClass(String id, String nroloteNaOH, String fechayhora) {
        this.id = id;
        this.nroloteNaOH = nroloteNaOH;
        Fechayhora = fechayhora;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNroloteNaOH() {
        return nroloteNaOH;
    }

    public void setNroloteNaOH(String nroloteNaOH) {
        this.nroloteNaOH = nroloteNaOH;
    }

    public String getFechayhora() {
        return Fechayhora;
    }

    public void setFechayhora(String fechayhora) {
        Fechayhora = fechayhora;
    }
}
