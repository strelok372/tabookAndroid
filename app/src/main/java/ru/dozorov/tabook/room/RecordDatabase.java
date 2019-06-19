package ru.dozorov.tabook.room;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import ru.dozorov.tabook.Record;

@Database(entities = Record.class, version = 1)
public abstract class RecordDatabase extends RoomDatabase {

    public abstract RecordDao dao();

    private static RecordDatabase instance;
    public static synchronized RecordDatabase getInstance(Context context){
        if (instance == null) {
            instance = Room
                    .databaseBuilder(context.getApplicationContext(), RecordDatabase.class, "record_database")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
}
