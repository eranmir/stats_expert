package com.example.statsexpert.singlegamescreen.view

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.statsexpert.R
import com.example.statsexpert.singlegamescreen.model.Comment
import com.example.statsexpert.singlegamescreen.view.adapter.CommentsAdapter

class GamePageActivity : AppCompatActivity() {

    private lateinit var recyclerViewComments: RecyclerView
    private lateinit var commentsAdapter: CommentsAdapter
    private lateinit var commentsList: MutableList<Comment>
    private lateinit var editTextComment: EditText
    private lateinit var buttonSubmitComment: Button
    private lateinit var textViewHomeTeam: TextView
    private lateinit var textViewHomeScore: TextView
    private lateinit var textViewAwayTeam: TextView
    private lateinit var textViewAwayScore: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.game_page)

        // Initialize views
        recyclerViewComments = findViewById(R.id.recyclerViewComments)
        editTextComment = findViewById(R.id.editTextComment)
        buttonSubmitComment = findViewById(R.id.buttonSubmitComment)
        textViewHomeTeam = findViewById(R.id.textViewHomeTeam)
        textViewHomeScore = findViewById(R.id.textViewHomeScore)
        textViewAwayTeam = findViewById(R.id.textViewAwayTeam)
        textViewAwayScore = findViewById(R.id.textViewAwayScore)

        // Initialize RecyclerView for comments
        recyclerViewComments.layoutManager = LinearLayoutManager(this)
        commentsList = mutableListOf()
        commentsAdapter = CommentsAdapter(commentsList)
        recyclerViewComments.adapter = commentsAdapter

        // Dummy data for comments (replace with actual data from Firebase)
        generateDummyComments()

        // Button click listener for submitting comments
        buttonSubmitComment.setOnClickListener {
            // Add new comment to the list and notify adapter
            val commentContent = editTextComment.text.toString()
            if (commentContent.isNotEmpty()) {
                val comment = Comment("User", commentContent)
                commentsList.add(comment)
                commentsAdapter.notifyDataSetChanged()
                // Clear the EditText after submitting comment
                editTextComment.text.clear()
            }
        }

        // Set initial team names and scores (replace with actual data)
        textViewHomeTeam.text = "Home Team"
        textViewHomeScore.text = "0"
        textViewAwayTeam.text = "Away Team"
        textViewAwayScore.text = "0"
    }

    private fun generateDummyComments() {
        // Generate dummy comments for testing
        val comment1 = Comment("User1", "Great match!")
        val comment2 = Comment("User2", "Exciting game!")
        val comment3 = Comment("User3", "Go Team A!")
        commentsList.addAll(listOf(comment1, comment2, comment3))
        commentsAdapter.notifyDataSetChanged()
    }
}
