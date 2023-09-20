package com.example.colpex20app_firebase.PrincipalClasses.ClaseLote;

public class LoteSilliceClass {

    private String id;
    private String nroloteSillice;
    private String Fechayhora;

    public LoteSilliceClass() {
    }

    public LoteSilliceClass(String id, String nroloteSillice, String fechayhora) {
        this.id = id;
        this.nroloteSillice = nroloteSillice;
        this.Fechayhora = fechayhora;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNroloteSillice() {
        return nroloteSillice;
    }

    public void setNroloteSillice(String nroloteSillice) {
        this.nroloteSillice = nroloteSillice;
    }

    public String getFechayhora() {
        return Fechayhora;
    }

    public void setFechayhora(String fechayhora) {
        Fechayhora = fechayhora;
    }
}
