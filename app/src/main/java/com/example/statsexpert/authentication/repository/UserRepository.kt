package com.example.statsexpert.authentication.repository

import com.example.statsexpert.authentication.database.UserDao
import com.example.statsexpert.authentication.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class UserRepository(private val userDao: UserDao) {

    suspend fun insertUser(user: User) = withContext(Dispatchers.IO) {
        userDao.insert(user)
    }

    suspend fun getUserByUid(uid: String): User? = withContext(Dispatchers.IO) {
        userDao.getUserByUid(uid)
    }
}
