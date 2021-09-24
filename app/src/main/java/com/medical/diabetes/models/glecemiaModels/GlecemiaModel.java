package com.medical.diabetes.models.glecemiaModels;


public class GlecemiaModel extends DateTimeHeader {
    public double tauxAvantRepas, tauxApresRepas, dozeInsuline;
    public String time;

    public GlecemiaModel(long _ID, String dateTime, double tauxAvantRepas, double tauxApresRepas, double dozeInsuline, String time) {
        super(_ID, 0, dateTime);
        this.tauxApresRepas = tauxApresRepas;
        this.tauxAvantRepas = tauxAvantRepas;
        this.dozeInsuline = dozeInsuline;
        this.time = time;
    }

}//
