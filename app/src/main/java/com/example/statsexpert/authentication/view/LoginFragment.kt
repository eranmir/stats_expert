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
import androidx.navigation.findNavController
import com.example.statsexpert.R
import com.google.firebase.auth.FirebaseAuth

class LoginFragment : Fragment() {

    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_login, container, false)

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()

        val emailEditText: EditText = view.findViewById(R.id.email)
        val passwordEditText: EditText = view.findViewById(R.id.password)
        val loginButton: Button = view.findViewById(R.id.loginButton)

        loginButton.setOnClickListener {
            val email = emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(context, "Email and password must not be empty.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            loginUser(email, password)
        }

        val registerLink = view.findViewById<TextView>(R.id.registerLink)
        registerLink.setOnClickListener {
            // Navigate to RegisterFragment using NavController
            view.findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }

        return view
    }

    private fun loginUser(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Toast.makeText(context, "Login successful.", Toast.LENGTH_SHORT).show()
                    // Navigate to the GamesListFragment using NavController
                    view?.findNavController()?.navigate(R.id.action_loginFragment_to_gamesListFragment)
                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(context, "Authentication failed.", Toast.LENGTH_SHORT).show()
                }
            }
    }
}
