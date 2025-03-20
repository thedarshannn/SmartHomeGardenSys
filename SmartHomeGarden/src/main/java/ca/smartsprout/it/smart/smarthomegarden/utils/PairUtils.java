package ca.smartsprout.it.smart.smarthomegarden.utils;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import ca.smartsprout.it.smart.smarthomegarden.MainActivity;
import ca.smartsprout.it.smart.smarthomegarden.ui.PairActivity;

public class PairUtils {

    // Implement singleton pattern
    private static PairUtils instance = null;

    private PairUtils() {
        // Private constructor to prevent instantiation
    }

    public static PairUtils getInstance() {
        if (instance == null) {
            instance = new PairUtils();
        }
        return instance;
    }

    public void checkIfPiIsPaired(Context context, String userId) {
        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("devices");
        databaseRef.orderByChild("userId").equalTo(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot device : snapshot.getChildren()) {
                        String piName = device.getKey();
                        savePiNameLocally(context, piName);

                        // Redirect to MainActivity if Pi is paired
                        Intent intent = new Intent(context, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        context.startActivity(intent);
                        return;
                    }
                }

                // No Pi found, redirect to PairActivity
                Intent intent = new Intent(context, PairActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                context.startActivity(intent);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Firebase", "Error fetching paired Pi", error.toException());
                Toast.makeText(context, "Error checking Pi pairing", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void savePiNameLocally(Context context, String piName) {
        SharedPreferences prefs = context.getSharedPreferences("SmartSproutPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("Pi_Name", piName);
        editor.apply();
    }
}
