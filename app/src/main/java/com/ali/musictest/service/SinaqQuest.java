package com.ali.musictest.service;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.ali.musictest.config.QuizListAdapter;
import com.ali.musictest.databinding.ActivitySinaqBinding;
import com.ali.musictest.models.QuestionModel;
import com.ali.musictest.models.QuizModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class SinaqQuest extends AppCompatActivity {

    ActivitySinaqBinding binding;
    List<QuizModel> quizModelList = new ArrayList<>();
    QuizListAdapter quizListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivitySinaqBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Log.d("MainActivity", "Activity created. Setting up RecyclerView...");
        setupRecyclerView();

        Log.d("MainActivity", "Fetching data from Firebase...");
        getDataFromFirebase();
    }

    private void setupRecyclerView() {
        Log.d("MainActivity", "Setting up RecyclerView...");
        quizListAdapter = new QuizListAdapter(quizModelList);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setAdapter(quizListAdapter);
        Log.d("MainActivity", "RecyclerView setup completed.");
    }

    private void getDataFromFirebase() {
        Log.d("MainActivity", "Fetching data from Firebase...");
        FirebaseDatabase.getInstance().getReference("quizz")
                .get()
                .addOnSuccessListener(dataSnapshot -> {
                    if (dataSnapshot.exists()) {
                        Log.d("MainActivity", "DataSnapshot exists. Number of children: " + dataSnapshot.getChildrenCount());
                        quizModelList.clear(); // Əvvəlki məlumatları təmizləyin (əgər varsa)

                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Log.d("MainActivity", "Snapshot key: " + snapshot.getKey());
                            Log.d("MainActivity", "Snapshot value: " + snapshot.getValue());

                            QuizModel quizModel = snapshot.getValue(QuizModel.class);
                            if (quizModel != null) {
                                quizModelList.add(quizModel);
                                Log.d("MainActivity", "QuizModel added: " + quizModel.getTitle());

                                // QuizModel-in məlumatlarını ətraflı çap edirik
                                Log.d("MainActivity", "QuizModel details - ID: " + quizModel.getId());
                                Log.d("MainActivity", "QuizModel details - Title: " + quizModel.getTitle());
                                Log.d("MainActivity", "QuizModel details - Subtitle: " + quizModel.getSubtitle());
                                Log.d("MainActivity", "QuizModel details - Time: " + quizModel.getTime());

                                if (quizModel.getQuestionList() != null) {
                                    Log.d("MainActivity", "QuizModel has " + quizModel.getQuestionList().size() + " questions.");
                                    for (QuestionModel question : quizModel.getQuestionList()) {
                                        Log.d("MainActivity", "Question: " + question.getQuestion());
                                        Log.d("MainActivity", "Correct Answer: " + question.getCorrect());
                                        Log.d("MainActivity", "Options: " + question.getOptions());
                                    }
                                } else {
                                    Log.d("MainActivity", "QuizModel has no questions.");
                                }
                            } else {
                                Log.d("MainActivity", "QuizModel is null.");
                            }
                        }

                        // Məlumat yeniləndikdən sonra adapteri xəbərdar edin
                        Log.d("MainActivity", "Notifying adapter of data change...");
                        quizListAdapter.notifyDataSetChanged();
                    } else {
                        Log.d("MainActivity", "No quizzes found in Firebase.");
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e("MainActivity", "Error fetching data from Firebase", e);
                    Toast.makeText(this, "Failed to load data!", Toast.LENGTH_SHORT).show();
                });
    }
}