package com.example.statsexpert.singlegamescreen.view

import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.statsexpert.R
import com.example.statsexpert.singlegamescreen.model.Comment
import com.example.statsexpert.singlegamescreen.view.adapter.CommentsAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.util.UUID

class GamePageActivity : AppCompatActivity() {

    private val PICK_IMAGE_REQUEST = 1
    private lateinit var recyclerViewComments: RecyclerView
    private lateinit var commentsAdapter: CommentsAdapter
    private lateinit var commentsList: MutableList<Comment>
    private lateinit var editTextComment: EditText
    private lateinit var buttonSubmitComment: Button
    private lateinit var textViewHomeTeam: TextView
    private lateinit var textViewHomeScore: TextView
    private lateinit var textViewAwayTeam: TextView
    private lateinit var textViewAwayScore: TextView
    private lateinit var gameId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.game_page)

        val backButton: ImageButton = findViewById(R.id.backButton)
        backButton.setOnClickListener {
            onBackPressed()
        }

        val buttonUploadPicture: Button = findViewById(R.id.buttonUploadPicture)
        buttonUploadPicture.setOnClickListener {
            // Open an image picker dialog or activity
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"
            startActivityForResult(intent, PICK_IMAGE_REQUEST)
        }

        // Initialize views
        recyclerViewComments = findViewById(R.id.recyclerViewComments)
        editTextComment = findViewById(R.id.editTextComment)
        buttonSubmitComment = findViewById(R.id.buttonSubmitComment)
        textViewHomeTeam = findViewById(R.id.textViewHomeTeam)
        textViewHomeScore = findViewById(R.id.textViewHomeScore)
        textViewAwayTeam = findViewById(R.id.textViewAwayTeam)
        textViewAwayScore = findViewById(R.id.textViewAwayScore)

        gameId = intent.getStringExtra("game_id")!!
        val homeTeam = intent.getStringExtra("home_team")
        val awayTeam = intent.getStringExtra("away_team")
        val homeScore = intent.getStringExtra("home_score")
        val awayScore = intent.getStringExtra("away_score")

        // Initialize RecyclerView for comments
        recyclerViewComments.layoutManager = LinearLayoutManager(this)
        commentsList = mutableListOf()
        commentsAdapter = CommentsAdapter(commentsList)
        recyclerViewComments.adapter = commentsAdapter

        // Dummy data for comments (replace with actual data from Firebase)
        gameId.let { loadCommentsForGame(it) }

        // Button click listener for submitting comments
        buttonSubmitComment.setOnClickListener {
            // Add new comment to the list and notify adapter
            val commentContent = editTextComment.text.toString()
            if (commentContent.isNotEmpty()) {
                val currentUser = FirebaseAuth.getInstance().currentUser
                val username = currentUser?.email ?: "Anonymous"
                val comment = Comment(gameId, username, commentContent)
                addComment(comment)
                commentsList.add(comment)
                commentsAdapter.notifyDataSetChanged()
                // Clear the EditText after submitting comment
                editTextComment.text.clear()
            }
        }

        // Set initial team names and scores (replace with actual data)
        textViewHomeTeam.text = homeTeam
        textViewHomeScore.text = homeScore
        textViewAwayTeam.text = awayTeam
        textViewAwayScore.text = awayScore
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            val imageUri = data.data
            // Upload the selected image to Firebase Storage
            if (imageUri != null) {
                uploadImage(imageUri)
            }
        }
    }

    // Function to upload the image to Firebase Storage
    private fun uploadImage(imageUri: Uri) {
        val storageRef =
            FirebaseStorage.getInstance().reference.child("images/${UUID.randomUUID()}")
        val uploadTask = storageRef.putFile(imageUri)
        uploadTask.addOnSuccessListener { taskSnapshot ->
            // Image uploaded successfully, get the download URL
            storageRef.downloadUrl.addOnSuccessListener { uri ->
                // Use the download URL to display or store the image
                val imageUrl = uri.toString()
                // Optionally, save the image URL along with other comment details to Firestore or Realtime Database
                val commentContent = editTextComment.text.toString()
                if (commentContent.isNotEmpty()) {
                    val currentUser = FirebaseAuth.getInstance().currentUser
                    val username = currentUser?.email ?: "Anonymous"
                    val comment = Comment(gameId, username, imageUrl)
                    addComment(comment)
                    commentsList.add(comment)
                    commentsAdapter.notifyDataSetChanged()
                    // Clear the EditText after submitting comment
                    editTextComment.text.clear()
                }
            }.addOnFailureListener { exception ->
                // Handle failure to get download URL
                Log.e(ContentValues.TAG, "Failed to get download URL", exception)
            }
        }.addOnFailureListener { exception ->
            // Handle failure to upload image
            Log.e(ContentValues.TAG, "Failed to upload image", exception)
        }
    }

    private fun addComment(comment: Comment) {
        val db = FirebaseFirestore.getInstance()
        db.collection("comments")
            .add(comment)
            .addOnSuccessListener { documentReference ->
                Log.d(ContentValues.TAG, "Comment added with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.w(ContentValues.TAG, "Error adding comment", e)
            }
    }

    private fun loadCommentsForGame(gameId: String) {
        // Clear the existing comments list
        commentsList.clear()

        // Query Firestore for comments with the specified game ID
        val commentsRef = FirebaseFirestore.getInstance().collection("comments")
            .whereEqualTo("gameId", gameId)

        commentsRef.get()
            .addOnSuccessListener { querySnapshot ->
                // Iterate through the query results and add comments to the list
                for (document in querySnapshot.documents) {
                    val comment = document.toObject(Comment::class.java)
                    if (comment != null) {
                        commentsList.add(comment)
                    }
                }
                // Notify the adapter of the data set change
                commentsAdapter.notifyDataSetChanged()
            }
            .addOnFailureListener { exception ->
                Log.e(ContentValues.TAG, "Error getting comments for game ID $gameId", exception)
            }
    }
}
