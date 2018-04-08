package Helper;

import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.CallLog;
import android.telephony.TelephonyManager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import Databases.Firebase.FirebaseDB;
import Databases.ROOM.AppDatabase;
import Databases.ROOM.CALL;
import Databases.ROOM.SMS;
import Entities.CallSample;
import Entities.SMSSample;
import Filters.StatisticalMeasuresCall;
import Filters.StatisticalMeasuresSMS;

import static Helper.Utils.checkForZeroCall;
import static Helper.Utils.checkForZeroMessages;
import static Helper.Utils.checkIfNumberExists;
import static Helper.Utils.checkIfObjExists;
import static sociability.com.MainActivity.fDB;


/**
 * Created by Larisa on 03.01.2018.
 */

public class FetchLogs extends AsyncTask<Void, Void, Void> {

    private AppDatabase db;
    private ProgressDialog mProgressDialog;
    private Context mContext;
    TelephonyManager tm;
    private HashMap<String,Double> sms_stats = new HashMap<String,Double>();
    private HashMap<String,Double> call_stats = new HashMap<String,Double>();

    public FetchLogs(ProgressDialog pD, Context c){
        this.mProgressDialog = pD;
        this.mContext = c;
    }

    public void onPreExecute() {
        mProgressDialog.show();
    }

    public Void doInBackground(Void... args){
        tm = (TelephonyManager)mContext.getSystemService(Context.TELEPHONY_SERVICE);

        db =  AppDatabase.getAppDatabase(mContext); //get my ROOM database instance //todo: see where you should close the db

        if(db.smsDao().countPhoneNumbers()== 0) { // read the sms log
            applySMSStatistics(getSMSDetails("content://sms/sent"),"sent");
            applySMSStatistics(getSMSDetails("content://sms/inbox"), "inbox");
            fDB.updateStatsToDB(sms_stats);

        }

        if(db.callDao().countPhoneNumbers()== 0) {
            getCallDetails();
            fDB.updateStatsToDB(call_stats);

        }
        return null;
    }

    protected void onPostExecute(Void result) {
        if (mProgressDialog.isShowing())
            mProgressDialog.dismiss();
    }

    private void getCallDetails() {
        List<CallSample> callList = new ArrayList<CallSample>();
        StringBuffer sb = new StringBuffer();
        Cursor cursor = mContext.getContentResolver().query( CallLog.Calls.CONTENT_URI,null, null,null, null);

        int number = cursor.getColumnIndex( CallLog.Calls.NUMBER );
        int type = cursor.getColumnIndex( CallLog.Calls.TYPE );
        int date = cursor.getColumnIndex( CallLog.Calls.DATE);
        int duration = cursor.getColumnIndex( CallLog.Calls.DURATION);


        while ( cursor.moveToNext() ) {
            String phNumber = cursor.getString( number );
            String callType = cursor.getString( type );
            String callDate = cursor.getString( date ); // you have to apply millistoDate from Utils class
            Date callDayTime = new Date(Long.valueOf(callDate));
            String callDuration = cursor.getString( duration );
            String dir = null;
            int index;

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

            if(phNumber.contains("+4"))
                phNumber = phNumber.substring(2);
            CallSample call = new CallSample(phNumber);
            index = checkIfNumberExists(callList,call);

            if(index > -1){
                callList.get(index).addCallDuration(callDuration);
            }
            else {
                callList.add(call);
            }
        }
        cursor.close();

        callList = checkForZeroCall(callList);
        applyCallStatistics(callList);
    }

    private  List<SMSSample> getSMSDetails(String path){
       // final String[] projection = {"date_sent" ,"address", "body", "type"};
        final String[] projection = {"address", "body"};

        int index;
        List<SMSSample> smsSent = new ArrayList<SMSSample>();
        Cursor cursor = mContext.getContentResolver().query(Uri.parse(path),projection , null, null, null);
        String phoneNumber="";
        String smsBody="";

        if (cursor.moveToFirst()) { // must check the result to prevent exception
            do {
                for(int idx=0;idx<cursor.getColumnCount();idx++)
                {
                    if(cursor.getColumnName(idx).equals("date_sent")){
                        String dateInMills = cursor.getString(idx);
                    }
                    else {
                        if(cursor.getColumnName(idx).equals("address")) {
                            phoneNumber = cursor.getString(idx);
                            if(phoneNumber.contains("+4"))// remove the country prefix
                                phoneNumber = phoneNumber.substring(2);
                        }
                        else if(cursor.getColumnName(idx).equals("body"))
                             smsBody = cursor.getString(idx);
                    }
                 }

                 SMSSample temp = new SMSSample(phoneNumber);
                 index = checkIfObjExists(smsSent,temp);

                if(index > -1){
                    smsSent.get(index).addSMS(smsBody);
                }
                else {
                    smsSent.add(temp);
                }


            } while (cursor.moveToNext());
        } else {
            // empty box, no SMS
        }
        cursor.close();

        smsSent = checkForZeroMessages(smsSent); //in order to delete the records with numbers and no messages

        return smsSent;
    }


