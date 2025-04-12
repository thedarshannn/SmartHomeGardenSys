/**
 * Smart Sprout
 * Members:
 * 1. Aditi Patel, n01525570, CENG322-RCB
 * 2. Birava Prajapati, n01579924, CENG322-RCA
 * 3. Darshankumar Prajapati, n01574247, CENG322-RCB
 * 4. Zeel Patel, n01526282, CENG322-RCB
 */

package ca.smartsprout.it.smart.smarthomegarden.ui.adapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ca.smartsprout.it.smart.smarthomegarden.R;
import ca.smartsprout.it.smart.smarthomegarden.data.model.FAQ;

public class FAQAdapter extends RecyclerView.Adapter<FAQAdapter.FAQViewHolder> {
    private final List<FAQ> faqList;
    private List<String> filteredList;
    public FAQAdapter(List<FAQ> faqList) {
        this.faqList = faqList;

    }

    @NonNull
    @Override
    public FAQViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_faq, parent, false);
        return new FAQViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FAQViewHolder holder, int position) {
        FAQ faq = faqList.get(position);
        holder.questionTextView.setText(faq.getQuestion());
        holder.answerTextView.setText(faq.getAnswer());

        boolean isExpanded = faq.isExpanded();
        holder.answerTextView.setVisibility(isExpanded ? View.VISIBLE : View.GONE);
        holder.arrowIcon.setRotation(isExpanded ? 180 : 0); // Show correct rotation

        holder.itemView.setOnClickListener(v -> {
            boolean currentlyExpanded = faq.isExpanded();
            faq.setExpanded(!currentlyExpanded);

            // Animate arrow
            holder.arrowIcon.animate()
                    .rotation(currentlyExpanded ? 0 : 90)
                    .setDuration(200)
                    .start();

            // Animate answer expand/collapse
            holder.answerTextView.setVisibility(faq.isExpanded() ? View.VISIBLE : View.GONE);

            // Optional: notify if only needed
            notifyItemChanged(position);
        });
    }


    @Override
    public int getItemCount() {
        return faqList.size();
    }

    public static class FAQViewHolder extends RecyclerView.ViewHolder {
        TextView questionTextView, answerTextView;
        ImageView arrowIcon;


        public FAQViewHolder(@NonNull View itemView) {
            super(itemView);
            questionTextView = itemView.findViewById(R.id.questionTextView);
            answerTextView = itemView.findViewById(R.id.answerTextView);
            arrowIcon = itemView.findViewById(R.id.arrowIcon);
        }
    }
}



