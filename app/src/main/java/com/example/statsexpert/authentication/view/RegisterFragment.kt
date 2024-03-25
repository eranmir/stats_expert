package com.example.statsexpert.authentication.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.statsexpert.R
import com.example.statsexpert.authentication.database.AppDatabase
import com.example.statsexpert.authentication.repository.UserRepository
import com.example.statsexpert.authentication.viewmodel.AuthViewModel
import com.example.statsexpert.authentication.viewmodel.AuthViewModelFactory

class RegisterFragment : Fragment() {

    // init the model
    private val authViewModel: AuthViewModel by viewModels {
        val userDao = AppDatabase.getDatabase(requireContext()).userDao()
        val userRepository = UserRepository(userDao)
        AuthViewModelFactory(userRepository)
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.fragment_register, container, false)

        val firstNameEditText: EditText = view.findViewById(R.id.editTextFirstName)
        val lastNameEditText: EditText = view.findViewById(R.id.editTextLastName)
        val emailEditText: EditText = view.findViewById(R.id.editTextEmail)
        val passwordEditText: EditText = view.findViewById(R.id.editTextPassword)
        val registerButton: Button = view.findViewById(R.id.buttonRegister)
        val loginLink: TextView = view.findViewById(R.id.loginLink)


        // function that is called when registering
        registerButton.setOnClickListener {
            val firstName = firstNameEditText.text.toString().trim()
            val lastName = lastNameEditText.text.toString().trim()
            val email = emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()

            // make null checks
            if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(context, "All fields are required", Toast.LENGTH_SHORT).show()
            } else {
                authViewModel.register(email, password, firstName, lastName)
            }
        }

        loginLink.setOnClickListener {
            findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
        }

        observeViewModel()

        return view
    }

    private fun observeViewModel() {
        authViewModel.registrationStatus.observe(viewLifecycleOwner) { isSuccess ->
            if (isSuccess == true) {
                Toast.makeText(context, "Registration successful", Toast.LENGTH_SHORT).show()
                findNavController().navigate(R.id.action_registerFragment_to_gamesListFragment)
            } else {
                Toast.makeText(context, "Registration failed", Toast.LENGTH_SHORT).show()
            }
        }

        authViewModel.navigateToGames.observe(viewLifecycleOwner) { shouldNavigate ->
            if (shouldNavigate) {
                findNavController().navigate(R.id.action_registerFragment_to_gamesListFragment)
            }
        }
    }


}
