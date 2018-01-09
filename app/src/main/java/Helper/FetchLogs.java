package Helper;

import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.CallLog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import Databases.ROOM.AppDatabase;
import Databases.ROOM.SMS;
import Entities.CallSample;
import Entities.SMSSample;
import Filters.StatisticalMeasuresSMS;

/**
 * Created by Larisa on 03.01.2018.
 */

public class FetchLogs extends AsyncTask<Void, Void, Void> {
    //todo: create a structure for the helper methods + add them here

    private ProgressDialog mProgressDialog;
    private Context mContext;

    public FetchLogs(ProgressDialog pD, Context c){
        this.mProgressDialog = pD;
        this.mContext = c;
    }

    public void onPreExecute() {
        mProgressDialog.show();
    }

    public Void doInBackground(Void... args){
       // getCallDetails();
        getSMSDetails();

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

        sb.append( "Call Details :");

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

            CallSample call = new CallSample(phNumber);
            index = checkIfNumberExists(callList,call);

            if(index > -1){
                callList.get(index).addCallDuration(callDuration);
            }
            else {
                callList.add(call);
            }

            //todo:add the call in the database


            // Log.d("CALLLOG ---->",sb.toString());
        }

        cursor.close();
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

        applyStatistics(smsSent);
/*
        for(int i=0;i<smsSent.size();i++){ //see the content of smsSent
            SMSSample s = smsSent.get(i);
            Log.d("NUMBER:",s.getPhoneNumber()+"\n");
            Log.d("SMS SENT: ",s.getSmsBody().toString()+"\n\n\n");
            Log.d("NUMBER OF CONTACTS:",smsSent.size()+"");

        }
*/
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

    public void applyStatistics(List<SMSSample> list){// todo: store in ROOM
        AppDatabase db =  AppDatabase.getAppDatabase(mContext); //get my ROOM database instance
        SMS sms = new SMS();
        SMSSample tempSMSSample;
        List<String> tempSMSList;

        for(int i=0 ; i<list.size();i++){
            tempSMSSample = list.get(i);
            tempSMSList = tempSMSSample.getSmsBody();

            sms.setPhoneNumber(tempSMSSample.phoneNumber);
            sms.setAvgSMSLength(StatisticalMeasuresSMS.averageMessageLength(tempSMSList));
            sms.setAvgWordLength(StatisticalMeasuresSMS.averageWordLength(tempSMSList));
            sms.setMedianWordLength(StatisticalMeasuresSMS.medianWordLength(tempSMSList));

          //  db.smsDao().insertAll(sms);
        }

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
