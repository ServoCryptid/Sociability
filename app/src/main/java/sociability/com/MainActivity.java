package sociability.com;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int MY_PERMISSIONS_REQUESTS = 333;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CardView cv1 = findViewById(R.id.cv1);
        cv1.setOnClickListener(this);

        CardView cv2 = findViewById(R.id.cv2);
        cv2.setOnClickListener(this);

        if( checkAndRequestPermissions()){
            //If you have already permitted the permission
            ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("We are gathering data...");

            new Helper.FetchLogs(progressDialog, this).execute();
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
        }
    }

    private boolean checkAndRequestPermissions(){
        boolean hasPermissionReadCallLog = (ContextCompat.checkSelfPermission(getApplicationContext(),
                android.Manifest.permission.READ_CALL_LOG) == PackageManager.PERMISSION_GRANTED);

        boolean hasPermissionReadSMSLog = (ContextCompat.checkSelfPermission(getApplicationContext(),
                android.Manifest.permission.READ_SMS) == PackageManager.PERMISSION_GRANTED);

        List<String> listPermissionsNeeded = new ArrayList<>();
        if (!hasPermissionReadCallLog) {
            listPermissionsNeeded.add(android.Manifest.permission.READ_CALL_LOG);
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
