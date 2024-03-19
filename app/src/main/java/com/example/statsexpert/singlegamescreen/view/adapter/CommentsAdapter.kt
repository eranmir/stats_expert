package com.example.statsexpert.singlegamescreen.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.statsexpert.R
import com.example.statsexpert.singlegamescreen.model.Comment
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

open class CommentsAdapter(var commentsList: MutableList<Comment>) :
    RecyclerView.Adapter<CommentsAdapter.CommentViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_comment, parent, false)
        return CommentViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        val currentItem = commentsList[position]
        holder.textViewUsername.text = currentItem.user
        holder.textViewContent.text = currentItem.content // Assuming 'commentText' is the correct field for the comment's text

        // Load the image using Picasso if imageUrl is available
        // Assuming 'imageUrl' is a field in your Comment model that holds the image URL.
        currentItem.content.let { url ->
            if (url.isNotBlank()) {
                holder.imageViewComment.visibility = View.VISIBLE
                Picasso.get().load(url).into(holder.imageViewComment)
            } else {
                holder.imageViewComment.visibility = View.GONE
            }
        } ?: run {
            holder.imageViewComment.visibility = View.GONE
        }
    }

    override fun getItemCount() = commentsList.size

    class CommentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewUsername: TextView = itemView.findViewById(R.id.userTextView)
        val textViewContent: TextView = itemView.findViewById(R.id.contentTextView)
        val imageViewComment: CircleImageView = itemView.findViewById(R.id.imageViewComment)
    }

    fun addComment(comment: Comment) {
        commentsList.add(comment)
        notifyItemInserted(commentsList.size - 1)
    }

    fun setComments(comments: List<Comment>) {
        commentsList = comments.toMutableList()
        notifyDataSetChanged()
    }
}
