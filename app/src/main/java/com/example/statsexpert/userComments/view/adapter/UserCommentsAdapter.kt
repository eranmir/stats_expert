package com.example.statsexpert.userComments.view.adapter

import android.view.View
import com.example.statsexpert.singlegamescreen.model.Comment
import com.example.statsexpert.singlegamescreen.view.adapter.CommentsAdapter
import com.squareup.picasso.Picasso

class UserCommentsAdapter(commentsList: MutableList<Comment>) : CommentsAdapter(commentsList) {
    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        val currentItem = commentsList[position]

        holder.textViewUsername.text =
            currentItem.gameName

        // Comment content remains unchanged.
        holder.textViewContent.text = currentItem.content

        // Handling for the image remains unchanged.
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
}
