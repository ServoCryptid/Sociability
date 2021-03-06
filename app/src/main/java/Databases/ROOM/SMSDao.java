package Databases.ROOM;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

/**
 * Created by Larisa on 09.01.2018.
 */
@Dao
public interface SMSDao {
    @Query("SELECT * FROM SMS")
    List<SMS> getAll();

    @Query("SELECT * FROM SMS where phoneNumber LIKE  :phoneNumber ")
    SMS findByPhoneNumber(String phoneNumber);

    @Query("SELECT COUNT(*) from SMS")
    int countPhoneNumbers();

    @Query("SELECT * FROM sms")
    SMS getSMSMetrics();

    @Insert
    void insertAll(SMS... sms);

    @Query("UPDATE SMS SET sms_with_unique_ID_inbox = :uniqueIDinbox, average_length_sms_inbox = :avgSMSlengthInbox ")
    void updateAvgSMSLengthI(int uniqueIDinbox, double avgSMSlengthInbox);

    @Query("UPDATE SMS SET sms_with_unique_ID_inbox = :uniqueIDinbox, sms_inbox = :smsInbox ")
    void updateSMSI(int uniqueIDinbox, double smsInbox);

    @Query("UPDATE SMS SET sms_with_unique_ID_inbox = :uniqueIDinbox, most_received_sms = :daytime ")
    void updateMostReceived(int uniqueIDinbox, String daytime);

    @Delete
    void delete(SMS sms);
}
