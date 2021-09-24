package com.medical.diabetes.models.infosLine;

import androidx.annotation.NonNull;

public class InfosModel extends HeaderInfos {
    /*these constants are used to know the type of data that should be read*/
    public static final String CONTENT_TYPE_NUMBER = "number";
    public static final String CONTENT_TYPE_TEXT = "text";
    public static final String CONTENT_TYPE_DATE = "date";

    //vars
    public String infosName, contentType, content;
    public int must_be_filled;

    /*public constructor*/
    public InfosModel(int header, String headerType, String contentType, String infosName, String content, int must_be_filled) {
        super(header, headerType);
        this.must_be_filled = must_be_filled;
        this.contentType = contentType;
        this.infosName = infosName;
        this.content = content;
    }


    @NonNull
    @Override
    public String toString() {
        return "header: " + header
                + " header_type: " + headerType
                + " content_type: " + contentType
                + " info_name: " + infosName
                + " content: " + content
                + " must_be_filled: " + must_be_filled;
    }
}//
