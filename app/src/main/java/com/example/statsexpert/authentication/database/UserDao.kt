package com.example.statsexpert.authentication.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.statsexpert.authentication.model.User

@Dao
interface UserDao {
    @Insert
    suspend fun insert(user: User)

    @Query("SELECT * FROM user WHERE uid = :uid")
    suspend fun getUserByUid(uid: String): User?
}

