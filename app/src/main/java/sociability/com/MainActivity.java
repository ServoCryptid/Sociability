package sociability.com;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView call;
    private StringBuffer notificationMsg;
    private static final int MY_PERMISSIONS_REQUEST_READ_CALL_LOG = 1;
    private static final int MY_PERMISSIONS_REQUEST_READ_SMS_LOG = 2;
    private static final int MY_PERMISSIONS_REQUEST_READ_PHONE_STATE = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button b1 = (Button) findViewById(R.id.button_show_results);
        b1.setOnClickListener(this);

        Button b2 = (Button) findViewById(R.id.button_quiz);
        b2.setOnClickListener(this);

        Button b3 = (Button) findViewById(R.id.button_about);
        b3.setOnClickListener(this);


    //    notificationMsg = new StringBuffer();*/

     //   LocalBroadcastManager.getInstance(this).registerReceiver(onNotice, new IntentFilter("Msg"));
        requestPermissionsNeeded();

        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("We are gathering data...");

        new Helper.FetchLogs(progressDialog, this).execute();

    }

    @Override
    public void onClick(View v) {
        // Perform action on click
        switch(v.getId()) {
            case R.id.button_show_results:
            //start the show results activity
                Intent intent = new Intent(this, ResultsActivity.class);
                startActivity(intent);
                break;
            case R.id.button_quiz:
            //start QuizActivity.java
                intent = new Intent(this, QuizActivity.class);
                startActivity(intent);
                break;
            case R.id.button_about:
                //start AboutActivity.java
                 intent = new Intent (this, AboutActivity.class);
                 startActivity(intent);
                 break;
        }
    }

    private BroadcastReceiver onNotice = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {

            String pack = intent.getStringExtra("package");
            String title = intent.getStringExtra("title");
            String text = intent.getStringExtra("text");

            notificationMsg.append(pack + "\n" + title + "\n" + text + "\n\n");

            call.setText(notificationMsg);

        }
    };

    private void requestPermissionsNeeded(){
        boolean hasPermissionReadPhoneState = (ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED);

        if (!hasPermissionReadPhoneState) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_PHONE_STATE},
                    MY_PERMISSIONS_REQUEST_READ_PHONE_STATE);
        }

        boolean hasPermissionReadCallLog = (ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.READ_CALL_LOG) == PackageManager.PERMISSION_GRANTED);

        if (!hasPermissionReadCallLog) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_CALL_LOG},
                    MY_PERMISSIONS_REQUEST_READ_CALL_LOG);
        }

        boolean hasPermissionReadSMSLog = (ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.READ_SMS) == PackageManager.PERMISSION_GRANTED);

        if (!hasPermissionReadSMSLog) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_SMS},
                    MY_PERMISSIONS_REQUEST_READ_SMS_LOG);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_CALL_LOG:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Permission granted.", Toast.LENGTH_SHORT).show();

                    //reload my activity with permission granted
                    finish();
                    startActivity(getIntent());

                } else {
                    // permission denied
                    Toast.makeText(this, "The app was not allowed to read your call log. Hence, it cannot function properly. Please consider granting it this permission", Toast.LENGTH_LONG).show();

                }
                break;

            case MY_PERMISSIONS_REQUEST_READ_SMS_LOG:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Permission granted.", Toast.LENGTH_SHORT).show();

                    //reload my activity with permission granted
                    finish();
                    startActivity(getIntent());

                } else {
                    // permission denied
                    Toast.makeText(this, "The app was not allowed to get your SMS log. Hence, it cannot function properly. Please consider granting it this permission", Toast.LENGTH_LONG).show();

                }
                break;
            case MY_PERMISSIONS_REQUEST_READ_PHONE_STATE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Permission granted.", Toast.LENGTH_SHORT).show();

                    //reload my activity with permission granted
                    finish();
                    startActivity(getIntent());

                } else {
                    // permission denied
                    Toast.makeText(this, "The app was not allowed to read your phone state. Hence, it cannot function properly. Please consider granting it this permission", Toast.LENGTH_LONG).show();

                }
                break;


            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

}
