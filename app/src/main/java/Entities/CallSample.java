package Entities;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Larisa on 12.12.2017.
 */

public class CallSample  {

    public String phoneNumber;
   // public String type ; // OUTGOING INCOMING
   // public String date; // format : Thu Jan 05 16:52:46 GMT+02:00 2017
    public List<String> callDurations = new ArrayList<String>(); //in seconds

    public CallSample(){}

    public CallSample(String phoneNumber){
        this.phoneNumber = phoneNumber;
    }
    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    //public String getType() {return type;}

  //  public void setType(String type) {this.type = type;}

  //  public String getDate() {return date;}

   // public void setDate(String date) {this.date = date;}

    public List<String> getCallDurations() {
        return callDurations;
    }

    public void setDuration(List<String> duration) {
        this.callDurations = duration;
    }

    public void addCallDuration(String duration ){
        this.callDurations.add(duration);
    }
    @Override
    public boolean equals (Object object){
        if(object !=null && object instanceof CallSample){
            return this.phoneNumber.equals(((CallSample) object).phoneNumber);
        }
        return false;
    }
    //todo: override hashCode?
}
