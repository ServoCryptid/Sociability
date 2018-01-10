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

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView call;
    private StringBuffer notificationMsg;
    private static final int MY_PERMISSIONS_REQUESTS = 333;

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

           if( checkAndRequestPermissions()){
            //If you have already permitted the permission
               ProgressDialog progressDialog = new ProgressDialog(this);
               progressDialog.setMessage("We are gathering data...");

               new Helper.FetchLogs(progressDialog, this).execute();
        }

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

    private boolean checkAndRequestPermissions(){
        boolean hasPermissionReadCallLog = (ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.READ_CALL_LOG) == PackageManager.PERMISSION_GRANTED);

        boolean hasPermissionReadSMSLog = (ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.READ_SMS) == PackageManager.PERMISSION_GRANTED);

        List<String> listPermissionsNeeded = new ArrayList<>();
        if (!hasPermissionReadCallLog) {
            listPermissionsNeeded.add(Manifest.permission.READ_CALL_LOG);
        }

        if (!hasPermissionReadSMSLog) {
            listPermissionsNeeded.add(Manifest.permission.READ_SMS);
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

    @Override
    protected void onDestroy(){
        // call the superclass method first
        super.onDestroy();

    }

}
