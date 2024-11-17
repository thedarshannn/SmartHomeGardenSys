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

import com.google.android.material.chip.Chip;

import java.util.List;

import ca.smartsprout.it.smart.smarthomegarden.R;
import ca.smartsprout.it.smart.smarthomegarden.data.model.Plant;

public class PlantAdapter extends RecyclerView.Adapter<PlantAdapter.PlantViewHolder> {

    private List<Plant> plantList;

    public PlantAdapter(List<Plant> plantList) {
        this.plantList = plantList;
    }

    @NonNull
    @Override
    public PlantViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.plant_rv_layout, parent, false);
        return new PlantViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlantViewHolder holder, int position) {
        Plant plant = plantList.get(position);

        // Set plant name and description (cycle information in place of description)
        holder.plantName.setText(plant.getCommon_name());
        holder.plantDescription.setText(plant.getCycle());

        // Set the watering Chip text and visibility
        holder.wateringChip.setText(plant.getWatering());
        holder.wateringChip.setVisibility(plant.getWatering() != null ? View.VISIBLE : View.GONE);

        // Example: For simplicity, setting static values to sunlightChip and indoorOutdoorChip as placeholders
        holder.sunlightChip.setText(plant.getSunlight().get(0));  // Customize as needed based on actual data
        holder.indoorOutdoorChip.setText(R.string.indoor);  // Placeholder text
    }

    @Override
    public int getItemCount() {
        return plantList != null ? plantList.size() : 0;
    }

    public void updatePlantList(List<Plant> plants) {
        this.plantList.clear();
        this.plantList.addAll(plants);
        notifyDataSetChanged();
    }

    static class PlantViewHolder extends RecyclerView.ViewHolder {
        TextView plantName, plantDescription;
        Chip wateringChip, sunlightChip, indoorOutdoorChip;

        public PlantViewHolder(@NonNull View itemView) {
            super(itemView);
            plantName = itemView.findViewById(R.id.plantNameTV);
            plantDescription = itemView.findViewById(R.id.plantDescriptionTV);
            wateringChip = itemView.findViewById(R.id.wateringChip);
            sunlightChip = itemView.findViewById(R.id.sunlightChip);
            indoorOutdoorChip = itemView.findViewById(R.id.indoorOutdoorChip);
        }
    }
}
