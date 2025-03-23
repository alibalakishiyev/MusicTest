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

import java.util.ArrayList;
import java.util.List;

public class QuizListAdapter extends RecyclerView.Adapter<QuizListAdapter.MyViewHolder> {

    private final List<QuizModel> quizModelList;

    public QuizListAdapter(List<QuizModel> quizModelList) {
        this.quizModelList = quizModelList;
        Log.d("QuizListAdapter", "Adapter created with " + quizModelList.size() + " items.");
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d("QuizListAdapter", "Creating ViewHolder...");
        QuizItemRecyclerRowBinding binding = QuizItemRecyclerRowBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Log.d("QuizListAdapter", "Binding data to ViewHolder at position " + position);
        QuizModel quizModel = quizModelList.get(position);
        holder.bind(quizModel);
    }

    @Override
    public int getItemCount() {
        Log.d("QuizListAdapter", "Total items: " + quizModelList.size());
        return quizModelList.size();
    }

    public void updateData(List<QuizModel> newQuizModelList) {
        Log.d("QuizListAdapter", "Updating data in adapter...");
        quizModelList.clear();
        quizModelList.addAll(newQuizModelList);
        notifyDataSetChanged();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private final QuizItemRecyclerRowBinding binding;

        public MyViewHolder(QuizItemRecyclerRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(QuizModel model) {
            Log.d("QuizListAdapter", "Binding data to ViewHolder for QuizModel: " + model.getTitle());
            if (model != null) {
                binding.quizTitleText.setText(model.getTitle() != null ? model.getTitle() : "No Title");
                binding.quizSubtitleText.setText(model.getSubtitle() != null ? model.getSubtitle() : "No Subtitle");

                Integer time = Integer.valueOf(model.getTime());
                String timeText = (time != null && time > 0) ? time + " min" : "No Time Set";
                binding.quizTimeText.setText(timeText);

                binding.getRoot().setOnClickListener(v -> {
                    if (model != null && model.getQuestionList() != null && !model.getQuestionList().isEmpty()) {
                        Log.d("QuizListAdapter", "Starting QuizActivity with " + model.getQuestionList().size() + " questions.");
                        Intent intent = new Intent(binding.getRoot().getContext(), QuizActivity.class);
                        intent.putParcelableArrayListExtra("questionList", new ArrayList<>(model.getQuestionList()));
                        intent.putExtra("time", model.getTime());
                        binding.getRoot().getContext().startActivity(intent);
                    } else {
                        Log.d("QuizListAdapter", "No questions available for this quiz.");
                        Toast.makeText(binding.getRoot().getContext(), "No questions available!", Toast.LENGTH_SHORT).show();
                    }
                });

            } else {
                Log.d("QuizListAdapter", "QuizModel is null.");
            }
        }
    }
}