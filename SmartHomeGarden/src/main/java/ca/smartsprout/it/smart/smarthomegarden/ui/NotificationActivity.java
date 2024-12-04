package ca.smartsprout.it.smart.smarthomegarden.ui;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.activity.OnBackPressedCallback;

import androidx.annotation.NonNull;

import androidx.appcompat.app.ActionBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
import ca.smartsprout.it.smart.smarthomegarden.utils.NotificationHelper;
import ca.smartsprout.it.smart.smarthomegarden.viewmodels.NotificationViewModel;

public class NotificationActivity extends AppCompatActivity {

    private DatabaseReference databaseReference;
    private final List<Notification> notificationList = new ArrayList<>();
    private NotificationAdapter adapter;
    private FirebaseUser currentUser;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        RecyclerView recyclerView = findViewById(R.id.recycler_view_notifications);
        Button btnClearAll = findViewById(R.id.btn_clear_all);
        ImageView imgNoNotifications = findViewById(R.id.img_no_notifications);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Create notification channel
        NotificationHelper.createNotificationChannel(this);
        NotificationHelper.createTaskReminderChannel(this);


        NotificationViewModel notificationViewModel = new ViewModelProvider(this).get(NotificationViewModel.class);

        adapter = new NotificationAdapter(notificationList);
        recyclerView.setAdapter(adapter);


        // Initialize ActionBar
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        // Handle the back button press
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                finish(); // Close the activity and go back
            }
        });

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

                // Toggle visibility of the "No Notifications" image and "Clear All" button
                if (notificationList.isEmpty()) {
                    btnClearAll.setVisibility(View.GONE);
                    imgNoNotifications.setVisibility(View.VISIBLE);
                } else {
                    btnClearAll.setVisibility(View.VISIBLE);
                    imgNoNotifications.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle possible errors.
            }
        });

        // Initialize FirebaseAuth
        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        // Get current user
        currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(userId).child("notifications");
            loadNotifications();
        } else {
            // Handle the case where there is no authenticated user
        }



        // Handle "Clear All" button click
        btnClearAll.setOnClickListener(v -> {
            databaseReference.removeValue();
            notificationList.clear();
            adapter.notifyDataSetChanged();

            btnClearAll.setVisibility(View.GONE);
            imgNoNotifications.setVisibility(View.VISIBLE); // Show the image
        });


        // Add ItemTouchHelper for swipe-to-dismiss functionality
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getBindingAdapterPosition();
                Notification notification = notificationList.get(position);

                if (notification.getId() != null) {
                    databaseReference.child(notification.getId()).removeValue();
                }

                notificationList.remove(position);
                adapter.notifyItemRemoved(position);

                if (notificationList.isEmpty()) {
                    btnClearAll.setVisibility(View.GONE);
                    imgNoNotifications.setVisibility(View.VISIBLE); // Show the image
                }
            }
        });

        itemTouchHelper.attachToRecyclerView(recyclerView);
    }


    @Override
    public boolean onSupportNavigateUp() {
        finish(); // Close the activity and go back
        return true;
    }

    // Method to add a notification
    private void addNotification(Notification notification) {
        if (currentUser != null) {
            String userId = currentUser.getUid();
            DatabaseReference userNotificationsRef = FirebaseDatabase.getInstance().getReference("Users").child(userId).child("notifications");
            String notificationId = userNotificationsRef.push().getKey();
            if (notificationId != null) {
                notification.setId(notificationId);
                userNotificationsRef.child(notificationId).setValue(notification);
            }
        }
    }

    private void loadNotifications() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                notificationList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Notification notification = snapshot.getValue(Notification.class);
                    notificationList.add(0, notification); // Add to the top of the list
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle possible errors.
            }
        });

    }
}
