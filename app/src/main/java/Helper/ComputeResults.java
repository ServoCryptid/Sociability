package Helper;

import android.support.annotation.NonNull;

import com.ultramegasoft.radarchart.RadarHolder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import Databases.ROOM.AppDatabase;
import sociability.com.FirstScreenActivity;

import static sociability.com.RadarChartActivity.fDB;
import static sociability.com.RadarChartActivity.mRadarView;


public class ComputeResults {
    private AppDatabase db;
    public static ArrayList<String> long_quiz_scores;

       // db =  AppDatabase.getAppDatabase(this); //get my ROOM database instance

    public static String getShortQuizResult(List<Integer> quiz_answers){
        int reversed [] = {7,6,5,4,3,2,1};
        HashMap <String,String> results = new HashMap<String,String>();

        double O=0;
        double C=0;
        double E=0;
        double A=0;
        double N=0;

        O = (quiz_answers.get(4) + reversed[quiz_answers.get(9)-1])/2;
        if(0 <= O && O<=4.30) {
            results.put("openness", " Low");
            O = 1;
        }
        else
            if(4.31<=O && O <=6.44) {
                results.put("openness", " Medium");
                O = 5;
            }
        else
            if(6.45<=O && O<=7.00) {
                results.put("openness", " High");
                O = 10;
            }

        C = (quiz_answers.get(2) + reversed[quiz_answers.get(7)-1])/2;
        if(0 <= C && C<=4.07) {
            results.put("conscientiousness ", " Low");
            C = 1;
        }
        else
            if(4.08<=C && C <=6.71) {
                results.put("conscientiousness", " Medium");
                C = 5;
            }
        else
            if(6.72<=C && C<=7.00) {
                results.put("conscientiousness", " High");
                C = 10;
            }

        E = (double)(quiz_answers.get(0) + reversed[quiz_answers.get(5)-1])/2;
        if(0 <= E && E<=2.98) {
            results.put("extraversion ", " Low");
            E = 1;
        }
        else
             if(2.99<=E && E <=5.88) {
                 results.put("extraversion", " Medium");
                 E = 5;
            }
        else
            if(5.89<=E && E<=7.00) {
                results.put("extraversion", " High");
                E = 10;
            }

        A = (quiz_answers.get(6) + reversed[quiz_answers.get(1)-1])/2;
        if(0 <= A && A<=4.11) {
            results.put("agreeableness ", " Low");
            A = 1;
        }
        else
            if(4.12<=A && A <=6.33) {
                results.put("agreeableness ", " Medium");
                A = 5;
            }
        else
            if(6.34<=A && A<=7.00) {
                results.put("agreeableness ", " High");
                A = 10;
            }

        N = (quiz_answers.get(3) + reversed[quiz_answers.get(8)-1])/2;
        if(0 <= N && N<=3.40) {
            results.put("neuroticism ", " Low");
            N = 1;
        }
        else
             if(3.41<=N && N <=6.24) {
                 results.put("neuroticism ", " Medium");
                 N = 5;
             }
        else
             if(6.25<=N && N<=7.00) {
                 results.put("neuroticism ", " High");
                 N = 10;
             }

        final int O_radar = (int)O,C_radar = (int)C, E_radar = (int)E, A_radar = (int)A, N_radar = (int)N;

        FirstScreenActivity.Oshort = O_radar;
        FirstScreenActivity.Cshort = C_radar;
        FirstScreenActivity.Eshort = E_radar;
        FirstScreenActivity.Ashort = A_radar;
        FirstScreenActivity.Nshort = N_radar;

        FirstScreenActivity.prefsUser.edit().putInt("Oshort", O_radar).apply();
        FirstScreenActivity.prefsUser.edit().putInt("Cshort", C_radar).apply();
        FirstScreenActivity.prefsUser.edit().putInt("Eshort", E_radar).apply();
        FirstScreenActivity.prefsUser.edit().putInt("Ashort", A_radar).apply();
        FirstScreenActivity.prefsUser.edit().putInt("Nshort", N_radar).apply();

        @NonNull
        ArrayList<RadarHolder> mData2 = new ArrayList<RadarHolder>() {// The data for the RadarView
            {
                add(new RadarHolder("O", O_radar));
                add(new RadarHolder("C", C_radar));
                add(new RadarHolder("E", E_radar));
                add(new RadarHolder("A", A_radar));
                add(new RadarHolder("N", N_radar));
            }
        };
        mRadarView.setData(mData2);
        //update the DB
        fDB.updateResponsesToDB_shortQuiz(results);

        return results.toString();
    }

