/**
 * Smart Sprout
 * Members:
 * 1. Aditi Patel, n01525570, CENG322-RCB
 * 2. Birava Prajapati, n01579924, CENG322-RCA
 * 3. Darshankumar Prajapati, n01574247, CENG322-RCB
 * 4. Zeel Patel, n01526282, CENG322-RCB
 */

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

import com.google.android.material.appbar.MaterialToolbar;
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

    private NotificationViewModel notificationViewModel;
    private DatabaseReference databaseReference;
    private List<Notification> notificationList = new ArrayList<>();
    private NotificationAdapter adapter;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        MaterialToolbar toolbar = findViewById(R.id.topAppBar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(v -> finish());

        RecyclerView recyclerView = findViewById(R.id.recycler_view_notifications);
        Button btnClearAll = findViewById(R.id.btn_clear_all);
        ImageView imgNoNotifications = findViewById(R.id.img_no_notifications);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Create notification channel
        NotificationHelper.createNotificationChannel(this);
        NotificationHelper.createTaskReminderChannel(this);

        notificationViewModel = new ViewModelProvider(this).get(NotificationViewModel.class);
        adapter = new NotificationAdapter(notificationList);
        recyclerView.setAdapter(adapter);
        // Handle the back button press
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                finish();
            }
        });

        // Initialize FirebaseAuth
        mAuth = FirebaseAuth.getInstance();

        // Get current user
        currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(userId).child("notifications");
            loadNotifications(btnClearAll, imgNoNotifications);

            // Add a login notification
            notificationViewModel.addNotification(getString(R.string.login1), getString(R.string.welcome_back1));
        }

        // Handle "Clear All" button click
        btnClearAll.setOnClickListener(v -> {
            databaseReference.removeValue();
            notificationList.clear();
            adapter.notifyDataSetChanged();
            updateVisibility(btnClearAll, imgNoNotifications); // Update visibility
        });

        // Add ItemTouchHelper for swipe-to-dismiss functionality
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getBindingAdapterPosition();
                Notification notification = notificationList.get(position);

                if (notification.getId() != null) {
                    databaseReference.child(notification.getId()).removeValue();
                }

                notificationList.remove(position);
                adapter.notifyItemRemoved(position);
                updateVisibility(btnClearAll, imgNoNotifications); // Update visibility
            }
        });

        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    // Refactored method to update visibility
    private void updateVisibility(Button btnClearAll, ImageView imgNoNotifications) {
        if (notificationList.isEmpty()) {
            btnClearAll.setVisibility(View.GONE);
            imgNoNotifications.setVisibility(View.VISIBLE);
        } else {
            btnClearAll.setVisibility(View.VISIBLE);
            imgNoNotifications.setVisibility(View.GONE);
        }
    }

    private void loadNotifications(Button btnClearAll, ImageView imgNoNotifications) {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                notificationList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Notification notification = snapshot.getValue(Notification.class);
                    notificationList.add(0, notification); // Add to the top of the list
                }
                adapter.notifyDataSetChanged();
                updateVisibility(btnClearAll, imgNoNotifications); // Update visibility
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle possible errors.
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
