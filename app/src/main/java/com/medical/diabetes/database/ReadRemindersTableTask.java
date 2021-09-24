package com.medical.diabetes.database;

import android.database.Cursor;
import android.os.AsyncTask;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.medical.diabetes.MainActivity;
import com.medical.diabetes.models.Reminder;

import java.util.ArrayList;

public class ReadRemindersTableTask extends AsyncTask<Void, Void, Void> {
    private ArrayList<Reminder> remindersList;
    private RecyclerView recyclerView;
    private TextView rapport;


    public ReadRemindersTableTask(ArrayList<Reminder> remindersList, RecyclerView recyclerView, TextView rapport) {
        this.remindersList = remindersList;
        this.recyclerView = recyclerView;
        this.rapport = rapport;

    }

    @Override
    protected Void doInBackground(Void... voids) {
        Cursor cursor = MainActivity.readableDB.query(DataBaseTablesSchemas.RemindersTable.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null);

        while (cursor.moveToNext()) {
            //
            int _ID = cursor.getInt(cursor.getColumnIndexOrThrow(DataBaseTablesSchemas.RemindersTable._ID));
            String title = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseTablesSchemas.RemindersTable.COLUMN_TITLE));
            int state = cursor.getInt(cursor.getColumnIndexOrThrow(DataBaseTablesSchemas.RemindersTable.COLUMN_STATE));
            String time = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseTablesSchemas.RemindersTable.COLUMN_TIME));
            String days = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseTablesSchemas.RemindersTable.COLUMN_DAYS));

            remindersList.add(new Reminder(_ID, title, state, time, days));

        }


        return null;
    }


    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);

        recyclerView.getAdapter().notifyDataSetChanged();
        // afficher le rapport
        if (remindersList.size() != 0)
            rapport.setText("Vous avez " + remindersList.size() + " alarme(s) dans cette liste...");
        else rapport.setText("Ajoutez des alarmes pour les voir en haut...");


    }
}//
