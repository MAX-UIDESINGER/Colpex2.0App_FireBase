package com.example.colpex20app_firebase.Model;


import android.view.MenuItem;

import com.example.colpex20app_firebase.PrincipalClasses.ClassBlanqueado.ClaseBlanqueado;
import com.example.colpex20app_firebase.PrincipalClasses.ClassDesgomado.ClaseDesgomado;
import com.example.colpex20app_firebase.PrincipalClasses.ClassNeutralizado.ClaseNeutralizado;


public interface RvItemClick {
    void onDataChanged();

    void DesgomadoMenuItem (int position, MenuItem menuItem , ClaseDesgomado item);

    void NeutralizadoMenuItem (int position, MenuItem menuItem , ClaseNeutralizado item);

    void BlanqueadoMenuItem (int position, MenuItem menuItem , ClaseBlanqueado item);
}
