package Databases.Firebase;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

import static Helper.Utils.getTimestamp;

/**
 * Created by Larisa on 02.01.2018.
 */

public class FirebaseDB  {
    private DatabaseReference databaseReference;

    private String simSerialNumber ;

    public void updateResponsesToDB_shortQuiz(HashMap <String,String> quizAnswers){//todo: and this
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("Users/" + simSerialNumber+"/quiz_results/short_quiz/" + getTimestamp());
        databaseReference.setValue(quizAnswers);
    }

    public void updateResponsesToDB_longQuiz(HashMap <String,String> quizAnswers){ //todo:refactor this
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("Users/" + simSerialNumber+"/quiz_results/long_quiz/" + getTimestamp());
        databaseReference.setValue(quizAnswers);
    }

    public void updateResponsesToDB_personalQuiz(HashMap <String,String> quizAnswers){//todo: and this
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("Users/" + simSerialNumber+"/quiz_results/personal_quiz/" + getTimestamp());
        databaseReference.setValue(quizAnswers);
    }

    public void updateStatsToDB(HashMap<String,Double> list){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference();

        if(list.size() == 12)
            databaseReference.child("Users").child(simSerialNumber).child("phone_stats").child("Call").setValue(list);
        else
            databaseReference.child("Users").child(simSerialNumber).child("phone_stats").child("SMS").setValue(list);


    }

    public void setSimSerialNumber(String simSerialNumber) {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference();
        this.simSerialNumber = simSerialNumber;
    }


}
