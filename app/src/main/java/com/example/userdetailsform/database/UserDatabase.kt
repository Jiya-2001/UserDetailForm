package com.example.userdetailsform.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.userdetailsform.dao.UserDao
import com.example.userdetailsform.model.UserEntity

@Database(entities = [UserEntity::class], version = 1, exportSchema = false)
abstract class UserDatabase : RoomDatabase(){
    abstract fun userDao(): UserDao

    companion object{
        @Volatile
        private var INSTANCE: UserDatabase?=null
        private const val DATABASE_NAME="user_database"

        @Synchronized
        fun getDatabase(context: Context): UserDatabase{
            if(INSTANCE==null){
                INSTANCE = Room.databaseBuilder(
                    context.applicationContext,
                    UserDatabase::class.java,
                    DATABASE_NAME
                ).fallbackToDestructiveMigration().build()
            }
            return INSTANCE as UserDatabase
        }
    }
}