package com.example.colpex20app_firebase.PrincipalClasses.ClassDesgomado;

public class ClaseDesgomado {

    private String id;
    private String operador1;
    private String operador2;
    private String NumeroLote;
    private String Reactor;
    private String ContadorBatch;
    private String Tonelaje;
    private String AcidoFosforico;
    private String Loteacido;
    private String TempTrabajo;
    private String TResidencia;
    private String ImagenUrl;
    private String UserName;
    private String FechaHora;
    private boolean expandable;

    public ClaseDesgomado() {
    }

    public ClaseDesgomado(String id, String operador1, String operador2, String numeroLote, String reactor, String contadorBatch, String tonelaje, String acidoFosforico, String loteacido, String tempTrabajo, String TResidencia, String imagenUrl, String userName, String fechaHora, boolean expandable) {
        this.id = id;
        this.operador1 = operador1;
        this.operador2 = operador2;
        this.NumeroLote = numeroLote;
        this.Reactor = reactor;
        this.ContadorBatch = contadorBatch;
        this.Tonelaje = tonelaje;
        this.AcidoFosforico = acidoFosforico;
        this.Loteacido = loteacido;
        this.TempTrabajo = tempTrabajo;
        this.TResidencia = TResidencia;
        this.ImagenUrl = imagenUrl;
        this.UserName = userName;
        this.FechaHora = fechaHora;
        this.expandable = false;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOperador1() {
        return operador1;
    }

    public void setOperador1(String operador1) {
        this.operador1 = operador1;
    }

    public String getOperador2() {
        return operador2;
    }

    public void setOperador2(String operador2) {
        this.operador2 = operador2;
    }

    public String getNumeroLote() {
        return NumeroLote;
    }

    public void setNumeroLote(String numeroLote) {
        NumeroLote = numeroLote;
    }

    public String getReactor() {
        return Reactor;
    }

    public void setReactor(String reactor) {
        Reactor = reactor;
    }

    public String getContadorBatch() {
        return ContadorBatch;
    }

    public void setContadorBatch(String contadorBatch) {
        ContadorBatch = contadorBatch;
    }

    public String getTonelaje() {
        return Tonelaje;
    }

    public void setTonelaje(String tonelaje) {
        Tonelaje = tonelaje;
    }

    public String getAcidoFosforico() {
        return AcidoFosforico;
    }

    public void setAcidoFosforico(String acidoFosforico) {
        AcidoFosforico = acidoFosforico;
    }

    public String getLoteacido() {
        return Loteacido;
    }

    public void setLoteacido(String loteacido) {
        Loteacido = loteacido;
    }

    public String getTempTrabajo() {
        return TempTrabajo;
    }

    public void setTempTrabajo(String tempTrabajo) {
        TempTrabajo = tempTrabajo;
    }

    public String getTResidencia() {
        return TResidencia;
    }

    public void setTResidencia(String TResidencia) {
        this.TResidencia = TResidencia;
    }

    public String getImagenUrl() {
        return ImagenUrl;
    }

    public void setImagenUrl(String imagenUrl) {
        ImagenUrl = imagenUrl;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getFechaHora() {
        return FechaHora;
    }

    public void setFechaHora(String fechaHora) {
        FechaHora = fechaHora;
    }

    public boolean isExpandable() {
        return expandable;
    }

    public void setExpandable(boolean expandable) {
        this.expandable = expandable;
    }
}
