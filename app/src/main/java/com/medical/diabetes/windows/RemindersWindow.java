/**
 * this project was designed and developped by "Rasmi Abdel Wadoud" if this last is used anywhere else without permission
 * you may face some problems
 * */











































package com.medical.diabetes.windows;


import android.app.AlertDialog;
import android.content.ContentValues;

import android.view.View;

import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;
import com.medical.diabetes.MainActivity;
import com.medical.diabetes.R;
import com.medical.diabetes.database.DataBaseTablesSchemas;
import com.medical.diabetes.fragments.utils.AlarmUtils;
import com.medical.diabetes.models.Reminder;

import java.util.ArrayList;

public final class RemindersWindow {
    //
    public ArrayList<Reminder> alarmsList;
    //
    public Reminder current;
    //
    private TextView rapport;

    public RemindersWindow(ArrayList<Reminder> alarmsList, TextView rapport) {
        this.alarmsList = alarmsList;
        this.rapport = rapport;
    }


    /*letting the window be active*/
    public void showWindow(final AlertDialog window, final RecyclerView recyclerView, TextView Rapport) {
        window.show();
        // setting window container background transparent
        window.getWindow().getDecorView().setBackgroundColor(0);
        //
        final TextInputEditText editText = window.findViewById(R.id.text_field);
        final TimePicker timePicker = window.findViewById(R.id.time_picker);
        //
        final CheckBox sat, sun, mon, tues, wed, thur, fri;
        //
        sat = window.findViewById(R.id.saturday);
        sun = window.findViewById(R.id.sunday);
        tues = window.findViewById(R.id.tuesday);
        wed = window.findViewById(R.id.wednesday);
        mon = window.findViewById(R.id.monday);
        thur = window.findViewById(R.id.thursday);
        fri = window.findViewById(R.id.friday);
        //filling with current infos in case of editing
        if (current != null) {
            //
            editText.setText(current.title);
            //
            if (current.days.contains("sat")) sat.setChecked(true);
            if (current.days.contains("sun")) sun.setChecked(true);
            if (current.days.contains("mon")) mon.setChecked(true);
            if (current.days.contains("tues")) tues.setChecked(true);
            if (current.days.contains("wed")) wed.setChecked(true);
            if (current.days.contains("thur")) thur.setChecked(true);
            if (current.days.contains("fri")) fri.setChecked(true);
            //
            String time[] = current.time.split(":");
            timePicker.setCurrentHour(Integer.valueOf(time[0]));
            timePicker.setCurrentMinute(Integer.valueOf(time[1]));

        } else {
            current = new Reminder(-1, "", "", "");
            //
            editText.setText("");
            //
            sat.setChecked(false);
            sun.setChecked(false);
            mon.setChecked(false);
            tues.setChecked(false);
            wed.setChecked(false);
            thur.setChecked(false);
            fri.setChecked(false);

        }
        window.findViewById(R.id.confirm_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //
                current.title = editText.getText().toString();
                //
                String time = ((timePicker.getCurrentHour() < 10) ? "0" + timePicker.getCurrentHour() : timePicker.getCurrentHour()) + ":"
                        + ((timePicker.getCurrentMinute() < 10) ? "0" + timePicker.getCurrentMinute() + "" : timePicker.getCurrentMinute());
                //
                String days = "";
                if (sat.isChecked()) {
                    days += "sat";
                }
                if (sun.isChecked()) {
                    days += "/sun";
                }
                if (mon.isChecked()) {
                    days += "/mon";
                }
                if (tues.isChecked()) {
                    days += "/tues";
                }
                if (wed.isChecked()) {
                    days += "/wed";
                }
                if (thur.isChecked()) {
                    days += "/thur";
                }
                if (fri.isChecked()) {
                    days += "/fri";
                }
                // in case there's a similar reminder known by time and days in the list so don't add it
                Reminder fake = new Reminder(-1, "", time,days);

                if (alarmsList.contains(fake)) {
                    Toast.makeText(v.getContext(), "Alarm déjà dans la liste", Toast.LENGTH_SHORT).show();
                    return;
                }
                //
                current.days = days;
                current.time = time;
                //
                ContentValues row = new ContentValues();
                row.put(DataBaseTablesSchemas.RemindersTable.COLUMN_TITLE, current.title);
                row.put(DataBaseTablesSchemas.RemindersTable.COLUMN_TIME, current.time);
                row.put(DataBaseTablesSchemas.RemindersTable.COLUMN_DAYS, current.days);
                //
                if (current._ID == -1) { /*if it's a new element*/
                    row.put(DataBaseTablesSchemas.RemindersTable.COLUMN_STATE, Reminder.STATE_ACTIVE);
                    current._ID = MainActivity.writableDB.insert(DataBaseTablesSchemas.RemindersTable.TABLE_NAME, null, row);
                    alarmsList.add(current);
                } else {
                    MainActivity.writableDB.update(DataBaseTablesSchemas.RemindersTable.TABLE_NAME, row, "_id = " + current._ID, null);
                }
                //
                if (alarmsList.size() != 0)
                    rapport.setText("Vous avez " + alarmsList.size() + " alarme(s) dans cette liste...");
                else rapport.setText("Ajoutez des alarmes pour les voir en haut...");
                //
                AlarmUtils.addAlarm(MainActivity.mainContext, current);
                recyclerView.getAdapter().notifyDataSetChanged();
                window.dismiss();
            }
        });
        //close window to cancel adding or modifying
        window.findViewById(R.id.cancel_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                window.dismiss();
            }
        });
        // close keyboard if opened
        window.findViewById(R.id.close_keyboard_img).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.closeKeyboard(v);
            }
        });
    }


}//
