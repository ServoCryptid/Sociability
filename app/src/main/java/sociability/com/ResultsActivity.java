package sociability.com;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import Databases.ROOM.AppDatabase;
import Databases.ROOM.CALL;
import Databases.ROOM.SMS;

import static sociability.com.MainActivity.fDB;

public class ResultsActivity extends AppCompatActivity implements View.OnClickListener {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<String> myData = new ArrayList<String>();
    private AppDatabase db;
    public static ArrayList<String> long_quiz_scores;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        Button bCall = (Button) findViewById(R.id.statisticsCallButton);
        bCall.setOnClickListener(this);

        Button bSMS = (Button) findViewById(R.id.statisticsSMSButton);
        bSMS.setOnClickListener(this);

        db =  AppDatabase.getAppDatabase(this); //get my ROOM database instance

    }

    @Override
    public void onClick(View v){
        switch(v.getId()){
            case R.id.statisticsCallButton:
                myData.clear();
                //set the recycle view with the call stats from Room DB
                List<CALL> dbCALL;

                dbCALL = db.callDao().getAll();

                for(int i = 0 ; i<dbCALL.size();i++){
                    myData.add(dbCALL.get(i).toString());
                }
                setUpRecycleView();
                break;

            case R.id.statisticsSMSButton:
                myData.clear();
                //set the recycle view with the SMS stats from Room DB

                List<SMS> dbSMS;
                dbSMS = db.smsDao().getAll();

                for(int i = 0 ; i<dbSMS.size();i++){
                    myData.add(dbSMS.get(i).toString());
                }

                setUpRecycleView();
                break;
        }
    }

    private void setUpRecycleView(){

        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        mRecyclerView.setVerticalScrollBarEnabled(true);
        //use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        mAdapter = new MyAdapter(myData);
        mRecyclerView.setAdapter(mAdapter);

    }

    public static String getShortQuizResult(List<Integer> quiz_answers){
        int reversed [] = {7,6,5,4,3,2,1};
        HashMap <String,String> results = new HashMap<String,String>();

        double O=0;
        double C=0;
        double E=0;
        double A=0;
        double N=0;

        O = (quiz_answers.get(4) + reversed[quiz_answers.get(9)-1])/2;
        if(0 <= O && O<=4.30)
            results.put("openness"," Low");
        else
        if(4.31<=O && O <=6.44)
            results.put("openness"," Medium");
        else
        if(6.45<=O && O<=7.00)
            results.put("openness"," High");

        C = (quiz_answers.get(2) + reversed[quiz_answers.get(7)-1])/2;
        if(0 <= C && C<=4.07)
            results.put("conscientiousness "," Low");
        else
        if(4.08<=C && C <=6.71)
            results.put("conscientiousness"," Medium");
        else
        if(6.72<=C && C<=7.00)
            results.put("conscientiousness"," High");

        E = (double)(quiz_answers.get(0) + reversed[quiz_answers.get(5)-1])/2;
        if(0 <= E && E<=2.98)
            results.put("extraversion "," Low");
        else
        if(2.99<=E && E <=5.88)
            results.put("extraversion"," Medium");
        else
        if(5.89<=E && E<=7.00)
            results.put("extraversion"," High");

        A = (quiz_answers.get(6) + reversed[quiz_answers.get(1)-1])/2;
        if(0 <= A && A<=4.11)
            results.put("agreeableness "," Low");
        else
        if(4.12<=A && A <=6.33)
            results.put("agreeableness "," Medium");
        else
        if(6.34<=A && A<=7.00)
            results.put("agreeableness "," High");

        N = (quiz_answers.get(3) + reversed[quiz_answers.get(8)-1])/2;
        if(0 <= N && N<=3.40)
            results.put("neuroticism "," Low");
        else
        if(3.41<=N && N <=6.24)
            results.put("neuroticism "," Medium");
        else
        if(6.25<=N && N<=7.00)
            results.put("neuroticism "," High");


        //update the DB
        fDB.updateResponsesToDB_shortQuiz(results);

        return results.toString();
    }

    public static String getLongQuizResult(List<Integer> quiz_answers){ //TODO: refactor this
        HashMap<String,String> results = new HashMap<String,String>();
        int O=0, O_max=0;
        int C=0, C_max=0;
        int E=0, E_max=0;
        int A=0, A_max=0;
        int N=0, N_max=0;

        for(int i=1;i<=50;i++){
            switch(i%5){
                case 1:
                    if(long_quiz_scores.get(i).charAt(1)=='+') // the model in score_points +/-
                        O += quiz_answers.get(i-1);
                    else
                        O += 6 - quiz_answers.get(i-1); //it's inverted see: http://ipip.ori.org/newScoringInstructions.htm

                    O_max+= quiz_answers.get(i-1);
                    break;
                case 2:
                    if(long_quiz_scores.get(i).charAt(1)=='+') // the model in score_points +/-
                        C += quiz_answers.get(i-1);
                    else
                        C += 6 - quiz_answers.get(i-1); //it's inverted see: http://ipip.ori.org/newScoringInstructions.htm

                    C_max += quiz_answers.get(i-1);
                    break;
                case 3:
                    if(long_quiz_scores.get(i).charAt(1)=='+') // the model in score_points +/-
                        E += quiz_answers.get(i-1);
                    else
                        E += 6 - quiz_answers.get(i-1); //it's inverted see: http://ipip.ori.org/newScoringInstructions.htm

                    E_max+= quiz_answers.get(i-1);
                    break;
                case 4:
                    if(long_quiz_scores.get(i).charAt(1)=='+') // the model in score_points +/-
                        A += quiz_answers.get(i-1);
                    else
                        A += 6 - quiz_answers.get(i-1); //it's inverted see: http://ipip.ori.org/newScoringInstructions.htm

                    A_max+= quiz_answers.get(i-1);
                    break;
                case 0:
                    if(long_quiz_scores.get(i).charAt(1)=='+') // the model in score_points +/-
                        N += quiz_answers.get(i-1);
                    else
                        N += 6 - quiz_answers.get(i-1); //it's inverted see: http://ipip.ori.org/newScoringInstructions.htm

                    N_max+= quiz_answers.get(i-1);
                    break;
            }
        }
        results.put("Openness ", O + "/ "+ O_max );

        results.put("Conscientiousness " , C +"/ "+ C_max );

        results.put("Extraversion " , E +"/ "+ E_max );

        results.put("Agreeableness ", A +"/ "+ A_max);

        results.put("Neuroticism ", N +"/ "+ N_max);

        fDB.updateResponsesToDB_longQuiz(results);

        return results.toString();
    }
}

class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> { //TODO : delete this. you don't need it
    private ArrayList<String> mDataset;

    // Provide a reference to the views for each data item
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView mTextView;

        public ViewHolder(View v) {
            super(v);
            mTextView = (TextView) v.findViewById(R.id.item);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public MyAdapter(ArrayList<String> myDataset) {
        mDataset = myDataset;
    }
    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        // create a new view
        View v = (View) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.my_text_view_for_results, parent, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element

        holder.mTextView.setText(mDataset.get(position));

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }

}


