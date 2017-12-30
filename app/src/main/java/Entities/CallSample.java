package Entities;

import io.realm.RealmObject;

/**
 * Created by Larisa on 12.12.2017.
 */

public class CallSample extends RealmObject {

    public String phoneNumber;
    public int type ; // 0 - for OUTGOING ; 1- for INCOMING
    public String date; // format : Thu Jan 05 16:52:46 GMT+02:00 2017
    public String duration; //in seconds

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }


    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }
}
