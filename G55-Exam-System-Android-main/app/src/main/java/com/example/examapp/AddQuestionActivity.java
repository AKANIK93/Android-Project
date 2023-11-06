package com.example.examapp;

import static com.example.examapp.AdminFragment.add_quesList;
import static com.example.examapp.DBQuery.g_selected_test_index;
import static com.example.examapp.DBQuery.g_testList;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.examapp.Models.QuestionModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AddQuestionActivity extends AppCompatActivity {


    EditText questionNumberEditTxt, questionEditTxt, option1EditTxt, option2EditTxt, option3EditTxt, option4EditTxt, correctAnswerEditTxt;
    Button addQuestionBtn;
    private String Option1,Option2,Option3,Option4,QuestionNumber,Question,Answer;
   // private HashMap<String, Object> user_details = new HashMap<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_question);

        init();

        addQuestionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 //addQuestion();
                //Gets The string from the editText
                QuestionNumber = questionNumberEditTxt.getText().toString();
                Question = questionEditTxt.getText().toString();
                Option1 = option1EditTxt.getText().toString();
                Option2 = option2EditTxt.getText().toString();
                Option3 = option3EditTxt.getText().toString();
                Option4 = option4EditTxt.getText().toString();
                Answer = correctAnswerEditTxt.getText().toString();


                if (QuestionNumber.isEmpty() || Question.isEmpty() || Option1.isEmpty() || Option2.isEmpty() || Option3.isEmpty() || Option4.isEmpty() || Answer.isEmpty()) {
                    //Toast.makeText(getContext(), "Please,Fill all the details", Toast.LENGTH_SHORT).show();
                    Toast.makeText(AddQuestionActivity.this,"Please,Fill all the details",Toast.LENGTH_SHORT).show();
                }else {

                    addQuestion();
                    //Add_Question_Method(QuestionNumber,Question,Option1,Option2,Option3,Option4,Integer.parseInt(Answer));
                }
            }
        });
    }


    private void addQuestion()
    {
        /*AlertDialog.Builder builder = new AlertDialog.Builder(AddQuestionActivity.this);
        builder.setCancelable(true);
        View view = getLayoutInflater().inflate(R.layout.add_question_alert_dialog,null);

        Button cancelB = view.findViewById(R.id.add_Q_cancelB);
        Button confirmB = view.findViewById(R.id.add_Q_confirmB);

        builder.setView(view);
        AlertDialog alertDialog = builder.create();

        /*cancelB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        confirmB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();*/
                Add_Question_Method(QuestionNumber,Question,Option1,Option2,Option3,Option4,Integer.parseInt(Answer));
                //Intent intent = new Intent(AddQuestionActivity.this,AddQuestionActivity.class);
                ///startActivity(intent);
                AddQuestionActivity.this.finish();
          //  }
       // });
        //alertDialog.show();
    }


    //add data to question list
    private void Add_Question_Method(String questionNumber, String question, String option1, String option2, String option3, String option4, int answer) {

        add_quesList.add(new addQuestionModel(
                question,
                option1,
                option2,
                option3,
                option4,
                answer
        ));

    }


    //initialization of data
    private void init()
    {
        questionNumberEditTxt = findViewById(R.id.QuestionNumberEditTxt);
        questionEditTxt = findViewById(R.id.QuestionEditTxt);
        option1EditTxt = findViewById(R.id.Option1EditTxt);
        option2EditTxt = findViewById(R.id.Option2EditTxt);
        option3EditTxt = findViewById(R.id.Option3EditTxt);
        option4EditTxt = findViewById(R.id.Option4EditTxt);
        correctAnswerEditTxt = findViewById(R.id.AnswerEditTxt);

        addQuestionBtn = findViewById(R.id.AddQuestions_confirm_B);

    }

}