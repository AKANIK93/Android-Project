package com.example.examapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class AddCategoryActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText addCategory;
    private Button cancelB,confirmB;
    private ImageView backB;
    private String categoryStr;
    private Dialog progressDialog;
    private TextView dialogText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_category);
        mAuth = FirebaseAuth.getInstance();

        progressDialog = new Dialog(AddCategoryActivity.this);
        progressDialog.setContentView(R.layout.dialog_layout);
        progressDialog.setCancelable(false);
        progressDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);

        dialogText = progressDialog.findViewById(R.id.dialog_text);
        dialogText.setText("Add Category...");

        addCategory =  findViewById(R.id.add_cat_name);
        confirmB = findViewById(R.id.add_category_confirmB);
        backB = findViewById(R.id.add_category_backB);

        confirmB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                categoryStr = addCategory.getText().toString();
                if(categoryStr.isEmpty())
                {
                    addCategory.setError("Category Name Can't be empty !");
                }
                else{
                    saveData();
                }
            }
        });

        backB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void saveData()
    {
        progressDialog.show();

        DBQuery.addCategory(categoryStr, new MyCompleteListener() {
            @Override
            public void onSuccess() {
                Toast.makeText(AddCategoryActivity.this,"Category Added Successfully",Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
                // Intent intent = new Intent(AddCategoryActivity.this,AdminFragment.class);
                //startActivity(intent);
            }

            @Override
            public void onFailure() {
                Toast.makeText(AddCategoryActivity.this,"Something went wrong ! Please try again later ? ",Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }


}