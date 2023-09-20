package com.example.colpex20app_firebase.PrincipalClasses.ClassBlanqueado;

import java.util.List;

public class ItemBlanqueado {
    private String itemTitleLote;
    private List<ClaseBlanqueado> subItemListBlanqueado;


    public ItemBlanqueado(String itemTitleLote, List<ClaseBlanqueado> subItemListBlanqueado) {
        this.itemTitleLote = itemTitleLote;
        this.subItemListBlanqueado = subItemListBlanqueado;
    }

    public String getItemTitleLote() {
        return itemTitleLote;
    }

    public void setItemTitleLote(String itemTitleLote) {
        this.itemTitleLote = itemTitleLote;
    }

    public List<ClaseBlanqueado> getSubItemListBlanqueado() {
        return subItemListBlanqueado;
    }

    public void setSubItemListBlanqueado(List<ClaseBlanqueado> subItemListBlanqueado) {
        this.subItemListBlanqueado = subItemListBlanqueado;
    }
}
