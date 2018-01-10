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

    @ColumnInfo(name = "average_sms_length")
    private double avgSMSLength=0;

    @ColumnInfo(name = "average_word_length")
    private double avgWordLength=0;

    @ColumnInfo(name = "median_word_length")
    private double medianWordLength=0;

    public  void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setAvgSMSLength(double avgSMSLength) {
        this.avgSMSLength = avgSMSLength;
    }

    public void setAvgWordLength(double avgWordLength) {
        this.avgWordLength = avgWordLength;
    }

    public void setMedianWordLength(double medianWordLength) {
        this.medianWordLength = medianWordLength;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public double getAvgSMSLength() {
        return avgSMSLength;
    }

    public double getAvgWordLength() {
        return avgWordLength;
    }

    public double getMedianWordLength() {
        return medianWordLength;
    }

    @Override
    public String toString(){
        String result="";
        DecimalFormat formatter = new DecimalFormat("#0.00");

        result +="Phone number: " + phoneNumber + "\n";
        result +="Average message length: " + formatter.format(avgSMSLength) +"\n";
        result +="Average word length: " + formatter.format(avgWordLength) +"\n";
        result +="Median word length: " + formatter.format(medianWordLength)+"\n";

        return result;
    }

}