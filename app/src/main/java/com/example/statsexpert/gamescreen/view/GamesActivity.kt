package com.example.statsexpert.gamescreen.view

import GamesAdapter
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.statsexpert.R
import com.example.statsexpert.gamescreen.model.Game
import com.example.statsexpert.gamescreen.service.ApiService
import com.example.statsexpert.gamescreen.service.GameCache
import kotlinx.coroutines.*
import okhttp3.OkHttpClient
import java.util.Calendar
import java.util.Date

class GamesActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var gamesAdapter: GamesAdapter
    private lateinit var gamesCache: GameCache
    private lateinit var games: List<Game>
    private val client = OkHttpClient()
    private val apiService = ApiService(client)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gamescreen)
        gamesCache = GameCache(this)

        // Initialize RecyclerView
        recyclerView = findViewById(R.id.recyclerViewGames)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Attempt to load games from cache
        loadGamesFromCache()
    }

    private fun loadGamesFromCache() {
        val cachedGames = gamesCache.getCachedGames()
        if (cachedGames !== null && cachedGames.isNotEmpty()) {
            displayGames(cachedGames)
        } else {
            fetchGames()
        }
    }

    private fun fetchGames() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val currentDate = getCurrentDate()
                games = apiService.getGamesPerDate(currentDate)
                // Save fetched games to cache
                gamesCache.cacheGames(games)
                // Update UI on the main thread
                withContext(Dispatchers.Main) {
                    displayGames(games)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun displayGames(games: List<Game>) {
        gamesAdapter = GamesAdapter(games)
        recyclerView.adapter = gamesAdapter
    }

    private fun getCurrentDate(): Date {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DATE, -1)
        return calendar.time
    }
}