    public static String getLongQuizResult(List<Integer> quiz_answers){ //TODO: refactor this
        HashMap<String,String> results = new HashMap<String,String>();
        int O=0, O_max = 50;
        int C=0, C_max = 50;
        int E=0, E_max = 50;
        int A=0, A_max = 50;
        int N=0, N_max = 50;

        for(int i=1;i<=50;i++){
            switch(i%5){
                case 1:
                    if(long_quiz_scores.get(i).charAt(1)=='+') // the model in score_points +/-
                        O += quiz_answers.get(i-1);
                    else
                        O += 6 - quiz_answers.get(i-1); //it's inverted see: http://ipip.ori.org/newScoringInstructions.htm

                    break;
                case 2:
                    if(long_quiz_scores.get(i).charAt(1)=='+') // the model in score_points +/-
                        C += quiz_answers.get(i-1);
                    else
                        C += 6 - quiz_answers.get(i-1); //it's inverted see: http://ipip.ori.org/newScoringInstructions.htm

                    break;
                case 3:
                    if(long_quiz_scores.get(i).charAt(1)=='+') // the model in score_points +/-
                        E += quiz_answers.get(i-1);
                    else
                        E += 6 - quiz_answers.get(i-1); //it's inverted see: http://ipip.ori.org/newScoringInstructions.htm

                    break;
                case 4:
                    if(long_quiz_scores.get(i).charAt(1)=='+') // the model in score_points +/-
                        A += quiz_answers.get(i-1);
                    else
                        A += 6 - quiz_answers.get(i-1); //it's inverted see: http://ipip.ori.org/newScoringInstructions.htm

                    break;
                case 0:
                    if(long_quiz_scores.get(i).charAt(1)=='+') // the model in score_points +/-
                        N += quiz_answers.get(i-1);
                    else
                        N += 6 - quiz_answers.get(i-1); //it's inverted see: http://ipip.ori.org/newScoringInstructions.htm

                    break;
            }
        }

        results.put("Openness ", O + "/ "+ O_max );

        results.put("Conscientiousness " , C +"/ "+ C_max );

        results.put("Extraversion " , E +"/ "+ E_max );

        results.put("Agreeableness ", A +"/ "+ A_max);

        results.put("Neuroticism ", N +"/ "+ N_max);

        final int valO = (int)(O*0.2), valC = (int)(C*0.2), valE = (int)(E*0.2), valA = (int)(A*0.2), valN = (int)(N*0.2);

        FirstScreenActivity.Olong = valO;
        FirstScreenActivity.Clong = valC;
        FirstScreenActivity.Elong = valE;
        FirstScreenActivity.Along = valA;
        FirstScreenActivity.Nlong = valN;

        FirstScreenActivity.prefsUser.edit().putInt("Olong", valO).apply();
        FirstScreenActivity.prefsUser.edit().putInt("Clong", valC).apply();
        FirstScreenActivity.prefsUser.edit().putInt("Elong", valE).apply();
        FirstScreenActivity.prefsUser.edit().putInt("Along", valA).apply();
        FirstScreenActivity.prefsUser.edit().putInt("Nlong", valN).apply();

        @NonNull
        ArrayList<RadarHolder> mData2 = new ArrayList<RadarHolder>() {// The data for the RadarView
            {
                add(new RadarHolder("O", valO));
                add(new RadarHolder("C", valC));
                add(new RadarHolder("E", valE));
                add(new RadarHolder("A", valA));
                add(new RadarHolder("N", valN));
            }
        };
        mRadarView.setData(mData2);
        fDB.updateResponsesToDB_longQuiz(results);

        return results.toString();
    }

    public static void getPersonalQuizResult(List<String> quiz_answers) {
        HashMap<String,String> hash = new HashMap<String,String>();
        //transform array to hashmap
        for(int i = 0; i < quiz_answers.size(); i++)
            hash.put((i+1)+"",quiz_answers.get(i));

        fDB.updateResponsesToDB_personalQuiz(hash);

    }
}


