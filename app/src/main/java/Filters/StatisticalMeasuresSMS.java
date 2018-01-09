package Filters;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Larisa on 07.01.2018.
 */

public class StatisticalMeasuresSMS extends StatisticalMeasures{

    public static double averageMessageLength(List<String> smsBody){
        int i = 0;
        double mean = 0;
        String aux;

        while(i<smsBody.size()) {
            aux = smsBody.get(i);
            aux = aux.replaceAll("\\s+",""); //removes anything that is a space character (including space, tab characters etc)
            mean += aux.length();
            i++;
        }

        return mean/i;
    }

    public static double averageWordLength(List<String> smsBody){

        int i = 0, numberOfWords = 0;
        double mean = 0;
        String [] wordsArray;
        String sms;

        while(i<smsBody.size()){

            sms = smsBody.get(i);
            wordsArray = sms.split(" ");
            numberOfWords += wordsArray.length;
            mean += sms.replaceAll("\\s+","").length();

            i++;
        }

        return mean/numberOfWords;

    }

    public static double medianWordLength(List <String> smsBody){
        int i = 0,j;
        List<Integer> wordLengths = new ArrayList<Integer>();
        String [] wordsArray ;
        String sms;

        while(i<smsBody.size()){

            sms = smsBody.get(i);
            wordsArray = sms.split(" ");

            j = 0;

            while(j<wordsArray.length) {
                wordLengths.add(wordsArray[j].length());
                j++;
            }
            i++;

        }

        Collections.sort(wordLengths); //sort the array in ascending order

        if(wordLengths.size() % 2 != 0)
            return wordLengths.get(wordLengths.size()/2 );
        else
            if(wordLengths.size()>0)
                return (double)(wordLengths.get((wordLengths.size()/2)-1)+wordLengths.get(wordLengths.size()/2))/2;
            else
                return 0;


    }
}