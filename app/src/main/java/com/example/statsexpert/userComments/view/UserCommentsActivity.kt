package com.example.statsexpert.userComments.view

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.statsexpert.R
import com.example.statsexpert.shared.BaseActivity
import com.example.statsexpert.singlegamescreen.model.Comment
import com.example.statsexpert.singlegamescreen.view.adapter.CommentsAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class UserCommentsActivity : BaseActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var commentsAdapter: CommentsAdapter
    private lateinit var userComments: MutableList<Comment>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_comments)

        // Initialize RecyclerView
        recyclerView = findViewById(R.id.recyclerViewComments)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Fetch and display user's comments
        fetchUserComments()
    }

    private fun fetchUserComments() {
        val currentUser = FirebaseAuth.getInstance().currentUser
        val userEmail = currentUser?.email ?: ""

        // Query comments where user email matches currentUserEmail
        val db = FirebaseFirestore.getInstance()
        val commentsRef = db.collection("comments")
        val query = commentsRef.whereEqualTo("user", userEmail)

        query.get()
            .addOnSuccessListener { documents ->
                userComments = mutableListOf()
                for (document in documents) {
                    val comment = document.toObject(Comment::class.java)
                    userComments.add(comment)
                }

                displayUserComments()
            }
            .addOnFailureListener { exception ->
                Log.d(TAG, "Error getting documents: ", exception)
            }
    }

    private fun displayUserComments() {
        commentsAdapter = CommentsAdapter(userComments)
        recyclerView.adapter = commentsAdapter
    }
}
