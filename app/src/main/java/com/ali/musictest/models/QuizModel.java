package com.ali.musictest.models;

import java.util.List;

public class QuizModel {
    private String id;
    private String title;
    private String subtitle;
    private String time; // String kimi saxla
    private List<QuestionModel> questionList;

    public QuizModel() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public List<QuestionModel> getQuestionList() {
        return questionList;
    }

    public void setQuestionList(List<QuestionModel> questionList) {
        this.questionList = questionList;
    }

    // Əlavə metod: String time-i int-ə çevir
    public int getTimeAsInt() {
        try {
            return Integer.parseInt(time);
        } catch (NumberFormatException e) {
            return 0; // və ya uyğun bir default dəyər
        }
    }
}