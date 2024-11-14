package ca.smartsprout.it.smart.smarthomegarden.ui;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import ca.smartsprout.it.smart.smarthomegarden.R;
import ca.smartsprout.it.smart.smarthomegarden.data.model.Notification;
import ca.smartsprout.it.smart.smarthomegarden.ui.adapter.NotificationAdapter;
import ca.smartsprout.it.smart.smarthomegarden.viewmodels.NotificationViewModel;
import ca.smartsprout.it.smart.smarthomegarden.utils.NotificationHelper;

public class NotificationActivity extends AppCompatActivity {

    private NotificationViewModel notificationViewModel;
    private DatabaseReference databaseReference;
    private List<Notification> notificationList = new ArrayList<>();
    private NotificationAdapter adapter;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        RecyclerView recyclerView = findViewById(R.id.recycler_view_notifications);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Create notification channel
        NotificationHelper.createNotificationChannel(this);

        notificationViewModel = new ViewModelProvider(this).get(NotificationViewModel.class);
        adapter = new NotificationAdapter(notificationList);
        recyclerView.setAdapter(adapter);

        // Initialize the database reference
        databaseReference = FirebaseDatabase.getInstance().getReference(getString(R.string.notificationsdatbase));

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                notificationList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Notification notification = snapshot.getValue(Notification.class);
                    notificationList.add(0, notification); // Add to the top of the list
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle possible errors.
            }
        });

        // Handle back button press
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                finish(); // Close the activity and go back to the previous screen
            }
        });

        // Add ItemTouchHelper for swipe-to-dismiss functionality
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                Notification notification = notificationList.get(position);

                // Check if notification ID is not null
                if (notification.getId() != null) {
                    // Remove the notification from the database
                    databaseReference.child(notification.getId()).removeValue();
                }

                // Remove the notification from the list
                notificationList.remove(position);
                adapter.notifyItemRemoved(position);
            }
        });

        itemTouchHelper.attachToRecyclerView(recyclerView);
    }
}
