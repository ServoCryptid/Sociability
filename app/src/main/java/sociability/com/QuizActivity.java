package sociability.com;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import static sociability.com.ResultsActivity.getShortQuizResults;

public class QuizActivity extends AppCompatActivity {
    public static ArrayList<String> quiz_questions_short;
    public static ArrayList<String> quiz_questions_long;
    private static ArrayList<String> quiz_questions;
    private List<Integer> quiz_answers = new ArrayList<Integer>(10);
    private int question_number;
    private static int MAX_NO_OF_QUESTIONS ;
    private RadioGroup radioGroup;
    private TextView current_question;
    private TextView remaining_questions_textView;
    private Button button_next_question;
    private TextView endingMessage;
    private String quizType; //"short quiz" or "long quiz"

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        quiz_answers.clear(); //clear the list of quiz answers

        //region Get the selected quiz type from the calling activity
        Bundle bundle = getIntent().getExtras();
        quizType = bundle.getString("message");

        if(quizType.equals("short quiz")) {
            setContentView(R.layout.activity_quiz);
            quiz_questions = quiz_questions_short;
            MAX_NO_OF_QUESTIONS = quiz_questions.size();

        }
        else if(quizType.equals("long quiz")){
            setContentView(R.layout.activity_quiz2);
            quiz_questions = quiz_questions_long;
            MAX_NO_OF_QUESTIONS = quiz_questions.size();

        }
        //endregion

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
                        getShortQuizResults(quiz_answers);

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

        if(quizType.equals("long quiz"))
            endingMessage.setText(ResultsActivity.getLongQuizScore(quiz_answers));

        endingMessage.setVisibility(View.VISIBLE);

    }

    private void updateRemainingQuestionsNumber(){
        int number = question_number;
        String textToDisplay = number +"/"+ (MAX_NO_OF_QUESTIONS-1);

        remaining_questions_textView.setText(textToDisplay);
    }

}
