package eu.napcode.gonoteit.dao.user;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

@Dao
public interface UserDao {

    @Query("SELECT * FROM " + UserEntity.TABLE_NAME + " LIMIT 1")
    LiveData <UserEntity> getUserEntityLiveData();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertUser(UserEntity userEntity);

    @Query("DELETE FROM " + UserEntity.TABLE_NAME)
    void deleteAll();

    @Query("SELECT * FROM " + UserEntity.TABLE_NAME + " LIMIT 1")
    UserEntity getUserEntity();
}
