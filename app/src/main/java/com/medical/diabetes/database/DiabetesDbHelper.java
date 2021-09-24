package com.medical.diabetes.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DiabetesDbHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "FeedReader.db";

    public DiabetesDbHelper(Context activity) {
        super(activity, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**/
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + DataBaseTablesSchemas.InfosTable.TABLE_NAME + "( "
                + DataBaseTablesSchemas.InfosTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + DataBaseTablesSchemas.InfosTable.COLUMN_HEADER + " INTEGER, "
                + DataBaseTablesSchemas.InfosTable.COLUMN_HEADER_TYPE + " TEXT, "
                + DataBaseTablesSchemas.InfosTable.COLUMN_INFO_NAME + " TEXT, "
                + DataBaseTablesSchemas.InfosTable.COLUMN_CONTENT_TYPE + " TEXT, "
                + DataBaseTablesSchemas.InfosTable.COLUMN_CONTENT + " TEXT,"
                + DataBaseTablesSchemas.InfosTable.COLUMN_MUST_BE_FILLED + " INTEGER"
                + ");");

        db.execSQL("CREATE TABLE " + DataBaseTablesSchemas.RemindersTable.TABLE_NAME + "( "
                + DataBaseTablesSchemas.RemindersTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + DataBaseTablesSchemas.RemindersTable.COLUMN_TITLE + " TEXT, "
                + DataBaseTablesSchemas.RemindersTable.COLUMN_STATE + " INTEGER, "
                + DataBaseTablesSchemas.RemindersTable.COLUMN_TIME + " TEXT, "
                + DataBaseTablesSchemas.RemindersTable.COLUMN_DAYS + " TEXT"
                + ");");

        db.execSQL("CREATE TABLE " + DataBaseTablesSchemas.GlecemiaMesuresTable.TABLE_NAME + "( "
                + DataBaseTablesSchemas.GlecemiaMesuresTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + DataBaseTablesSchemas.GlecemiaMesuresTable.COLUMN_HEADER + " INTEGER DEFAULT 0, "
                + DataBaseTablesSchemas.GlecemiaMesuresTable.COLUMN_SENT_TO_DOCTOR + " INTEGER DEFAULT 0,"
                + DataBaseTablesSchemas.GlecemiaMesuresTable.COLUMN_DATE_TIME + " TEXT DEFAULT NULL, "
                + DataBaseTablesSchemas.GlecemiaMesuresTable.COLUMN_TIME + " TEXT DEFAULT NULL, "
                + DataBaseTablesSchemas.GlecemiaMesuresTable.COLUMN_TAUX_AVANT_REPAS + " REAL DEFAULT NULL, "
                + DataBaseTablesSchemas.GlecemiaMesuresTable.COLUMN_TAUX_APRES_REPAS + " REAL DEFAULT NULL, "
                + DataBaseTablesSchemas.GlecemiaMesuresTable.COLUMN_DOZE_INSULINE_INJECTEE + " REAL DEFAULT NULL"
                + ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DataBaseTablesSchemas.InfosTable.TABLE_NAME + ";");
        db.execSQL("DROP TABLE IF EXISTS " + DataBaseTablesSchemas.RemindersTable.TABLE_NAME + ";");
        db.execSQL("DROP TABLE IF EXISTS " + DataBaseTablesSchemas.GlecemiaMesuresTable.TABLE_NAME + ";");
        onCreate(db);
    }
}//
