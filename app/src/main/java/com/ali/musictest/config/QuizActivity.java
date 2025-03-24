package com.ali.musictest.config;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.ali.musictest.R;
import com.ali.musictest.databinding.ScoreDialogBinding;
import com.ali.musictest.models.QuestionModel;

import java.util.ArrayList;
import java.util.List;

public class QuizActivity extends AppCompatActivity implements View.OnClickListener {

    public static List<QuestionModel> questionModelList = new ArrayList<>();
    public static String time = "";
    private CountDownTimer countDownTimer;

    private int currentQuestionIndex = 0;
    private String selectedAnswer = "";
    private int score = 0;

    private TextView questionIndicator, questionTextview, timerIndicator;
    private ProgressBar questionProgressIndicator;
    private Button btn0, btn1, btn2, btn3, nextBtn;

    private android.app.AlertDialog dialog;  // Sinif səviyyəli dəyişən

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_quiz);

        // Intent-dən məlumatları al
        Intent intent = getIntent();
        if (intent != null) {
            questionModelList = intent.getParcelableArrayListExtra("questionList");
            time = intent.getStringExtra("time");
            Log.d("QuizActivity", "Received questionList size: " + questionModelList.size());
            Log.d("QuizActivity", "Received time: " + time);
        }

        // Log: questionModelList-in ölçüsü və məzmunu
        Log.d("QuizActivity", "questionModelList size: " + questionModelList.size());
        if (questionModelList == null || questionModelList.isEmpty()) {
            Log.e("QuizActivity", "questionModelList is empty!");
            Toast.makeText(this, "No questions available!", Toast.LENGTH_SHORT).show();
            finish();  // Aktivliyi bağla
            return;
        }

        for (int i = 0; i < questionModelList.size(); i++) {
            Log.d("QuizActivity", "Question " + i + ": " + questionModelList.get(i).getQuestion());
        }

        // Log: questionModelList-in ölçüsü və məzmunu
        Log.d("QuizActivity", "questionModelList size: " + questionModelList.size());
        if (questionModelList.isEmpty()) {
            Log.e("QuizActivity", "questionModelList is empty!");
            Toast.makeText(this, "No questions available!", Toast.LENGTH_SHORT).show();
            finish();  // Aktivliyi bağla
            return;
        }

        for (int i = 0; i < questionModelList.size(); i++) {
            Log.d("QuizActivity", "Question " + i + ": " + questionModelList.get(i).getQuestion());
        }

        // UI elementləri
        questionIndicator = findViewById(R.id.question_indicator_textview);
        questionProgressIndicator = findViewById(R.id.question_progress_indicator);
        questionTextview = findViewById(R.id.question_textview);
        timerIndicator = findViewById(R.id.timer_indicator_textview);
        btn0 = findViewById(R.id.btn0);
        btn1 = findViewById(R.id.btn1);
        btn2 = findViewById(R.id.btn2);
        btn3 = findViewById(R.id.btn3);
        nextBtn = findViewById(R.id.next_btn);

        // Click listener-lər
        btn0.setOnClickListener(this);
        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
        btn3.setOnClickListener(this);
        nextBtn.setOnClickListener(this);

        // İlk sualı yüklə
        loadQuestions(currentQuestionIndex);
        startTimer();
    }

    private void startTimer() {
        if (time == null || time.isEmpty()) {
            time = "10"; // 10 dəqiqəlik zaman
        }

        try {
            long totalTimeInMillis = Integer.parseInt(time) * 60 * 1000L;

            countDownTimer = new CountDownTimer(totalTimeInMillis, 1000L) {
                @Override
                public void onTick(long millisUntilFinished) {
                    long seconds = millisUntilFinished / 1000;
                    long minutes = seconds / 60;
                    long remainingSeconds = seconds % 60;

                    String formattedTime = String.format("%02d:%02d", minutes, remainingSeconds);
                    timerIndicator.setText(formattedTime);
                }

                @Override
                public void onFinish() {
                    timerIndicator.setText("Time's up!");
                    finishQuiz();  // Zaman bitdikdə quiz bitəcək
                }
            }.start();
        } catch (NumberFormatException e) {
            Log.e("QuizActivity", "Invalid time format: " + time);
            Toast.makeText(this, "Invalid time format, using default time", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadQuestions(int currentQuestionIndex) {
        selectedAnswer = "";

        // Əgər suallar bitibsə, quiz bitir
        if (currentQuestionIndex >= questionModelList.size()) {
            Log.d("QuizActivity", "No more questions. Finishing quiz.");
            finishQuiz();
            return;
        }

        QuestionModel currentQuestion = questionModelList.get(currentQuestionIndex);


        questionIndicator.setText("Question " + (currentQuestionIndex + 1) + " / " + questionModelList.size());
        questionProgressIndicator.setProgress((int) ((currentQuestionIndex / (float) questionModelList.size()) * 100));
        questionTextview.setText(currentQuestion.getQuestion());

        // Cavab variantlarını düymələrə təyin edin
        List<String> options = currentQuestion.getOptions();
        if (options != null && options.size() >= 4) {
            btn0.setText(options.get(0));
            btn1.setText(options.get(1));
            btn2.setText(options.get(2));
            btn3.setText(options.get(3));
        } else {
            Log.e("QuizActivity", "Invalid question options!");
            Toast.makeText(this, "Invalid question options!", Toast.LENGTH_SHORT).show();
        }

        // "Next" düyməsini passiv edin
        nextBtn.setEnabled(false);
    }

    @Override
    public void onClick(View v) {
        Button clickedButton = (Button) v;

        // Reset all button colors
        btn0.setBackgroundColor(getResources().getColor(R.color.gray));
        btn1.setBackgroundColor(getResources().getColor(R.color.gray));
        btn2.setBackgroundColor(getResources().getColor(R.color.gray));
        btn3.setBackgroundColor(getResources().getColor(R.color.gray));

        if (v.getId() == R.id.btn0 || v.getId() == R.id.btn1 ||
                v.getId() == R.id.btn2 || v.getId() == R.id.btn3) {

            selectedAnswer = clickedButton.getText().toString();
            clickedButton.setBackgroundColor(getResources().getColor(R.color.orange));
            nextBtn.setEnabled(true);

        } else if (v.getId() == R.id.next_btn) {
            if (selectedAnswer.isEmpty()) {
                Toast.makeText(this, "Please select an answer!", Toast.LENGTH_SHORT).show();
            } else {
                // Extract just the letter (A/B/C/D) from selected answer
                String selectedOption = selectedAnswer.substring(0, 1);

                // Get correct answer from question model
                String correctAnswer = questionModelList.get(currentQuestionIndex).getCorrect();

                // Compare only the letters
                if (selectedOption.equals(correctAnswer)) {
                    score++;
                    Log.i("QuizActivity", "Correct! Score: " + score);
                } else {
                    Log.i("QuizActivity", "Wrong. Selected: " + selectedOption +
                            ", Correct: " + correctAnswer);
                }

                currentQuestionIndex++;
                if (currentQuestionIndex < questionModelList.size()) {
                    loadQuestions(currentQuestionIndex);
                } else {
                    finishQuiz();
                }
            }
        }
    }

    private void finishQuiz() {
        if (countDownTimer != null) {
            countDownTimer.cancel();  // Timeri dayandırırıq
        }

        int totalQuestions = questionModelList.size();
        int percentage = (int) (((float) score / (float) totalQuestions) * 100);

        // Log: Bal və nəticə
        Log.d("QuizActivity", "Total Questions: " + totalQuestions);
        Log.d("QuizActivity", "Score: " + score);
        Log.d("QuizActivity", "Percentage: " + percentage + "%");

        ScoreDialogBinding dialogBinding = ScoreDialogBinding.inflate(getLayoutInflater());

        dialogBinding.scoreProgressIndicator.setProgress(percentage);
        dialogBinding.scoreProgressText.setText(percentage + " %");

        if (percentage > 60) {
            dialogBinding.scoreTitle.setText("Təbriklər! Keçdiniz");
            dialogBinding.scoreTitle.setTextColor(Color.BLUE);
        } else {
            dialogBinding.scoreTitle.setText("Üzgünüz! Keçə bilmədiniz");
            dialogBinding.scoreTitle.setTextColor(Color.RED);
        }

        dialogBinding.scoreSubtitle.setText(score + " sualdan " + totalQuestions + " doğru cavabladınız");

        dialogBinding.finishBtn.setOnClickListener(v -> {
            if (dialog != null && dialog.isShowing()) {
                dialog.dismiss();
            }
            finish();  // Quiz bitdikdən sonra aktivliyi bitiririk
        });

        dialog = new android.app.AlertDialog.Builder(this)
                .setView(dialogBinding.getRoot())
                .setCancelable(false)
                .create();

        dialog.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();  // Aktivlik məhv edilərkən dialoqu bağlayırıq
        }
        if (countDownTimer != null) {
            countDownTimer.cancel();  // Timeri dayandırırıq
        }
    }
}