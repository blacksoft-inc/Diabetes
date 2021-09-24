package com.medical.diabetes.models.glecemiaModels;

public class DateTimeHeader {
    public long _ID;
    public int header;
    public String dateTime;

    public DateTimeHeader(long _ID, int header, String dateTime) {
        this.dateTime = dateTime;
        this.header = header;
        this._ID = _ID;
    }

}
