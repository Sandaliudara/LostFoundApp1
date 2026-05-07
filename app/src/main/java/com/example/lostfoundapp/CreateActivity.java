package com.example.lostfoundapp;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;

public class CreateActivity extends AppCompatActivity {

    DBHelper dbHelper;
    Uri imageUri;

    EditText etName, etPhone, etDesc, etDate, etLocation;
    Spinner spinnerCategory;
    ImageView imageView;
    RadioGroup radioGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);

        dbHelper = new DBHelper(this);

        // CONNECT VIEWS (CRITICAL)
        etName = findViewById(R.id.etName);
        etPhone = findViewById(R.id.etPhone);
        etDesc = findViewById(R.id.etDesc);
        etDate = findViewById(R.id.etDate);
        etLocation = findViewById(R.id.etLocation);
        spinnerCategory = findViewById(R.id.spinnerCategory);
        imageView = findViewById(R.id.imageView);
        radioGroup = findViewById(R.id.radioGroup);

        Button btnUpload = findViewById(R.id.btnUpload);
        Button btnSave = findViewById(R.id.btnSave);

        // Spinner
        String[] categories = {"Electronics", "Pets", "Wallets"};
        spinnerCategory.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, categories));

        // Upload image
        btnUpload.setOnClickListener(v -> {
            Toast.makeText(this, "Clicked", Toast.LENGTH_SHORT).show(); // DEBUG

            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            startActivity(intent);
        });

        // Save
        btnSave.setOnClickListener(v -> saveData());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            imageUri = data.getData();
            imageView.setImageURI(imageUri);
        }
    }

    private void saveData() {

        int selectedId = radioGroup.getCheckedRadioButtonId();

        if (selectedId == -1) {
            Toast.makeText(this, "Select Lost or Found", Toast.LENGTH_SHORT).show();
            return;
        }

        if (imageUri == null) {
            Toast.makeText(this, "Please upload an image", Toast.LENGTH_SHORT).show();
            return;
        }

        RadioButton radio = findViewById(selectedId);

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("type", radio.getText().toString());
        values.put("name", etName.getText().toString());
        values.put("phone", etPhone.getText().toString());
        values.put("description", etDesc.getText().toString());
        values.put("date", etDate.getText().toString());
        values.put("location", etLocation.getText().toString());
        values.put("category", spinnerCategory.getSelectedItem().toString());
        values.put("image", imageUri != null ? imageUri.toString() : "");
        values.put("timestamp", String.valueOf(System.currentTimeMillis()));

        db.insert("items", null, values);

        Toast.makeText(this, "Saved!", Toast.LENGTH_SHORT).show();
        finish();
    }
}