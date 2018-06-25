package Helper;

import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.CallLog;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Databases.ROOM.CALL;
import Databases.ROOM.SMS;
import Entities.CallSample;
import Entities.SMSSample;
import Filters.StatisticalMeasuresCall;
import Filters.StatisticalMeasuresSMS;
import sociability.com.FirstScreenActivity;

import static Helper.Utils.checkForZeroCall;
import static Helper.Utils.checkForZeroMessages;
import static Helper.Utils.checkIfNumberExists;
import static Helper.Utils.checkIfObjExists;
import static sociability.com.FirstScreenActivity.db;
import static sociability.com.RadarChartActivity.fDB;


/**
 * Created by Larisa on 03.01.2018.
 */

public class FetchLogs extends AsyncTask<Void, Void, Void> {
    private ProgressDialog mProgressDialog;
    private Context mContext;
    TelephonyManager tm;
    private HashMap<String, String> sms_stats = new HashMap<String, String>();
    private HashMap<String, String> call_stats = new HashMap<String, String>();
    private double callLogHours = 0, smsLogHours = 0;
    private String hour="";
    private Map<String, Integer> mapActionsReceived = new HashMap<String,Integer>(); // create the dictionary for the incoming calls/ SMS inbox periods
    private Map<String, Integer> mapActionsMade = new HashMap<String,Integer>(); // create the dictionary for the outgoing calls /SMS sent periods


    public FetchLogs(ProgressDialog pD, Context c) {
        this.mProgressDialog = pD;
        this.mContext = c;
    }

    public void onPreExecute() {
        mProgressDialog.show();
    }

    public Void doInBackground(Void... args) {
        tm = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);


        // read the sms log
        applySMSStatistics(getSMSDetails("content://sms/sent"), "sent");
        applySMSStatistics(getSMSDetails("content://sms/inbox"), "inbox");
        fDB.updateStatsToDB(sms_stats);


        // read the call log
        applyCallStatistics(getCallDetails());
        fDB.updateStatsToDB(call_stats);


