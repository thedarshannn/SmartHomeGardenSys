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
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;


import java.util.List;

import ca.smartsprout.it.smart.smarthomegarden.R;
import ca.smartsprout.it.smart.smarthomegarden.data.model.Plant;
import ca.smartsprout.it.smart.smarthomegarden.ui.fragments.PlantDetailsBottomSheetFragment;

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
        holder.plantName.setText(plant.getName());

        // if description is not available, invisible the description text view and set the user friendly message
        if (plant.getDescription() != null) {
            holder.plantDescription.setText(plant.getDescription());
        } else {
            holder.plantDescription.setText(R.string.description_not_available);
            holder.plantDescription.setVisibility(View.VISIBLE);
        }


        holder.itemView.setOnClickListener(v -> {
            PlantDetailsBottomSheetFragment fragment = PlantDetailsBottomSheetFragment.newInstance(plant);
            fragment.show(((AppCompatActivity) holder.itemView.getContext()).getSupportFragmentManager(), "PlantDetailsBottomSheet");
        });

//        // Set watering chip
//        holder.wateringChip.setText(plant.getWatering().toString());
//        holder.wateringChip.setVisibility(plant.getWatering() != null ? View.VISIBLE : View.GONE);

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
        private final ImageView ivThumbnail;

        public PlantViewHolder(@NonNull View itemView) {
            super(itemView);
            plantName = itemView.findViewById(R.id.tvName);
            plantDescription = itemView.findViewById(R.id.tvDescription);
            ivThumbnail = itemView.findViewById(R.id.ivThumbnail);
        }
    }
}
