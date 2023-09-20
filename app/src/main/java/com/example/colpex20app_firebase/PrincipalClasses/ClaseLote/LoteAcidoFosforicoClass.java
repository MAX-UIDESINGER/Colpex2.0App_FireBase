package com.example.colpex20app_firebase.PrincipalClasses.ClaseLote;

public class LoteAcidoFosforicoClass {
    private String id;
    private String nroloteAcidoFosforico;
    private String Fechayhora;

    public LoteAcidoFosforicoClass() {
    }

    public LoteAcidoFosforicoClass(String id, String nroloteAcidoFosforico, String fechayhora) {
        this.id = id;
        this.nroloteAcidoFosforico = nroloteAcidoFosforico;
        this.Fechayhora = fechayhora;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNroloteAcidoFosforico() {
        return nroloteAcidoFosforico;
    }

    public void setNroloteAcidoFosforico(String nroloteAcidoFosforico) {
        this.nroloteAcidoFosforico = nroloteAcidoFosforico;
    }

    public String getFechayhora() {
        return Fechayhora;
    }

    public void setFechayhora(String fechayhora) {
        Fechayhora = fechayhora;
    }

}
