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

    @ColumnInfo(name = "average_call_duration_O")
    private double avgDurationO;

    @ColumnInfo(name = "total_call_duration_I")
    private double totalDurationI;

    @ColumnInfo(name = "total_call_duration_O")
    private double totalDurationO;

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

    @Override
    public String toString(){
        String result="";
        DecimalFormat formatter = new DecimalFormat("#0.00");

        result +="Phone number: " + phoneNumber + "\n";
        result +="Average INCOMING call duration: " + formatter.format(avgDurationI) +" mins\n";
        result +="Average OUTGOING call duration: " + formatter.format(avgDurationO) +" mins\n";
        result +="Total INCOMING call duration: " + formatter.format(totalDurationI) +" mins\n";
        result +="Total OUTGOING call duration: " + formatter.format(totalDurationO) +" mins\n";

        return result;
    }
}
