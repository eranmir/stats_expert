import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.statsexpert.R
import com.example.statsexpert.gamescreen.model.Game
import com.example.statsexpert.singlegamescreen.view.GamePageActivity

class GamesAdapter(private val context: Context, private val games: List<Game>) :
    RecyclerView.Adapter<GamesAdapter.GameViewHolder>() {

    inner class GameViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val dateTextView: TextView = itemView.findViewById(R.id.dateTextView)
        val homeTeamTextView: TextView = itemView.findViewById(R.id.homeTeamTextView)
        val homeScoreTextView: TextView = itemView.findViewById(R.id.homeScoreTextView)
        val homeColorView: View = itemView.findViewById(R.id.homeColorView)
        val awayTeamTextView: TextView = itemView.findViewById(R.id.awayTeamTextView)
        val awayScoreTextView: TextView = itemView.findViewById(R.id.awayScoreTextView)
        val awayColorView: View = itemView.findViewById(R.id.awayColorView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_game, parent, false)
        return GameViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: GameViewHolder, position: Int) {
        val currentGame = games[position]

        // Bind game details to views
        holder.dateTextView.text = currentGame.date
        holder.homeTeamTextView.text = currentGame.homeTeamName
        holder.homeScoreTextView.text = currentGame.homeScore.toString()
        holder.homeColorView.setBackgroundColor(currentGame.homeColor)
        holder.awayTeamTextView.text = currentGame.awayTeamName
        holder.awayScoreTextView.text = currentGame.awayScore.toString()
        holder.awayColorView.setBackgroundColor(currentGame.awayColor)

        // Set click listener for the game item
        holder.itemView.setOnClickListener {
            // Create an intent to start the GamePageActivity
            val intent = Intent(context, GamePageActivity::class.java).apply {
                // Pass the game data as extras
                //putExtra("game_date", currentGame.date)
                putExtra("home_team", currentGame.homeTeamName)
                putExtra("away_team", currentGame.awayTeamName)
                putExtra("home_score", currentGame.homeScore)
                putExtra("away_score", currentGame.awayScore)
                putExtra("game_id", currentGame.id)
                // Add more data as needed
            }
            // Start the activity with the intent
            context.startActivity(intent)
        }
    }

    override fun getItemCount() = games.size
}
