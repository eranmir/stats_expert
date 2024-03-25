package com.example.statsexpert.userProfile.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.statsexpert.R
import com.example.statsexpert.authentication.database.AppDatabase
import com.example.statsexpert.authentication.model.User
import com.example.statsexpert.authentication.repository.UserRepository
import com.example.statsexpert.authentication.viewmodel.AuthViewModel
import com.example.statsexpert.authentication.viewmodel.AuthViewModelFactory
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ProfileFragment : Fragment() {

    private lateinit var tvFirstName: TextView
    private lateinit var tvLastName: TextView
    private lateinit var tvEmail: TextView
    private lateinit var currUser: User

    private val viewModel: AuthViewModel by viewModels {
        val userDao = AppDatabase.getDatabase(requireContext()).userDao()
        val userRepository = UserRepository(userDao)
        AuthViewModelFactory(userRepository)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        tvFirstName = view.findViewById(R.id.tvFirstName)
        tvLastName = view.findViewById(R.id.tvLastName)
        tvEmail = view.findViewById(R.id.tvEmail)

        viewModel.getCurrentUser()

        viewModel.currentUser.observe(viewLifecycleOwner) { user ->

            if (user != null) {
                tvFirstName.text = "First Name: " +  user.firstName
                tvLastName.text = "Last Name: " + user.lastName
                tvEmail.text = "Email: " + user.email
            }

            currUser = user!!
        }

        val saveButton: Button = view.findViewById(R.id.buttonSave)
        val firstNameEditText: EditText = view.findViewById(R.id.editTextFirstName)
        val lastNameEditText: EditText = view.findViewById(R.id.editTextLastName)

        saveButton.setOnClickListener {
            val firstNameInput = firstNameEditText.text.toString().trim()
            val lastNameInput = lastNameEditText.text.toString().trim()

            val firstName = firstNameInput.ifEmpty { currUser.firstName }
            val lastName = lastNameInput.ifEmpty { currUser.lastName }

            val updatedUser = User(currUser.uid, firstName, lastName, currUser.email) // Construct the updated user object

            viewModel.updateUser(updatedUser)

            tvFirstName.text = "First Name: " +  firstName
            tvLastName.text = "Last Name: " + lastName
        }

        return view
    }
}