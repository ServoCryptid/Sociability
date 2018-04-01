package sociability.com;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import Databases.FirebaseDB;
import Databases.FirebaseQuizzes;

public class QuizActivity extends AppCompatActivity {
    private RadioGroup radioGroup;
    private static ArrayList<String> quiz_questions;
    public static ArrayList<String> quiz_questions_short;
    public static ArrayList<String> quiz_questions_long;
    private List<Integer> quiz_answers = new ArrayList<Integer>(10);
    private int question_number;
    private TextView current_question;
    private TextView remaining_questions_textView;
    private Button button_next_question;
    private TextView endingMessage;
    private static int MAX_NO_OF_QUESTIONS ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getIntent().getExtras();
        String message = bundle.getString("message");

        if(message.equals("short quiz")) {
            setContentView(R.layout.activity_quiz);
            quiz_questions = quiz_questions_short;
            MAX_NO_OF_QUESTIONS = quiz_questions.size();

        }
        else if(message.equals("long quiz")){
            setContentView(R.layout.activity_quiz2);
            quiz_questions = quiz_questions_long;
            MAX_NO_OF_QUESTIONS = quiz_questions.size();

        }

        question_number = 1 ; //the quiz starts with the first question
        current_question = findViewById(R.id.question_textView);
        current_question.setText(quiz_questions.get(question_number));

        button_next_question = findViewById(R.id.button_next);
        button_next_question.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(radioGroup.getCheckedRadioButtonId()==-1) // check if a radiobutton was selected
                {
                    Toast.makeText(getApplicationContext(), "Please select an answer!", Toast.LENGTH_SHORT).show();
                }
                else {
                    radioGroup.clearCheck();
                    quiz_answers.remove(question_number);

                    question_number++;

                    if (question_number ==  MAX_NO_OF_QUESTIONS) { //we reached the last question
                        showEndingMessage();
                        getQuizResults();

                    } else {
                        current_question.setText(quiz_questions.get(question_number));
                        updateRemainingQuestionsNumber();
                    }
                }
            }
        });

        remaining_questions_textView = findViewById(R.id.displaying_no_questions_textView);
        updateRemainingQuestionsNumber();

        endingMessage = findViewById(R.id.endingMessage);
        endingMessage.setVisibility(View.INVISIBLE); //until the quiz is completed

        radioGroup = findViewById(R.id.radioGroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // find which radio button is selected
                if(checkedId == R.id.radio_button_option1) {
                    quiz_answers.add(1);
                } else if(checkedId == R.id.radio_button_option2) {
                    quiz_answers.add(2);

                } else if(checkedId == R.id.radio_button_option3) {
                    quiz_answers.add(3);

                } else if(checkedId == R.id.radio_button_option4) {
                    quiz_answers.add(4);

                } else if(checkedId == R.id.radio_button_option5) {
                    quiz_answers.add(5);

                } else if(checkedId == R.id.radio_button_option6) {
                    quiz_answers.add(6);

                } else if(checkedId == R.id.radio_button_option7){
                    quiz_answers.add(7);

                }
            }
        });
    }

    private void showEndingMessage() {
        current_question.setVisibility(View.INVISIBLE);
        radioGroup.setVisibility(View.INVISIBLE);
        button_next_question.setVisibility(View.INVISIBLE);
        remaining_questions_textView.setVisibility(View.INVISIBLE);

        endingMessage.setVisibility(View.VISIBLE);
    }

    private void updateRemainingQuestionsNumber(){
        int number = question_number;
        String textToDisplay = number +"/"+ (MAX_NO_OF_QUESTIONS-1);

        remaining_questions_textView.setText(textToDisplay);
    }

    private void getQuizResults(){
        int reversed [] = {7,6,5,4,3,2,1};
        HashMap <String,String> results = new HashMap<String,String>();

        double O=0;
        double C=0;
        double E=0;
        double A=0;
        double N=0;

        O = (quiz_answers.get(4) + reversed[quiz_answers.get(9)-1])/2;
        if(0 <= O && O<=4.30)
            results.put("openness"," - Low");
        else
            if(4.31<=O && O <=6.44)
                results.put("openness","- Medium");
            else
                if(6.45<=O && O<=7.00)
                    results.put("openness"," - High");

        C = (quiz_answers.get(2) + reversed[quiz_answers.get(7)-1])/2;
        if(0 <= C && C<=4.07)
            results.put("conscientiousness "," - Low");
        else
        if(4.08<=C && C <=6.71)
            results.put("conscientiousness"," -  Medium");
        else
        if(6.72<=C && C<=7.00)
            results.put("conscientiousness"," - High");

        E = (double)(quiz_answers.get(0) + reversed[quiz_answers.get(5)-1])/2;
        if(0 <= E && E<=2.98)
            results.put("extraversion "," - Low");
        else
            if(2.99<=E && E <=5.88)
             results.put("extraversion"," -  Medium");
            else
            if(5.89<=E && E<=7.00)
                results.put("extraversion"," - High");

        A = (quiz_answers.get(6) + reversed[quiz_answers.get(1)-1])/2;
        if(0 <= A && A<=4.11)
            results.put("agreeableness "," - Low");
        else
            if(4.12<=A && A <=6.33)
                results.put("agreeableness "," -  Medium");
            else
            if(6.34<=A && A<=7.00)
                results.put("agreeableness "," - High");

        N = (quiz_answers.get(3) + reversed[quiz_answers.get(8)-1])/2;
        if(0 <= N && N<=3.40)
            results.put("neuroticism "," - Low");
        else
            if(3.41<=N && N <=6.24)
                results.put("neuroticism "," -  Medium");
            else
            if(6.25<=N && N<=7.00)
                results.put("neuroticism "," - High");


        //update the DB
         TelephonyManager tm = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE); // todo:put this in a helper class

        FirebaseDB db = new FirebaseDB();
        db.setSimSerialNumber(tm.getSimSerialNumber().toString());
         db.updateResponsesToDB(results);
    }

}
