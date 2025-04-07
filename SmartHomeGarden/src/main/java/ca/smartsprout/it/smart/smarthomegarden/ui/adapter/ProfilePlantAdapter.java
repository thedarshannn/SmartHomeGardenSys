/**
 * Smart Sprout
 * Members:
 * 1. Aditi Patel, n01525570, CENG322-RCB
 * 2. Birava Prajapati, n01579924, CENG322-RCA
 * 3. Darshankumar Prajapati, n01574247, CENG322-RCB
 * 4. Zeel Patel, n01526282, CENG322-RCB
 */

package ca.smartsprout.it.smart.smarthomegarden.ui.adapter;

import android.annotation.SuppressLint;
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
import ca.smartsprout.it.smart.smarthomegarden.utils.Util;

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

    @SuppressLint("SetTextI18n")
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
            holder.moisture.setText("Moisture: " + Util.convertMoistureToPercentage(sensor.getMoisture()) + "%");
            holder.light.setText("Light: " + sensor.getLux() + " lx");
            holder.uv.setText("UV: " + Util.getUVLevelDescription(sensor.getUV()));
        } else {
            holder.moisture.setText("Moisture: --%");
            holder.light.setText("Light: -- lx");
            holder.uv.setText("UV: --");
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
        TextView moisture, light, uv;
        MaterialCardView cardView;

        public PlantViewHolder(@NonNull View itemView) {
            super(itemView);
            plantName = itemView.findViewById(R.id.plantName);
            plantSpecies = itemView.findViewById(R.id.customName);
            dateAdded = itemView.findViewById(R.id.dateAdded);
            cardView = itemView.findViewById(R.id.cardView);
            moisture = itemView.findViewById(R.id.moistureLevel);
            light = itemView.findViewById(R.id.lightExposure);
            uv = itemView.findViewById(R.id.uv);
        }
    }

    public interface OnPlantClickListener {
        void onPlantClick(Plant plant);
    }
}
