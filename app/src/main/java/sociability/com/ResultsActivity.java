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
import java.util.List;

import Databases.ROOM.AppDatabase;
import Databases.ROOM.CALL;
import Databases.ROOM.SMS;

public class ResultsActivity extends AppCompatActivity implements View.OnClickListener {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<String> myData = new ArrayList<String>();
    private AppDatabase db;

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
}

class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
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


