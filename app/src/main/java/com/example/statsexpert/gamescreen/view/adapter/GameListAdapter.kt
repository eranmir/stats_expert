package com.example.statsexpert.gamescreen.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.statsexpert.R
import com.example.statsexpert.gamescreen.model.Game


class GameListAdapter(private val gameList: List<Game>) :
    RecyclerView.Adapter<GameListAdapter.GameViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_game, parent, false)
        return GameViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: GameViewHolder, position: Int) {
        val currentGame = gameList[position]
        holder.bind(currentGame)
    }

    override fun getItemCount() = gameList.size

    class GameViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val dateTextView: TextView = itemView.findViewById(R.id.dateTextView)
        private val homeTeamTextView: TextView = itemView.findViewById(R.id.homeTeamTextView)
        private val homeScoreTextView: TextView = itemView.findViewById(R.id.homeScoreTextView)
        private val awayTeamTextView: TextView = itemView.findViewById(R.id.awayTeamTextView)
        private val awayScoreTextView: TextView = itemView.findViewById(R.id.awayScoreTextView)

        fun bind(game: Game) {
            dateTextView.text = game.date
            homeTeamTextView.text = game.homeTeamName
            homeScoreTextView.text = game.homeScore.toString()
            awayTeamTextView.text = game.awayTeamName
            awayScoreTextView.text = game.awayScore.toString()
        }
    }
}

