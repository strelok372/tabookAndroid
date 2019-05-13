package com.example.myapplication;

import android.content.ClipData;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    SQLiteDatabase database;
    private RecyclerView rvRecords;
    private NumbersAdapter numbersAdapter;

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
                startActivity(intent);
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

    }

    public void onResume(){
        super.onResume();
        numbersAdapter.swapCursor(getAllItems());
    }

    private Cursor getAllItems(){
        return database.query(
                RecordsContract.RecordEntry.TABLE_NAME,null,null,null,null,null, RecordsContract.RecordEntry.KEY_TIMESTAMP + " DESC");
    }
}
