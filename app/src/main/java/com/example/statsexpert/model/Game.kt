package com.example.statsexpert.model

data class Game(
    val date: String,
    val homeTeamName: String,
    val homeScore: Int,
    val homeColor: Int,
    val awayTeamName: String,
    val awayScore: Int,
    val awayColor: Int
)