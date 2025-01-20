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

    private final List<Plant> plantList;

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

        // Bind data to the views
        holder.tvName.setText(plant.getName() != null ? plant.getName() : "Unknown");
        holder.tvDescription.setText(plant.getDescription() != null ? plant.getDescription() : "No description available.");
        holder.tvToxicity.setText("Toxicity: " + (plant.getToxicity() != null ? plant.getToxicity() : "N/A"));
        holder.tvSuitability.setText("Suitability: " + (plant.getSuitability() != null ? plant.getSuitability() : "N/A"));
        holder.tvWateringPeriod.setText("Watering Period: " + (plant.getWateringPeriod() != null ? plant.getWateringPeriod() : "N/A"));

        // Set a placeholder thumbnail
        holder.ivThumbnail.setImageResource(R.drawable.ic_sprout);

        // OnClickListener for bottom sheet
        holder.itemView.setOnClickListener(v -> {
            PlantDetailsBottomSheetFragment fragment = PlantDetailsBottomSheetFragment.newInstance(plant);
            fragment.show(((AppCompatActivity) holder.itemView.getContext()).getSupportFragmentManager(), "PlantDetailsBottomSheet");
        });
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
        TextView tvName, tvDescription, tvWateringPeriod, tvToxicity, tvSuitability;
        ImageView ivThumbnail;

        public PlantViewHolder(@NonNull View itemView) {
            super(itemView);
            // Initialize the views using findViewById
            tvName = itemView.findViewById(R.id.tvName);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            tvWateringPeriod = itemView.findViewById(R.id.tvWateringPeriod);
            tvToxicity = itemView.findViewById(R.id.tvToxicity);
            tvSuitability = itemView.findViewById(R.id.tvSuitability);
            ivThumbnail = itemView.findViewById(R.id.ivThumbnail);
        }
    }
}
