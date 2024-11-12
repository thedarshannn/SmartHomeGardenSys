package ca.smartsprout.it.smart.smarthomegarden.ui;


import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import ca.smartsprout.it.smart.smarthomegarden.R;
import ca.smartsprout.it.smart.smarthomegarden.ui.adapter.NotificationAdapter;
import ca.smartsprout.it.smart.smarthomegarden.viewmodels.NotificationViewModel;

public class NotificationActivity extends AppCompatActivity {

    private NotificationViewModel notificationViewModel;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        RecyclerView recyclerView = findViewById(R.id.recycler_view_notifications);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        notificationViewModel = new ViewModelProvider(this).get(NotificationViewModel.class);
        NotificationAdapter adapter = new NotificationAdapter(notificationViewModel.getNotifications().getValue());
        recyclerView.setAdapter(adapter);

        notificationViewModel.getNotifications().observe(this, notifications -> {
            adapter.setNotifications(notifications);
            adapter.notifyDataSetChanged();
        });

        // Handle back button press
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                finish(); // Close the activity and go back to the previous screen
            }
        });
    }
}
