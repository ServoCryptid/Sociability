package sociability.com;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
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

import Databases.Firebase.FirebaseDB;
import Databases.Firebase.FirebaseQuizzes;

public class FirstScreenActivity extends AppCompatActivity implements View.OnClickListener {
   // private TextView call;
    private StringBuffer notificationMsg;
    public static FirebaseDB fDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_screen);

         final Button b = (Button) findViewById(R.id.start_button);
         final Drawable d = b.getBackground();

         b.setOnClickListener(this);
         b.setEnabled(false);
         b.setBackground(d);

         //region Set the SIM serial number as identifier for the user
         TelephonyManager tm ;
         tm = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
         fDB = new FirebaseDB();
         fDB.setSimSerialNumber(tm.getSimSerialNumber().toString());
         //endregion

         //region Retrieving the Quiz questions from Firebase
        FirebaseQuizzes fq = new FirebaseQuizzes();
        fq.getSmallQuizQuestions(); //retrieve questions from Firebase short quiz
        fq.getLargeQuizQuestions();//retrieve questions from Firebase large quiz
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
        TextView mTextView = findViewById(R.id.textView);
        mTextView.setText(myString);
        mTextView.setMovementMethod(LinkMovementMethod.getInstance());
        mTextView.setHighlightColor(getResources().getColor(R.color.colorAccent));

        //endregion

        CheckBox mCheckBox= ( CheckBox ) findViewById( R.id.policy_agree_checkbox);
        mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked){
                if ( isChecked ) {
                    b.setEnabled(true);
                    b.setBackgroundColor(getResources().getColor(R.color.colorAccent));

                }else{
                    b.setEnabled(false);
                    b.setBackground(d);
                }

            }
        });

    //    notificationMsg = new StringBuffer();*/
     //   LocalBroadcastManager.getInstance(this).registerReceiver(onNotice, new IntentFilter("Msg"));


    }

    @Override
    public void onClick(View v) {
        // Perform action on click
        switch(v.getId()) {
            case R.id.start_button:
            //start the main activity
                Intent intent = new Intent(this, MainActivity.class);
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

    }
}
