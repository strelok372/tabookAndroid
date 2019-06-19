package ru.dozorov.tabook.room;

import androidx.lifecycle.LiveData;

import java.util.List;

import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import ru.dozorov.tabook.Record;

public interface RecordDao {
    @Insert
    void insert(Record record);

    @Update
    void update(Record record);

    @Delete
    void delete(Record record);

    @Query("DELETE FROM records")
    void deleteAll();

    @Query("SELECT * FROM records ORDER BY dateTime DESC")
    LiveData<List<Record>> getAllRecords();
}
