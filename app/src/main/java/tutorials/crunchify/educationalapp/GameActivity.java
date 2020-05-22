package tutorials.crunchify.educationalapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Collections;
import java.util.List;

public class GameActivity extends AppCompatActivity {

    private TextView questionT;
    private ImageView foodImage;
    private TextView timeT;
    private EditText inputT;
    private Button buttonC;

    private CountDownTimer count;
    private SharedPreferences prefs;
    private AssetManager assetManager;
    private SoundManager soundManager;
    private int clap;

    private String rightAnswer;
    private String alertTitle;
    private int rightAnswerCount;
    private int quizCount = 1;
    private List<String> foodNames;
    private String difOption;
    private String musicOption;

    static private final int QUIZ_COUNT = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        questionT = (TextView) findViewById(R.id.questionText);
        foodImage = (ImageView) findViewById(R.id.foodImage);
        timeT = (TextView) findViewById(R.id.timeText);
        inputT = (EditText) findViewById(R.id.inputText);
        buttonC = (Button) findViewById(R.id.button);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        difOption = prefs.getString("difSelection","1");
        assetManager = new AssetManager(this, difOption);
        foodNames = assetManager.allFoodNames(difOption);

        soundManager = new SoundManager(this);
        clap = soundManager.addSound(R.raw.clap);

        musicOption = prefs.getString("musicSelection", "On");


        showNextQuiz();
        count = new CountDownTimer(10 * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeT.setText("Seconds Remaining: " + millisUntilFinished / 1000);
            }

            @Override
            public void onFinish() {
                timeT.setText("Done");
                buttonC.performClick();
            }
        };
        count.start();
    }

    public void showNextQuiz () {
        questionT.setText("Question " + quizCount + " Out of " + QUIZ_COUNT);

        Collections.shuffle(foodNames);
        foodImage.setImageDrawable(assetManager.imageForFood(foodNames.get(0)));
        rightAnswer = foodNames.get(0);

        foodNames.remove(0);
        Collections.shuffle(foodNames);
    }

    public void checkAnswer(View view) {
        String answer = inputT.getText().toString();
        inputT.setText("");

        if (answer.equals(rightAnswer)){
            alertTitle = "Correct!";
            rightAnswerCount++;
            if (musicOption.equals("On")){
                soundManager.play(clap);
            }
        } else {
            alertTitle = "Wrong...";
        }

        prefs = getSharedPreferences("value", MODE_PRIVATE);
        prefs.edit().putInt("Score", rightAnswerCount * 10).apply();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        if (quizCount == QUIZ_COUNT) {
            count.cancel();
            builder.setTitle("Game Over");
            builder.setMessage("Score: " + rightAnswerCount * 10);
            builder.setPositiveButton("Close", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                }
            });
        } else {
            count.cancel();
            builder.setTitle(alertTitle);
            builder.setMessage("Answer : " + rightAnswer);
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    quizCount++;
                    showNextQuiz();
                    count = new CountDownTimer(10 * 1000, 1000) {
                        @Override
                        public void onTick(long millisUntilFinished) {
                            timeT.setText("Seconds Remaining: " + millisUntilFinished / 1000);
                        }

                        @Override
                        public void onFinish() {
                            timeT.setText("Done");
                            buttonC.performClick();
                        }
                    };
                    count.start();
                }
            });
        }
        builder.setCancelable(false);
        builder.show();
    }
}
