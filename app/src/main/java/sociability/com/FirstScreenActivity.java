package sociability.com;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import Databases.Firebase.FirebaseQuizzes;

public class FirstScreenActivity extends AppCompatActivity implements View.OnClickListener {
   // private TextView call;
    private StringBuffer notificationMsg;
    private  FirebaseQuizzes fq;
    private final static String PREFS_SETTINGS = "prefs_settings";
    public static SharedPreferences prefsUser, prefsApp;
    public static int short_quiz_completed = 0 ;// - if not completed, 1 for completed
    public static int long_quiz_completed = 0 ;// - if not completed, 1 for completed
    public static int personal_quiz_completed = 0 ;// - if not completed, 1 for completed
    public static int agree_terms = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_screen);

        // named preference file
        prefsUser = getSharedPreferences(PREFS_SETTINGS, Context.MODE_PRIVATE);
        // default prefs file for this app
        prefsApp = getPreferences(Context.MODE_PRIVATE);

        short_quiz_completed  = prefsUser.getInt("short", 0);
        long_quiz_completed  = prefsUser.getInt("long", 0);
        personal_quiz_completed  = prefsUser.getInt("personal", 0);
        agree_terms = prefsUser.getInt("agree_terms", 0);

         final Button b = (Button) findViewById(R.id.start_button);
         final Drawable d = b.getBackground();

         b.setOnClickListener(this);
         b.setEnabled(false);
         b.setBackground(d);

         //region Retrieving the Quiz questions from Firebase

        fq = new FirebaseQuizzes();
        fq.getSmallQuizQuestions(); //retrieve questions from Firebase short quiz
        fq.getLargeQuizQuestions();//retrieve questions from Firebase large quiz
        fq.getPersonalQuizQuestions();//retrieve questions from Firebase for personal quiz
        fq.getLargeQuizScore(); //retrieve the scoring points from Firebase

        //endregion

         //region  Setting the spannablestring

        SpannableString myString = new SpannableString("I agree with Sociability's privacy policy.");
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
              //  startActivity(new Intent(MyActivity.this, NextActivity.class));
            }
            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);
            }
        };

        myString.setSpan(clickableSpan, 27, 42, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);

        dialog.setTitle("Privacy text");// todo: text stored in string resource
        dialog.setMessage("gvs"); //todo: text stored in string resource
        dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

            } });

        TextView mTextView = findViewById(R.id.textView);
        mTextView.setText(myString);
        mTextView.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                dialog.show();
            }
        });
        mTextView.setMovementMethod(LinkMovementMethod.getInstance());
        mTextView.setHighlightColor(getResources().getColor(R.color.colorAccent));

        //endregion

        CheckBox mCheckBox= ( CheckBox ) findViewById( R.id.policy_agree_checkbox);

        if(FirstScreenActivity.agree_terms == 1){
            mCheckBox.setChecked(true);
            b.setEnabled(true);
            b.setBackgroundColor(getResources().getColor(R.color.colorAccent));
        }
        else {
            mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        b.setEnabled(true);
                        b.setBackgroundColor(getResources().getColor(R.color.colorAccent));

                    } else {
                        b.setEnabled(false);
                        b.setBackground(d);
                    }
                }
            });
        }

    //    notificationMsg = new StringBuffer();*/
     //   LocalBroadcastManager.getInstance(this).registerReceiver(onNotice, new IntentFilter("Msg"));


    }

    @Override
    public void onClick(View v) {
        // Perform action on click
        switch(v.getId()) {
            case R.id.start_button:
            //start the RadarChart activity
               // Intent intent = new Intent(this, MainActivity.class);
                Intent intent = new Intent(this, RadarChartActivity.class);
                startActivity(intent);
                break;
        }
    }

  /*  private BroadcastReceiver onNotice = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {

            String pack = intent.getStringExtra("package");
            String title = intent.getStringExtra("title");
            String text = intent.getStringExtra("text");

            notificationMsg.append(pack + "\n" + title + "\n" + text + "\n\n");

            call.setText(notificationMsg);

        }
    };
    */

    @Override
    protected void onDestroy(){
        // call the superclass method first
        super.onDestroy();
        fq.databaseReference.removeEventListener(fq.myListenerShort);
        fq.databaseReference.removeEventListener(fq.myListenerLong);
        fq.databaseReference.removeEventListener(fq.myListenerPersonal);
    }
}
