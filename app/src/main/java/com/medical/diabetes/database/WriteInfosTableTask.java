package com.medical.diabetes.database;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;

import com.medical.diabetes.MainActivity;
import com.medical.diabetes.models.infosLine.HeaderInfos;
import com.medical.diabetes.models.infosLine.InfosModel;

import java.util.ArrayList;

public class WriteInfosTableTask extends AsyncTask<Void, Void, Void> {
    private ArrayList<HeaderInfos> infos;
    private SQLiteDatabase writableDB;
    private boolean update = true;

    /**/
    public WriteInfosTableTask(ArrayList<HeaderInfos> infos, SQLiteDatabase writableDB, boolean update) {
        this.infos = infos;
        this.writableDB = writableDB;
        this.update = update;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        //filling the content to be stored in DB
        for (int i = 0; i < infos.size(); i++) {
            ContentValues values = new ContentValues();
            HeaderInfos info = infos.get(i);

            //
            values.put(DataBaseTablesSchemas.InfosTable._ID, i);
            values.put(DataBaseTablesSchemas.InfosTable.COLUMN_HEADER, info.header);
            values.put(DataBaseTablesSchemas.InfosTable.COLUMN_HEADER_TYPE, info.headerType);
            //
            if (info.header == 1) {
                values.put(DataBaseTablesSchemas.InfosTable.COLUMN_INFO_NAME, "");
                values.put(DataBaseTablesSchemas.InfosTable.COLUMN_CONTENT_TYPE, "");
                values.put(DataBaseTablesSchemas.InfosTable.COLUMN_CONTENT, "");
                values.put(DataBaseTablesSchemas.InfosTable.COLUMN_MUST_BE_FILLED, 0);

            } else {
                InfosModel infosModel = (InfosModel) info;
                values.put(DataBaseTablesSchemas.InfosTable.COLUMN_INFO_NAME, infosModel.infosName);
                values.put(DataBaseTablesSchemas.InfosTable.COLUMN_CONTENT_TYPE, infosModel.contentType); /*convert char into string*/
                values.put(DataBaseTablesSchemas.InfosTable.COLUMN_CONTENT, infosModel.content);
                values.put(DataBaseTablesSchemas.InfosTable.COLUMN_MUST_BE_FILLED, infosModel.must_be_filled);/*converting boolean into integer*/
            }


            if (update) {
                if (info.header != 1) {
                    writableDB.update(DataBaseTablesSchemas.InfosTable.TABLE_NAME, values, "_id = " + i, null);
                }
            } else {
                writableDB.insert(DataBaseTablesSchemas.InfosTable.TABLE_NAME, null, values);

            }

        }

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
    }
}//
