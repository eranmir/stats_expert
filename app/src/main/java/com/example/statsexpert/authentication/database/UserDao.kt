package com.example.statsexpert.authentication.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.statsexpert.authentication.model.User

@Dao
interface UserDao {
    //insert new user
    @Insert
    suspend fun insert(user: User)

    //get the
    @Query("SELECT * FROM user WHERE uid = :uid")
    suspend fun getUserByUid(uid: String): User?

    //update the user
    @Update
    suspend fun updateUser(user: User)
}

