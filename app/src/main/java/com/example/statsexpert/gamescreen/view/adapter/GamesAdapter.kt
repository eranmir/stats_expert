import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.statsexpert.R
import com.example.statsexpert.model.Game

class GamesAdapter(private val games: List<Game>) :
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
    }

    override fun getItemCount() = games.size
}
