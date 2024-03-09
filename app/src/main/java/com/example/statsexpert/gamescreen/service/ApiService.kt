package com.example.statsexpert.gamescreen.service

import android.graphics.Color
import com.example.statsexpert.gamescreen.model.Game
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

class ApiService(private val client: OkHttpClient) {

    @Throws(Exception::class)
    fun getGamesPerDate(date: Date): List<Game> {
        val formattedDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(date)
        val url = "https://www.oddsshark.com/api/scores/nba/$formattedDate?_format=json"
        val request = Request.Builder()
            .url(url)
            .build()

        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) {
                throw Exception("Failed to fetch games for date: $formattedDate")
            }

            val responseBody = response.body?.string()
            val json = JSONObject(responseBody)

            val scoresArray = json.getJSONArray("scores")
            val games = mutableListOf<Game>()

            for (i in 0 until scoresArray.length()) {
                val score = scoresArray.getJSONObject(i)

                val teams = score.getJSONObject("teams")
                val id = score.getString("id")
                val homeTeamColor = teams.getJSONObject("home").getJSONObject("colors").getString("primary")
                val awayTeamColor = teams.getJSONObject("away").getJSONObject("colors").getString("primary")
                val homeTeamName = teams.getJSONObject("home").getJSONObject("names").getString("name")
                val homeScore = teams.getJSONObject("home").getString("score")
                val awayTeamName = teams.getJSONObject("away").getJSONObject("names").getString("name")
                val awayScore = teams.getJSONObject("away").getString("score")

                val game = Game(id, formattedDate, homeTeamName, homeScore, Color.parseColor(homeTeamColor), awayTeamName, awayScore, Color.parseColor(awayTeamColor))
                games.add(game)
            }

            return games
        }
    }
}
