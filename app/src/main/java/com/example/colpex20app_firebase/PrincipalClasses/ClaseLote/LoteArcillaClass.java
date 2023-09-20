package com.example.colpex20app_firebase.PrincipalClasses.ClaseLote;

public class LoteArcillaClass {
    private String id;
    private String nroloteArcilla;
    private String Fechayhora;

    public LoteArcillaClass() {
    }

    public LoteArcillaClass(String id, String nroloteArcilla, String fechayhora) {
        this.id = id;
        this.nroloteArcilla = nroloteArcilla;
        Fechayhora = fechayhora;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNroloteArcilla() {
        return nroloteArcilla;
    }

    public void setNroloteArcilla(String nroloteArcilla) {
        this.nroloteArcilla = nroloteArcilla;
    }

    public String getFechayhora() {
        return Fechayhora;
    }

    public void setFechayhora(String fechayhora) {
        Fechayhora = fechayhora;
    }
}
