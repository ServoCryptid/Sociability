package Database;

import android.util.Log;

import Entities.CallSample;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by Larisa on 02.01.2018.
 */
public class RealmDB extends DatabaseOperator{
    private Realm realm;
    private String pNumber;
    private String date ;
    private String duration;
    private String type;

    public void setpNumber(String pNumber) {
        this.pNumber = pNumber;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void updateDB(){
        realm = Realm.getDefaultInstance();

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                // Add a person
                CallSample call = realm.createObject(CallSample.class);
                call.setPhoneNumber(pNumber);
                call.setType(type);
                call.setDate(date);
                call.setDuration(duration);

                //realm.copyToRealmOrUpdate(call);

                //Log.d("REAAALMM ",pNumber+"-------"+type+"-----"+date+"------"+duration );
            }
        });

        realm.close(); // Remember to close Realm when done.
    }

    public void fetchdata(){

        RealmResults<CallSample> results = realm.where(CallSample.class).findAll();
        Log.d("DBBBB",results.toString());

    }


}
