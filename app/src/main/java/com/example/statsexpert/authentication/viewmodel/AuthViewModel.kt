package com.example.statsexpert.authentication.viewmodel

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
                // Save user to Firestore
                FirebaseFirestore.getInstance().collection("users")
                    .document(user.uid)
                    .set(user).await()
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

                FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password).await()
                _loginStatus.postValue(true)
            } catch (e: Exception) {
                _loginStatus.postValue(false)
                val a = 3
            }
        }
    }
}
