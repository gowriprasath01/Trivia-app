package com.bawp.triviaapp.model;

public class Questions {

    private String answers;
    private boolean answerTrue;

    public Questions() {

    }

    public Questions(String answers, boolean answerTrue) {
        this.answers = answers;
        this.answerTrue = answerTrue;
    }

    public String getAnswers() {
        return answers;
    }

    public void setAnswers(String answers) {
        this.answers = answers;
    }

    public boolean isAnswerTrue() {
        return answerTrue;
    }

    public void setAnswerTrue(boolean answerTrue) {
        this.answerTrue = answerTrue;
    }

    @Override
    public String toString() {
        return "Questions{" +
                "answers='" + answers + '\'' +
                ", answerTrue=" + answerTrue +
                '}';
    }
}


