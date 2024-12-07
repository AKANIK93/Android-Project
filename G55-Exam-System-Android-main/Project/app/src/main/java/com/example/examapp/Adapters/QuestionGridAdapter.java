package com.example.examapp.Adapters;

import static com.example.examapp.DBQuery.ANSWERED;
import static com.example.examapp.DBQuery.NOT_VISITED;
import static com.example.examapp.DBQuery.REVIEW;
import static com.example.examapp.DBQuery.UNANSWERED;
import static com.example.examapp.DBQuery.g_quesList;

import android.content.Context;
import android.content.res.ColorStateList;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.example.examapp.DBQuery;
import com.example.examapp.QuestionsActivity;
import com.example.examapp.R;

public class QuestionGridAdapter extends BaseAdapter {

    private int numOfQues;
    private Context contex;

    public QuestionGridAdapter(Context contex,int numOfQues) {

        this.contex=contex;
        this.numOfQues = numOfQues;
    }

    @Override
    public int getCount() {
        return numOfQues;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View myView;

        if(convertView == null)
        {
            myView = LayoutInflater.from(parent.getContext()).inflate(R.layout.ques_grid_item,parent,false);
        }
        else{
            myView = convertView;
        }

        myView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(contex instanceof QuestionsActivity)
                {
                    ((QuestionsActivity)contex).goToQuestion(position);
                }

            }
        });

        TextView quesTV = myView.findViewById(R.id.ques_num);
        quesTV.setText(String.valueOf(position+1));

        Log.d("LOGGGGGG", String.valueOf(g_quesList.get(position).getStatus()));
        switch (DBQuery.g_quesList.get(position).getStatus())
        {
            case NOT_VISITED :
                quesTV.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(myView.getContext(),R.color.grey)));
                break;
            case UNANSWERED:
                quesTV.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(myView.getContext(),R.color.red)));
                break;
            case ANSWERED :
                quesTV.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(myView.getContext(),R.color.green)));
                break;
            case REVIEW :
                quesTV.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(myView.getContext(),R.color.pink)));
                break;
            default:
                break;
        }

        return myView;
    }
}
