package com.medical.diabetes.models.infosLine;

import androidx.annotation.NonNull;

public class HeaderInfos {
    //these constants are used to know the type of infos listed below
    public static final String HEADER_TYPE_MALADE = "Informations personelles";
    public static final String HEADER_TYPE_MEDECIN = "Diabétologue";
    public static final String HEADER_TYPE_NUMS_URGENCE = "Numéros d'urgence";
    public static final String HEADER_TYPE_HOPITAL = "Hopital";
    //
    public int header; /*0 if not a header 1 if it's a header*/
    public String headerType;

    public HeaderInfos(int header, String headerType) {
        this.header = header;
        this.headerType = headerType;
    }

    @NonNull
    @Override
    public String toString() {
        return "header: " + header
                + " header_type: " + headerType;
    }
}//
