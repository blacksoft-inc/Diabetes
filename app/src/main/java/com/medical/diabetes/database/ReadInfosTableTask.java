package com.medical.diabetes.database;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.text.Html;
import android.util.Log;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.medical.diabetes.MainActivity;
import com.medical.diabetes.fragments.glecemia_level;
import com.medical.diabetes.fragments.infos;
import com.medical.diabetes.models.glecemiaModels.DateTimeHeader;
import com.medical.diabetes.models.glecemiaModels.GlecemiaModel;
import com.medical.diabetes.models.infosLine.HeaderInfos;
import com.medical.diabetes.models.infosLine.InfosModel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.util.ArrayList;

public class ReadInfosTableTask extends AsyncTask<Void, Void, Void> {
    private ArrayList<HeaderInfos> infos;
    private SQLiteDatabase readableDB;
    private RecyclerView infosList;

    public ReadInfosTableTask(ArrayList<HeaderInfos> infos, SQLiteDatabase readableDB, RecyclerView infosList) {
        this.infos = infos;
        this.readableDB = readableDB;
        this.infosList = infosList;
    }

    //reading data in background task to prevent the main thread from crashing
    @Override
    protected Void doInBackground(Void... voids) {
        // reading all table from DB

        Cursor cursor = readableDB.query(DataBaseTablesSchemas.InfosTable.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null);

        // reading infos line per line
        while (cursor.moveToNext()) {
            //
            HeaderInfos infoLine;
            //
            int header = cursor.getInt(cursor.getColumnIndexOrThrow(DataBaseTablesSchemas.InfosTable.COLUMN_HEADER));
            String header_type = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseTablesSchemas.InfosTable.COLUMN_HEADER_TYPE));


            //in case it's not a header
            int must_be_filled = cursor.getInt(cursor.getColumnIndexOrThrow(DataBaseTablesSchemas.InfosTable.COLUMN_MUST_BE_FILLED));

            //
            String infoName = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseTablesSchemas.InfosTable.COLUMN_INFO_NAME));
            String content = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseTablesSchemas.InfosTable.COLUMN_CONTENT));
            String content_type = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseTablesSchemas.InfosTable.COLUMN_CONTENT_TYPE));
            //
            infoLine = new InfosModel(header, header_type, content_type, infoName, content, must_be_filled);
            infos.add(infoLine);
            //
        }

        cursor.close();

        return null;
    }

    /**/

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        if (infosList != null) infosList.getAdapter().notifyDataSetChanged();

    }
}//