        return null;
    }

    protected void onPostExecute(Void result) {
        if (mProgressDialog.isShowing())
            mProgressDialog.dismiss();

        FirstScreenActivity.agree_terms = 1; // the user agreed to the terms and conditions
        FirstScreenActivity.prefsUser.edit().putInt("agree_terms", 1).apply();
    }

    private List<CallSample> getCallDetails() {
        List<CallSample> callList = new ArrayList<CallSample>();
        String [] projection = null, selectionArgs = null;
        String selection = null, sortOrder = null;
        StringBuffer sb = new StringBuffer();
        String callDate = "";
        long firstCallDate = 0, lastCallDate = 0;
        Cursor cursor = mContext.getContentResolver().query(
                CallLog.Calls.CONTENT_URI, // The content URI of the call table
                projection, // The columns to return for each row
                selection, // Selection criteria
                selectionArgs, // Selection criteria
                sortOrder); // The sort order for the returned rows


        //set the periods of time as keys
        mapActionsReceived.put("morning", 0);
        mapActionsReceived.put("afternoon", 0);
        mapActionsReceived.put("evening", 0);
        mapActionsReceived.put("night", 0);

        mapActionsMade.put("morning", 0);
        mapActionsMade.put("afternoon", 0);
        mapActionsMade.put("evening", 0);
        mapActionsMade.put("night", 0);

        int number = cursor.getColumnIndex(CallLog.Calls.NUMBER);
        int type = cursor.getColumnIndex(CallLog.Calls.TYPE);
        int date = cursor.getColumnIndex(CallLog.Calls.DATE);
        int duration = cursor.getColumnIndex(CallLog.Calls.DURATION);

        while (cursor.moveToNext()) {
            String phNumber = cursor.getString(number);
            String callType = cursor.getString(type);
            callDate = cursor.getString(date);
            String callDuration = cursor.getString(duration);
            String dir ;
            int index;

            hour = Utils.millisToDate(Long.parseLong(callDate)).substring(11,13);
            int dircode = Integer.parseInt(callType);

            switch (dircode) {
                case CallLog.Calls.OUTGOING_TYPE:
                    dir = "outgoing";

                    if(Utils.isBetween(Integer.parseInt(hour),6,12)) { //check if the hour is in the morning
                        mapActionsMade.put("morning", mapActionsMade.get("morning") + 1); //update the number of incoming calls from the morning
                    }
                    else if(Utils.isBetween(Integer.parseInt(hour),12,18)) { //check if the hour is in the afternoon
                        mapActionsMade.put("afternoon", mapActionsMade.get("afternoon") + 1); //update the number of incoming calls from the afternoon
                    }
                    else if (Utils.isBetween(Integer.parseInt(hour),18,22)) { //check if the hour is in the evening
                        mapActionsMade.put("evening", mapActionsMade.get("evening") + 1); //update the number of incoming calls from the evening
                    }
                    else if (Utils.isBetween(Integer.parseInt(hour),22,24)|| Utils.isBetween(Integer.parseInt(hour),0,6)) { //check if the hour is in the night
                        mapActionsMade.put("night", mapActionsMade.get("night") + 1); //update the number of incoming calls from the night
                    }
                    break;

                case CallLog.Calls.INCOMING_TYPE:
                    dir = "incoming";

                    if(Utils.isBetween(Integer.parseInt(hour),6,12)) { //check if the hour is in the morning
                        mapActionsReceived.put("morning", mapActionsReceived.get("morning") + 1); //update the number of incoming calls from the morning
                    }
                    else if(Utils.isBetween(Integer.parseInt(hour),12,18)) { //check if the hour is in the afternoon
                        mapActionsReceived.put("afternoon", mapActionsReceived.get("afternoon") + 1); //update the number of incoming calls from the afternoon
                    }
                    else if (Utils.isBetween(Integer.parseInt(hour),18,22)) { //check if the hour is in the evening
                        mapActionsReceived.put("evening", mapActionsReceived.get("evening") + 1); //update the number of incoming calls from the evening
                    }
                    else if (Utils.isBetween(Integer.parseInt(hour),22,24)|| Utils.isBetween(Integer.parseInt(hour),0,6)) { //check if the hour is in the night
                        mapActionsReceived.put("night", mapActionsReceived.get("night") + 1); //update the number of incoming calls from the night
                    }
                    break;

                case CallLog.Calls.MISSED_TYPE:
                    dir = "missed";
                    break;
                default:
                    dir = "irrelevant"; // we are not interested in other types of calls
                    break;
            }

            if (!dir.equals("irrelevant")) {
                if (phNumber.contains("+4"))
                    phNumber = phNumber.substring(2);
                CallSample call = new CallSample(phNumber);
                index = checkIfNumberExists(callList, call);

                if(callList.isEmpty()){ //get the date for the first call in log
                    firstCallDate = Long.parseLong(callDate);
                }
                if (index > -1) {
                    callList.get(index).addCallDuration(callDuration, dir);
                } else {

                    callList.add(call);
                }
            }
        }
        cursor.close();
        lastCallDate = Long.parseLong(callDate); //get the date of the last call in log

        callLogHours = (lastCallDate - firstCallDate)/3600000;

        callList = checkForZeroCall(callList);

        return callList;
    }

    private List<SMSSample> getSMSDetails(String path) {
        final String[] projection = {"date_sent","address", "body"};

        int index;
        List<SMSSample> smsSent = new ArrayList<SMSSample>();
        Cursor cursor = mContext.getContentResolver().query(Uri.parse(path), projection, null, null, null);
        String phoneNumber = "";
        String smsBody = "";

        //reinitialize the 2 dictionaries
        if(path.contains("inbox")) {
            mapActionsReceived.put("morning", 0);
            mapActionsReceived.put("afternoon", 0);
            mapActionsReceived.put("evening", 0);
            mapActionsReceived.put("night", 0);
        }
        else {
            mapActionsMade.put("morning", 0);
            mapActionsMade.put("afternoon", 0);
            mapActionsMade.put("evening", 0);
            mapActionsMade.put("night", 0);
        }

        if (cursor.moveToFirst()) { // must check the result to prevent exception
            do {
                for (int idx = 0; idx < cursor.getColumnCount(); idx++) {
                    if (cursor.getColumnName(idx).equals("date_sent")) {
                        String dateInMills = cursor.getString(idx);

                        hour = Utils.millisToDate(Long.parseLong(dateInMills)).substring(11,13);

                        Log.d("DATAA--->",Utils.millisToDate(Long.parseLong(dateInMills)));

                        if(path.contains("sent")){
                            if(Utils.isBetween(Integer.parseInt(hour),6,12)) { //check if the hour is in the morning
                                mapActionsMade.put("morning", mapActionsMade.get("morning") + 1); //update the number of incoming calls from the morning
                            }
                            else if(Utils.isBetween(Integer.parseInt(hour),12,18)) { //check if the hour is in the afternoon
                                mapActionsMade.put("afternoon", mapActionsMade.get("afternoon") + 1); //update the number of incoming calls from the afternoon
                            }
                            else if (Utils.isBetween(Integer.parseInt(hour),18,22)) { //check if the hour is in the evening
                                mapActionsMade.put("evening", mapActionsMade.get("evening") + 1); //update the number of incoming calls from the evening
                            }
                            else if (Utils.isBetween(Integer.parseInt(hour),22,24) || Utils.isBetween(Integer.parseInt(hour),0,6) ) { //check if the hour is in the night
                                mapActionsMade.put("night", mapActionsMade.get("night") + 1); //update the number of incoming calls from the night
                            }

                        }
                        else{
                            if(Utils.isBetween(Integer.parseInt(hour),6,12)) { //check if the hour is in the morning
                                mapActionsReceived.put("morning", mapActionsReceived.get("morning") + 1); //update the number of incoming calls from the morning
                            }
                            else if(Utils.isBetween(Integer.parseInt(hour),12,18)) { //check if the hour is in the afternoon
                                mapActionsReceived.put("afternoon", mapActionsReceived.get("afternoon") + 1); //update the number of incoming calls from the afternoon
                            }
                            else if (Utils.isBetween(Integer.parseInt(hour),18,22)) { //check if the hour is in the evening
                                mapActionsReceived.put("evening", mapActionsReceived.get("evening") + 1); //update the number of incoming calls from the evening
                            }
                            else if (Utils.isBetween(Integer.parseInt(hour),22,24)|| Utils.isBetween(Integer.parseInt(hour),0,6)) { //check if the hour is in the night
                                mapActionsReceived.put("night", mapActionsReceived.get("night") + 1); //update the number of incoming calls from the night
                            }
                        }

                    } else {
                        if (cursor.getColumnName(idx).equals("address")) {
                            phoneNumber = cursor.getString(idx);
                            if (phoneNumber.contains("+4"))// remove the country prefix
                                phoneNumber = phoneNumber.substring(2);
                        } else if (cursor.getColumnName(idx).equals("body"))
                            smsBody = cursor.getString(idx);
                    }
                }

                SMSSample temp = new SMSSample(phoneNumber);
                index = checkIfObjExists(smsSent, temp);

                if (index > -1) {
                    smsSent.get(index).addSMS(smsBody);
                } else {
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


    public void applySMSStatistics(List<SMSSample> list, String type) {
        SMSSample tempSMSSample;
        double overallAverageSMSLength = 0;
        double overallMedianWordLength = 0;
        double overallAverageWordLength = 0;
        int noOfSMS = 0;

        DecimalFormat formatter = new DecimalFormat("#0.00");
        SMS sms = new SMS();
        for (int i = 0; i < list.size(); i++) {

            tempSMSSample = list.get(i);

            sms.setPhoneNumber(tempSMSSample.phoneNumber);

            if (type.equals("sent")) {
                overallAverageWordLength += StatisticalMeasuresSMS.averageWordLength(tempSMSSample.getSmsBody());
                overallMedianWordLength += StatisticalMeasuresSMS.medianWordLength(tempSMSSample.getSmsBody());
                overallAverageSMSLength += StatisticalMeasuresSMS.averageMessageLength(tempSMSSample.getSmsBody());
            }

            if (type.equals("inbox")) { //for the inbox messages we need only the average SMS length
                overallAverageSMSLength += StatisticalMeasuresSMS.averageMessageLength(tempSMSSample.getSmsBody());
            }
            noOfSMS += tempSMSSample.smsBody.size();
        }

        if (overallAverageSMSLength != 0) {
            overallAverageSMSLength = overallAverageSMSLength / list.size();

            if(type.equals("sent")){
                sms.setAvgLengthSMSSent(overallAverageSMSLength);
            }
            else
                sms.setAvgLengthSMSInbox(overallAverageSMSLength);

            sms_stats.put("Average SMS length " + type, formatter.format(overallAverageSMSLength));
        }

        if (overallAverageWordLength != 0) {
            overallAverageWordLength = overallAverageWordLength / list.size();

            sms.setAvgWordLengthSent(overallAverageWordLength);
            sms_stats.put("Average word length " + type, formatter.format(overallAverageWordLength));
        }

        if (overallMedianWordLength != 0) {
            overallMedianWordLength = overallMedianWordLength / list.size();
            sms.setMedianWordLengthSent(overallMedianWordLength);
            sms_stats.put("Median word length " + type, formatter.format(overallMedianWordLength));
        }

        if(type.equals("sent")){
            sms.setUniqueIDsent(list.size());
        }
        else
            sms.setUniqueIDinbox(list.size());

        sms_stats.put("Messages with unique ID " + type, formatter.format(list.size()));

        if (type.equals("sent")) {
            sms.setSmsSent(noOfSMS);
            sms.setMostSentSMS(getKeyForMaxValue(mapActionsMade));
        }
        else{
            sms.setSmsInbox(noOfSMS);
            sms.setMostReceivedSMS(getKeyForMaxValue(mapActionsReceived));
        }
        sms_stats.put("Number of messages " + type, formatter.format( noOfSMS));

        if(type.equals("sent")){
            sms_stats.put("Most SMS sent in the ", getKeyForMaxValue(mapActionsMade));
        }
        else{
            sms_stats.put("Most SMS received in the ", getKeyForMaxValue(mapActionsReceived));
        }

        if(type.equals("sent")) {
            db.smsDao().insertAll(sms);
        }
        else {
            db.smsDao().updateAvgSMSLengthI(sms.getUniqueIDinbox(), sms.getAvgLengthSMSInbox());
            db.smsDao().updateSMSI(sms.getSmsInbox(), sms.getSmsInbox());
            db.smsDao().updateMostReceived(sms.getSmsInbox(), sms.getMostReceivedSMS());
        }
        //db.smsDao().delete(sms);
    }

    public void applyCallStatistics(List<CallSample> list) {
        CallSample tempCallSample;
        double overallAverageDurationI = 0;
        double overallAverageDurationO = 0;

        double overallTotalDurationI = 0;
        double overallTotalDurationO = 0;
        int missedCalls = 0;
        int uniqueContactsM = 0; //unique contacts for missed calls
        int outgoingCalls = 0;
        int uniqueContactsO = 0;
        int incomingCalls = 0;
        int uniqueContactsI = 0;

        CALL call = new CALL();

        DecimalFormat formatter = new DecimalFormat("#0.00");

        for (int i = 0; i < list.size(); i++) {
            tempCallSample = list.get(i);

            call.setPhoneNumber(tempCallSample.phoneNumber);

            StatisticalMeasuresCall.averageDuration(tempCallSample.getCallDurations("incoming"));
            StatisticalMeasuresCall.averageDuration(tempCallSample.getCallDurations("outgoing"));

            call.setTotalDurationI(StatisticalMeasuresCall.totalDuration(tempCallSample.getCallDurations("incoming")));
            call.setTotalDurationI(StatisticalMeasuresCall.totalDuration(tempCallSample.getCallDurations("outgoing")));

            overallAverageDurationI += StatisticalMeasuresCall.averageDuration(tempCallSample.getCallDurations("incoming"));
            overallAverageDurationO += StatisticalMeasuresCall.averageDuration(tempCallSample.getCallDurations("outgoing"));

            overallTotalDurationI += StatisticalMeasuresCall.totalDuration(tempCallSample.getCallDurations("incoming"));
            overallTotalDurationO += StatisticalMeasuresCall.totalDuration(tempCallSample.getCallDurations("outgoing"));

            if (tempCallSample.outgoingCallDurations.size() > 0) {
                outgoingCalls += tempCallSample.outgoingCallDurations.size();
                uniqueContactsO++;
            }
            if (tempCallSample.incomingCallDurations.size() > 0) {
                incomingCalls += tempCallSample.incomingCallDurations.size();
                uniqueContactsI++;
            }
            if (tempCallSample.missedCalls != 0) {
                missedCalls += tempCallSample.missedCalls;
                uniqueContactsM++;
            }

        }
        overallAverageDurationI = overallTotalDurationI / incomingCalls;
        overallAverageDurationO = overallTotalDurationO / outgoingCalls;

        call.setAvgDurationI(overallAverageDurationI);
        call_stats.put("Average INCOMING call duration(h)", formatter.format(overallAverageDurationI));

        call.setTotalDurationI(overallTotalDurationI);
        call_stats.put("Total INCOMING call duration(h)", formatter.format(overallTotalDurationI));

        call.setCallsI(incomingCalls);
        call_stats.put("INCOMING calls no", formatter.format(incomingCalls));

        call.setUniqueContactsI(uniqueContactsI);
        call_stats.put("Unique contacts INCOMING calls", formatter.format(uniqueContactsI));

        call.setAvgDurationO(overallAverageDurationO);
        call_stats.put("Average OUTGOING call duration(h)", formatter.format(overallAverageDurationO));

        call.setTotalDurationO(overallTotalDurationO);
        call_stats.put("Total OUTGOING call duration(h)", formatter.format(overallTotalDurationO));

        call.setCallsO(outgoingCalls);
        call_stats.put("OUTGOING calls no", formatter.format(outgoingCalls));

        call.setUniqueContactsO(uniqueContactsO);
        call_stats.put("Unique contacts OUTGOING calls", formatter.format(uniqueContactsO));

        call.setAverageCallDuration((overallTotalDurationI + overallTotalDurationO) / (incomingCalls + outgoingCalls));
        call_stats.put("Average I + O call duration(h)", formatter.format((overallTotalDurationI + overallTotalDurationO) / (incomingCalls + outgoingCalls)));

        call.setTotalCallDuration(overallTotalDurationI + overallTotalDurationO);
        call_stats.put("Total I + O call duration(h)", formatter.format(overallTotalDurationI + overallTotalDurationO));

        call.setMissedCalls(missedCalls);
        call_stats.put("Missed calls no", formatter.format(missedCalls));

        call.setUniqueContacts(uniqueContactsM);
        call_stats.put("Unique contacts MISSED calls", formatter.format(uniqueContactsM));

        call.setCall_log_hours(callLogHours);
        call_stats.put("Call log (h)", formatter.format(callLogHours));

        call.setMostIncomingCalls(getKeyForMaxValue(mapActionsReceived));
        call_stats.put("Most INCOMING calls made in the ", getKeyForMaxValue(mapActionsReceived));

        call.setMostOutgoingCalls(getKeyForMaxValue(mapActionsMade));
        call_stats.put("Most OUTGOING calls made in the ", getKeyForMaxValue(mapActionsMade));

        db.callDao().insertAll(call);
        // db.callDao().delete(call);
    }

    private String getKeyForMaxValue(Map<String,Integer> map){
        String key = "";
        int value = 0;
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
           if(entry.getValue() > value){
               value = entry.getValue();
               key = entry.getKey();
           }
        }
        return key;
    }
}
