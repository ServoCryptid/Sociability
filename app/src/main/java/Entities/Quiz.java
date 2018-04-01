package Entities;

import java.util.HashMap;

/**
 * Created by Larisa on 28.03.2018.
 */

public class Quiz {
    public HashMap<Integer,String> questions = new HashMap<Integer,String>();

    public Quiz() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public Quiz(HashMap<Integer, String> map){
        this.questions = map;
    }
}
