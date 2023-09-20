package com.example.colpex20app_firebase.PrincipalClasses.ClassNeutralizado;

import java.util.List;

public class ItemNeutralizado {
    private String itemTitleLote;
    private List<ClaseNeutralizado> subItemListNeutralizado;

    public ItemNeutralizado(String itemTitleLote, List<ClaseNeutralizado> subItemListNeutralizado) {
        this.itemTitleLote = itemTitleLote;
        this.subItemListNeutralizado = subItemListNeutralizado;
    }

    public String getItemTitleLote() {
        return itemTitleLote;
    }

    public void setItemTitleLote(String itemTitleLote) {
        this.itemTitleLote = itemTitleLote;
    }

    public List<ClaseNeutralizado> getSubItemListNeutralizado() {
        return subItemListNeutralizado;
    }

    public void setSubItemListNeutralizado(List<ClaseNeutralizado> subItemListNeutralizado) {
        this.subItemListNeutralizado = subItemListNeutralizado;
    }
}
