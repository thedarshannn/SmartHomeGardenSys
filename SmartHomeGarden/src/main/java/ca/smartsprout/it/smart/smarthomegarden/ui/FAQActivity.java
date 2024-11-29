package ca.smartsprout.it.smart.smarthomegarden.ui;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

import ca.smartsprout.it.smart.smarthomegarden.R;
import ca.smartsprout.it.smart.smarthomegarden.data.model.FAQ;
import ca.smartsprout.it.smart.smarthomegarden.ui.adapter.FAQAdapter;

public class FAQActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_faq);
        RecyclerView faqRecyclerView = findViewById(R.id.faqRecyclerView);
        faqRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Hardcoded FAQ list
        List<FAQ> faqList = new ArrayList<>();
        faqList.add(new FAQ("How do I use this app?", "This app helps you manage your plants effectively."));
        faqList.add(new FAQ("How to add a new plant?", "Go to the 'Add Plant' section and fill in the details."));
        faqList.add(new FAQ("Can I change the watering schedule?", "Yes, you can edit schedules in the 'Settings' menu."));
        faqList.add(new FAQ("How do I track plant health?", "Use the 'Health Tracker' feature in the app."));

        FAQAdapter adapter = new FAQAdapter(faqList);
        faqRecyclerView.setAdapter(adapter);
    }
}