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

import java.util.List;

import ca.smartsprout.it.smart.smarthomegarden.R;
import ca.smartsprout.it.smart.smarthomegarden.data.model.Plant;

public class ProfilePlantAdapter extends RecyclerView.Adapter<ProfilePlantAdapter.PlantViewHolder> {

    private List<Plant> plantList;
    private final OnPlantClickListener listener;

    public ProfilePlantAdapter(List<Plant> plantList, OnPlantClickListener listener) {
        this.plantList = plantList;
        this.listener = listener;
    }

    public void updatePlantList(List<Plant> updatedList) {
        this.plantList = updatedList;
        notifyDataSetChanged();
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
        holder.dateAdded.setText(plant.getDateAdded().toString());

        holder.cardView.setOnClickListener(v -> listener.onPlantClick(plant));
    }

    @Override
    public int getItemCount() {
        return plantList != null ? plantList.size() : 0;
    }

    static class PlantViewHolder extends RecyclerView.ViewHolder {

        TextView plantName;
        TextView plantSpecies;
        TextView dateAdded;
        MaterialCardView cardView;

        public PlantViewHolder(@NonNull View itemView) {
            super(itemView);
            plantName = itemView.findViewById(R.id.plantName);
            plantSpecies = itemView.findViewById(R.id.plantSpecies);
            dateAdded = itemView.findViewById(R.id.dateAdded);
        }
    }

    public interface OnPlantClickListener {
        void onPlantClick(Plant plant);
    }
}
