package ru.dozorov.tabook.views;

import androidx.lifecycle.Observer;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import java.util.List;

import androidx.lifecycle.ViewModelProviders;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.dozorov.tabook.RecordViewModel;
import ru.dozorov.tabook.deprecated.DBhelper;
import ru.dozorov.tabook.NumbersAdapter;
import ru.dozorov.tabook.R;
import ru.dozorov.tabook.deprecated.RecordsContract;
import ru.dozorov.tabook.retrofit.RetrofitService;
import ru.dozorov.tabook.Record;
import ru.dozorov.tabook.room.RecordDatabase;

public class MainActivity extends AppCompatActivity {
    private RecordViewModel viewModel;
    public static String UserId;
    SQLiteDatabase database;

    private RecyclerView rvRecords;
    private NumbersAdapter numbersAdapter;

    boolean isSynchronized = false;

    public static final String APP_PREFERENCES = "mysettings";
    public static final String APP_PREFERENCES_SYNC = "s";
    final private int AUTH_CODE = 5;
    private SharedPreferences mSettings;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu, menu);
        //return true;

        MenuItem mI = menu.findItem(R.id.mi_find_record);
        SearchView sv = (SearchView) mI.getActionView();
        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            public boolean onQueryTextSubmit(String s) {
                //здесь скорее всего ничего
                return false;
            }

            public boolean onQueryTextChange(String s) {
                //здесь будет поиск по коллекции существующих элементов
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.mi_settings:
                Intent intent = new Intent(this, AuthPage.class);
                startActivityForResult(intent, AUTH_CODE);
                break;
            default:
                super.onOptionsItemSelected(item);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setSupportActionBar((Toolbar) findViewById(R.id.my_toolbar));

        DBhelper dBhelper = new DBhelper(this);
        database = dBhelper.getWritableDatabase();

        rvRecords = findViewById(R.id.rv_records);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rvRecords.setLayoutManager(layoutManager);
        rvRecords.setHasFixedSize(true);

        numbersAdapter = new NumbersAdapter(this, getAllItems());
        rvRecords.setAdapter(numbersAdapter);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getApplicationContext(), 1);
        rvRecords.addItemDecoration(dividerItemDecoration);

        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if (account != null){
            UserId = account.getId();
        }

        mSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);

        viewModel = ViewModelProviders.of(this).get(RecordViewModel.class);
        viewModel.getAllRecords().observe(this, new Observer<List<Record>>() {
            @Override
            public void onChanged(@Nullable List<Record> records) {
                //update RecyclerView
            }
        });

//        updateUI(account);

//        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback() {
//            @Override
//            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
//                return false;
//            }
//
//            @Override
//            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
//
//            }
//        })
    }

    public void onClick(View view) {
        switch (view.getId()){
            case(R.id.floatingActionButton):
                Intent intent = new Intent(this, NewRecordAdd.class);
                startActivity(intent);
                //setContentView(R.layout.new_record);
                break;
            case(R.id.declineAdding):
                //setContentView(R.layout.activity_main);
                //setSupportActionBar((Toolbar) findViewById(R.id.my_toolbar));
                break;
        }
    }

    public void updateUI(){
        RecordDatabase r = RecordDatabase.getInstance(this);
//        r.dao().update();
    }

    public void onResume(){
        super.onResume();
        numbersAdapter.swapCursor(getAllItems());
    }

    private Cursor getAllItems(){
        return database.query(
                RecordsContract.RecordEntry.TABLE_NAME,null,null,null,null,null, RecordsContract.RecordEntry.KEY_TIMESTAMP + " DESC");
    }

    private void getAllRecords(){

    }

    @Override
    protected void onStop() {
        super.onStop();
        mSettings.edit().putBoolean(APP_PREFERENCES_SYNC, isSynchronized).apply();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case AUTH_CODE:
                if (resultCode == RESULT_OK){
                    getAllRecords();
                }
                break;
        }

    }
}
