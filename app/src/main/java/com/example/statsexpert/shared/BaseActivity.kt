package com.example.statsexpert.shared

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.statsexpert.authentication.view.LoginActivity
import com.google.firebase.auth.FirebaseAuth

open class BaseActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    fun logout(view: View) {
        // Add your logout logic here, such as clearing session data or navigating to the login screen
        // For example:
        FirebaseAuth.getInstance().signOut()
        // After logging out from Firebase, navigate to the login screen or perform any other necessary actions
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}