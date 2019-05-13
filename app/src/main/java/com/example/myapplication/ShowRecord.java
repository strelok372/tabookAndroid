package com.example.myapplication;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class ShowRecord extends AppCompatActivity {
    Cursor mCursor;
    DBhelper dBhelper;
    SQLiteDatabase database;
    int a;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_record);


        //mCursor = database.query(RecordsContract.RecordEntry.TABLE_NAME,null,null,null,null,null,null);

        a = getIntent().getIntExtra("adapterpos", 0);
        dBhelper = new DBhelper(this);
        database = dBhelper.getWritableDatabase();
        //mCursor = database.query(RecordsContract.RecordEntry.TABLE_NAME,null,null,null,null,null,null);

        mCursor = database.query(RecordsContract.RecordEntry.TABLE_NAME,null,null,null,null,null, RecordsContract.RecordEntry.KEY_TIMESTAMP + " DESC");
        if (!mCursor.moveToPosition(a)){
            return;
        }

        ((TextView) findViewById(R.id.tv_show_name)).setText(mCursor.getString(mCursor.getColumnIndex(RecordsContract.RecordEntry.KEY_NAME)));
        ((TextView) findViewById(R.id.tv_show_tags)).setText(mCursor.getString(mCursor.getColumnIndex(RecordsContract.RecordEntry.KEY_TAGS)));
        ((TextView) findViewById(R.id.tv_show_description)).setText(mCursor.getString(mCursor.getColumnIndex(RecordsContract.RecordEntry.KEY_DESCRIPTION)));
    }
    ShowRecord(){
//        dBhelper = new DBhelper(this);
//        database = dBhelper.getWritableDatabase();
//        mCursor = database.query(RecordsContract.RecordEntry.TABLE_NAME,null,null,null,null,null,null);
//
////        mCursor = sqLiteDatabase.query(RecordsContract.RecordEntry.TABLE_NAME,null,null,null,null,null, RecordsContract.RecordEntry.KEY_TIMESTAMP + " DESC");
//        if (!mCursor.moveToPosition(a)){
//            return;
//        }
//
//        ((TextView) findViewById(R.id.tv_show_name)).setText(mCursor.getString(mCursor.getColumnIndex(RecordsContract.RecordEntry.KEY_NAME)));
//        ((TextView) findViewById(R.id.tv_show_tags)).setText(mCursor.getString(mCursor.getColumnIndex(RecordsContract.RecordEntry.KEY_TAGS)));
//        ((TextView) findViewById(R.id.tv_show_description)).setText(mCursor.getString(mCursor.getColumnIndex(RecordsContract.RecordEntry.KEY_DESCRIPTION)));
    }
}
