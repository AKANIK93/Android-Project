package com.example.examapp;

import static com.example.examapp.AdminFragment.add_quesList;
import static com.example.examapp.DBQuery.gFirestore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.ArrayMap;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.examapp.Models.CategoryModel;
import com.example.examapp.Models.QuestionModel;
import com.example.examapp.Models.TestModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.example.examapp.AddQuestionActivity;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.firestore.WriteBatch;

public class AddTestActivity extends AppCompatActivity {

    private EditText catT,testT,timeT;
    public static String  catStr,testStr,timeStr,id,id_doc;
    private Button addQB,addTCB;
    private ImageView backB;
    private int test_no;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_test);

        init();


        //add question button action
        addQB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validData())
                {
                    Intent intent = new Intent(AddTestActivity.this,AddQuestionActivity.class);
                    startActivity(intent);
                    //setContentView(R.layout.question_item_layout);
                }
            }
        });

        addTCB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addTest();
            }
        });

        backB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void addTest()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(AddTestActivity.this);
        builder.setCancelable(true);
        View view = getLayoutInflater().inflate(R.layout.add_test_alert_dialog,null);

        Button add_test_cancelB = view.findViewById(R.id.add_test_cancelB);
        Button add_test_yesB = view.findViewById(R.id.add_test_yesB);

        builder.setView(view);
        AlertDialog alertDialog = builder.create();

        add_test_cancelB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        //if we click add test confirmation button this test will be add in related category
        add_test_yesB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alertDialog.dismiss();
                submitTest();
                //Intent intent = new Intent(AddTestActivity.this,AdminFragment.class);
               // startActivity(intent);
                AddTestActivity.this.finish();
            }
        });
        alertDialog.show();
    }

    private void submitTest()
    {
        int i;
        MyCompleteListener completeListener = new MyCompleteListener() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onFailure() {

            }
        };


        //we store our test in firestore database after confirmation
        gFirestore.collection("CATEGORY").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        Map<String, QueryDocumentSnapshot> docList = new ArrayMap<>();

                        for(QueryDocumentSnapshot doc : queryDocumentSnapshots)
                        {
                            docList.put(doc.getId(),doc);
                        }

                        QueryDocumentSnapshot catListDoc = docList.get("Categories");

                        //get total category
                        long catCount = catListDoc.getLong("COUNT");

                        //get category id
                        for(int i=1; i<=catCount ; i++)
                        {
                            id = catListDoc.getString("CAT"+String.valueOf(i)+"_ID");
                            QueryDocumentSnapshot catDoc = docList.get(id);

                            //int noOfTest = catDoc.getLong("NO_OF_TESTS").intValue();
                            String catName = catDoc.getString("NAME");
                            //id_doc = catDoc.getString("CAT_ID");

                            if(catName.equals(catStr))
                            {
                                test_no = catDoc.getLong("NO_OF_TESTS").intValue();
                                test_no++;
                                id_doc = catDoc.getString("CAT_ID");
                                break;
                            }

                        }

                        saveData();

                        //store test questions and test information in firestore database
                        WriteBatch batch = gFirestore.batch();
                        DocumentReference countDoc = gFirestore.collection("CATEGORY").document(id_doc);

                        batch.update(countDoc,"NO_OF_TESTS",test_no);

                        Map<String,Object>  profileData = new ArrayMap<>();
                        profileData.put("TEST"+test_no+"_ID",testStr);
                        profileData.put("TEST"+test_no+"_TIME",Integer.parseInt(timeStr));

                        DocumentReference userDoc = gFirestore.collection("CATEGORY").document(id_doc);
                        DocumentReference testDoc = userDoc.collection("TEST_LIST").document("TEST_INFO");
                        batch.set(testDoc,profileData, SetOptions.merge());

                        batch.commit()
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {

                                        completeListener.onSuccess();

                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {

                                        completeListener.onFailure();

                                    }
                                });


                        completeListener.onSuccess();


                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        completeListener.onFailure();
                    }
                });

        ///Intent intent = new Intent(AddTestActivity.this,AddTestActivity.class);
        // startActivity(intent);
    }


    //save test questions in profile data(map)
    private void saveData()
    {
        int i;

        for(i=0;i<add_quesList.size();i++)
        {
            id =  gFirestore.collection("QUESTIONS").document().getId();
            Map<String,Object> profileData = new ArrayMap<>();
            profileData.put("QUESTION",add_quesList.get(i).getQuestion());
            profileData.put("A",add_quesList.get(i).getOption1());
            profileData.put("B",add_quesList.get(i).getOption2());
            profileData.put("C",add_quesList.get(i).getOption3());
            profileData.put("D",add_quesList.get(i).getOption4());
            profileData.put("ANSWER",add_quesList.get(i).getAnswer());
            profileData.put("CATEGORY",id_doc);
            profileData.put("TEST",testStr);
            gFirestore.collection("QUESTIONS").document(id)
                    .set(profileData);
        }




    }


    private void init()
    {
        catT = findViewById(R.id.add_test_cat_name);
        testT = findViewById(R.id.add_test_name);
        timeT = findViewById(R.id.test_time);
        backB = findViewById(R.id.add_test_backB);
        addQB = findViewById(R.id.add_test_questionB);
        addTCB = findViewById(R.id.add_test_confirmB);
    }

    private boolean validData()
    {
        catStr = catT.getText().toString().trim();
        testStr = testT.getText().toString().trim();
        timeStr = timeT.getText().toString().trim();

        if (catStr.isEmpty())
        {
            catT.setError("Enter Category Name");
            return false;
        }
        if(testStr.isEmpty())
        {
            testT.setError("Enter Test Name");
            return false;
        }
        if(timeStr.isEmpty())
        {
            timeT.setError("Enter Time");
            return false;
        }
        return true;
    }
}