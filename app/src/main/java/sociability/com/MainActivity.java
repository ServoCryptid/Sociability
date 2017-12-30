package sociability.com;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CallLog;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Date;

import Database.DatabaseOperator;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView call;
    private StringBuffer notificationMsg;
    private static final int MY_PERMISSIONS_REQUEST_READ_CALL_LOG = 1;
    private static final int MY_PERMISSIONS_REQUEST_READ_SMS_LOG = 2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button b1 = (Button) findViewById(R.id.button_show_results);
        b1.setOnClickListener(this);

        Button b2 = (Button) findViewById(R.id.button_quiz);
        b2.setOnClickListener(this);
     /*   Realm.init(this);
        //Stetho.initializeWithDefaults(this);
        Stetho.initialize(
                Stetho.newInitializerBuilder(this)
                        .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
                        .enableWebKitInspector(RealmInspectorModulesProvider.builder(this).build())
                        .build());

        call = (TextView) findViewById(R.id.call);
        notificationMsg = new StringBuffer();*/

     //   LocalBroadcastManager.getInstance(this).registerReceiver(onNotice, new IntentFilter("Msg"));
      //  requestPermissionsNeeded();

    //   getCallDetails();
        // getSMSDetails();

    }

    @Override
    public void onClick(View v) {
        // Perform action on click
        switch(v.getId()) {
            case R.id.button_show_results:
            //start the show results activity
                break;
            case R.id.button_quiz:
            //start QuizActivity.java
                Intent intent = new Intent(this, QuizActivity.class);
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


            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
    private void getCallDetails() {
        DatabaseOperator db = new DatabaseOperator();

        StringBuffer sb = new StringBuffer();
        Cursor cursor = getContentResolver().query( CallLog.Calls.CONTENT_URI,null, null,null, null);

        int number = cursor.getColumnIndex( CallLog.Calls.NUMBER );
        int type = cursor.getColumnIndex( CallLog.Calls.TYPE );
        int date = cursor.getColumnIndex( CallLog.Calls.DATE);
        int duration = cursor.getColumnIndex( CallLog.Calls.DURATION);

        sb.append( "Call Details :");

        cursor.moveToNext();
       // while ( cursor.moveToNext() ) {
            String phNumber = cursor.getString( number );
            String callType = cursor.getString( type );
            String callDate = cursor.getString( date );
            Date callDayTime = new Date(Long.valueOf(callDate));
            String callDuration = cursor.getString( duration );
            String dir = null;

            int dircode = Integer.parseInt( callType );

            switch( dircode ) {
                case CallLog.Calls.OUTGOING_TYPE:
                    dir = "OUTGOING";
                    break;

                case CallLog.Calls.INCOMING_TYPE:
                    dir = "INCOMING";
                    break;

                case CallLog.Calls.MISSED_TYPE:
                    dir = "MISSED";
                    break;
            }

            db.addCall(phNumber,0,callDayTime.toString(),callDuration);

            sb.append( "\nPhone Number:--- "+phNumber +" \nCall Type:--- "+dir+" \nCall Date:--- "+callDayTime+" \nCall duration in sec :--- "+callDuration );
            sb.append("\n----------------------------------");
     //   }

        cursor.close();
     //   call.setText(sb);
    }

    private void getSMSDetails(){
        //final String[] projection = {"_id", "address", "body", "type"};

        Cursor cursor = getContentResolver().query(Uri.parse("content://sms/inbox"), null, null, null, null);
        StringBuffer msgData = new StringBuffer();

        if (cursor.moveToFirst()) { // must check the result to prevent exception
            //do {
                for(int idx=0;idx<cursor.getColumnCount();idx++)
                {
                    msgData.append("\t\t\t\t" + cursor.getColumnName(idx) +"\n"); // get all the columns

                    //msgData.append(" " + cursor.getColumnName(idx) + ":" + cursor.getString(idx));
                    //msgData.append("\n----------------------------------");
                }

          //  } while (cursor.moveToNext());
        } else {
            // empty box, no SMS
        }
        cursor.close();
        call.setText(msgData);
    }

}
