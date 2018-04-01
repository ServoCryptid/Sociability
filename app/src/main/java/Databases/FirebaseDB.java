package Databases;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

/**
 * Created by Larisa on 02.01.2018.
 */

public class FirebaseDB  {
    private DatabaseReference databaseReference;

    private String simSerialNumber ;

    public void updateResponsesToDB(HashMap <String,String> quizAnswers){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference();
        databaseReference.child("Users").child(simSerialNumber).child("quiz_results").setValue(quizAnswers);
    }

    public void updateStatsToDB(HashMap<String,Double> list){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference();

        if(list.size() ==  3)
            databaseReference.child("Users").child(simSerialNumber).child("phone_stats").child("SMS").setValue(list);
        else
            databaseReference.child("Users").child(simSerialNumber).child("phone_stats").child("Call").setValue(list);

    }

    public void setSimSerialNumber(String simSerialNumber) {
        this.simSerialNumber = simSerialNumber;
    }


}
