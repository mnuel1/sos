package com.example.samplelangs;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class mechfeedback extends AppCompatActivity {

    private ListView list;
    private FeedbackDBHelper feedbackDBHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        list = findViewById(R.id.feedbacklist);

        // Retrieving the username in the main activity
        SharedPreferences preferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String username = preferences.getString("username", "");
        feedbackDBHelper = new FeedbackDBHelper(this);
        try {
            List<Feedback> feedback = feedbackDBHelper.getFeedback(username);
            for (Feedback feedbacks : feedback) {
                System.out.println("Username: " + feedbacks.getFeedbackName());
                System.out.println("Rate: " + feedbacks.getRate());
                System.out.println("-----------------------------------");
            }
            // Create an ArrayList of Strings to hold the display data
            ArrayList<String> displayData = new ArrayList<>();
            for (Feedback fback : feedback) {
                displayData.add("Message: " + fback.getFeedbackName() + ", Rating: " + fback.getRate());
            }

            // Create an ArrayAdapter to bind the data to the ListView
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, displayData);

            // Set the adapter on the ListView
            list.setAdapter(adapter);


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
