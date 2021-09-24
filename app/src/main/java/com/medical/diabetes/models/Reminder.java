/**
 * this project was designed and developped by "Rasmi Abdel Wadoud" if this last is used anywhere else without permission
 * you may face some problems
 * */






package com.medical.diabetes.models;

import androidx.annotation.Nullable;

import java.io.Serializable;
import java.util.Calendar;

public class Reminder implements Serializable {
    //state values
    public static final int STATE_ACTIVE = 1;
    public static final int STATE_PASSIVE = 0;
    //days int values like in calendar
    public static final int SATURDAY_INT = Calendar.SATURDAY;
    public static final int SUNDAY_INT = Calendar.SUNDAY;
    public static final int MONDAY_INT = Calendar.MONDAY;
    public static final int TUESDAY_INT = Calendar.TUESDAY;
    public static final int WEDNESDAY_INT = Calendar.WEDNESDAY;
    public static final int THURSDAY_INT = Calendar.THURSDAY;
    public static final int FRIDAY_INT = Calendar.FRIDAY;
    //
    public int state;
    public long _ID;
    public String time, days, title;

    //constructor without state
    public Reminder(long _ID, String title, String time, String days) {
        this._ID = _ID;
        this.title = title;
        this.state = STATE_ACTIVE;
        this.time = time;
        this.days = days;
    }

    //constructor with state
    public Reminder(int _ID, String title, int state, String time, String days) {
        this._ID = _ID;
        this.title = title;
        this.state = state;
        this.time = time;
        this.days = days;
    }
    // how many times per week

    public int timesPerWeek() {
        int t = 0;
        if (days.contains("sat")) t++;
        if (days.contains("sun")) t++;
        if (days.contains("mon")) t++;
        if (days.contains("tues")) t++;
        if (days.contains("wed")) t++;
        if (days.contains("thur")) t++;
        if (days.contains("fri")) t++;
        return t;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        Reminder reminder = (Reminder) obj;
        return this.time.equals(reminder.time) && this.days.equals(reminder.days);
    }
}//






















































/**
 * this project was designed and developped by "Rasmi Abdel Wadoud" if this last is used anywhere else without permission
 * you may face some problems
 * */