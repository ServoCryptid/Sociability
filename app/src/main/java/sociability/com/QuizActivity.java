package sociability.com;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import Database.FirebaseDB;

public class QuizActivity extends AppCompatActivity {
    private RadioGroup radioGroup;
    private Resources resources;
    private String[] quiz_questions;
    private List<String> quiz_answers = new ArrayList<String>(10);
    private int question_number;
    private TextView current_question;
    private TextView remaining_questions_textView;
    private static final int MAX_NO_OF_QUESTIONS = 9;//starting from 0 - 9
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        resources = getResources();
        quiz_questions = resources.getStringArray(R.array.quiz_questions);

        question_number = 0 ; //the quiz starts with the first question
        current_question = findViewById(R.id.question_textView);
        current_question.setText(quiz_questions[question_number]);

        remaining_questions_textView = findViewById(R.id.displaying_no_questions_textView);
        updateRemainingQuestionsNumber();

        radioGroup = findViewById(R.id.radioGroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // find which radio button is selected
                if(checkedId == R.id.radio_button_option1) {
                    quiz_answers.add("1");
                } else if(checkedId == R.id.radio_button_option2) {
                    quiz_answers.add("2");

                } else if(checkedId == R.id.radio_button_option3) {
                    quiz_answers.add("3");

                } else if(checkedId == R.id.radio_button_option4) {
                    quiz_answers.add("4");

                } else if(checkedId == R.id.radio_button_option5) {
                    quiz_answers.add("5");

                } else if(checkedId == R.id.radio_button_option6) {
                    quiz_answers.add("6");

                } else if(checkedId == R.id.radio_button_option7){
                    quiz_answers.add("7");

                }
            }
        });
    }

    public void nextQuestion(View v){ // when the next button is pressed, update the textview

        if(radioGroup.getCheckedRadioButtonId()==-1) // check if a radiobutton was selected
        {
            Toast.makeText(getApplicationContext(), "Please select an answer!", Toast.LENGTH_SHORT).show();
        }
        else {
            radioGroup.clearCheck();
            quiz_answers.remove(question_number);

            question_number++;

            if (question_number == 1+ MAX_NO_OF_QUESTIONS) { //we reached the last question
                Button b = findViewById(R.id.button_next);
                b.setClickable(false);

                //update the DB
                TelephonyManager tm = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE); // todo:put this in a helper class

                FirebaseDB db = new FirebaseDB();
                db.setQuizAnswers(quiz_answers);
                db.setSimSerialNumber(tm.getSimSerialNumber().toString());
                db.updateDB();


            } else {
                current_question.setText(quiz_questions[question_number]);
                updateRemainingQuestionsNumber();
            }
        }
    }

    private void updateRemainingQuestionsNumber(){
        int number = question_number + 1;
        String textToDisplay = number+"/10";

        remaining_questions_textView.setText(textToDisplay);
    }


}
