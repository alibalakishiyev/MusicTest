package com.ali.musictest.config;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ali.musictest.databinding.QuizItemRecyclerRowBinding;
import com.ali.musictest.models.QuizModel;
import com.ali.musictest.models.QuestionModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class QuizListAdapter extends RecyclerView.Adapter<QuizListAdapter.MyViewHolder> {

    private final List<QuizModel> quizModelList;
    private final Random random = new Random();
    private final int QUESTIONS_TO_SHOW = 10; // Göstəriləcək sual sayı

    public QuizListAdapter(List<QuizModel> quizModelList) {
        this.quizModelList = quizModelList != null ? quizModelList : new ArrayList<>();
        Log.d("QuizListAdapter", "Adapter created with " + this.quizModelList.size() + " items.");
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d("QuizListAdapter", "Creating ViewHolder...");
        QuizItemRecyclerRowBinding binding = QuizItemRecyclerRowBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new MyViewHolder(binding, this); // Adapteri ViewHolder-a ötürürük
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Log.d("QuizListAdapter", "Binding data to position " + position);
        if (position >= 0 && position < quizModelList.size()) {
            QuizModel quizModel = quizModelList.get(position);
            holder.bind(quizModel);
        }
    }

    @Override
    public int getItemCount() {
        return quizModelList.size();
    }

    public void updateData(List<QuizModel> newQuizModelList) {
        Log.d("QuizListAdapter", "Updating data with " + (newQuizModelList != null ? newQuizModelList.size() : 0) + " items");
        quizModelList.clear();
        if (newQuizModelList != null) {
            quizModelList.addAll(newQuizModelList);
        }
        notifyDataSetChanged();
    }

    // Artıq private deyil ki, ViewHolder istifadə edə bilsin
    public List<QuestionModel> getRandomQuestions(List<QuestionModel> allQuestions) {
        if (allQuestions == null || allQuestions.isEmpty()) {
            Log.d("QuizListAdapter", "No questions available for randomization");
            return new ArrayList<>();
        }

        // Orijinal siyahını dəyişdirməmək üçün yeni siyahı yaradırıq
        List<QuestionModel> shuffled = new ArrayList<>(allQuestions);

        // Sualları qarışdırırıq
        Collections.shuffle(shuffled, random);

        // Müəyyən edilmiş sayda sual seçirik
        int questionCount = Math.min(QUESTIONS_TO_SHOW, shuffled.size());
        Log.d("QuizListAdapter", "Selected " + questionCount + " random questions");

        return shuffled.subList(0, questionCount);
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private final QuizItemRecyclerRowBinding binding;
        private final QuizListAdapter adapter; // Adapterə istinad saxlayırıq

        public MyViewHolder(QuizItemRecyclerRowBinding binding, QuizListAdapter adapter) {
            super(binding.getRoot());
            this.binding = binding;
            this.adapter = adapter; // Adapteri qeyd edirik
        }

        public void bind(QuizModel model) {
            if (model == null) {
                Log.e("QuizListAdapter", "Null QuizModel in bind");
                return;
            }

            Log.d("QuizListAdapter", "Binding quiz: " + model.getTitle());
            binding.quizTitleText.setText(model.getTitle() != null ? model.getTitle() : "No Title");
            binding.quizSubtitleText.setText(model.getSubtitle() != null ? model.getSubtitle() : "No Subtitle");

            try {
                int time = Integer.parseInt(model.getTime());
                binding.quizTimeText.setText(time + " dəq");
            } catch (NumberFormatException e) {
                Log.e("QuizListAdapter", "Invalid time format: " + model.getTime());
                binding.quizTimeText.setText("Vaxt müəyyən edilməyib");
            }

            binding.getRoot().setOnClickListener(v -> {
                if (model.getQuestionList() == null || model.getQuestionList().isEmpty()) {
                    Log.e("QuizListAdapter", "No questions in this quiz");
                    Toast.makeText(binding.getRoot().getContext(),
                            "Bu testdə heç bir sual yoxdur", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Artıq adapter dəyişənindən istifadə edirik
                List<QuestionModel> randomQuestions = adapter.getRandomQuestions(model.getQuestionList());

                Log.d("QuizListAdapter", "Starting quiz with " + randomQuestions.size() + " questions");

                Intent intent = new Intent(binding.getRoot().getContext(), QuizActivity.class);
                intent.putParcelableArrayListExtra("questionList", new ArrayList<>(randomQuestions));
                intent.putExtra("time", model.getTime());
                binding.getRoot().getContext().startActivity(intent);
            });
        }
    }
}