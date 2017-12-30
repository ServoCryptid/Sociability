package Database;

import android.util.Log;

import Entities.SMSSample;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by Larisa on 19.12.2017.
 */

public class DatabaseOperator { //Perform basic Create/Read/Update/Delete (CRUD) operations
    private Realm realm;

    public DatabaseOperator(){
        realm = Realm.getDefaultInstance();
    }

    public void addCall(final String pNumber, final int t, final String d,final String duration){
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                // Add a person
                SMSSample call = realm.createObject(SMSSample.class);
                call.setNumber(pNumber);
               // call.setType(t);
               // call.setDate(d);
              //  call.setDuration(duration);

              //  realm.copyToRealmOrUpdate(call);
            }
        });

        RealmResults<SMSSample> results = realm.where(SMSSample.class).findAll();

        Log.d("Size of result set: ",results.toString() );
            realm.close(); // Remember to close Realm when done.

    }



}
