package com.example.colpex20app_firebase.PrincipalClasses;

import android.net.Uri;

public class NoticiasClass {
    public Uri masInfo;
    int image;
    String title, description;


    public NoticiasClass(int image, String title, String description, Uri masInfo) {
        this.image = image;
        this.title = title;
        this.description = description;
        this.masInfo = masInfo;
    }

    public int getImage() {
        return image;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }
}
