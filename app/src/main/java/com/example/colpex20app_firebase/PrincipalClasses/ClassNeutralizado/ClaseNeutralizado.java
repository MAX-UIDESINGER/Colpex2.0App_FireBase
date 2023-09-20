package com.example.colpex20app_firebase.PrincipalClasses.ClassNeutralizado;

public class ClaseNeutralizado {

    private String id;
    private String operador1;
    private String operador2;
    private String NumeroLote;
    private String Reactor;
    private String ContadorBatch;
    private String FFAInicial;
    private String TempTrabajo;
    private String ConcentracionNaOH;
    private String LoteNAOH;
    private String AceiteNeutro;
    private String ImagenUrl;
    private String UserName;
    private String FechaHora;
    private String Porsentaje;
    private String contador;
    private boolean expandable;

    public ClaseNeutralizado() {
    }

    public ClaseNeutralizado(String id, String operador1, String operador2, String numeroLote, String reactor, String contadorBatch, String FFAInicial, String tempTrabajo, String concentracionNaOH, String loteNAOH, String aceiteNeutro, String imagenUrl, String userName, String fechaHora, String porsentaje, String contador, boolean expandable) {
        this.id = id;
        this.operador1 = operador1;
        this.operador2 = operador2;
        this.NumeroLote = numeroLote;
        this.Reactor = reactor;
        this.ContadorBatch = contadorBatch;
        this.FFAInicial = FFAInicial;
        this.TempTrabajo = tempTrabajo;
        this.ConcentracionNaOH = concentracionNaOH;
        this.LoteNAOH = loteNAOH;
        this.AceiteNeutro = aceiteNeutro;
        this.ImagenUrl = imagenUrl;
        this.UserName = userName;
        this.FechaHora = fechaHora;
        this.Porsentaje = porsentaje;
        this.contador = contador;
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

    public String getFFAInicial() {
        return FFAInicial;
    }

    public void setFFAInicial(String FFAInicial) {
        this.FFAInicial = FFAInicial;
    }

    public String getTempTrabajo() {
        return TempTrabajo;
    }

    public void setTempTrabajo(String tempTrabajo) {
        TempTrabajo = tempTrabajo;
    }

    public String getConcentracionNaOH() {
        return ConcentracionNaOH;
    }

    public void setConcentracionNaOH(String concentracionNaOH) {
        ConcentracionNaOH = concentracionNaOH;
    }

    public String getLoteNAOH() {
        return LoteNAOH;
    }

    public void setLoteNAOH(String loteNAOH) {
        LoteNAOH = loteNAOH;
    }

    public String getAceiteNeutro() {
        return AceiteNeutro;
    }

    public void setAceiteNeutro(String aceiteNeutro) {
        AceiteNeutro = aceiteNeutro;
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

    public String getPorsentaje() {
        return Porsentaje;
    }

    public void setPorsentaje(String porsentaje) {
        Porsentaje = porsentaje;
    }

    public String getContador() {
        return contador;
    }

    public void setContador(String contador) {
        this.contador = contador;
    }

    public void setExpandable(boolean expandable) {
        this.expandable = expandable;
    }
}


