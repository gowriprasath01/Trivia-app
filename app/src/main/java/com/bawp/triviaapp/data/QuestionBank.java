package com.bawp.triviaapp.data;


import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.bawp.triviaapp.Controller.AppController;
import com.bawp.triviaapp.model.Questions;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

public class QuestionBank {
    ArrayList <Questions> questionsArrayList = new ArrayList<Questions>();
    private String url =  "https://raw.githubusercontent.com/curiousily/simple-quiz/master/script/statements-data.json";
    public List<Questions> getQuestions(final AnswerListAsyncResponse callBack) {
        final JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, (JSONArray) null,
                new Response.Listener<JSONArray>( ) {
                    @Override
                    public void onResponse(JSONArray response) {

//                        Log.d("JSON ARRAY", "onResponse: "+ response);

                        for (int i=0; i < response.length(); i++){
                            try {
                                 Questions questions = new Questions();
                                 questions.setAnswers( response.getJSONArray(i).getString(0));
                                 questions.setAnswerTrue(response.getJSONArray(i).getBoolean(1));
                                 questions.toString();
                                 questionsArrayList.add(questions);
                                 Log.d("new", "onResponse: "+ questions);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                        if (null!= callBack){
                            callBack.processfinished(questionsArrayList);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

        AppController.getInstance().addToRequestQueue(jsonArrayRequest);
        Log.d("array", "getQuestions: " + questionsArrayList);
        return questionsArrayList;

    }

}
