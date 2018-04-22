package Databases.Firebase;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import sociability.com.QuizActivity;
import Helper.ComputeResults;

/**
 * Created by Larisa on 28.03.2018.
 */

public class FirebaseQuizzes {
    public DatabaseReference databaseReference;
    private FirebaseDatabase database ;
    public ValueEventListener myListenerShort;
    public ValueEventListener myListenerLong;
    public ValueEventListener myListenerPersonal;

    public void getSmallQuizQuestions(){ // get the quiz questions from the database
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("Quizzes/short_quiz");
        myListenerShort = databaseReference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                GenericTypeIndicator <ArrayList<String>> myGenericType = new GenericTypeIndicator<ArrayList<String>>(){};
                QuizActivity.quiz_questions_short = dataSnapshot.getValue(myGenericType);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}

        }); // it does only one read
    }

    public void getLargeQuizQuestions(){ //todo: refactor this

        databaseReference = database.getReference("Quizzes/long_quiz");
        myListenerLong = databaseReference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                GenericTypeIndicator <ArrayList<String>> myGenericType = new GenericTypeIndicator<ArrayList<String>>(){};
                QuizActivity.quiz_questions_long = dataSnapshot.getValue(myGenericType);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}


        });
    }

    public void getPersonalQuizQuestions(){
        databaseReference = database.getReference("Quizzes/personal_quiz");
        myListenerPersonal = databaseReference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                GenericTypeIndicator <ArrayList<String>> myGenericType = new GenericTypeIndicator<ArrayList<String>>(){};
                QuizActivity.quiz_questions_personal = dataSnapshot.getValue(myGenericType);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}


        });
    }

    public void getLargeQuizScore(){
        databaseReference = database.getReference("Scores/long_quiz");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                GenericTypeIndicator <ArrayList<String>> myGenericType = new GenericTypeIndicator<ArrayList<String>>(){};
                ComputeResults.long_quiz_scores = dataSnapshot.getValue(myGenericType);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}


        });
    }
}
