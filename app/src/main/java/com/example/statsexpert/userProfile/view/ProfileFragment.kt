package com.example.statsexpert.userProfile.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.statsexpert.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ProfileFragment : Fragment() {

    private lateinit var tvFirstName: TextView
    private lateinit var tvLastName: TextView
    private lateinit var tvEmail: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        tvFirstName = view.findViewById(R.id.tvFirstName)
        tvLastName = view.findViewById(R.id.tvLastName)
        tvEmail = view.findViewById(R.id.tvEmail)

        fetchUserProfile()

        return view
    }

    private fun fetchUserProfile() {
        val user = FirebaseAuth.getInstance().currentUser
        val userId = user?.uid

        // Assuming you have a 'users' collection and the document ID is the user's UID
        userId?.let {
            FirebaseFirestore.getInstance().collection("users").document(it)
                .get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        val firstName = document.getString("firstName") ?: ""
                        val lastName = document.getString("lastName") ?: ""
                        val email = document.getString("email") ?: ""

                        tvFirstName.text = "First Name: $firstName"
                        tvLastName.text = "Last Name: $lastName"
                        tvEmail.text = "Email: $email"
                    }
                }
                .addOnFailureListener { exception ->
                    // Handle any errors
                }
        }
    }
}