package com.sofe4640.locationfinder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Intent;
import android.database.Cursor;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.widget.SearchView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements RVAdapter.LocationViewHolder.OnLocationItemListener {

    private DBHandler db;
    private double[] coordinates;
    private StringBuilder sb;
    private String completeAddress;
    private RecyclerView locationsRecyclerView;
    private ArrayList<Location> locationsList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new DBHandler(this);
        Geocoder gc = new Geocoder(this);

        // Read the 50 pairs of latitude/longitude coordinates from a text file and save them to DB
        try {
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(getAssets().open("locations.txt")));
            for (String line = reader.readLine(); line != null; line = reader.readLine()) {
                // latitude at index 0, longitude at index 1
                coordinates = new double[2];
                coordinates[0] = Double.parseDouble(line.split(", ")[0]);
                coordinates[1] = Double.parseDouble(line.split(", ")[1]);

                List<Address> addresses  = gc.getFromLocation(coordinates[0],coordinates[1], 1);
                sb = new StringBuilder();
                sb.append(addresses.get(0).getAddressLine(0).split(",")[0] + ",\n");
                sb.append(addresses.get(0).getLocality() + ",");
                sb.append(addresses.get(0).getAdminArea() + ",\n");
                sb.append(addresses.get(0).getPostalCode() + ",\n");
                sb.append(addresses.get(0).getCountryName());

                completeAddress = sb.toString();
                db.addLocation(completeAddress, coordinates[0], coordinates[1]);
            }

            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Render the recycler list view (all records in DB are shown)
        locationsRecyclerView = findViewById(R.id.rv_locations);
        displayLocations("");

        // Got to new Location Activity view on button click
        FloatingActionButton addLocation = findViewById(R.id.add_btn);
        addLocation.setOnClickListener(v -> {
            Intent i = new Intent(getApplicationContext(), NewLocationActivity.class);
            startActivity(i);

        });

        // Search and show results when query is submitted to search view
        androidx.appcompat.widget.SearchView search = findViewById(R.id.searchbox);
        search.setOnQueryTextListener(new androidx.appcompat.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                displayLocations(newText);
                return false;
            }
        });
    }

    // Utility functions to render and display the list of locations
    private void displayView() {
        RVAdapter locationAdapter = new RVAdapter(locationsList, this);
        locationsRecyclerView.setAdapter(locationAdapter);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        locationsRecyclerView.setLayoutManager(layoutManager);
    }

    private void displayLocations(String query) {
        populateLocationList(query);

        displayView();
    }

    // Populate arraylist with entries from db
    private void populateLocationList(String query) {
        locationsList = new ArrayList<>();
        Cursor cursor = null;
        if(query.equals("")){
            cursor = db.getLocations();
        } else {
            cursor = db.searchAddress(query);
        }

        if (cursor.getCount() != 0) {
            while (cursor.moveToNext()) {
                Location location = new Location();
                location.setAddress(cursor.getString(1));
                location.setLatitude(Double.parseDouble(cursor.getString(2)));
                location.setLongitude(Double.parseDouble(cursor.getString(3)));

                locationsList.add(location);
            }
        }
    }

    // Save fields in an intent when a recycler list item (location) is clicked
    public void onLocationItemClick(int position) {
        Intent intent = new Intent(this, UpdateLocationActivity.class);
        intent.putExtra("address", locationsList.get(position).getAddress());
        intent.putExtra("latitude", String.valueOf(locationsList.get(position).getLatitude()));
        intent.putExtra("longitude", String.valueOf(locationsList.get(position).getLongitude()));
        startActivity(intent);
    }
}