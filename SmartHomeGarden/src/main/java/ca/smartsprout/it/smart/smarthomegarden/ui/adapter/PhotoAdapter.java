/**
 * Smart Sprout
 * Members:
 * 1. Aditi Patel, n01525570, CENG322-RCB
 * 2. Birava Prajapati, n01579924, CENG322-RCA
 * 3. Darshankumar Prajapati, n01574247, CENG322-RCB
 * 4. Zeel Patel, n01526282, CENG322-RCB
 */
package ca.smartsprout.it.smart.smarthomegarden.ui.adapter;
import ca.smartsprout.it.smart.smarthomegarden.R;
import ca.smartsprout.it.smart.smarthomegarden.data.model.Photo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.Date;
import java.util.List;
import java.util.Random;

import android.widget.Button;

public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.PhotoViewHolder> {

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
        String currentDate = java.text.DateFormat.getDateTimeInstance().format(new Date());
        holder.textViewDate.setText(currentDate);

        // Handle delete button click
        holder.buttonDelete.setOnClickListener(v -> {
            photos.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, photos.size());
        });
    }

    @Override
    public int getItemCount() {
        return photos.size();
    }

    static class PhotoViewHolder extends RecyclerView.ViewHolder {
        ImageView imageViewPhoto;
        TextView textViewDate;
        Button buttonDelete;

        public PhotoViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewPhoto = itemView.findViewById(R.id.imageViewPhoto);
            textViewDate = itemView.findViewById(R.id.textViewDate);
            buttonDelete = itemView.findViewById(R.id.buttonDelete);
        }
    }

    // Generate random heights for items
    private int getRandomHeight() {
        int[] heights = {300, 400, 500, 600}; // Example heights in pixels
        return heights[random.nextInt(heights.length)];
    }

    public void addPhoto(Photo photo) {
        this.photos.add(0, photo);
        notifyItemInserted(0);
    }
}
