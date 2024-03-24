package com.example.statsexpert.authentication.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User(
    @PrimaryKey val uid: String,
    val firstName: String,
    val lastName: String,
    val email: String
)