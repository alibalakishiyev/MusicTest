package com.ali.musictest.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class QuestionModel implements Parcelable {
    private String question;
    private String correct;
    private List<String> options;

    // Parametrsiz konstruktor (Firebase üçün vacibdir)
    public QuestionModel() {
    }

    // Getter və Setter metodları
    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getCorrect() {
        return correct;
    }

    public void setCorrect(String correct) {
        this.correct = correct;
    }

    public List<String> getOptions() {
        return options;
    }

    public void setOptions(List<String> options) {
        this.options = options;
    }

    // Parcelable interfeysini tətbiq etmək üçün metodlar
    protected QuestionModel(Parcel in) {
        question = in.readString();
        correct = in.readString();
        options = in.createStringArrayList();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(question);
        dest.writeString(correct);
        dest.writeStringList(options);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<QuestionModel> CREATOR = new Creator<QuestionModel>() {
        @Override
        public QuestionModel createFromParcel(Parcel in) {
            return new QuestionModel(in);
        }

        @Override
        public QuestionModel[] newArray(int size) {
            return new QuestionModel[size];
        }
    };
}