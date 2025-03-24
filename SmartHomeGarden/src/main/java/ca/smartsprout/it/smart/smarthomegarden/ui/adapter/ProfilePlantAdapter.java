/**
 * Smart Sprout
 * Members:
 * 1. Aditi Patel, n01525570, CENG322-RCB
 * 2. Birava Prajapati, n01579924, CENG322-RCA
 * 3. Darshankumar Prajapati, n01574247, CENG322-RCB
 * 4. Zeel Patel, n01526282, CENG322-RCB
 */

package ca.smartsprout.it.smart.smarthomegarden.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ca.smartsprout.it.smart.smarthomegarden.R;
import ca.smartsprout.it.smart.smarthomegarden.data.model.Plant;
import ca.smartsprout.it.smart.smarthomegarden.data.model.SensorData;

public class ProfilePlantAdapter extends RecyclerView.Adapter<ProfilePlantAdapter.PlantViewHolder> {

    private List<Plant> plantList;
    private OnPlantClickListener listener = null;
    private Map<String, SensorData> sensorDataMap = new HashMap<>();

    public ProfilePlantAdapter(List<Plant> plantList) {
        this.plantList = plantList;
    }

    public void updatePlantList(List<Plant> updatedList) {
        this.plantList.clear();
        this.plantList.addAll(updatedList);
        notifyDataSetChanged();
    }

    public void updateSensorDataMap(Map<String, SensorData> newMap) {
        this.sensorDataMap = newMap;
        notifyDataSetChanged();
    }

    public void setOnPlantClickListener(OnPlantClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public PlantViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_plant, parent, false);
        return new PlantViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlantViewHolder holder, int position) {
        Plant plant = plantList.get(position);

        holder.plantName.setText(plant.getCustomName() != null ? plant.getCustomName() : plant.getName());
        holder.plantSpecies.setText(plant.getName());

        if (plant.getDateAdded() != null) {
            holder.dateAdded.setText(plant.getDateAdded().toString());
        } else {
            holder.dateAdded.setText(R.string.no_date_provided);
        }

        SensorData sensor = sensorDataMap.get(plant.getId());
        if (sensor != null) {
            holder.moisture.setText("Moisture: " + sensor.getMoisture() + "%");
            holder.temperature.setText("Temp: " + sensor.getTemperature() + "°C");
            holder.sunlight.setText("Light: " + sensor.getLux() + " lx");
        } else {
            holder.moisture.setText("Moisture: --%");
            holder.temperature.setText("Temp: --°C");
            holder.sunlight.setText("Light: -- lx");
        }

        holder.cardView.setOnClickListener(v -> {
            if (listener != null) listener.onPlantClick(plant);
        });
    }

    @Override
    public int getItemCount() {
        return plantList != null ? plantList.size() : 0;
    }

    static class PlantViewHolder extends RecyclerView.ViewHolder {

        TextView plantName, plantSpecies, dateAdded;
        TextView moisture, temperature, sunlight;
        MaterialCardView cardView;

        public PlantViewHolder(@NonNull View itemView) {
            super(itemView);
            plantName = itemView.findViewById(R.id.plantName);
            plantSpecies = itemView.findViewById(R.id.customName);
            dateAdded = itemView.findViewById(R.id.dateAdded);
            cardView = itemView.findViewById(R.id.cardView);
            moisture = itemView.findViewById(R.id.moistureLevel);
            temperature = itemView.findViewById(R.id.temperature);
            sunlight = itemView.findViewById(R.id.lightExposure);
        }
    }

    public interface OnPlantClickListener {
        void onPlantClick(Plant plant);
    }
}
