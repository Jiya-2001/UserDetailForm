package com.example.userdetailsform.repository

import com.example.userdetailsform.dao.UserDao
import com.example.userdetailsform.model.UserEntity

class UserRepo(private val userDao: UserDao) {
    suspend fun insertUser(userDetails: UserEntity) {
        userDao.insert(userDetails)
    }
}