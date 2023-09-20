package com.example.colpex20app_firebase.PrincipalClasses;

public class CertificadosClass {
    int image;
    String title,description;

    public CertificadosClass(int image, String title, String description) {
        this.image = image;
        this.title = title;
        this.description = description;
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
