package Entities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Larisa on 12.12.2017.
 */

public class CallSample  {

    public String phoneNumber;
   // public String date; // format : Thu Jan 05 16:52:46 GMT+02:00 2017
    public List<String> incomingCallDurations = new ArrayList<String>(); //in seconds
    public List<String> outgoingCallDurations = new ArrayList<String>(); //in seconds
    public int missedCalls = 0;

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

  //  public String getDate() {return date;}

   // public void setDate(String date) {this.date = date;}

    public List<String> getCallDurations(String type) {
        if(type.equals("incoming"))
            return incomingCallDurations;

        return outgoingCallDurations;
    }

    public void addCallDuration(String duration, String callType ){
        if(callType.equals("incoming")) {
            this.incomingCallDurations.add(duration);
        }
        else
            if(callType.equals("outgoing")){
                this.outgoingCallDurations.add(duration);
            }
        else
            this.missedCalls ++;
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
