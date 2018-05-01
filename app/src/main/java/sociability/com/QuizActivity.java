package sociability.com;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import Helper.ComputeResults;

public class QuizActivity extends BaseActivity {
    public static ArrayList<String> quiz_questions_short;
    public static ArrayList<String> quiz_questions_long;
    public static ArrayList<String> quiz_questions_personal;
    private static ArrayList<String> quiz_questions;
    private List<Integer> quiz_answers = new ArrayList<Integer>(10);
    private List<String> quiz_answers_personal = new ArrayList<String>(10);
    private int question_number = 1;//the quiz starts with the first question
    private static int MAX_NO_OF_QUESTIONS ;
    private RadioGroup radioGroup;
    private TextView current_question;
    private TextView remaining_questions_textView;
    private Button button_next_question;
    private String quizType; //"short quiz" or "long quiz"
    private String myString;
    private int choice_number;// for the answer of the user at a certain question

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        quiz_answers.clear(); //clear the list of quiz answers

        //region Get the selected quiz type from the calling activity
        Bundle bundle = getIntent().getExtras();
        quizType = bundle.getString("message");

        if(quizType.equals("short quiz")) {
            setContentView(R.layout.activity_short_quiz);
            quiz_questions = quiz_questions_short;
            MAX_NO_OF_QUESTIONS = quiz_questions.size();

        }
        else if(quizType.equals("long quiz")){
            setContentView(R.layout.activity_long_quiz);
            quiz_questions = quiz_questions_long;
            MAX_NO_OF_QUESTIONS = quiz_questions.size();

        }
        else if(quizType.equals("personal quiz")){
            setContentView(R.layout.activity_personal_quiz);
            quiz_questions = quiz_questions_personal;
            MAX_NO_OF_QUESTIONS = quiz_questions.size();

        }
        //endregion

        current_question = findViewById(R.id.question_textView);
        radioGroup = findViewById(R.id.radioGroup);

        if(quizType.equals("personal quiz"))
            setRadioGroupText();

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
                    if(quizType.equals("personal quiz")) {
                        //quiz_answers_personal.remove(getStringResourceByName(myString));
                        quiz_answers_personal.add(getStringResourceByName(myString));
                    }
                    else
                        quiz_answers.add(choice_number);

                    question_number++;

                    if (question_number ==  MAX_NO_OF_QUESTIONS) { //we reached the last question
                        applyChanges();

                    } else {
                        if(quizType.equals("personal quiz"))
                            setRadioGroupText();

                        current_question.setText(quiz_questions.get(question_number));
                        updateRemainingQuestionsNumber();
                    }
                }
            }
        });

        remaining_questions_textView = findViewById(R.id.displaying_no_questions_textView);
        updateRemainingQuestionsNumber();

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // find which radio button is selected
                if(checkedId == R.id.radio_button_option1) {
                    myString = "Q" + question_number;
                    if(quizType.equals("personal quiz")){
                        myString += 0;
                    }
                    else
                        choice_number = 1;
                } else if(checkedId == R.id.radio_button_option2) {
                    myString = "Q" + question_number;
                    if(quizType.equals("personal quiz")){
                        myString += 1;
                    }
                    else
                        choice_number = 2;

                } else if(checkedId == R.id.radio_button_option3) {
                    myString = "Q" + question_number;
                    if(quizType.equals("personal quiz")){
                        myString += 2;
                    }
                    else
                        choice_number = 3;

                } else if(checkedId == R.id.radio_button_option4) {
                    choice_number = 4;

                } else if(checkedId == R.id.radio_button_option5) {
                    choice_number = 5;

                } else if(checkedId == R.id.radio_button_option6) {
                    choice_number = 6;

                } else if(checkedId == R.id.radio_button_option7){
                    choice_number = 7;
                }
            }
        });
    }

    private void setRadioGroupText(){
        String myString;
        for (int i =0 ; i < radioGroup.getChildCount(); i++){
            myString = "Q" + question_number + i;
            ((RadioButton) radioGroup.getChildAt(i)).setText(getStringResourceByName(myString));

            if((question_number >= 6 && question_number <= 8) && i == 2) // these 2 questions have 2, not 3 answers
                ((RadioButton) radioGroup.getChildAt(i)).setVisibility(View.GONE);
            else if (question_number > 8){
                ((RadioButton) radioGroup.getChildAt(i)).setVisibility(View.VISIBLE);
            }
       }
    }


    private String getStringResourceByName(String aString) {
        String packageName = getPackageName();
        int resId = getResources().getIdentifier(aString, "string", packageName);
        return getString(resId);
    }

    private void applyChanges() {

        if(quizType.equals("short quiz")) {
            ComputeResults.getShortQuizResult(quiz_answers);
            FirstScreenActivity.short_quiz_completed = 1;
            FirstScreenActivity.prefsUser.edit().putInt("short", 1).apply();
            finish();
        }
        else if(quizType.equals("long quiz")) {
            ComputeResults.getLongQuizResult(quiz_answers);
            FirstScreenActivity.long_quiz_completed = 1;
            FirstScreenActivity.prefsUser.edit().putInt("long", 1).apply();
            finish();
        }

        else if(quizType.equals("personal quiz")) {
            ComputeResults.getPersonalQuizResult(quiz_answers_personal);
            FirstScreenActivity.personal_quiz_completed = 1;
            FirstScreenActivity.prefsUser.edit().putInt("personal", 1).apply();
            finish();
        }

    }

    private void updateRemainingQuestionsNumber(){
        int number = question_number;
        String textToDisplay = number +"/"+ (MAX_NO_OF_QUESTIONS-1);

        remaining_questions_textView.setText(textToDisplay);
    }
}
