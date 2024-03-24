package com.example.statsexpert
import android.app.Application
import com.example.statsexpert.authentication.database.AppDatabase
import com.example.statsexpert.authentication.repository.UserRepository

class MyApp : Application() {
    val appDatabase by lazy { AppDatabase.getDatabase(this) }
    val userRepository by lazy { UserRepository(appDatabase.userDao()) }
}
