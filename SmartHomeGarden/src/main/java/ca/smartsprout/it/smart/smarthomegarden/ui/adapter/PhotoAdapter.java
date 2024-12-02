package ca.smartsprout.it.smart.smarthomegarden.ui.adapter;
import ca.smartsprout.it.smart.smarthomegarden.R;
import ca.smartsprout.it.smart.smarthomegarden.data.model.Photo;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;
import java.util.Random;

public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.PhotoViewHolder>  {

    private final Context context;
    private final List<Photo> photos;
    private final Random random = new Random();

    public PhotoAdapter(Context context, List<Photo> photos) {
        this.context = context;
        this.photos = photos;
    }

    @NonNull
    @Override
    public PhotoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_photos, parent, false);
        return new PhotoViewHolder(view);
    }



    @Override
    public void onBindViewHolder(@NonNull PhotoViewHolder holder, int position) {
        Photo photo = photos.get(position);
        // Randomize item height
        int randomHeight = getRandomHeight();
        ViewGroup.LayoutParams layoutParams = holder.imageViewPhoto.getLayoutParams();
        layoutParams.height = randomHeight; // Set dynamic height
        holder.imageViewPhoto.setLayoutParams(layoutParams);

        // Load the photo using Glide
        Glide.with(context).load(photo.getUrl()).into(holder.imageViewPhoto);

        // Set the name and date
        holder.textViewName.setText(photo.getName());
        holder.textViewDate.setText(photo.getDate());
    }

    @Override
    public int getItemCount() {
        return photos.size();
    }

    static class PhotoViewHolder extends RecyclerView.ViewHolder {
        ImageView imageViewPhoto;
        TextView textViewName;
        TextView textViewDate;

        public PhotoViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewPhoto = itemView.findViewById(R.id.imageViewPhoto);
            textViewName = itemView.findViewById(R.id.textViewName);
            textViewDate = itemView.findViewById(R.id.textViewDate);
        }
    }
    // Generate random heights for items
    private int getRandomHeight() {
        int[] heights = {300, 400, 500, 600}; // Example heights in pixels
        return heights[random.nextInt(heights.length)];
    }
}
