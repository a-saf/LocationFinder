package com.sofe4640.locationfinder;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class NewLocationActivity extends AppCompatActivity {

    private EditText address, latitude, longitude;
    DBHandler db;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_location_activity);

        db  = new DBHandler(this);
        // Assign edit text boxes to variables
        address = findViewById(R.id.editAddress);
        latitude = findViewById(R.id.editLatitude);
        longitude = findViewById(R.id.editLongitude);

        // When save button is clicked save the text input to database
        FloatingActionButton saveButton = findViewById(R.id.save_btn);
        saveButton.setOnClickListener(view -> {
            boolean saved = saveLocation();
            if (saved) {
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);
            }

        });
    }

    // Utility method to save input from edit text fields to database and do some basic troubleshooting
    private boolean saveLocation() {
        boolean status = false;
        double latDecimal;
        double longDecimal;
        String addStr;
        try {
            latDecimal = Double.parseDouble(latitude.getText().toString().trim());
            longDecimal = Double.parseDouble(longitude.getText().toString().trim());
            addStr = address.getText().toString().trim();

            boolean success = db.addLocation(addStr, latDecimal, longDecimal);

            if (success) {
                Toast.makeText(this, "Saved!", Toast.LENGTH_SHORT).show();
                status = true;
            }

        }
        catch (Exception e) {
            Toast.makeText(this, "Oh-oh! Make sure you entered correct values.", Toast.LENGTH_SHORT).show();
            System.out.println(e.getMessage());
        }
        return status;
    }


}
