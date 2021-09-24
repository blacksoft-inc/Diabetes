/**
 * this project was designed and developped by "Rasmi Abdel Wadoud" if this last is used anywhere else without permission
 * you may face some problems
 * */





package com.medical.diabetes.models.recettes;

public class recettesHeader {
    //
    public static String RECETTE_TYPE_ENTREE = "Entrées";
    public static String RECETTE_TYPE_PLATS = "Plats";
    public static String RECETTE_TYPE_DESSERTS = "Desserts";
    //
    public String recetteType;
    //
    public recettesHeader(String recettesType){
        this.recetteType = recettesType;
    }
}
