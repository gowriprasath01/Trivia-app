package com.bawp.triviaapp.data;

import com.bawp.triviaapp.model.Questions;

import java.util.ArrayList;

public interface AnswerListAsyncResponse {
    public void processfinished(ArrayList<Questions> jsonArrayRequest);
}
