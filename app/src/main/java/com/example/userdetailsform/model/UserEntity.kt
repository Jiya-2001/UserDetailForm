package com.example.userdetailsform.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_data")
data class UserEntity(
    @PrimaryKey(autoGenerate = true) val userId: Int=0,
    @ColumnInfo val name: String?,
    @ColumnInfo val age: String?,
    @ColumnInfo val dob: String?,
    @ColumnInfo val address: String?
)