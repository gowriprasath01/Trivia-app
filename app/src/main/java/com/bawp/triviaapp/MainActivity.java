package com.bawp.triviaapp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bawp.triviaapp.data.AnswerListAsyncResponse;
import com.bawp.triviaapp.data.QuestionBank;
import com.bawp.triviaapp.model.Questions;
import com.bawp.triviaapp.model.Score;
import com.bawp.triviaapp.util.Prefs;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
//    private static final String SCORE = "scores";
    private TextView questiontext;
    private TextView countertext;
    private ImageButton next_Button;
    private ImageButton prev_Button;
    private Button TrueButton;
    private Button FalseButton;
    private Button ShareButton;
    private int Currentindex= 0;
    private List<Questions> questionsList;
    private int Scorecounter = 0;
    private TextView scorekeeper;
    private TextView Highestscore;
    private com.bawp.triviaapp.model.Score score = new Score(Scorecounter);
    private Prefs prefs;
    private SoundPool soundPool;
    int sound1, sound2;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        questionsList= new QuestionBank().getQuestions(new AnswerListAsyncResponse() {
            @SuppressLint("SetTextI18n")
            @Override
            public void processfinished(ArrayList<Questions> jsonArrayRequest) {

                questiontext.setText(jsonArrayRequest.get(Currentindex).getAnswers());
                countertext.setText(Currentindex + "/" + jsonArrayRequest.size());
                scorekeeper.setText("Your Score: "+ Scorecounter);
                Highestscore.setText("Highest Score: "+ prefs.gethighsore());



//                Log.d("inside", "processfinished: "+ jsonArrayRequest);

            }
        });

        questiontext = findViewById(R.id.question_textView);
        countertext= findViewById(R.id.counter_text);
        next_Button = findViewById(R.id.nextButton);
        prev_Button = findViewById(R.id.prevButton);
        TrueButton = findViewById(R.id.trueButton);
        FalseButton = findViewById(R.id.falseButton);
        scorekeeper = findViewById(R.id.scoreboard);
        Highestscore = findViewById(R.id.Highestscore);
        ShareButton= findViewById(R.id.shareButton);


        next_Button.setOnClickListener(this);
        prev_Button.setOnClickListener(this);
        TrueButton.setOnClickListener(this);
        FalseButton.setOnClickListener(this);
        ShareButton.setOnClickListener(this);

        prefs = new Prefs(MainActivity.this);

        AudioAttributes audioAttributes=new AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION)
                .build();
        soundPool = new SoundPool.Builder()
                .setAudioAttributes(audioAttributes)
                .setMaxStreams(2)
                .build();

        sound1=soundPool.load(this,R.raw.correct,1);
        sound2=soundPool.load(this,R.raw.defeat_one,1);






    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.nextButton:
                Currentindex = (Currentindex + 1) % questionsList.size();
                updateQuestion();
                break;
            case R.id.prevButton:
                if(Currentindex>0){
                    Currentindex= (Currentindex - 1)% questionsList.size();
                    updateQuestion();
                }
                break;
            case R.id.trueButton:
                checkAnswers(true);
                updateQuestion();


                break;
            case R.id.falseButton:
                checkAnswers(false);
                updateQuestion();


                break;

            case R.id.shareButton:
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_SUBJECT,"highest score"+ Highestscore.getText());
                intent.putExtra(Intent.EXTRA_TEXT,"Current_Score :" + scorekeeper.getText());
                startActivity(intent);



//            default:
//                throw new IllegalStateException("Unexpected value: " + v.getId());
        }
    }

    private void checkAnswers(boolean userAnswer) {

        boolean correctAnswer = questionsList.get(Currentindex).isAnswerTrue();
        if(correctAnswer == userAnswer){
            soundPool.play(sound1,1,1,0,0,1);
            fadedAnimation();
            addScore();
            Toast.makeText(MainActivity.this, "You are Correct!", Toast.LENGTH_SHORT).show();

        }else{
            soundPool.play(sound2,1,1,0,0,1);
            Animation();
            deductScore();
            Toast.makeText(MainActivity.this, "You are Wrong!", Toast.LENGTH_SHORT).show();
        }

    }

    private void fadedAnimation() {
        final CardView cardView = findViewById(R.id.card);
        AlphaAnimation alphaAnimation= new AlphaAnimation(2.00f, 0.00f);
        alphaAnimation.setDuration(200);
        alphaAnimation.setRepeatCount(1);
        alphaAnimation.setRepeatMode(Animation.REVERSE);
        cardView.setAnimation(alphaAnimation);

        alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                cardView.setCardBackgroundColor(Color.GREEN);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                cardView.setCardBackgroundColor(Color.WHITE);
                nextQuestion();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }

    private void updateQuestion() {
        questiontext.setText(questionsList.get(Currentindex).getAnswers());
        countertext.setText(Currentindex + "/" + questionsList.size());

    }
    private void nextQuestion(){
        Currentindex = (Currentindex+1)%questionsList.size();
        updateQuestion();

    }

    private void addScore(){
        Scorecounter = Scorecounter+1;
        score.setScore(Scorecounter);
        scorekeeper.setText("Your Score: "+score.getScore());

    }
    private void deductScore(){
        if (Scorecounter>0){
            Scorecounter= Scorecounter-1;
            score.setScore(Scorecounter);
            scorekeeper.setText("Your Score: "+ score.getScore());
        }


    }

    @Override
    protected void onPause() {

        super.onPause();
        prefs.savehighscore(score.getScore());


    }

    private void Animation(){
        Animation shake= AnimationUtils.loadAnimation(MainActivity.this, R.anim.shake_animation);
        final CardView cardView = findViewById(R.id.card);
        cardView.setAnimation(shake);
        shake.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                cardView.setCardBackgroundColor(Color.RED);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                cardView.setCardBackgroundColor(Color.WHITE);
                nextQuestion();

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }


        });

    }
}