package com.medical.diabetes.database;

import android.provider.BaseColumns;

public final class DataBaseTablesSchemas {

    private DataBaseTablesSchemas() {
    }

    public static final class InfosTable implements BaseColumns {
        public static final String TABLE_NAME = "infos";
        public static final String COLUMN_HEADER = "header";
        public static final String COLUMN_HEADER_TYPE = "header_type";
        public static final String COLUMN_INFO_NAME = "info_name";
        public static final String COLUMN_CONTENT_TYPE = "content_type";
        public static final String COLUMN_CONTENT = "content";
        public static final String COLUMN_MUST_BE_FILLED = "must_be_filled";
    }

    public static final class RemindersTable implements BaseColumns {
        public static final String TABLE_NAME = "reminders";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_STATE = "state";
        public static final String COLUMN_TIME = "time";
        public static final String COLUMN_DAYS = "days";

    }

    public static final class GlecemiaMesuresTable implements BaseColumns {
        public static final String TABLE_NAME = "glecemias";
        public static final String COLUMN_HEADER = "header";
        public static final String COLUMN_SENT_TO_DOCTOR = "sent";
        public static final String COLUMN_DATE_TIME = "datetime";
        public static final String COLUMN_TIME = "time";
        public static final String COLUMN_TAUX_AVANT_REPAS = "tauxavantrepas";
        public static final String COLUMN_TAUX_APRES_REPAS = "tauxapresrepas";
        public static final String COLUMN_DOZE_INSULINE_INJECTEE = "dozeinsuline";

    }

}//
