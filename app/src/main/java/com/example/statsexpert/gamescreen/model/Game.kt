package com.example.statsexpert.gamescreen.model

data class Game(
    val id: Int,
    val date: String,
    val homeTeamName: String,
    val homeScore: String,
    val homeColor: Int,
    val awayTeamName: String,
    val awayScore: String,
    val awayColor: Int
)