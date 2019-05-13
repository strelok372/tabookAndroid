package com.example.myapplication;

import android.provider.BaseColumns;

public final class RecordsContract {

    private RecordsContract(){}

    public static class RecordEntry implements BaseColumns{
        public static final String DATABASE_NAME = "records";
        public static final String TABLE_NAME = "note";
        public static final String KEY_NAME = "name";
        public static final String KEY_TAGS = "tags";
        public static final String KEY_DESCRIPTION = "description";
        public static final String KEY_TIMESTAMP = "timestamp";
        public static final String ID = "id";
    }
}
