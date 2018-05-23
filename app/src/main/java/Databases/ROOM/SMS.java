package Databases.ROOM;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.text.DecimalFormat;

/**
 * Created by Larisa on 07.01.2018.
 */
@Entity(tableName = "SMS")
public class SMS {
    @PrimaryKey
    @NonNull
    private String phoneNumber;

    @ColumnInfo(name = "average_length_sms_sent")
    private double avgLengthSMSSent=0;

    @ColumnInfo(name = "average_length_sms_inbox")
    private double avgLengthSMSInbox=0;

    @ColumnInfo(name = "average_word_length_sent")
    private double avgWordLengthSent=0;

    @ColumnInfo(name = "median_word_length_sent")
    private double medianWordLengthSent=0;

    @ColumnInfo(name = "sms_with_unique_ID_inbox")
    private int uniqueIDinbox=0;

    @ColumnInfo(name = "sms_with_unique_ID_sent")
    private int uniqueIDsent=0;

    @ColumnInfo(name = "sms_sent")
    private int smsSent=0;

    public  void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setAvgLengthSMSSent(double avgSMSLength) {
        this.avgLengthSMSSent= avgSMSLength;
    }

    public void setAvgLengthSMSInbox(double avgSMSLength) {
        this.avgLengthSMSInbox= avgSMSLength;
    }

    public void setAvgWordLengthSent(double avgWordLength) {
        this.avgWordLengthSent = avgWordLength;
    }

    public void setMedianWordLengthSent(double medianWordLength) {
        this.medianWordLengthSent = medianWordLength;
    }

    public void setUniqueIDinbox(int uniqueIDNo){ this.uniqueIDinbox = uniqueIDNo; }

    public void setUniqueIDsent(int uniqueIDNo){ this.uniqueIDsent = uniqueIDNo; }

    public void setSmsSent(int smsNo){ this.smsSent = smsNo; }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public double getAvgLengthSMSSent() {
        return avgLengthSMSSent;
    }

    public double getAvgLengthSMSInbox() {
        return avgLengthSMSInbox;
    }

    public double getAvgWordLengthSent() {
        return avgWordLengthSent;
    }

    public double getMedianWordLengthSent() {
        return medianWordLengthSent;
    }

    public int getUniqueIDinbox() {
        return uniqueIDinbox;
    }

    public int getUniqueIDsent() {
        return uniqueIDsent;
    }

    public int getSmsSent() {
        return smsSent;
    }

    @Override
    public String toString(){
        String result="";
        DecimalFormat formatter = new DecimalFormat("#0.00");

       // result +="Phone number: " + phoneNumber + "\n";
        result +="Average message length inbox: " + "65.41" +"\n"; //dummy
        result +="Average message length sent: " + formatter.format(avgLengthSMSSent) +"\n";
        result +="Average word length sent: " + formatter.format(avgWordLengthSent) +"\n";
        result +="Median word length sent: " + formatter.format(medianWordLengthSent)+"\n";
        result +="Messages with unique ID inbox: " + "42.00"+"\n"; //dummy
        result +="Messages with unique ID sent: " + formatter.format(uniqueIDsent)+"\n";
        result +="Number of messages sent: " + formatter.format(smsSent)+"\n";

        return result;
    }

}