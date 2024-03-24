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
import androidx.navigation.findNavController
import com.example.statsexpert.MyApp
import com.example.statsexpert.R
import com.example.statsexpert.authentication.viewmodel.AuthViewModel
import com.example.statsexpert.authentication.viewmodel.AuthViewModelFactory

class LoginFragment : Fragment() {

    private val authViewModel: AuthViewModel by viewModels {
        AuthViewModelFactory((activity?.application as MyApp).userRepository)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_login, container, false)

        val emailEditText: EditText = view.findViewById(R.id.email)
        val passwordEditText: EditText = view.findViewById(R.id.password)
        val loginButton: Button = view.findViewById(R.id.loginButton)

        loginButton.setOnClickListener {
            val email = emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()
            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(context, "Email and password must not be empty.", Toast.LENGTH_SHORT).show()
            } else {
                authViewModel.login(email, password)
            }
        }

        val registerLink = view.findViewById<TextView>(R.id.registerLink)
        registerLink.setOnClickListener {
            view.findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }

        observeViewModel()

        return view
    }

    private fun observeViewModel() {
        authViewModel.loginStatus.observe(viewLifecycleOwner) { isSuccess ->
            if (isSuccess == true) {
                Toast.makeText(context, "Login successful.", Toast.LENGTH_SHORT).show()
                view?.findNavController()?.navigate(R.id.action_loginFragment_to_gamesListFragment)
            } else {
                Toast.makeText(context, "Authentication failed.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
