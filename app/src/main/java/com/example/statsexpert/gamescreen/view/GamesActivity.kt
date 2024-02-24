package com.example.statsexpert.gamescreen.view

import GamesAdapter
import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.statsexpert.R
import com.example.statsexpert.model.Game

class GamesActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var gamesAdapter: GamesAdapter
    private lateinit var games: List<Game>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_games)

        // Initialize RecyclerView
        recyclerView = findViewById(R.id.recyclerViewGames)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Populate dummy data
        games = generateDummyData()

        // Set up adapter
        gamesAdapter = GamesAdapter(games)
        recyclerView.adapter = gamesAdapter
    }

    private fun generateDummyData(): List<Game> {
        // Generate dummy data for the games
        // For simplicity, we're using hardcoded data here
        // In a real app, you would fetch this data from a database or API
        // Example:
        val game1 = Game("2024-02-24", "Team A", 3, Color.RED, "Team B", 2, Color.BLUE)
        val game11 = Game("2024-02-24", "Team AA", 3, Color.RED, "Team BB", 2, Color.BLUE)
        val game2 = Game("2024-02-25", "Team C", 1, Color.GREEN, "Team D", 0, Color.YELLOW)
        val game3 = Game("2024-02-26", "Team E", 2, Color.MAGENTA, "Team F", 2, Color.CYAN)
        val game4 = Game("2024-02-27", "Team G", 4, Color.DKGRAY, "Team H", 1, Color.LTGRAY)

        return listOf(game1,game11, game2, game3, game4);
    }
}
