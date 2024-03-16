package com.example.statsexpert.gamescreen.service

import android.content.Context
import com.example.statsexpert.gamescreen.model.Game
import com.google.common.reflect.TypeToken
import com.google.gson.Gson
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class GameCache(private val context: Context) {

    private val sharedPref = context.getSharedPreferences("game_cache", Context.MODE_PRIVATE)
    private val gson = Gson()

    fun cacheGames(date: Date, games: List<Game>) {
        // Format the date to a string representation
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val dateString = dateFormat.format(date)

        // Retrieve the cached games map
        val gamesJson = sharedPref.getString("gamesMap", null)
        val gamesMap = (gson.fromJson<Map<String, List<Game>>>(gamesJson, object : TypeToken<Map<String, List<Game>>>() {}.type) ?: mutableMapOf()).toMutableMap()

        // Add or update the games for the specified date in the map
        gamesMap[dateString] = games

        // Save the updated games map to shared preferences
        val editor = sharedPref.edit()
        val gamesMapJson = gson.toJson(gamesMap)
        editor.putString("gamesMap", gamesMapJson)
        editor.apply()
    }

    fun getCachedGames(date: Date): List<Game>? {
        // Format the date to a string representation
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val dateString = dateFormat.format(date)

        // Retrieve the cached games map
        val gamesJson = sharedPref.getString("gamesMap", null)
        val gamesMap = gson.fromJson<Map<String, List<Game>>>(gamesJson, object : TypeToken<Map<String, List<Game>>>() {}.type)

        // Retrieve the games for the specified date from the map
        return gamesMap?.get(dateString)
    }

    fun clearCache() {
        val editor = sharedPref.edit()
        editor.clear()
        editor.apply()
    }
}
