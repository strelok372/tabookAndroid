package ru.dozorov.tabook.views;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import ru.dozorov.tabook.deprecated.DBhelper;
import ru.dozorov.tabook.R;

import static ru.dozorov.tabook.deprecated.RecordsContract.RecordEntry.KEY_DESCRIPTION;
import static ru.dozorov.tabook.deprecated.RecordsContract.RecordEntry.KEY_NAME;
import static ru.dozorov.tabook.deprecated.RecordsContract.RecordEntry.KEY_TAGS;
import static ru.dozorov.tabook.deprecated.RecordsContract.RecordEntry.TABLE_NAME;

public class NewRecordAdd extends AppCompatActivity {

    DBhelper dBhelper;
    EditText etName;
    EditText etTags;
    EditText etDescription;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_record);
        dBhelper = new DBhelper(this);

        etName = (EditText)findViewById(R.id.nameOfRecord);
        etTags = (EditText)findViewById(R.id.tagsOfRecord);
        etDescription = (EditText)findViewById(R.id.descriptionOfRecord);
    }
    public void onClick(View view){

        SQLiteDatabase sqLiteDatabase = dBhelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        switch (view.getId()){
            case R.id.addNewRecord:
                if (etName.getText().toString().trim().length() == 0 || etTags.getText().toString().trim().length() == 0 || etDescription.getText().toString().trim().length() == 0){
                    return;
                }
                contentValues.put(KEY_NAME, etName.getText().toString());
                contentValues.put(KEY_TAGS, etTags.getText().toString());
                contentValues.put(KEY_DESCRIPTION, etDescription.getText().toString());
                sqLiteDatabase.insert(TABLE_NAME, null, contentValues);
                finish();
                break;
            case R.id.addPicture:
                Cursor cursor = sqLiteDatabase.query(TABLE_NAME, null,null,null, null, null, null);
                if (cursor.moveToFirst())
                    do {
                        etName.setText(cursor.getString(cursor.getColumnIndex(KEY_NAME)));
                        etTags.setText(cursor.getString(cursor.getColumnIndex(KEY_TAGS)));
                        etDescription.setText(cursor.getString(cursor.getColumnIndex(KEY_DESCRIPTION)));
                        Log.d("MY_LOG: ", cursor.getString(cursor.getColumnIndex(KEY_NAME)) + cursor.getString(cursor.getColumnIndex(KEY_TAGS)) + cursor.getString(cursor.getColumnIndex(KEY_DESCRIPTION)));
                    }
                    while (cursor.moveToNext());
                cursor.close();
                break;
            case R.id.declineAdding:
                finish();
                break;
        }
        sqLiteDatabase.close();
    }

}
