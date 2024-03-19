package com.example.statsexpert.gamescreen.view

import GamesAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.statsexpert.R
import com.example.statsexpert.gamescreen.model.Game
import com.example.statsexpert.gamescreen.service.ApiService
import com.example.statsexpert.gamescreen.service.GameCache
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import java.text.SimpleDateFormat
import java.util.*

class GamesFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var gamesAdapter: GamesAdapter
    private lateinit var gamesCache: GameCache
    private lateinit var games: List<Game>
    private val client = OkHttpClient()
    private val apiService = ApiService(client)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_games, container, false)

        val dateContainer = view.findViewById<LinearLayout>(R.id.dateContainer)
        recyclerView = view.findViewById(R.id.recyclerViewGames)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        gamesCache = GameCache(requireContext())

        val logoutButton: Button = view.findViewById(R.id.logoutButton)
        logoutButton.setOnClickListener {
            logout(requireView())
        }

        fetchGames(getYesterday())

        // Assuming GamesAdapter is modified to accept a NavController
        gamesAdapter = GamesAdapter(requireContext(), listOf())
        recyclerView.adapter = gamesAdapter

        setupDateBar(dateContainer)
        return view
    }

    private fun setupDateBar(dateContainer: LinearLayout) {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DATE, -1)
        val dateFormat = SimpleDateFormat("MMM dd", Locale.getDefault())

        for (i in 0 until 7) {
            val dateTextView = TextView(requireContext())
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
    fun logout(view: View) {
        FirebaseAuth.getInstance().signOut()
        val navController = Navigation.findNavController(view)
        navController.navigate(R.id.action_gamesFragment_to_loginFragment)
    }

    private fun fetchGames(date: Date) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val cachedGames = gamesCache.getCachedGames(date)
                games = cachedGames?.takeIf { it.isNotEmpty() } ?: apiService.getGamesPerDate(date).also {
                    gamesCache.cacheGames(date, it)
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
        gamesAdapter = GamesAdapter(requireContext(), games)
        recyclerView.adapter = gamesAdapter
    }

    private fun getYesterday(): Date = Calendar.getInstance().apply { add(Calendar.DATE, -1) }.time
}
