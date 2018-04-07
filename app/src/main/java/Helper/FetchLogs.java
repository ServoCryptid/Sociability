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

/**
 * Created by Larisa on 03.01.2018.
 */

public class FetchLogs extends AsyncTask<Void, Void, Void> {
    //todo: create a structure for the helper methods + add them here
    private AppDatabase db;
    private ProgressDialog mProgressDialog;
    private Context mContext;
    TelephonyManager tm;

    FirebaseDB fdb = new FirebaseDB();

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

        if(db.smsDao().countPhoneNumbers()==0) // read the sms log once
             getSMSDetails();


        if(db.callDao().countPhoneNumbers()==0)
            getCallDetails();
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
            String callDate = cursor.getString( date );
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

    private void getSMSDetails(){
       // final String[] projection = {"date_sent" ,"address", "body", "type"};
        final String[] projection = {"address", "body"};

        int index;
        List<SMSSample> smsSent = new ArrayList<SMSSample>();
        Cursor cursor = mContext.getContentResolver().query(Uri.parse("content://sms/sent"),projection , null, null, null);
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
        applySMSStatistics(smsSent);

    }

    public int checkIfObjExists(List<SMSSample> list, SMSSample sample){ // check if the number is already in the list //todo: change this (duplicate code)
        int i;
        SMSSample aux;

        for(i=0;i<list.size();i++){
            aux = list.get(i);
            if(aux.equals(sample))
                return i;
        }
        return -1;
    }


    public int checkIfNumberExists(List<CallSample> list, CallSample sample){ // check if the number is already in the list //todo: change this duplicate code
        int i;
        CallSample aux;

        for(i=0;i<list.size();i++){
            aux = list.get(i);
            if(aux.equals(sample))
                return i;
        }
        return -1;
    }

    public List<SMSSample> checkForZeroMessages(List<SMSSample> list){//todo: duplicate code. change it
        List<SMSSample> newList ;
        newList = list;
        for(int i=0;i<list.size();i++){
            SMSSample temp = list.get(i);
            if(temp.smsBody.size()==0) {
                newList.remove(i);
                i--;
            }
        }

        return newList;
    }

    public List<CallSample> checkForZeroCall(List<CallSample> list){ //todo: duplicate code. change it
        List<CallSample> newList ;
        newList = list;
        for(int i=0;i<list.size();i++){
            CallSample temp = list.get(i);
            if(temp.callDurations.size()==0) {
                newList.remove(i);
                i--;
            }
        }

        return newList;
    }

    public void applySMSStatistics(List<SMSSample> list){ // changed temporary to compute the overall sms statistics (for the contest)
        SMSSample tempSMSSample;
        double overallAverageSMSLength = 0;
        double overallMedianWordLength = 0;
        double overallAverageWordLength = 0;
        HashMap<String,Double> sms_stats = new HashMap<String,Double>();

        for(int i=0 ; i<list.size();i++){
            SMS sms = new SMS();
            tempSMSSample = list.get(i);

            sms.setPhoneNumber(tempSMSSample.phoneNumber);
            sms.setAvgSMSLength(StatisticalMeasuresSMS.averageMessageLength(tempSMSSample.getSmsBody()));
            sms.setAvgWordLength(StatisticalMeasuresSMS.averageWordLength(tempSMSSample.getSmsBody()));
            sms.setMedianWordLength(StatisticalMeasuresSMS.medianWordLength(tempSMSSample.getSmsBody()));

            overallAverageSMSLength += StatisticalMeasuresSMS.averageMessageLength(tempSMSSample.getSmsBody());
            overallAverageWordLength += StatisticalMeasuresSMS.averageWordLength(tempSMSSample.getSmsBody());
            overallMedianWordLength += StatisticalMeasuresSMS.medianWordLength(tempSMSSample.getSmsBody());

            db.smsDao().insertAll(sms);
           //db.smsDao().delete(sms);
        }

        overallAverageSMSLength = overallAverageSMSLength/ list.size();
        sms_stats.put("Average SMS length",overallAverageSMSLength);

        overallAverageWordLength = overallAverageWordLength /list.size();
        sms_stats.put("Average word length",overallAverageWordLength );

        overallMedianWordLength = overallMedianWordLength/ list.size();
        sms_stats.put("Median word length",overallMedianWordLength);

        fdb.setSimSerialNumber(tm.getSimSerialNumber().toString());
        fdb.updateStatsToDB(sms_stats);
    }

    public void applyCallStatistics (List<CallSample> list){// changed temporary to compute the overall sms statistics (for the contest)
        CallSample tempCallSample;
        double overallAverageDuration = 0;
        double oversallTotalDuration = 0;

        HashMap<String,Double> call_stats = new HashMap<String,Double>();

        for(int i = 0;i<list.size();i++){
            CALL call = new CALL();
            tempCallSample = list.get(i);

            call.setPhoneNumber(tempCallSample.phoneNumber);
            call.setAvgDuration(StatisticalMeasuresCall.averageDuration(tempCallSample.getCallDurations()));
            call.setTotalDuration(StatisticalMeasuresCall.totalDuration(tempCallSample.getCallDurations()));

            overallAverageDuration += StatisticalMeasuresCall.averageDuration(tempCallSample.getCallDurations());
            oversallTotalDuration += StatisticalMeasuresCall.totalDuration(tempCallSample.getCallDurations());

            db.callDao().insertAll(call);
           // db.callDao().delete(call);
        }
        overallAverageDuration = oversallTotalDuration/list.size();

        call_stats.put("Average call duration",overallAverageDuration);
        call_stats.put("Total call duration", oversallTotalDuration);

        fdb.setSimSerialNumber(tm.getSimSerialNumber().toString());
        fdb.updateStatsToDB(call_stats);
    }

    public static String millisToDate(long currentTime) { //todo: put this in a helper class
        String finalDate;
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(currentTime);
        Date date = calendar.getTime();
        finalDate = date.toString();
        return finalDate;
    }

}
