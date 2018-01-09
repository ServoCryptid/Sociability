package Databases;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Larisa on 02.01.2018.
 */

public class FirebaseDB  {
    private DatabaseReference databaseReference;

    private String simSerialNumber ;
    private List<String> quizAnswers = new ArrayList<String>();


    public void updateDB(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference();
        databaseReference.child("QuizResponses").child(simSerialNumber).setValue(quizAnswers);
    }

    public void setSimSerialNumber(String simSerialNumber) {
        this.simSerialNumber = simSerialNumber;
    }

    public void setQuizAnswers(List<String> quizAnswers) {
        this.quizAnswers = quizAnswers;
    }


}
