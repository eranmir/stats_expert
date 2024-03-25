package com.example.statsexpert.authentication.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.statsexpert.authentication.model.User
import com.example.statsexpert.authentication.repository.UserRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class AuthViewModel(private val userRepository: UserRepository): ViewModel() {

    private val _currentUser = MutableLiveData<User?>()
    val currentUser: LiveData<User?> = _currentUser


    private val _registrationStatus = MutableLiveData<Boolean?>()
    val registrationStatus: LiveData<Boolean?> = _registrationStatus

    private val _loginStatus = MutableLiveData<Boolean?>()
    val loginStatus: LiveData<Boolean?> = _loginStatus

    private val _navigateToGames = MutableLiveData<Boolean>()
    val navigateToGames: LiveData<Boolean> = _navigateToGames

    fun register(email: String, password: String, firstname: String, lastname: String) {
        viewModelScope.launch {
            try {
                val authResult = FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password).await()
                val user = User(
                    uid = authResult.user?.uid ?: "",
                    firstName = firstname,
                    lastName = lastname,
                    email = email
                )


                // Save user to db
                FirebaseFirestore.getInstance().collection("users")
                    .document(user.uid)
                    .set(user).await()

                // Save user to room
                userRepository.insertUser(user)
                _registrationStatus.postValue(true)
            } catch (e: Exception) {
                _registrationStatus.postValue(false)
            }
        }
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            try {
                val authResult = FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password).await()
                val uid = authResult.user?.uid ?: throw IllegalStateException("Authentication failed, user not found.")

                val userSnapshot = FirebaseFirestore.getInstance().collection("users").document(uid).get().await()
                val user = userSnapshot.toObject(User::class.java) ?: throw IllegalStateException("User details not found in Firestore.")

                val existing = userRepository.getUserByUid(uid)

                //check if exists in room
                if (existing === null) {
                    userRepository.insertUser(user)
                }

                _loginStatus.postValue(true)
            } catch (e: Exception) {
                Log.e("LoginError", "Error during login process", e)
                _loginStatus.postValue(false)
            }
        }
    }

    fun updateUser(user: User) {
        viewModelScope.launch {
            userRepository.updateUser(user)
            FirebaseFirestore.getInstance().collection("users")
                .document(user.uid)
                .set(user)
                .await()
        }
    }

    fun getCurrentUser() {
        viewModelScope.launch {
            val firebaseUser = FirebaseAuth.getInstance().currentUser
            firebaseUser?.uid?.let { uid ->
                val user = userRepository.getUserByUid(uid)
                _currentUser.postValue(user)
            }
        }
    }
}