    public void applySMSStatistics(List<SMSSample> list, String type){
        SMSSample tempSMSSample;
        double overallAverageSMSLength = 0;
        double overallMedianWordLength = 0;
        double overallAverageWordLength = 0;
        int noOfSMS = 0;

        for(int i=0 ; i<list.size();i++){
            SMS sms = new SMS();
            tempSMSSample = list.get(i);

            sms.setPhoneNumber(tempSMSSample.phoneNumber);

            if(type.equals("sent")){
                sms.setAvgWordLengthSent(StatisticalMeasuresSMS.averageWordLength(tempSMSSample.getSmsBody()));
                overallAverageWordLength += StatisticalMeasuresSMS.averageWordLength(tempSMSSample.getSmsBody());

                sms.setMedianWordLengthSent(StatisticalMeasuresSMS.medianWordLength(tempSMSSample.getSmsBody()));
                overallMedianWordLength += StatisticalMeasuresSMS.medianWordLength(tempSMSSample.getSmsBody());

                sms.setAvgLengthSMSSent(StatisticalMeasuresSMS.averageMessageLength(tempSMSSample.getSmsBody()));
                overallAverageSMSLength += StatisticalMeasuresSMS.averageMessageLength(tempSMSSample.getSmsBody());


                noOfSMS += tempSMSSample.smsBody.size();
            }

            if(type.equals("inbox")) { //for the inbox messages we need only the average SMS length
                sms.setAvgLengthSMSInbox(StatisticalMeasuresSMS.averageMessageLength(tempSMSSample.getSmsBody()));
                overallAverageSMSLength += StatisticalMeasuresSMS.averageMessageLength(tempSMSSample.getSmsBody());

            }

           // db.smsDao().insertAll(sms); //TODO: what do we insert in ROOM DB ?
           db.smsDao().delete(sms);
        }

        if(overallAverageSMSLength != 0) {
            overallAverageSMSLength = overallAverageSMSLength / list.size();
            sms_stats.put("Average SMS length " + type, overallAverageSMSLength);
        }

        if(overallAverageWordLength != 0) {
            overallAverageWordLength = overallAverageWordLength / list.size();
            sms_stats.put("Average word length " + type, overallAverageWordLength);
        }

        if(overallMedianWordLength != 0 ) {
            overallMedianWordLength = overallMedianWordLength / list.size();
            sms_stats.put("Median word length " + type, overallMedianWordLength);
        }

        sms_stats.put("Messages with unique ID " + type, (double)list.size()); //TODO: e okay asa?

        if(type.equals("sent"))
            sms_stats.put("Number of messages " + type, (double)noOfSMS);

    }

    public void applyCallStatistics (List<CallSample> list){// changed temporary to compute the overall sms statistics (for the contest)
        CallSample tempCallSample;
        double overallAverageDuration = 0;
        double oversallTotalDuration = 0;

        for(int i = 0;i<list.size();i++){
            CALL call = new CALL();
            tempCallSample = list.get(i);

            call.setPhoneNumber(tempCallSample.phoneNumber);
            call.setAvgDuration(StatisticalMeasuresCall.averageDuration(tempCallSample.getCallDurations()));
            call.setTotalDuration(StatisticalMeasuresCall.totalDuration(tempCallSample.getCallDurations()));

            overallAverageDuration += StatisticalMeasuresCall.averageDuration(tempCallSample.getCallDurations());
            oversallTotalDuration += StatisticalMeasuresCall.totalDuration(tempCallSample.getCallDurations());

           // db.callDao().insertAll(call);
           // db.callDao().delete(call);
        }
        overallAverageDuration = oversallTotalDuration/list.size();

        call_stats.put("Average call duration",overallAverageDuration);
        call_stats.put("Total call duration", oversallTotalDuration);

    }
}
