package Helper;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import Entities.CallSample;
import Entities.SMSSample;

public class Utils {

    public static String millisToDate(long currentTime) {
        String finalDate;
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(currentTime);
        Date date = calendar.getTime();
        finalDate = date.toString();
        return finalDate;
    }

    public static int checkIfObjExists(List<SMSSample> list, SMSSample sample){ // check if the number is already in the list //todo: change this (duplicate code)
        int i;
        SMSSample aux;

        for(i=0;i<list.size();i++){
            aux = list.get(i);
            if(aux.equals(sample))
                return i;
        }
        return -1;
    }

    public static int checkIfNumberExists(List<CallSample> list, CallSample sample){ // check if the number is already in the list //todo: change this duplicate code
        int i;
        CallSample aux;

        for(i=0;i<list.size();i++){
            aux = list.get(i);
            if(aux.equals(sample))
                return i;
        }
        return -1;
    }

    public static List<SMSSample> checkForZeroMessages(List<SMSSample> list){//todo: duplicate code. change it
        List<SMSSample> newList ;
        newList = list;
        for(int i=0;i<list.size();i++){
            SMSSample temp = list.get(i);
            if(temp.smsBody.size()==0) {
                newList.remove(i);
                i--;
            }
        }

        return newList;
    }


    public static List<CallSample> checkForZeroCall(List<CallSample> list){ //todo: duplicate code. change it
        List<CallSample> newList ;
        newList = list;
        for(int i=0;i<list.size();i++){
            CallSample temp = list.get(i);
            if(temp.incomingCallDurations.size() == 0 && temp.outgoingCallDurations.size() == 0) {
                newList.remove(i);
                i--;
            }
        }

        return newList;
    }

    public static String getTimestamp (){
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            return dateFormat.format(new Date()); // Find todays date
        } catch (Exception e) {
            e.printStackTrace();

            return null;
        }
    }

}
