package com.medical.diabetes.database;

import android.database.Cursor;
import android.os.AsyncTask;
import android.util.Log;

import androidx.recyclerview.widget.RecyclerView;

import com.medical.diabetes.MainActivity;
import com.medical.diabetes.models.glecemiaModels.DateTimeHeader;
import com.medical.diabetes.models.glecemiaModels.GlecemiaModel;

import java.util.ArrayList;

public class ReadGlecemiaTableTask extends AsyncTask<Void, Void, Void> {
    private ArrayList<DateTimeHeader> list;
    private RecyclerView.Adapter adapter;

    //
    public ReadGlecemiaTableTask(ArrayList<DateTimeHeader> list, RecyclerView.Adapter adapter) {
        this.list = list;
        this.adapter = adapter;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        //
        Cursor cursor = MainActivity.readableDB.query(DataBaseTablesSchemas.GlecemiaMesuresTable.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null);

        while (cursor.moveToNext()) {
            long _id = cursor.getLong(cursor.getColumnIndex(DataBaseTablesSchemas.GlecemiaMesuresTable._ID));
            int header = cursor.getInt(cursor.getColumnIndex(DataBaseTablesSchemas.GlecemiaMesuresTable.COLUMN_HEADER));
            String dateTime = cursor.getString(cursor.getColumnIndex(DataBaseTablesSchemas.GlecemiaMesuresTable.COLUMN_DATE_TIME));
            //
            if (header == 1) {
                list.add(new DateTimeHeader(_id, 1, dateTime));
                continue;
            }
            list.add(new GlecemiaModel(_id, dateTime,
                    cursor.getDouble(cursor.getColumnIndex(DataBaseTablesSchemas.GlecemiaMesuresTable.COLUMN_TAUX_APRES_REPAS)),
                    cursor.getDouble(cursor.getColumnIndex(DataBaseTablesSchemas.GlecemiaMesuresTable.COLUMN_TAUX_APRES_REPAS)),
                    cursor.getDouble(cursor.getColumnIndex(DataBaseTablesSchemas.GlecemiaMesuresTable.COLUMN_DOZE_INSULINE_INJECTEE)),
                    cursor.getString(cursor.getColumnIndex(DataBaseTablesSchemas.GlecemiaMesuresTable.COLUMN_TIME))));

        }
        cursor.close();
        return null;
    }


    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        if (!list.isEmpty() && adapter != null) adapter.notifyDataSetChanged();
    }
}//
