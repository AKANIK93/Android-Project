package com.example.examapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Dialog;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.TimeUnit;

public class ScoreActivity extends AppCompatActivity {

    private TextView scoreTV,timeTV,totalQTV,correctQTV,wrongQTV,unAttemptedQTV;
    Button dashboardB,viewAnsB;
    private long timeTaken;
    private Dialog progressDialog;
    private TextView dialogText;
    private int finalScore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);


        getSupportActionBar().setTitle("Result");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        progressDialog = new Dialog(ScoreActivity.this);
        progressDialog.setContentView(R.layout.dialog_layout);
        progressDialog.setCancelable(false);
        progressDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);

        dialogText = progressDialog.findViewById(R.id.dialog_text);
        dialogText.setText("Loading...");

        init();

        loadData();

        viewAnsB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        saveResult();
    }

    private void init()
    {
        scoreTV = findViewById(R.id.score);
        timeTV = findViewById(R.id.time);
        totalQTV = findViewById(R.id.totalQ);
        correctQTV = findViewById(R.id.correctQ);
        wrongQTV = findViewById(R.id.wrongQ);
        unAttemptedQTV = findViewById(R.id.un_attemptedQ);
        dashboardB = findViewById(R.id.dashboardB);
        viewAnsB = findViewById(R.id.view_answersB);
    }

    private void loadData()
    {
        int correctQ = 0,wrongQ = 0, unattemptedQ = 0;

        for(int i=0 ; i < DBQuery.g_quesList.size() ; i++)
        {
            if(DBQuery.g_quesList.get(i).getSelectedAns() == -1)
            {
                unattemptedQ++;
            }
            else{
                if(DBQuery.g_quesList.get(i).getSelectedAns() == DBQuery.g_quesList.get(i).getCorrectAns())
                {
                    correctQ++;
                }
                else{
                    wrongQ++;
                }
            }
        }

        correctQTV.setText(String.valueOf(correctQ));
        wrongQTV.setText(String.valueOf(wrongQ));
        unAttemptedQTV.setText(String.valueOf(unattemptedQ));
        totalQTV.setText(String.valueOf(DBQuery.g_quesList.size()));

        finalScore = (correctQ*100)/DBQuery.g_quesList.size();
        //finalScore = correctQ;

        scoreTV.setText(String.valueOf(finalScore));
        timeTaken = getIntent().getLongExtra("Time Taken",0);

        String time = String.format("%02d:%02d min",
                TimeUnit.MILLISECONDS.toMinutes(timeTaken),
                TimeUnit.MILLISECONDS.toSeconds(timeTaken) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(timeTaken))
        );

        timeTV.setText(time);

    }
    private void saveResult()
    {
        DBQuery.saveResult(finalScore, new MyCompleteListener() {
            @Override
            public void onSuccess() {
                progressDialog.dismiss();
            }

            @Override
            public void onFailure() {
                Toast.makeText(ScoreActivity.this,"Something went wrong ? Please try again later !",Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId() == android.R.id.home)
        {
            ScoreActivity.this.finish();
        }

        return super.onOptionsItemSelected(item);
    }

}