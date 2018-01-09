package Databases.ROOM;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

/**
 * Created by Larisa on 07.01.2018.
 */
@Entity(tableName = "SMS")
public class SMS {
    @PrimaryKey
    @NonNull
    private String phoneNumber;

    @ColumnInfo(name = "average_sms_length")
    private double avgSMSLength;

    @ColumnInfo(name = "average_word_length")
    private double avgWordLength;

    @ColumnInfo(name = "median_word_length")
    private double medianWordLength;

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

}