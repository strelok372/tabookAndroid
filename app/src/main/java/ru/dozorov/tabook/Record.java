package ru.dozorov.tabook;

import java.time.LocalDateTime;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@Entity(tableName = "records")
public class Record {

    @PrimaryKey(autoGenerate = true)
    private Long id;

    //для изменения id в случае с синхронизацией с сервером
    public void setId(Long id) {
        this.id = id;
    }

    @NonNull
    private Long userId;
    @NonNull
    private String title;
    @NonNull
    private String text;
    @NonNull
    private String tags;
    @NonNull
    private LocalDateTime dateTime;

}
