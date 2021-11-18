package com.sofe4640.locationfinder;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class UpdateLocationActivity extends AppCompatActivity {

    String address;
    double latitude;
    double longitude;
    int id;

    EditText addr;
    EditText lat;
    EditText longit;
    DBHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_location);

        // Set variables to current edit text input
        this.addr = findViewById(R.id.updateAddress);
        this.lat = findViewById(R.id.updateLatitude);
        this.longit = findViewById(R.id.updateLongitude);

        db = new DBHandler(this);

        // Get values passed from main activity through intent
        Intent intent = getIntent();
        this.address = intent.getStringExtra("address");
        this.latitude = Double.parseDouble(intent.getStringExtra("latitude"));
        this.longitude = Double.parseDouble(intent.getStringExtra("longitude"));
        this.id = db.getLocationId(latitude, longitude);

        // Set edit text fields to these values
        addr.setText(address);
        lat.setText(String.valueOf(latitude));
        longit.setText(String.valueOf(longitude));

        // Update button updated db
        Button updateBtn = findViewById(R.id.update_btn);
        updateBtn.setOnClickListener(v -> {
            updateLocation();
        });

        // Delete button deletes the entry from db
        Button deleteBtn = findViewById(R.id.delete_btn);
        deleteBtn.setOnClickListener(v -> {
            deleteLocation();
        });
    }

    // Utility method for updating
    public void updateLocation() {
        if (addr.getText().toString().trim().isEmpty() ||
            lat.getText().toString().trim().isEmpty() ||
            longit.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "Oh-oh! Make sure you provide address, latitude and longitude!", Toast.LENGTH_SHORT).show();
        } else {
            db.updateLocation(addr.getText().toString(), Double.valueOf(lat.getText().toString()), Double.valueOf(longit.getText().toString()));
            Toast.makeText(this, "Updated!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
    }

    // Utility method for deleting
    public void deleteLocation() {
        db.deleteLocation(addr.getText().toString());
        Toast.makeText(this, "Deleted!", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}