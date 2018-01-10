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
public interface CallDao {
    @Query("SELECT * FROM call")
    List<CALL> getAll();

    @Query("SELECT * FROM call where phoneNumber LIKE  :phoneNumber ")
    CALL findByPhoneNumber(String phoneNumber);

    @Query("SELECT COUNT(*) from call")
    int countPhoneNumbers();

    @Insert
    void insertAll(CALL... calls);

    @Delete
    void delete(CALL call);
}
