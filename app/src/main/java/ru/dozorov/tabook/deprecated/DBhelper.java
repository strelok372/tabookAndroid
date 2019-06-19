package ru.dozorov.tabook.deprecated;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBhelper extends SQLiteOpenHelper{

    public DBhelper(Context context){
        super(context, RecordsContract.RecordEntry.DATABASE_NAME, null, 1);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + RecordsContract.RecordEntry.TABLE_NAME
                + " (id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + RecordsContract.RecordEntry.KEY_NAME + " TEXT,"
                + RecordsContract.RecordEntry.KEY_TAGS + " TEXT,"
                + RecordsContract.RecordEntry.KEY_DESCRIPTION + " TEXT,"
                + RecordsContract.RecordEntry.KEY_TIMESTAMP + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP);"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + RecordsContract.RecordEntry.TABLE_NAME);
            onCreate(db);
    }
}

