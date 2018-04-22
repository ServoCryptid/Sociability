package sociability.com;

import android.*;
import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import Databases.Firebase.FirebaseDB;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int MY_PERMISSIONS_REQUESTS = 333;
    public static FirebaseDB fDB;
    public static int short_quiz_completed = 0 ;// - if not completed, 1 for completed
    public static int long_quiz_completed = 0 ;// - if not completed, 1 for completed
    public static int personal_quiz_completed = 0 ;// - if not completed, 1 for completed
    private final static String PREFS_SETTINGS = "prefs_settings";
    public static SharedPreferences prefsUser, prefsApp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // named preference file
        prefsUser = getSharedPreferences(PREFS_SETTINGS, Context.MODE_PRIVATE);
        // default prefs file for this app
        prefsApp = getPreferences(Context.MODE_PRIVATE);

        short_quiz_completed  = prefsUser.getInt("short", 0);
        long_quiz_completed  = prefsUser.getInt("long", 0);
        personal_quiz_completed  = prefsUser.getInt("personal", 0);

        CardView cv1 = findViewById(R.id.cv1);
        cv1.setOnClickListener(this);

        CardView cv2 = findViewById(R.id.cv2);
        cv2.setOnClickListener(this);

        CardView cv3 = findViewById(R.id.cv3);
        cv3.setOnClickListener(this);


        updateCardImages();

        if( checkAndRequestPermissions()){
            //If you have already permitted the permission
            ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("We are gathering data...");

            //region Set the SIM serial number as identifier for the user
            TelephonyManager tm ;
            tm = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
            fDB = new FirebaseDB();
            fDB.setSimSerialNumber(tm.getSimSerialNumber().toString());
            //endregion
            new Helper.FetchLogs(progressDialog, this).execute();
        }
    }

    private void updateCardImages(){

        if(short_quiz_completed == 1) {
            ImageView img = findViewById(R.id.image1);
            img.setImageResource(R.drawable.quiz_finished_card_icon);
        }

        if(long_quiz_completed == 1) {
            ImageView img2 = findViewById(R.id.image2);
            img2.setImageResource(R.drawable.quiz_finished_card_icon);
        }

        if(personal_quiz_completed == 1) {
            ImageView img3 = findViewById(R.id.image3);
            img3.setImageResource(R.drawable.quiz_finished_card_icon);
        }
    }
    @Override
    public void onClick(View v){
        Intent intent = new Intent(this, QuizActivity.class);

        switch(v.getId()){
            case R.id.cv1:
                //open quiz1
                intent.putExtra("message", "short quiz");
                startActivity(intent);
                break;
            case R.id.cv2:
                //open quiz2
                intent.putExtra("message", "long quiz");
                startActivity(intent);
                break;
            case R.id.cv3:
                //open quiz2
                intent.putExtra("message", "personal quiz");
                startActivity(intent);
                break;
        }
    }

    private boolean checkAndRequestPermissions(){
        boolean hasPermissionReadPhoneState = (ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED);

        boolean hasPermissionReadCallLog = (ContextCompat.checkSelfPermission(getApplicationContext(),
                android.Manifest.permission.READ_CALL_LOG) == PackageManager.PERMISSION_GRANTED);

        boolean hasPermissionReadSMSLog = (ContextCompat.checkSelfPermission(getApplicationContext(),
                android.Manifest.permission.READ_SMS) == PackageManager.PERMISSION_GRANTED);

        List<String> listPermissionsNeeded = new ArrayList<>();

        if (!hasPermissionReadCallLog) {
            listPermissionsNeeded.add(android.Manifest.permission.READ_CALL_LOG);
        }

        if (!hasPermissionReadPhoneState) {
            listPermissionsNeeded.add(Manifest.permission.READ_PHONE_STATE);
        }

        if (!hasPermissionReadSMSLog) {
            listPermissionsNeeded.add(android.Manifest.permission.READ_SMS);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this,
                    listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), MY_PERMISSIONS_REQUESTS);
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUESTS:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Permissions granted.", Toast.LENGTH_SHORT).show();


                } else {
                    // permission denied
                    Toast.makeText(this, "The app was not allowed the permissions it needs . Hence, it cannot function properly. Please consider granting it this permission", Toast.LENGTH_LONG).show();

                }
                break;

        }
        //restart activity with the permissions granted
        finish();
        startActivity(getIntent());
    }

}
