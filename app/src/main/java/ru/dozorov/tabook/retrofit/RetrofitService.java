package ru.dozorov.tabook.retrofit;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import ru.dozorov.tabook.Record;

public interface RetrofitService {
    @GET("/records")
    Call<List<Record>> fetchAllUsers(); //получение всех записей

    @GET("/records")
    Call<List<Record>> getRecordsByUser(@Query("id") String UserId); //записи по id пользователя

    @POST("/records")
    Call<Record> addRecord(@Body Record record); //добавление новой записи

    @PATCH("/records/{userId}/{id}")
    Call<Record> updateRecord(@Path("id") Long id, @Body Record record);

    @DELETE("/records/{userId}/{id}")
    Call<Void> deleteRecord(@Path("id") Long id);
}
