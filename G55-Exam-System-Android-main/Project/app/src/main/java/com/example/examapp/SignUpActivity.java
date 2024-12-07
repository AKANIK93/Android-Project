package com.example.examapp;

//import android.support.v7.app.AppCompatActivity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignUpActivity extends AppCompatActivity {

    //signup activity

    private EditText name,pass,email,confirm_pass;
    private Button signupB;
    private ImageView backB;
    private FirebaseAuth mAuth;
    private String emailStr,passStr,nameStr,confirmPassStr;
    private Dialog progressDialog;
    private TextView dialogText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        name = findViewById(R.id.user_name);
        email = findViewById(R.id.email);
        pass = findViewById(R.id.password);
        confirm_pass = findViewById(R.id.confirm_pass);
        signupB = findViewById(R.id.signupB);
        backB = findViewById(R.id.backB);

        progressDialog = new Dialog(SignUpActivity.this);
        progressDialog.setContentView(R.layout.dialog_layout);
        progressDialog.setCancelable(false);
        progressDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);

        dialogText = progressDialog.findViewById(R.id.dialog_text);
        dialogText.setText("Registering User...");

        mAuth = FirebaseAuth.getInstance();

        //back button action
        backB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });

        //sign up button action
        signupB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validate()) {
                    signupNewUser();
                }
            }
        });

    }


    //check new user data validation
    private boolean validate()
    {
        nameStr = name.getText().toString().trim();
        passStr = pass.getText().toString().trim();
        emailStr = email.getText().toString().trim();
        confirmPassStr = confirm_pass.getText().toString().trim();

        if (nameStr.isEmpty())
        {
            name.setError("Enter Your Name");
            return false;
        }
        if(emailStr.isEmpty())
        {
            email.setError("Enter E-Mail ID");
            return false;
        }
        if(passStr.isEmpty())
        {
            pass.setError("Enter Password");
            return false;
        }
        if(confirmPassStr.isEmpty())
        {
            confirm_pass.setError("Enter Password");
            return false;
        }
        if(passStr.compareTo(confirmPassStr)!=0)
        {
            Toast.makeText(SignUpActivity.this, "Password and confirm Password should be same", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    //create new user functionality
    private void signupNewUser()
    {
        progressDialog.show();
        mAuth.createUserWithEmailAndPassword(emailStr, passStr)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            //verification email functionality
                            FirebaseUser user = mAuth.getCurrentUser();
                            user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Toast.makeText(SignUpActivity.this,"Verification Email has been sent in your email",Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(SignUpActivity.this,"Email not sent",Toast.LENGTH_SHORT).show();
                                }
                            });

                            Toast.makeText(SignUpActivity.this,"Sign Up Successful",Toast.LENGTH_SHORT).show();

                            //create new user data
                            DBQuery.createUserData(emailStr, nameStr, new MyCompleteListener() {
                                @Override
                                public void onSuccess() {

                                    DBQuery.loadData(new MyCompleteListener() {
                                        @Override
                                        public void onSuccess() {
                                            progressDialog.dismiss();

                                            //Intent intent = new Intent(SignUpActivity.this,LoginActivity.class);
                                            //startActivity(intent);
                                            SignUpActivity.this.finish();
                                        }

                                        @Override
                                        public void onFailure() {
                                            Toast.makeText(SignUpActivity.this,"Something went wrong ? please try again Later!",Toast.LENGTH_SHORT).show();
                                            progressDialog.dismiss();
                                        }
                                    });



                                }

                                @Override
                                public void onFailure() {

                                    Toast.makeText(SignUpActivity.this,"Something went wrong ? please try again Later!",Toast.LENGTH_SHORT).show();
                                    progressDialog.dismiss();

                                }
                            });

                        } else {
                            // If sign in fails, display a message to the user.
                            progressDialog.dismiss();
                            Toast.makeText(SignUpActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}