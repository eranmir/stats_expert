package com.example.statsexpert.gamescreen.view

import GamesAdapter
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.statsexpert.R
import com.example.statsexpert.authentication.view.LoginActivity
import com.example.statsexpert.gamescreen.model.Game
import com.example.statsexpert.gamescreen.service.ApiService
import com.example.statsexpert.gamescreen.service.GameCache
import com.example.statsexpert.navigation.DatePickerFragment
import com.example.statsexpert.shared.BaseActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.*
import okhttp3.OkHttpClient
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class GamesActivity : BaseActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var gamesAdapter: GamesAdapter
    private lateinit var gamesCache: GameCache
    private lateinit var games: List<Game>
    private val client = OkHttpClient()
    private val apiService = ApiService(client)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gamescreen)
        val dateContainer = findViewById<LinearLayout>(R.id.dateContainer)

        // Initialize RecyclerView
        recyclerView = findViewById(R.id.recyclerViewGames)
        recyclerView.layoutManager = LinearLayoutManager(this)
        gamesCache = GameCache(this)

        // Attempt to load games from cache
        fetchGames(getYesterday())

        // Initialize the adapter
        gamesAdapter = GamesAdapter(this, listOf())

        // Create and register the observer
        val observer = object : RecyclerView.AdapterDataObserver() {
            override fun onChanged() {
                super.onChanged()
                // Handle data set changed if needed
            }
        }
        gamesAdapter.registerAdapterDataObserver(observer)

        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DATE, -1)
        val dateFormat = SimpleDateFormat("MMM dd", Locale.getDefault())

        for (i in 0 until 7) {
            val dateTextView = TextView(this)
            val date = calendar.time // Get the date associated with the TextView
            dateTextView.text = dateFormat.format(date)
            dateTextView.setPadding(16, 8, 16, 8)
            dateTextView.setBackgroundResource(R.drawable.date_border) // Set border drawable
            dateTextView.setOnClickListener { // Set OnClickListener to fetch games for the selected date
                fetchGames(date)
            }
            dateContainer.addView(dateTextView)
            calendar.add(Calendar.DATE, -1)
        }
    }

    private fun fetchGames(date: Date) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val cachedGames = gamesCache.getCachedGames(date)
                if (cachedGames !== null && cachedGames.isNotEmpty()) {
                   games = cachedGames
                } else {
                    games = apiService.getGamesPerDate(date)
                    gamesCache.cacheGames(date ,games)
                }

                withContext(Dispatchers.Main) {
                    displayGames(games)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun displayGames(games: List<Game>) {
        gamesAdapter = GamesAdapter(this, games)
        recyclerView.adapter = gamesAdapter
    }


    private fun getYesterday(): Date {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DATE, -1)
        return calendar.time
    }
}
