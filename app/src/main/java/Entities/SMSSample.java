package Entities;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Larisa on 12.12.2017.
 */

public class SMSSample {
   // public String dateSent;

    public String phoneNumber;
    public List<String> smsBody = new ArrayList<String>();
  //  public String type ;//1= received, 2=sent

    public SMSSample(String phoneNumber ){
        this.phoneNumber = phoneNumber;
    }
   // public void setDateSent(String dateSent) {this.dateSent = dateSent}

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setSmsBody(List<String> smsBody) {
        this.smsBody = smsBody;
    }

   // public void setType(String type) {this.type = type;}

   // public String getDateSent() {return dateSent;}

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public List<String> getSmsBody() {
        return smsBody;
    }

   // public String getType() {return type;}

    public void addSMS (String sms){
        this.smsBody.add(sms);
    }

    @Override
    public boolean equals (Object object){
        if(object !=null && object instanceof SMSSample){
            return this.phoneNumber.equals(((SMSSample) object).phoneNumber);
        }
        return false;
    }
    //todo: override hashCode?
}
