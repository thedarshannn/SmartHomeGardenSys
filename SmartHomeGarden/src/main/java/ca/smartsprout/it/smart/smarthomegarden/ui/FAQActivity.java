package ca.smartsprout.it.smart.smarthomegarden.ui;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.ActionBar;
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

        // Hardcoded FAQ list
        List<FAQ> faqList = new ArrayList<>();
        faqList.add(new FAQ(getString(R.string.faq1), getString(R.string.faq5)));
        faqList.add(new FAQ(getString(R.string.faq2), getString(R.string.faq6)));
        faqList.add(new FAQ(getString(R.string.faq3), getString(R.string.faq7)));
        faqList.add(new FAQ(getString(R.string.faq4), getString(R.string.faq8)));

        FAQAdapter adapter = new FAQAdapter(faqList);
        faqRecyclerView.setAdapter(adapter);


    }

    @Override
    public boolean onSupportNavigateUp() {
        finish(); // Close the activity and go back
        return true;
    }
}