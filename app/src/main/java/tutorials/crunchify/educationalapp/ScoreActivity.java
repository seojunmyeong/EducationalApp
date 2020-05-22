package tutorials.crunchify.educationalapp;

import android.content.SharedPreferences;
import android.os.Bundle;


import android.widget.TextView;
import android.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;

public class ScoreActivity extends AppCompatActivity {

    private SharedPreferences prefs;
    private TextView textView;

    private int highestScore;
    private int newScore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        textView = (TextView) findViewById(R.id.highestScore);

        prefs = getSharedPreferences("value", MODE_PRIVATE);
        newScore = prefs.getInt("Score", 0);
        highestScore = prefs.getInt("highestScore", 0);


        if (newScore > highestScore){
            highestScore = newScore;
        }

        prefs.edit().putInt("highestScore", highestScore).apply();
        textView.setText("" + highestScore);
    }
}
