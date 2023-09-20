package com.example.colpex20app_firebase.PrincipalClasses.ClassDesgomado;


import java.util.List;

public class ItemDesgomado {

    private String itemTitleLote;
    private List<ClaseDesgomado> subItemListDesgomado;

    public ItemDesgomado( String itemTitleLote, List<ClaseDesgomado> subItemListDesgomado) {
        this.itemTitleLote = itemTitleLote;
        this.subItemListDesgomado = subItemListDesgomado;
    }

    public String getItemTitleLote() {
        return itemTitleLote;
    }

    public void setItemTitleLote(String itemTitleLote) {
        this.itemTitleLote = itemTitleLote;
    }

    public List<ClaseDesgomado> getSubItemListDesgomado() {
        return subItemListDesgomado;
    }

    public void setSubItemListDesgomado(List<ClaseDesgomado> subItemListDesgomado) {
        this.subItemListDesgomado = subItemListDesgomado;
    }
}
