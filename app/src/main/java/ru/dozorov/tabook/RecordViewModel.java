package ru.dozorov.tabook;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.annotation.NonNull;

import java.util.List;

public class RecordViewModel extends AndroidViewModel {
    private RecordRepository recordRepository;
    private LiveData<List<Record>> records;

    public RecordViewModel(@NonNull Application application) {
        super(application);
        recordRepository = new RecordRepository(application);
        records = recordRepository.getAllRecords();
    }

    public void insert(Record record) {
        recordRepository.insert(record);
    }
    public void update(Record record) {
        recordRepository.update(record);
    }
    public void delete(Record record) {
        recordRepository.delete(record);
    }
//    public void deleteAll() {
//        recordRepository.deleteAll();
//    }
    public LiveData<List<Record>> getAllRecords() {
        return records;
    }
    public LiveData<List<Record>> getRecordsFromServer(){
        recordRepository.getRecordsFromServer();
        //records = recordRepository.getAllRecords(); ПОД ВОПРОСОМ!!!
        return records;
    };
}
