package com.sofe4640.locationfinder;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RVAdapter extends RecyclerView.Adapter<RVAdapter.LocationViewHolder>{

    private final ArrayList<Location> locations;
    private LocationViewHolder.OnLocationItemListener olListener;

    public RVAdapter(ArrayList<Location> locations, LocationViewHolder.OnLocationItemListener olListener) {
        this.locations = locations;
        this.olListener = olListener;
    }

    @NonNull
    @Override
    public LocationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new LocationViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.rv_locations_item,
                        parent,
                        false
                ), olListener
        );
    }

    @Override
    public void onBindViewHolder(@NonNull LocationViewHolder holder, int position) {
        holder.setLocation(locations.get(position));

    }

    @Override
    public int getItemCount() {
        return locations.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    static class LocationViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView address, coord;
        OnLocationItemListener olListener;

        LocationViewHolder(@NonNull View itemView, OnLocationItemListener olListener) {
            super(itemView);
            address = itemView.findViewById(R.id.address);
            coord = itemView.findViewById(R.id.lat_and_long);
            this.olListener = olListener;
            itemView.setOnClickListener(this);
        }

        // Render address and coordinates as list items visible on main activity view
        void setLocation(Location location) {
            address.setText(location.getAddress());
            coord.setText(String.valueOf(location.getLatitude()) + ", " + String.valueOf(location.getLongitude()));
        }

        // onClick listener configuration
        @Override
        public void onClick(View view) {
            olListener.onLocationItemClick(getAdapterPosition());
        }

        public interface OnLocationItemListener {
            void onLocationItemClick(int position);
        }
    }
}
