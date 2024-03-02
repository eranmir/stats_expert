package com.example.statsexpert.gamescreen.service

import android.content.Context
import com.example.statsexpert.gamescreen.model.Game
import com.google.gson.Gson

class GameCache(private val context: Context) {

    private val sharedPref = context.getSharedPreferences("game_cache", Context.MODE_PRIVATE)
    private val gson = Gson()

    fun cacheGames(games: List<Game>) {
        val editor = sharedPref.edit()
        val gamesJson = gson.toJson(games)
        editor.putString("games", gamesJson)
        editor.apply()
    }

    fun getCachedGames(): List<Game>? {
        val gamesJson = sharedPref.getString("games", null)
        return gson.fromJson(gamesJson, Array<Game>::class.java)?.toList()
    }

    fun clearCache() {
        val editor = sharedPref.edit()
        editor.clear()
        editor.apply()
    }
}
