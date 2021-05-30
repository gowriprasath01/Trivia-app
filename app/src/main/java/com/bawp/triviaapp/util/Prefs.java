package com.bawp.triviaapp.util;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class Prefs {
    private SharedPreferences preferences;

    public Prefs(Activity activity) {
        this.preferences = activity.getPreferences(activity.MODE_PRIVATE);
    }

    public void savehighscore(int score){
        int currentScore = score;
        int highScore = preferences.getInt("high_score", 0);
        if(currentScore> highScore){
            preferences.edit().putInt("high_score",currentScore).apply();
        }

    }



    public int gethighsore(){
        return preferences.getInt("high_score",0);

    }
}
