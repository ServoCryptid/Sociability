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

    @ColumnInfo(name = "average_call_duration")
    private double avgDuration;

    @ColumnInfo(name = "total_call_duration")
    private double totalDuration;

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public double getAvgDuration() {
        return avgDuration;
    }

    public void setAvgDuration(double avgDuration) {
        this.avgDuration = avgDuration;
    }

    public double getTotalDuration() {
        return totalDuration;
    }

    public void setTotalDuration(double totalDuration) {
        this.totalDuration = totalDuration;
    }

    @Override
    public String toString(){
        String result="";
        DecimalFormat formatter = new DecimalFormat("#0.00");

        result +="Phone number: " + phoneNumber + "\n";
        result +="Average call duration: " + formatter.format(avgDuration) +" mins\n";
        result +="Total call duration: " + formatter.format(totalDuration) +" mins\n";

        return result;
    }
}
