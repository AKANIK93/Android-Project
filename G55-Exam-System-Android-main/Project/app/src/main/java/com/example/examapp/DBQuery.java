package com.example.examapp;

import android.content.Intent;
import android.util.ArrayMap;

import androidx.annotation.NonNull;

import com.example.examapp.Models.CategoryModel;
import com.example.examapp.Models.ProfileModel;
import com.example.examapp.Models.QuestionModel;
import com.example.examapp.Models.ResultModel;
import com.example.examapp.Models.TestModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.firestore.WriteBatch;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class DBQuery {

        public static FirebaseFirestore gFirestore;
        public static List<CategoryModel> g_catList = new ArrayList<>();
        public static int g_selected_cat_index = 0;
        public static List<TestModel> g_testList =new ArrayList<>();
        public static  int g_selected_test_index = 0;
        public static List<QuestionModel> g_quesList = new ArrayList<>();
        public static ResultModel myPerformance = new ResultModel(null,0,-1);
        public static List<ResultModel> g_usersList = new ArrayList<>();
        public static int usersCount = 0;
        public static boolean isMeOnTopList = false;

        public static final int NOT_VISITED = 0;
        public static final int UNANSWERED = 1;
        public static final int ANSWERED = 2;
        public static final int REVIEW = 3;



        public static ProfileModel myProfile = new ProfileModel("Anik", null, null);
        private Class<? extends WriteBatch> aClass;

        public static void createUserData(String email,String name,MyCompleteListener completeListener)
        {
               Map<String,Object> userData = new ArrayMap<>();
                userData.put("EMAIL_ID",email);
                userData.put("NAME",name);
                userData.put("TOTAL_SCORE",0);

                DocumentReference userDoc = gFirestore.collection("USERS").document(FirebaseAuth.getInstance().getCurrentUser().getUid());

                WriteBatch batch = gFirestore.batch();

                batch.set(userDoc,userData);

                DocumentReference countDoc = gFirestore.collection("USERS").document("TOTAL_USERS");
                batch.update(countDoc,"COUNT", FieldValue.increment(1));

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
        }

        //save users information in firestore database
        public static void saveProfileData(String name,String phone,MyCompleteListener completeListener)
        {
             Map<String,Object>  profileData = new ArrayMap<>();
             profileData.put("NAME",name);
             if(phone!=null)
             {
                 profileData.put("PHONE",phone);
             }
             gFirestore.collection("USERS").document(FirebaseAuth.getInstance().getUid())
                     .update(profileData)
                     .addOnSuccessListener(new OnSuccessListener<Void>() {
                         @Override
                         public void onSuccess(Void unused) {
                            myProfile.setName(name);
                            if(phone!= null)
                            {
                                myProfile.setPhone(phone);
                            }
                            completeListener.onSuccess();
                         }
                     })
                     .addOnFailureListener(new OnFailureListener() {
                         @Override
                         public void onFailure(@NonNull Exception e) {
                                completeListener.onFailure();
                         }
                     });
        }


        public static void getUserData(MyCompleteListener completeListener)
        {
            gFirestore.collection("USERS").document(FirebaseAuth.getInstance().getUid())
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            myProfile.setName(documentSnapshot.getString("NAME"));
                            myProfile.setEmail(documentSnapshot.getString("EMAIL_ID"));

                            // store user phone number
                            if(documentSnapshot.getString("PHONE")  != null)
                            {
                                myProfile.setPhone(documentSnapshot.getString("PHONE"));
                            }

                            //store user score in particular test
                            myPerformance.setScore(documentSnapshot.getLong("TOTAL_SCORE").intValue());

                            completeListener.onSuccess();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                              completeListener.onFailure();
                        }
                    });
        }

        //ranking by total score
        public static void getTopUsers(MyCompleteListener completeListener)
        {
            g_usersList.clear();
            String myUID = FirebaseAuth.getInstance().getUid();

            gFirestore.collection("USERS")
                    .whereGreaterThan("TOTAL_SCORE",0)
                    .orderBy("TOTAL_SCORE", Query.Direction.DESCENDING)
                    .limit(20)
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                            int rank=1;
                            for(QueryDocumentSnapshot doc:queryDocumentSnapshots)
                            {
                                g_usersList.add(new ResultModel(
                                        doc.getString("NAME"),
                                        doc.getLong("TOTAL_SCORE").intValue(),
                                        rank
                                ));

                                if(myUID.compareTo(doc.getId())==0)
                                {
                                    isMeOnTopList=true;
                                    myPerformance.setRank(rank);
                                }

                                rank++;
                            }
                            completeListener.onSuccess();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                               completeListener.onFailure();
                        }
                    });
        }

        //count total users
        public static void getUsersCount(MyCompleteListener completeListener)
        {
            gFirestore.collection("USERS").document("TOTAL_USERS")
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                             usersCount = documentSnapshot.getLong("COUNT").intValue();
                             completeListener.onSuccess();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            completeListener.onFailure();
                        }
                    });
        }

        //get test top score
        public static void loadMyScore(MyCompleteListener completeListener)
        {
            gFirestore.collection("USERS").document(FirebaseAuth.getInstance().getUid())
                    .collection("USER_DATA").document("MY_SCORES")
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            for(int i=0 ; i<g_testList.size() ;i++)
                            {
                                int top=0;
                                if(documentSnapshot.get(g_testList.get(i).getTestID()) != null)
                                {
                                     top = documentSnapshot.getLong(g_testList.get(i).getTestID()).intValue();
                                }
                                g_testList.get(i).setTopScore(top);
                            }
                             completeListener.onSuccess();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                              completeListener.onFailure();
                        }
                    });

        }

        //update score in firebase database,update top score of test
        public  static void saveResult(final int score,MyCompleteListener completeListener)
        {
               WriteBatch batch =gFirestore.batch();
               DocumentReference userDoc = gFirestore.collection("USERS").document(FirebaseAuth.getInstance().getUid());
               batch.update(userDoc,"TOTAL_SCORE",score);

               if(score > g_testList.get(g_selected_test_index).getTopScore())

               {
                   DocumentReference scoreDoc = userDoc.collection("USER_DATA").document("MY_SCORES");
                   Map<String,Object> testData = new ArrayMap<>();
                   //get test name and update score
                   testData.put(g_testList.get(g_selected_test_index).getTestID(),score);
                   //testData.put(g_catList.get(g_selected_cat_index).g,score);
                   batch.set(scoreDoc,testData, SetOptions.merge());
               }

               batch.commit()
                       .addOnSuccessListener(new OnSuccessListener<Void>() {
                           @Override
                           public void onSuccess(Void unused) {
                                  if(score > g_testList.get(g_selected_test_index).getTopScore() )
                                  {
                                      g_testList.get(g_selected_test_index).setTopScore(score);
                                  }
                                  myPerformance.setScore(score);
                                  completeListener.onSuccess();
                           }
                       })
                       .addOnFailureListener(new OnFailureListener() {
                           @Override
                           public void onFailure(@NonNull Exception e) {
                                  completeListener.onFailure();
                           }
                       });
        }

        //get all category information from fire store database and load categories information in category fragment
       //section after clicking home navigation button
        public static void loadCategories( MyCompleteListener completeListener)
        {
             g_catList.clear();
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

                             long catCount = catListDoc.getLong("COUNT");

                             for(int i=1; i<=catCount ; i++)
                             {
                                  String catID = catListDoc.getString("CAT"+String.valueOf(i)+"_ID");
                                  QueryDocumentSnapshot catDoc = docList.get(catID);

                                  int noOfTest = catDoc.getLong("NO_OF_TESTS").intValue();
                                  String catName = catDoc.getString("NAME");

                                  g_catList.add(new CategoryModel(catID,catName,noOfTest));

                             }

                             completeListener.onSuccess();


                         }
                     })
                     .addOnFailureListener(new OnFailureListener() {
                         @Override
                         public void onFailure(@NonNull Exception e) {
                                completeListener.onFailure();
                         }
                     });
        }

        //get questions for selected test from database then loadQuestions after clicking start test button
        public static void loadQuestions(MyCompleteListener completeListener)
        {
            g_quesList.clear();
             gFirestore.collection("QUESTIONS")
                     .whereEqualTo("CATEGORY",g_catList.get(g_selected_cat_index).getDocID())
                     .whereEqualTo("TEST",g_testList.get(g_selected_test_index).getTestID(

                     ))
                     .get()
                     .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                         @Override
                         public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                             for(DocumentSnapshot doc: queryDocumentSnapshots)
                             {
                                   g_quesList.add(new QuestionModel(
                                         doc.getString("QUESTION"),
                                           doc.getString("A"),
                                           doc.getString("B"),
                                           doc.getString("C"),
                                           doc.getString("D"),
                                           doc.getLong("ANSWER").intValue(),
                                           -1,
                                           NOT_VISITED
                                   ));

                             }
                             completeListener.onSuccess();
                         }
                     })
                     .addOnFailureListener(new OnFailureListener() {
                         @Override
                         public void onFailure(@NonNull Exception e) {
                              completeListener.onFailure();
                         }
                     });
        }


        //get test information from database and show these infomation after clicking selected category
        //section
        public static void loadTestData( MyCompleteListener completeListener)
        {
            g_testList.clear();

            gFirestore.collection("CATEGORY").document(g_catList.get(g_selected_cat_index).getDocID())
                    .collection("TEST_LIST").document("TEST_INFO")
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {

                            int noOfTests = g_catList.get(g_selected_cat_index).getNoOfTests();

                            for(int i=1 ; i <= noOfTests ;i++)
                            {
                                 g_testList.add(new TestModel(
                                         documentSnapshot.getString("TEST"+String.valueOf(i)+"_ID"),
                                         documentSnapshot.getLong("TEST"+String.valueOf(i)+"_TIME").intValue(),
                                         0
                                 ));
                            }


                              completeListener.onSuccess();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                          completeListener.onFailure();
                }
            });
        }


        public static void loadData( MyCompleteListener completeListener)
        {
               loadCategories(new MyCompleteListener() {
                   @Override
                   public void onSuccess() {
                       getUserData(new MyCompleteListener() {
                           @Override
                           public void onSuccess() {
                               getUsersCount(completeListener);
                           }

                           @Override
                           public void onFailure() {
                               completeListener.onFailure();
                           }
                       });
                   }

                   @Override
                   public void onFailure() {
                         completeListener.onFailure();
                   }
               });
        }

        //store new  category information in database
        public static void addCategory(String cat,MyCompleteListener completeListener)
        {
            WriteBatch batch =gFirestore.batch();
            //userDoc = gFirestore.collection("CATEGORY");
            Map<String,Object>  profileData = new ArrayMap<>();

            String id = gFirestore.collection("CATEGORY").document().getId();

            profileData.put("CAT_ID",id);
            profileData.put("NAME",cat);
            profileData.put("NO_OF_TESTS",0);

            g_catList.add(new CategoryModel(id,cat,0));

            gFirestore.collection("CATEGORY").document(id).set(profileData);


            //batch.set(userDoc,profileData, SetOptions.merge());

            DocumentReference countDoc = gFirestore.collection("CATEGORY").document("Categories");

            batch.update(countDoc,"COUNT", FieldValue.increment(1));

            long x = g_catList.size();

            batch.update(countDoc,"CAT"+String.valueOf(x)+"_ID",id);

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

        }

}
