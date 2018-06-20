package Databases.ROOM;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.text.DecimalFormat;

/**
 * Created by Larisa on 09.01.2018.
 */
@Entity(tableName = "call")
public class CALL {
    @PrimaryKey
    @NonNull
    private String phoneNumber;

    @ColumnInfo(name = "average_call_duration_I")
    private double avgDurationI;

    @ColumnInfo(name = "total_call_duration_I")
    private double totalDurationI;

    @ColumnInfo(name = "calls_I")
    private double callsI;

    @ColumnInfo(name = "calls_log_hours")
    private double call_log_hours;

    @ColumnInfo(name = "unique_contacts_I")
    private double uniqueContactsI;

    @ColumnInfo(name = "average_call_duration_O")
    private double avgDurationO;

    @ColumnInfo(name = "total_call_duration_O")
    private double totalDurationO;

    @ColumnInfo(name = "calls_O")
    private double callsO;

    @ColumnInfo(name = "unique_contacts_O")
    private double uniqueContactsO;

    @ColumnInfo(name = "average_call_duration") //I+O
    private double averageCallDuration;

    @ColumnInfo(name = "total_call_duration") //I+O
    private double totalCallDuration;

    @ColumnInfo(name = "missed_calls")
    private double missedCalls;

    @ColumnInfo(name = "unique_contacts") //for missed calls
    private double uniqueContacts;

    @ColumnInfo(name = "most_incoming_calls")
    private String mostIncomingCalls;

    @ColumnInfo(name = "most_outgoing_calls")
    private String mostOutgoingCalls;

    public double getCallsI() {
        return callsI;
    }

    public void setCallsI(double callsI) {
        this.callsI = callsI;
    }

    public double getUniqueContactsI() {
        return uniqueContactsI;
    }

    public void setUniqueContactsI(double uniqueContactsI) {
        this.uniqueContactsI = uniqueContactsI;
    }


    public double getCallsO() {
        return callsO;
    }

    public void setCallsO(double callsO) {
        this.callsO = callsO;
    }

    public double getUniqueContactsO() {
        return uniqueContactsO;
    }

    public void setUniqueContactsO(double uniqueContactsO) {
        this.uniqueContactsO = uniqueContactsO;
    }

    public double getAverageCallDuration() {
        return averageCallDuration;
    }

    public void setAverageCallDuration(double averageCallDuration) {
        this.averageCallDuration = averageCallDuration;
    }

    public double getTotalCallDuration() {
        return totalCallDuration;
    }

    public void setTotalCallDuration(double totalCallDuration) {
        this.totalCallDuration = totalCallDuration;
    }

    public double getMissedCalls() {
        return missedCalls;
    }

    public void setMissedCalls(double missedCalls) {
        this.missedCalls = missedCalls;
    }

    public double getUniqueContacts() {
        return uniqueContacts;
    }

    public void setUniqueContacts(double uniqueContacts) {
        this.uniqueContacts = uniqueContacts;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public double getAvgDurationI() {
        return avgDurationI;
    }

    public void setAvgDurationI(double avgDuration) {
        this.avgDurationI = avgDuration;
    }

    public double getAvgDurationO() {
        return avgDurationO;
    }

    public void setAvgDurationO(double avgDuration) {
        this.avgDurationO = avgDuration;
    }

    public double getTotalDurationI() {
        return totalDurationI;
    }

    public void setTotalDurationI(double totalDuration) {
        this.totalDurationI = totalDuration;
    }

    public double getTotalDurationO() {
        return totalDurationO;
    }

    public void setTotalDurationO(double totalDuration) {
        this.totalDurationO = totalDuration;
    }

    public double getCall_log_hours() {
        return call_log_hours;
    }

    public void setCall_log_hours(double call_log_hours) {
        this.call_log_hours = call_log_hours;
    }

    public String getMostIncomingCalls() {
        return mostIncomingCalls;
    }

    public void setMostIncomingCalls(String mostIncomingCalls) {
        this.mostIncomingCalls = mostIncomingCalls;
    }

    public String getMostOutgoingCalls() {
        return mostOutgoingCalls;
    }

    public void setMostOutgoingCalls(String mostOutgoingCalls) {
        this.mostOutgoingCalls = mostOutgoingCalls;
    }


    @Override
    public String toString() {
        String result = "";
        DecimalFormat formatter = new DecimalFormat("#0.00");

       // result += "Phone number: " + phoneNumber + "\n";
        result += "Average INCOMING call duration: " + formatter.format(avgDurationI) + " mins\n";
        result += "Total INCOMING call duration: " + formatter.format(totalDurationI) + " mins\n";
        result += "INCOMING calls: " + formatter.format(callsI) + "\n";
        result += "Unique contacts INCOMING calls: " + formatter.format(uniqueContactsI) + " \n";

        result += "Average OUTGOING call duration: " + formatter.format(avgDurationO) + " mins\n";
        result += "Total OUTGOING call duration: " + formatter.format(totalDurationO) + " mins\n";
        result += "OUTGOING calls: " + formatter.format(callsO) + " \n";
        result += "Unique contacts OUTGOING calls: " + formatter.format(totalDurationI) + " \n";

        result += "Average I + O call duration: " + formatter.format(averageCallDuration) + " mins\n";
        result += "Total I + O call duration: " + formatter.format(totalCallDuration) + " mins\n";

        result += "Missed calls: " + formatter.format(missedCalls) + " \n";
        result += "Unique contacts MISSED calls: " + formatter.format(uniqueContacts) + " \n";

        result += "Call log hours: " + formatter.format(call_log_hours) + " \n";

        result += "Most INCOMING calls made in the: " + mostIncomingCalls + " \n";
        result += "Most OUTGOING calls made in the: " + mostOutgoingCalls + " \n";


        return result;
    }
}
