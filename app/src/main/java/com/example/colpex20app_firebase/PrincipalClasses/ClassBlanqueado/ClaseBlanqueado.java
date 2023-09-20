package com.example.colpex20app_firebase.PrincipalClasses.ClassBlanqueado;

public class ClaseBlanqueado {

    private String id;
    private String operador1;
    private String operador2;
    private String NumeroLote;
    private String Reactor;
    private String Carga;
    private String Vacio;
    private String TempTrabajo;
    private String Arcilla;
    private String ALLote;
    private String CarbonActivado;
    private String CALote;
    private String Silice;
    private String SLote;
    private String FFAFinal;
    private String ColorFinal;
    private String AndisidinaFinal;
    private String ImagenUrl;
    private String UserName;
    private String ContadorBatch;
    private String FechaHora;
    private boolean expandable;

    public ClaseBlanqueado() {
    }

    public ClaseBlanqueado(String id, String operador1, String operador2, String numeroLote, String reactor, String carga, String vacio, String tempTrabajo, String arcilla, String ALLote, String carbonActivado, String CALote, String silice, String SLote, String FFAFinal, String colorFinal, String andisidinaFinal, String imagenUrl, String userName, String contadorBatch, String fechaHora, boolean expandable) {
        this.id = id;
        this.operador1 = operador1;
        this.operador2 = operador2;
        this.NumeroLote = numeroLote;
        this.Reactor = reactor;
        this.Carga = carga;
        this.Vacio = vacio;
        this.TempTrabajo = tempTrabajo;
        this.Arcilla = arcilla;
        this.ALLote = ALLote;
        this.CarbonActivado = carbonActivado;
        this.CALote = CALote;
        this.Silice = silice;
        this.SLote = SLote;
        this.FFAFinal = FFAFinal;
        this.ColorFinal = colorFinal;
        this.AndisidinaFinal = andisidinaFinal;
        this.ImagenUrl = imagenUrl;
        this.UserName = userName;
        this.ContadorBatch = contadorBatch;
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

    public String getCarga() {
        return Carga;
    }

    public void setCarga(String carga) {
        Carga = carga;
    }

    public String getVacio() {
        return Vacio;
    }

    public void setVacio(String vacio) {
        Vacio = vacio;
    }

    public String getTempTrabajo() {
        return TempTrabajo;
    }

    public void setTempTrabajo(String tempTrabajo) {
        TempTrabajo = tempTrabajo;
    }

    public String getArcilla() {
        return Arcilla;
    }

    public void setArcilla(String arcilla) {
        Arcilla = arcilla;
    }

    public String getALLote() {
        return ALLote;
    }

    public void setALLote(String ALLote) {
        this.ALLote = ALLote;
    }

    public String getCarbonActivado() {
        return CarbonActivado;
    }

    public void setCarbonActivado(String carbonActivado) {
        CarbonActivado = carbonActivado;
    }

    public String getCALote() {
        return CALote;
    }

    public void setCALote(String CALote) {
        this.CALote = CALote;
    }

    public String getSilice() {
        return Silice;
    }

    public void setSilice(String silice) {
        Silice = silice;
    }

    public String getSLote() {
        return SLote;
    }

    public void setSLote(String SLote) {
        this.SLote = SLote;
    }

    public String getFFAFinal() {
        return FFAFinal;
    }

    public void setFFAFinal(String FFAFinal) {
        this.FFAFinal = FFAFinal;
    }

    public String getColorFinal() {
        return ColorFinal;
    }

    public void setColorFinal(String colorFinal) {
        ColorFinal = colorFinal;
    }

    public String getAndisidinaFinal() {
        return AndisidinaFinal;
    }

    public void setAndisidinaFinal(String andisidinaFinal) {
        AndisidinaFinal = andisidinaFinal;
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

    public String getContadorBatch() {
        return ContadorBatch;
    }

    public void setContadorBatch(String contadorBatch) {
        ContadorBatch = contadorBatch;
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
