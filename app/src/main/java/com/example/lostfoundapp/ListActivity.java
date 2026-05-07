package com.example.lostfoundapp;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.*;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class ListActivity extends AppCompatActivity {

    DBHelper dbHelper;
    ListView listView;
    SearchView searchView;

    ArrayList<String> list;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        dbHelper = new DBHelper(this);

        listView = findViewById(R.id.listView);
        searchView = findViewById(R.id.searchView);

        loadData("");

        // SEARCH FILTER
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                loadData(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                loadData(newText);
                return false;
            }
        });

        // CLICK ITEM  OPEN DETAIL
        listView.setOnItemClickListener((parent, view, position, id) -> {
            String selected = list.get(position);

            // extract ID from text
            int itemId = Integer.parseInt(selected.split("-")[0]);

            Intent intent = new Intent(this, DetailActivity.class);
            intent.putExtra("id", itemId);
            startActivity(intent);
        });
    }

    private void loadData(String filter) {

        list = new ArrayList<>();

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT * FROM items WHERE category LIKE ?",
                new String[]{"%" + filter + "%"}
        );

        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String name = cursor.getString(2);
            String category = cursor.getString(7);

            list.add(id + "-" + name + " (" + category + ")");
        }

        adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, list);

        listView.setAdapter(adapter);
    }
}