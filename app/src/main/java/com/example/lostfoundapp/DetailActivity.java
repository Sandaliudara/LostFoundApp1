package com.example.lostfoundapp;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;

public class DetailActivity extends AppCompatActivity {

    DBHelper dbHelper;
    TextView txtDetails;
    Button btnDelete;

    int itemId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        dbHelper = new DBHelper(this);

        txtDetails = findViewById(R.id.txtDetails);
        btnDelete = findViewById(R.id.btnDelete);

        itemId = getIntent().getIntExtra("id", -1);

        loadDetails();

        btnDelete.setOnClickListener(v -> {
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            db.delete("items", "id=?", new String[]{String.valueOf(itemId)});

            Toast.makeText(this, "Deleted!", Toast.LENGTH_SHORT).show();
            finish();
        });
    }

    private void loadDetails() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT * FROM items WHERE id=?",
                new String[]{String.valueOf(itemId)}
        );

        if (cursor.moveToFirst()) {
            String details =
                    "Type: " + cursor.getString(1) + "\n" +
                            "Name: " + cursor.getString(2) + "\n" +
                            "Phone: " + cursor.getString(3) + "\n" +
                            "Description: " + cursor.getString(4) + "\n" +
                            "Date: " + cursor.getString(5) + "\n" +
                            "Location: " + cursor.getString(6) + "\n" +
                            "Category: " + cursor.getString(7);

            txtDetails.setText(details);
        }
    }
}