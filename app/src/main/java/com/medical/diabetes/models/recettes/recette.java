/**
 * this project was designed and developped by "Rasmi Abdel Wadoud" if this last is used anywhere else without permission
 * you may face some problems
 * */


package com.medical.diabetes.models.recettes;

public class recette extends recettesHeader {
    //
    public String title, filepath;

    public recette(String title, String filepath) {
        super(null);
        this.title = title;
        this.filepath = filepath;
    }
}//
