package com.example.colpex20app_firebase.PrincipalClasses.ClasesJefeProducion;

public class Lote {
    private String id;
    private String nro_lote;
    private String producto;
    private String cliente;
    private String tonelaje;
    private String batch;
    private String batchDesgomado;
    private String batchNeutralizado;
    private String batchBlanqueado;
    private String fecha;
    private String acido_fosforico;
    private String temperatura_trabajo;
    private String t_residencia;
    private String ImagenUrl;
    private String UserName;
    private String Descripcion;
    private String cargoArea;
    private String minutos;
    private String badgenotification;
    private boolean expandable;

    public Lote() {
    }

    public Lote(String id, String nro_lote, String producto, String cliente, String tonelaje, String batch, String batchDesgomado, String batchNeutralizado, String batchBlanqueado, String fecha, String acido_fosforico, String temperatura_trabajo, String t_residencia, String imagenUrl, String userName, String descripcion, String cargoArea, String minutos, String badgenotification, boolean expandable) {
        this.id = id;
        this.nro_lote = nro_lote;
        this.producto = producto;
        this.cliente = cliente;
        this.tonelaje = tonelaje;
        this.batch = batch;
        this.batchDesgomado = batchDesgomado;
        this.batchNeutralizado = batchNeutralizado;
        this.batchBlanqueado = batchBlanqueado;
        this.fecha = fecha;
        this.acido_fosforico = acido_fosforico;
        this.temperatura_trabajo = temperatura_trabajo;
        this.t_residencia = t_residencia;
        this.ImagenUrl = imagenUrl;
        this.UserName = userName;
        this.Descripcion = descripcion;
        this.cargoArea = cargoArea;
        this.minutos = minutos;
        this.badgenotification = badgenotification;
        this.expandable = false;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNro_lote() {
        return nro_lote;
    }

    public void setNro_lote(String nro_lote) {
        this.nro_lote = nro_lote;
    }

    public String getProducto() {
        return producto;
    }

    public void setProducto(String producto) {
        this.producto = producto;
    }

    public String getCliente() {
        return cliente;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
    }

    public String getTonelaje() {
        return tonelaje;
    }

    public void setTonelaje(String tonelaje) {
        this.tonelaje = tonelaje;
    }


    public String getBatch() {
        return batch;
    }

    public String getBatchDesgomado() {
        return batchDesgomado;
    }

    public void setBatchDesgomado(String batchDesgomado) {
        this.batchDesgomado = batchDesgomado;
    }

    public String getBatchNeutralizado() {
        return batchNeutralizado;
    }

    public void setBatchNeutralizado(String batchNeutralizado) {
        this.batchNeutralizado = batchNeutralizado;
    }

    public String getBatchBlanqueado() {
        return batchBlanqueado;
    }

    public void setBatchBlanqueado(String batchBlanqueado) {
        this.batchBlanqueado = batchBlanqueado;
    }

    public void setBatch(String batch) {
        this.batch = batch;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getAcido_fosforico() {
        return acido_fosforico;
    }

    public void setAcido_fosforico(String acido_fosforico) {
        this.acido_fosforico = acido_fosforico;
    }

    public String getTemperatura_trabajo() {
        return temperatura_trabajo;
    }

    public void setTemperatura_trabajo(String temperatura_trabajo) {
        this.temperatura_trabajo = temperatura_trabajo;
    }

    public String getT_residencia() {
        return t_residencia;
    }

    public void setT_residencia(String t_residencia) {
        this.t_residencia = t_residencia;
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

    public int getId(int pos) {
        return 0;
    }

    public String getDescripcion() {
        return Descripcion;
    }

    public void setDescripcion(String descripcion) {
        Descripcion = descripcion;
    }

    public String getCargoArea() {
        return cargoArea;
    }

    public void setCargoArea(String cargoArea) {
        this.cargoArea = cargoArea;
    }

    public String getMinutos() {
        return minutos;
    }

    public void setMinutos(String minutos) {
        this.minutos = minutos;
    }

    public String getBadgenotification() {
        return badgenotification;
    }

    public void setBadgenotification(String badgenotification) {
        this.badgenotification = badgenotification;
    }

    public boolean isExpandable() {
        return expandable;
    }

    public void setExpandable(boolean expandable) {
        this.expandable = expandable;
    }
}
