package Entities;

import io.realm.RealmObject;

/**
 * Created by Larisa on 12.12.2017.
 */

public class SMSSample extends RealmObject {
    public String number;

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}
