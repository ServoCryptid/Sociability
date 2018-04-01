package Databases;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import sociability.com.QuizActivity;

/**
 * Created by Larisa on 28.03.2018.
 */

public class FirebaseQuizzes {
    private DatabaseReference databaseReference;

    public void getSmallQuizQuestions(){ // get the quiz questions from the database //trebuie sa returneze array-ul cu intrebarile

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("Quizzes/short_quiz");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                GenericTypeIndicator <ArrayList<String>> myGenericType = new GenericTypeIndicator<ArrayList<String>>(){};
                QuizActivity.quiz_questions_short = dataSnapshot.getValue(myGenericType);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}

        }); // it does only one read
    }

    public void getLargeQuizQuestions(){ // get the quiz questions from the database //trebuie sa returneze array-ul cu intrebarile

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("Quizzes/long_quiz");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                GenericTypeIndicator <ArrayList<String>> myGenericType = new GenericTypeIndicator<ArrayList<String>>(){};
                QuizActivity.quiz_questions_long = dataSnapshot.getValue(myGenericType);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}


        }); // it does only one read
    }
}
