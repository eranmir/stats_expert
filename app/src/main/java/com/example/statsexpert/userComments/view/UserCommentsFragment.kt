package com.example.statsexpert.userComments.view

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.statsexpert.R
import com.example.statsexpert.singlegamescreen.model.Comment
import com.example.statsexpert.singlegamescreen.view.adapter.CommentsAdapter
import com.example.statsexpert.userComments.view.adapter.UserCommentsAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class UserCommentsFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var commentsAdapter: UserCommentsAdapter
    private lateinit var textViewUserEmail: TextView
    private var userComments: MutableList<Comment> = mutableListOf()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_user_comments, container, false)

        textViewUserEmail = view.findViewById(R.id.textViewUserEmail)
        recyclerView = view.findViewById(R.id.recyclerViewComments)
        recyclerView.layoutManager = LinearLayoutManager(context)

        val currentUser = FirebaseAuth.getInstance().currentUser
        textViewUserEmail.text = currentUser?.email ?: "User Email"

        fetchUserComments()

        return view
    }

    private fun fetchUserComments() {
        val currentUser = FirebaseAuth.getInstance().currentUser
        val userEmail = currentUser?.email ?: ""

        val db = FirebaseFirestore.getInstance()
        val commentsRef = db.collection("comments").whereEqualTo("user", userEmail)

        commentsRef.get().addOnSuccessListener { documents ->
            userComments.clear()
            for (document in documents) {
                document.toObject(Comment::class.java)?.let { comment ->
                    userComments.add(comment)
                }
            }
            displayUserComments()
        }.addOnFailureListener { exception ->
            Log.d(TAG, "Error getting documents: ", exception)
        }
    }

    private fun displayUserComments() {
        if (!::commentsAdapter.isInitialized) {
            commentsAdapter = UserCommentsAdapter(userComments)
            recyclerView.adapter = commentsAdapter
        } else {
            commentsAdapter.notifyDataSetChanged()
        }
    }
}
