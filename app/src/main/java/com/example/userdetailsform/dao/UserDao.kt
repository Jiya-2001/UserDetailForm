package com.example.userdetailsform.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.userdetailsform.model.UserEntity

@Dao
interface UserDao {
    @Query("SELECT * FROM user_data")
    fun getAll(): LiveData<List<UserEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(user: UserEntity)
}