package sociability.com;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.ultramegasoft.radarchart.RadarHolder;
import com.ultramegasoft.radarchart.RadarView;

import java.util.ArrayList;
import java.util.List;

import Databases.Firebase.FirebaseDB;

import static sociability.com.FirstScreenActivity.Along;
import static sociability.com.FirstScreenActivity.Ashort;
import static sociability.com.FirstScreenActivity.Clong;
import static sociability.com.FirstScreenActivity.Cshort;
import static sociability.com.FirstScreenActivity.Elong;
import static sociability.com.FirstScreenActivity.Eshort;
import static sociability.com.FirstScreenActivity.Nlong;
import static sociability.com.FirstScreenActivity.Nshort;
import static sociability.com.FirstScreenActivity.Olong;
import static sociability.com.FirstScreenActivity.Oshort;

public class RadarChartActivity extends BaseActivity implements View.OnClickListener{
    public static RadarView mRadarView;
    private static final int MY_PERMISSIONS_REQUESTS = 333;
    public static FirebaseDB fDB;
    private ArrayList<RadarHolder> radarData = new ArrayList<RadarHolder>();

    @NonNull
    private ArrayList<RadarHolder> mData = new ArrayList<RadarHolder>() {// The data for the RadarView
        {
            add(new RadarHolder("O", 3));
            add(new RadarHolder("C", 4));
            add(new RadarHolder("E", 4));
            add(new RadarHolder("A", 4));
            add(new RadarHolder("N", 2));

        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_radar_chart);

        mRadarView = findViewById(R.id.radar);
        mRadarView.setData(mData);
        mRadarView.setMaxValue(10);
        mRadarView.setInteractive(false);

        Button b = (Button)findViewById(R.id.goTo_quiz_button);
        b.setOnClickListener(this);
        b.setBackgroundColor(getResources().getColor(R.color.colorAccent));

        Button b1 = (Button)findViewById(R.id.buttonAfterFirstQ);
        b1.setOnClickListener(this);
        b1.setBackgroundColor(getResources().getColor(R.color.colorAccent));

        Button b2 = (Button)findViewById(R.id.buttonAfterSecondQ);
        b2.setOnClickListener(this);
        b2.setBackgroundColor(getResources().getColor(R.color.colorAccent));

        if(FirstScreenActivity.agree_terms == 0){
            if (checkAndRequestPermissions()) {

                //region Set the SIM serial number as identifier for the user
                TelephonyManager tm;
                tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                fDB = new FirebaseDB();
                fDB.setSimSerialNumber(tm.getSimSerialNumber().toString());
                //endregion

                ProgressDialog progressDialog = new ProgressDialog(this);
                progressDialog.setMessage("We are gathering data...");

                new Helper.FetchLogs(progressDialog, this).execute();
            }
        }
        else{
            //region Set the SIM serial number as identifier for the user
            TelephonyManager tm;
            tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            fDB = new FirebaseDB();
            fDB.setSimSerialNumber(tm.getSimSerialNumber().toString());
            //endregion
        }


    }

    @Override
    public void onClick(View v) {
        // Perform action on click
        switch(v.getId()) {
            case R.id.goTo_quiz_button:
                //start the main activity
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                break;
            case R.id.buttonAfterFirstQ:
                //update the chart with the answers for the TIPI quiz
                radarData.clear();
                radarData.add(new RadarHolder("O",Oshort));
                radarData.add(new RadarHolder("C",Cshort));
                radarData.add(new RadarHolder("E",Eshort));
                radarData.add(new RadarHolder("A",Ashort));
                radarData.add(new RadarHolder("N",Nshort));

                mRadarView.setData(radarData);
                break;
            case R.id.buttonAfterSecondQ:
                //update the radarchart with the answers for the IPIP quiz
                radarData.clear();
                radarData.add(new RadarHolder("O",Olong));
                radarData.add(new RadarHolder("C",Clong));
                radarData.add(new RadarHolder("E",Elong));
                radarData.add(new RadarHolder("A",Along));
                radarData.add(new RadarHolder("N",Nlong));

                mRadarView.setData(radarData);
                break;
        }
    }

    private boolean checkAndRequestPermissions(){
        boolean hasPermissionReadCallLog = (ContextCompat.checkSelfPermission(getApplicationContext(),
                android.Manifest.permission.READ_CALL_LOG) == PackageManager.PERMISSION_GRANTED);

        boolean hasPermissionReadSMSLog = (ContextCompat.checkSelfPermission(getApplicationContext(),
                android.Manifest.permission.READ_SMS) == PackageManager.PERMISSION_GRANTED);

        boolean hasPermissionReadPhoneState = (ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED);

        List<String> listPermissionsNeeded = new ArrayList<>();

        if (!hasPermissionReadCallLog) {
            listPermissionsNeeded.add(android.Manifest.permission.READ_CALL_LOG);
        }

        if (!hasPermissionReadSMSLog) {
            listPermissionsNeeded.add(android.Manifest.permission.READ_SMS);
        }

        if (!hasPermissionReadPhoneState) {
            listPermissionsNeeded.add(Manifest.permission.READ_PHONE_STATE);
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
    public void onBackPressed() { } //disable the back button for this activity
}
