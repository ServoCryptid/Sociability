package Filters;

import java.util.List;

/**
 * Created by Larisa on 08.01.2018.
 */

public class StatisticalMeasuresCall extends StatisticalMeasures{

    public static  double averageDuration(List<String> callDurations){ // the call duration is returned in hours
        double totalDuration = 0;
        String temp;

        for(int i = 0;i<callDurations.size();i++){
            temp = callDurations.get(i);
            totalDuration += Integer.parseInt(temp);
        }

        return totalDuration/(callDurations.size());
    }

    public static double totalDuration(List<String> callDurations){ // the call duration is returned in hours
        double totalDuration = 0;
        String temp;

        for(int i = 0;i<callDurations.size();i++){
            temp = callDurations.get(i);
            totalDuration += Integer.parseInt(temp);
        }

        return totalDuration/3600;
    }
}
