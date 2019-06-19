package ru.dozorov.tabook;

import android.app.Application;
import androidx.lifecycle.LiveData;
import android.os.AsyncTask;
import android.util.Log;
import android.view.ViewParent;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import androidx.lifecycle.MutableLiveData;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.dozorov.tabook.retrofit.RetrofitService;
import ru.dozorov.tabook.room.RecordDao;
import ru.dozorov.tabook.room.RecordDatabase;
import ru.dozorov.tabook.views.MainActivity;

public class RecordRepository {
    private RecordDao recordDao;
    private LiveData<List<Record>> records;
    private Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://10.0.0.2:8080")
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    RetrofitService retrofitService = retrofit.create(RetrofitService.class);
    String log = "my_DEBUG_log";

    public RecordRepository(Application application) {
        recordDao = RecordDatabase.getInstance(application).dao();
        records = recordDao.getAllRecords();
    }

    public void insert(Record record) {
        new InsertRecordAsync(recordDao).execute(record);
        if (MainActivity.UserId != null) addRec(record);
    }

    public void update(Record record) {
        new UpdateRecordAsync(recordDao).execute(record);
        if (MainActivity.UserId != null) updateRec(record.getId(), record);
    }

    public void delete(Record record) {
        new DeleteRecordAsync(recordDao).execute(record);
        if (MainActivity.UserId != null) deleteRec(record.getId());
    }

//    public void deleteAll(){
//        new DeleteAllRecordsAsync(recordDao).execute();
//        if (MainActivity.UserId != null) addRec();
//    }

    public LiveData<List<Record>> getAllRecords() {
        return records;
    }

    public void getRecordsFromServer(){
        List<Record> rec = getRecords(); //1:получение Листа с сервера
        if (rec != null && rec.size() > 0){
            long o = rec.size();
            for (Record r : records.getValue()){
                r.setId(++o); //обновление ID записи
                new UpdateRecordAsync(recordDao).execute(r); //обновление записи в БД
                addRec(r); //отправка записи на сервер
            }
        }
        //1:получение Листа с сервера 2:обновление записей в бд на телефоне 3:добавление в БД записей с сервера
    }

    public List<Record> getRecords(){
        Call<List<Record>> call = retrofitService.getRecordsByUser(MainActivity.UserId);
        final List<Record> list = new ArrayList<>();
        call.enqueue(new Callback<List<Record>>() {
            @Override
            public void onResponse(Call<List<Record>> call, Response<List<Record>> response) {
                if (!response.isSuccessful()){
                    Log.i(log, String.valueOf(response.code()));
                    return;
                }
                List<Record> temp = response.body();
                if (temp != null){
                    list.addAll(temp);
                }
            }
            @Override
            public void onFailure(Call<List<Record>> call, Throwable t) {
                Log.i(log, t.getMessage());
            }
        });
        return list;
    }

    public void addRec(Record record){
        Call<Record> call = retrofitService.addRecord(record);
        call.enqueue(new Callback<Record>() {
            @Override
            public void onResponse(Call<Record> call, Response<Record> response) {
                if (!response.isSuccessful()){
                    Log.i(log, String.valueOf(response.code()));
                } //ЗДЕСЬ БЫ КАКАЯ-НИБУДЬ РЕАКЦИЯ НА УСПЕШНЫЙ РЕЗУЛЬТАТ
            }
            @Override
            public void onFailure(Call<Record> call, Throwable t) {
                Log.i(log, t.getMessage());
            }
        });
    }

    public void updateRec(long id, Record record){
        Call<Record> call = retrofitService.updateRecord(id, record);
        call.enqueue(new Callback<Record>() {
            @Override
            public void onResponse(Call<Record> call, Response<Record> response) {
                if (!response.isSuccessful()){
                    Log.i(log, String.valueOf(response.code()));
                } //ЗДЕСЬ БЫ КАКАЯ-НИБУДЬ РЕАКЦИЯ НА УСПЕШНЫЙ РЕЗУЛЬТАТ
            }
            @Override
            public void onFailure(Call<Record> call, Throwable t) {
                Log.i(log, t.getMessage());
            }
        });
    }

    public void deleteRec(long id){
        Call<Void> call = retrofitService.deleteRecord(id);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (!response.isSuccessful()){
                    Log.i(log, String.valueOf(response.code()));
                } //ЗДЕСЬ БЫ КАКАЯ-НИБУДЬ РЕАКЦИЯ НА УСПЕШНЫЙ РЕЗУЛЬТАТ
            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.i(log, t.getMessage());
            }
        });
    }

    private static class InsertRecordAsync extends AsyncTask<Record, Void, Void> {
        private RecordDao recordDao;

        private InsertRecordAsync(RecordDao recordDao){
            this.recordDao = recordDao;
        }
        @Override
        protected Void doInBackground(Record... records) {
            recordDao.insert(records[0]);
            return null;
        }
    }

    private static class UpdateRecordAsync extends AsyncTask<Record, Void, Void> {
        private RecordDao recordDao;

        private UpdateRecordAsync(RecordDao recordDao){
            this.recordDao = recordDao;
        }
        @Override
        protected Void doInBackground(Record... records) {
            recordDao.update(records[0]);
            return null;
        }
    }

    private static class DeleteRecordAsync extends AsyncTask<Record, Void, Void> {
        private RecordDao recordDao;

        private DeleteRecordAsync(RecordDao recordDao){
            this.recordDao = recordDao;
        }
        @Override
        protected Void doInBackground(Record... records) {
            recordDao.delete(records[0]);
            return null;
        }
    }

//    private static class DeleteAllRecordsAsync extends AsyncTask<Void, Void, Void> {
//        private RecordDao recordDao;
//
//        private DeleteAllRecordsAsync(RecordDao recordDao){
//            this.recordDao = recordDao;
//        }
//        @Override
//        protected Void doInBackground(Void... voids) {
//            recordDao.deleteAll();
//            return null;
//        }
//    }
}
