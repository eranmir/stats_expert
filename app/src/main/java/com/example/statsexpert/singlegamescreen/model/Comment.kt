package com.example.statsexpert.singlegamescreen.model

import com.google.firebase.firestore.PropertyName

data class Comment(
    @get:PropertyName("gameId")
    @set:PropertyName("gameId")
    var gameId: String = "",
    @get:PropertyName("user")
    @set:PropertyName("user")
    var user: String = "",
    @get:PropertyName("content")
    @set:PropertyName("content")
    var content: String = ""
) {
    // Default no-argument constructor required by Firestore
    constructor() : this("", "", "")
}