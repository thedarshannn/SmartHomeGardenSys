/**
 * Smart Sprout
 * Members:
 * 1. Aditi Patel, n01525570, CENG322-RCB
 * 2. Birava Prajapati, n01579924, CENG322-RCA
 * 3. Darshankumar Prajapati, n01574247, CENG322-RCB
 * 4. Zeel Patel, n01526282, CENG322-RCB
 */

package ca.smartsprout.it.smart.smarthomegarden.ui.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextClock;
import android.widget.TextView;

import ca.smartsprout.it.smart.smarthomegarden.R;

public class HomeFragment extends Fragment {


    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View view = inflater.inflate(R.layout.fragment_home, container, false);


        @SuppressLint({"MissingInflatedId", "LocalSuppress"})
        TextClock clockTC = view.findViewById(R.id.textClock);
        clockTC.setFormat12Hour(getString(R.string.date_format));

        TextView greetingTextView = view.findViewById(R.id.greetingTextView);
        // Set the greeting with the username
        String username = "John"; // Gonna change this later with the username of the user
        greetingTextView.setText(getString(R.string.hello) + username);

        return view;
    }
}