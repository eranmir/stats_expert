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
import com.example.statsexpert.R
import com.google.firebase.auth.FirebaseAuth
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuthException

class RegisterFragment : Fragment() {

    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_register, container, false)

        auth = FirebaseAuth.getInstance()

        val firstNameEditText: EditText = view.findViewById(R.id.editTextFirstName)
        val lastNameEditText: EditText = view.findViewById(R.id.editTextLastName)
        val emailEditText: EditText = view.findViewById(R.id.editTextEmail)
        val passwordEditText: EditText = view.findViewById(R.id.editTextPassword)
        val registerButton: Button = view.findViewById(R.id.buttonRegister)

        val loginLink = view.findViewById<TextView>(R.id.loginLink)
        loginLink.setOnClickListener {
            // Navigate to LoginFragment using NavController
            findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
        }

        registerButton.setOnClickListener {
            val firstName = firstNameEditText.text.toString().trim()
            val lastName = lastNameEditText.text.toString().trim()
            val email = emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()

            if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(context, "All fields are required", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            registerUser(email, password)
        }

        return view
    }

    private fun registerUser(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    // Registration successful
                    Toast.makeText(context, "Registration successful", Toast.LENGTH_SHORT).show()
                    // Navigate to the next screen or perform another action
                    findNavController().navigate(R.id.action_registerFragment_to_gamesListFragment)
                } else {
                    // Registration failed, handle the error
                    val errorCode = (task.exception as FirebaseAuthException).errorCode
                    Toast.makeText(context, "Registration failed: $errorCode", Toast.LENGTH_SHORT).show()
                }
            }
    }
}
